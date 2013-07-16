package tetraPong;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface PlayerToMainInterface  extends Remote{
	public int getMatch(PlayerInterface player) throws RemoteException;
	public PlayerInterface getOpponent(int gameID,int id) throws RemoteException;
	public void unregisterPlayer(int gameID,int id,PlayerInterface palyer) throws RemoteException;
	public void errorSignal(int gameID,int id) throws RemoteException;
}
