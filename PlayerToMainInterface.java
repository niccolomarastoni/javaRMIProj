package tetraPong;
import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * Interface used by players to interact with the Main Server.
 * 
 * @author <A HREF="mailto:niccolo.marastoni@studenti.univr.it">Niccolò Marastoni</A>
 * @author <A HREF="mailto:andrei.munteanu@studenti.univr.it">Andrei Munteanu</A>
 * @version 1.0
 */
public interface PlayerToMainInterface  extends Remote{
	/**
	 * Registers a player for a game.
	 * 
	 * @param player
	 * @return gameID -1 = in waiting, -2 = player banned, otherwise unique identifier of the match.
	 * @throws RemoteException
	 */
	public int getMatch(PlayerInterface player) throws RemoteException;
	/**
	 * Retrieves a stub of the opponent.
	 * @param gameID identifier of the match.
	 * @param id player's id.
	 * @return Opponent's stub.
	 * @throws RemoteException
	 */
	public PlayerInterface getOpponent(int gameID,int id) throws RemoteException;
	/**
	 * Unregisters the player from the MainServer.
	 * 
	 * @param gameID identifier of the match. 
	 * @param id player's identifier.
	 * @param player player's stub.
	 * @throws RemoteException
	 */
	public void unregisterPlayer(int gameID,int id,PlayerInterface player) throws RemoteException;
	/**
	 * Informs the Main Server the opponent is unreachable. The Main Server will end the match.
	 * 
	 * @param gameID identifier of the match.
	 * @param id player's identifier.
	 * @throws RemoteException
	 */
	public void errorSignal(int gameID,int id) throws RemoteException;
}
