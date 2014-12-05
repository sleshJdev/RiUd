package by.slesh.ri.cp.ushkindaria.app.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;

import by.slesh.ri.cp.ushkindaria.app.G;
import by.slesh.ri.cp.ushkindaria.app.view.service.ControlViewInterface;

public class ControlPanelView extends JPanel implements ControlViewInterface {

    private static final long serialVersionUID = -2454572918534173516L;

    private JTextField mPercentValueTextField;
    private JScrollBar mPercentScrollBar;
    private JButton mBinarizationButton;
    private JTextField mSegmentThresholdValueTextField;
    private JScrollBar mSegmentThresholScrollBar;
    private JButton mHistogramSegmentButton;
    private JButton mSkeletonizationButton;
    private JButton mOpenFileButton;
    private JButton mBugSegmentButton;
    private JButton mResetButton;
    private JButton mTrimButton;
    private JButton mExtractAreaInterestButton;
    private JButton mDilateButton;
    private JButton mErodeButton;
    private JButton mNeuralNetworkUseButton;
    private JButton mExtractGroupNumberButton;
    private JButton mSegmentGroupNumberButton;
    private JButton mRecognizeNumberButton;

    public ControlPanelView() {

	mOpenFileButton = new JButton("��������� �����������");
	mOpenFileButton.setActionCommand(ACTION_FILE_OPEN);

	mSkeletonizationButton = new JButton("������� �����-����");
	mSkeletonizationButton.setActionCommand(ACTION_SKELETONIZATION);
	mSkeletonizationButton.setEnabled(false);

	mBugSegmentButton = new JButton("�������� ����");
	mBugSegmentButton.setActionCommand(ACTION_SEGMENT_BUG);
	mBugSegmentButton.setEnabled(false);

	/* Binarization controls */
	mBinarizationButton = createButton("������������", ACTION_BINARIZATION,
	        false);
	mPercentScrollBar = new JScrollBar(JScrollBar.HORIZONTAL,
	        G.INIT_BIN_PERCENT, 1, 0, 100);
	mPercentScrollBar.setEnabled(false);
	mPercentValueTextField = new JTextField("������� ��� ����������� = "
	        + G.INIT_BIN_PERCENT);
	mPercentValueTextField.setHorizontalAlignment(JTextField.CENTER);
	mPercentValueTextField.setEditable(false);
	/* =========== */

	/* Segment controls */
	mHistogramSegmentButton = createButton("�������� ����",
	        ACTION_SEGMENT_HISTOGRAM, false);
	mSegmentThresholScrollBar = new JScrollBar(JScrollBar.HORIZONTAL,
	        G.INIT_SEGMENT_THRESHOLD, 1, 0, 500);
	mSegmentThresholScrollBar.setEnabled(false);
	mSegmentThresholdValueTextField = new JTextField("����� ����������� = "
	        + G.INIT_SEGMENT_THRESHOLD);
	mSegmentThresholdValueTextField
	        .setHorizontalAlignment(JTextField.CENTER);
	mSegmentThresholdValueTextField.setEditable(false);
	/* =========== */

	mResetButton = createButton("��������", ACTION_RESET, false);

	mTrimButton = createButton("�������� ����(40 ��������)", ACTION_TRIM,
	        false);

	mExtractAreaInterestButton = createButton(
	        "�������� ������������ �������", ACTION_EXTRACT_AREA_INTEREST,
	        false);

	mDilateButton = createButton("����������", ACTION_DILATE, false);

	mErodeButton = createButton("������", ACTION_ERODE, false);

	mNeuralNetworkUseButton = createButton("������������ ���������",
	        ACTION_NEURALNETWORK, false);

	mExtractGroupNumberButton = createButton("����� ����� ������",
	        ACTION_EXTRACT_GROUP_NUMBER, false);

	mSegmentGroupNumberButton = createButton("�������������� �����",
	        ACTION_SEGMENT_GROUP_NUMBER, false);

	mRecognizeNumberButton = createButton("����������",
	        ACTION_RECOGNIZE_NUMBER, false);
	mRecognizeNumberButton.setBackground(Color.GREEN);

	setLayout(new GridLayout(20, 1));

	add(mOpenFileButton);
	add(mResetButton);
	add(mSkeletonizationButton);
	add(mBugSegmentButton);
	add(new JPanel());
	add(mBinarizationButton);
	add(mPercentScrollBar);
	add(mPercentValueTextField);

	add(mTrimButton);
	add(mErodeButton);
	add(mDilateButton);

	add(mHistogramSegmentButton);
	add(mSegmentThresholScrollBar);
	add(mSegmentThresholdValueTextField);

	add(mNeuralNetworkUseButton);
	add(mExtractAreaInterestButton);
	add(mExtractGroupNumberButton);
	add(mSegmentGroupNumberButton);
	add(mRecognizeNumberButton);
    }

