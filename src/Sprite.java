import java.awt.Rectangle;

import javax.swing.JLabel;

public class Sprite {

	protected int x,y;
	protected String img;

	protected int height,width;
	protected Rectangle r;
	private Cat cat,cat2;
	private JLabel lCat;
	
	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
		this.updateRectanglePosition();
	}
	
	public void setCat2(Cat cat) {
		this.cat2 = cat;
	}
	
	public Cat getCat2() {
		return this.cat2;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;	
		this.updateRectanglePosition();
	}


	public String getImg() {
		return img;
	}


	public void setImg(String img) {
		this.img = img;
	}


	public int getHeight() {
		return height;
	}

	public void setCatLabel(JLabel label) {
		this.lCat = label;
	}
	
	public void setCat(Cat temp) {
		this.cat = temp;
	}
	
	public JLabel getCatLabel() {
		return this.lCat;
	}
	
	public Cat getCat() {
		return this.cat;
	}
	public void setHeight(int height) {
		this.height = height;
		this.updateRectangleSize();
	}


	public int getWidth() {
		return width;
	}


	public void setWidth(int width) {
		this.width = width;
		this.updateRectangleSize();
	}

	public Rectangle getRectangle() {
		return this.r;
	}
	
	public Sprite() {
		super();
		this.x = -1;
		this.y = -1;
		this.height = -1;
		this.width = -1;
		this.img = "";
		this.r = new Rectangle(0,0,0,0);
	}
	
	public Sprite(int x, int y, int height, int width, String img) {
		super();
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.img = img;
		this.r = new Rectangle(x,y,width,height);
	}
	
	public void updateRectanglePosition() {
		this.r.x = this.x;
		this.r.y = this.y;
	}
	
	public void updateRectangleSize() {
		this.r.width = this.width;
		this.r.height = this.height;
	}
	
}
