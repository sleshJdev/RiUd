package by.slesh.ri.cp.ushkindaria.ipt;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/*
 * Implements Zhang-Suen thinning algorithm
 */
public class Skeletonizator extends Tool {
    private static final int[][] mOffsets = { { 0, -1 }, { 1, -1 }, { 1, 0 },
	    { 1, 1 }, { 0, 1 }, { -1, 1 }, { -1, 0 }, { -1, -1 }, { 0, -1 } };

    private static final int[][][] mCheckGroups = {
	    { { 0, 2, 4 }, { 2, 4, 6 } }, { { 0, 2, 6 }, { 0, 4, 6 } } };

    private static List<Point> toWhite = new ArrayList<>();
    private int[] sourceRgb;
    private int w;
    private int h;

    public BufferedImage skeletonization(BufferedImage source) {
	w = source.getWidth();
	h = source.getHeight();
	sourceRgb = source.getRGB(0, 0, w, h, null, 0, w);
	boolean firstStep = false;
	boolean hasChanged;
	do {
	    hasChanged = false;
	    firstStep = !firstStep;
	    for (int y = 1; y < h - 1; y++) {
		for (int x = 1; x < w - 1; x++) {
		    if (sourceRgb[w * y + x] != _1) {
			continue;
		    }
		    int n = countBlackNeighbors(y, x);
		    if (n < 2 || n > 6) {
			continue;
		    }
		    if (countTransitionsWhiteBlack(y, x) != 1) {
			continue;
		    }
		    if (!atLeastOneIsWhite(y, x, firstStep ? 0 : 1)) {
			continue;
		    }
		    toWhite.add(new Point(x, y));
		    hasChanged = true;
		}
	    }
	    for (Point p : toWhite)
		sourceRgb[w * p.y + p.x] = _0;
	    toWhite.clear();
	} while (hasChanged || firstStep);
	BufferedImage taget = new BufferedImage(w, h, source.getType());
	taget.setRGB(0, 0, w, h, sourceRgb, 0, w);
	return taget;
    }

    private int countBlackNeighbors(int y, int x) {
	int count = 0;
	for (int i = 0; i < mOffsets.length - 1; i++) {
	    int posY = y + mOffsets[i][1];
	    int posX = x + mOffsets[i][0];
	    int pos = w * posY + posX;
	    if (sourceRgb[pos] == _1) {
		++count;
	    }
	}
	return count;
    }

    private int countTransitionsWhiteBlack(int y, int x) {
	int count = 0;
	for (int i = 0; i < mOffsets.length - 1; i++) {
	    int posY = y + mOffsets[i][1];
	    int posX = x + mOffsets[i][0];
	    int pos = w * posY + posX;
	    if (sourceRgb[pos] == _0) {
		posY = y + mOffsets[i + 1][1];
		posX = x + mOffsets[i + 1][0];
		pos = w * posY + posX;
		if (sourceRgb[pos] == _1) {
		    count++;
		}
	    }
	}
	return count;
    }

    private boolean atLeastOneIsWhite(int y, int x, int step) {
	int count = 0;
	int[][] group = mCheckGroups[step];
	for (int i = 0; i < 2; i++) {
	    for (int j = 0; j < group[i].length; j++) {
		int[] nbr = mOffsets[group[i][j]];
		int posY = y + nbr[1];
		int posX = x + nbr[0];
		int pos = w * posY + posX;
		if (sourceRgb[pos] == _0) {
		    count++;
		    break;
		}
	    }
	}
	return count > 1;
    }
}
