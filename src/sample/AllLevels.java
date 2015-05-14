package sample;

import java.util.ArrayList;

/**
 * zoznam vsetkych zatial vytvorenych levelov - sluzi na vypis levelov v uvodnom okne pre vyber levelov
 */
public class AllLevels {
    public ArrayList<String> getLevels()

    {
        ArrayList<String> list = new ArrayList<>();
        //n treba definovat podla poctu levelov
        int n = 3;
        for (int i = 1; i <= n; i++) {
            list.add("levels/level" + i + ".txt");
        }
        return list;
    }
}
