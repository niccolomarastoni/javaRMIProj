package tetraPong;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface PlayerInterface extends Remote {
	public void setBall(double dx, double dy, double x, double y) throws RemoteException;
	public void Update(double baseX, double baseY) throws RemoteException;
	public void updateScore(int p1Score, int p2Score) throws RemoteException;
	public void startGame() throws RemoteException;
	public String getUser() throws RemoteException;
}