package tetraPong;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MainToPlayerInterface extends Remote{
	public void gameReady(int gameID) throws RemoteException;
}
