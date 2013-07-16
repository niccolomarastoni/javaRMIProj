package tetraPong;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AdminServerInterface extends Remote{
	public Object[][] getMatchData() throws RemoteException;
	public void banUser(String user) throws RemoteException;
	public boolean pingUser(String user) throws RemoteException;
}
