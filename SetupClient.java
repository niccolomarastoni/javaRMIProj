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
		String HOME_DIR = System.getProperty("user.home");
		String codebase = "http://157.27.241.248:8000/common/";
		String ServerPongClass = "tetraPong.ServerPong";
		String policyGroup =HOME_DIR + "/javarmi/tetraPong/group.policy";//occhio ai Policy
		TetraPongProxy game = null;
		try {
			Properties prop = new Properties();

			prop.put("java.security.policy", policyGroup);
			prop.put("java.rmi.server.codebase",codebase);
			prop.put("java.class.path","no_classpath");
			prop.put("java.rmi.dgc.leaseValue","3000");

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
