package tetraPong;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;
import java.io.Serializable;

public class Admin extends UnicastRemoteObject implements AdminUserInterface, Serializable{
	private AdminServerInterface mainRef;
	private Remote adminRef;
	public Admin(AdminServerInterface mainRef)throws RemoteException{
		super(30003);
		this.mainRef = mainRef;
	}
	@Override
	public void startAdmin() throws RemoteException{
		new AdminGUI(this);
	}
	public Object[][] getMatchData() throws RemoteException {
		return mainRef.getMatchData();
	}
}
