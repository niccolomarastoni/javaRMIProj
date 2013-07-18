package tetraPong;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface used by the client in the bootstrap phase.
 * 
 * @author <A HREF="mailto:niccolo.marastoni@studenti.univr.it">Niccolò Marastoni</A>
 * @author <A HREF="mailto:andrei.munteanu@studenti.univr.it">Andrei Munteanu</A>
 * @version 1.0
 */
public interface Bootstrap extends Remote {
	/**
	 * Loads client class.
	 * @return client's class.
	 * @throws RemoteException
	 */
	public Runnable getClient() throws RemoteException;
}
