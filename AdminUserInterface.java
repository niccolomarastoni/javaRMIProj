package tetraPong;
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface AdminUserInterface extends Remote {
public void startAdmin() throws RemoteException;
}
