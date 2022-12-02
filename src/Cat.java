import javax.swing.JLabel;

public class Cat extends Sprite{
//
	private Log log;
	private boolean isOnLog;
	private int score;
	private JLabel lScore;
	
	public Cat() {
		super(0,0, Properties.catHeight, Properties.catWidth, Properties.catImg);
		this.updateRectangleSize();
		this.updateRectanglePosition();
		this.isOnLog = false;
	}
	
	
	public void setLog(Log log) {
		this.log = log;
	}
	
	public Log getLog() {
		return this.log;
	}
	
	public void setScoreLabel(JLabel lScore) {
		this.lScore = lScore;
	}
	
	public void setOnLog(boolean bool) {
		this.isOnLog = bool;
	}
	
	public boolean getOnLog() {
		return this.isOnLog;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public void setScore(int temp) {
		this.score = temp;
		lScore.setText(Integer.toString(this.score));
	}
	
	@Override
	public void updateRectanglePosition() {
		this.r.x = this.x;
		this.r.y = this.y+15;
	}
	
	@Override
	public void updateRectangleSize() {
		this.r.width = this.width;
		this.r.height = this.height-15;
	}
	
}
