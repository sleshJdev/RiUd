/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.nn;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * @author slesh
 *
 */
public class NeuralNetwork {

	private class Index {

		int	i	= 0;
	}

	private Layer[]		layers;
	private int			quantityLayers	= 0;
	private int			quantityIn;
	private int			quantityOut;
	private double[][]	out;
	private double[][]	delta;

	public NeuralNetwork(int quantityIn, int quantityOut) {

		quantityLayers = 1;
		layers = new Layer[quantityLayers];
		layers[0] = new Layer(quantityIn, quantityOut);
		layers[0].generateWeights();
	}

	public NeuralNetwork(int quantityIn, int[] layers) {

		quantityLayers = layers.length;
		this.quantityIn = quantityIn;
		this.layers = new Layer[quantityLayers];
		quantityOut = layers[quantityLayers - 1];
		out = new double[quantityLayers + 1][];
		out[0] = new double[quantityIn];
		delta = new double[quantityLayers][];

		int qOut = 0;
		int qIn = quantityIn;
		for (int i = 0; i < quantityLayers; ++i) {
			qOut = layers[i];
			out[i + 1] = new double[qOut];
			delta[i] = new double[qOut];
			this.layers[i] = new Layer(qIn, qOut);
			this.layers[i].generateWeights();
			qIn = qOut;
		}
	}

