/* 	Author: Angelo Chung                
*	Instructor: Ming Wu Chen                 
* 	Purpose:    Users are able to play checkers while being able to use in-game chat system.
*	@author Angelo Chung
*	@version 1.1 
*/

import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.util.Scanner;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.IOException;

public class CheckersGame implements Runnable {
	/**
	 * Constructs a checkers game and initializing all needed classes for the interface.
	 * This creates the layout of the game including buttons players, chatbox, and the checkers board.
	 * 
	 * @param socket 	the connection socket 
	 */
	public CheckersGame(Socket socket) throws IOException {
		this.socket = socket;

		frame = new JFrame();
		introLabel = new JLabel("Welcome to Checkers Game");
		whiteLabel = new JLabel("White : ");
		blackLabel = new JLabel("Black : ");
		statusLabel = new JLabel();
		whiteNameField = new JTextField(6);
		blackNameField = new JTextField(6);
		inputChatField = new JTextField(50);
		outputChatField = new JTextArea(10, 60);
		scrollPane = new JScrollPane(outputChatField);
		introPanel = new JPanel();
		playerStatusPanel = new JPanel();
		whitePanel = new JPanel();
		blackPanel = new JPanel();
		outputChatPanel = new JPanel();
		westBorderPanel = new JPanel();
		southBorderPanel = new JPanel();
		eastBorderPanel = new JPanel();
		readyButtonPanel = new JPanel();
		sitWhiteButtonPanel = new JPanel();
		sitBlackButtonPanel = new JPanel();
		sitWhiteButton = new JButton("Sit !!");
		sitBlackButton = new JButton("Sit !!");
		readyButton = new JButton("Ready !!");

		in = new Scanner(socket.getInputStream());
		out = new PrintWriter(socket.getOutputStream());

		northBorderLayout();
		westBorderLayout();
		southBorderLayout();
		eastBorderLayout();

		ActionListener listener = new inputChatListener();
		inputChatField.addActionListener(listener);

		frame.setSize(FRAME_SIDE, FRAME_SIDE);
		frame.setTitle("Checker Board!!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		component = new CheckerboardComponent(out);
		frame.add(component, BorderLayout.CENTER);
		frame.setVisible(true);

	}

	/**
	 * Helper method that runs doService() method and catches IOExceptions.
	 * Makes sures it closes the socket variable.
	 */
	public void run() {
		try {
			try {
				doService();
			} finally {
				socket.close();
			}
		} catch (IOException e) {
			// do nothing
		}
	}
	
	/**
	 * Accepts string singals to execute certain commands.
	 * 
	 * @throws IOException
	 */
	public void doService() throws IOException {
		while (true) {
			if (!in.hasNext())return;
			
			String command = in.next();

			if (command.equals("disableWhiteButton")) {
				sitWhiteButton.setEnabled(false);
				whiteNameField.setText(in.next());
				
			} else if (command.equals("disableBlackButton")) {
				sitBlackButton.setEnabled(false);
				blackNameField.setText(in.next());
				
			} else if (command.equals("inputChat")) {
				String chat = in.nextLine();
				outputChatField.append(chat + "\n");
				
			} else if (command.equals("removePiece")) {
				int row = in.nextInt();
				int col = in.nextInt();
				component.removePiece(row, col);
				
			} else if (command.equals("placePiece")) {
				int row = in.nextInt();
				int col = in.nextInt();
				int type = in.nextInt();
				int colour = in.nextInt();
				if (colour == WHITE_COLOR)
					pieceColour = Color.WHITE;
				else
					pieceColour = Color.BLACK;

				component.placePiece(row, col, type, pieceColour);
			}
		}
	}
	
	/**
	 * Initializes all designs needed for the north side of the boarder.
	 */
	public void northBorderLayout() {
		introLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 36));

		introPanel.add(introLabel);
		introPanel.setBackground(Color.ORANGE);

