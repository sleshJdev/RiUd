/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.app.view;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import by.slesh.ri.cp.ushkindaria.app.view.service.FileViewInterface;

/**
 * @author slesh
 *
 */
public class FileMenuView extends JMenu implements FileViewInterface {
    private JMenuItem mTeachMenuItem;

    public FileMenuView() {
	super("Нейросеть");

	mTeachMenuItem = new JMenuItem("Обучить нейросеть");
	mTeachMenuItem.setActionCommand(ACTION_TEACH);
	add(mTeachMenuItem);
    }

    @Override
    public void addTeachClickListener(ActionListener l) {
	mTeachMenuItem.addActionListener(l);
    }
}
