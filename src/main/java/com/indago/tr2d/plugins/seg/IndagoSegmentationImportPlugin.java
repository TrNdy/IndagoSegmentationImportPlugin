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
import com.indago.tr2d.ui.model.Tr2dImportedSegmentationModel;
import com.indago.tr2d.ui.view.Tr2dImportedSegmentationPanel;

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
	private Tr2dImportedSegmentationModel model;

	public static Logger log = IndagoLog.stderrLogger().subLogger(IndagoSegmentationImportPlugin.class.getSimpleName());

	/**
	 * @see com.indago.tr2d.plugins.seg.Tr2dSegmentationPlugin#getInteractionPanel()
	 */
	@Override
	public JPanel getInteractionPanel() {
		return panel;
	}

	/**
	 * @see com.indago.tr2d.plugins.seg.Tr2dSegmentationPlugin#getOutputs()
	 */
	@Override
	public List< RandomAccessibleInterval< IntType > > getOutputs() {
		return model.getSegmentHypothesesImages();
	}

	/**
	 * @see com.indago.tr2d.plugins.seg.Tr2dSegmentationPlugin#setTr2dModel(com.indago.tr2d.ui.model.Tr2dModel)
	 */
	@Override
	public void setProjectFolderAndData( final ProjectFolder projectFolder, final RandomAccessibleInterval< DoubleType > rawData ) {
		this.projectFolder = projectFolder;
		this.model = new Tr2dImportedSegmentationModel( projectFolder, rawData );
		panel = new Tr2dImportedSegmentationPanel( this.model );
		log.info( "Tr2dSegmentationImportPlugin is set up." );
	}

	/**
	 * @see com.indago.tr2d.plugins.seg.Tr2dSegmentationPlugin#getUiName()
	 */
	@Override
	public String getUiName() {
		return "imported segmentations";
	}

	@Override
	public void setLogger(final Logger logger) {
		log = logger;
	}
}
