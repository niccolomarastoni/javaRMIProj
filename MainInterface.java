package tetraPong;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface used by logged clients in order to get admin's Mobile Server or player's Mobile Server.
 * 
 * @author <A HREF="mailto:niccolo.marastoni@studenti.univr.it">Niccolò Marastoni</A>
 * @author <A HREF="mailto:andrei.munteanu@studenti.univr.it">Andrei Munteanu</A>
 * @version 1.0
 */
public interface MainInterface extends Remote{
	/**
	 * Retrieves player's Mobile Server.
	 * @return player's Mobile Server.
	 * @throws RemoteException
	 */
	SetupClient getClient() throws RemoteException;
	/**
	 * Retrieves admin's Mobile Server.
	 * @return admin's Mobile Server.
	 * @throws RemoteException
	 */
	Admin getAdmin() throws RemoteException;
}
