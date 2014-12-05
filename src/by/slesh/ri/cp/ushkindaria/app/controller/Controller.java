package by.slesh.ri.cp.ushkindaria.app.controller;

import java.awt.Color;
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
import by.slesh.ri.cp.ushkindaria.app.view.service.ControlViewInterface;
import by.slesh.ri.cp.ushkindaria.app.view.service.FileViewInterface;
import by.slesh.ri.cp.ushkindaria.app.view.service.ImageBoxesViewInterface;
import by.slesh.ri.cp.ushkindaria.ipt.binarization.Binarizator;
import by.slesh.ri.cp.ushkindaria.ipt.morph.DilateMorph;
import by.slesh.ri.cp.ushkindaria.ipt.morph.ErodeMorph;
import by.slesh.ri.cp.ushkindaria.ipt.segment.RelationSegmentator;
import by.slesh.ri.cp.ushkindaria.nn.FinderPerseptron;

public class Controller implements ActionListener {

    private class BinPercentsChangeValueListener implements AdjustmentListener {

	@Override
	public void adjustmentValueChanged(AdjustmentEvent arg0) {
	    Binarizator.sPercents = arg0.getValue();
	    mIontrolPanelView.updatePercentValue(arg0.getValue());
	}
    }

    private class SegmentThresholChangeValueListener implements
	    AdjustmentListener {
	@Override
	public void adjustmentValueChanged(AdjustmentEvent arg0) {
	    mModel.setSegmentThreshold(arg0.getValue());
	    mIontrolPanelView.updateSegmentThresholdValue(arg0.getValue());
	}
    }

    private class MouseClickAdapter extends MouseAdapter {

	@Override
	public void mouseClicked(MouseEvent arg0) {

	    Point location = new Point(arg0.getPoint());
	    mImageBoxesView.getLocationOnImage(location);
	    mModel.setStartPointForBugSegmentator(location);
	    mImageBoxesView.updateTarget(mModel.getTargetImage());
	}
    }

    private ControlViewInterface mIontrolPanelView;
    private ImageBoxesViewInterface mImageBoxesView;
    private FileViewInterface mFileView;
    private Model mModel;

