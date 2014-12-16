/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.ipt.segment;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import by.slesh.ri.cp.ushkindaria.ipt.Tool;

/**
 * @author slesh
 *
 */
public class RelationSegmentator {
    private int mWidth;
    private int mHeight;
    private int[] mPixelData;
    private List<Digit> mDigits = new ArrayList<Digit>();

    public RelationSegmentator(BufferedImage source) {
	mWidth = source.getWidth();
	mHeight = source.getHeight();
	mPixelData = source.getRGB(0, 0, mWidth, mHeight, null, 0, mWidth);
    }

    private Digit mCurrentDigit;
    private int mQuantityPixels;

    public BufferedImage[] segment() {
	for (int x = 0; x < mWidth; ++x) {
	    int index = mWidth * (mHeight / 2 + 10) + x;
	    if (mPixelData[index] == Tool._1) {
		mCurrentDigit = new Digit(createColor());
		mQuantityPixels = 0;
		mark(index);
		if (mQuantityPixels > 20) mDigits.add(mCurrentDigit);
	    }
	}
	BufferedImage[] digits = new BufferedImage[mDigits.size()];
	for (int k = 0; k < digits.length; ++k) {
	    digits[k] = mDigits.get(k).toImage();
	}
	return digits;
    }

    private void mark(int index) {
	if (index < 0 || index >= mPixelData.length) return;
	if (mPixelData[index] == Tool._0) return;
	if (mPixelData[index] == mCurrentDigit.getColor().getRGB()) return;
	for (Digit digit : mDigits) {
	    if (mPixelData[index] == digit.getColor().getRGB()) return;
	}
	mPixelData[index] = mCurrentDigit.getColor().getRGB();
	mCurrentDigit.addPoint(new Point(index % mWidth, index / mWidth));
	++mQuantityPixels;

	mark(index - mWidth);
	mark(index + mWidth);

	mark(index - 1);
	mark(index + 1);

	mark(index - mWidth - 1);
	mark(index - mWidth + 1);

	mark(index + mWidth - 1);
	mark(index + mWidth + 1);
    }

    private static final Random GENERATOR = new Random();

    private static final Color createColor() {
	int r = GENERATOR.nextInt(200);
	int g = GENERATOR.nextInt(200);
	int b = GENERATOR.nextInt(200);
	return new Color(r, g, b);
    }
}
