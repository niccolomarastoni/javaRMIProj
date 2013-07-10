package tetraPong;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TetraPongProxy extends Remote{
	public void activate() throws RemoteException;

}
