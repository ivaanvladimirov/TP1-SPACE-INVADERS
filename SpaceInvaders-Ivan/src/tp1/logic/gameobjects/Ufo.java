package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.Move;
import tp1.logic.Position;
import tp1.view.Messages;

/**
 * This class manages the UFO. <br>
 * Contains the attributes and the movement of the UFO.
 */

public class Ufo{
	//Constants
	public static final String SYMBOL = Messages.UFO_SYMBOL;
	public static final int SCORE = 25;
	//Attributes
	private final Game game;
	private int health = 1;
	private Position position;
	private final Move dir;

	/**
	 * Constructor for the UFO.
	 * @param game
	 */
	public Ufo(Game game) {
		this.game = game;
		this.position = new Position(Game.DIM_X, 0);
		this.dir = Move.LEFT;
	}

	/**
	 * Performs the movement of the Ufo.
	 * @param move is ALWAYS left.
	 */
	public void performMovement(Move move) {
		this.position = position.move(move);
	}

	/**
	 * Used to perform the movement automatically to the left, without the necessity to provide always the parameter "Left".
	 */
	public void automaticMove() {
		this.performMovement(dir);
	}

	/**
	 * Method used to check if the Ufo has received an attack from the UCMLaser.
	 * @return True if the Ufo has been shot, False otherwise
	 */
	public boolean receiveAttack() {
		if(--this.health == 0) {
			this.remove();
			return true;
		}
		return false;
	}

	public Position getPosition() {
		return this.position;
	}

	/**
	 * Method used to move the Ufo across the board in the X-axis and checking if it has left the board and disabling it.
	 */
	public void computerAction() {
		this.automaticMove();
		if (Game.isOutOfBoundX(this.position)){
			game.disableUfo();
		}
	}

	/**
	 * Removes the Ufo from the game if it has been destroyed or it has left the board.
	 */
	public void remove(){
		this.game.disableUfo();
	}

	/**
	 * Method that returns the symbol of the Ufo and its health
	 * @return U[health]
	 */
	public String getSymbol() {
		return SYMBOL + "[" + this.health + "]";
	}
}
