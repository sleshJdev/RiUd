/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.nn;

import java.util.Random;

/**
 * @author slesh
 *
 */
public class Layer {

	private double[][]	weights;
	private int			quantityIn;
	private int			quantityOut;

	public void generateWeights() {

		Random rnd = new Random();
		for (int i = 0; i < quantityIn; ++i) {
			for (int j = 0; j < quantityOut; ++j) {
				weights[i][j] = rnd.nextDouble() - 0.5;
			}
		}
	}

	public Layer(int countX, int countY) {

		quantityIn = countX;
		quantityOut = countY;
		weights = new double[quantityIn][quantityOut];
	}

	public int getQuantityIn() {

		return quantityIn;
	}

	public int getQuantityOut() {

		return quantityOut;
	}

	public double get(int row, int col) {

		return weights[row][col];
	}

	public void set(int row, int col, double value) {

		weights[row][col] = value;
	}

}
