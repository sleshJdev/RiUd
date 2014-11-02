package by.slesh.ri.cp.ushkindaria.app.view.service;

import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;

public interface ControlPanelViewInterface {

    static final String ACTION_FILE_OPEN = "action_file_open";
    static final String ACTION_BINARIZATION = "action_binarization";
    static final String ACTION_SKELETONIZATION = "action_skeletonization";
    static final String ACTION_SEGMENT_HISTOGRAM = "action_segment_histogram";
    static final String ACTION_SEGMENT_BUG = "action_segment_bug";

    void addBugSegmentClickListener(ActionListener l);

    void addHistogramSegmentClickListener(ActionListener l);

    void addOpenFileClickListener(ActionListener l);

    void addBinarizateClickListener(ActionListener l);

    void addSkeletonizationClickListener(ActionListener l);

    void addBinPercentChangeValueListener(AdjustmentListener l);

    void updatePercentValue(int value);

    void updateSegmentThresholdValue(int value);

    void enableControls();

    void addSegmentThresholdChangeValueListener(AdjustmentListener l);
}
