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

@SuppressWarnings("serial")
public class AutenticationServer extends Activatable 
								implements Bootstrap, Autentication, Unreferenced{
	MainInterface mainRef;
	public AutenticationServer(ActivationID id,MarshalledObject obj) throws IOException, ClassNotFoundException{
		//nel marshalledobject ci va il mainserver
		super(id,3000);
		mainRef = (MainInterface)obj.get();
		System.out.println("Autentication su! " + id);
		
	}
	
	private int checkLogin(String username, String password){
		return 0; //0 = client 1 = admin -1 = error		
	}

	public int login(String username, String password)throws RemoteException{
		//if(verifica che sia client)
		//return main.getClient();
	/*	switch(checkLogin(username,password)){
		case 0: return mainRef.getClient();
		case 1: //Admin cic =  mainRef.getAdmin();
						//System.out.println("ADmin :D "+ cic);
						System.out.println("Login() restituisce " + admin);
						return mainRef.getAdmin();
		default: return null;*/
		return 0;
	}

	public boolean register(String username, String password)throws RemoteException{

		return false;
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
