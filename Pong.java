package tetraPong;
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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Pong extends JPanel implements Runnable{
		private ServerPong serverPong;
		private int xSize = 504;
		private int ySize = 535;
		private float baseDX = 0;
		private float baseDY = 0;

		//testing &&/|| configurabile
		private float baseDK = 0; //opponent x
		private float baseDZ = 0; //opponent y

		private float baseSpeed = 3.0f;

		private final static int BASE_SMALL_SIZE = 10;
		private final static int BASE_BIG_SIZE = 50;
		private final static int BALL_SIZE = 20;

		private String p1Name = "Player 1";
		private String p2Name = "Player 2";
		private String pressSpace = "PRESS SPACE \nTO START";

		// punteggi player 1 e 2
		int p1Score = 0;
		int p2Score = 0;

		double dx;
		double dy;
		private double startPosX = 250;
		private double startPosY = 250;
		//private double ballAcceleration = 0;// solo per testing, poi il valore sarà circa 0.00014; // non va bene cicicomerda
		boolean running = false;
		private boolean singlePlayer = false;
		private boolean enabled = true;
		boolean starting = false;
		private boolean baseUpdate = false;
		private int startCounter = 450;
		private int baseFontSize = 180; //180;
		private Line2D.Double line;
		Ellipse2D.Double ball;
		Rectangle2D.Double leftBase;
		Rectangle2D.Double lowBase;
		Rectangle2D.Double rightBase;
		Rectangle2D.Double highBase;

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
			JFrame f = new JFrame("tetraPong v. 0.5 (\\|) ( 0,,,0) (|/)");
			f.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					running = false;
					starting = false;
					enabled = false;
					Pong.this.serverPong.closeGame();
					}
			});
			f.getContentPane().add("Center", this);
			f.setSize(new Dimension(xSize,ySize));
			f.setVisible(true);
			addListeners();
			dx = -0.4;//Math.random()*2; // inizializzaione random... in fase di test è inutile :D
			dy = -1;//2 - dx;
		}
		public void deleteProxyRef(){
			serverPong = null;
		}
		public void addListeners(){

			this.addKeyListener(new KeyListener(){
				@Override
				public void keyPressed(KeyEvent arg) {// vanno creati dei metodi appositi per l'aggiornamento della pos dei myBase così
					//System.out.println(arg.getKeyCode());
					switch(arg.getKeyCode()){//da poterli fermare ai bordi sarebbe fichissimo accelerare il movimento se il tasto è premuto per un tot.
					case 32:
						if(singlePlayer){
							running = !running; // spazio per mettere in pausa o far ripartire un gioco
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
					default ://System.out.println("D:");
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
					// opponent
					case 38:
					case 40: baseDZ = 0;
					break;
					case 37:
					case 39: baseDK = 0;
					break;
					/// fine opponent
					default :break;
					}
				}

				@Override
				public void keyTyped(KeyEvent arg0) {
				}
			});
		}

		public void updateBallPos(){//il primo if va trasformato in un metodo così l'altro può chiamarlo in caso di ritadi sulla rete e aggiornare il punteggio
			if(ball.x < -BALL_SIZE || ball.y > 500 || (singlePlayer && (ball.x > 480 + BALL_SIZE ||ball.y < -BALL_SIZE ))){
				// se la pallina esce dal bordo incremento un punteggio (nessuno dei due se è uscito proprio sull'angolo)
				if(singlePlayer){
					pressSpace = "PRESS SPACE \nTO START";
					p1Score += (ball.x < -BALL_SIZE || ball.y > 500)?0:1;
				}
				p2Score += (ball.y < -BALL_SIZE || ball.x > 480 + BALL_SIZE)?0:1;
				if(singlePlayer){
					running = false;
					starting = true;
				}
				// riprisitiniamo anche la posizione delle basi
				lowBase.x = 225; lowBase.y = 490;
				highBase.x = 225; highBase.y = 0;
				leftBase.x = 0; leftBase.y = 225;
				rightBase.x = 490; rightBase.y = 225;

				ball.x = startPosX;
				ball.y = startPosY;
				double norm = 1.5;
				// ad ogni nuovo round diamo una piccola accelerazione in più;
				//ball.x += dx*(1 + dx*0.0002); //:D
				//ball.y += dy*(1 + dy*0.0002); //:D
				//dx = -0.6;//facciamo l'inizializzazione random
				//dy = -1.4;
				/* codice per ininizializzaione random*/
				double angle = Math.random()*2.0*Math.PI/18 - Math.PI/18;
				int rotation =(int) (Math.random()*5.0);
				angle += rotation*Math.PI/2;
				dx = norm*Math.cos(angle);
				dy = norm*Math.sin(angle);

				if(!singlePlayer)
					serverPong.updateOppentScore();

			}

			if((ball.x < 11 || ball.y > 470) || (singlePlayer && (ball.x > 460 || ball.y < 11))){
				double tempDx = dx;
				double tempDy = dy;
				checkCollision();
				if(!singlePlayer && (dx != tempDx || dy != tempDy)){
					serverPong.updateOpponentBallSpeed();
					//System.out.println("URTO");
				}
			}

			ball.x += dx*(1 + dx*0.00014);
			ball.y += dy*(1 + dy*0.00014);//dopo avere aggiornato la pos serve metodo per stabilire a chi tocca aggiornare la pos
			repaint();
		}

		// idea: la nostra pallina per ora si muove in moto rettilineo uniforme, quindi il vettore di velocità
		// deve mantenere il modulo uguale, quindi sqr(dx^2 + dy^2) è sempre radice di 2
		// per far cambiare l'angolo dopo la collisione modifichiamo l'angolo d'uscita
		// prendendo l'angolo di collisione e ricavando il nuovo dx = sqr(2)*cost
		// e il nuovo dy = sqr(2)*sint

		private void checkCollision() { // per adesso ho fatto una sola variazione all'angolo d'uscita (per le basi verticali)
			double angle;
			double smallBias = 10;
			double bigBias = 40;
			double norm = Math.sqrt(dx*dx +dy*dy); // così la palla può accelerare

			if(ball.x < 11 && ball.x > 0 && dx < 0){
				if(ball.intersects(leftBase)){// controllo per l'effetto :D
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
				if(ball.intersects(rightBase)){// controllo per l'effetto :D
					if(baseDZ != 0)
						dx += baseDZ/4;
					if(ball.x > 474 && Math.abs(ball.y + 10 - rightBase.y) < smallBias){// piccolo aggiustamento perchè si trova a destra D:

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
				if(ball.intersects(highBase)){// controllo per l'effetto :D

					if(baseDK != 0);
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
				if(ball.intersects(lowBase)){// controllo per l'effetto :D
					if(baseDX != 0)
						dx += baseDX/4;
					if(ball.y > 474 && Math.abs(ball.x +5 - lowBase.x) < smallBias){

						angle = Math.atan(((dx < 0) ? 16*(dx/dy): (dx/dy))/8);
						dy = -norm*Math.cos(angle);
						dx = norm*Math.sin(angle);
					}
					else if(ball.y > 474 && Math.abs(ball.x - (lowBase.x+3)) > bigBias){

						angle = Math.atan(((dx > 0) ? 16*(dx/dy) : (dx/dy))/8);
						System.out.println("Angle "+angle);
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
				leftBase.y += baseDY; // ((Math.abs(baseDY) > 6)? baseDY : (baseDY += baseDY/20));


			//System.out.println("VELOCITAAAAA: " + baseDY);

			if((lowBase.getX() + baseDX) >= 0 && (lowBase.getX()+ baseDX) <= xSize - BASE_BIG_SIZE)
				lowBase.x += baseDX; // (Math.abs(baseDX) > 6)? baseDX : (baseDX += baseDX/20);


			if(singlePlayer && ((rightBase.getY() + baseDZ) >= 0 && (rightBase.getY() + baseDZ) <= ySize - BASE_BIG_SIZE -31))
				rightBase.y += baseDZ; // (Math.abs(baseDZ) > 6)? baseDZ : (baseDZ += baseDZ/20);


			if(singlePlayer && ((highBase.getX() + baseDK) >= 0 && (highBase.getX()+ baseDK) <= xSize - BASE_BIG_SIZE))
				highBase.x += baseDK; // (Math.abs(baseDK) > 6)? baseDK : (baseDK += baseDK/20);
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
			//if(running && !starting)
			updateBasePos();
			//g2.draw(ball);
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
					startCounter = 450;// se dovesse dare problemi qui c'è la soluzione
				}

			}
		}

		public void startGame(){
			while(enabled){
				if(baseUpdate)
					serverPong.updateOpponentBase();

				//System.out.println(dx);
				if(running && !starting){
					updateBallPos();
					//System.out.println(dx);
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
