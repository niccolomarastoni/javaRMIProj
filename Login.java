package tetraPong;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Login extends JFrame implements Runnable{
	private JTextField userField;
	private JPasswordField passField;
	private String user;
	private char[] pass;
	private JTextArea userArea, passArea;
	private loginButton login;
	private cancelButton cancel;
	Autentication auth;
	public Login(Autentication auth){
		//		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		//	this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		this.auth = auth;
		System.out.println("Stub auth = " + auth);
	}

	private class cancelButton extends JButton{
		public cancelButton(){
			super("Cancel");
			setBounds(160, 100, 100, 40);
			this.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					System.exit(0);
				}

			});
		}
	}

	private class loginButton extends JButton{
		public loginButton(){
			super("Login");
			setBounds(60, 100, 80, 40);
			this.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					pass = passField.getPassword();
					String passW = "";
					for(char c:pass)
						passW += c;
					
					int authenticationCode = 0; 		
					AdminUserInterface rem = null;
					System.out.println("user = " + user + " pass = " + passW);
					try {
						authenticationCode = auth.login(user, passW);
						System.out.println("Auth. Code "+rem);
						if(authenticationCode == -1){
							System.out.println("Error code");
							System.exit(0);
						}

						else {
							MainInterface mainRef = auth.getMainServer();
							if(authenticationCode == 1){
								System.out.println("Sono istanza di AUI");								
								AdminUserInterface admin = mainRef.getAdmin();
								admin.startAdmin();
							}else {
								System.out.println("ciao");	
								SetupClient setupClient = mainRef.getClient();
								System.out.println("ciao passato");		
								TetraPongProxy game = (TetraPongProxy)setupClient.init();
								System.out.println("ciao passato di nuovo");		
								game.activate();
								System.out.println("Game is up! :|]");
							}
								
						}
						System.exit(0);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						System.out.println("I'm a catchman "+rem);
						e.printStackTrace();
					}

				}
			});
		}
	}



	public static void main(String argv[]){
		//new Login();
	}

	private void init(){
		this.setSize(300,200);
		getContentPane().setLayout(null);
		login = new loginButton();
		cancel = new cancelButton();
		userArea = new JTextArea("User: ");
		userArea.setFont(new Font("myFont",1,16));
		userArea.setEditable(false);
		userArea.setVisible(true);
		userArea.setBounds(10,10,50,30);
		passArea = new JTextArea("Pass: ");
		passArea.setFont(new Font("myFont",1,16));
		passArea.setEditable(false);
		passArea.setBounds(10,44,50,30);
		userField = new JTextField();
		passField = new JPasswordField();
		passField.setEchoChar('*');
		userField.setBounds(80, 5, 200, 30);
		getContentPane().add(login);
		getContentPane().add(cancel);
		getContentPane().add(userField);
		getContentPane().add(userArea);
		getContentPane().add(passArea);
		passField.setBounds(80,40,200,30);


		getContentPane().add(passField);

		setResizable(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setVisible(true);		
	}

	@Override
	public void run() {
		//System.setSecurityManager(new SecurityManager());
		init();

	}

}
