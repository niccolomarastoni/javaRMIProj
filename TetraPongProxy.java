package tetraPong;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * Interface used by the client to interact with the Mobile Server.
 * @author <A HREF="mailto:niccolo.marastoni@studenti.univr.it">Niccolò Marastoni</A>
 * @author <A HREF="mailto:andrei.munteanu@studenti.univr.it">Andrei Munteanu</A>
 * @version 1.0
 */
public interface TetraPongProxy extends Remote{
	/**
	 * Calls the Mobile server in order to start a match.
	 * @param user username.
	 * @throws RemoteException
	 */
	public void activate(String user) throws RemoteException;

}
