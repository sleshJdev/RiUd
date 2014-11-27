package by.slesh.ri.cp.ushkindaria.nn;

public class Perceptron {
    private Neuron[]          mNeurons;
    private int               mQuantityNeurons;
    private int               mQuantityInputs;
    private int[]             mSums;

    private static Perceptron mInstance;

    private Perceptron(int quantityNeurons, int quantityInputs) {
	mQuantityNeurons = quantityNeurons;
	mQuantityInputs = quantityInputs;
	mNeurons = new Neuron[mQuantityNeurons];
	for (int j = 0; j < mNeurons.length; j++) {
	    mNeurons[j] = new Neuron(mQuantityInputs);
	}
    }
    
    public static void initInstance(int quantityNeurons, int quantityInputs) {
	if (mInstance == null) {
	    mInstance = new Perceptron(quantityNeurons, quantityInputs);
	}
    }

    public static Perceptron getInstance() {
	return mInstance;
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
	while (!VectorEqual(t, y)) {
	    for (int j = 0; j < mNeurons.length; j++) {
		int d = y[j] - t[j];
		mNeurons[j].changeWeights(v, d, x);
	    }
	    t = recognize(x);
	}
    }

    private Boolean VectorEqual(int[] a, int[] b) {
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
