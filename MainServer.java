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
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class MainServer extends Activatable 
implements Unreferenced, MainInterface, AdminServerInterface,PlayerToMainInterface{
	private AdminUserInterface admin;
	private int nSlots = 5;
	private int available = nSlots;
	private PlayerInterface[][] games = new PlayerInterface[nSlots][];
	private int index = 0;
	private Vector <PlayerInterface> waitingRoom = new Vector<PlayerInterface>();
	private Set <String> bannedList = new HashSet<String>();
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
			System.out.println("Admin server unexported!");
		return admin;
	}

	@Override
	public SetupClient getClient() throws RemoteException {
		return new SetupClient((PlayerToMainInterface)this);
	}

	@Override
	public PlayerInterface getOpponent(int gameID,int id) throws RemoteException {
		return games[gameID][id];
	}

	@Override
	public int getMatch(PlayerInterface player) throws RemoteException {
		System.out.println(player.getUser()+" wants a match!");
		if(waitingRoom.isEmpty() || available == 0){
			waitingRoom.add(player);
			System.out.println("In waitingroom");
			return -1;
		}
		else{
			games[index] = new PlayerInterface[2];
			games[index][0] = player;
			games[index][1] = waitingRoom.firstElement();
			waitingRoom.removeElementAt(0);
			System.out.println("Game created");
			MainToPlayerInterface aux = (MainToPlayerInterface)games[index][1]; 
			aux.gameReady(index);
			int temp = index;
			do{
				index = (index + 1)%nSlots;
			}while(games[index] != null );
			available--;

			return temp;
		}
	}

	@Override
	public void unregisterPlayer(int gameID,int id, PlayerInterface player) throws RemoteException {//da implementare
		if(gameID < 0){
			waitingRoom.remove(player);
			System.out.println("Removing from waiting room. Is empty "+ waitingRoom.isEmpty());
		}
		else{
			if(games[gameID][(id+1)%2] == null){
				games[gameID] = null; 
				index = gameID;
				available++;
				//se è zero e due nella waiting room gioca;
			}
			else { 
				games[gameID][id] = null;
				id = (id+1)%2;
				((MainToPlayerInterface)(games[gameID][id])).opponentLeft();
			}
		}
		System.out.println("Player " +player.getUser()+ " unregistered");
		System.gc();

	}

	@Override
	public void errorSignal(int gameID, int id) throws RemoteException {
		if(games[gameID][(id+1)%2] != null){
			System.out.print("Error signal received: ");
			System.out.println(games[gameID][id].getUser() + "Couldn't reach" + games[gameID][(id+1)%2]);
			System.out.println("Closing game: " +gameID);
			games[gameID] = null;
			((MainToPlayerInterface)(games[gameID][id])).opponentLeft();
		}

	}

	@Override
	public Object[][] getMatchData() throws RemoteException {
		Object[][] data = new Object[nSlots-available][5];
		int j = 0;
		for(int i = 0;i < nSlots;i++){
			if(games[i] != null){
				data[j][0] = i + 1; //game id
				data[j][1] = (games[i][0] == null)?null:games[i][0].getUser(); //player1
				data[j][2] = (games[i][1] == null)?null:games[i][1].getUser(); //player2
				data[j][3] = ((data[j][1] == null) || (data[j][2] == null))?false:true;
				data[j][4] =((data[j][1] == null) || (data[j][2] == null))?"0 - 0":((MainToPlayerInterface)games[i][0]).getScore();
				j++;
			}
		}
		return data;
	}

	@Override
	public void banUser(String user) throws RemoteException {

	}

	@Override
	public boolean pingUser(String user) throws RemoteException {
		return false;
	}


}
