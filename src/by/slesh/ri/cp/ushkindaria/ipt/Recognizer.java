/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.ipt;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author slesh
 *
 */
public class Recognizer {
    private static double[][] mPatterns = {
	    { 0.007371007371007371, 0.02375102375102375, 0.02375102375102375, 0.013923013923013924, 	0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,   1.0,      0 },// 0
	    { 0.02304147465437788, 0.021889400921658985, 0.027649769585253458, 0.018433179723502304, 	0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,   1.0,      0 },// 0
	    { 0.014729950900163666, 0.015275504637206765, 0.01800327332242226, 0.01691216584833606, 	0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,   1.0,  	 0 },// 0
	    { 0.008064516129032258, 0.024193548387096774, 0.025985663082437275, 0.015232974910394265, 	0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,   1.0,      0 },// 0
	    { 0.01818181818181818, 0.018787878787878787, 0.01818181818181818, 0.020606060606060607, 	0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,   1.0,      0 },// 0
	    { 0.019157088122605363, 0.02375478927203065, 0.02375478927203065, 0.017624521072796936,  	0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,   1.0,      0 },// 0
	    	    	
	    { 0.003205128205128205, 0.019230769230769232, 0.017628205128205128, 0.0,  			0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0,  	0.0,	1 },// 1	    
	    { 0.004273504273504274, 0.03205128205128205,  0.038461538461538464, 0.0, 			0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 	0.0,	1 },// 1	    
	    { 0.0, 		    0.030107526881720432, 0.030107526881720432, 0.002150537634408602, 	0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 	0.0, 	1 },// 1	    
	    { 0.002699055330634278, 0.033738191632928474, 0.021592442645074223, 0.004048582995951417,   0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 	0.0, 	1 },// 1	    
	    { 0.008, 		    0.036,                0.048,                0.0, 			0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 	0.0, 	1 },// 1	    
	    
	    { 0.012711864406779662, 0.017890772128060263, 0.01694915254237288, 0.011299435028248588, 	0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 	0.0,    2 },// 2
	    { 0.008868243243243243, 0.016047297297297296, 0.016891891891891893, 0.009712837837837838, 	1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 	0.0,    2 },// 2
	    
	    { 0.0014347202295552368, 0.024390243902439025, 0.009086561453849833, 0.01482544237207078, 	0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 	0.0,         3 },// 3
	    { 0.002649708532061473, 0.024377318494965553, 0.013248542660307366, 0.018018018018018018, 	0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 	0.0,         3 },// 3
	    { 0.010835913312693499, 0.010061919504643963, 0.011222910216718266, 0.016640866873065017, 	1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 	0.0,         3 },// 3
	    
	    { 0.014627659574468085, 0.014627659574468085, 0.009308510638297872, 0.017287234042553192, 	1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 	0.0,        4 },// 4
	    { 0.009129640900791236, 0.004564820450395618, 0.009738283627510651, 0.013694461351186854, 	0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 	0.0,        4 },// 4
	    
	    { 0.008479067302596715, 0.013248542660307366, 0.01483836777954425, 0.0042395336512983575, 	0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 	0.0,        5},// 5
	    { 0.027412280701754384, 0.01864035087719298, 0.003289473684210526, 0.01864035087719298,   	0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 	0.0,        5},// 5
	    { 0.02631578947368421, 0.018947368421052633, 0.011578947368421053, 0.017894736842105262,  	0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 	0.0,        5},// 5
	    { 0.022519352568613652, 0.01618578465869106, 0.0063335679099225895, 0.019000703729767768, 	0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 1.0, 0.0, 	0.0,        5},// 5
	    
	    { 0.014945652173913044, 0.0, 		  0.02717391304347826, 0.02377717391304348,   	0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 	1.0,        6 },// 6
	    { 0.010598834128245893, 0.013248542660307366, 0.02278749337572867, 0.0042395336512983575, 	0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 	1.0,        6 },// 6
	    
	    { 0.012195121951219513, 0.010365853658536586, 0.007317073170731708, 0.023170731707317073, 	1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 1.0, 0.0, 	0.0,       7 },//7
	    { 0.011486486486486487, 0.011486486486486487, 0.008783783783783784, 0.024324324324324326, 	1.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 	0.0,       7 },//7
	    { 0.013477088948787063, 0.013926325247079964, 0.0035938903863432167, 0.01931716082659479, 	1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 2.0, 	0.0,       7 },//7
	    { 0.004597701149425287, 0.01264367816091954, 0.020114942528735632, 0.008045977011494253, 	0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 2.0, 0.0, 0.0, 	0.0,        7 },//7
	    
	    { 0.018823529411764704, 0.023529411764705882, 0.018823529411764704, 0.01647058823529412, 	0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 	2.0,        8 },// 8
	    { 0.018823529411764704, 0.023529411764705882, 0.018823529411764704, 0.01647058823529412, 	0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 	2.0,        8 },// 8
	    
	    { 0.017825311942959002, 0.0196078431372549, 0.014854426619132501, 0.020202020202020204, 	0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 	1.0,        9 }, // 9
	    { 0.018880208333333332, 0.020833333333333332, 0.020182291666666668, 0.016276041666666668, 	0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 	1.0,        9 } // 9
    };

