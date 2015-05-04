package old;

import robots.Robot;

/** Interface určujúci metódy políčka, ktoré môže robot volať. Každá
 *  trieda, ktorá môže obsahovať robota, musí tieto metódy implementovať.
 *  V našom prípade je to trieda EmptySquare a jej podtriedy.
 */
public interface RobotHolder {

    /** Z policka odisiel robot, uprav si podla toho stav a vykonaj
     * dalsie nasledky tejto zmeny, napr. padajuci roboti z horneho
     * suseda.  Tuto metodu spravidla vola robot. */
     void deregisterRobot();

    /** Na policko prisiel robot, uprav si podla toho stav a vykonaj
     * pripadne nasledky tejto zmeny.  Tuto metodu spravidla vola
     * robot. */
     void registerRobot(Robot otherRobot);

    /** Aktualny robot z tohto policka sa chce posunut v smere direction.
     * Bud mu policko akciu povoli, vykona ju a vsetky jej nasledky a vrati true,
     * alebo akciu vyhodnoti ako nemoznu a vrati false.  Tuto metodu
     * spravidla vola robot stojaci na tomto policku.
     */
     boolean actionMove(Direction direction);

    /** Aktualny robot z tohto policka chce kopat vedlajsie policko v smere direction.
     * Bud mu policko akciu povoli, vykona ju a vsetky jej nasledky a vrati true,
     * alebo akciu vyhodnoti ako nemoznu a vrati false.  Tuto metodu
     * spravidla vola robot stojaci na tomto policku.
     */
     boolean actionDigging(Direction direction);


    /** Aktualny robot z tohto policka chce vybuchnut.  Bud mu policko akciu
     * povoli, vykona ju a vsetky jej nasledky a vrati true, alebo
     * akciu vyhodnoti ako nemoznu a vrati false.  Tuto metodu
     * spravidla vola robot stojaci na tomto policku.
     */
     boolean actionExploding();
}


