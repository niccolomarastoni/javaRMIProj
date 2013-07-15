package tetraPong;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AdminServerInterface extends Remote{
	public Object[][] getMatchData() throws RemoteException;
}
