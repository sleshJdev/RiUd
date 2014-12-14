/**
 * 
 */
package by.slesh.ri.cp.ushkindaria.ipt.segment;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

import by.slesh.ri.cp.ushkindaria.ipt.Tool;

/**
 * @author slesh
 *
 */
public class SimpleSkeletonizator {
    public static BufferedImage skeleton(BufferedImage bmp) {
	int i = 1, j = 1, width = bmp.getWidth(), height = bmp.getHeight();
	int[][] bitmap = new int[width + 2][height + 2];

	for (i = 2; i < width; i++) {
	    for (j = 2; j < height; j++) {
		if (bmp.getRGB(i - 1, j - 1) == Tool._1) bitmap[i][j] = 1;
		else bitmap[i][j] = 0;
	    }
	}

	int[][] okr = new int[3][3];
	int[] okrs = new int[10];
	boolean flag = true;
	while (flag) {
	    flag = false;
	    for (i = 1; i <= width; i++) {
		for (j = 1; j <= height; j++) {
		    if (bitmap[i][j] == 1) {
			okr[0][0] = okrs[0] = bitmap[i - 1][j - 1];
			okr[1][0] = okrs[1] = bitmap[i][j - 1];
			okr[2][0] = okrs[2] = bitmap[i + 1][j - 1];
			okr[2][1] = okrs[3] = bitmap[i + 1][j];
			okr[2][2] = okrs[4] = bitmap[i + 1][j + 1];
			okr[1][2] = okrs[5] = bitmap[i][j + 1];
			okr[0][2] = okrs[6] = bitmap[i - 1][j + 1];
			okr[0][1] = okrs[7] = bitmap[i - 1][j];
			okrs[8] = okrs[0];
			okrs[9] = okrs[1];

			if (pointB(okrs) > 1 && point03(okrs)
			        && checkConsistency(okr)) {
			    bitmap[i][j] = 2;
			}
		    }
		}
	    }
	    for (i = 1; i <= width; i++) {
		for (j = 1; j <= height; j++) {
		    if (bitmap[i][j] == 2) {
			bitmap[i][j] = 0;
			flag = true;
		    }
		}
	    }

	}
	for (i = 1; i <= width - 1; i++) {
	    for (j = 1; j <= height - 1; j++) {
		if (bitmap[i][j] == 1) bmp.setRGB(i - 1, j - 1,
		        Color.BLACK.getRGB());
		else bmp.setRGB(i - 1, j - 1, Color.WHITE.getRGB());
	    }
	}
	return bmp;
    }

    private static boolean point03(int[] m) {
	for (int i = 0; i < 8; i++) {
	    if (m[i] == 0 && m[i + 1] == 0 && m[i + 2] == 0) {
		return true;
	    }
	}
	return false;
    }

    private static int pointB(int[] m) {
	int sum = 0;
	for (int i = 0; i < 8; i++) {
	    if (m[i] != 0) sum++;
	}
	return sum;
    }

    private static boolean checkConsistency(int[][] m) {
	int[][] tmp = new int[3][3];
	int bi = -1, bj = -1;
	for (int i = 0; i < 3; i++) {
	    for (int j = 0; j < 3; j++) {
		if ((tmp[i][j] = m[i][j]) == 1 && (i != 1 || j != 1)) {
		    bi = i;
		    bj = j;
		}
	    }
	}
	tmp[1][1] = 0;
	if (bi != -1) {
	    Queue<Integer> q = new LinkedList<Integer>();
	    q.add(bi);
	    q.add(bj);
	    int i, j;
	    while (q.size() != 0) {
		i = q.poll();
		j = q.poll();

		tmp[i][j] = 0;

		if ((i != 0) && (j != 0) && (tmp[i - 1][j - 1] == 1)) {
		    q.add(i - 1);
		    q.add(j - 1);

		}
		if (i != 0 && tmp[i - 1][j] == 1) {
		    q.add(i - 1);
		    q.add(j);
		}
		if (i != 0 && j != 2 && tmp[i - 1][j + 1] == 1) {
		    q.add(i - 1);
		    q.add(j + 1);
		}
		if (j != 0 && tmp[i][j - 1] == 1) {
		    q.add(i);
		    q.add(j - 1);
		}
		if (j != 2 && tmp[i][j + 1] == 1) {
		    q.add(i);
		    q.add(j + 1);
		}
		if (i != 2 && j != 0 && tmp[i + 1][j - 1] == 1) {
		    q.add(i + 1);
		    q.add(j - 1);
		}
		if (i != 2 && tmp[i + 1][j] == 1) {
		    q.add(i + 1);
		    q.add(j);
		}
		if (i != 2 && j != 2 && tmp[i + 1][j + 1] == 1) {
		    q.add(i + 1);
		    q.add(j + 1);
		}
	    }
	    bi = -1;
	    bj = -1;
	    for (i = 0; i < 3; i++) {
		for (j = 0; j < 3; j++) {
		    if (tmp[i][j] == 1 && (i != 1 || j != 1)) {
			bi = i;
			bj = j;
		    }
		}
	    }
	    if (bi != -1) return false;
	    else return true;

	} else {
	    return true;
	}

    }
}
