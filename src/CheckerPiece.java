/* 	Author: Angelo Chung                
*	Instructor: Ming Wu Chen                 
* 	Purpose:    Users are able to play checkers while being able to use in-game chat system.
*	@author Angelo Chung
*	@version 1.1 
*/

import java.awt.Color;

public class CheckerPiece {
	// Instant fields and constants
	private int type;
	private Color colour;
	public static int REGULAR = 0;
	public static int KING = 1;

	/**
	 * Constructs a checker piece
	 * 
	 * @param type 	the type of the piece wanted
	 * @param color the colour of the piece wanted
	 */
	public CheckerPiece(int type, Color colour) {
		this.type = type;
		this.colour = colour;
	}

	/**
	 * Returns the colour of the checker piece
	 * 
	 * @return 	the colour
	 */
	public Color getColour() {
		return colour;
	}

	/**
	 * Returns the type of the checker piece
	 * 
	 * @return 	the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Changes the Colour of a checker piece
	 * 
	 * @param colour 	the colour wanted
	 */
	public void setColour(Color colour) {
		this.colour = colour;
	}

	/**
	 * Changes the type of a checker piece
	 * 
	 * @param type 	the type wanted
	 */
	public void setType(int type) {
		this.type = type;
	}
}