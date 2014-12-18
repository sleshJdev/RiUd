/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.app;

import by.slesh.ri.cp.ushkindaria.app.view.MainView;


/**
 * @author slesh
 *
 */
public class G {
    public static MainView sMainView;
    
    public static int INIT_BIN_PERCENT = 7;
    public static int INIT_SEGMENT_THRESHOLD = 250;

    public static final String[] NAMES_FOR_FINDER = { "O", "1", "2", "3", "4" };

    public static final int QUANTITY_NEURONS = NAMES_FOR_FINDER.length;
    
    public static final int WIDTH_IMAGE = 64;
    public static final int HEIGHT_IMAGE = 64;

    public static final int QUANTITY_INPUTS_FOR_FINDER = HEIGHT_IMAGE
	    * WIDTH_IMAGE;

    public static final String PATH_WEIGHTS_FINDER = "resources\\teach\\weights\\finder";

    public static final String PATH_SET_FIDNER = "resources\\teach\\set\\finder";

    public static final String PATH_IMAGES_FOR_FINDER = "resources\\teach\\images\\finder";
}
