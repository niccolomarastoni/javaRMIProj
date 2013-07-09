package tetraPong;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AdminServerInterface extends Remote{

	String registerAdmin(AdminUserInterface admin) throws RemoteException;
	
}
