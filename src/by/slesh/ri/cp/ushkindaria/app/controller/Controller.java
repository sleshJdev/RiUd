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
import by.slesh.ri.cp.ushkindaria.ipt.ContourWorker;
import by.slesh.ri.cp.ushkindaria.ipt.Tool;
import by.slesh.ri.cp.ushkindaria.ipt.binarization.Binarizator;
import by.slesh.ri.cp.ushkindaria.ipt.morph.DilateMorph;
import by.slesh.ri.cp.ushkindaria.ipt.morph.ErodeMorph;

public class Controller implements ActionListener {

    private class BinPercentsChangeValueListener implements AdjustmentListener {

	@Override
	public void adjustmentValueChanged(AdjustmentEvent arg0) {
	    Binarizator.sPercents = arg0.getValue();
	    System.out.println(Binarizator.sPercents);
	    mIontrolPanelView.updatePercentValue(arg0.getValue());
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

	mFileView.addTeachClickListener(this);

	mImageBoxesView.addTargetImageBoxClickListener(new MouseClickAdapter());

	mIontrolPanelView.addBinarizateClickListener(this);
	mIontrolPanelView.addOpenFileClickListener(this);
	mIontrolPanelView
	        .addBinPercentChangeValueListener(new BinPercentsChangeValueListener());
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
	    BufferedImage copy = Tool.trim(mModel.getTargetImage(), 0, 0, 0, 0);
	    mModel.histogramSegment();
	    mImageBoxesView.updateTarget(mModel.getTargetImage());
	    mModel.setTargetImage(copy);
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
	    mModel.setTargetImage(mModel.trim());
	    mImageBoxesView.updateTarget(mModel.getTargetImage());
	    break;
	case ControlViewInterface.ACTION_NEURALNETWORK:
	    mModel.findSymbols();
	    mImageBoxesView.updateTarget(mModel.getTargetImage());
	    break;
	case ControlViewInterface.ACTION_EXTRACT_AREA_INTEREST:
	    mModel.extractAreaInterest();
	    mImageBoxesView.updateAreaInterest(mModel.getTargetImage());
	    G.sMainView.refresh();
	    break;
	case ControlViewInterface.ACTION_EXTRACT_GROUP_NUMBER:
	    mModel.extractGroupNumber();
	    mImageBoxesView.updateGroupNumber(mModel.getTargetImage());
	    break;
	case ControlViewInterface.ACTION_SEGMENT_GROUP_NUMBER:
	    mImageBoxesView.updateSegmentGroupNumber(mModel
		    .segmentGroupNumber());
	    mImageBoxesView.updateUnrecognizeNumber(mModel
		    .skeletonizationSegmentDigits());
	    break;
	case ControlViewInterface.ACTION_RECOGNIZE_NUMBER:
	    mImageBoxesView.updateRecognizeNumber(mModel.recognizeNumber());
	    G.sMainView.refresh();
	    break;
	}
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

  /*  public static void main(String[] args) {

	try {
	    BufferedImage source1 = ImageIO.read(new File("d:\\image1.png"));
	    BufferedImage source2 = ImageIO.read(new File("d:\\image2.png"));
	    BufferedImage source3 = ImageIO.read(new File("d:\\image3.png"));
	    BufferedImage source4 = ImageIO.read(new File("d:\\image4.png"));
	    BufferedImage source5 = ImageIO.read(new File("d:\\image5.png"));

	    ContourWorker cw1 = new ContourWorker(source1);
	    ContourWorker cw2 = new ContourWorker(source2);
	    ContourWorker cw3 = new ContourWorker(source3);
	    ContourWorker cw4 = new ContourWorker(source4);
	    ContourWorker cw5 = new ContourWorker(source5);
	    
	    System.out.println("1==="+cw1.getCentersOfContours().length);
	    System.out.println("2==="+cw2.getCentersOfContours().length);
	    System.out.println("3==="+cw3.getCentersOfContours().length);
	    System.out.println("4==="+cw4.getCentersOfContours().length);
	    System.out.println("5==="+cw5.getCentersOfContours().length);
	    
	    ImageIO.write(source1, "png", new File("d:\\imageCONTOUR1.png"));
	    ImageIO.write(source2, "png", new File("d:\\imageCONTOUR2.png"));
	    ImageIO.write(source3, "png", new File("d:\\imageCONTOUR3.png"));
	    ImageIO.write(source4, "png", new File("d:\\imageCONTOUR4.png"));
	    ImageIO.write(source5, "png", new File("d:\\imageCONTOUR5.png"));
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }*/

