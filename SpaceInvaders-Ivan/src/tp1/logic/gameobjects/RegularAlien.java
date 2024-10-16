package tp1.logic.gameobjects;

import tp1.logic.AlienManager;
import tp1.logic.Move;
import tp1.logic.Position;
import tp1.view.Messages;

/**
 * 
 * Class representing a regular alien. <br>
 * Extends the abstract class of Alien.
 *
 */
public class RegularAlien extends Alien {
	public static final int SCORE = 5;
	public RegularAlien(AlienManager alienManager, Position position) {
		super(alienManager, position);
		this.dir = Move.LEFT;
		this.symbol = Messages.REGULAR_ALIEN_SYMBOL;
		this.health = 2;
	}


}