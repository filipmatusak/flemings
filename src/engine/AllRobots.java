/** Trieda reprezentuje iterovatelny zoznam vsetkych robotov bez funkcii */

package engine;

import robots.DiggingRobot;
import robots.ExplodingRobot;
import robots.Robot;

import java.util.ArrayList;

/**
 * Vrati zoznam vsetkych robotov s potlacenymi funkciami (nereaguju na kliknutie, nehybu sa)
 */
public class AllRobots {
        public static ArrayList<Robot> getTypes() {
            ArrayList<Robot> list = new ArrayList<>();
            list.add(new Robot());
            list.add(new DiggingRobot());
            list.add(new ExplodingRobot());
            return list;
        }
}
