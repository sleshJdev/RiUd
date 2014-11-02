package by.slesh.ri.cp.ushkindaria.app.controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.JFileChooser;

import by.slesh.ri.cp.ushkindaria.app.model.Model;
import by.slesh.ri.cp.ushkindaria.app.view.service.ControlPanelViewInterface;
import by.slesh.ri.cp.ushkindaria.app.view.service.ImageBoxesViewInterface;

public class Controller implements ActionListener {

    private class BinPercentsChangeValueListener implements AdjustmentListener {

        @Override
        public void adjustmentValueChanged(AdjustmentEvent arg0) {
            mModel.setPercents(arg0.getValue());
            mModel.binarization();
            mImageBoxesView.updateTarget(mModel.getmTargetImage());
            mControlPanelView.updatePercentValue(arg0.getValue());
        }
    }

    private class SegmentThresholChangeValueListener implements AdjustmentListener {

        @Override
        public void adjustmentValueChanged(AdjustmentEvent arg0) {
            mModel.setSegmentThreshold(arg0.getValue());
            mModel.binarization();
            mModel.histogramSegment();
            mImageBoxesView.updateTarget(mModel.getmTargetImage());
            mControlPanelView.updateSegmentThresholdValue(arg0.getValue());
        }
    }

    private class MouseClickAdapter extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent arg0) {
            Point location = new Point(arg0.getPoint());
            mImageBoxesView.getLocationOnImage(location);
            System.out.println("1=" + arg0.getPoint());
            System.out.println("2=" + location);
            mModel.setStartPointForBugSegmentator(location);
            mImageBoxesView.updateTarget(mModel.getmTargetImage());
        }
    }

    private ControlPanelViewInterface mControlPanelView;
    private ImageBoxesViewInterface mImageBoxesView;
    private Model mModel;

    public Controller(ControlPanelViewInterface controlPanelView, ImageBoxesViewInterface imageBoxesView, Model model) {
        mImageBoxesView = imageBoxesView;
        mControlPanelView = controlPanelView;
        mModel = model;

        mImageBoxesView.addTargetImageBoxClickListener(new MouseClickAdapter());

        mControlPanelView.addBinarizateClickListener(this);
        mControlPanelView.addOpenFileClickListener(this);
        mControlPanelView.addBinPercentChangeValueListener(new BinPercentsChangeValueListener());
        mControlPanelView.addSegmentThresholdChangeValueListener(new SegmentThresholChangeValueListener());
        mControlPanelView.addSkeletonizationClickListener(this);
        mControlPanelView.addHistogramSegmentClickListener(this);
        mControlPanelView.addBugSegmentClickListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        switch (((AbstractButton) arg0.getSource()).getActionCommand()) {
        case ControlPanelViewInterface.ACTION_BINARIZATION:
            mModel.binarization();
            mImageBoxesView.updateTarget(mModel.getmTargetImage());
            break;
        case ControlPanelViewInterface.ACTION_FILE_OPEN:
            BufferedImage source = openFile();
            if (source == null) return;
            mModel.setmSourceImage(source);
            mModel.setmTargetImage(source);
            mImageBoxesView.updateSource(mModel.getmSourceImage());
            mImageBoxesView.updateTarget(mModel.getmTargetImage());
            mControlPanelView.enableControls();
            break;
        case ControlPanelViewInterface.ACTION_SKELETONIZATION:
            mModel.skeletonization();
            mImageBoxesView.updateTarget(mModel.getmTargetImage());
            break;
        case ControlPanelViewInterface.ACTION_SEGMENT_HISTOGRAM:
            mModel.histogramSegment();
            mImageBoxesView.updateTarget(mModel.getmTargetImage());
            break;
        case ControlPanelViewInterface.ACTION_SEGMENT_BUG:
            mModel.bugSegment();
            mImageBoxesView.updateTarget(mModel.getmTargetImage());
            break;
        }
    }

    private static final JFileChooser FC = new JFileChooser();

    private BufferedImage openFile() {
        BufferedImage loadedImage = null;
        int returnValue = FC.showDialog(null, ControlPanelViewInterface.ACTION_FILE_OPEN);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = FC.getSelectedFile();
            try {
                loadedImage = ImageIO.read(file);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return loadedImage;
    }
}
