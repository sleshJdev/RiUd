package by.slesh.ri.cp.ushkindaria.view.service;

import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;

public interface ControlPanelViewInterface {
	static final String ACTION_FILE_OPEN = "action_file_open";
	static final String ACTION_BINARIZATION = "action_binarization";
	static final String ACTION_SKELETONIZATION = "action_skeletonization";
	
	void addOnOpenFileListener(ActionListener listener);
	void addOnBinarizateListener(ActionListener listener);
	void addOnSkeletonizationListener(ActionListener listener);
	void addOnAdjustmentListener(AdjustmentListener listener);
	void updatePercentValue(int value);
	void unableControls();
}
