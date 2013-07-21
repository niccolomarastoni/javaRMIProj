package tetraPong;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.Serializable;

public class Admin extends UnicastRemoteObject implements UserToAdminInterface{
	private AdminToServerInterface mainRef;
	private Remote adminRef;
	public Admin(AdminToServerInterface mainRef)throws RemoteException{
		super(30003);
		this.mainRef = mainRef;
	}
	
	@Override
	public void startAdmin() throws RemoteException{
		new AdminGUI(this);
	}
	
	protected Object[][] getMatchData() throws RemoteException {
		return mainRef.getMatchData();
	}
	
	protected void ban(String user){
		try {
			mainRef.banUser(user);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	protected void stopMatch(int gameID){
		try {
			mainRef.stopMatch(gameID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	protected long pingUser(String user){
		try {
			return mainRef.pingUser(user);
		} catch (RemoteException e) {
			return -1;
		}
	}
}
