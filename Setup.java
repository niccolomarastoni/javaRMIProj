import java.net.MalformedURLException;
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

	public static void main(String[] argv){
		String codebase = "";
		String autenticationServerClass = "";
		String mainServerClass = "";
		String policyGroup1 = "";
		String policyGroup2 = "";
	System.setSecurityManager(new SecurityManager());
	try{
		Properties prop1 = new Properties();
		Properties prop2 = new Properties();
		
		prop1.put("java.security.policy", policyGroup1);
		//prop1.put(,);
		prop1.put("java.class.path","no_classpath");
		
		prop2.put("java.security.policy", policyGroup2);
		//prop2.put(,);
		prop2.put("java.class.path","no_classpath");
		
		ActivationGroupDesc gDesc1 = new ActivationGroupDesc(prop1, null);
		ActivationGroupDesc gDesc2 = new ActivationGroupDesc(prop2, null);
		
		ActivationGroupID gID1 = ActivationGroup.getSystem().registerGroup(gDesc1);
		ActivationGroupID gID2 = ActivationGroup.getSystem().registerGroup(gDesc2);
		
		ActivationDesc autenticationServerDesc = new ActivationDesc(gID1, autenticationServerClass, codebase, null);
		ActivationDesc mainServerDesc = new ActivationDesc(gID2, mainServerClass, codebase, null);
		
		Autentication autenticatioStub = (Autentication)Activatable.register(autenticationServerDesc);
		Activatable.register(mainServerDesc);
		
		Naming.rebind("//:1098/autenticationServer", autenticatioStub);
		
		
	}catch(RemoteException e){
		e.printStackTrace();
	} catch (ActivationException e) {
		e.printStackTrace();
	} catch (MalformedURLException e) {
		e.printStackTrace();
	}
		
	}
}