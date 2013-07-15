package tetraPong;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.MarshalledObject;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationDesc;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationGroupDesc;
import java.rmi.activation.ActivationGroupID;
import java.util.Properties;

public class Setup{
	//codebase su file e popup di configurazione
	public static void main(String[] argv){
		String HOME_DIR = System.getProperty("user.home");
		String codebase = "http://157.27.241.248:8000/common/";
		String autenticationServerClass = "tetraPong.AutenticationServer";
		String mainServerClass = "tetraPong.MainServer";
		String policyGroup1 = HOME_DIR + "/javarmi/tetraPong/group.policy";
		String policyGroup2 = HOME_DIR + "/javarmi/tetraPong/group.policy";
		System.setProperty("java.security.policy",HOME_DIR + "/javarmi/tetraPong/setup.policy");
		System.setProperty("java.rmi.server.codebase",codebase);
		System.setSecurityManager(new SecurityManager());
		try{
			Properties prop1 = new Properties();
			Properties prop2 = new Properties();

			prop1.put("java.security.policy", policyGroup1);
			prop1.put("java.rmi.server.codebase",codebase);
			prop1.put("java.class.path","no_classpath");
			prop1.put("java.rmi.dgc.leaseValue","30000");
			prop1.put("javax.net.ssl.trustStore", HOME_DIR + "/javarmi/tetraPong/authServer.keystore");
			prop1.put("javax.net.ssl.keyStore", HOME_DIR + "/javarmi/tetraPong/authServer.keystore");
			prop1.put("javax.net.ssl.keyStorePassword", "authServer");
			
			prop2.put("java.security.policy", policyGroup2);
			prop2.put("java.rmi.server.codebase",codebase);
			prop2.put("java.class.path","no_classpath");
			prop2.put("java.rmi.dgc.leaseValue","30000");

			ActivationGroupDesc gDesc1 = new ActivationGroupDesc(prop1, null);
			ActivationGroupDesc gDesc2 = new ActivationGroupDesc(prop2, null);

			ActivationGroupID gID1 = ActivationGroup.getSystem().registerGroup(gDesc1);
			ActivationGroupID gID2 = ActivationGroup.getSystem().registerGroup(gDesc2);

			ActivationDesc mainServerDesc = new ActivationDesc(gID2, mainServerClass, codebase, null);

			MainInterface mainInt = (MainInterface)Activatable.register(mainServerDesc);
			MarshalledObject data = new MarshalledObject(mainInt);
			ActivationDesc autenticationServerDesc = new ActivationDesc(gID1, autenticationServerClass, codebase, data);
			Bootstrap autenticationStub = (Bootstrap)Activatable.register(autenticationServerDesc);
			System.out.println("Stubbe: " + autenticationStub);

			Naming.rebind("//:1099/AutenticationServer", autenticationStub);
			


		}catch(RemoteException e){
			e.printStackTrace();
		} catch (ActivationException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
