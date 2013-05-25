
public class TestGame { // per testare il gioco in locale
  private Pong game;
	public TestGame(){
		game = new Pong();
		game.startGame();
	}
	
	public static void main(String[] argv){
		new TestGame();
	}
}
