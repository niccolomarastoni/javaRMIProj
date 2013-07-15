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
import java.util.Vector;

public class MainServer extends Activatable 
implements Unreferenced, MainInterface, AdminServerInterface,ProxyToMainInterface{
	private AdminUserInterface admin;
	private int available = 5;
	private PlayerInterface[][] games = new PlayerInterface[available][];
	private int index = 0;
	private Vector <PlayerInterface> waitingRoom = new Vector<PlayerInterface>();
	protected MainServer(ActivationID id, MarshalledObject<?> data) throws RemoteException {
		super(id, 30001);
		System.out.println("Main su le mani!" + id);
	}

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
	public PlayerInterface getOpponent(int gameID,int id) throws RemoteException {
		return games[gameID][id];
	}

	@Override
	public int getMatch(PlayerInterface player) throws RemoteException {
		if(waitingRoom.isEmpty() || available == 0){
			waitingRoom.add(player);
			return -1;
		}
		else{
			games[index] = new PlayerInterface[2];
			games[index][0] = player;
			games[index][1] = waitingRoom.firstElement();
			waitingRoom.removeElementAt(0);
			MainToPlayerInterface aux = (MainToPlayerInterface)games[index][1]; 
			aux.gameReady(index);
			int temp = index;
			index = (index + 1)%available;
			return temp;
		}
	}

	@Override
	public void unregisterPlayer() throws RemoteException {//da implementare
		System.out.println("Player Unregistered");

		System.gc();
		
	}

	@Override
	public Object[][] getMatchData() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}


}
