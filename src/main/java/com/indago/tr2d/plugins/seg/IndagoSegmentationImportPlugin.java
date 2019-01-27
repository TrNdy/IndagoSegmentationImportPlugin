/**
 *
 */
package com.indago.tr2d.plugins.seg;

import java.util.List;

import javax.swing.JPanel;

import org.scijava.log.Logger;
import org.scijava.plugin.Plugin;

import com.indago.IndagoLog;
import com.indago.io.ProjectFolder;
import com.indago.plugins.seg.IndagoSegmentationPlugin;
import com.indago.tr2d.ui.model.IndagoImportedSegmentationModel;
import com.indago.tr2d.ui.view.IndagoImportedSegmentationPanel;

import net.imagej.ImgPlus;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * @author jug
 */
@Plugin( type = IndagoSegmentationPlugin.class, name = "Indago Segmentation Importer" )
public class IndagoSegmentationImportPlugin implements IndagoSegmentationPlugin {

	JPanel panel = null;

	private ProjectFolder projectFolder;
	private IndagoImportedSegmentationModel model;

	public static Logger log = IndagoLog.log.subLogger( IndagoSegmentationImportPlugin.class.getSimpleName() );

	@Override
	public JPanel getInteractionPanel() {
		return panel;
	}

	@Override
	public List< RandomAccessibleInterval< IntType > > getOutputs() {
		return model.getSegmentHypothesesImages();
	}

	@Override
	public void setProjectFolderAndData( final ProjectFolder projectFolder, final ImgPlus< DoubleType > rawData ) {
		this.projectFolder = projectFolder;
		this.model = new IndagoImportedSegmentationModel( projectFolder, rawData );
		panel = new IndagoImportedSegmentationPanel( this.model );
		log.info( "IndagoSegmentationImportPlugin is set up." );
	}

	@Override
	public String getUiName() {
		return "imported segmentations";
	}

	@Override
	public void setLogger(final Logger logger) {
		log = logger;
	}
}
