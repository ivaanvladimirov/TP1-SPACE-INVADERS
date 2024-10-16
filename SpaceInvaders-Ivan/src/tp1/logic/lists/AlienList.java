package tp1.logic.lists;

import tp1.logic.gameobjects.Alien;

/**
 * Class containing the Array of Aliens. <br>
 * They can be Regular Aliens or Destroyer Aliens.
 */
public class AlienList {
    //Array of the abstract class of Alien.
    public Alien[] aliens;
    //Constant size of the Array.
    public final int initSize;

    public AlienList(int initSize) {
        this.initSize = initSize;
        this.aliens = new Alien[initSize];
    }

    /**
     * Method to remove an Alien from the Array of Aliens if it is dead. <br>
     * Creates a new Array with the Aliens that are still alive.
     * @param alien to be removed from the Array.
     */
    public void remove(Alien alien){
        Alien[] newArr = new Alien[aliens.length-1];
        for(int i = 0, x = 0; i < aliens.length; i++) {
            if(aliens[i].equals(alien)) continue;
            newArr[x++] = aliens[i];
        }
        this.aliens = newArr;
    }
}
