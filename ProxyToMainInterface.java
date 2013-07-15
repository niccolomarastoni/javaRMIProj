package tetraPong;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ProxyToMainInterface  extends Remote{
	public int getMatch(PlayerInterface player) throws RemoteException;
	public PlayerInterface getOpponent(int gameID,int id) throws RemoteException;
	public void unregisterPlayer() throws RemoteException;
}
