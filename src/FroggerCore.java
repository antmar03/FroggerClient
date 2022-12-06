import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class FroggerCore extends JFrame implements KeyListener, ActionListener{

	
	int CLIENT_PORT = 5656;
	final static int SERVER_PORT = 5556;
	
	//Graphical Content
	private JButton startButton;
	private JLabel lCat,lCat2,lBackground,score;
	private Container content;
	private ImageIcon iCat, iCar;
	private ConnectToDatabase db;
	private int catNumber;
	private static String playerName;
	private boolean isOver,moving;
	//Sprites
	private Cat cat, cat2;
	private static FroggerCore game = null;
	//private Car car;
	Socket s;
	OutputStream outstream; 
	PrintWriter out;
	private static RowHandler rowHandler,logRowHandler;
	
	private void initializeWindow() {
		setSize(Properties.SCREEN_WIDTH, Properties.SCREEN_HEIGHT);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter()
		{
		    @Override
		    public void windowClosing(WindowEvent e)
		    {
		        super.windowClosing(e);
		        db.closeConnection();
		        System.out.println("Closing db");
		    }
		});
	}
	
	private void initializeGraphics() {
		content = getContentPane();
		cat = new Cat();
		cat.setX((Properties.SCREEN_WIDTH / 2) - Properties.catWidth);
		cat.setY((Properties.SCREEN_HEIGHT - Properties.catHeight) - 70);
		cat2 = new Cat();
		cat2.setX((Properties.SCREEN_WIDTH / 2) - Properties.catWidth);
		cat2.setY((Properties.SCREEN_HEIGHT - Properties.catHeight) - 70);
		
		
		
		lBackground = new JLabel();
		lBackground.setSize(Properties.SCREEN_WIDTH, Properties.SCREEN_HEIGHT);
		lBackground.setIcon(new ImageIcon(getClass().getResource(Properties.BACKGROUND_IMAGE)));
		
		lCat = new JLabel();
		lCat2 = new JLabel();
		
		startButton = new JButton("Start");
		startButton.setSize(200,100);
		startButton.setLocation(Properties.SCREEN_WIDTH-200, Properties.SCREEN_HEIGHT - 200);
		startButton.addActionListener(this);
		startButton.setFocusable(false);
		
		iCat = new ImageIcon(getClass().getResource(cat.getImg()));
		
		//Set up the labels
		lCat.setIcon(iCat);
		lCat.setSize(cat.getWidth(), cat.getHeight());
		lCat.setLocation(cat.getX(), cat.getY());
		cat.setCatLabel(lCat);
		
		//Set up the labels
		lCat2.setIcon(iCat);
		lCat2.setSize(cat2.getWidth(), cat2.getHeight());
		lCat2.setLocation(cat2.getX(), cat2.getY());
		cat2.setCatLabel(lCat);
		
		score = new JLabel();
		score.setLocation(Properties.SCREEN_WIDTH/2, 50);
		score.setFont(new Font("Serif", Font.PLAIN, 25));
		score.setSize(60,60);
		cat.setScoreLabel(score);
		cat2.setScoreLabel(score);
		

		rowHandler = new RowHandler(3, 4, RowType.CAR, Properties.ROW_START, cat, lCat,cat2,lCat2);
		logRowHandler = new RowHandler(3,4, RowType.LOG, Properties.LOG_ROW_START, cat, lCat,cat2,lCat2);
		
		
		content.add(score);
		content.add(startButton);
		content.add(lCat);
		content.add(lCat2);
		rowHandler.applyRows(content);
		logRowHandler.applyRows(content);
		content.add(lBackground);
		content.setBackground(Color.GRAY);
		content.addKeyListener(this);
		content.setFocusable(true);
		
	}
	
	public void setCatNumber(int catNumber) {
		this.catNumber = catNumber;
	}
	
	public int getCatNumber() {
		return catNumber;
	}
	
	private void updateCatLabel(Cat cat, int catNumber) {
		if(catNumber == 1) {
			lCat.setLocation(cat.getX(), cat.getY());
		}else {
			lCat2.setLocation(cat.getX(), cat.getY());
		}
	}
	
	public void updatePlayer(int x, int y, int catNumber) {
		if(catNumber == 1) {
			cat.setX(x);
			cat.setY(y);
			updateCatLabel(cat,catNumber);
			
		}else {
			cat2.setX(x);
			cat2.setY(y);
			updateCatLabel(cat2, catNumber);
		}
		
	}
	
	public FroggerCore() {
		isOver = false;
		moving = false;
		playerName = "Anthony";
		
		//Initialize the Graphics Settings
		initializeGraphics();
		
		db = ConnectToDatabase.getInstance(cat, this.playerName);
		//db.setPlayerValues(this.playerName);
		cat.setScore(db.getPlayerScore(playerName));
		score.setText(Integer.toString(cat.getScore()));
		//Initialize Window Settings
		initializeWindow();
		
		ServerSocket client = null;
		try {
			client = new ServerSocket(CLIENT_PORT);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			try {
				client = new ServerSocket(CLIENT_PORT + 1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		final ServerSocket finalClient = client;
		
		//set up listening server
		Thread t1 = new Thread ( new Runnable () {
			public void run ( ) {
				synchronized(this) {
					
					System.out.println("Waiting for server responses...");
					while(true) {
						Socket s2;
						try {
							s2 = finalClient.accept();
							ClientService cService = new ClientService (s2);
							Thread t = new Thread(cService);
							t.start();
							
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("client connected");
						
					}

					
				}
			}
		});
		
		t1.start( );

		
		
		//set up a communication socket
		try {
			s = new Socket("localhost", SERVER_PORT);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Initialize data stream to send data out
		try {
			outstream = s.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	    out = new PrintWriter(outstream);

		String command = "GET\n";
		System.out.println("Sending: " + command);
		out.println(command);
		out.flush();
		
		//command = "PLAYER 1 DOWN\n";
		//System.out.println("Sending: " + command);
		//out.println(command);
		//out.flush();
		
		//s.close();


		

		
		
		
	}
	
	public static void main(String[] args) {
		game = new FroggerCore();
		game.setVisible(true);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int x = cat.getX();
		int y = cat.getY();
		int step = Properties.STEP;

		if(cat.getOnLog()) {
			step = Properties.logHeight + Properties.catHeight + Properties.ROW_SPACING;
		}
		
		//set up a communication socket

		//Initialize data stream to send data out
		String command;

		switch(e.getKeyCode()) {
			case KeyEvent.VK_UP:
				command = "PLAYER " + catNumber + " UP";
				System.out.println("Sending: " + command);
				out.println(command);
				out.flush();
			break;
			
			case KeyEvent.VK_DOWN:
				command = "PLAYER " + catNumber + " DOWN";
				System.out.println("Sending: " + command);
				out.println(command);
				out.flush();
			break;
			
			case KeyEvent.VK_LEFT:
				command = "PLAYER " + catNumber + " LEFT";
				System.out.println("Sending: " + command);
				out.println(command);
				out.flush();
			break;
			
			case KeyEvent.VK_RIGHT:
				command = "PLAYER " + catNumber + " RIGHT";
				System.out.println("Sending: " + command);
				out.println(command);
				out.flush();
			break;
			
			default:
				System.out.println("Invalid Input");
			break;
			
		}
		
		cat.setX(x);
		cat.setY(y);
		
		//updateGraphics();
	}
	
	private void updateGraphics() {
		//Cat.setLocation(cat.getX(), cat.getY());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void endGame(boolean won) {
		/*isOver = true;
		rowHandler.stopMovingCars();
		logRowHandler.stopMovingCars();
		
		if(won) {
			cat.setScore(cat.getScore()+25);
		}else {
			cat.setScore(cat.getScore()-50);
			cat.getCatLabel().setIcon(new ImageIcon(getClass().getResource(Properties.catDead)));
		}
		
		score.setText(Integer.toString(cat.getScore()));
		startButton.setText("Restart");*/
	}
	
	public RowHandler getRowHandler(int index) {
		if(index == 1) {
			return rowHandler;
		}else {
			return logRowHandler;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == startButton) {
				if(!moving) {
					startButton.setText("Stop");
					String command = "START";
					System.out.println("Sending: " + command);
					out.println(command);
					out.flush();
					moving = true;
				}else {
					startButton.setText("Start");
					String command = "STOP";
					System.out.println("Sending: " + command);
					out.println(command);
					out.flush();
					moving = false;
				}
		}
		
	}
	
	public static FroggerCore getInstance() {
		return game;
	}
		
}
