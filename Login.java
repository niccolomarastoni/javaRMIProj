package tetraPong;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Login extends JFrame implements Runnable{
	private JTextField userField;
	private JPasswordField passField;
	private String user;
	private char[] pass;
	private JTextArea userArea, passArea;
	private LoginButton login;
	private CancelButton cancel;
	private RegisterButton register;
	Authentication auth;
	public Login(Authentication auth){
		//		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		//	this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		this.auth = auth;
		System.out.println("Stub auth = " + auth);
	}

	private class CancelButton extends JButton{
		public CancelButton(){
			super("Cancel");
			setBounds(180, 80, 100, 30);
			this.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					System.exit(0);
				}
			});
		}
	}

	private class LoginButton extends JButton{
		public LoginButton(){
			super("Login");
			setBounds(80, 80, 80, 30);
			this.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					user = userField.getText();
					pass = passField.getPassword();
					String passW = "";
					for(char c:pass)
						passW += c;
					
					int authenticationCode = 0; 		
					System.out.println("user = " + user + " pass = " + passW);
					try {
						authenticationCode = auth.login(user, passW);
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
								SetupClient setupClient = mainRef.getClient();
								TetraPongProxy game = (TetraPongProxy)setupClient.init();
								game.activate(user);
								System.exit(0);
							}
								
						}
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
		}
	}
	
	private class RegisterButton extends JButton{
		public RegisterButton(){
			super("Register");
			setBounds(80,120,100,20);
			this.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					user = userField.getText();
					pass = passField.getPassword();
					String passW = "";
					for(char c:pass)
						passW += c;
					try {
						if(!auth.register(user, passW))
							System.out.println("Already registered!");
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}});
		}
	}

	private void init(){
		this.setSize(300,200);
		this.setTitle("Login");
		getContentPane().setLayout(null);
		login = new LoginButton();
		cancel = new CancelButton();
		register = new RegisterButton();
		register.setVisible(true);
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
		getContentPane().add(register);
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
