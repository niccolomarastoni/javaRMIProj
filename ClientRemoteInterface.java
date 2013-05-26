import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ClientRemoteInterface extends Remote {
  public void getOpponent() throws MalformedURLException, RemoteException, NotBoundException;
	public void setBallSpeed(double dx, double dy) throws RemoteException;
<<<<<<< HEAD
	public double[] Update(double baseX, double baseY) throws RemoteException;
=======
	public double[] getUpdate() throws RemoteException;
>>>>>>> 5ff721bf92b97b1a352c61bd0b84bcf7c8ac70f0
}
