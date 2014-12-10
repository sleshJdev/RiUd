/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.ipt;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import by.slesh.ri.cp.ushkindaria.ipt.segment.BugSegmentator;
import by.slesh.ri.cp.ushkindaria.ipt.segment.Contour;

/**
 * @author slesh
 *
 */
public class Recognizer {
    
    public static void main(String[] args) {
	try {
	    BufferedImage image = ImageIO.read(new File("d:\\image5.png"));
	    int h = image.getHeight();
	    int w = image.getWidth();
	    BufferedImage newi = new BufferedImage(w + 2, h + 1, image.getType());
	    newi.getGraphics().drawImage(image, 1, 1, null);
	    BugSegmentator bug = new BugSegmentator();
	    bug.setFrom(new Point(w - 1,2));
	    Contour contour = bug.segment(newi);
	    contour.drawOnImage(newi);
	    ImageIO.write(newi, "png", new File("d:\\www.png"));
//	    int[] pixels = image.getRGB(0, 0, w, h, null, 0, w);
//	    Point max = new Point();
//	    int maxN = 0;
//	    for (int y = 0; y < h; ++y) {
//		for (int x = 0; x < w; ++x) {
//		    if (image.getRGB(x, y) == Tool._0) {
//			int x1 = 0;
//			int y1 = 0;
//			int x2 = 0;
//			int y2 = 0;
//			boolean bottom = false;
//			for(int i = y; i < h; ++i){
//			    if(image.getRGB(x, i) == Tool._1){
//				y2 = y;
//				bottom = true;
//				break;
//			    }
//			}
//			boolean top = false;
//			for(int i = y; i >= 0; --i){
//			    if(image.getRGB(x, i) == Tool._1){
//				y1 = i;
//				top = true;
//				break;
//			    }
//			}
//			boolean right = false;
//			for(int i = x; i < w; ++i){
//			    if(image.getRGB(i, y) == Tool._1){
//				x2 = i;
//				right = true;
//				break;
//			    }
//			}
//			boolean left = false;
//			for(int i = x; i >= 0; --i){
//			    if(image.getRGB(i, y) == Tool._1){
//				x1 = i;
//				left = true;
//				break;
//			    }
//			}
//			if(top&&bottom&&left&&right){
////			    image.setRGB(x, y, Color.GREEN.getRGB());
//			    BugSegmentator bug = new BugSegmentator();
//			    bug.setFrom(new Point(x,y));
//			    Contour contour = bug.segment(image);
//			    contour.drawOnImage(image);
//			    ImageIO.write(image, "png", new File("d:\\www.png"));
//			    y = h;
//			    x = w;
//			    if(contour.isClosed() &&
//				    contour.getMinX() >= x1 &&
//				    contour.getMaxX() <= x2 &&
//				    contour.getMinY() >= y1 &&
//				    contour.getMaxY() <= y2){
//				contour.drawOnImage(image);
//			    }
//			}
//		    }
//		}
//	    }
//	    ImageIO.write(image, "png", new File("d:\\www.png"));
	} catch (IOException e) {
	    
	}
    }
    
    static int check = 0;
    static int num = 0;
    
    private static void mark(BufferedImage im, int x, int y, int prevX, int prevY) {
	if (++check > 15) {
	    if (im.getRGB(prevX, prevY) == Color.RED.getRGB()
		    && im.getRGB(x, y) == Color.GREEN.getRGB()) return;
	}
	for (int i = y - 1; i < y + 1; ++i) {
	    for (int j = x - 1; j < x + 1; ++j) {
		if(i == y && j == x) continue;
		if(i < 0 || i >= im.getHeight() || j < 0 || j >= im.getWidth()) continue;
		if (im.getRGB(j, i) == Tool._1) {
		    mark(im, j, i, x, y);
		}
	    }
	}
    }
    
