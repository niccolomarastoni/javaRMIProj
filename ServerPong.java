package tetraPong;
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
			System.out.println("Waiting! :D");
		else if(gameID == -2){
			System.out.println("User "+user+ " banned popup");
			pong.f.dispatchEvent(new WindowEvent(pong.f, WindowEvent.WINDOW_CLOSING));
		}
		else{
			id = 0;
			opponent = mainRef.getOpponent(gameID,1);
		}


	}
	@Override
	public void setBall(double dx, double dy, double x, double y) throws RemoteException{
		//System.out.println("Setto dx = " + dx + " dy = " + dy);
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

	public void updateOppentScore() {
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
			e1.printStackTrace();
		}
	}

	public void updateOpponentBase(){
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
			e1.printStackTrace();
		}
	}
	public void updateOpponentBallSpeed(){
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
			e1.printStackTrace();
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
			pong.showVictory("YOU WON");
		}
		else
			pong.starting = true;
	}

	@Override
	public void Update(double baseX, double baseY) throws RemoteException {// i due giocatori hanno lo dx e dy uguali e opposti(in teoria)
		pong.rightBase.y = baseY;
		pong.highBase.x = baseX;
	}


	@Override
	public void stopGame() throws RemoteException {
		pong.gameEnded = true;
		pong.running = false;
		pong.starting = false;
		pong.showVictory("ADMIN STOP");
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
		System.out.println("Hello i'm a player. Please kill me!");
		try {
			if(Activatable.unexportObject(this, false))
				Activatable.inactive(getID());
		} catch (NoSuchObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ActivationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.gc();

	}

	void closeGame() {
		try {
			mainRef.unregisterPlayer(gameID,id,this);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			pong.showVictory("OPPONENT LEFT");

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
