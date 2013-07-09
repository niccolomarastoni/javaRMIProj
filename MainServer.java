package tetraPong;

import java.rmi.MarshalledObject;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationID;
import java.rmi.activation.UnknownObjectException;
import java.rmi.server.RemoteObject;
import java.rmi.server.Unreferenced;
import java.rmi.server.UnicastRemoteObject;

public class MainServer extends Activatable implements Unreferenced, MainInterface, AdminServerInterface{
	private AdminUserInterface admin;
	protected MainServer(ActivationID id, MarshalledObject data) throws RemoteException {
		super(id, 3001);
		System.out.println("Main su le mani!" + id);
	}

	// ciao grazie
	@Override
	public void unreferenced() {	
		System.out.println("I'm trying to die!(Main)");
		try {
			if(Activatable.unexportObject(this, true))
				Activatable.inactive(getID());
		} catch (NoSuchObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ActivationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.gc();
	}

	@Override
	public Admin getAdmin() throws RemoteException {
		Admin admin = new Admin((AdminServerInterface)this);
		if(UnicastRemoteObject.unexportObject(admin,true))
			System.out.println("dho-Esportato da dio!");
		return admin;
	}

	@Override
	public RemoteObject getClient() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String registerAdmin(AdminUserInterface admin) throws RemoteException {
		this.admin = admin;
		System.out.println("stub admin = " + admin);
		return "YEAH";
	}
	
	
}
