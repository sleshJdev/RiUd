package by.slesh.ri.cp.ushkindaria.app.controller;

import java.awt.Container;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import by.slesh.ri.cp.ushkindaria.app.G;
import by.slesh.ri.cp.ushkindaria.app.model.Model;
import by.slesh.ri.cp.ushkindaria.app.view.service.ControlViewInterface;
import by.slesh.ri.cp.ushkindaria.app.view.service.FileViewInterface;
import by.slesh.ri.cp.ushkindaria.app.view.service.ImageBoxesViewInterface;
import by.slesh.ri.cp.ushkindaria.ipt.AbstractTool;
import by.slesh.ri.cp.ushkindaria.ipt.DilateMorph;
import by.slesh.ri.cp.ushkindaria.ipt.ErodeMorph;
import by.slesh.ri.cp.ushkindaria.nn.Helpers;
import by.slesh.ri.cp.ushkindaria.nn.Perceptron;

public class Controller implements ActionListener {

    private class BinPercentsChangeValueListener implements AdjustmentListener {

	@Override
	public void adjustmentValueChanged(AdjustmentEvent arg0) {

	    mModel.setPercents(arg0.getValue());
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

    private ControlViewInterface    mIontrolPanelView;
    private ImageBoxesViewInterface mImageBoxesView;
    private FileViewInterface       mFileView;
    private Model                   mModel;

    public Controller(ControlViewInterface controlViewInterface,
	    ImageBoxesViewInterface imageBoxesViewInterface,
	    FileViewInterface fileViewInterface, Model model) {

	mImageBoxesView = imageBoxesViewInterface;
	mIontrolPanelView = controlViewInterface;
	mFileView = fileViewInterface;
	mModel = model;

	mModel.setPercents(G.INIT_BIN_PERCENT);
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
	mIontrolPanelView.addExtractClickListener(this);
	mIontrolPanelView.addNeuralNetworkClickListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

	switch (arg0.getActionCommand()) {
	case FileViewInterface.ACTION_TEACH:
	    Perceptron.initInstance(G.QUANTITY_NEURONS, G.QUANTITY_INPUTS);
	    teach(G.PATH_SET, 5);
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
	case ControlViewInterface.ACTION_EXTRACT:
	    mModel.extract();
	    mImageBoxesView.updateTarget(mModel.getTargetImage());
	    break;
	case ControlViewInterface.ACTION_SKELETONIZATION:
	    mModel.skeletonization();
	    mImageBoxesView.updateTarget(mModel.getTargetImage());
	    break;
	case ControlViewInterface.ACTION_SEGMENT_HISTOGRAM:
	    mModel.histogramSegment(true);
	    mImageBoxesView.updateTarget(mModel.getTargetImage());
	    break;
	case ControlViewInterface.ACTION_SEGMENT_BUG:
	    mModel.bugSegment();
	    mImageBoxesView.updateTarget(mModel.getTargetImage());
	    break;
	case ControlViewInterface.ACTION_RESET:
	    mModel.setTargetImage(mModel.getSourceImage());
	    break;
	case ControlViewInterface.ACTION_TRIM:
	    mModel.setTargetImage(mModel.trim(mModel.getTargetImage()));
	    mImageBoxesView.updateTarget(mModel.getTargetImage());
	    break;
	case ControlViewInterface.ACTION_NEURALNETWORK:
	    mModel.detect();
	    mImageBoxesView.updateTarget(mModel.getTargetImage());
	    break;
	}
    }

    private void teach(BufferedImage source, int label) {
	Perceptron perceptron = Perceptron.getInstance();
	int[] x = Helpers.initX(source);
	int[] y = Helpers.initY(label - 1);
	perceptron.teach(x, y);
    }

    private void teach(String path, int n) {
	class JPGFilter implements FilenameFilter {
	    public boolean accept(File dir, String name) {
		return (name.endsWith(".bmp"));
	    }
	}

	String[] list = new File(path + "/").list(new JPGFilter());
	BufferedImage[] img = new BufferedImage[list.length];
	MediaTracker mediaTracker = new MediaTracker(new Container());

	for (int i = 0; i < list.length; ++i) {
	    try {
		img[i] = ImageIO.read(new File(path + "/" + list[i]));
	    } catch (IOException err) {
		JOptionPane.showMessageDialog(null, err.getMessage());
	    }

	    mediaTracker.addImage(img[i], 0);

	    try {
		mediaTracker.waitForAll();
	    } catch (InterruptedException e) {
		JOptionPane.showMessageDialog(null, e.getMessage());
	    }
	}

	while (n-- > 0) {
	    for (int j = 0; j < img.length; j++) {
		int label = Integer.parseInt(String.valueOf(list[j].charAt(0)));
		teach(img[j], label);
	    }
	}

	for (int k = 0; k < G.QUANTITY_NEURONS; ++k) {
	    int[] weights = Perceptron.getInstance().getWeights(k);
	    try {
		FileWriter writer = new FileWriter(getFileName(k));
		for (int index = 0, y = 0; y < G.HEIGHT; ++y) {
		    for (int x = 0; x < G.WIDTH; ++x) {
			String s = String.format("%1$3s",
			        Integer.toString(weights[index++]));
			writer.write(s);
		    }
		    writer.write("\n");
		}
		writer.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	JOptionPane.showMessageDialog(null, "Обучение завершено!");
    }

    private String getFileName(int id) {
	return G.PATH_WEIGHTS + "\\" + G.getNameById(id) + ".txt";
    }

    private static final JFileChooser FC = new JFileChooser(".");

    private File chooseDirectory() {
	FC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	File dirFile = null;
	int returnValue = FC.showDialog(null, "Эта директория");
	if (returnValue == JFileChooser.APPROVE_OPTION) {
	    dirFile = FC.getSelectedFile();
	}
	return dirFile;
    }

    private BufferedImage openFile() {
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