		frame.add(introPanel, BorderLayout.NORTH);
	}

	/**
	 * Initializes all designs needed for the west side of the boarder.
	 */
	public void westBorderLayout() {
		createReadyButton().setBackground(Color.ORANGE);

		westBorderPanel.setLayout(new GridLayout(1, 1));
		westBorderPanel.add(createReadyButton());

		frame.add(westBorderPanel, BorderLayout.WEST);
	}

	/**
	 * Initializes all designs needed for the south side of the boarder.
	 */
	public void southBorderLayout() {
		outputChatPanel.add(scrollPane);

		southBorderPanel.add(inputChatField);
		southBorderPanel.add(outputChatPanel);

		frame.add(southBorderPanel, BorderLayout.SOUTH);
	}

	/**
	 * Initializes all designs needed for the east side of the boarder.
	 */
	public void eastBorderLayout() {
		eastBorderPanel.setLayout(new GridLayout(3, 1));
		whitePanel.setBackground(Color.WHITE);
		blackPanel.setBackground(Color.BLACK);
		whiteLabel.setForeground(Color.BLACK);
		blackLabel.setForeground(Color.WHITE);

		playerStatusPanel.setLayout(new GridLayout(2, 1));
		playerStatusPanel.add(statusLabel);

		whitePanel.add(whiteLabel);
		blackPanel.add(blackLabel);

		whitePanel.add(whiteNameField);
		blackPanel.add(blackNameField);

		whitePanel.add(createSitWhiteButton());
		blackPanel.add(createSitBlackButton());

		eastBorderPanel.add(playerStatusPanel);
		eastBorderPanel.add(whitePanel);
		eastBorderPanel.add(blackPanel);

		frame.add(eastBorderPanel, BorderLayout.EAST);
	}

	/**
	 * Creates a panel then initializes a button that implements the ActionListener interface. 
	 * Button notifies the other player 'ready'.
	 * 
	 * @return a panel with a ready button
	 */
	private JPanel createReadyButton() {
		class ReadyButtonListener implements ActionListener {

			public void actionPerformed(ActionEvent event) {
				out.println("inputChat " + playerName + " IS READY!!");
				out.flush();
				readyButton.setEnabled(false);
			}
		}
		ActionListener listener = new ReadyButtonListener();
		readyButton.addActionListener(listener);

		readyButtonPanel.add(readyButton);

		return readyButtonPanel;
	}

	/**
	 * Creates a panel then initializes a button that implements the ActionListener interface. 
	 * A user can choose to which colour they want to play in.
	 * 
	 * @return a panel with a sit in colour button
	 */
	private JPanel createSitWhiteButton() {
		class SitWhiteButtonListener implements ActionListener {

			public void actionPerformed(ActionEvent event) {
				sitWhiteButton.setEnabled(false);
				sitBlackButton.setEnabled(false);
				statusLabel.setText("You play WHITE");
				playerName = whiteNameField.getText();
				out.println("disableWhiteButton " + playerName);
				out.flush();
			}
		}
		ActionListener listener = new SitWhiteButtonListener();
		sitWhiteButton.addActionListener(listener);

		sitWhiteButtonPanel.add(sitWhiteButton);

		return sitWhiteButtonPanel;
	}

	/**
	 * Creates a panel then initializes a button that implements the ActionListener interface. 
	 * A user can choose to which colour they want to play in.
	 * 
	 * @return a panel with a sit in colour button
	 */
	private JPanel createSitBlackButton() {
		class SitBlackButtonListener implements ActionListener {

			public void actionPerformed(ActionEvent event) {
				sitBlackButton.setEnabled(false);
				sitWhiteButton.setEnabled(false);
				statusLabel.setText("You play BLACK");
				playerName = blackNameField.getText();
				out.println("disableBlackButton " + playerName);
				out.flush();
			}
		}
		ActionListener listener = new SitBlackButtonListener();
		sitBlackButton.addActionListener(listener);
		sitBlackButtonPanel.add(sitBlackButton);

		return sitBlackButtonPanel;
	}

	/**
	 * Creates a chat class that implements ActionListener interface.
	 * Users can communicate to each other.
	 *
	 */
	class inputChatListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			out.println("inputChat " + playerName + ": "+ inputChatField.getText());
			out.flush();

			outputChatField.append(" " + playerName + ": "+ inputChatField.getText() + "\n");
			inputChatField.setText("");
		}
	}
	
	// Instant fields and constants
		private CheckerboardComponent component;
		private Color pieceColour;
		private Color playerColour;
		private Socket socket;
		private Scanner in;
		private PrintWriter out;
		private String playerName;
		private JFrame frame;
		private JLabel introLabel;
		private JLabel whiteLabel;
		private JLabel blackLabel;
		private JLabel statusLabel;
		private JTextField whiteNameField;
		private JTextField blackNameField;
		private JTextField inputChatField;
		private JTextArea outputChatField;
		private JScrollPane scrollPane;
		private JPanel introPanel;
		private JPanel outputChatPanel;
		private JPanel whitePanel;
		private JPanel blackPanel;
		private JPanel playerStatusPanel;
		private JPanel westBorderPanel;
		private JPanel southBorderPanel;
		private JPanel eastBorderPanel;
		private JPanel readyButtonPanel;
		private JPanel sitWhiteButtonPanel;
		private JPanel sitBlackButtonPanel;
		private JButton sitWhiteButton;
		private JButton sitBlackButton;
		private JButton readyButton;

		private final int WHITE_COLOR = 0;
		private static final int FRAME_SIDE = 765;
}