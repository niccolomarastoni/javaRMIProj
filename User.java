import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class User extends JFrame implements Runnable{
	private JTextField user, password;
	private JTextArea userArea, passArea;
	private loginButton login;
	private cancelButton cancel;
	public User(){
		//		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		//	this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
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
		user = new JTextField();
		password = new JTextField();
		user.setBounds(80, 5, 200, 30);
		getContentPane().add(login);
		getContentPane().add(cancel);
		getContentPane().add(user);
		getContentPane().add(userArea);
		getContentPane().add(passArea);
		password.setBounds(80,40,200,30);


		getContentPane().add(password);

		setResizable(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setVisible(true);
	}

	private class cancelButton extends JButton{
		public cancelButton(){
			super("Cancel");
			setBounds(160, 100, 100, 40);
			this.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {

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

				}

			});
		}
	}

	public static void main(String argv[]){
		new User();
	}

	@Override
	public void run() {

	}

}
