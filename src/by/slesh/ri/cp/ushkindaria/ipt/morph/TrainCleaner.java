/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.ipt.morph;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import by.slesh.ri.cp.ushkindaria.ipt.Tool;

/**
 * @author slesh
 *
 */
public class TrainCleaner {
    private static final int LIMIT = 30;

    private static int mWidth;
    private static int mHeight;
    private static int[] mPixelsData;
    private static int mQuantityPixels;
    private static List<Point> mNeighbors = new ArrayList<Point>();
    private static List<Point> mTrain = new ArrayList<Point>();

    public static BufferedImage remove(BufferedImage source) {
	mWidth = source.getWidth();
	mHeight = source.getHeight();
	mPixelsData = source.getRGB(0, 0, mWidth, mHeight, null, 0, mWidth);
	for (int y = 1; y < mHeight - 1; ++y) {
	    for (int x = 1; x < mWidth - 1; ++x) {
		int index = mWidth * y + x;
		if (mPixelsData[index] == Tool._1) {
		    mNeighbors.clear();
		    if (countNeighbors(x, y) == 4) {
			mPixelsData[index] = Tool._0;
			for (Point neighbor : mNeighbors) {
			    mQuantityPixels = 0;
			    int pos = neighbor.y * mWidth + neighbor.x;
			    mPixelsData[pos] = Tool._0;
			    mTrain.clear();
			    mark(x, y);
			    if (mQuantityPixels > LIMIT) {
				mPixelsData[pos] = Tool._1;
				for (Point p : mTrain) {
				    int k = mWidth * p.y + p.x;
				    mPixelsData[k] = Tool._1;
				}
			    } else {
				for (Point p : mTrain) {
				    int k = mWidth * p.y + p.x;
				    mPixelsData[k] = Tool._0;
				}
			    }
			}
			mPixelsData[index] = Tool._1;
		    }
		}
	    }
	}
	source.setRGB(0, 0, mWidth, mHeight, mPixelsData, 0, mWidth);
	return source;
    }

    private static int countNeighbors(int x, int y) {
	int quantity = 0;
	for (int i = y - 1; i <= y + 1; ++i) {
	    for (int j = x - 1; j <= x + 1; ++j) {
		if (i == y && j == x) continue;
		if (mPixelsData[i * mWidth + j] == Tool._1) {
		    mNeighbors.add(new Point(i, j));
		    ++quantity;
		}
	    }
	}
	return quantity;
    }

    private static void mark(int x, int y) {
	if (x < 0 || x >= mWidth || y < 0 || y >= mHeight) return;
	if (mPixelsData[mWidth * y + x] == Tool._05) return;
	if (++mQuantityPixels > LIMIT) return;
	mPixelsData[mWidth * y + x] = Tool._05;
	mTrain.add(new Point(x, y));

	for (int i = y - 1; i <= y + 1; ++i) {
	    for (int j = x - 1; j <= x + 1; ++j) {
		if (i == y && j == x) continue;
		if (mPixelsData[i * mWidth + j] == Tool._1) {
		    mark(i, j);
		}
	    }
	}
    }
}
