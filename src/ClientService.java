import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

//processing routine on server (B)
public class ClientService implements Runnable {

	private Socket s;
	private Scanner in;

	public ClientService (Socket aSocket) {
		this.s = aSocket;
	}
	public void run() {
		
		try {
			in = new Scanner(s.getInputStream());
			processRequest( );
		} catch (IOException e){
			e.printStackTrace();
		} finally {
			try {
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	//processing the requests
	public void processRequest () throws IOException {
		//if next request is empty then return
		while(true) {
			if(!in.hasNext( )){
				return;
			}
			String command = in.next();
			if (command.equals("Quit")) {
				return;
			} else {
				executeCommand(command);
			}
		}
	}
	
	public void executeCommand(String command) throws IOException{
		FroggerCore main = FroggerCore.getInstance();
		if(command.equals("SET")) {
			int pNum = in.nextInt();
			main.setCatNumber(pNum);
			
		}
		
		if(command.equals("MOVE")) {
			main.getRowHandler(1).stepOnce();
			main.getRowHandler(2).stepOnce();
		}
		
		if(command.equals("UPDATE")) {
			int score = in.nextInt();
			main.setScoreLabel(score);
		}
		
		
		if ( command.equals("PLAYER")) {
			int playerNo = in.nextInt();
			//String playerAction = in.next();
			int playerX = in.nextInt();
			int playerY = in.nextInt();
			main.updatePlayer(playerX, playerY,playerNo);
			//System.out.println("Player "+playerNo+" "+playerAction + " "+playerX+", "+playerY);
		}
	}
}
