package by.slesh.ri.cp.ushkindaria.ipt;

import java.awt.image.BufferedImage;

public class PercentsBinarizator extends AbstractBinarizator {
	private int mPercents = 40;
	
	@Override
	public BufferedImage binarization(BufferedImage source) {
		return super.binarization(source, findThreshold(source));
	}

	/*
	 * Find threshold value for method 40 percents
	 */
	private int findThreshold(BufferedImage source) {
		double limit = mPercents / 100.0;
		double size = source.getWidth() * source.getHeight();
		int[] repeats = IptUtil.countBrightnessRepeats(source);
		double collector = 0;
		for (int brighness = 0; brighness < 256; ++brighness) {
			collector += repeats[brighness];
			double ratio = collector / size;
			if (ratio > limit) return --brighness;
		}
		return 0;
	}

	public void setmPercents(int value) {
		this.mPercents = value;
	}

}
