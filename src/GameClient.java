/* 	Author: Angelo Chung                
*	Instructor: Ming Wu Chen                 
* 	Purpose:    Users are able to play checkers while being able to use in-game chat system.
*	@author Angelo Chung
*	@version 1.1 
*/

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class GameClient {
	
	/**
	 * Runs a game client to connect to a specific sever
	 */
	public static void main(String[] args) throws IOException {
		Socket s = new Socket("localhost", 8888);

		CheckersGame game = new CheckersGame(s);
		Thread t = new Thread(game);
		t.start();
	}
}