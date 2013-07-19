package tetraPong;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
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
	private PlayerToMainInterface mainRef;
	public SetupClient(PlayerToMainInterface mainRef){
		this.mainRef = mainRef;
	}
	public TetraPongProxy init() {
		TetraPongProxy game = null;
		String HOME_DIR = System.getProperty("user.home");
		String saveRef = HOME_DIR + "/javarmi/tetraPong/.RemoteRef";
		boolean fileNotFound = false;
		try {
			System.out.println("Searching for savedRef");
			ObjectInputStream in = new ObjectInputStream(new URL("file://"+saveRef).openStream());
			Object obj = ((MarshalledObject)in.readObject()).get();
			game = (TetraPongProxy)obj;
			System.out.println("Found");

		}catch(FileNotFoundException e){
			fileNotFound = true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		if(fileNotFound){
			System.out.println("No savedRef. Creating a new one!");
			String codebase = "http://157.27.241.192:8000/common/";
			String ServerPongClass = "tetraPong.ServerPong";
			String policyGroup =HOME_DIR + "/javarmi/tetraPong/group.policy";
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

				ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream(saveRef));
				out.writeObject(new MarshalledObject(game));


			}catch(RemoteException e){
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ActivationException e) {
				e.printStackTrace();
			} 

		}
		return game;
	}
}
