package tetraPong;
import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * Interface used by players to interact whit each other.
 * 
 * @author <A HREF="mailto:niccolo.marastoni@studenti.univr.it">Niccolò Marastoni</A>
 * @author <A HREF="mailto:andrei.munteanu@studenti.univr.it">Andrei Munteanu</A>
 * @version 1.0
 */
public interface PlayerInterface extends Remote {
	/**
	 * Sets ball's position a velocity.
	 * @param dx = horizontal speed.
	 * @param dy = vertical speed.
	 * @param x = horizontal position.
	 * @param y = vertical position.
	 * @throws RemoteException
	 */
	public void setBall(double dx, double dy, double x, double y) throws RemoteException;
	/**
	 * Sends the bases position to the opponent.
	 * @param baseX = the position of the horizontal base.
	 * @param baseY = the position of the vertical base
	 * @throws RemoteException
	 */
	public void Update(double baseX, double baseY) throws RemoteException;
	/**
	 * Sets the score of the opponent.
	 * @param p1Score
	 * @param p2Score
	 * @throws RemoteException
	 */
	public void updateScore(int p1Score, int p2Score) throws RemoteException;
	/**
	 * Begins the game.
	 * @throws RemoteException
	 */
	public void startGame() throws RemoteException;
	/**
	 * Retrieves player's username.
	 * @return username.
	 * @throws RemoteException
	 */
	public String getUser() throws RemoteException;
}