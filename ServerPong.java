package tetraPong;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.rmi.MarshalledObject;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationID;
import java.rmi.activation.UnknownObjectException;
import java.rmi.server.Unreferenced;

import javax.swing.JFrame;

public class ServerPong extends Activatable 
implements PlayerInterface ,TetraPongProxy,MainToPlayerInterface,Unreferenced{
	private Pong pong;
	private PlayerInterface opponent = null;
	private PlayerToMainInterface mainRef = null;
	private int gameID;
	private int id;
	private String user;

	public ServerPong(ActivationID id, MarshalledObject data) throws IOException, ClassNotFoundException{
		super(id,30002);
		mainRef = (PlayerToMainInterface)data.get();
	}

	@Override
	public void activate(String user) throws RemoteException {
		this.user = user;
		pong = new Pong(this);
		System.out.println(this +" Activated ");
		if((gameID = mainRef.getMatch(this)) == -1)
			System.out.println("In Waiting Room");
		else if(gameID == -2){
			pong.showVictory(" BANNED", true, " Banned from the game");
		}

		else{
			id = 0;
			opponent = mainRef.getOpponent(gameID,1);
		}
	}

	@Override
	public void setBall(double dx, double dy, double x, double y) throws RemoteException{
		pong.ball.x = x;
		pong.ball.y = y;
		pong.dx = dx;
		pong.dy = dy;
	}

	@Override
	public void startGame(){
		System.out.println("Start game call received!");
		new Thread(pong).start();
		pong.starting = true;
		pong.running = true;
	}

	private void init() throws RemoteException{
		new Thread(pong).start();
		opponent.startGame();
		System.out.println("Starting!:D");
		pong.starting = true;
		pong.running = true;
		opponent.setBall(-pong.dx,-pong.dy, 250, 250);
	}

	protected void updateOppentScore() {
		boolean error = false;
		try {
			opponent.updateScore(pong.p2Score, pong.p1Score);
			pong.starting = true;
			opponent.setBall(-pong.dx,-pong.dy, 250, 250);
		} catch (RemoteException e) {
			error = true;
		}
		try {
			if(error)
				mainRef.errorSignal(gameID,id);
		} catch (RemoteException e1) {
			pong.showVictory("Connection Error", false, "Main Server Unreachable");
		}
	}

	protected void updateOpponentBase(){
		boolean error = false;
		try {
			opponent.Update(454 - pong.lowBase.x, 455 - pong.leftBase.y);
		} catch (RemoteException e) {
			error = true;
		}
		try {
			if(error)
				mainRef.errorSignal(gameID,id);
		} catch (RemoteException e1) {
			pong.showVictory("Connection Error", false, "Main Server Unreachable");
		}
	}

	protected void updateOpponentBallSpeed(){
		boolean error = false;
		try {
			opponent.setBall(-pong.dx,-pong.dy, 480- pong.ball.x, 480 - pong.ball.y);
		} catch (RemoteException e) {
			error = true;
		}
		try {
			if(error)
				mainRef.errorSignal(gameID,id);
		} catch (RemoteException e1) {
			pong.showVictory("Connection Error", false, "Main Server Unreachable");
		}
	}

	@Override
	public void updateScore(int p1Score, int p2Score) throws RemoteException {	
		pong.p1Score = p1Score;
		pong.p2Score = p2Score;
		if(p1Score == 5 || p2Score == 5){
			pong.starting = false;
			pong.running = false;
			pong.gameEnded = true;
			pong.showVictory("YOU WON", true);
		}
		else
			pong.starting = true;
	}

	@Override
	public void Update(double baseX, double baseY) throws RemoteException {
		if(pong != null){
			pong.rightBase.y = baseY;
			pong.highBase.x = baseX;
		}
	}

	@Override
	public void stopGame() throws RemoteException {
		pong.gameEnded = true;
		pong.running = false;
		pong.starting = false;
		pong.showVictory("Connection Error", false, "Game stopped by admin");
	}

	@Override
	public void gameReady(int gameID) throws RemoteException {
		opponent = mainRef.getOpponent(gameID,0);	
		this.gameID = gameID;
		id =1;
		System.out.println("Hello received player id = "+opponent.getUser());
		init();
	}

	@Override
	public void unreferenced() {
		System.out.println( this + " called unreferenced() method.");
		try {
			if(Activatable.unexportObject(this, false))
				Activatable.inactive(getID());
		} catch (NoSuchObjectException e) {
			e.printStackTrace();
		} catch (UnknownObjectException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ActivationException e) {
			e.printStackTrace();
		}
		System.gc();
	}

	void closeGame() {
		try {
			mainRef.unregisterPlayer(gameID,id,this);
		} catch (RemoteException e) {
			System.out.println("Unable to reache the Main Server.");
		}
		System.out.println("Successfully called unregister");
		pong.setVisible(false);
		pong.deleteProxyRef();
		pong.setEnabled(false);
		pong = null;
		System.gc();
	}

	@Override
	public void opponentLeft() throws RemoteException {
		pong.starting = false;
		pong.running = false;
		if(!pong.gameEnded)
			pong.showVictory("Connection Error", false, "Opponent left the game");
	}

	@Override
	public String getScore() throws RemoteException {
		return "" + pong.p1Score + " - " + pong.p2Score;
	}

	@Override
	public String toString(){
		return user;
	}

	@Override
	public String getUser() throws RemoteException {
		return user;
	}

	@Override
	public int ping() throws RemoteException {
		return 1;		
	}

}
