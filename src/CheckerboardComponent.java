/* 	Author: Angelo Chung                
*	Instructor: Ming Wu Chen                 
* 	Purpose:    Users are able to play checkers while being able to use in-game chat system.
*	@author Angelo Chung
*	@version 1.1 
*/

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.Font;
import java.awt.Stroke;
import java.awt.Rectangle;
import java.awt.BasicStroke;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.PrintWriter;
import javax.swing.JPanel;
import javax.swing.JComponent;

/**
 * This component displays a checker board with pieces and player(s) can use it
 * but they have to do legal moves by dragging and dropping checker pieces using
 * a mouse commands.
 */
public class CheckerboardComponent extends JComponent {
	
	/**
	 * Constructs a checkerBoard and fills up the pieces array
	 */
	public CheckerboardComponent(PrintWriter out) {
		checkerBoard = new Rectangle[ROWS][COLUMNS];
		pieces = new CheckerPiece[ROWS][COLUMNS];
		fillArrayAndDrawStartPieces();

		this.out = out;

		class DragListener implements MouseMotionListener {
			public void mouseDragged(MouseEvent event) {
				// Get mouse position x, y
				int x = event.getX();
				int y = event.getY();
				processDragged(x, y);
			}

			public void mouseMoved(MouseEvent e) {
			}
		}

		class ReleaseListener implements MouseListener {
			public void mouseReleased(MouseEvent event) {
				// Get mouse position x, y
				int x = event.getX();
				int y = event.getY();
				processReleased(x, y);
			}

			public void mouseClicked(MouseEvent event) {
			}

			public void mousePressed(MouseEvent event) {
			}

			public void mouseEntered(MouseEvent event) {
			}

			public void mouseExited(MouseEvent event) {
			}
		}

		MouseMotionListener dragListener = new DragListener();
		addMouseMotionListener(dragListener);

		MouseListener releaseListener = new ReleaseListener();
		addMouseListener(releaseListener);
	}

	/**
	 * This paintComponent is where the drawing instructions are
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		// helper methods
		drawBoard(g2); 			// draw board
		drawCoordinates(g2);	// draw letter and number coordinate
		drawPieces(g2); 		// draw pieces
	}

	/**
	 * This is a helper method that fills up the pieces array with their initial
	 * game setup
	 */
	private void fillArrayAndDrawStartPieces() {
		for (int i = 0; i < ROWS; i++) { 				// i is rows
			for (int j = 0; j < COLUMNS; j++) { 		// j is columns
				if (i < LAYER_OF_PIECES) {				// LAYER_OF_PIECES = 3
					if ((i + j) % 2 == 1) {				// create white pieces
						pieces[i][j] = new CheckerPiece(CheckerPiece.REGULAR, Color.WHITE);
					} else
						pieces[i][j] = null;
					
				} else if (i > 1 + LAYER_OF_PIECES) {
					if ((i + j) % 2 == 1) {				// create black pieces
						pieces[i][j] = new CheckerPiece(CheckerPiece.REGULAR, Color.BLACK);
					} else
						pieces[i][j] = null;
					
				} else {
					pieces[i][j] = null;
				}
			}
		}
	}

