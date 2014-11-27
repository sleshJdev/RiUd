/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.app.view.service;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * @author slesh
 *
 */
public interface FileViewInterface{
    String ACTION_TEACH = "action_teach";
    
    void addTeachClickListener(ActionListener l);
}
