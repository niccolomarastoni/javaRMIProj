package tetraPong;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationDesc;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationGroupDesc;
import java.rmi.activation.ActivationGroupID;
import java.util.Properties;


public class SetupClient implements Serializable {
	private ProxyToMainInterface mainRef;
	public SetupClient(ProxyToMainInterface mainRef){
		this.mainRef = mainRef;
	}
	public TetraPongProxy init(){
		String codebase = "http://157.27.241.163:8000/common/";
		String ServerPongClass = "tetraPong.ServerPong";
		String policyGroup = "/home/accounts/studenti/id270mam/javarmi/tetraPong/group.policy";//occhio ai Policy
		System.setProperty("java.security.policy","/home/accounts/studenti/id270mam/javarmi/tetraPong/setup.policy");
		System.setProperty("java.rmi.server.codebase",codebase);
		//System.setSecurityManager(new SecurityManager());
		TetraPongProxy game = null;
		try {
			Properties prop = new Properties();

			prop.put("java.security.policy", policyGroup);
			prop.put("java.rmi.server.codebase",codebase);
			prop.put("java.class.path","no_classpath");
			prop.put("java.rmi.dgc.leaseValue","30000");

			MarshalledObject data = new MarshalledObject(mainRef);

			ActivationGroupDesc gDesc = new ActivationGroupDesc(prop, null);

			ActivationGroupID gID = ActivationGroup.getSystem().registerGroup(gDesc);

			ActivationDesc serverPongDesc = new ActivationDesc(gID, ServerPongClass, codebase, data);
			game = (TetraPongProxy) Activatable.register(serverPongDesc);


		}catch(RemoteException e){
			e.printStackTrace();
		} catch (ActivationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return game;
	}
}
