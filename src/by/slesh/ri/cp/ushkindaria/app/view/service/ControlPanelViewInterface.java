package by.slesh.ri.cp.ushkindaria.app.view.service;

import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;

public interface ControlPanelViewInterface {

	static final String	ACTION_FILE_OPEN				= "action_file_open";
	static final String	ACTION_BINARIZATION				= "action_binarization";
	static final String	ACTION_SKELETONIZATION			= "action_skeletonization";
	static final String	ACTION_SEGMENT_HISTOGRAM		= "action_segment_histogram";
	static final String	ACTION_SEGMENT_BUG				= "action_segment_bug";
	static final String	ACTION_RESET					= "action_reset";
	static final String	ACTION_TRIM						= "action_trim";
	static final String	ACTION_EXTRACT					= "action_extract";
	static final String	ACTION_DILATE					= "action_dilate";
	static final String	ACTION_ERODE					= "action_erode";
	static final String	ACTION_NEURALNETWORK					= "action_neuralnetwork";

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
    
    void addResetClickListener(ActionListener l);

    void addErodeClickListener(ActionListener l);
    
    void addDilateClickListener(ActionListener l);
    
    void addExtractClickListener(ActionListener l);

    void addTrimClickListener(ActionListener l);

    void addNeuralNetworkClickListener(ActionListener l);
    
}
