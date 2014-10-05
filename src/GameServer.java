/* 	Author: Angelo Chung                
*	Instructor: Ming Wu Chen                 
* 	Purpose:    Users are able to play checkers while being able to use in-game chat system.
*	@author Angelo Chung
*	@version 1.1 
*/

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class GameServer {

	/**
	 * Runs a game server where clients can connect to
	 */
	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(8888);
		System.out.println("waiting for clients...");

		while (true) {
			Socket s = server.accept();
			System.out.println("someone conencted!!");
			CheckersGame game = new CheckersGame(s);
			Thread t = new Thread(game);
			t.start();
		}
	}
}