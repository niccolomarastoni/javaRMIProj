package tetraPong;

import java.awt.event.WindowEvent;

public class TestGame { 
  private Pong game;
	public TestGame(){
		game = new Pong();
		game.startGame();
	}
	
	public static void main(String[] argv){
		new TestGame();
	}
}
