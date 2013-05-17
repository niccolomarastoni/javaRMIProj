import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.*;

import javax.swing.JFrame;
import javax.swing.JPanel;



public class Pong extends JPanel{
	private static int xSize = 504;
	private static int ySize = 535;
	private int baseDX = 0;
	private int baseDY = 0;

	//testing &&/|| configurabile 
	private int baseDK = 0; //opponent x
	private int baseDZ = 0; //opponent y

	private final static int BASE_WIDTH = 10;
	private final static int BASE_HEIGHT = 50;
	private final static int BALL_SIZE = 20;
	private double dx = -1;
	private double dy = -1;
	private boolean computing = true;
	private Ellipse2D.Double ball;
	private Rectangle2D.Double leftBase;
	private Rectangle2D.Double lowBase;
	private Rectangle2D.Double rightBase;
	private Rectangle2D.Double highBase;

	public Pong(){// qualcuno deve partire con l'elaborazione e poi c'è il problema di come vede ciascun utente la prorpia finestre e
		super();// come effettivamente funziona il gioco
		ball = new Ellipse2D.Double(250,400,BALL_SIZE,BALL_SIZE);
		leftBase = new Rectangle2D.Double(0,225,BASE_WIDTH,BASE_HEIGHT);
		lowBase = new Rectangle2D.Double(225,490,BASE_HEIGHT,BASE_WIDTH);
		rightBase = new Rectangle2D.Double(490,225,BASE_WIDTH,BASE_HEIGHT);
		highBase =new Rectangle2D.Double(225,0,BASE_HEIGHT,BASE_WIDTH);
		this.setFocusable(true);
		addListeners();
	}


	public void addListeners(){

		this.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg) {// vanno creati dei metodi appositi per l'aggiornamento della pos dei myBase così
				//System.out.println(arg.getKeyCode());
				switch(arg.getKeyCode()){//da poterli fermare ai bordi sarebbe fichissimo accelerare il movimento se il tasto è premuto per un tot.
				case 87: baseDY = -5; //w
				break;
				case 83: baseDY = 5; //s
				break;
				case 65: baseDX = - 5; //a
				break;
				case 68: baseDX = 5; //d
				break;
				// da qui in poi opponent, per testing (oppure poi facciamo configurabile, qualcuno preferisce freccette)
				case 38: baseDZ = -5;
				break;
				case 40: baseDZ = 5;
				break;
				case 37: baseDK = -5;
				break;
				case 39: baseDK = 5;
				break;
				// fine opponent
				default :System.out.println("D:");break;
				}
			}

			@Override
			public void keyReleased(KeyEvent arg) {
				switch(arg.getKeyCode()){
				case 87:;
				case 83: baseDY = 0;
				break;
				case 65:;
				case 68: baseDX = 0;
				break;
				// opponent
				case 38:
				case 40: baseDZ = 0;
				break;
				case 37:
				case 39: baseDK = 0;
				break;
				// fine opponent
				default :break;
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

	}

	public void updateBallPosition(){
		if(ball.x < 11 || ball.x > 473 || ball.y < 11 || ball.y > 400)
			checkCollision();

		ball.x += dx = dx*(1 + 0.00005);
		ball.y += dy = dy*(1 + 0.00005);//dopo avere aggiornato la pos serve metodo per stabilire a chi tocca aggiornare la pos
		repaint();
		checkIntersection();
	}

	private void checkCollision() {

		//double collisionAngle = Math.atan(dx/dy);
		//collisionAngle = (dx > 0)?collisionAngle:collisionAngle + Math.PI; // ricavo l'angolo del vettore 
		double exitAngle;
		exitAngle = Math.atan(-dx/dy);
		// idea: la nostra pallina per ora si muove in moto rettilineo uniforme, quindi il vettore di velocità
		// deve mantenere il modulo uguale, quindi sqr(dx^2 + dy^2) è sempre radice di 2
		// per far cambiare l'angolo dopo la collisione modifichiamo l'angolo d'uscita
		// prendendo l'angolo di collisione e ricavando il nuovo dx = sqr(2)*cost 
		// e il nuovo dy = sqr(2)*sint
		//System.out.println(angle);

		if(((ball.x < 11) && dx < 0 || (ball.x > 480) && dx > 0)){
			if(checkIntersection()){
				exitAngle = (dy < 0)?exitAngle:exitAngle + Math.PI;
				dx = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy,2))*Math.cos(exitAngle);
			}
		}

		if(((ball.y < 11) && dy < 0) || ((ball.y > 450) && dy > 0)){
			if(checkIntersection()){
			exitAngle = (dy < 0)?exitAngle:exitAngle + Math.PI;
			dy = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy,2))*Math.sin(exitAngle);
			}
		}
	}


	private boolean checkIntersection(){//controllo se incrocia una base e se esce dai bordi i punteggi e ripristino dal centro con direzione arbitraria
			return ball.intersects(leftBase) || ball.intersects(rightBase) || ball.intersects(highBase) || ball.intersects(lowBase);

	/*	
		if(computing){
			// ci vogliono variabili dx e dy per i base per dare "effetto alla palla" nb solo se la base è in movimento
			//myVBase.intersects(r)
			//ball.i

		}
*/
	}
	public void updateOpponentBase(){ // riceve aggiornamenti dall'altro giocatore sulle proprie posizioni sarebbe carino usare la classe 2dPosition
		// magari questo metodo restituisce la posizione dei propri base.
	} // questo metodo lo invoca chi è incaricato di calcorare la posizione della palla.

	public void startElaboration(){ // metodo che chiama un giocatore quando supera la soglia così avvisa l'altro che tocca a lui elaborare la pos.

	}
	private void updateBasePos(){
		if((leftBase.getY() + baseDY) >= 0 && (leftBase.getY() + baseDY) <= ySize - BASE_HEIGHT -31)
			leftBase.y += baseDY;
		if((lowBase.getX() + baseDX) >= 0 && (lowBase.getX()+ baseDX) <= xSize - BASE_HEIGHT)
			lowBase.x += baseDX;

		if((rightBase.getY() + baseDZ) >= 0 && (rightBase.getY() + baseDZ) <= ySize - BASE_HEIGHT -31)
			rightBase.y += baseDZ; 
		if((highBase.getX() + baseDK) >= 0 && (highBase.getX()+ baseDK) <= xSize - BASE_HEIGHT)
			highBase.x += baseDK;

	}

	public void paint(Graphics g) {
		updateBasePos();
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.BLACK);
		g2.fill(ball);
		//g2.draw(ball);
		g2.setColor(Color.BLUE);
		g2.fill(lowBase);
		g2.fill(leftBase);
		g2.setColor(Color.RED);
		g2.fill(highBase);
		g2.fill(rightBase);
	}

	public static void startGame(){
		JFrame f = new JFrame("SuperPong v. 0.1");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);}
		});
		Pong applet = new Pong();
		f.getContentPane().add("Center", applet);
		f.setSize(new Dimension(xSize,ySize));
		f.setVisible(true);
		while(true){
			applet.updateBallPosition();
			try {
				Thread.sleep(5);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static void main(String[] argv){
		Pong.startGame();
	}

}
