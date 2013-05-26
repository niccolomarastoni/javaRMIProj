
public class TestGame { // per testare il gioco in locale
  private Pong game;
	public TestGame(){
<<<<<<< HEAD
		game = new Pong(250,265);
=======
		game = new Pong();
>>>>>>> 5ff721bf92b97b1a352c61bd0b84bcf7c8ac70f0
		game.startGame();
	}
	
	public static void main(String[] argv){
		new TestGame();
	}
}
