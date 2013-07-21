package tetraPong;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface used by Main Server to interact with players.
 * 
 * @author <A HREF="mailto:niccolo.marastoni@studenti.univr.it">Niccolò Marastoni</A>
 * @author <A HREF="mailto:andrei.munteanu@studenti.univr.it">Andrei Munteanu</A>
 * @version 1.0
 */
public interface MainToPlayerInterface extends Remote{
	/**
	 * Informs the player that a game is ready. And provides a gameID.
	 * gameID is a unique identifier for the game in the Main Server.
	 * @param gameID
	 * @throws RemoteException
	 */
	public void gameReady(int gameID) throws RemoteException;
	/**
	 * Informs a player that his opponent has left the match.
	 * @throws RemoteException
	 */
	public void opponentLeft() throws RemoteException;
	/**
	 * Retrieves the match's score.
	 * @return match's score.
	 * @throws RemoteException
	 */
	public String getScore() throws RemoteException;
	/**
	 * Terminates a match.
	 * @throws RemoteException
	 */
	public void stopGame() throws RemoteException;
	/**
	 * Pings a player.
	 * @return int.
	 * @throws RemoteException
	 */
	public int ping() throws RemoteException;
}
