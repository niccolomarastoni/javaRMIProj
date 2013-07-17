package tetraPong;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MainToPlayerInterface extends Remote{
	public void gameReady(int gameID) throws RemoteException;
	public void opponentLeft() throws RemoteException;
	public String getScore() throws RemoteException;
	public void stopGame() throws RemoteException;
	public int ping() throws RemoteException;
}
