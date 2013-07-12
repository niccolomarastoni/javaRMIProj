package tetraPong;

import java.rmi.MarshalledObject;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationID;
import java.rmi.activation.UnknownObjectException;
import java.rmi.server.Unreferenced;
import java.rmi.server.UnicastRemoteObject;

public class MainServer extends Activatable 
implements Unreferenced, MainInterface, AdminServerInterface,ProxyToMainInterface{
	private AdminUserInterface admin;
	private ClientRemoteInterface player1 = null;
	private ClientRemoteInterface player2 = null;
	protected MainServer(ActivationID id, MarshalledObject data) throws RemoteException {
		super(id, 3001);
		System.out.println("Main su le mani!" + id);
	}

	// ciao grazie
	@Override
	public void unreferenced() {	
		System.out.println("I'm trying to die!(Main)");
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
	public SetupClient getClient() throws RemoteException {
		return new SetupClient((ProxyToMainInterface)this);
	}

	@Override
	public String registerAdmin(AdminUserInterface admin) throws RemoteException {
		this.admin = admin;
		System.out.println("stub admin = " + admin);
		return "YEAH";
	}

	@Override
	public ClientRemoteInterface getOpponent(int id) throws RemoteException {
		if(id == 1)
			return player1;
		else
			return player2;
	}

	@Override
	public boolean registerPlayer(ClientRemoteInterface player)
	throws RemoteException {
		if(player1 == null && player2 == null){
			player1 = player;
			System.out.println("Registering p1 = "+player1);
		}
		else if(player2 == null && player1 != null){
			player2 = player;
			System.out.println("Registering p2 = "+player2);
			player1.gameReady(2);
			player2.gameReady(1);
		}
		else return false;
		return true;
	}

	@Override
	public void unregisterPlayer() throws RemoteException {
		player1 = null;
		player2 = null;
		System.out.println("Player Unregistered");

		System.gc();
		
	}


}
