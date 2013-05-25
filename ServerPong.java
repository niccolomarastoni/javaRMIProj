import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;


public class ServerPong extends RemoteServer implements ClientRemoteInterface{
  String ip = "//157.27.241.183";
	private Pong pong;
	private ClientRemoteInterface opponent = null;
	private boolean myTurn = true;

	public ServerPong(){
		pong = new Pong();
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			UnicastRemoteObject.exportObject(this, 5000);
			registry.rebind("PongServer", this);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void getOpponent() throws MalformedURLException, RemoteException, NotBoundException{
		opponent = (ClientRemoteInterface)Naming.lookup(ip + ":1099/PongServer");
	}

	public void setOpponent() throws RemoteException{
		if(myTurn)
			opponent.setBallSpeed(pong.getDx(),pong.getDy());

		//System.out.println(opponent);
	}

	public void setBallSpeed(double dx, double dy){
		pong.setBallSpeed(dx, dy);
	}

	public static void main(String[] argv){
		ServerPong sPong = new ServerPong();

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Server attivo\nPremi invio");
		try {
			in.readLine();
			sPong.getOpponent();
			sPong.setOpponent();
			sPong.init();
		} 
		catch (RemoteException e){
			// jizz
		}catch (IOException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	private void init() throws RemoteException{
		double [] update;
		pong.startGame(); // facciamolo thread, che non muoia!!
		
		while(true){
			if(pong.opponentTurn()){
				update = opponent.getUpdate();
				check(update);
			}
		}
	}

	private void check(double[] update) {
		double delta = 0.5; 
		if(Math.abs(pong.getBallX() - update[0]) > delta || Math.abs(pong.getBallY() - update[1]) > delta ){
			pong.setBallPosition(update[0], update[1]);
		}
		
		if(pong.getDx() != update[2] || pong.getDy() != update[3]){
			pong.setBallSpeed(update[2], update[3]);
		}
	}

	@Override
	public double[] getUpdate() throws RemoteException {
		double [] ret = {pong.getBallX(), pong.getBallY(), pong.getDx(), pong.getDy()};
		return ret;
	}

}
