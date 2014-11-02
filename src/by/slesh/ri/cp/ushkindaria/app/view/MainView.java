package by.slesh.ri.cp.ushkindaria.app.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import by.slesh.ri.cp.ushkindaria.app.controller.Controller;
import by.slesh.ri.cp.ushkindaria.app.model.Model;

public class MainView extends JFrame {

    private static final long serialVersionUID = -8285836465968854073L;

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;

    public MainView() {
        super("�������� ������ ������ ����� 5 'a' �����");

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        double x = (size.width - WIDTH) / 2;
        double y = (size.height - HEIGHT) / 2;
        setBounds((int) x, (int) y, WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initGui();
    }

    private void initGui() {
        ImageBoxesView imageBoxesView = new ImageBoxesView();
        ControlPanelView controlPanelView = new ControlPanelView();

        getContentPane().add(imageBoxesView);
        getContentPane().add(controlPanelView, BorderLayout.PAGE_END);

        Model model = new Model();
        new Controller(controlPanelView, imageBoxesView, model);
    }

    public static void main(String[] args) {
        MainView mainView = new MainView();
        mainView.setVisible(true);
    }
}
