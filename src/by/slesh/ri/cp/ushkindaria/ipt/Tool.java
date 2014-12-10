package by.slesh.ri.cp.ushkindaria.ipt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tool {

    public static final int IMAGE_TYPE = BufferedImage.TYPE_4BYTE_ABGR;

    public static final int _0 = Color.WHITE.getRGB();
    public static final int _1 = Color.BLACK.getRGB();
    public static final int _05 = Color.GRAY.getRGB();

    public static BufferedImage rgbToImage(int[] rgb, int w, int h) {

	BufferedImage target = new BufferedImage(w, h, IMAGE_TYPE);
	target.setRGB(0, 0, w, h, rgb, 0, w);
	return target;
    }

    public static BufferedImage centrain(BufferedImage source) {
	int h = source.getHeight();
	int w = source.getWidth();
	int[] sourcePixels = source.getRGB(0, 0, w, h, null, 0, w);
	Point center = new Point();
	int n = 0;
	for (int y = 0; y < h; ++y) {
	    for (int x = 0; x < w; ++x) {
		if (sourcePixels[w * y + x] == Tool._1) {
		    center.translate(x, y);
		    ++n;
		}
	    }
	}
	if(n == 0) return source;
	center.setLocation(center.x / n, center.y / n);
	int dx = w / 2 - center.x;
	int dy = h / 2 - center.y;
	int[] targetPixels = new int[sourcePixels.length];
	Arrays.fill(targetPixels, Tool._0);
	for (int k = 0, y = 0; y < h; ++y) {
	    for (int x = 0; x < w; ++x, ++k) {
		int x1 = x + dx;
		int y1 = y + dy;

		if (x1 < 0) x1 = 0;
		if (y1 < 0) y1 = 0;

		if (x1 >= w) x1 = w - 1;
		if (y1 >= h) y1 = h - 1;

		int k1 = w * y1 + x1;
		targetPixels[k1] = sourcePixels[k];
	    }
	}
	source.setRGB(0, 0, w, h, targetPixels, 0, w);
	return source;
    }

    public static BufferedImage centrain1(BufferedImage source) {
	int h = source.getHeight();
	int w = source.getWidth();
	int[] pixels = source.getRGB(0, 0, w, h, null, 0, w);
	int xMin = w - 1;
	int xMax = 0;
	int yMin = h - 1;
	int yMax = 0;
	for (int y = 0; y < h; ++y) {
	    for (int x = 0; x < w; ++x) {
		if (pixels[w * y + x] == Tool._1) {
		    if (x < xMin) xMin = x;
		    if (x > xMax) xMax = x;
		    if (y < yMin) yMin = y;
		    if (y > yMax) yMax = y;
		}
	    }
	}
	if (xMax < w - 1) ++xMax;
	if (yMax < h - 1) ++yMax;
	if(xMin < 0) xMin = 0;
	if(xMax >= w) xMax = w - 1;
	if(yMin < 0) yMin = 0;
	if(yMax >= h) yMax = h - 1;
	BufferedImage bi = cut(source, xMin, yMin, xMax, yMax);
	// bi = Resizer.scaleUp(bi, G.WIDTH_IMAGE, G.HEIGHT_IMAGE, true);
	return bi;
    }

    /**
     * @param source
     *            image to ratate
     * @param angle
     *            angel in degrees
     */
    public static BufferedImage rotate(BufferedImage source, double angle) {
	BufferedImage target = new BufferedImage(source.getWidth(null),
	        source.getHeight(null), source.getType());
	Graphics2D g = (Graphics2D) target.getGraphics();
	g.drawImage(source, 0, 0, null);
	AffineTransform at = new AffineTransform();
	at.scale(1.0, 1.0);
	at.rotate(angle, target.getWidth() / 2.0, target.getHeight() / 2.0);
	AffineTransform translationTransform = findTranslation(at, target);
	at.preConcatenate(translationTransform);
	BufferedImageOp bio = new AffineTransformOp(at,
	        AffineTransformOp.TYPE_BILINEAR);
	return bio.filter(target, null);
    }

    /*
     * Find proper translations to keep rotated image correctly displayed
     */
    public static AffineTransform findTranslation(AffineTransform at,
	    BufferedImage bi) {
	Point2D p2din, p2dout;

	p2din = new Point2D.Double(0.0, 0.0);
	p2dout = at.transform(p2din, null);
	double ytrans = p2dout.getY();

	p2din = new Point2D.Double(0, bi.getHeight());
	p2dout = at.transform(p2din, null);
	double xtrans = p2dout.getX();

	AffineTransform tat = new AffineTransform();
	tat.translate(-xtrans, -ytrans);
	return tat;
    }

    public static int readBrightness(int rgb) {

	return (rgb >> 0) & 0xff;
    }

    public static int getRgbFromValue(int value) {

	return brightnessToRgb(255, value, value, value);
    }

    public static int[] countBrightnessRepeats(BufferedImage source) {
	int[] rgb = source.getRGB(0, 0, source.getWidth(), source.getHeight(),
	        null, source.getWidth(), source.getWidth());
	int[] repeats = new int[256];
	for (int index = 0; index < rgb.length; ++index) {
	    ++repeats[rgbToBrightness(rgb[index])];
	}
	return repeats;
    }

    public static int rgbToBrightness(int rgb) {
	int r = (rgb >> 16) & 0xff;
	int g = (rgb >> 8) & 0xff;
	int b = rgb & 0xff;
	rgb = (r * 77 + g * 151 + b * 28) >> 8;
	return rgb;
    }

    public static int brightnessToRgb(int value) {
	return brightnessToRgb(255, value, value, value);
    }

    public static int brightnessToRgb(double a, double r, double g, double b) {
	return brightnessToRgb((int) a, (int) r, (int) g, (int) b);
    }

    public static int brightnessToRgb(int a, int r, int g, int b) {
	return ((a & 0xff) << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8)
	        | ((b & 0xff) << 0);
    }

    public static BufferedImage cut(BufferedImage source, int x1, int y1,
	    int x2, int y2) {

	BufferedImage target = new BufferedImage(x2 - x1, y2 - y1,
	        source.getType());
	Graphics g = target.getGraphics();
	g.drawImage(source, 0, 0, x2 - x1, y2 - y1, x1, y1, x2, y2, null);
	return target;
    }

    public static BufferedImage trim(BufferedImage source) {
	return trim(source, 40, 40, 40, 40);
    }

    public static BufferedImage trim(BufferedImage source, int indentTop,
	    int indentRight, int indentBottom, int indentLeft) {
	int x1 = indentLeft;
	int y1 = indentTop;
	int x2 = source.getWidth() - indentRight;
	int y2 = source.getHeight() - indentBottom;
	return cut(source, x1, y1, x2, y2);
    }
}
