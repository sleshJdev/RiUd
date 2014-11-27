package by.slesh.ri.cp.ushkindaria.nn;

import java.util.Random;

public class Neuron {
    private int[]            mWeights;
    private static final int LIMIT = 50;

    Neuron(int m) {
	mWeights = new int[m];
    }

    public int transfer(int[] x) {
	return activator(adder(x));
    }

    public void initWeights(int n) {
	Random rand = new Random();
	for (int i = 0; i < mWeights.length; i++) {
	    mWeights[i] = n / 2 - rand.nextInt(n);
	}
    }

    public void changeWeights(int v, int d, int[] x) {
	for (int i = 0; i < mWeights.length; i++) {
	    mWeights[i] += v * d * x[i];
	}
    }

    public int adder(int[] x) {
	double sum = 0;
	for (int k = 0; k < x.length; ++k) {
	    sum += mWeights[k] * x[k];
	}
	return (int) sum;
    }

    private int activator(int nec) {
	return nec >= LIMIT ? 1 : 0;
    }

    public int[] getWeights() {
	return mWeights;
    }
}
