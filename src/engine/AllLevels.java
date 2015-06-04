/**
 * Trieda reprezentuje zoznam vsetkych zatial vytvorenych levelov - sluzi na vypis levelov v uvodnom okne pre vyber levelov
 */
package engine;

import java.io.File;
import java.util.ArrayList;

/** Vrati zoznam vsetkych levelov */
public class AllLevels {
    public static ArrayList<File> getLevels() {
        ArrayList<File> list = new ArrayList<>();
        File folder = new File("src/maps/levels");
        for (File file : folder.listFiles()) {
            list.add(file);
        }
        return list;
    }

    /** Vrati nasledujuci level k zadanemu levelu alebo null, ak uz ide o posledny level */
    public static File getNextLevel(File level){
        ArrayList<File> list = AllLevels.getLevels();
        int i = list.indexOf(level);
        i++;
        if (i >= list.size()) return null;
        return list.get(i);
    }
}