    private static int mQuantityEtalons = mPatterns.length;
    private static int mQuantitySigns = 14;
    private static double[] mNormFactors;
    
    public static void main(String[] args){
	double[][] m = {
		{0.5, 3},
		{0.1, 1},
		{0.8, 0},
		{0.3, 9},
	};
	bubblesort(m);
	for (double[] ds : m) {
	    System.out.println(Arrays.toString(ds));
        }
    }
    
    static {
	normMatrix();
    }
    
    private static final void normMatrix() {
	mNormFactors = new double[mQuantitySigns];
	for (int k = 0; k < mQuantitySigns; ++k) {
	    mNormFactors[k] = mPatterns[0][k];
	}
	for (int j = 0; j < mQuantitySigns; ++j) {
	    for (int i = 0; i < mQuantityEtalons; ++i) {
		if (mPatterns[i][j] > mNormFactors[j]) {
		    mNormFactors[j] = mPatterns[i][j];
		}
	    }
	}
	for (int j = 0; j < mQuantitySigns; ++j) {
	    for (int i = 0; i < mQuantityEtalons; ++i) {
		if (mNormFactors[j] == 0) continue;
		mPatterns[i][j] /= mNormFactors[j];
	    }
	}
    }

    private static double[] normVector(double[] vector) {
	for (int k = 0; k < mQuantitySigns; ++k) {
	    if (mNormFactors[k] == 0.0) continue;
	    vector[k] /= mNormFactors[k];
	}
	return vector;
    }
    
    public static BufferedImage[] recognize(BufferedImage[] digits) {
	BufferedImage[] result = new BufferedImage[digits.length];
	for (int k = 0; k < digits.length; ++k) {
	    System.out
		    .println("------------------------------------------------------");
	    double[] a = defineSigns(digits[k]);
	    a = normVector(a);
	    int digit = 0;
	    ContourWorker cw = new ContourWorker(digits[k]);
	    Point[] centers = cw.getCentersOfContours();
	    if (centers.length == 2) {
		digit = 8;
	    } else if (centers.length == 1) {
		int h = digits[k].getHeight();
		int q = countSingletons(digits[k], null);
		if (centers[0].y < h / 2 && q == 1) {
		    digit = 9;
		} else if (centers[0].y >= h / 2 && q == 1) {
		    digit = 6;
		} else if (q == 0) {
		    digit = 0;
		}
	    } else {
		System.out.println("norm=" + Arrays.toString(a));
		double[][] d = countDistances(a);
		digit = recognize(d);
	    }
	    System.out.println("digit==========" + digit);
	    result[k] = createDigit(digit, digits[k]);
	    System.out
		    .println("-------------------------------------------------");
	}
	return result;
    }

    private static void swap(double[][] arr, int i, int j) {
	double[] row = arr[i];
	arr[i] = arr[j];
	arr[i] = arr[j];
	arr[j] = row;
    }

    private static void bubblesort(double[][] d) {
	for (int i = d.length - 1; i >= 0; i--) {
	    for (int j = 0; j < i; j++) {
		if (d[j][0] > d[j + 1][0]) swap(d, j, j + 1);
	    }
	}
    }
    
    
    private static int recognize(double[][] d) {
	bubblesort(d);
	int[] quantities = new int[10];
	for (int k = 0; k < 5; ++k) {
	    ++quantities[(int)d[k][1]];
	}
//	int max = quantities[0];
//	int digit = 0;
//	for(int k = 0; k < quantities.length; ++k){
//	    if(quantities[k] > max) {
//		max = quantities[k];
//		digit = k;
//	    }
//	}
	int digit = (int)d[0][1];
	System.out.println("================"+Arrays.toString(quantities));
	return digit;
    }
    
