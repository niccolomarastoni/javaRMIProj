package tetraPong;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.MarshalledObject;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationID;
import java.rmi.activation.UnknownObjectException;
import java.rmi.server.Unreferenced;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class MainServer extends Activatable 
implements Unreferenced, MainInterface, AdminToServerInterface,PlayerToMainInterface{
	private UserToAdminInterface admin;
	private int nSlots = 5;
	private int available = nSlots;
	private PlayerInterface[][] games = new PlayerInterface[nSlots][];
	private int index = 0;
	private long startingTime;
	private Vector <PlayerInterface> waitingRoom = new Vector<PlayerInterface>();
	private Set <String> banList = new HashSet<String>(); 
	protected MainServer(ActivationID id, MarshalledObject<?> data) throws RemoteException {
		super(id, 30001);
		banList.add("paolo");
		System.out.println(" [MAIN] Main Server exported." + id);
		String HOME_DIR = System.getProperty("user.home");
		String saveRef = HOME_DIR + "/javarmi/tetraPong/.banList";
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new URL("file://"+saveRef).openStream());
			Object  banList = ((MarshalledObject)in.readObject()).get();
			this.banList = (Set<String>)banList;
			System.out.println(" [MAIN] Banned users: ");
			
			for(String user:this.banList)
				System.out.println(" [MAIN] "+user);
		}catch(FileNotFoundException e) {
			System.out.println(" [MAIN] Empty database.");
		} catch (MalformedURLException e) {
			System.out.println(" [MAIN] Error database");
		} catch (IOException e) {
			System.out.println(" [MAIN] Error database");
		} catch (ClassNotFoundException e) {
			System.out.println(" [MAIN] Error database");
		}
	}

	@Override
	public void unreferenced() {	
		System.out.println(" [MAIN] unreferenced method called.");
		try {
			if(Activatable.unexportObject(this, false))
				Activatable.inactive(getID());
			String HOME_DIR = System.getProperty("user.home");
			ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream(HOME_DIR+"/javarmi/tetraPong/.banList"));
			out.writeObject(new MarshalledObject(banList));
			System.out.println(" [MAIN] Database saved.");
		} catch (NoSuchObjectException e) {
			e.printStackTrace();
		} catch (UnknownObjectException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ActivationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println(" [MAIN] Error database.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.gc();
	}

	@Override
	public Admin getAdmin() throws RemoteException {
		Admin admin = new Admin((AdminToServerInterface)this);
		UnicastRemoteObject.unexportObject(admin,true);

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
		System.out.println(" [MAIN] "+player.getUser()+" wants a match.");
		if(banList.contains(player.getUser()))
			return -2;
		if(waitingRoom.isEmpty() || available == 0){
			waitingRoom.add(player);
			System.out.println(" [MAIN] "+player.getUser()+" in waiting room.");
			return -1;
		}
		else{
			games[index] = new PlayerInterface[2];
			games[index][0] = player;
			games[index][1] = waitingRoom.firstElement();
			waitingRoom.removeElementAt(0);
			System.out.println(" [MAIN] Game created. GameID = " + index);
			MainToPlayerInterface aux = (MainToPlayerInterface)games[index][1];
			try{
				aux.gameReady(index);
			}catch(RemoteException e){

			}
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
			System.out.println(" [MAIN] Removing "+player.getUser() + " from waiting room.");
		}
		else{

			if(games[gameID][(id+1)%2] == null){
				games[gameID] = null; 
				index = gameID;
				available++;
			}
			else { 
				games[gameID][id] = null;
				id = (id+1)%2;
				try{
					((MainToPlayerInterface)(games[gameID][id])).opponentLeft();
				}catch(RemoteException e){
					System.out.println(" [MAIN] Unable to reach" + games[gameID][id]+". Closing game.");
					games[gameID] = null;
				}
			}
		}
		System.out.println(" [MAIN] Player " +player.getUser()+ " unregistered");
		System.gc();

	}

	@Override
	public void errorSignal(int gameID, int id) throws RemoteException {
		if(games[gameID][(id+1)%2] != null){
			System.out.print(" [MAIN] Error signal received: ");
			System.out.println(games[gameID][id].getUser() + "Couldn't reach" + games[gameID][(id+1)%2]);
			System.out.println(" [MAIN] Closing game: " +gameID);
			games[gameID] = null;
			try{
				((MainToPlayerInterface)(games[gameID][id])).opponentLeft();
			}catch(RemoteException e){

			}
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
		banList.add(user);
		System.out.println(" [MAIN] Banned users: ");
		
		for(String u:this.banList)
			System.out.println(" [MAIN] \t\t\t"+u);
	}

	@Override
	public long pingUser(String user) throws RemoteException {
		System.out.println(" [MAIN] Ping " + user);
		MainToPlayerInterface player = null;
		for(PlayerInterface[] game: games)
			if(game[0].getUser().equals(user)){
				player =(MainToPlayerInterface)game[0];
				break;
			}
			else if(game[1].getUser().equals(user)){
				player =(MainToPlayerInterface)game[1];
				break;
			}
		startingTime = System.currentTimeMillis();
		try{
			player.ping();
		}catch(RemoteException e){
			return -1;
		}
		return System.currentTimeMillis() -  startingTime;
	}


	@Override
	public void stopMatch(int gameID) throws RemoteException {
		((MainToPlayerInterface)games[gameID][0]).stopGame();
		((MainToPlayerInterface)games[gameID][1]).stopGame();
		System.out.println(" [MAIN] Stopped game: "+gameID);
	}


}
