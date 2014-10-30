package by.slesh.ri.cp.ushkindaria.model;

import java.awt.image.BufferedImage;

import by.slesh.ri.cp.ushkindaria.ipt.PercentsBinarizator;
import by.slesh.ri.cp.ushkindaria.ipt.Skeletonizator;

public class Model {
	private BufferedImage mSourceImage;
	private BufferedImage mTargetImage;
	private PercentsBinarizator mBinarizator = new PercentsBinarizator();
	private Skeletonizator mSkeletonizator = new Skeletonizator();

	public void binarization() {
		mTargetImage = mBinarizator.binarization(mSourceImage);
	}

	public void skeletonization() {
		mTargetImage = mSkeletonizator.skeletonization(mTargetImage);
	}

	public BufferedImage getmSourceImage() {
		return mSourceImage;
	}

	public void setmSourceImage(BufferedImage mSourceFrame) {
		this.mSourceImage = mSourceFrame;
	}

	public BufferedImage getmTargetImage() {
		return mTargetImage;
	}

	public void setmTargetImage(BufferedImage mTargetFrame) {
		this.mTargetImage = mTargetFrame;
	}

	public void setmPercents(int value) {
		mBinarizator.setmPercents(value);;
	}
}
