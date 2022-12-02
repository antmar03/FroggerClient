import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Car extends Sprite{

	private Boolean moving;
	private JLabel lCar;
	
	public Car() {
		super(0,0, Properties.carHeight, Properties.carWidth, Properties.carImg);	
		this.moving = false;
	}
	
	public void setMoving(Boolean temp) {
		this.moving = temp;
	}
	
	public Boolean getMoving() {
		return moving;
	}
	
	public void startMoving() {
	}
	
	public void setCarLabel(JLabel label) {
		this.lCar = label;
	}
	
	
	public void moveCar(int direction, int speedMultiplier) {
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

		
		//Update the position
		this.setX(currentX);
		
		//Set Label Position
		this.lCar.setLocation(this.getX(), this.getY());
		this.detectCollision();
	}
	
	private void detectCollision() {
		if(this.getRectangle().intersects(this.getCat().getRectangle())) {
			FroggerCore.getInstance().endGame(false);
		}
		
		if(this.getRectangle().intersects(this.getCat2().getRectangle())) {
			FroggerCore.getInstance().endGame(false);
		}
		
	}
	


	
}
