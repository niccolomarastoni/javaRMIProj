import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class ClientBootstrap {
	
	public static void main(String [] argv){
		String ip;
		Bootstrap bs;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Identification Server IP: ");
		try {
			ip = in.readLine();
			bs = (Bootstrap)Naming.lookup("//" + ip + "/IdentificationServer");
			bs.getClient().run();
		} catch(RemoteException e){
			// TODODODODODOD
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
