import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JLabel;

public class ConnectToDatabase {
			
	private static ConnectToDatabase instance = null;
	Connection conn = null;
	Statement stmt = null;
	Cat cat;
	String name;
	public ConnectToDatabase(Cat cat, String name) {
			try {
				//Load the SQL driver
				Class.forName("org.sqlite.JDBC");
				System.out.println("driver loaded");
				
				String dbURL = "jdbc:sqlite:products.db";
				conn = DriverManager.getConnection(dbURL);
			
				if(conn != null) {
					stmt = conn.createStatement();
					System.out.println("Connected to Database");
					
					String sql = "CREATE TABLE IF NOT EXISTS PLAYERS " +
							 "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
							 " NAME TEXT NOT NULL, " + 
							 " SCORE INT NOT NULL)";
					
					stmt.executeUpdate(sql);
					conn.close();
				}
				this.name = name;
				this.cat = cat;
				setPlayerValues(name);
				int score = getPlayerScore(name);
				cat.setScore(score);
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	
	
	public void setPlayerValues(String name) {
		try {
			//Load the SQL driver
			Class.forName("org.sqlite.JDBC");
			System.out.println("driver loaded");
			
			String dbURL = "jdbc:sqlite:products.db";
			conn = DriverManager.getConnection(dbURL);
		
			if(conn != null) {
				stmt = conn.createStatement();
				String sql = "INSERT INTO PLAYERS (NAME, SCORE) VALUES ( + '" + name + "', 0)";
				
				stmt.executeUpdate(sql);
				conn.close();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getScore(ResultSet rs) throws SQLException {
		 while(rs.next()) {
			int score = rs.getInt("SCORE");
			return score;
		 }
		return 0;
		 
	}
	
	public int getPlayerScore(String name) {
		int score = 0;
		try {
			//Load the SQL driver
			Class.forName("org.sqlite.JDBC");
			System.out.println("driver loaded");
			
			String dbURL = "jdbc:sqlite:products.db";
			conn = DriverManager.getConnection(dbURL);
		
			if(conn != null) {
				stmt = conn.createStatement();
				String sql = "SELECT * FROM PLAYERS WHERE NAME =  '" + name + "';"; 
				ResultSet rs = stmt.executeQuery(sql);
				score = getScore(rs);
				conn.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return score;
	}
	
	public void setCat(Cat cat) {
		this.cat = cat;
	}
	
	
	public void setPlayerScore(String name, int score) {
		try {
			//Load the SQL driver
			Class.forName("org.sqlite.JDBC");
			System.out.println("driver loaded");
			
			String dbURL = "jdbc:sqlite:products.db";
			conn = DriverManager.getConnection(dbURL);
		
			if(conn != null) {
				stmt = conn.createStatement();

				String sql = "UPDATE PLAYERS SET SCORE = ? WHERE NAME = ?"; 
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, score);
				pstmt.setString(2, name);
				pstmt.executeUpdate();
				conn.close();
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ConnectToDatabase getInstance(Cat cat, String name) {
		if(instance == null) {
			instance = new ConnectToDatabase(cat, name);
		}
		return instance;
	}
	
	public void closeConnection() {
		setPlayerScore(name,cat.getScore());
	}
	
}
