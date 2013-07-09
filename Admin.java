package tetraPong;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;
import java.io.Serializable;

public class Admin extends UnicastRemoteObject implements AdminUserInterface, Serializable{
	//costruttore che prende come input main server
	private AdminServerInterface mainRef;
	private Remote adminRef;
	public Admin(AdminServerInterface mainRef)throws RemoteException{
		super(30001);
		this.mainRef = mainRef;
	}
	@Override
	public void startAdmin() throws RemoteException{
		//init() interfaccia grafica
		System.out.println("Ciao sono io amore mio pensavo a te, vieni appena puoi anche tardi se tu vuoi!");
		//adminRef = (Remote)UnicastRemoteObject.exportObject(this, 0);
		System.out.println("Main server says: " + mainRef.registerAdmin(this));
		System.exit(0);
	}
}
