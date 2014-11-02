package by.slesh.ri.cp.ushkindaria.ipt;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class BugSegmentator extends AbstractTool {

    private Point mFrom = new Point();

    private enum Direction {
        Up, Right, Down, Left
    }

    public void setmFrom(Point from) {
        mFrom = from;
    }

    public Contour segment(BufferedImage source) {
        return segment(source, mFrom);
    }

    public Contour segment(BufferedImage source, Point from) {
        int h = source.getHeight();
        int w = source.getWidth();
        int[] rgb = source.getRGB(0, 0, w, h, null, w, w);
        Contour contour = findContour(mFrom, rgb, h, w);
        return contour;
    }

    private static Point findEntryPoint(Point from, int[] rgb, int h, int w) {
        for (int y = from.y; y < h; ++y) {
            for (int x = from.x; x < w; ++x) {
                if (rgb[w * y + x] == _1) return new Point(x, y);
            }
        }
        return from;
    }

    private static Contour findContour(Point from, int[] rgb, int h, int w) {
        Contour contour = new Contour();
        Point start = findEntryPoint(from, rgb, h, w);
        Direction whereLook = Direction.Right;
        Direction whereMove = Direction.Left;
        int oldColor = _0;
        int stopper = 0;
        do {
            if (stopper++ == 1000000) break;
            if (start.x < 0 || start.x >= w) break;
            if (start.y < 0 || start.y >= h) break;
            whereLook = moveBug(start, whereLook, whereMove);
            int currentColor = rgb[w * start.y + start.x];
            if (currentColor != oldColor) {
                contour.addPoint(new Point(start));
                oldColor = currentColor;
            }
            if (currentColor == _1) whereMove = Direction.Left;
            else whereMove = Direction.Right;
        } while (!contour.isClosed());
        return contour;
    }

    private static Direction moveBug(Point current, Direction whereLook, Direction whereMove) {
        boolean isRight = whereMove == Direction.Right;
        switch (whereLook) {
        case Up:
            if (isRight) current.translate(1, 0);
            else current.translate(-1, 0);
            return isRight ? Direction.Right : Direction.Left;
        case Right:
            if (isRight) current.translate(0, 1);
            else current.translate(0, -1);
            return isRight ? Direction.Down : Direction.Up;
        case Down:
            if (isRight) current.translate(-1, 0);
            else current.translate(1, 0);
            return isRight ? Direction.Left : Direction.Right;
        case Left:
            if (isRight) current.translate(0, -1);
            else current.translate(0, 1);
            return isRight ? Direction.Up : Direction.Down;
        }
        return whereLook;
    }
}
