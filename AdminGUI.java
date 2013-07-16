package tetraPong;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class AdminGUI extends JFrame {
	private Admin admin;
	private JTextArea matchesText;
	private JTable matches;
	private AdminPanel adminPanel;
	private int[] selection;
	String[] columnNames = {"Match ID", "Player 1", "Player 2", "Ongoing", "Score" };
	Object[][] data;
	public AdminGUI(Admin admin){
		this.admin = admin;
		
		init();
	}


	private void init() {
		try {
			data = admin.getMatchData();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		this.setSize(700,250);
		this.setTitle("Admin Panel");	
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.white);
		RefreshButton refresh = new RefreshButton();
		refresh.setVisible(true);
		refresh.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AdminGUI.this.showAdminPanel(false);
				AdminGUI.this.refreshMatches();
			}});
		matchesText = new JTextArea("Matches:");
		matchesText.setFont(new Font("myFont",Font.BOLD,16));
		matchesText.setEditable(false);
		matchesText.setVisible(true);
		matchesText.setBounds(10,10,80,30);
		initMatches();
		getContentPane().add(refresh);
		getContentPane().add(matchesText);

		setResizable(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setVisible(true);	

	}

	private void initMatches(){

		matches = new JTable(data,columnNames);
		matches.setFillsViewportHeight(true);
		matches.setCellSelectionEnabled(false);
		matches.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent arg0) {

			}});
		matches.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1){
					selection = new int[]{matches.getSelectedRow(), matches.getSelectedColumn()};
					AdminGUI.this.showAdminPanel(true);

				}
				else if(e.getButton() == MouseEvent.BUTTON3)
					System.out.println("Right");
			}
		});
		//matches.setPreferredScrollableViewportSize(new Dimension(500, 70));
		matches.setBounds(10,50,500,80);
		matches.setFont(new Font("mine", Font.PLAIN, 12));
		for(int i = 0;i < 5;i++)
			matches.getColumnModel().getColumn(i).setMinWidth(100);

		matches.setVisible(true);
		getContentPane().add(matches);
	}
	private void showAdminPanel(Boolean flag){
		if(flag){
			if(adminPanel != null)
				getContentPane().remove(adminPanel);
			if(selection[1] < 3 && selection[1] > 0){
				adminPanel = new AdminPanel(flag);
			}
			else
				adminPanel = new AdminPanel(!flag);
			getContentPane().add(adminPanel);
			getContentPane().repaint();
		}
		else{
			if(adminPanel != null)
				getContentPane().remove(adminPanel);
			getContentPane().repaint();
		}
	}

	protected void refreshMatches() {
		try {
			data = admin.getMatchData();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		getContentPane().remove(matches);
		initMatches();
		getContentPane().repaint();
	}

	private class RefreshButton extends JButton{
		public RefreshButton(){
			super("Refresh");
			setBounds(550,50,100,20);
		}
	}

	private class AdminPanel extends JPanel{
		private BanButton ban;
		private CancelButton cancel;
		private PingButton ping;
		private StopButton stop;
		public AdminPanel(boolean flag){
			this.setBounds(10, 140, 500, 20);
			this.setBackground(Color.white);
			stop = new StopButton();
			ban = new BanButton();
			ping = new PingButton();
			cancel = new CancelButton();
			cancel.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					AdminGUI.this.showAdminPanel(false);
				}});
			stop.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					AdminGUI.this.stopMatch();
				}});
			ban.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					AdminGUI.this.banUser();
				}});
			ping.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					AdminGUI.this.pingUser();					
				}});
			cancel.setVisible(true);
			ping.setVisible(true);
			ban.setVisible(true);
			stop.setVisible(true);
			if(!flag){
				ban.setEnabled(false);
				ping.setEnabled(false);
			}
			this.add(stop);
			this.add(ban);
			this.add(ping);
			this.add(cancel);
			getContentPane().repaint();
		}
		private class StopButton extends JButton{
			public StopButton(){
				super("Stop Match");
				setBounds(0,0,120,20);
			}
		}
		private class BanButton extends JButton{
			public BanButton(){
				super("Ban User");
				setBounds(120,0,100,20);
			}
		}
		private class CancelButton extends JButton{
			public CancelButton(){
				super("Cancel");
				setBounds(340,0,100,20);
			}
		}
		private class PingButton extends JButton{
			public PingButton(){
				super("Ping User");
				setBounds(220,0,120,20);
			}
		}
	}

	protected void banUser() {
		System.out.println("Banning user => " + matches.getValueAt(selection[0], selection[1]));
	}

	protected void stopMatch() {
		System.out.println("Stoppin match => " + matches.getValueAt(selection[0], 0));
	}

	protected void pingUser() {
		System.out.println("Pinging user => " + matches.getValueAt(selection[0], selection[1]));		
	}
}
