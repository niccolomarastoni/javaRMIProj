package tetraPong;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Bootstrap extends Remote {
	public Runnable getClient() throws RemoteException;
}
