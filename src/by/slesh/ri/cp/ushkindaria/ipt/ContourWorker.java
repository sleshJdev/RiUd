/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.ipt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author slesh
 *
 */
public class ContourWorker {
    private int[] mPixelsData;
    private int mHeight;
    private int mWidth;

    public ContourWorker(BufferedImage source) {
	mHeight = source.getHeight();
	mWidth = source.getWidth();
	mPixelsData = source.getRGB(0, 0, mWidth, mHeight, null, 0, mWidth);
	allocateContour(source);
	findCentersOfContours(source);
    }

    public void drawResultOn(BufferedImage source) {
	source.setRGB(0, 0, mWidth, mHeight, mPixelsData, 0, mWidth);
    }

    public Point[] getCentersOfContours() {
	Point[] centers = new Point[mContourCenters.size()];
	mContourCenters.toArray(centers);
	return centers;
    }

    private int mAllocateColor = Color.RED.getRGB();
    private int mFillColor;

    private static final Random GENERATOR = new Random();

    private static final Color createColor() {
	int r = GENERATOR.nextInt(200);
	int g = GENERATOR.nextInt(200);
	int b = GENERATOR.nextInt(200);
	return new Color(r, g, b);
    }

    private void fill(int k) {
	if (k < 0 || k >= mPixelsData.length) return;
	if (mPixelsData[k] != Tool._0) return;
	mPixelsData[k] = mFillColor;

	mCollector.translate(k % mWidth, k / mWidth);
	++mContourLength;
	
	fill(k - 1);
	fill(k + 1);

	fill(k - mWidth);
	fill(k + mWidth);

	// fill(k - mWidth - 1);
	// fill(k - mWidth + 1);
	//
	// fill(k + mWidth - 1);
	// fill(k + mWidth + 1);
    }

    private List<Point> mContourCenters = new ArrayList<Point>();
    private Point mCollector;
    private int mContourLength;

    int counter = 0;

    private void findCentersOfContours(BufferedImage source) {
	mHeight = source.getHeight();
	mWidth = source.getWidth();
	mPixelsData = source.getRGB(0, 0, mWidth, mHeight, null, 0, mWidth);
	for (int k = 0; k < mPixelsData.length; ++k) {
	    if (mPixelsData[k] == Color.RED.getRGB()) {
		int index = k;
		if ((index = isNext(index + 1, Tool._1)) != -1) {
		    if ((index = isNext(index + 1, Tool._0)) != -1) {
			int inner = index;
			if ((index = isNext(index + 1, Tool._1)) != -1) {
			    mCollector = new Point();
			    mContourLength = 0;
			    mFillColor = createColor().getRGB();
			    fill(inner);
			    if (mContourLength > 0) {
				mCollector.setLocation(mCollector.x
				        / mContourLength, mCollector.y
				        / mContourLength);
				mContourCenters.add(mCollector);
				Graphics g = source.getGraphics();
				g.setColor(Color.BLACK);
				g.fillOval(mCollector.x - 2, mCollector.y, 4, 4);
			    }
			}
		    }
		}
	    }
	}
    }

    private int isNext(int start, int nextColor) {
	int offset = mWidth - start % mWidth;
	for (int t = 0, k = start; k < mPixelsData.length && t < offset
	        && mPixelsData[k] != Color.RED.getRGB()
	        && mPixelsData[k] != mFillColor; ++k, ++t) {
	    if (mPixelsData[k] == nextColor) {
		return k;
	    }
	}
	return -1;
    }

    private static Point findEntryPoint(BufferedImage source) {
	int h = source.getHeight();
	int w = source.getWidth();
	int[] pixels = source.getRGB(0, 0, w, h, null, 0, w);
	for (int k = 0, y = 0; y < h; ++y)
	    for (int x = 0; x < w; ++x, ++k) {
		if (pixels[k] == Tool._1) return new Point(x - 1, y);
	    }

	return null;
    }

    private void allocateContour(BufferedImage source) {
	Point entry = findEntryPoint(source);
	int h = source.getHeight();
	int w = source.getWidth();
	Point next = null;
	Point curr = new Point(entry);
	Point old = new Point(entry);
	old.translate(0, 1);
	Point[] offsets;
	do {
	    offsets = getOffset(old, curr);
	    for (Point offset : offsets) {
		next = new Point(curr);
		next.translate(offset.x, offset.y);
		if (next.x < 0 || next.x >= w || next.y < 0 || next.y >= h) {
		    return;
		}
		int index = mWidth * next.y + next.x;
		if (mPixelsData[index] == Tool._0
		        || mPixelsData[index] == mAllocateColor) {
		    source.setRGB(next.x, next.y, mAllocateColor);
		    old = curr;
		    curr = next;
		    break;
		}
	    }
	} while (!entry.equals(curr));
    }

    private static Point[] getOffset(Point prev, Point curr) {
	if (curr.x - prev.x == 1) {
	    // from left
	    return new Point[] { new Point(0, 1), new Point(1, 0),
		    new Point(0, -1), new Point(-1, 0) };

	} else if (curr.x - prev.x == -1) {
	    // from right
	    return new Point[] { new Point(0, -1), new Point(-1, 0),
		    new Point(0, 1), new Point(1, 0) };
	} else if (curr.y - prev.y == 1) {
	    // from top
	    return new Point[] { new Point(-1, 0), new Point(0, 1),
		    new Point(1, 0), new Point(0, -1) };
	} else if (curr.y - prev.y == -1) {
	    // from bottom
	    return new Point[] { new Point(1, 0), new Point(0, -1),
		    new Point(-1, 0), new Point(0, 1) };
	}
	return null;
    }
}
