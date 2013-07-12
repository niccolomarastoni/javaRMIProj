package tetraPong;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ProxyToMainInterface  extends Remote{
	public boolean registerPlayer(ClientRemoteInterface player) throws RemoteException;
	public ClientRemoteInterface getOpponent(int id) throws RemoteException;
	public void unregisterPlayer() throws RemoteException;
}
