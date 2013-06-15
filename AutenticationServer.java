import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationID;
import java.rmi.server.Unreferenced;

@SuppressWarnings("serial")
public class AutenticationServer extends Activatable 
								implements Autentication ,	Unreferenced{
	
	public AutenticationServer(ActivationID id, int port) throws RemoteException{
		super(id, port);
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
		// TODO Auto-generated method stub
		
	}
	
}