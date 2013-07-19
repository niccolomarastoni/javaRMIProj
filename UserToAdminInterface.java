package tetraPong;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface used by the user to interact with the "Admin Mobile Server".
 * 
 * @author <A HREF="mailto:niccolo.marastoni@studenti.univr.it">Niccolò Marastoni</A>
 * @author <A HREF="mailto:andrei.munteanu@studenti.univr.it">Andrei Munteanu</A>
 * @version 1.0
 */
public interface UserToAdminInterface extends Remote {
	/**
	 * Lunches the Admin GUI.
	 * @throws RemoteException
	 */
	public void startAdmin() throws RemoteException;
}
