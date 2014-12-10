/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.nn;

import java.awt.Container;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import by.slesh.ri.cp.ushkindaria.app.G;
import by.slesh.ri.cp.ushkindaria.ipt.Tool;

/**
 * @author slesh
 *
 */
public class Perseptron {
    private static Perseptron mInstance;

    private Neuron[] mNeurons;
    private int mQuantityNeurons;
    private int mQuantityInputs;
    private int[] mSums;

    protected String[] mNames;
    protected int mTargetWidth;
    protected int mTargetHeight;
    protected String mPathToSaveWeights;
    protected String mPathToSet;

    private Perseptron(int quantityNeurons, int quantityInputs) {
	mQuantityNeurons = quantityNeurons;
	mQuantityInputs = quantityInputs;
	mNeurons = new Neuron[mQuantityNeurons];
	mPathToSaveWeights = G.PATH_WEIGHTS_FINDER;
	mPathToSet = G.PATH_SET_FIDNER;
	mTargetWidth = G.WIDTH_IMAGE;
	mTargetHeight = G.HEIGHT_IMAGE;
	mNames = G.NAMES_FOR_FINDER;
	for (int j = 0; j < mNeurons.length; j++) {
	    mNeurons[j] = new Neuron(mQuantityInputs);
	    // mNeurons[j].initWeights(10);
	}
    }

    public static void initInstance(int quantityNeurons, int quantityInputs) {
	if (mInstance == null) {
	    mInstance = new Perseptron(quantityNeurons, quantityInputs);
	}
    }

    public static Perseptron getInstance() {
	return mInstance;
    }

    public int[] recognize(BufferedImage image) {
	return recognize(initX(image));
    }

    public int[] recognize(int[] x) {
	int[] y = new int[mNeurons.length];
	mSums = new int[mNeurons.length];
	for (int j = 0; j < mNeurons.length; j++) {
	    y[j] = mNeurons[j].transfer(x);
	    mSums[j] = mNeurons[j].adder(x);
	}
	return y;
    }

    public void initWeights(int max) {
	for (Neuron neuron : mNeurons) {
	    neuron.initWeights(max);
	}
    }

    public void teach(int[] x, int[] y) {
	final int v = 1;
	int[] t = recognize(x);
	while (!isVctorEqual(t, y)) {
	    for (int j = 0; j < mNeurons.length; j++) {
		int d = y[j] - t[j];
		mNeurons[j].changeWeights(v, d, x);
	    }
	    t = recognize(x);
	}
    }

    private Boolean isVctorEqual(int[] a, int[] b) {
	if (a.length != b.length) {
	    return false;
	}
	for (int k = 0; k < a.length; ++k) {
	    if (a[k] != b[k]) {
		return false;
	    }
	}
	return true;
    }

    private void teach(BufferedImage source, int label) {
	int[] x = initX(source);
	int[] y = initY(label, mQuantityNeurons);
	teach(x, y);
    }

    public void teach(int n) {
	class JPGFilter implements FilenameFilter {
	    public boolean accept(File dir, String name) {
		return (name.endsWith(".bmp"));
	    }
	}
	String[] list = new File(mPathToSet + "/").list(new JPGFilter());
	BufferedImage[] img = new BufferedImage[list.length];
	MediaTracker mediaTracker = new MediaTracker(new Container());
	for (int i = 0; i < list.length; ++i) {
	    try {
		img[i] = ImageIO.read(new File(mPathToSet + "\\" + list[i]));
	    } catch (IOException e) {
		JOptionPane.showMessageDialog(null, e.getMessage());
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
	for (int k = 0; k < mQuantityNeurons; ++k) {
	    int[] weights = getWeights(k);
	    try {
		FileWriter writer = new FileWriter(getFileName(k));
		for (int index = 0, y = 0; y < mTargetHeight; ++y) {
		    for (int x = 0; x < mTargetWidth; ++x) {
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
    }

    public String getNameById(int id) {
	return mNames[id];
    }

    public int getIdClassByName(String name) {
	int id = 0;
	for (int i = 0; i < mQuantityNeurons; ++i) {
	    if (mNames[i].equals(name)) {
		id = i;
		break;
	    }
	}
	return id;
    }

    private String getFileName(int id) {
	return mPathToSaveWeights + "\\" + getNameById(id) + ".txt";
    }

    public int getNeuronCount() {
	return mQuantityNeurons;
    }

    public int getQuantityInputs() {
	return mQuantityInputs;
    }

    public int[] getWeights(int i) {
	return mNeurons[i].getWeights();
    }

    public int[] getAnswer() {
	return mSums;
    }

    public static int[] initX(BufferedImage image) {
	int h = image.getHeight();
	int w = image.getWidth();
	int[] binaryImage = new int[w * h];
	for (int y = 0, k = 0; y < h; ++y) {
	    for (int x = 0; x < w; ++x) {
		binaryImage[k++] = image.getRGB(x, y) == Tool._0 ? -1 : 1;
	    }
	}
	return binaryImage;
    }

    public static int[] initY(int code, int n) {

	int[] y = new int[n];
	y[code] = 1;
	return y;
    }
}