    private static void fill(int k) {
	if (k < 0 || k >= pixelsData.length) return;
	if (pixelsData[k] != Tool._0) return;
	pixelsData[k] = Color.GREEN.getRGB();
	
	fill(k - 1);
	fill(k + 1);
	
	fill(k - width);
	fill(k + width);

	fill(k - width - 1);
	fill(k - width + 1);

	fill(k + width - 1);
	fill(k + width + 1);
    }

    static int[] pixelsData;
    static int height;
    static int width;

    private static void fillContour(BufferedImage source) {
	height = source.getHeight();
	width = source.getWidth();
	pixelsData = source.getRGB(0, 0, width, height, null, 0, width);
	for (int k = 0; k < pixelsData.length; ++k) {
	    if (pixelsData[k] == Color.RED.getRGB()) {
		int index = k;
		if ((index = isNext(source, index + 1, Tool._1)) != -1) {
		    if ((index = isNext(source, index + 1, Tool._0)) != -1) {
			int inner = index;
			if ((index = isNext(source, index + 1, Tool._1)) != -1) {
			    fill(inner);
			}
		    }
		}
	    }
	}
	source.setRGB(0, 0, width, height, pixelsData, 0, width);
    }

    private static int isNext(BufferedImage source, int start, int nextColor) {
	int h = source.getHeight();
	int w = source.getWidth();
	int offset = w - start % w;
	int[] pixels = source.getRGB(0, 0, w, h, null, 0, w);
	for (int t = 0, k = start; k < pixels.length && t < offset
	        && pixels[k] != Color.RED.getRGB(); ++k, ++t) {
	    if (pixels[k] == nextColor) {
		return k;
	    }
	}
	return -1;
    }

    private static Point findEntryPoint(BufferedImage source) {
	int h = source.getHeight();
	int w = source.getWidth();
	int[] pixels = source.getRGB(0, 0, w, h, null, 0, w);
	for (int k = 0, y = 0; y < h; ++y)
	    for (int x = 0; x < w; ++x, ++k) {
		if (pixels[k] == Tool._1) return new Point(x - 1, y);
		// source.setRGB(x, y, Color.YELLOW.getRGB());
	    }

	return null;
    }

    private static int contourFinder(BufferedImage source) {
	Point entry = findEntryPoint(source);

	int h = source.getHeight();
	int w = source.getWidth();

	Point next = null;
	Point curr = new Point(entry);
	Point old = new Point(entry);
	old.translate(0, 1);

	Point[] offsets;

	// source.setRGB(entry.x, entry.y, Color.GREEN.getRGB());
	// source.setRGB(old.x, old.y, Color.BLUE.getRGB());

	int counter = 0;
	do {
	    System.out.println("--------------");
	    offsets = getOffset(old, curr);

	    for (Point offset : offsets) {
		next = new Point(curr);
		next.translate(offset.x, offset.y);
		if (next.x < 0 || next.x >= w || next.y < 0 || next.y >= h) {
		    return 0;
		}
		if (source.getRGB(next.x, next.y) == Tool._0
		        || source.getRGB(next.x, next.y) == Color.RED.getRGB()) {
		    source.setRGB(next.x, next.y, Color.RED.getRGB());
		    old = curr;
		    curr = next;
		    break;
		}
	    }
	    if (next.equals(old)) {
		curr = old;
	    }

	    System.out.println("entry=" + entry);
	    System.out.println("old=" + old);
	    System.out.println("curr=" + curr);
	    for (Point point : offsets) {
		System.out.println(point + " ");
	    }
	    System.out.println("-----------------------");
	    // if(++counter == 122) break;
	} while (!entry.equals(curr));

	// source.setRGB(entry.x, entry.y, Color.GREEN.getRGB());
	// source.setRGB(old.x, old.y, Color.BLUE.getRGB());

	return 0;
    }

    private static Point[] getOffset(Point prev, Point curr) {
	if (curr.x - prev.x == 1) {
	    // from left
	    System.out.println("from left");
	    return new Point[] { new Point(0, 1), new Point(1, 0),
		    new Point(0, -1), new Point(-1, 0) };

	} else if (curr.x - prev.x == -1) {
	    // from right
	    System.out.println("from right");
	    return new Point[] { new Point(0, -1), new Point(-1, 0),
		    new Point(0, 1), new Point(1, 0) };
	} else if (curr.y - prev.y == 1) {
	    // from top
	    System.out.println("from top");
	    return new Point[] { new Point(-1, 0), new Point(0, 1),
		    new Point(1, 0), new Point(0, -1) };
	} else if (curr.y - prev.y == -1) {
	    // from bottom
	    System.out.println("from bottom");
	    return new Point[] { new Point(1, 0), new Point(0, -1),
		    new Point(-1, 0), new Point(0, 1) };
	}
	return null;
    }

    public static BufferedImage openFile() {
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
