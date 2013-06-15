import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationID;
import java.rmi.server.Unreferenced;

public class MainServer extends Activatable implements Unreferenced{

	protected MainServer(ActivationID id, int port) throws RemoteException {
		super(id, port);
		// TODO Auto-generated constructor stub
	}

	// ciao grazie
	@Override
	public void unreferenced() {
		// TODO Auto-generated method stub
		
	}
	
	
}