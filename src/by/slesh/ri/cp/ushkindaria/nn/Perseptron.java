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

abstract class Perseptron {
    private Neuron[] mNeurons;
    private int mQuantityNeurons;
    private int mQuantityInputs;
    private int[] mSums;

    protected enum ResizeState {
	SCALE_UP, SCALE_LOW, NO
    }

    protected String[] names;
    protected int targetWidth;
    protected int targetHeight;
    protected ResizeState resizeState = ResizeState.NO;
    protected String pathToSaveWeights;
    protected String pathToSet;

    protected Perseptron(int quantityNeurons, int quantityInputs) {
	mQuantityNeurons = quantityNeurons;
	mQuantityInputs = quantityInputs;
	mNeurons = new Neuron[mQuantityNeurons];
	for (int j = 0; j < mNeurons.length; j++) {
	    mNeurons[j] = new Neuron(mQuantityInputs);
	    // mNeurons[j].initWeights(10);
	}
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
	int[] x = Helpers.initX(source);
	int[] y = Helpers.initY(label, mQuantityNeurons);
	teach(x, y);
    }

    public void teach(int n) {
	class JPGFilter implements FilenameFilter {
	    public boolean accept(File dir, String name) {
		return (name.endsWith(".bmp"));
	    }
	}
	String[] list = new File(pathToSet + "/").list(new JPGFilter());
	BufferedImage[] img = new BufferedImage[list.length];
	MediaTracker mediaTracker = new MediaTracker(new Container());
	for (int i = 0; i < list.length; ++i) {
	    try {
		img[i] = ImageIO.read(new File(pathToSet + "\\" + list[i]));
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
		for (int index = 0, y = 0; y < targetHeight; ++y) {
		    for (int x = 0; x < targetWidth; ++x) {
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
	return names[id];
    }

    public int getIdClassByName(String name) {
	int id = 0;
	for (int i = 0; i < mQuantityNeurons; ++i) {
	    if (names[i].equals(name)) {
		id = i;
		break;
	    }
	}
	return id;
    }

    private String getFileName(int id) {
	return pathToSaveWeights + "\\" + getNameById(id) + ".txt";
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
}
