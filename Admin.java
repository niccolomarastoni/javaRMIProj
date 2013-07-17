package tetraPong;

import java.rmi.Remote;
import java.rmi.RemoteException;
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
	
	public void ban(String user){
		try {
			mainRef.banUser(user);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void stopMatch(int gameID){
		try {
			mainRef.stopMatch(gameID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public long pingUser(String user){
		try {
			return mainRef.pingUser(user);
		} catch (RemoteException e) {
			return -1;
		}
	}
}
