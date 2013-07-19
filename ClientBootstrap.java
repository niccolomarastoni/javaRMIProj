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
		String ip ="157.27.241.192";
		Bootstrap bs;
		String HOME_DIR = System.getProperty("user.home");
		System.setProperty("javax.net.ssl.trustStore", HOME_DIR + "/javarmi/tetraPong/loginClient.keystore");
		System.setProperty("javax.net.ssl.trustStorePassword","loginClient");
		System.setProperty("java.security.policy",HOME_DIR + "/javarmi/tetraPong/policy");
		System.setSecurityManager(new SecurityManager());
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		try {
			bs = (Bootstrap)Naming.lookup("//" + ip + "/AuthenticationServer");
			bs.getClient().run();

		} catch(RemoteException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		} catch (NotBoundException e){
			e.printStackTrace();
		}
	}

}
