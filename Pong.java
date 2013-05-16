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
	private final static int BASE_WIDTH = 10;
	private final static int BASE_HEIGHT = 50;
	private final static int BALL_SIZE = 20;
	private int dx = -1;
	private int dy = -1;
	private boolean computing = true;
	private Ellipse2D.Double ball;
	private Rectangle2D.Double myVBase;// o meglio farli canvas?
	private Rectangle2D.Double myHBase;
	private Rectangle2D.Double opponentVBase;
	private Rectangle2D.Double opponentHBase;

	public Pong(){// qualcuno deve partire con l'elaborazione e poi c'è il problema di come vede ciascun utente la prorpia finestre e
		super();// come effettivamente funziona il gioco
		ball = new Ellipse2D.Double(250,250,BALL_SIZE,BALL_SIZE);
		myVBase = new Rectangle2D.Double(0,225,BASE_WIDTH,BASE_HEIGHT);
		myHBase = new Rectangle2D.Double(225,490,BASE_HEIGHT,BASE_WIDTH);
		opponentVBase = new Rectangle2D.Double(490,225,BASE_WIDTH,BASE_HEIGHT);
		opponentHBase =new Rectangle2D.Double(225,0,BASE_HEIGHT,BASE_WIDTH);
		this.setFocusable(true);
		addListeners();
	}


	public void addListeners(){

		this.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg) {// vanno creati dei metodi appositi per l'aggiornamento della pos dei myBase così
				System.out.println(arg.getKeyCode());
				switch(arg.getKeyChar()){//da poterli fermare ai bordi sarebbe fichissimo accelerare il movimento se il tasto è premuto per un tot.
				case 'w':;
				case 'W': baseDY = -5;// va implementato con il release
				repaint();
				break;
				case 's':;
				case 'S': baseDY = 5;
				repaint();
				break;
				case 'a':;
				case 'A': baseDX = - 5;
				break;
				case 'd':;
				case 'D': baseDX = 5;
				break;
				default :System.out.println("D:");break;
				}
				/*System.out.println(arg.getKeyCode());
				if( arg.getKeyCode() == 38) // up
					myVBase.y = myVBase.getY() - 10;
				else if(arg.getKeyCode() == 40) // down
					;
				else if(arg.getKeyCode() == 38)
					;
				else if(arg.getKeyCode() == 38)
					;*/
			}

			@Override
			public void keyReleased(KeyEvent arg) {
				switch(arg.getKeyChar()){//da poterli fermare ai bordi sarebbe fichissimo accelerare il movimento se il tasto è premuto per un tot.
				case 'w':;
				case 'W':;// va implementato con il release
				case 's':;
				case 'S': baseDY = 0;
				repaint();
				break;
				case 'a':;
				case 'A': ;
				case 'd':;
				case 'D': baseDX = 0;
				repaint();
				break;
				default :break;
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

	}

	public void updatePosition(){
		ball.x = ball.getX() + dx;
		ball.y = ball.getY() + dy;//dopo avere aggiornato la pos serve metodo per stabilire a chi tocca aggiornare la pos
		repaint();
		checkIntersection();
	}

	private void checkIntersection(){//controllo se incrocia una base e se esce dai bordi i punteggi e ripristino dal centro con direzione arbitraria
		if(computing){
			// ci vogliono variabili dx e dy per i base per dare "effetto alla palla" nb solo se la base è in movimento
			//myVBase.intersects(r)
			//ball.i
		}
	}
	public void updateOpponentBase(){ // riceve aggiornamenti dall'altro giocatore sulle proprie posizioni sarebbe carino usare la classe 2dPosition
		// magari questo metodo restituisce la posizione dei propri base.
	} // questo metodo lo invoca chi è incaricato di calcorare la posizione della palla.

	public void startElaboration(){ // metodo che chiama un giocatore quando supera la soglia così avvisa l'altro che tocca a lui elaborare la pos.

	}
	private void updateBasePos(){
		if((myVBase.getY() + baseDY) >= 0 && (myVBase.getY() + baseDY) <= ySize - BASE_HEIGHT -31)
			myVBase.y = myVBase.getY() + baseDY;
		if((myHBase.getX() + baseDX) >= 0 && (myHBase.getX()+ baseDX) <= xSize - BASE_HEIGHT)
			myHBase.x = myHBase.getX() + baseDX;
	}

	public void paint(Graphics g) {
		updateBasePos();
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(new Color(0, 0, 200));
		g2.fill(ball);
		//g2.draw(ball);
		g2.draw(myHBase);
		g2.fill(myVBase);
		g2.draw(opponentHBase);
		g2.draw(opponentVBase);
	}
	public static void startGame(){
		JFrame f = new JFrame("ShapesDemo2D");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);}
		});
		Pong applet = new Pong();
		f.getContentPane().add("Center", applet);
		f.setSize(new Dimension(xSize,ySize));
		f.setVisible(true);
		while(true){
			applet.updatePosition();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static void main(String[] argv){
		Pong.startGame();
	}

}
