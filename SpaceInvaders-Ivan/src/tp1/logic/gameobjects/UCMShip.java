package tp1.logic.gameobjects;

import tp1.logic.Move;
import tp1.logic.Position;
import tp1.view.Messages;

/**
 * Class of the UCMShip that contains all the attributes and methods.
 */
public class UCMShip {
    //Constant.
    public static final String SYMBOL = Messages.UCMSHIP_SYMBOL;
    //Instance of the position.
    private Position position;
    //Health attribute.
    private int health = 3;

    /**
     * Constructor for the UCMShip.
     */
    public UCMShip() {
        this.position = new Position(4, 7);
    }

    /**
     * Method used to perform the movement of the UCMShip with the direction provided by the user.
     * @param move
     */
    public void preformMovement(Move move) {
        this.position = position.move(move);
    }

    public Position getPosition() {
        return this.position;
    }

    /**
     *
     * @return True if the UCMShip is alive, False otherwise.
     */
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Method that decreases the health of the UCMShip
     */
    public void receiveAttack() { this.health--; }

    public int getHealth() {
        return this.health;
    }
    public void setHealth(int health){
        this.health = health;
    }
}
