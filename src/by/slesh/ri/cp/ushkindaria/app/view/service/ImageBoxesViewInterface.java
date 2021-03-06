package by.slesh.ri.cp.ushkindaria.app.view.service;

import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public interface ImageBoxesViewInterface {

    void updateSource(BufferedImage source);

    void updateTarget(BufferedImage target);

    void updateAreaInterest(BufferedImage area);

    void updateGroupNumber(BufferedImage groupNumber);

    void addTargetImageBoxClickListener(MouseListener l);

    void getLocationOnImage(Point currentLocation);

    void updateSegmentGroupNumber(BufferedImage[] digits);
    
    void updateUnrecognizeNumber(BufferedImage[] digits);

    void updateRecognizeNumber(BufferedImage[] recognizeNumber);
}
