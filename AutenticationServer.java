package tetraPong;

import java.rmi.NoSuchObjectException;
import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationID;
import java.rmi.activation.UnknownObjectException;
import java.rmi.server.Unreferenced;

@SuppressWarnings("serial")
public class AutenticationServer extends Activatable 
								implements Bootstrap, Autentication, Unreferenced{
	public AutenticationServer(ActivationID id,MarshalledObject obj) throws RemoteException{
		super(id,3000);
		System.out.println("Autentication su! " + id);
	}

	public boolean login(String username, String password)throws RemoteException{

		return false;
	}

	public boolean register(String username, String password)throws RemoteException{

		return false;
	}

	public Remote getMainServer() throws RemoteException{
		return null;
	}
	@Override
	public void unreferenced() {
		System.out.println("I'm trying to die!(Autentication)");
		try {
			if(Activatable.unexportObject(this, false))
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
	}

	@Override
	public Runnable getClient() throws RemoteException {
		return new Login();
	}	
}
