package by.slesh.ri.cp.ushkindaria.app.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
	mOpenFileButton = new JButton("Загрузить изображение");
	mOpenFileButton.setActionCommand(ACTION_FILE_OPEN);

	mSkeletonizationButton = new JButton("Алгорит Зонга-Суня");
	mSkeletonizationButton.setActionCommand(ACTION_SKELETONIZATION);
	mSkeletonizationButton.setEnabled(false);

	mBugSegmentButton = new JButton("Алгоритм жука");
	mBugSegmentButton.setActionCommand(ACTION_SEGMENT_BUG);
	mBugSegmentButton.setEnabled(false);

	/* Binarization controls */
	mBinarizationButton = createButton("Бинаризовать", ACTION_BINARIZATION,
	        false);
	mPercentScrollBar = new JScrollBar(JScrollBar.HORIZONTAL,
	        G.INIT_BIN_PERCENT, 1, 0, 100);
	mPercentScrollBar.setEnabled(false);
	mPercentValueTextField = new JTextField("Процент для бинаризации = "
	        + G.INIT_BIN_PERCENT);
	mPercentValueTextField.setHorizontalAlignment(JTextField.CENTER);
	mPercentValueTextField.setEditable(false);
	/* =========== */

	/* Segment controls */
	mHistogramSegmentButton = createButton("Выделить края",
	        ACTION_SEGMENT_HISTOGRAM, false);

	mResetButton = createButton("Сбросить", ACTION_RESET, false);

	mTrimButton = createButton("Обрезать по краям края", ACTION_TRIM,
	        false);

	mExtractAreaInterestButton = createButton(
	        "Выделить интересующий участок", ACTION_EXTRACT_AREA_INTEREST,
	        false);

	mDilateButton = createButton("Расширение", ACTION_DILATE, false);

	mErodeButton = createButton("Эрозия", ACTION_ERODE, false);

	mNeuralNetworkUseButton = createButton("Использовать нейросеть",
	        ACTION_NEURALNETWORK, false);

	mExtractGroupNumberButton = createButton("Найти номер группы",
	        ACTION_EXTRACT_GROUP_NUMBER, false);

	mSegmentGroupNumberButton = createButton("Сегментировать цифры",
	        ACTION_SEGMENT_GROUP_NUMBER, false);

	mRecognizeNumberButton = createButton("РАСПОЗНАТЬ",
	        ACTION_RECOGNIZE_NUMBER, false);
	mRecognizeNumberButton.setBackground(Color.GREEN);

	BoxLayout bl = new BoxLayout(this, BoxLayout.Y_AXIS);
	setLayout(bl);

	JPanel panel = null;
	JPanel panelTitle = null;

	panel = new JPanel(new GridLayout(1, 2, 5, 5));
	panel.add(mOpenFileButton);
	panel.add(mResetButton);
	panelTitle = new JPanel(new GridLayout(1, 1, 5, 5));
	panelTitle.setBorder(BorderFactory.createTitledBorder("Файл"));
	panelTitle.add(panel);
	add(panelTitle);

	panel = new JPanel(new GridLayout(1, 2, 5, 5));
	panel.add(mSkeletonizationButton);
	panel.add(mBugSegmentButton);
	panelTitle = new JPanel();
	panelTitle.setBorder(BorderFactory
	        .createTitledBorder("Утоньшение/Оконтуривание"));
	panelTitle.add(panel);
	add(panelTitle);

	panel = new JPanel(new GridLayout(1, 2, 5, 5));
	panel.add(mPercentScrollBar);
	panel.add(mPercentValueTextField);
	panelTitle = new JPanel(new GridLayout(2, 1, 5, 5));
	panelTitle.setBorder(BorderFactory.createTitledBorder("Бинаризация"));
	panelTitle.add(mBinarizationButton);
	panelTitle.add(panel);
	add(panelTitle);

	panelTitle = new JPanel(new GridLayout(2, 1, 5, 5));
	panelTitle.setBorder(BorderFactory.createTitledBorder("Обработка"));
	panel = new JPanel(new GridLayout(1, 2, 5, 5));
	panel.add(mHistogramSegmentButton);
	panel.add(mTrimButton);
	panelTitle.add(panel);
	panel = new JPanel(new GridLayout(1, 2, 5, 5));
	panel.add(mErodeButton);
	panel.add(mDilateButton);
	panelTitle.add(panel);
	add(panelTitle);
	
	panelTitle = new JPanel(new GridLayout(2, 1, 5, 5));
	panelTitle.setBorder(BorderFactory.createTitledBorder("Сегментация"));
	panel = new JPanel(new GridLayout(4, 1, 5, 5));
	panel.add(mNeuralNetworkUseButton);
	panel.add(mExtractAreaInterestButton);
	panel.add(mExtractGroupNumberButton);
	panel.add(mSegmentGroupNumberButton);
	panelTitle.add(panel);
	add(panelTitle);
	
	panelTitle = new JPanel(new GridLayout(1, 1, 5, 5));
	panelTitle.setBorder(BorderFactory.createTitledBorder("Распознование"));
	panel = new JPanel();
	panel.add(mRecognizeNumberButton);
	panelTitle.add(panel);
	add(panelTitle);
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

	String text = "Процент для бинаризации = " + value;
	mPercentValueTextField.setText(text);
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
