package tp1.logic;

import tp1.logic.gameobjects.*;
import tp1.view.Messages;

import java.util.Random;

/**
 * The single instance of game will chain all game objects together and keep reference to them.
 * The game instance will also keep the current score, cycle and shockwave.
 * The game will execute the cycle.
 *
 */
public class Game {
	// class constants
	public static final int DIM_X = 9, DIM_Y = 8;

	// instance game object attributes
	private UCMShip UCMship;
	private UCMLaser laser;
	private Ufo ufo;
	private AlienManager alienManager;

	// instance final attributes
	private final Level level;
	private Random random;
	private final long seed;

	// instance attributes
	private int cycleCount = 0;
	private int points = 0;
	private boolean shockWave = false;

	/**
	 * Game constructor
	 * @param level
	 * @param seed
	 */
	public Game(Level level, long seed) {
		this.level 	= level;
		this.seed	= seed;
		this.alienManager = new AlienManager(this, level);
		this.UCMship = new UCMShip();
		this.random = new Random(seed);
	}

	/**
	 * Method will return String with the current state of the game.
	 * Life, Points, Shockwave,
	 *
	 * @return String current state of game formatted
	 */
	public String stateToString() {
		String sb = "Life: " + this.UCMship.getHealth() + System.lineSeparator() +
				"Points: " + this.points + System.lineSeparator() +
				"ShockWave: " + (shockWave ? "ON" : "OFF") + System.lineSeparator();
		return sb;
	}

	/** @return current cycle number */
	public int getCycle() {
		return this.cycleCount;
	}
	public Level getLevel() { return this.level; }
	public int getRemainingAliens() {
		return alienManager.getRemainingAliens();
	}

	/**
	 * Will randomly evaluate if a bomb should be dropped based on the current level
	 * @return true if shot should be fired
	 */
	public boolean tryFiringChance() { return random.nextDouble() < level.shootFrequency; }

	/**
	 * Will randomly evaluate if a UFO should be spawned based on the current level
	 * @return true if UFO should be spawned
	 */
	public boolean tryUfoSpawnChange() { return this.ufo == null && random.nextDouble() < level.ufoFrequency; }

	/**	@return true if the player has won else false otherwise */
	public boolean playerWin() {
		return alienManager.getRemainingAliens() == 0;
	}

	/**	@return true if the aliens have won else false otherwise */
	public boolean aliensWin() {
		return inFinalRow() || !this.UCMship.isAlive();
	}
	public boolean inFinalRow(){
		if(alienManager.getSquadInFinalRow()){
			UCMship.setHealth(-200);
			return true;
		}
		else
			return false;
	}
	/**
	 * Returns the string representative of the position in the play field based on which element is within it.
	 *
	 * @param col integer [0-{@value DIM_X}] of the column
	 * @param row integer [0-{@value DIM_Y}] of the column
	 * @return String representative of the current grid position
	 */
	public String positionToString(int col, int row) {

		if(ufo != null && ufo.getPosition().equals(col, row))
			return ufo.getSymbol();

		if (UCMship.getPosition().equals(col, row) && aliensWin())
			return Messages.UCMSHIP_DEAD_SYMBOL;

		else if(UCMship.getPosition().equals(col, row))
			return UCMShip.SYMBOL;



		for (Alien alien: alienManager.getAliens()) {
			if (alien.getPosition().equals(col, row)) {
				return alien.getSymbol();
			}
			//Prints the bombs if they are active and if they are in the board, returning the symbol.
			if (alien instanceof DestroyerAlien da) {
				if (da.isBombActive() && da.getBombPosition().equals(col, row)) {
					return Bomb.SYMBOL;
				}
			}
		}

		if(laser != null && laser.getPosition().equals(col, row))
			return UCMLaser.SYMBOL;

		return "";
	}

	public void enableLaser() {
		if(laser != null) return;

		laser = new UCMLaser(
			this.UCMship.getPosition(),
			Move.UP, this
		);
	}

	public void disableLaser() {
		this.laser = null;
	}

	public void disableUfo() {
		this.ufo = null;
	}

	public void enableUfo() {
		this.ufo = new Ufo(this);
	}

	public static boolean isOnBorderX(Position position) {
		return (position.col >= DIM_X-1 || position.col <= 0);
	}

	public static boolean isOutOfBoundX(Position position) {
		return (position.col >= DIM_X || position.col < 0);
	}

	public static boolean isInFinalRow(Position position) {
		return position.row == Game.DIM_Y - 1;
	}

	public static boolean isOutOfBoundY(Position position) {
		return (position.row >= DIM_Y || position.row < 0);
	}

	/**
	 * Method that executes the cycle by moving aliens,
	 * moving bombs and lasers and checking for collisions between them.
	 * It takes care of the score and current cycle.
	 */
	public void performCycle() {
		// increment cycle
		cycleCount++;

		// if UFO is null and random true -> spawn ufo
		if(tryUfoSpawnChange()) enableUfo();

		// calculate ufo action if exists
		if(ufo != null) ufo.computerAction();

		// Bombs attack UCMShip
		alienManager.destroyerAttack(UCMship, laser);

		// move aliens
		alienManager.automaticMove();

		// move laser if exists
		if(laser != null) {
			Alien[] aliens = alienManager.getAliens();
			boolean collision; int index = 0;

			do {
				Alien hitAlien = aliens[index];
				collision = laser.performAttack(hitAlien);

				if(hitAlien.getHealth() == 0) {
					if(hitAlien instanceof RegularAlien)
						points += RegularAlien.SCORE;
					else if(hitAlien instanceof DestroyerAlien)
						points += DestroyerAlien.SCORE;
				}

			} while (!collision && ++index < aliens.length);
			// while there is no collision and there are still untested aliens

			// if laser is about to be on highest row AND there was no collision AND ufo is set
			if(!collision && laser.getPosition().inRow(1) && ufo != null) {
				// performAttack on UFO
				collision = laser.performAttack(ufo);
				// ufo was shot down
				if(collision) {
					shockWave = true;
					points += Ufo.SCORE;
				}
			}

			if(!collision) laser.automaticMove();
		}
	}

	/**
	 * attempt to move the UCMShip
	 * @param move Direction of the attempted move
	 * @return true if attempt to move was successful, false otherwise
	 */
	public boolean moveShip(Move move)
	{
		if(Game.isOutOfBoundX(UCMship.getPosition().move(move)))
			return false;

		UCMship.preformMovement(move);
		return true;
	}

	public boolean LaserOn(){
		if (this.laser == null)
			return false;
		else
			return true;
	}

	/**
	 * Will reset the cycle count, aliens, UCMShip and disable all bombs and laser.
	 */
	public void resetGame()
	{
		cycleCount = 0;
		alienManager.resetAliens();
		UCMship = new UCMShip();
		disableLaser();
		if(ufo != null) ufo.remove();
	}

	/**
	 * Method that will perform shockwave if possible.
	 * @return true if shockwave performed.
	 */
	public boolean performShockWave()
	{
		if(!shockWave) return false;
		for (Alien a: alienManager.getAliens()) a.receiveAttack();
		shockWave = false;
		return true;
	}
}