	public NeuralNetwork(String fileName) {

		try {
			Open(fileName);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Open neutral network
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public void Open(String fileName) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream input = new FileInputStream(new File(fileName));
		int quantityGet;
		byte[] buffer = new byte[1024];
		while ((quantityGet = input.read(buffer, 0, buffer.length)) != -1) {
			baos.write(buffer, 0, quantityGet);
		}
		baos.flush();
		input.close();

		/* Binary neutral network */
		byte[] bin = baos.toByteArray();

		Index pos = new Index();
		quantityLayers = ReadFromArrayInt(bin, pos);
		layers = new Layer[quantityLayers];

		int qOut = 0;
		int qIn = ReadFromArrayInt(bin, pos);
		quantityIn = qIn;

		out = new double[quantityLayers + 1][];

		out[0] = new double[qIn];
		delta = new double[quantityLayers][];

		for (int i = 0; i < quantityLayers; i++) {
			qOut = ReadFromArrayInt(bin, pos);
			layers[i] = new Layer(qIn, qOut);
			qIn = qOut;

			out[i + 1] = new double[qOut];
			delta[i] = new double[qOut];
		}

		quantityOut = qOut;
		for (int i = 0; i < quantityLayers; i++)
			for (int j = 0; j < layers[i].getQuantityIn(); j++)
				for (int k = 0; k < layers[i].getQuantityOut(); k++) {
					double value = ReadFromArrayDouble(bin, pos);
					layers[i].set(j, k, value);
				}
	}

	/**
	 * Save neutral network
	 * 
	 * @param FileName
	 */
	public void Save(String FileName) {

		byte[] bin = new byte[countSize()];
		Index pos = new Index();
		WriteInArray(bin, pos, quantityLayers);

		if (quantityLayers <= 0) return;

		WriteInArray(bin, pos, layers[0].getQuantityIn());
		for (int i = 0; i < quantityLayers; i++)
			WriteInArray(bin, pos, layers[i].getQuantityOut());

		/* Write weights */
		for (int i = 0; i < quantityLayers; i++)
			for (int j = 0; j < layers[i].getQuantityIn(); j++)
				for (int k = 0; k < layers[i].getQuantityOut(); k++)
					WriteInArray(bin, pos, layers[i].get(j, k));

		try {
			FileOutputStream output = new FileOutputStream(FileName);
			output.write(bin);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void countNetOut(double[] in, double[] outY, int jLayer) {

		GetOUT(in, jLayer);
		int N = out[jLayer].length;

		for (int i = 0; i < N; i++) {
			outY[i] = out[jLayer][i];
		}

	}

	public void netOut(double[] x, double[] y) {

		int j = quantityLayers;
		countNetOut(x, y, j);
	}

	public double countError(double[] x, double[] y) {

		double error = 0;
		for (int i = 0; i < y.length; i++) {
			error += Math.pow(y[i] - out[quantityLayers][i], 2);
		}
		return 0.5 * error;
	}

	/*
	 * Обучает сеть, изменяя ее весовые коэффициэнты. X, Y - обучающая пара.
	 * kLern - скорость обучаемости В качестве результата метод возвращает
	 * ошибку 0.5(Y-outY)^2
	 */
	public double learn(double[] X, double[] Y, double kLern) {

		double in; // Вход нейрона
		double s;

		// Вычисляем выход сети
		GetOUT(X);

		// Заполняем дельта последнего слоя
		for (int j = 0; j < layers[quantityLayers - 1].getQuantityOut(); j++) {
			in = out[quantityLayers][j];
			delta[quantityLayers - 1][j] = (Y[j] - in) * in * (1 - in);
		}

		// Перебираем все слои начиная споследнего
		// изменяя веса и вычисляя дельта для скрытого слоя
		for (int k = quantityLayers - 1; k >= 0; k--) {
			// Изменяем веса выходного слоя
			for (int j = 0; j < layers[k].getQuantityOut(); j++) {
				for (int i = 0; i < layers[k].getQuantityIn(); i++) {
					double value = kLern * delta[k][j] * out[k][i];
					layers[k].set(i, j, value);
				}
			}
			if (k > 0) {
				// Вычисляем дельта слоя к-1
				for (int j = 0; j < layers[k - 1].getQuantityOut(); j++) {

					s = 0;
					for (int i = 0; i < layers[k].getQuantityOut(); i++) {
						s += layers[k].get(j, i) * delta[k][i];
					}

					delta[k - 1][j] = out[k][j] * (1 - out[k][j]) * s;
				}
			}
		}

		return countError(X, Y);
	}

	// Свойства. Возвращает число входов и выходов сети
	public int getInQuatity() {

		return quantityIn;
	}

	public int getOutQuatity() {

		return quantityOut;
	}

	public int CountLayers() {

		return quantityLayers;
	}

	/* Вспомогательные закрытые функции */

	// Возвращает все значения нейронов до lastLayer слоя
	void GetOUT(double[] inX, int lastLayer) {

		double s;

		for (int j = 0; j < layers[0].getQuantityIn(); j++)
			out[0][j] = inX[j];

		for (int i = 0; i < lastLayer; i++) {
			// размерность столбца проходящего через i-й слой
			for (int j = 0; j < layers[i].getQuantityOut(); j++) {
				s = 0;
				for (int k = 0; k < layers[i].getQuantityIn(); k++) {
					s += layers[i].get(k, j) * out[i][k];
				}
				// Вычисляем значение активационной функции
				s = 1.0 / (1 + Math.exp(-s));
				out[i + 1][j] = 0.998 * s + 0.001;
			}
		}

	}

	// Возвращает все значения нейронов всех слоев
	void GetOUT(double[] inX) {

		GetOUT(inX, quantityLayers);
	}

	static int	sizeOfInt		= Integer.SIZE / 8;
	static int	sizeOfDouble	= Double.SIZE / 8;

	// Возвращает размер НС в байтах
	int countSize() {

		int sizeNW = sizeOfInt * (quantityLayers + 2);
		for (int i = 0; i < quantityLayers; i++) {
			sizeNW += sizeOfDouble * layers[i].getQuantityIn() * layers[i].getQuantityOut();
		}
		return sizeNW;

	}

	// Возвращает num-й слой Нейронной сети
	public Layer Layer(int num) {

		return layers[num];
	}

	private static void reverse(byte[] bytes) {

		for (int i = 0, j = bytes.length - 1; i <= j;) {
			byte buffer = bytes[i];
			bytes[i++] = bytes[j];
			bytes[j--] = buffer;
		}
	}

	void WriteInArray(byte[] mas, Index pos, int value) {

		byte[] intBytes = new byte[sizeOfInt];
		ByteBuffer intByteBuffer = ByteBuffer.wrap(intBytes);
		intByteBuffer.putInt(value);
		for (int k = 0; k < intBytes.length; ++k)
			mas[pos.i++] = intBytes[k];
	}

	int ReadFromArrayInt(byte[] mas, Index pos) {

		byte[] intBytes = new byte[sizeOfInt];
		for (int k = 0; k < intBytes.length; ++k)
			intBytes[k] = mas[pos.i++];
		reverse(intBytes);
		ByteBuffer intByteBuffer = ByteBuffer.wrap(intBytes);
		return intByteBuffer.getInt();
	}

	// Разбивает переменную типа int на байты и записывает в массив
	void WriteInArray(byte[] mas, Index pos, double value) {

		byte[] doubleBytes = new byte[sizeOfDouble];
		ByteBuffer doublebyteBuffer = ByteBuffer.wrap(doubleBytes);
		doublebyteBuffer.putDouble(value);
		for (int k = 0; k < doubleBytes.length; ++k)
			mas[pos.i++] = doubleBytes[k];
	}

	// Извлекает переменную типа double из 8-ми байтов массива
	double ReadFromArrayDouble(byte[] mas, Index pos) {

		byte[] doubleBytes = new byte[sizeOfDouble];
		for (int k = 0; k < doubleBytes.length; ++k)
			doubleBytes[k] = mas[pos.i++];
		reverse(doubleBytes);
		ByteBuffer doublebyteBuffer = ByteBuffer.wrap(doubleBytes);
		return doublebyteBuffer.getDouble();
	}
}
