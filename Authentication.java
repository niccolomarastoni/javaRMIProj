package tetraPong;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Authentication extends Remote{
		public int login(String user, String pass) throws RemoteException;
		public MainInterface getMainServer() throws RemoteException;
		public boolean register(String user, String pass)throws RemoteException;

}
