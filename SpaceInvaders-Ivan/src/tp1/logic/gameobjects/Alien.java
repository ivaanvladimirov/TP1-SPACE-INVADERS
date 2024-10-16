package tp1.logic.gameobjects;

import tp1.logic.AlienManager;
import tp1.logic.Move;
import tp1.logic.Position;

/**
 * Abstract class for the aliens. <br>
 * Implements the shared movement and other shared attributes of aliens
 */
public abstract class Alien {

    protected String symbol;
    protected int health;

    protected Move dir;

    protected Position position;
    protected AlienManager alienManager;

    public Alien(AlienManager alienManager, Position position){
        this.alienManager 	= alienManager;
        this.position 		= position;
    }

    /**
     *  Implements the automatic movement of the regular alien
     */
    public void automaticMove() {
        this.performMovement(dir);
    }

    /**
     * Method updates the position based on the current position and the direction of the move
     * @param dir direction of the move
     */
    private void performMovement(Move dir) {
        this.position = this.position.move(dir);
    }

    /**
     * changes direction and moves the alien down on the playing field
     */
    public void changeDirection() {
        this.position = position.move(Move.DOWN);
        this.dir = switch (this.dir) {
            case LEFT -> Move.RIGHT;
            case RIGHT -> Move.LEFT;
            default -> Move.NONE;
        };
    }

    /**
     * subtracts health from the alien
     * @return true if alien was destroyed else false
     */
    public boolean receiveAttack() {
        if(--this.health == 0) {
            this.alienManager.removeAlien(this);
            return true;
        }
        return false;
    }

    /**
     * Method return the string representation of Alien and its health
     * @return String - symbol[health]
     */
    public String getSymbol() {
        return this.symbol + "[" + this.health + "]";
    }

    public Position getPosition() {
        return position;
    }

    public int getHealth() {
        return this.health;
    }
}
