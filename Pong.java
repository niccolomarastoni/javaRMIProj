package tetraPong;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class Pong extends JPanel implements Runnable{
	private ServerPong serverPong;
	private int xSize = 504;
	private int ySize = 535;
	private float baseDX = 0;
	private float baseDY = 0;
	private float baseDK = 0; 
	private float baseDZ = 0; 
	private float baseSpeed = 3.0f;
	private final static int BASE_SMALL_SIZE = 10;
	private final static int BASE_BIG_SIZE = 50;
	private final static int BALL_SIZE = 20;
	private String pressSpace = "PRESS SPACE \nTO START";
	int p1Score = 0;
	int p2Score = 0;
	double dx;
	double dy;
	private double startPosX = 250;
	private double startPosY = 250;
	boolean running = false;
	private boolean singlePlayer = false;
	private boolean enabled = true;
	boolean gameEnded = false;	
	boolean starting = false;
	private boolean baseUpdate = false;
	private int startCounter = 450;
	private int baseFontSize = 180; 
	private Line2D.Double line;
	JFrame f;
	Ellipse2D.Double ball;
	Rectangle2D.Double leftBase;
	Rectangle2D.Double lowBase;
	Rectangle2D.Double rightBase;
	Rectangle2D.Double highBase;
	private VictoryPopUp victory;

	public Pong(ServerPong serverPong){
		super();
		this.serverPong = serverPong;
		line = new Line2D.Double(0,0,xSize,ySize - 30);
		ball = new Ellipse2D.Double(startPosX,startPosY,BALL_SIZE,BALL_SIZE);
		leftBase = new Rectangle2D.Double(0,225,BASE_SMALL_SIZE,BASE_BIG_SIZE);
		lowBase = new Rectangle2D.Double(225,490,BASE_BIG_SIZE,BASE_SMALL_SIZE);
		rightBase = new Rectangle2D.Double(490,225,BASE_SMALL_SIZE,BASE_BIG_SIZE);
		highBase =new Rectangle2D.Double(225,0,BASE_BIG_SIZE,BASE_SMALL_SIZE);
		this.setFocusable(true);
		f = new JFrame("tetraPong v. 1.0 (\\|) ( 0,,,0) (|/)");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exitGame();
			}
		});
		f.getContentPane().add("Center", this);
		f.setBounds(400,100,xSize,ySize);
		f.setVisible(true);
		addListeners();
		dx = -0.4;
		dy = -1;
	}

	public Pong(){
		super();
		line = new Line2D.Double(0,0,xSize,ySize - 30);
		ball = new Ellipse2D.Double(startPosX,startPosY,BALL_SIZE,BALL_SIZE);
		leftBase = new Rectangle2D.Double(0,225,BASE_SMALL_SIZE,BASE_BIG_SIZE);
		lowBase = new Rectangle2D.Double(225,490,BASE_BIG_SIZE,BASE_SMALL_SIZE);
		rightBase = new Rectangle2D.Double(490,225,BASE_SMALL_SIZE,BASE_BIG_SIZE);
		highBase =new Rectangle2D.Double(225,0,BASE_BIG_SIZE,BASE_SMALL_SIZE);
		this.setFocusable(true);
		f = new JFrame("tetraPong v. 0.5 (\\|) ( 0,,,0) (|/)");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				running = false;
				starting = false;
				enabled = false;
				System.exit(0);
			}
		});
		f.getContentPane().add("Center", this);
		f.setBounds(400,100,xSize,ySize);
		f.setVisible(true);
		addListeners();
		singlePlayer = true;
		dx = -0.4;
		dy = -1;
	}

	public void deleteProxyRef(){
		serverPong = null;
	}

	public void addListeners(){
		this.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent arg) {
				switch(arg.getKeyCode()){
				case 32:
					if(singlePlayer){
						running = !running; 
						pressSpace = "";
						if(running)
							starting = true;
					}

					break;
				case 87: baseDY = -baseSpeed; //w
				if(!singlePlayer){
					baseUpdate = true;
				}
				break;
				case 83: baseDY = baseSpeed; //s
				if(!singlePlayer){
					baseUpdate = true;
				}
				break;
				case 65: baseDX = - baseSpeed; //a
				if(!singlePlayer){
					baseUpdate = true;
				}
				break;
				case 68: baseDX = baseSpeed; //d
				if(!singlePlayer){
					baseUpdate = true;
				}
				break;
				case 38: baseDZ = -baseSpeed;
				break;
				case 40: baseDZ = baseSpeed;
				break;
				case 37: baseDK = -baseSpeed;
				break;
				case 39: baseDK = baseSpeed;
				break;

				default :
					break;
				}
			}

			@Override
			public void keyReleased(KeyEvent arg) {
				switch(arg.getKeyCode()){
				case 87:;
				case 83: baseDY = 0;
				if(!singlePlayer){
					baseUpdate = false;
				}
				break;
				case 65:;
				case 68: baseDX = 0;
				if(!singlePlayer){
					baseUpdate = false;
				}
				break;
				case 38:
				case 40: baseDZ = 0;
				break;
				case 37:
				case 39: baseDK = 0;
				break;
				default :break;
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}
		});
	}

	public void updateBallPos(){
		if(ball.x < -BALL_SIZE || ball.y > 500 || (singlePlayer && (ball.x > 480 + BALL_SIZE ||ball.y < -BALL_SIZE ))){
			if(singlePlayer){
				pressSpace = "PRESS SPACE \nTO START";
				p1Score += (ball.x < -BALL_SIZE || ball.y > 500)?0:1;
			}
			p2Score += (ball.y < -BALL_SIZE || ball.x > 480 + BALL_SIZE)?0:1;
			if(singlePlayer){
				running = false;
				starting = false;
			}
			lowBase.x = 225; lowBase.y = 490;
			highBase.x = 225; highBase.y = 0;
			leftBase.x = 0; leftBase.y = 225;
			rightBase.x = 490; rightBase.y = 225;

			ball.x = startPosX;
			ball.y = startPosY;
			double norm = 1.5;
			double angle = Math.random()*2.0*Math.PI/18 - Math.PI/18;
			int rotation =(int) (Math.random()*5.0);
			angle += rotation*Math.PI/2;
			dx = norm*Math.cos(angle);
			dy = norm*Math.sin(angle);

			if(!singlePlayer){
				if(p1Score == 5 || p2Score == 5){
					serverPong.updateOppentScore();
					starting = false;
					running = false;
					gameEnded = true;
					showVictory("YOU LOST", true);
				}
				else
					serverPong.updateOppentScore();
			}
			else{
				if(p1Score == 5 || p2Score == 5)
					showVictory((p1Score == 1)?"YOU WON":"YOU LOST", true);
			}
		}

		if((ball.x < 11 || ball.y > 470) || (singlePlayer && (ball.x > 460 || ball.y < 11))){
			double tempDx = dx;
			double tempDy = dy;
			checkCollision();
			if(!singlePlayer && (dx != tempDx || dy != tempDy)){
				serverPong.updateOpponentBallSpeed();
			}
		}

		ball.x += dx*(1 + dx*0.00014);
		ball.y += dy*(1 + dy*0.00014);
		repaint();
	}

	public void exitGame(){
		running = false;
		starting = false;
		enabled = false;
		if(singlePlayer)
			System.exit(0);

		Pong.this.serverPong.closeGame();
	}

	public void showVictory(String s, boolean bold){
		victory = new VictoryPopUp(s, bold);
	}

	public void showVictory(String s, boolean bold, String auxString){
		victory = new VictoryPopUp(s, bold, auxString);
	}

	private void hideVictory(){
		victory.setVisible(false);
	}

	private class VictoryPopUp extends JFrame{
		private OkButton ok;
		private JTextArea text;
		private JTextArea aux;
		public VictoryPopUp(String s, boolean bold){
			super("Game Over");
			text = new JTextArea(s);
			text.setVisible(true);
			text.setEditable(false);
			text.setFont(new Font("font",bold?Font.BOLD:Font.PLAIN, bold?32:20));
			text.setBounds(60,20,180,40);

			getContentPane().setLayout(null);
			getContentPane().setBackground(Color.white);
			setBounds(520,200, 300, 200);
			ok = new OkButton();
			ok.setBounds(100,130,80,30);
			ok.setVisible(true);
			getContentPane().add(text);
			getContentPane().add(ok);
			setResizable(false);
			setLocationRelativeTo(null);

			setVisible(true);
		}
		public VictoryPopUp(String s, boolean bold, String auxString){
			this(s, bold);
			aux = new JTextArea(auxString);
			aux.setVisible(true);
			aux.setEditable(false);
			aux.setFont(new Font("font",Font.PLAIN, 14));
			aux.setBounds(60,80,180,40);
			getContentPane().add(aux);
		}

		private class OkButton extends JButton{
			public OkButton(){
				super("OK");
				this.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						Pong.this.hideVictory();
						f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
					}});
			}
		}
	}

	private void checkCollision() { 
		double angle;
		double smallBias = 10;
		double bigBias = 40;
		double norm = Math.sqrt(dx*dx +dy*dy); 

		if(ball.x < 11 && ball.x > 0 && dx < 0){
			if(ball.intersects(leftBase)){
				if(baseDY != 0)
					dy += baseDY/4;
				if(ball.x < 11 && Math.abs(ball.y - leftBase.y) < smallBias){
					angle = Math.atan(((dy < 0) ? 16*(dy/dx): (dy/dx))/8);
					dy = -norm*Math.sin(angle);
					dx = norm*Math.cos(angle);
				}
				
				else if(ball.x < 11 && Math.abs(ball.y - leftBase.y) > bigBias){
					angle = Math.atan(((dy > 0) ? 16*(dy/dx) : (dy/dx))/8);
					dy = -norm*Math.sin(angle);
					dx = norm*Math.cos(angle);
				}
				
				else
					dx = -dx;
				
			}
		}

		if(singlePlayer && ( ball.x > 474 && ball.x < 485 && dx > 0)){
			if(ball.intersects(rightBase)){
				if(baseDZ != 0)
					dx += baseDZ/4;
				if(ball.x > 474 && Math.abs(ball.y + 10 - rightBase.y) < smallBias){
					angle = Math.atan(((dy < 0) ? 16*(dx/dy) : (dx/dy))/8);
					dy = norm*Math.sin(angle);
					dx = -norm*Math.cos(angle);
				}
				
				else if(ball.x > 474 && Math.abs(ball.y - rightBase.y) > bigBias){
					angle = Math.atan(((dy > 0) ? 16*(dx/dy) : (dx/dy))/8);
					dy = norm*Math.sin(angle);
					dx = -norm*Math.cos(angle);
				}
				
				else
					dx = -dx;

			}
		}

		if(singlePlayer && (ball.y < 11 && ball.y > 0 && dy < 0 )){
			if(ball.intersects(highBase)){
				if(baseDK != 0)
					dx += baseDK/4;
				
				if(ball.y < 11 && Math.abs(ball.x +15 - highBase.x) < smallBias){
					angle = Math.atan(((dx < 0) ? 16*(dx/dy): (dx/dy))/8);
					dy = norm*Math.cos(angle);
					dx = -norm*Math.sin(angle);
				}
				
				else if(ball.y < 11 && Math.abs(ball.x - highBase.x) > bigBias){
					angle = Math.atan(((dx > 0) ? 16*(dx/dy) : (dx/dy))/8);
					dy = norm*Math.cos(angle);
					dx = -norm*Math.sin(angle);
				}
				
				else
					dy = -dy;
				
			}
		}

		if(ball.y > 474 && ball.y < 485 && dy > 0){
			if(ball.intersects(lowBase)){
				if(baseDX != 0)
					dx += baseDX/4;
				if(ball.y > 474 && Math.abs(ball.x +5 - lowBase.x) < smallBias){
					angle = Math.atan(((dx < 0) ? 16*(dx/dy): (dx/dy))/8);
					dy = -norm*Math.cos(angle);
					dx = norm*Math.sin(angle);
				}
				
				else if(ball.y > 474 && Math.abs(ball.x - (lowBase.x+3)) > bigBias){
					angle = Math.atan(((dx > 0) ? 16*(dx/dy) : (dx/dy))/8);
					dy = -norm*Math.cos(angle);
					dx = norm*Math.sin(angle);
				}
				
				else
					dy = -dy;
				
			}
		}
	}
	
	private void updateBasePos(){
		if((leftBase.getY() + baseDY) >= 0 && (leftBase.getY() + baseDY) <= ySize - BASE_BIG_SIZE -31)
			leftBase.y += baseDY; 

		if((lowBase.getX() + baseDX) >= 0 && (lowBase.getX()+ baseDX) <= xSize - BASE_BIG_SIZE)
			lowBase.x += baseDX; 


		if(singlePlayer && ((rightBase.getY() + baseDZ) >= 0 && (rightBase.getY() + baseDZ) <= ySize - BASE_BIG_SIZE -31))
			rightBase.y += baseDZ; 


		if(singlePlayer && ((highBase.getX() + baseDK) >= 0 && (highBase.getX()+ baseDK) <= xSize - BASE_BIG_SIZE))
			highBase.x += baseDK; 
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.LIGHT_GRAY);
		g2.setFont(new Font("myFont",1,80));
		g2.drawString(String.valueOf(p1Score), 120, 400);
		g2.drawString(String.valueOf(p2Score), 320, 180);
		g2.draw(line);
		if(!starting){
			g2.setColor(Color.BLACK);
			g2.fill(ball);
		}

		updateBasePos();
		g2.setColor(Color.BLUE);
		g2.fill(lowBase);
		g2.fill(leftBase);
		g2.setColor(Color.RED);
		g2.fill(highBase);
		g2.fill(rightBase);
		g2.setFont(new Font("myFont",1,20));
		if(singlePlayer)
			g2.drawString(pressSpace, 130,240);

		if(starting){
			int shift = 150 - (startCounter % 150);

			g2.setFont(new Font("myFont",1,baseFontSize - shift));
			g2.drawString(String.valueOf(startCounter/150 + 1),180 + (shift /2), 320 - (shift / 2));

			if(startCounter-- == 0){
				starting = false;
				startCounter = 450;
			}
		}
	}

	public void startGame(){
		while(enabled){
			if(baseUpdate)
				serverPong.updateOpponentBase();

			if(running && !starting){
				updateBallPos();
			}

			else{
				repaint();
			}

			try {
				Thread.sleep(5);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		startGame();
	}
}
