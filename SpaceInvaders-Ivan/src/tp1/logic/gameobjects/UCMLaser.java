package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.Move;
import tp1.logic.Position;
import tp1.view.Messages;

/**
 * 
 * Class that represents the laser fired by {@link UCMShip}
 *
 */
public class UCMLaser {
	public static final String SYMBOL = Messages.LASER_SYMBOL;
	private final Move dir;
	private final Game game;
	private Position position;

	public UCMLaser(Position position, Move dir, Game game) {
		this.game = game;
		this.position = position;
		this.dir = dir;
	}

	/**
	 *  Method called when the laser disappears from the board
	 */
	public void onDelete() {
		game.disableLaser();
	}

	/**
	 *  Implements the automatic movement of the laser	
	 */
	public void automaticMove () {
		performMovement(dir);
		if(isOut()) die();
	}

	private void die() {
		onDelete();
	}

	private boolean isOut() {
		return Game.isOutOfBoundY(this.position);
	}

	private void performMovement(Move dir) {
		this.position = position.move(dir);
	}

	/**
	 * Method that implements the attack by the laser to a regular alien.
	 * It checks whether both objects are alive and in the same position.
	 * If so call the "actual" attack method
	 * @param other the regular alien possibly under attack
	 * @return <code>true</code> if the alien has been attacked by the laser.
	 */
	public boolean performAttack(Alien other) {
		boolean isHit = this.position
				.move(dir)
				.equals(other.getPosition());

		return isHit && this.weaponAttack(other);
	}
	/**
	 * Method that implements the attack by the laser to the Ufo.
	 * It checks whether both objects are alive and in the same position.
	 * If so call the "actual" attack method
	 * @param ufo possibly under attack
	 * @return <code>true</code> if the ufo has been attacked by the laser.
	 */
	public boolean performAttack(Ufo ufo) {
		boolean isHit = this.position
				.move(dir)
				.equals(ufo.getPosition());

		return isHit && this.weaponAttack(ufo);
	}
	/**
	 * Method that implements the attack by the laser to a Bomb.
	 * It checks whether both objects are alive and in the same position.
	 * If so call the "actual" attack method
	 * @param bomb possibly under attack
	 * @return <code>true</code> if the bomb has been attacked by the laser.
	 */
	public boolean performAttack(Bomb bomb) {
		Position bombPos = bomb.getPosition();

		boolean isHit = this.position
				.equals(bombPos);

		boolean willHit = this.position
				.move(dir)
				.equals(bombPos);

		return (isHit || willHit) && this.weaponAttack(bomb);
	}

	/**
	 * 
	 * @param other regular alien under attack by the laser
	 * @return always returns <code>true</code>
	 */
	private boolean weaponAttack(Alien other) {
		this.game.disableLaser();
		other.receiveAttack();
		return true;
	}

	/**
	 *
	 * @param ufo under attack by the laser
	 * @return always returns <code>true</code>
	 */
	private boolean weaponAttack(Ufo ufo) {
		this.game.disableLaser();
		return ufo.receiveAttack();
	}
	/**
	 *
	 * @param bomb under attack by the laser
	 * @return always returns <code>true</code>
	 */
	private boolean weaponAttack(Bomb bomb) {
		this.game.disableLaser();
		return bomb.receiveAttack();
	}

	public Position getPosition() {
		return position;
	}
}
