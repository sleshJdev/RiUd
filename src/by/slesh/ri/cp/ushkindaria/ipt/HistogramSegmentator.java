package by.slesh.ri.cp.ushkindaria.ipt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class HistogramSegmentator extends AbstractTool {

    private int[] mHistogramX;
    private int[] mHistogramY;

    private int mThreshold = 50;
    
    public BufferedImage segment(BufferedImage source) {
        int h = source.getHeight();
        int w = source.getWidth();
        countHistogram(source.getRGB(0, 0, w, h, null, w, w), h, w);
        Graphics g = source.getGraphics();
        g.setColor(Color.RED);
        for (int x = 1; x < mHistogramX.length; ++x) {
            int jump = Math.abs(mHistogramX[x] - mHistogramX[x - 1]);
            if (jump > mThreshold) g.drawLine(x, 0, x, h);
        }
        g.setColor(Color.GREEN);
        for (int y = 1; y < mHistogramY.length; ++y) {
            int jump = Math.abs(mHistogramX[y] - mHistogramX[y - 1]);
            if (jump > mThreshold) g.drawLine(0, y, w, y);
        }
        return source;
    }

    private void countHistogram(int[] rgb, int h, int w) {
        mHistogramX = new int[w];
        mHistogramY = new int[h];
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                if (rgb[y * w + x] == _1) {
                    ++mHistogramX[x];
                    ++mHistogramY[y];
                }
            }
        }
    }

    public int getmThreshold() {
        return mThreshold;
    }

    public void setmThreshold(int mThreshold) {
        this.mThreshold = mThreshold;
    }

}
