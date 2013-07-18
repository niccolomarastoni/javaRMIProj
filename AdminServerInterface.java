package tetraPong;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface used by Admin to comunicate with the Main Server. 
 * @author <A HREF="mailto:niccolo.marastoni@studenti.univr.it">Niccolò Marastoni</A>
 * @author <A HREF="mailto:andrei.munteanu@studenti.univr.it">Andrei Munteanu</A>
 * @version 1.0
 */
public interface AdminServerInterface extends Remote{
	/**
	 * Retrieves information about ongoing games from Main Server. 
	 * For each game info: gameID, player1, player2, game-active and score.
	 * @return an array of game info.
	 * @throws RemoteException
	 */
	public Object[][] getMatchData() throws RemoteException;
	/**
	 * Puts in the ban list the user.
	 * @param user to ban.
	 * @throws RemoteException
	 */
	public void banUser(String user) throws RemoteException;
	/**
	 * Pings the user. 
	 * @param user to ping.
	 * @return ping.
	 * @throws RemoteException
	 */
	public long pingUser(String user) throws RemoteException;
	/**
	 * Interrupts a match.
	 * @param gameID which identifies the match.
	 * @throws RemoteException
	 */
	public void stopMatch(int gameID) throws RemoteException;
}
