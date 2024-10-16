package tp1.logic;

import tp1.logic.gameobjects.*;
import tp1.logic.lists.AlienList;


/**
 * Manages alien initialization and is used by aliens to coordinate movement.
 */
public class AlienManager {

	// instance attributes of game and level.
	private Level level;
	private Game game;

	// instance attributes for different states of the game
	private boolean squadInFinalRow = false, onBorder = false;
	private int cyclesToMove;

	// instance attributes of Lists of Aliens
	private AlienList regularAliens;
	private AlienList destroyerAliens;

	/**
	 * Constructor for AlienManager instance
	 * @param game
	 * @param level
	 */
	public AlienManager(Game game, Level level) {
		this.level = level;
		this.game = game;

		this.regularAliens = this.initializeRegularAliens();
		this.destroyerAliens = this.initializeDestroyerAliens();
		this.cyclesToMove = (level.numCyclesToMoveOneCell - 1);
	}
		
	/**
	 * Initializes the list of regular aliens
	 * @return the initial list of regular aliens according to the current level
	 */
	private AlienList initializeRegularAliens() {
		AlienList list = new AlienList(level.numRegularAliens);
		for (int row = 0, index = 0; row < level.numRowsRegularAliens; row++) {
			for (int col = 0; col < level.getNumAliensPerRow(); col++, index++) {
				int reqCenter = (Game.DIM_X/2) - (level.getNumAliensPerRow()/2);
				list.aliens[index] = new RegularAlien(
						this,
						new Position(col + reqCenter , row + 1)
				);
			}
		}
		return list;
	}

	/**
	 * Initializes the list of destroyer aliens
	 * @return the initial list of destroyer aliens according to the current level
	 */
	private AlienList initializeDestroyerAliens() {
		AlienList list = new AlienList(level.numDestroyerAliens);
		for(int i = 0; i < list.aliens.length; i++) {

			int reqCenter = (Game.DIM_X/2) - (level.getNumAliensPerRow()/2);
			int offset = reqCenter + (level.getNumAliensPerRow() / level.numDestroyerAliens) - 1;

			list.aliens[i] = new DestroyerAlien(
					this,
					new Position(i + offset, level.numRowsRegularAliens + 1)
			);
		}

		return list;
	}



	/**
	 * Returns one array of all the remaining aliens
	 * @return Alien[] remaining aliens
	 */
	public Alien[] getAliens() {
		Alien[] aliens = new Alien[regularAliens.aliens.length + destroyerAliens.aliens.length];
		System.arraycopy(regularAliens.aliens, 0, aliens, 0, regularAliens.aliens.length);
		System.arraycopy(destroyerAliens.aliens, 0, aliens, regularAliens.aliens.length, destroyerAliens.aliens.length);
		return  aliens;
	}

	/**
	 * Returns the number of all the remaining aliens within AlienManager Lists
	 * @return int
	 */
	public int getRemainingAliens() {
		return this.regularAliens.aliens.length + this.destroyerAliens.aliens.length;
	}

	public boolean getSquadInFinalRow() { return this.squadInFinalRow; }

	/**
	 * All destroyer aliens bombs will be checked if their bombs made contact with UCMShip or UCMLaser
	 * if it hit the laser - it will disable laser
	 * @param ship the UCMShip to be hit
	 * @param laser UCMLaser to be hit
	 */
	public void destroyerAttack(UCMShip ship, UCMLaser laser) {
		for (Alien a: destroyerAliens.aliens) {
			DestroyerAlien da = ((DestroyerAlien) a);
			if(da.isBombActive()) {
				if(laser != null && da.isBombHit(laser)) {
					game.disableLaser();
					return;
				}
				if(da.performAttack(ship)) return;
			}
		}
	}

	/**
	 * Will calculate the next movement and move all the aliens automatically
	 */
	public void automaticMove() {
		Alien[] aliens = getAliens();


		boolean nowOnBorder = false;
		for (Alien a: aliens) {
			if(Game.isOnBorderX(a.getPosition())) {
				nowOnBorder = true;
				break;
			}
		}
		for (Alien a: aliens) {
			if(a instanceof DestroyerAlien da) {
				da.moveBomb();
				if(game.tryFiringChance()) da.enableBomb();
			}
		}

		if(nowOnBorder) {
			if(onBorder) {
				// already was on border -> wait for move then reset onBorder
				if(cyclesToMove == 0) onBorder = false;

			} else {
				// newly on border -> descend now
				for (Alien a: aliens) {
					a.changeDirection();
					if(Game.isInFinalRow(a.getPosition()))
						this.squadInFinalRow = true;
				}
				if(cyclesToMove == 0) cyclesToMove++;
				onBorder = true;
			}
		}

		if(cyclesToMove-- == 0) {
			for (Alien a: aliens) a.automaticMove();
			// reset cycle counter back to default
			cyclesToMove = (level.numCyclesToMoveOneCell-1);
		}
	}

	/**
	 * Method that will reset itself to its initial state
	 */
	public void resetAliens() {
		this.destroyerAliens = this.initializeDestroyerAliens();
		this.regularAliens = this.initializeRegularAliens();
		this.onBorder = false;
	}

	/**
	 * Will remove alien from the corresponding list
	 * @param alien to be removed
	 */
	public void removeAlien(Alien alien)
	{
		if(alien instanceof DestroyerAlien)
			destroyerAliens.remove(alien);
		else if(alien instanceof  RegularAlien)
			regularAliens.remove(alien);
	}
}
