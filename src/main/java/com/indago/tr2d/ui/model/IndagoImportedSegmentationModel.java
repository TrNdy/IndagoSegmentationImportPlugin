/**
 *
 */
package com.indago.tr2d.ui.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.ListModel;

import com.indago.io.IntTypeImgLoader;
import com.indago.io.ProjectFile;
import com.indago.io.ProjectFolder;
import com.indago.tr2d.plugins.seg.IndagoSegmentationImportPlugin;
import com.indago.ui.bdv.BdvOwner;
import com.indago.util.ImglibUtil;
import com.jgoodies.common.collect.LinkedListModel;

import bdv.util.BdvHandlePanel;
import bdv.util.BdvSource;
import net.imagej.ImgPlus;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.real.DoubleType;
import weka.gui.ExtensionFileFilter;

/**
 * @author jug
 */
public class IndagoImportedSegmentationModel implements BdvOwner {

	private ProjectFolder projectFolder;
	private final Img< DoubleType > rawData;

	private final Vector< ProjectFile > files;
	private final List< RandomAccessibleInterval< IntType > > imgs;

	private LinkedListModel< ProjectFile > linkedListModel = null;

	private BdvHandlePanel bdvHandlePanel;
	private final List< BdvSource > bdvSources = new ArrayList< >();

	public IndagoImportedSegmentationModel( final ProjectFolder parentFolder, final ImgPlus< DoubleType > rawData ) {
		this.projectFolder = projectFolder;
		this.rawData = rawData;

		try {
			this.projectFolder = parentFolder.addFolder( "imported" );
			this.projectFolder.loadFiles();
		} catch ( final IOException e ) {
			this.projectFolder = null;
			IndagoSegmentationImportPlugin.log.error( "Subfolder for imported segmentation hypotheses could not be created." );
			e.printStackTrace();
		}

		imgs = new ArrayList< >();
		files = new Vector< ProjectFile >( projectFolder.getFiles( new ExtensionFileFilter( "tif", "TIFF Image Stack" ) ) );
		for ( final ProjectFile pf : files ) {
			final RandomAccessibleInterval< IntType > rai = IntTypeImgLoader.loadTiffEnsureType( pf.getFile() );
			imgs.add( rai );
		}
		linkedListModel = new LinkedListModel< >( getLoadedFiles() );
	}

	/**
	 * @return
	 */
	public List< RandomAccessibleInterval< IntType > > getSegmentHypothesesImages() {
		return imgs;
	}

	public < T extends RealType< T > & NativeType< T > > int getRawDataSpatialDimensions() {
		return ImglibUtil.getNumberOfSpatialDimensions( ( ImgPlus< T > ) rawData );
	}

	/**
	 * @return the projectFolder
	 */
	public ProjectFolder getProjectFolder() {
		return projectFolder;
	}

	/**
	 * @return
	 */
	public Vector< ProjectFile > getLoadedFiles() {
		return files;
	}

	/**
	 * Copies the given file into the project folder.
	 *
	 * @param f
	 * @throws IOException
	 */
	public void importSegmentation( final File f ) throws IOException {
		final ProjectFile pf = projectFolder.addFile( f.getName() );
		Files.copy( f.toPath(), pf.getFile().toPath() );

		files.add( pf );
		linkedListModel.add( pf );
		RandomAccessibleInterval< IntType > rai;
		rai = IntTypeImgLoader.loadTiffEnsureType( pf.getFile() );
		imgs.add( rai );
	}

	/**
	 * @param idx
	 */
	public void removeSegmentation( final int idx ) {
		bdvRemove( imgs.get( idx ) );
		imgs.remove( idx );
		final ProjectFile pf = files.remove( idx );
		linkedListModel.remove( pf );

		if ( !pf.getFile().delete() ) {
			IndagoSegmentationImportPlugin.log.error( String.format( "Imported segmentation file %s could not be deleted from project folder.", pf ) );
		}
	}
    public void removeAllSegmentations() {
    		for (final ProjectFile pf : files) {
        		if ( !pf.getFile().delete() ) {
        			IndagoSegmentationImportPlugin.log.error( String.format( "Imported segmentation file %s could not be deleted from project folder.", pf ) );
        		}
        		linkedListModel.remove( pf );
    		}
    		files.clear();
    		imgs.clear();
    		bdvSources.clear();
    }
	/**
	 * @param indices
	 */
	public void removeSegmentations( final int[] indices ) {
		Arrays.sort( indices );
		for ( int i = indices.length - 1; i >= 0; i-- ) {
			removeSegmentation( indices[ i ] );
		}
	}

	/**
	 * @return
	 */
	public ListModel< ProjectFile > getListModel() {
		return linkedListModel;
	}

	/**
	 * @see com.indago.ui.bdv.BdvOwner#bdvSetHandlePanel(BdvHandlePanel)
	 */
	@Override
	public void bdvSetHandlePanel( final BdvHandlePanel bdvHandlePanel ) {
		this.bdvHandlePanel = bdvHandlePanel;
	}

	/**
	 * @see com.indago.ui.bdv.BdvOwner#bdvGetHandlePanel()
	 */
	@Override
	public BdvHandlePanel bdvGetHandlePanel() {
		return bdvHandlePanel;
	}

	/**
	 * @see com.indago.ui.bdv.BdvOwner#bdvGetSources()
	 */
	@Override
	public List< BdvSource > bdvGetSources() {
		return bdvSources;
	}

	/**
	 * @see com.indago.ui.bdv.BdvOwner#bdvGetSourceFor(net.imglib2.RandomAccessibleInterval)
	 */
	@Override
	public < T extends RealType< T > & NativeType< T > > BdvSource bdvGetSourceFor( final RandomAccessibleInterval< T > img ) {
		final int idx = imgs.indexOf( img );
		if ( idx == -1 ) return null;
		return bdvGetSources().get( idx );
	}
}
