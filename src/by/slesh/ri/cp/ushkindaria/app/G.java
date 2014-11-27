/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.app;

/**
 * @author slesh
 *
 */
public class G {
    public static final String[] NAMES                  = { "1", "2", "3","4", "O"};
    public static int            INIT_BIN_PERCENT       = 8;
    public static int            INIT_SEGMENT_THRESHOLD = 250;
    
    public static final int      WIDTH                  = 12;
    public static final int      HEIGHT                 = 12;
    public static final int      QUANTITY_NEURONS       = 5;
    public static final int      QUANTITY_INPUTS        = HEIGHT * WIDTH;
    
    public static final String   PATH_WEIGHTS            = "resources\\teach\\weights";
    public static final String   PATH_SET               = "resources\\teach\\set";
    
    public static String getNameById(int id) {
	return NAMES[id];
    }

    public static int getIdClassByName(String name) {
	int id = 0;
	for (int i = 0; i < QUANTITY_NEURONS; ++i) {
	    if (NAMES[i].equals(name)) {
		id = i;
		break;
	    }
	}
	return id;
    }
}