    public Controller(ControlViewInterface controlViewInterface,
	    ImageBoxesViewInterface imageBoxesViewInterface,
	    FileViewInterface fileViewInterface, Model model) {

	mImageBoxesView = imageBoxesViewInterface;
	mIontrolPanelView = controlViewInterface;
	mFileView = fileViewInterface;
	mModel = model;

	Binarizator.sPercents = G.INIT_BIN_PERCENT;
	mModel.setSegmentThreshold(G.INIT_SEGMENT_THRESHOLD);

	mFileView.addTeachClickListener(this);

	mImageBoxesView.addTargetImageBoxClickListener(new MouseClickAdapter());

	mIontrolPanelView.addBinarizateClickListener(this);
	mIontrolPanelView.addOpenFileClickListener(this);
	mIontrolPanelView
	        .addBinPercentChangeValueListener(new BinPercentsChangeValueListener());
	mIontrolPanelView
	        .addSegmentThresholdChangeValueListener(new SegmentThresholChangeValueListener());
	mIontrolPanelView.addSkeletonizationClickListener(this);
	mIontrolPanelView.addHistogramSegmentClickListener(this);
	mIontrolPanelView.addBugSegmentClickListener(this);
	mIontrolPanelView.addTrimClickListener(this);
	mIontrolPanelView.addErodeClickListener(this);
	mIontrolPanelView.addDilateClickListener(this);
	mIontrolPanelView.addResetClickListener(this);
	mIontrolPanelView.addExtractAreaInterestClickListener(this);
	mIontrolPanelView.addNeuralNetworkClickListener(this);
	mIontrolPanelView.addExtractGroupNumberClickListener(this);
	mIontrolPanelView.addSegmentGroupNumberClickListener(this);
	mIontrolPanelView.addRecognizeNumberClickListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

	switch (arg0.getActionCommand()) {
	case FileViewInterface.ACTION_TEACH:

	    break;
	case ControlViewInterface.ACTION_FILE_OPEN:
	    BufferedImage source = openFile();
	    if (source == null) return;
	    mModel.setSourceImage(source);
	    mModel.setTargetImage(source);
	    mImageBoxesView.updateSource(mModel.getSourceImage());
	    mImageBoxesView.updateTarget(mModel.getTargetImage());
	    mIontrolPanelView.enableControls();
	    break;
	case ControlViewInterface.ACTION_BINARIZATION:
	    mModel.binarization();
	    mImageBoxesView.updateTarget(mModel.getTargetImage());
	    break;
	case ControlViewInterface.ACTION_DILATE:
	    mModel.morph(new DilateMorph());
	    mImageBoxesView.updateTarget(mModel.getTargetImage());
	    break;
	case ControlViewInterface.ACTION_ERODE:
	    mModel.morph(new ErodeMorph());
	    mImageBoxesView.updateTarget(mModel.getTargetImage());
	    break;
	case ControlViewInterface.ACTION_SKELETONIZATION:
	    mModel.skeletonization();
	    mImageBoxesView.updateTarget(mModel.getTargetImage());
	    break;
	case ControlViewInterface.ACTION_SEGMENT_HISTOGRAM:
	    mModel.histogramSegment();
	    mImageBoxesView.updateTarget(mModel.getTargetImage());
	    break;
	case ControlViewInterface.ACTION_SEGMENT_BUG:
	    mModel.bugSegment();
	    mImageBoxesView.updateTarget(mModel.getTargetImage());
	    break;
	case ControlViewInterface.ACTION_RESET:
	    mModel.setTargetImage(mModel.getSourceImage());
	    mImageBoxesView.updateTarget(mModel.getTargetImage());
	    break;
	case ControlViewInterface.ACTION_TRIM:
	    mModel.setTargetImage(mModel.trim(mModel.getTargetImage()));
	    mImageBoxesView.updateTarget(mModel.getTargetImage());
	    break;
	case ControlViewInterface.ACTION_NEURALNETWORK:
	    mModel.detect();
	    mImageBoxesView.updateTarget(mModel.getTargetImage());
	    break;
	case ControlViewInterface.ACTION_EXTRACT_AREA_INTEREST:
	    mModel.extractAreaInterest();
	    mImageBoxesView.updateAreaInterest(mModel.getTargetImage());
	    break;
	case ControlViewInterface.ACTION_EXTRACT_GROUP_NUMBER:
	    mModel.extractGroupNumber();
	    mImageBoxesView.updateGroupNumber(mModel.getTargetImage());
	    break;
	case ControlViewInterface.ACTION_SEGMENT_GROUP_NUMBER:
	    mImageBoxesView
		    .updateUnrecognizeNumber(mModel.segmentGroupNumber());
	    break;
	case ControlViewInterface.ACTION_RECOGNIZE_NUMBER:
	    mImageBoxesView.updateRecognizeNumber(mModel.recognizeNumber());
	    break;
	}
    }

    private static final JFileChooser FC = new JFileChooser(".");

    // private File chooseDirectory() {
    // FC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    // File dirFile = null;
    // int returnValue = FC.showDialog(null, "Эта директория");
    // if (returnValue == JFileChooser.APPROVE_OPTION) {
    // dirFile = FC.getSelectedFile();
    // }
    // return dirFile;
    // }

    private static BufferedImage openFile() {
	FC.setFileSelectionMode(JFileChooser.FILES_ONLY);
	BufferedImage loadedImage = null;
	int returnValue = FC.showDialog(null, "Этот файл");
	if (returnValue == JFileChooser.APPROVE_OPTION) {
	    File file = FC.getSelectedFile();
	    try {
		loadedImage = ImageIO.read(file);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	return loadedImage;
    }
}
