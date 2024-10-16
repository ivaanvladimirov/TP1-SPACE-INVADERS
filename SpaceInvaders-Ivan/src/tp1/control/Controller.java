package tp1.control;

import static tp1.view.Messages.debug;

import java.sql.SQLOutput;
import java.util.Scanner;

import tp1.logic.Game;
import tp1.logic.Move;
import tp1.logic.Position;
import tp1.logic.gameobjects.*;
import tp1.view.GamePrinter;
import tp1.view.Messages;

/**
 *  Accepts user input and coordinates the game execution logic
 */
public class Controller {

	private Game game;
	private Scanner scanner;
	private GamePrinter printer;

	public Controller(Game game, Scanner scanner) {
		this.game = game;
		this.scanner = scanner;
		printer = new GamePrinter(game);
	}

	/**
	 * Show prompt and request command.
	 *
	 * @return the player command as words
	 */
	private String[] prompt() {
		System.out.print(Messages.PROMPT);
		String line = scanner.nextLine();
		String[] words = line.toLowerCase().trim().split("\\s+");

		System.out.println(debug(line));

		return words;
	}

	/**
	 * Runs the game logic.
	 */
	public void run() {

		printGame();
		beginning: while(true) {
			if(game.playerWin() || game.aliensWin()) {
				printEndMessage();
				return;
			}

			String[] prompt = prompt();
			switch (prompt[0]) {
				// move [direction]
				case "move":
				case "m":
					Move move = switch (prompt[1]) {
						case "left" -> Move.LEFT;
						case "lleft" -> Move.LLEFT;
						case "right" -> Move.RIGHT;
						case "rright" -> Move.RRIGHT;
						default -> Move.NONE;
					};
					// move the ship and if return false -> ship couldn't be moved
					if(!this.game.moveShip(move)) {
						System.out.println(Messages.MOVEMENT_ERROR);
						continue beginning;
					}
					break;

                case "":
                    break;
				// shoot
				case "s":
					if (!game.LaserOn()){
						this.game.enableLaser();
						break;
					}
					else{
						System.out.println(Messages.LASER_ERROR);
						continue beginning;
					}

				// none
				case "n": break;

				// ask for help
				case "h":
					System.out.println(Messages.HELP);
					continue beginning;

				// displays the list of objects
				case "l":
					System.out.println(Messages.ucmShipDescription(
							Messages.UCMSHIP_DESCRIPTION,
							1, 3)
					);
					System.out.println(Messages.alienDescription(
							Messages.REGULAR_ALIEN_DESCRIPTION,
							RegularAlien.SCORE, 0, 2    )
					);
					System.out.println(Messages.alienDescription(
						Messages.DESTROYER_ALIEN_DESCRIPTION,
						DestroyerAlien.SCORE, 1, 1)
					);
					System.out.println(Messages.alienDescription(
							Messages.UFO_DESCRIPTION,
							Ufo.SCORE, 0, 1)
					);
					continue beginning;

				// reset
				case "r":
					this.game.resetGame();
					printGame();
					continue beginning;

				// end
				case "e":
					printEndMessage();
					return;
				case "w":
					boolean result = game.performShockWave();
					if(!result) {
						System.out.println(Messages.SHOCKWAVE_ERROR);
						continue beginning;
					}
                default:
                    System.out.println(Messages.UNKNOWN_COMMAND);
                    continue beginning;
            }




			this.game.performCycle();
			printGame();
		}
	}

	/**
	 * Draw / paint the game
	 */
	private void printGame() {
		System.out.println(printer);
	}

	/**
	 * Prints the final message once the game is finished
	 */
	public void printEndMessage() {
		System.out.println(printer.endMessage());
	}

}
