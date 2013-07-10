package tetraPong;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;

public interface MainInterface extends Remote{
	SetupClient getClient() throws RemoteException;
	Admin getAdmin() throws RemoteException;
}
