package sample;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * zoznam vsetkych zatial vytvorenych levelov - sluzi na vypis levelov v uvodnom okne pre vyber levelov
 */
public class AllLevels {
    public static ArrayList<File> getLevels() {
        ArrayList<File> list = new ArrayList<>();
        File folder = new File("src/maps/levels");
        for (File file : folder.listFiles()) {
            list.add(file);
        }
        return list;
    }

    public static File getNextLevel(File level){
        System.out.println(level.toPath());
        ArrayList<File> list = AllLevels.getLevels();
        for (File file : list){
            System.out.println(file.toPath());
        }
        int i = list.indexOf(level);
        System.out.println(i);
        i++;
        if (i >= list.size()) return null;
        System.out.println(list.get(i).toPath());
        return list.get(i);
    }
}