	/*
	 * This is a helper method that draws all pieces
	 */
	private void drawPieces(Graphics2D g2) {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (pieces[i][j] != null) {
					if (pieces[i][j].getType() == CheckerPiece.REGULAR) {
						drawRegularPiece(i, j, g2);
					} else if (pieces[i][j].getType() == CheckerPiece.KING) {
						drawKingPiece(i, j, g2);
					}
				}
			}
		}
	}

	/*
	 * This is a helper method that draws the coordinates 1-8 for the row and
	 * a-h for the column
	 */
	private void drawCoordinates(Graphics2D g2) {
		for (int i = ROWS; i > 0; i--) {
			g2.setColor(Color.BLUE);
			g2.setFont(THE_FONT);
			g2.drawString(i + "", MIDDLE_OF_SQUARE, MARGIN + MIDDLE_OF_SQUARE + SIDE * (ROWS - i));
		}
		for (int j = 0; j < COLUMNS; j++) {
			char columnChar = (char) ('a' + j);
			g2.setColor(Color.BLUE);
			g2.setFont(THE_FONT);
			g2.drawString(columnChar + "", MARGIN + MIDDLE_OF_SQUARE + (SIDE * j), MARGIN + ROWS * SIDE + MIDDLE_OF_SQUARE);
		}
	}

	/**
	 * This is a helper method that draws the board
	 */
	private void drawBoard(Graphics2D g2) {
		// ~~ Draw the board ~~///
		for (int i = 0; i < ROWS; i++) 				// i is rows
		{
			for (int j = 0; j < COLUMNS; j++) 		// j is columns
			{
				checkerBoard[i][j] = new Rectangle(j * SIDE + MARGIN, i * SIDE
						+ MARGIN, SIDE, SIDE);
				if ((i + j) % 2 == 0) {
					g2.setColor(FLESH);
					g2.fill(checkerBoard[i][j]);
					g2.draw(checkerBoard[i][j]);
				} else {
					g2.setColor(BROWN);
					g2.fill(checkerBoard[i][j]);
					g2.draw(checkerBoard[i][j]);
				}
			}
		}
	}

	/**
	 * This is a helper method that draws a regular checker piece
	 * 
	 * @param i		
	 * @param j		
	 * @param g2	
	 */
	private void drawRegularPiece(int i, int j, Graphics2D g2) {
		Ellipse2D.Double ellipse = new Ellipse2D.Double(j * SIDE + MARGIN + 5, i * SIDE + MARGIN + 5, DIAMETER, DIAMETER);
		g2.setStroke(THICKNESS);
		g2.setColor(Color.BLACK);
		// checker piece
		g2.draw(ellipse);
		g2.setColor(pieces[i][j].getColour());
		g2.fill(ellipse);
	}

	/**
	 * This is a helper method that draws a king checker piece
	 */
	private void drawKingPiece(int i, int j, Graphics2D g2) {
		Ellipse2D.Double ellipse = new Ellipse2D.Double(j * SIDE + MARGIN + 5, i * SIDE + MARGIN + 5, DIAMETER, DIAMETER);
		Ellipse2D.Double smallDot = new Ellipse2D.Double(j * SIDE + MARGIN + PUT_IN_MIDDLE, i * SIDE + MARGIN + PUT_IN_MIDDLE,	SMALL_DOT_DIAMETER, SMALL_DOT_DIAMETER);
		g2.setStroke(THICKNESS);
		g2.setColor(Color.BLACK);
		// checker piece
		g2.draw(ellipse);
		g2.setColor(pieces[i][j].getColour());
		g2.fill(ellipse);
		// small dot
		g2.draw(smallDot);
		g2.setColor(Color.RED);
		g2.fill(smallDot);
	}

	/**
	 * Drags the checker piece. It also checks if the user does legal moves
	 * 
	 * @param x	the x pixel coordinate
	 * @param y	the y pixel coordinate
	 */
	public void processDragged(int x, int y) {

		row = (int) (Math.floor((double) (y - MARGIN) / SIDE));
		col = (int) (Math.floor((double) (x - MARGIN) / SIDE));
		// System.out.println("col:"+col+"row"+row);

		if (onDrag == false && row < ROWS && col < COLUMNS && row >= 0
				&& col >= 0) {
			returnToRow = row;
			returnToCol = col;
			if (pieces[row][col] != null) {
				colourOfDraggedPiece = pieces[row][col].getColour();
				typeOfDraggedPiece = pieces[row][col].getType();

				if (colourOfDraggedPiece == Color.WHITE)
					colourInt = WHITE_COLOUR;
				else
					colourInt = BLACK_COLOUR;

				removePiece(row, col);

				out.println("removePiece " + row + " " + col);
				out.flush();

				onDrag = true;
			}
		}
	}

	/**
	 * Drops the checker piece. It also checks if the user does legal moves
	 * 
	 * @param x	the x pixel coordinate
	 * @param y	the y pixel coordinate
	 */
	public void processReleased(int x, int y) {

		row = (int) (Math.floor((double) (y - MARGIN + 5) / SIDE));
		col = (int) (Math.floor((double) (x - MARGIN + 5) / SIDE));
		// System.out.println("col:"+col+"row"+row);
		if (onDrag && row < ROWS && col < COLUMNS && row >= 0 && col >= 0) {
			if (pieces[row][col] == null) {
				if (colourOfDraggedPiece == Color.BLACK	&& row == KING_THE_BLACK) {
					placePiece(row, col, CheckerPiece.KING,	colourOfDraggedPiece);
					out.println("placePiece " + row + " " + col + " " + CheckerPiece.KING + " " + colourInt);
					out.flush();
				} else if (colourOfDraggedPiece == Color.WHITE && row == KING_THE_WHITE) {
					placePiece(row, col, CheckerPiece.KING,	colourOfDraggedPiece);
					out.println("placePiece " + row + " " + col + " " + CheckerPiece.KING + " " + colourInt);
					out.flush();
				} else {
					placePiece(row, col, typeOfDraggedPiece, colourOfDraggedPiece);
					out.println("placePiece " + row + " " + col + " " + typeOfDraggedPiece + " " + colourInt);
					out.flush();
				}
			} else if (pieces[row][col] != null) {
				placePiece(returnToRow, returnToCol, typeOfDraggedPiece, colourOfDraggedPiece);
				out.println("placePiece " + returnToRow + " " + returnToCol + " " + typeOfDraggedPiece + " " + colourInt);
				out.flush();
			}
		}
		onDrag = false;
	}

	/**
	 * Draws a new piece on the checker board.
	 * 
	 * @param row 		the row where the user wants to put the piece
	 * @param column 	the columm where the user wants to put the piece
	 * @param type 		the type of the piece
	 * @param colour 	the colour of the piece
	 */
	public void placePiece(int row, int column, int type, Color colour) {
		pieces[row][column] = new CheckerPiece(type, colour);
		repaint();
	}

	/**
	 * Removes a piece on the checker board.
	 * 
	 * @param row 		the row where the user wants to remove the piece
	 * @param column 	the columm where the user wants to remove the piece
	 */
	public void removePiece(int row, int column) {
		pieces[row][column] = null;
		repaint();
	}

	// Instant fiends and constants
	private int row;
	private int col;
	private int returnToRow;
	private int returnToCol;
	private int colourInt;
	private Rectangle[][] checkerBoard;
	private CheckerPiece[][] pieces;
	private boolean onDrag;
	private Color colourOfDraggedPiece;
	private int typeOfDraggedPiece;
	private PrintWriter out;

	// Declare colours
	public final Color FLESH = new Color(251, 212, 180);
	public final Color BROWN = new Color(204, 102, 0);

	// Declare font
	private final Font THE_FONT = new Font("arial", Font.BOLD, 18);

	// Declare stroke thickness
	private final Stroke THICKNESS = new BasicStroke(2);

	// Declare initial constants
	public final int WHITE_COLOUR = 0;
	private final int BLACK_COLOUR = 1;
	private final int KING_THE_BLACK = 0;
	private final int KING_THE_WHITE = 7;
	private final int ROWS = 8;
	private final int COLUMNS = 8;
	private final int SIDE = 50;
	private final int MARGIN = 50;
	private final int DIAMETER = 40;
	private final int SMALL_DOT_DIAMETER = 10;
	private final int MIDDLE_OF_SQUARE = 25;
	private final int LAYER_OF_PIECES = 3;
	private final int PUT_IN_MIDDLE = 20;
}
