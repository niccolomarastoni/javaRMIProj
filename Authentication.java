package tetraPong;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * Interface used by clients for authentication.
 * 
 * @author <A HREF="mailto:niccolo.marastoni@studenti.univr.it">Niccolò Marastoni</A>
 * @author <A HREF="mailto:andrei.munteanu@studenti.univr.it">Andrei Munteanu</A>
 * @version 1.0
 */
public interface Authentication extends Remote{
	/**
	 * Login. 
	 * 
	 * @param user
	 * @param pass
	 * @return authentication code: -1 = authentication failed, 0 = player, 1 admin.  
	 * @throws RemoteException
	 */
	public int login(String user, String pass) throws RemoteException;
	
	/**
	 * Registers a client.
	 * 
	 * @param user
	 * @param pass
	 * @return false if already registered true otherwise.
	 * @throws RemoteException
	 */
	public boolean register(String user, String pass)throws RemoteException;
	
	/**
	 * Retrieves the MainServer's stub.
	 * 
	 * @return MainServer's stub.
	 * @throws RemoteException
	 */
	public MainInterface getMainServer() throws RemoteException;

}
