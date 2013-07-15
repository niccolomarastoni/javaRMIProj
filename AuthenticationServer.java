package tetraPong;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationID;
import java.rmi.activation.UnknownObjectException;
import java.rmi.server.RemoteObject;
import java.rmi.server.Unreferenced;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

@SuppressWarnings("serial")
public class AuthenticationServer extends Activatable 
implements Bootstrap, Authentication, Unreferenced{
	MainInterface mainRef;
	private Map<String,String> clients = new HashMap<String,String>();
	private String [] admin= {"admin","pass"};
	public AuthenticationServer(ActivationID id,MarshalledObject<?> obj) throws IOException, ClassNotFoundException{
		super(id,30000, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory());
		mainRef = (MainInterface)obj.get();
		System.out.println("Autentication su! " + id);

	}
	
	@Override
	public int login(String username, String password)throws RemoteException{
		if(username.equals(admin[0]) && password.equals(admin[1]))
			return 1;
		else if(clients.containsKey(username) && clients.get(username).equals(password))
			return 0;
		return -1; 
	}
	
	@Override
	public boolean register(String username, String password)throws RemoteException{
		if(clients.containsKey(username))
			return false;
		clients.put(username, password);
		return true;
	}
	
	@Override
	public MainInterface getMainServer() throws RemoteException{
		return mainRef;
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
		return new Login(this);
	}	
}
