package tetraPong;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationID;
import java.rmi.activation.UnknownObjectException;
import java.rmi.server.Unreferenced;

public class MainServer extends Activatable implements Unreferenced{

	protected MainServer(ActivationID id, int port) throws RemoteException {
		super(id, port);
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
	
	
}