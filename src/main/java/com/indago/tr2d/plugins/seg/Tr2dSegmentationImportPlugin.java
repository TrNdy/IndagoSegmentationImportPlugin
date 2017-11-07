/**
 *
 */
package com.indago.tr2d.plugins.seg;

import java.util.List;

import javax.swing.JPanel;

import org.scijava.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.indago.tr2d.ui.model.Tr2dImportedSegmentationModel;
import com.indago.tr2d.ui.model.Tr2dModel;
import com.indago.tr2d.ui.view.Tr2dImportedSegmentationPanel;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.integer.IntType;

/**
 * @author jug
 */
@Plugin( type = Tr2dSegmentationPlugin.class, name = "Tr2d Segmentation Importer" )
public class Tr2dSegmentationImportPlugin implements Tr2dSegmentationPlugin {

	JPanel panel = null;

	private Tr2dModel tr2dModel;
	private Tr2dImportedSegmentationModel model;

	public static Logger log = LoggerFactory.getLogger( Tr2dSegmentationImportPlugin.class );

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
	public void setTr2dModel( final Tr2dModel model ) {
		this.tr2dModel = model;
		this.model = new Tr2dImportedSegmentationModel( tr2dModel.getSegmentationModel(), tr2dModel.getSegmentationModel().getProjectFolder() );
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

	/**
	 * @see com.indago.tr2d.plugins.seg.Tr2dSegmentationPlugin#getLogger()
	 */
	@Override
	public Logger getLogger() {
		return log;
	}
}
