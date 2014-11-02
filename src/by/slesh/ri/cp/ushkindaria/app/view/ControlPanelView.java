package by.slesh.ri.cp.ushkindaria.app.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;

import by.slesh.ri.cp.ushkindaria.app.view.service.ControlPanelViewInterface;

public class ControlPanelView extends JPanel implements ControlPanelViewInterface {

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

    public ControlPanelView() {
        setLayout(new GridLayout(3, 3));
        /* Binarization controls */
        mBinarizationButton = new JButton("Бинаризовать");
        mBinarizationButton.setActionCommand(ACTION_BINARIZATION);
        mBinarizationButton.setEnabled(false);

        mPercentScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 40, 1, 0, 100);
        mPercentScrollBar.setEnabled(false);

        mPercentValueTextField = new JTextField("Процент для бинаризации = ");
        mPercentValueTextField.setHorizontalAlignment(JTextField.CENTER);
        mPercentValueTextField.setEditable(false);
        /* Segment controls */
        mHistogramSegmentButton = new JButton("Сегментировать");
        mHistogramSegmentButton.setActionCommand(ACTION_SEGMENT_HISTOGRAM);
        mHistogramSegmentButton.setEnabled(false);

        mSegmentThresholScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 50, 1, 0, 500);
        mSegmentThresholScrollBar.setEnabled(false);

        mSegmentThresholdValueTextField = new JTextField("Порог сегментации = 50");
        mSegmentThresholdValueTextField.setHorizontalAlignment(JTextField.CENTER);
        mSegmentThresholdValueTextField.setEditable(false);

        mSkeletonizationButton = new JButton("Алгорит Зонга-Суня");
        mSkeletonizationButton.setActionCommand(ACTION_SKELETONIZATION);
        mSkeletonizationButton.setEnabled(false);

        mOpenFileButton = new JButton("Загрузить изображение");
        mOpenFileButton.setActionCommand(ACTION_FILE_OPEN);

        mBugSegmentButton = new JButton("Алгоритм жука");
        mBugSegmentButton.setActionCommand(ACTION_SEGMENT_BUG);
        mBugSegmentButton.setEnabled(false);

        add(mBinarizationButton);
        add(mPercentScrollBar);
        add(mPercentValueTextField);

        add(mHistogramSegmentButton);
        add(mSegmentThresholScrollBar);
        add(mSegmentThresholdValueTextField);

        add(mOpenFileButton);
        add(mSkeletonizationButton);
        add(mBugSegmentButton);
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
    public void updateSegmentThresholdValue(int value) {
        String text = "Порог сегментации = " + value;
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
}
