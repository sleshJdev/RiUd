package by.slesh.ri.cp.ushkindaria.app.controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import by.slesh.ri.cp.ushkindaria.app.G;
import by.slesh.ri.cp.ushkindaria.app.model.Model;
import by.slesh.ri.cp.ushkindaria.app.view.service.ControlPanelViewInterface;
import by.slesh.ri.cp.ushkindaria.app.view.service.ImageBoxesViewInterface;
import by.slesh.ri.cp.ushkindaria.ipt.DilateMorph;
import by.slesh.ri.cp.ushkindaria.ipt.ErodeMorph;

public class Controller implements ActionListener {

	private class BinPercentsChangeValueListener implements AdjustmentListener {

		@Override
		public void adjustmentValueChanged(AdjustmentEvent arg0) {

			mModel.setPercents(arg0.getValue());
			controlPanelView.updatePercentValue(arg0.getValue());
		}
	}

	private class SegmentThresholChangeValueListener implements AdjustmentListener {

		@Override
		public void adjustmentValueChanged(AdjustmentEvent arg0) {

			mModel.setSegmentThreshold(arg0.getValue());
			controlPanelView.updateSegmentThresholdValue(arg0.getValue());
		}
	}

	private class MouseClickAdapter extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent arg0) {

			Point location = new Point(arg0.getPoint());
			imageBoxesView.getLocationOnImage(location);
			mModel.setStartPointForBugSegmentator(location);
			imageBoxesView.updateTarget(mModel.getTargetImage());
		}
	}

	private ControlPanelViewInterface	controlPanelView;
	private ImageBoxesViewInterface		imageBoxesView;
	private Model						mModel;

	public Controller(ControlPanelViewInterface controlPanelView, ImageBoxesViewInterface imageBoxesView, Model model) {

		this.imageBoxesView = imageBoxesView;
		this.controlPanelView = controlPanelView;
		this.mModel = model;

		model.setPercents(G.INIT_BIN_PERCENT);
		model.setSegmentThreshold(G.INIT_SEGMENT_THRESHOLD);

		imageBoxesView.addTargetImageBoxClickListener(new MouseClickAdapter());

		controlPanelView.addBinarizateClickListener(this);
		controlPanelView.addOpenFileClickListener(this);
		controlPanelView.addBinPercentChangeValueListener(new BinPercentsChangeValueListener());
		controlPanelView.addSegmentThresholdChangeValueListener(new SegmentThresholChangeValueListener());
		controlPanelView.addSkeletonizationClickListener(this);
		controlPanelView.addHistogramSegmentClickListener(this);
		controlPanelView.addBugSegmentClickListener(this);
		controlPanelView.addTrimClickListener(this);
		controlPanelView.addErodeClickListener(this);
		controlPanelView.addDilateClickListener(this);
		controlPanelView.addResetClickListener(this);
		controlPanelView.addExtractClickListener(this);
		controlPanelView.addNeuralNetworkClickListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		switch (arg0.getActionCommand()) {
		case ControlPanelViewInterface.ACTION_BINARIZATION:
			mModel.binarization();
			imageBoxesView.updateTarget(mModel.getTargetImage());
			break;
		case ControlPanelViewInterface.ACTION_FILE_OPEN:
			BufferedImage source = openFile();
			if (source == null) return;
			mModel.setSourceImage(source);
			mModel.setTargetImage(source);
			imageBoxesView.updateSource(mModel.getSourceImage());
			imageBoxesView.updateTarget(mModel.getTargetImage());
			controlPanelView.enableControls();
			break;
		case ControlPanelViewInterface.ACTION_DILATE:
			mModel.morph(new DilateMorph());
			imageBoxesView.updateTarget(mModel.getTargetImage());
			break;
		case ControlPanelViewInterface.ACTION_ERODE:
			mModel.morph(new ErodeMorph());
			imageBoxesView.updateTarget(mModel.getTargetImage());
			break;
		case ControlPanelViewInterface.ACTION_EXTRACT:
			mModel.extract();
			imageBoxesView.updateTarget(mModel.getTargetImage());
			break;
		case ControlPanelViewInterface.ACTION_SKELETONIZATION:
			mModel.skeletonization();
			imageBoxesView.updateTarget(mModel.getTargetImage());
			break;
		case ControlPanelViewInterface.ACTION_SEGMENT_HISTOGRAM:
			mModel.histogramSegment(true);
			imageBoxesView.updateTarget(mModel.getTargetImage());
			break;
		case ControlPanelViewInterface.ACTION_SEGMENT_BUG:
			mModel.bugSegment();
			imageBoxesView.updateTarget(mModel.getTargetImage());
			break;
		case ControlPanelViewInterface.ACTION_RESET:
			mModel.setTargetImage(mModel.getSourceImage());
			break;
		case ControlPanelViewInterface.ACTION_TRIM:
			mModel.setTargetImage(mModel.trim(mModel.getTargetImage()));
			imageBoxesView.updateTarget(mModel.getTargetImage());
			break;
		case ControlPanelViewInterface.ACTION_NEURALNETWORK:
			mModel.detect();
			imageBoxesView.updateTarget(mModel.getTargetImage());
			break;
		}
	}

	private static final JFileChooser	FC	= new JFileChooser();

	private BufferedImage openFile() {

		BufferedImage loadedImage = null;
		int returnValue = FC.showDialog(null, ControlPanelViewInterface.ACTION_FILE_OPEN);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File file = FC.getSelectedFile();
			try {
				loadedImage = ImageIO.read(file);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return loadedImage;
	}
}
