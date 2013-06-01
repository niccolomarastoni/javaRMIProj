import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ClientRemoteInterface extends Remote {
	public void getOpponent() throws MalformedURLException, RemoteException, NotBoundException;
	public void setBall(double dx, double dy, double x, double y) throws RemoteException;
	public void Update(double baseX, double baseY) throws RemoteException;
	public void updateScore(int p1Score, int p2Score) throws RemoteException;
	public void startGame() throws RemoteException;
}