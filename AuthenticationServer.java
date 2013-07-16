package tetraPong;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
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
	public AuthenticationServer(ActivationID id,MarshalledObject<?> obj) throws RemoteException{
		super(id,30000, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory());
		
		try {
			mainRef = (MainInterface)obj.get();
			System.out.println("Autentication su! " + id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String HOME_DIR = System.getProperty("user.home");
		String saveRef = HOME_DIR + "/javarmi/tetraPong/.Clients";
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new URL("file://"+saveRef).openStream());
			Object  clientsData = ((MarshalledObject)in.readObject()).get();
			clients = (Map<String,String>)clientsData;
		}catch(FileNotFoundException e) {
			System.out.println("Empty database.");
		} catch (MalformedURLException e) {
			System.out.println("Error database");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error database");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Error database");
			e.printStackTrace();
		}
		
		

	}
	
	@Override
	public int login(String username, String password)throws RemoteException{
		System.out.println("Number of clients registerd "+clients.size());
		
		for (String client : clients.keySet())
			System.out.println("Client = " + client + " pass = " + clients.get(client));
		
		System.out.println("contains ? "+clients.containsKey(username));
		
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
			String HOME_DIR = System.getProperty("user.home");
			ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream(HOME_DIR+"/javarmi/tetraPong/.Clients"));
			out.writeObject(new MarshalledObject(clients));
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
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.gc();
	}

	@Override
	public Runnable getClient() throws RemoteException {
		return new Login(this);
	}	
}
