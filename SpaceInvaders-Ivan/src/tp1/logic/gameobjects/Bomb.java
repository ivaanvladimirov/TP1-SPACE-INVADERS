package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.Move;
import tp1.logic.Position;
import tp1.view.Messages;

public class Bomb {
    public static final String SYMBOL = Messages.BOMB_SYMBOL;
    private final Move dir = Move.DOWN;
    private int health = 1;
    private final DestroyerAlien alien;
    private Position position;

    public Bomb(DestroyerAlien alien) {
        this.alien = alien;
        this.position = alien.getPosition().move(dir);
    }

    public boolean receiveAttack() {
        if(--this.health == 0) {
            this.alien.disableBomb();
            return true;
        }
        return false;
    }

    public void onDelete() {
        alien.disableBomb();
    }

    private void die() {
        onDelete();
    }

    private void performMovement(Move dir) {
        this.position = position.move(dir);
    }
    private boolean isOut() {
        return Game.isOutOfBoundY(this.position);
    }

    public Position getPosition() { return this.position; }

    /**
     * Method that implements the attack by the bomb to UCMShip.
     * It checks whether both objects are alive and in the same position.
     * @return <code>true</code> if the ship has been attacked by the laser.
     */
    public boolean performAttack(UCMShip ship) {
        Position shipPos = ship.getPosition();
        boolean isHit = this.position
                .move(dir)
                .equals(shipPos);

        return isHit && this.weaponAttack(ship);
    }

    /**
     *
     * @param ship UCMShip under attack by the bomb
     * @return always returns <code>true</code>
     */
    private boolean weaponAttack(UCMShip ship) {
        alien.disableBomb();
        ship.receiveAttack();
        return true;
    }

    public void automaticMove () {
        performMovement(dir);
        if(isOut()) die();
    }
}
