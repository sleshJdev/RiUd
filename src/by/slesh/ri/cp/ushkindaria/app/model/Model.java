package by.slesh.ri.cp.ushkindaria.app.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import by.slesh.ri.cp.ushkindaria.ipt.BugSegmentator;
import by.slesh.ri.cp.ushkindaria.ipt.Contour;
import by.slesh.ri.cp.ushkindaria.ipt.PercentsBinarizator;
import by.slesh.ri.cp.ushkindaria.ipt.HistogramSegmentator;
import by.slesh.ri.cp.ushkindaria.ipt.Skeletonizator;

public class Model {

    private BufferedImage mSourceImage;
    private BufferedImage mTargetImage;
    private PercentsBinarizator mBinarizator = new PercentsBinarizator();
    private Skeletonizator mSkeletonizator = new Skeletonizator();
    private HistogramSegmentator mHistogramSegmentator = new HistogramSegmentator();
    private BugSegmentator mBugSegmentator = new BugSegmentator();

    public void binarization() {
        mTargetImage = mBinarizator.binarization(mSourceImage);
    }

    public void skeletonization() {
        mTargetImage = mSkeletonizator.skeletonization(mTargetImage);
    }

    public void histogramSegment() {
        mTargetImage = mHistogramSegmentator.segment(mTargetImage);
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

    public void setPercents(int value) {
        mBinarizator.setmPercents(value);
    }

    public void setSegmentThreshold(int value) {
        mHistogramSegmentator.setmThreshold(value);
    }

    public void bugSegment() {
        Contour contour = mBugSegmentator.segment(mTargetImage);
        mTargetImage = contour.sub(mTargetImage);
    }

    public void setStartPointForBugSegmentator(Point point) {
        mBugSegmentator.setmFrom(point);
        Graphics g = mTargetImage.getGraphics();
        g.setColor(Color.BLUE);
        g.fillOval(point.x - 5, point.y - 5, 10, 10);
    }
}
