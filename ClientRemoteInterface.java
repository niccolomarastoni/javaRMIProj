import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ClientRemoteInterface extends Remote {
  public void getOpponent() throws MalformedURLException, RemoteException, NotBoundException;
	public void setBallSpeed(double dx, double dy) throws RemoteException;
	public double[] Update(double baseX, double baseY) throws RemoteException;
}
