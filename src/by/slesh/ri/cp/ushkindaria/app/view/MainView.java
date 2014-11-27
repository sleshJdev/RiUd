package by.slesh.ri.cp.ushkindaria.app.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import by.slesh.ri.cp.ushkindaria.app.controller.Controller;
import by.slesh.ri.cp.ushkindaria.app.model.Model;
import by.slesh.ri.cp.ushkindaria.app.view.service.ControlViewInterface;
import by.slesh.ri.cp.ushkindaria.app.view.service.FileViewInterface;
import by.slesh.ri.cp.ushkindaria.app.view.service.ImageBoxesViewInterface;

public class MainView extends JFrame {

    private static final long serialVersionUID = -8285836465968854073L;

    private static final int  WIDTH            = 1000;
    private static final int  HEIGHT           = 700;

    public MainView() {

	super("Курсовой проект Ушкина Дарья 5 'a' класс");

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
	FileMenuView fileView = new FileMenuView();

	getContentPane().add(imageBoxesView);
	getContentPane().add(controlPanelView, BorderLayout.PAGE_END);

	JMenuBar menuBar = new JMenuBar();
	menuBar.add(fileView);
	setJMenuBar(menuBar);

	Model model = new Model();
	new Controller(controlPanelView, imageBoxesView, fileView, model);
    }

}
