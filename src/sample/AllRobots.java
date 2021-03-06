package sample;

import robots.DiggingRobot;
import robots.ExplodingRobot;
import robots.Robot;

import java.util.ArrayList;

/**
 *ulozene vsetky typy robotov
 * PROBLEM: nemozeme takto vytvarat instancie robotov bez parametrov a ak ich vytvorime, mohli by si mysliet, ze su
 * sucastou hry -> spravit zoznam robotov inak
 * RE: uz by to mohlo fungovat ;)
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
