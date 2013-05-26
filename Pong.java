import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
	private float baseDX = 0;
	private float baseDY = 0;

	//testing &&/|| configurabile 
	private float baseDK = 0; //opponent x
	private float baseDZ = 0; //opponent y

	private float baseSpeed = 1.8f;

	private final static int BASE_SMALL_SIZE = 10;
	private final static int BASE_BIG_SIZE = 50;
	private final static int BALL_SIZE = 20;

	private String p1Name = "Player 1";
	private String p2Name = "Player 2";
	private String pressSpace = "PRESS SPACE \nTO START";

	// punteggi player 1 e 2
	private int p1Score = 0;
	private int p2Score = 0;

	private double dx;
	private double dy;
	//private double ballAcceleration = 0;// solo per testing, poi il valore sarà circa 0.00014; // non va bene cicicomerda
	private boolean computing = true;
	private static boolean running = false;
	private boolean starting = false;
	private int startCounter = 450;
	private int baseFontSize = 180;
	private Line2D.Double line;
	private Ellipse2D.Double ball;
	private Rectangle2D.Double leftBase;
	private Rectangle2D.Double lowBase;
	private Rectangle2D.Double rightBase;
	private Rectangle2D.Double highBase;

	public Pong(){// qualcuno deve partire con l'elaborazione e poi c'è il problema di come vede ciascun utente la prorpia finestre e
		super();// come effettivamente funziona il gioco
		line = new Line2D.Double(0,0,xSize,ySize - 30);
		ball = new Ellipse2D.Double(250,250,BALL_SIZE,BALL_SIZE);
		leftBase = new Rectangle2D.Double(0,225,BASE_SMALL_SIZE,BASE_BIG_SIZE);
		lowBase = new Rectangle2D.Double(225,490,BASE_BIG_SIZE,BASE_SMALL_SIZE);
		rightBase = new Rectangle2D.Double(490,225,BASE_SMALL_SIZE,BASE_BIG_SIZE);
		highBase =new Rectangle2D.Double(225,0,BASE_BIG_SIZE,BASE_SMALL_SIZE);
		this.setFocusable(true);
		JFrame f = new JFrame("SuperPong v. 0.2 D:");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);}
		});

		f.getContentPane().add("Center", this);
		f.setSize(new Dimension(xSize,ySize));
		f.setVisible(true);
		addListeners();
		dx = 1;//Math.random()*2; // inizializzaione random... in fase di test è inutile :D
		dy = 0.4;//2 - dx;
	}


	public void addListeners(){

		this.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent arg) {// vanno creati dei metodi appositi per l'aggiornamento della pos dei myBase così
				System.out.println(arg.getKeyCode());
				switch(arg.getKeyCode()){//da poterli fermare ai bordi sarebbe fichissimo accelerare il movimento se il tasto è premuto per un tot.
				case 32: running = !running; // spazio per mettere in pausa o far ripartire un gioco
				pressSpace = "";
				if(running)
					starting = true;

				break;
				case 87: baseDY = -baseSpeed; //w
				break;
				case 83: baseDY = baseSpeed; //s
				break;
				case 65: baseDX = - baseSpeed; //a
				break;
				case 68: baseDX = baseSpeed; //d
				break;
				// da qui in poi opponent, per testing (oppure poi facciamo configurabile, qualcuno preferisce freccette)
				case 38: baseDZ = -baseSpeed;
				break;
				case 40: baseDZ = baseSpeed;
				break;
				case 37: baseDK = -baseSpeed;
				break;
				case 39: baseDK = baseSpeed;
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
			}
		});
	}

	public void updateBallPos(){
		if(ball.x < -BALL_SIZE || ball.y < -BALL_SIZE || ball.x > 480 + BALL_SIZE || ball.y > 500){
			// se la pallina esce dal bordo incremento un punteggio (nessuno dei due se è uscito proprio sull'angolo)
			pressSpace = "PRESS SPACE \nTO START";
			p1Score += (ball.x < -BALL_SIZE ||  ball.y > 500)?0:1;
			p2Score += (ball.y < -BALL_SIZE || ball.x > 480 + BALL_SIZE)?0:1;
			running = false;

			ball.x = 250;
			ball.y = 250;
			// ad ogni nuovo round diamo una piccola accelerazione in più;
			ball.x += dx*(1 + dx*0.0002); //:D
			ball.y += dy*(1 + dy*0.0002);  //:D
			//dx = -0.6;//facciamo l'inizializzazione random
			//dy = -1.4;
			/* codice per ininizializzaione random*/
			double norm = Math.sqrt(dx*dx + dy*dy);
			double angle = Math.random()*(3*Math.PI/2) - Math.PI/2; // angle va tra 3/2PI e -1/2PI
			dx = norm*Math.cos(angle);
			dy = norm*Math.sin(angle);

		}

		if(ball.x < 11 || ball.x > 460 || ball.y < 11 || ball.y > 470)
			checkCollision();

		ball.x += dx*(1 + dx*0.00014); 
		ball.y += dy*(1 + dy*0.00014);//dopo avere aggiornato la pos serve metodo per stabilire a chi tocca aggiornare la pos
		repaint();
		//	checkIntersection();
	}

	// idea: la nostra pallina per ora si muove in moto rettilineo uniforme, quindi il vettore di velocità
	// deve mantenere il modulo uguale, quindi sqr(dx^2 + dy^2) è sempre radice di 2
	// per far cambiare l'angolo dopo la collisione modifichiamo l'angolo d'uscita
	// prendendo l'angolo di collisione e ricavando il nuovo dx = sqr(2)*cost 
	// e il nuovo dy = sqr(2)*sint

	private void checkCollision() { // per adesso ho fatto una sola variazione all'angolo d'uscita (per le basi verticali)
		// se riesci implementane altre, se no puppa
		double angle;
		double smallBias = 10; 
		double bigBias = 40;
		double norm = Math.sqrt(dx*dx +dy*dy); // così la palla può accelerare

		if(ball.x < 11 && ball.x > 0 && dx < 0){
			if(ball.intersects(leftBase)){// controllo per l'effetto :D
				if(baseDY != 0)
					dy += baseDY/4;
				if(ball.x < 11 && Math.abs(ball.y - leftBase.y) < smallBias){
					System.out.println("Small bias");	

					angle = Math.atan(((dy < 0) ? 16*(dy/dx): (dy/dx))/8);
					dy = -norm*Math.sin(angle);
					dx = -norm*Math.cos(angle);
					System.out.println("dx"+dx +"dy"+ dy);
				}
				else if(ball.x < 11  && Math.abs(ball.y - leftBase.y) > bigBias){
					System.out.println("Big bias");

					angle = Math.atan(((dy > 0) ? 16*(dy/dx) : (dy/dx))/8);
					System.out.println("Angle "+angle);
					dy = -norm*Math.sin(angle);
					dx = -norm*Math.cos(angle);
					System.out.println("dx"+dx +"dy"+ dy);
				}
				else
					System.out.println("normal");
				dx = -dx;
			}
		}

		if( ball.x > 474  && ball.x < 485 && dx > 0){
			if(ball.intersects(rightBase)){// controllo per l'effetto :D
				System.out.println("Small angle" + Math.abs(ball.y - rightBase.y));
				if(baseDZ != 0)
					dx += baseDZ/4;
				if(ball.x > 474 && Math.abs(ball.y + 10 - rightBase.y) < smallBias){// piccolo aggiustamento perchè si trova a destra D:
					System.out.println("Small bias");	

					angle = Math.atan(((dy < 0) ? 16*(dx/dy) : (dx/dy))/8);
					dy = norm*Math.sin(angle);
					dx = norm*Math.cos(angle);
					System.out.println("dx"+dx +"dy"+ dy);
				}
				else if(ball.x > 474 && Math.abs(ball.y - rightBase.y) > bigBias){
					System.out.println("Big bias");

					angle = Math.atan(((dy > 0) ? 16*(dx/dy) : (dx/dy))/8);
					dy = norm*Math.sin(angle);
					dx = norm*Math.cos(angle);
					System.out.println("dx"+dx +"dy"+ dy);
				}
				else
					System.out.println("normal");
				dx = -dx;

			}
		}

		if(ball.y < 11 && ball.y > 0 && dy < 0 ){ 
			if(ball.intersects(highBase)){// controllo per l'effetto :D

				System.out.println("Small angle" + Math.abs(ball.x - highBase.x));
				if(baseDK != 0);
				dx += baseDK/4;
				if(ball.y < 11 && Math.abs(ball.x +15 - highBase.x) < smallBias){
					System.out.println("Small bias");	

					angle = Math.atan(((dx < 0) ? 16*(dx/dy): (dx/dy))/8);
					dy = -norm*Math.cos(angle);
					dx = -norm*Math.sin(angle);
					System.out.println("dx"+dx +"dy"+ dy);
				}
				else if(ball.y < 11  && Math.abs(ball.x - highBase.x) > bigBias){
					System.out.println("Big bias");

					angle = Math.atan(((dx > 0) ? 16*(dx/dy) : (dx/dy))/8);
					System.out.println("Angle "+angle);
					dy = -norm*Math.cos(angle);
					dx = -norm*Math.sin(angle);
					System.out.println("dx"+dx +"dy"+ dy);
				}
				else
					System.out.println("normal");
				dy = -dy;
			}
		}

		if(ball.y > 474 && ball.y < 485 && dy > 0){
			if(ball.intersects(lowBase)){// controllo per l'effetto :D
				if(baseDX != 0)
					dx += baseDX/4;
				if(ball.y > 474 && Math.abs(ball.x +5 - lowBase.x) < smallBias){
					System.out.println("Small bias");	

					angle = Math.atan(((dx < 0) ? 16*(dx/dy): (dx/dy))/8);
					dy = norm*Math.cos(angle);
					dx = norm*Math.sin(angle);
					System.out.println("dx"+dx +"dy"+ dy);
				}
				else if(ball.y > 474  && Math.abs(ball.x - (lowBase.x+3)) > bigBias){
					System.out.println("Big bias");

					angle = Math.atan(((dx > 0) ? 16*(dx/dy) : (dx/dy))/8);
					System.out.println("Angle "+angle);
					dy = norm*Math.cos(angle);
					dx = norm*Math.sin(angle);
					System.out.println("dx"+dx +"dy"+ dy);
				}
				else
					System.out.println("normal");
				dy = -dy; 
			}
		}


	}
	public void updateOpponentBase(){ // riceve aggiornamenti dall'altro giocatore sulle proprie posizioni sarebbe carino usare la classe 2dPosition
		// magari questo metodo restituisce la posizione dei propri base.
	} // questo metodo lo invoca chi è incaricato di calcorare la posizione della palla.

	public void startElaboration(){ // metodo che chiama un giocatore quando supera la soglia così avvisa l'altro che tocca a lui elaborare la pos.

	}

	private void updateBasePos(){
		if((leftBase.getY() + baseDY) >= 0 && (leftBase.getY() + baseDY) <= ySize - BASE_BIG_SIZE -31)
			leftBase.y += baseDY; // ((Math.abs(baseDY) > 6)? baseDY : (baseDY += baseDY/20));

		//System.out.println("VELOCITAAAAA: " + baseDY);

		if((lowBase.getX() + baseDX) >= 0 && (lowBase.getX()+ baseDX) <= xSize - BASE_BIG_SIZE)
			lowBase.x += baseDX; // (Math.abs(baseDX) > 6)? baseDX : (baseDX += baseDX/20);


		if((rightBase.getY() + baseDZ) >= 0 && (rightBase.getY() + baseDZ) <= ySize - BASE_BIG_SIZE -31)
			rightBase.y += baseDZ; // (Math.abs(baseDZ) > 6)? baseDZ : (baseDZ += baseDZ/20); 

		if((highBase.getX() + baseDK) >= 0 && (highBase.getX()+ baseDK) <= xSize - BASE_BIG_SIZE)
			highBase.x += baseDK; // (Math.abs(baseDK) > 6)? baseDK : (baseDK += baseDK/20);
	}

	public void paint(Graphics g) {
		updateBasePos();
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
		//g2.draw(ball);
		g2.setColor(Color.BLUE);
		g2.fill(lowBase);
		g2.fill(leftBase);
		g2.setColor(Color.RED);
		g2.fill(highBase);
		g2.fill(rightBase);
		g2.setFont(new Font("myFont",1,20));
		g2.drawString(pressSpace, 130,240);
		if(starting){ 
			int shift = 150 - (startCounter % 150);
			g2.setFont(new Font("myFont",1,(baseFontSize - shift)));
			g2.drawString(String.valueOf(startCounter/150 + 1),180 + (shift /2), 320 - (shift / 2));

			if(startCounter-- == 0){
				starting = false;
				startCounter = 450;
			}
		}
	}

	public void startGame(){
		while(true){
			if(running && !starting)
				updateBallPos();

			else{
				updateBasePos();
				repaint();
			}

			try {
				Thread.sleep(5);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void setBallPosition(double x, double y){
		ball.x = x;
		ball.y = y;
	}

	public double getBallX(){
		return ball.x;
	}

	public double getBallY(){
		return ball.y;
	}

	public void setBallSpeed(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}


	public double getDx() {
		return dx;
	}


	public double getDy() {
		return dy;
	}


	public boolean opponentTurn() {
		return ball.x > ball.y;
	}
}