    private static final int countSingletons(BufferedImage digit,
	    List<Point> list) {
	int h = digit.getHeight();
	int w = digit.getWidth();
	int[] pixels = digit.getRGB(0, 0, w, h, null, 0, w);
	int quantity = 0;
	for (int k = 0; k < pixels.length; ++k) {
	    if (pixels[k] == Tool._1) {
		int x = k % w;
		int y = k / w;
		if (countNeighborsForPixel(pixels, w, x, y) == 1) {
		    list.add(new Point(x, y));
		    ++quantity;
		}
	    }
	}
	return quantity;
    }

    private static int countNeighborsForPixel(int[] pixels, int offset, int x,
	    int y) {
	int quantity = 0;
	for (int i = y - 1; i <= y + 1; ++i) {
	    for (int j = x - 1; j <= x + 1; ++j) {
		if (i == y && j == x) continue;
		int index = i * offset + j;
		if (index < 0 || index >= pixels.length) continue;
		if (pixels[index] == Tool._1) {
		    ++quantity;
		}
	    }
	}
	return quantity;
    }
    
    private static final BufferedImage createDigit(int digit,
	    BufferedImage original) {
	int type = original.getType();
	BufferedImage answer = new BufferedImage(50, 100, type);
	Graphics g = answer.getGraphics();
	g.setFont(new Font("TimesRoman", Font.PLAIN, 80));
	g.setColor(Color.red);
	g.drawString(Integer.toString(digit), 0, 80);
	return answer;
    }

    private static double[][] countDistances(double[] a) {
	double[][] distances = new double[mQuantityEtalons][2];
	for (int y = 0; y < mQuantityEtalons; ++y) {
	    for (int x = 0; x < mQuantitySigns; ++x) {
		distances[y][0] += Math.pow(a[x] - mPatterns[y][x], 2);
	    }
	    distances[y][0] = Math.sqrt(distances[y][0]);
	    distances[y][1] = mPatterns[y][mQuantitySigns];
	}
	return distances;
    }

    private static double[] defineSigns(BufferedImage digit) {
	double[] answer = new double[mQuantitySigns];
	int h = digit.getHeight();
	int w = digit.getWidth();
	int[] pixels = digit.getRGB(0, 0, w, h, null, 0, w);
	for (int y = 0; y < h / 2; ++y)
	    for (int x = 0; x < w / 2; ++x) {
		if (pixels[y * w + x] == Tool._1) ++answer[0];
		pixels[y * w + x] = Color.GREEN.getRGB();
	    }

	for (int y = 0; y < h / 2; ++y)
	    for (int x = w / 2; x < w; ++x) {
		if (pixels[y * w + x] == Tool._1) ++answer[1];
		pixels[y * w + x] = Color.RED.getRGB();
	    }

	for (int y = h / 2; y < h; ++y)
	    for (int x = 0; x < w / 2; ++x) {
		if (pixels[y * w + x] == Tool._1) ++answer[2];
		pixels[y * w + x] = Color.BLUE.getRGB();
	    }

	for (int y = h / 2; y < h; ++y)
	    for (int x = w / 2; x < w; ++x) {
		if (pixels[y * w + x] == Tool._1) ++answer[3];
		pixels[y * w + x] = Color.YELLOW.getRGB();
	    }

	for (int k = 0; k < 4; ++k)
	    answer[k] /= w * h;

	List<Point> list = new ArrayList<Point>();
	int n = countSingletons(digit, list);

	int dx = w / 3;
	int dy = h / 3;
	for (Point p : list) {
	    int i = p.y / dy;
	    int j = p.x / dx;
	    int k = i * 3 + j;
	    if (k >= 9) k = 8;
	    if (k < 0) k = 0;
	    ++answer[4 + k];
	}
	
	ContourWorker cw = new ContourWorker(digit);
	cw.drawResultOn(digit);
	
	answer[13] = cw.getCentersOfContours().length;
	
	System.out.println("signs vector not norm =" + n + "  " + Arrays.toString(answer));
	
	return answer;
    }
}
