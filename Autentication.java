package tetraPong;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;


public interface Autentication extends Remote{
		public int login(String user, String pass) throws RemoteException;
		public MainInterface getMainServer() throws RemoteException;
		public boolean register(String user, String pass)throws RemoteException;

}