    private static double[][] mPatterns = {
	    { 0.16603508771929826, 0.06792158458825126, 0.04617052681699146, 0.0863737822558775, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,      0 },// 0 
	    { 0.05022910216718267, 0.3018575851393189, 0.4440634949632027, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0,                       1 },// 1 
	    { 0.10878010878010878, 0.08983131563776725, 0.05726934415929632, 0.09657009657009658, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0,     2 },// 2 
	    { 0.03076923076923077, 0.09743589743589744, 0.038461538461538464, 0.07692307692307693, 1, 1, 0, 0, 0, 0, 1, 0, 0,                      3 },// 3 
	    { 0.08, 0.08, 0.0725, 0.0325, 1, 0, 0, 1, 0, 0, 0, 0, 1,                                                                               4 },// 4
	    { 0.05263157894736842, 0.05263157894736842, 0.038011695906432746, 0.07894736842105263, 0, 0, 1, 0, 0, 0, 1, 0, 0,                      5 },// 5 
	    { 0.11153376537991921, 0.04835528408729401, 0.07434343434343434, 0.2143997638503133, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,      6},// 6
	    { 0.010432190760059613, 0.02533532041728763, 0.01639344262295082, 0.0067064083457526085, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0,  7 },// 
	    { 0.10065691883873702, 0.09018859927950837, 0.08515702479338842, 0.18014712560167107, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,     8 },// 8 
	    { 0.10415887966908376, 0.08552635280745881, 0.02427540713254999, 0.20398907862464713, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0,     9 } // 9 
    };			

    private static double[] mNormFactors;

    static {
	normMatrix();
	for (int k = 0; k < mPatterns.length; ++k) {
	    for(int a = 0; a < mPatterns[k].length; ++a){
		System.out.print(mPatterns[k][a]+"   ");
	    }
	    System.out.println("-------------");
	}
    }

    private static final void normMatrix() {
	int h = mPatterns.length;// height
	int w = mPatterns[0].length - 1;// width
	mNormFactors = new double[w];
	for (int k = 0; k < w; ++k) {
	    mNormFactors[k] = mPatterns[0][k];
	}
	for (int j = 0; j < w; ++j) {
	    for (int i = 0; i < h; ++i) {
		if (mPatterns[i][j] > mNormFactors[j]) {
		    mNormFactors[j] = mPatterns[i][j];
		}
	    }
	}
	for (int j = 0; j < w; ++j) {
	    for (int i = 0; i < h; ++i) {
		if(mNormFactors[j] == 0) continue;
		mPatterns[i][j] /= mNormFactors[j];
	    }
	}
    }

    private static void normvector(double[] vector) {
	for (int k = 0; k < vector.length; ++k) {
	    if(mNormFactors[k] == 0) continue;
	    vector[k] /= mNormFactors[k];
	}
    }

    public static BufferedImage[] recognize(BufferedImage[] digits) {
	BufferedImage[] result = new BufferedImage[digits.length];
	for (int k = 0; k < digits.length; ++k) {
	    double[] a = defineSigns(digits[k]);
	    normvector(a);
	    System.out.println("norm=" + Arrays.toString(a));
	    double[] d = countDistances(a);
	    int digit = recognize(d);
	    System.out.println("digit=" + digit);
	    result[k] = createDigit(digit, digits[k]);
	    System.out.println("----------------------");
	}
	return result;
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

    private static int countNeighborsForPixel(int[] pixels, int offset, int x, int y) {
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

    private static int recognize(double[] d) {
	double min = d[0];
	int digit = 1;
	for (int k = 0; k < d.length; ++k) {
	    if (d[k] < min) {
		min = d[k];
		digit = k;
	    }
	}
	return digit;
    }

    private static double[] countDistances(double[] a) {
	double[] distances = new double[10];
	for (int y = 0; y < 10; ++y) {
	    for (int x = 0; x < a.length; ++x) {
		distances[y] += Math.pow(a[x] - mPatterns[y][x], 2);
	    }
	    distances[y] = Math.sqrt(distances[y]);
	}
	return distances;
    }

    private static double[] defineSigns(BufferedImage digit) {
	double[] answer = new double[4+9];
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

	for (int k = 0; k < answer.length; ++k)
	    answer[k] /= w * h;

	List<Point> list = new ArrayList<Point>();
	int n = countSingletons(digit, list);

	int[] a = new int[9];

	int dx = w / 3;
	int dy = h / 3;
//	System.out.println("dx=" + dx + " dy=" + dy);
	for (Point p : list) {
	    int i = p.y / dy;
	    int j = p.x / dx;
	    System.out.println("x=" + p.x + " y=" + p.y + " i=" + i + " j=" + j
		    + " n=" + i * 3 + j);
	    int k = i * 3 + j;
	    if (k >= 9) k = 8;
	    if (k < 0) k = 0;
	    ++answer[4 + k];
	}

	System.out.println("total =" + n + "  " + Arrays.toString(answer));

	return answer;
    }
}
