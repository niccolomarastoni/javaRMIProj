package tetraPong;

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
		Runnable client;
		String HOME_DIR = System.getProperty("user.home");
		System.setProperty("javax.net.ssl.trustStore", HOME_DIR + "/javarmi/tetraPong/loginClient.keystore");
		System.setProperty("javax.net.ssl.trustStorePassword","loginClient");
		System.setSecurityManager(new SecurityManager());
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		try {
			if(argv.length == 0){
				System.out.print("Autentication Server IP: ");
				ip = in.readLine();
			}
			else
				ip = argv[0];

			bs = (Bootstrap)Naming.lookup("//" + ip + "/AutenticationServer");
			bs.getClient().run();

		} catch(RemoteException e){
			e.printStackTrace();
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
