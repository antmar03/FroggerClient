import javax.swing.JLabel;

public class Log extends Sprite{
	
	private JLabel lLog, lCat, lCat2;
	private Cat cat,cat2;
	
	private boolean onLog;
	public Log(Cat cat, JLabel lCat, Cat cat2, JLabel lCat2) {
		super(0,0, Properties.logHeight, Properties.logWidth, Properties.logImg);	
		this.cat = cat;
		this.cat2 = cat2;
		this.lCat = lCat;
		this.lCat2 = lCat2;
		this.onLog = false;
		updateRectangleSize();
		updateRectanglePosition();
	}
	
	
	public void setLogLabel(JLabel log) {
		this.lLog = log;
	}
	
	private void checkCollision(int speedMultiplier, int direction) {
		
		if(this.getRectangle().intersects(cat.getRectangle())) {
			cat.setX(cat.getX() + ((Properties.CAR_STEP * speedMultiplier)/2) * direction);
			System.out.println(cat.getX() + " " + cat.getY());
			lCat.setLocation(cat.getX(), cat.getY());
			onLog = true;
		}else if(this.getRectangle().intersects(cat2.getRectangle())) {
			cat2.setX(cat2.getX() + ((Properties.CAR_STEP * speedMultiplier)/2) * direction);
			System.out.println(cat2.getX() + " " + cat2.getY());
			lCat2.setLocation(cat2.getX(), cat2.getY());
			onLog = true;
		} else {
			onLog = false;
		}
		
		
	}
	
	public void move(int direction, int speedMultiplier) {
		int currentX = this.getX();
		
		//Increase the value
		currentX += ((Properties.CAR_STEP * speedMultiplier)/2) * direction;
		
		
		//Check boundaries
		if(direction > 0) {
			if(currentX >= Properties.SCREEN_WIDTH) {
				currentX = -this.getWidth();
			}
		}else{
			if(currentX + getWidth() <= 0) {
				currentX = Properties.SCREEN_WIDTH;
			}	
		}
		
		checkCollision(speedMultiplier, direction);
		
		//Update the position
		this.setX(currentX);
		
		//Set Label Position
		this.lLog.setLocation(this.getX(), this.getY());
	}
	
	
	@Override
	public void updateRectanglePosition() {
		this.r.x = this.x;
		this.r.y = this.y-10;
	}
	
	@Override
	public void updateRectangleSize() {
		this.r.width = this.width;
		this.r.height = this.height-10;
	}
	
	public boolean getOnLog() {
		return this.onLog;
	}
	
}
