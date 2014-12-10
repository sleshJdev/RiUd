/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.ipt.segment;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import by.slesh.ri.cp.ushkindaria.ipt.Tool;

/**
 * @author slesh
 *
 */
public class Digit {
    private List<Point> mPoints = new ArrayList<Point>();

    private int mXMin = Integer.MAX_VALUE;
    private int mXMax = Integer.MIN_VALUE;

    private int mYMin = Integer.MAX_VALUE;
    private int mYMax = Integer.MIN_VALUE;

    private Color mColor;

    public Digit(Color c) {
	mColor = c;
    }

    public void addPoint(Point p) {
	if (p.x < mXMin) mXMin = p.x;
	if (p.x > mXMax) mXMax = p.x;
	if (p.y < mYMin) mYMin = p.y;
	if (p.y > mYMax) mYMax = p.y;
	mPoints.add(p);
    }

    public BufferedImage toImage() {
	int h = mYMax - mYMin + 1 + 4;
	int w = mXMax - mXMin + 1 + 4;
	BufferedImage digit = new BufferedImage(w, h, Tool.IMAGE_TYPE);
	digit.getGraphics().setColor(Color.WHITE);
	digit.getGraphics().fillRect(0, 0, w, h);
	for (Point point : mPoints) {
	    digit.setRGB(point.x - mXMin + 2, point.y - mYMin + 2, mColor.getRGB());
	}
	return digit;
    }

    public Color getColor() {
	return mColor;
    }
}
