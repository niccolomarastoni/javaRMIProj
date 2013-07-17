package tetraPong;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MainInterface extends Remote{
	SetupClient getClient() throws RemoteException;
	Admin getAdmin() throws RemoteException;
}