    private JButton createButton(String caption, String actionCommand,
	    boolean isEnable) {

	JButton button = new JButton(caption);
	button.setActionCommand(actionCommand);
	button.setEnabled(isEnable);
	return button;
    }

    public void enableComponents(Container container, boolean enable) {

	Component[] components = container.getComponents();
	for (Component component : components) {
	    component.setEnabled(enable);
	    if (component instanceof Container) {
		enableComponents((Container) component, enable);
	    }
	}
    }

    @Override
    public void updatePercentValue(int value) {

	String text = "������� ��� ����������� = " + value;
	mPercentValueTextField.setText(text);
    }

    @Override
    public void updateSegmentThresholdValue(int value) {
	String text = "����� ����������� = " + value;
	mSegmentThresholdValueTextField.setText(text);
    }

    @Override
    public void enableControls() {
	enableComponents(this, true);
    }

    @Override
    public void addOpenFileClickListener(ActionListener l) {
	mOpenFileButton.addActionListener(l);
    }

    @Override
    public void addBinarizateClickListener(ActionListener l) {
	mBinarizationButton.addActionListener(l);
    }

    @Override
    public void addSkeletonizationClickListener(ActionListener l) {
	mSkeletonizationButton.addActionListener(l);
    }

    @Override
    public void addBinPercentChangeValueListener(AdjustmentListener l) {
	mPercentScrollBar.addAdjustmentListener(l);
    }

    @Override
    public void addSegmentThresholdChangeValueListener(AdjustmentListener l) {
	mSegmentThresholScrollBar.addAdjustmentListener(l);
    }

    @Override
    public void addHistogramSegmentClickListener(ActionListener l) {
	mHistogramSegmentButton.addActionListener(l);
    }

    @Override
    public void addBugSegmentClickListener(ActionListener l) {
	mBugSegmentButton.addActionListener(l);
    }

    @Override
    public void addResetClickListener(ActionListener l) {
	mResetButton.addActionListener(l);
    }

    @Override
    public void addErodeClickListener(ActionListener l) {
	mErodeButton.addActionListener(l);
    }

    @Override
    public void addDilateClickListener(ActionListener l) {
	mDilateButton.addActionListener(l);
    }

    @Override
    public void addExtractAreaInterestClickListener(ActionListener l) {
	mExtractAreaInterestButton.addActionListener(l);
    }

    @Override
    public void addTrimClickListener(ActionListener l) {
	mTrimButton.addActionListener(l);
    }

    @Override
    public void addNeuralNetworkClickListener(ActionListener l) {
	mNeuralNetworkUseButton.addActionListener(l);
    }

    @Override
    public void addExtractGroupNumberClickListener(ActionListener l) {
	mExtractGroupNumberButton.addActionListener(l);
    }

    @Override
    public void addSegmentGroupNumberClickListener(ActionListener l) {
	mSegmentGroupNumberButton.addActionListener(l);
    }

    @Override
    public void addRecognizeNumberClickListener(ActionListener l) {
	mRecognizeNumberButton.addActionListener(l);
    }
}
