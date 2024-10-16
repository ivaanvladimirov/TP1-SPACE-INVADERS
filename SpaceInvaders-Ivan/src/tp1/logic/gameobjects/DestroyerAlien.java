package tp1.logic.gameobjects;

import tp1.logic.AlienManager;
import tp1.logic.Move;
import tp1.logic.Position;
import tp1.view.Messages;

/**
 * Class containing all the attributes and methods of the destroyer aliens. <br>
 * Also, this class extends the abstract class of Alien.
 */
public class DestroyerAlien extends Alien
{
    //Instance of the bomb, which is the main weapon of the destroyers.
    private Bomb bomb;
    public static final int SCORE = 10;

    /**
     * Constructor of the Destroyer Aliens.
     * @param alienManager
     * @param position
     */
    public DestroyerAlien(AlienManager alienManager, Position position) {
        super(alienManager, position);
        this.dir = Move.LEFT;
        this.symbol = Messages.DESTROYER_ALIEN_SYMBOL;
        this.health = 1;
    }

    /**
     * Method that checks whether the bomb is active and moves it through the board.
     */
    public void moveBomb() {
        if(isBombActive()) bomb.automaticMove();
    }

    /**
     * Method used to create the bomb if it hasn't been already created. <br>
     * If it has been already created, it does nothing.
     */
    public void enableBomb() {
        if(bomb != null) return;

        bomb = new Bomb(this);
    }

    public void disableBomb(){
        this.bomb = null;
    }

    /**
     * Method that returns true if the bomb has hit the UCMShip.
     *
     * @param ship UCMShip
     */
    public boolean performAttack(UCMShip ship) {
        return bomb.performAttack(ship);
    }

    public Position getBombPosition() {
        return bomb.getPosition();
    }

    public boolean isBombActive() { return this.bomb != null; }

    /**
     * Method that returns true if the bomb has hit the UCMLaser.
     * @param laser UCMLaser
     * @return True if hit, False otherwise.
     */
    public boolean isBombHit(UCMLaser laser) {
        if(!isBombActive()) return false;
        return laser.performAttack(this.bomb);
    }
}
