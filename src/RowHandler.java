import java.awt.Container;

import javax.swing.JLabel;


//TODO Make cars all move on one thread instead of each car having their own thread

public class RowHandler implements Runnable{

	//Row rowsArray[];
	private Boolean carsMoving;
	private int y;
	private Row rowArray[];
	private Thread thread;
	
	public RowHandler(int rows, int itemsPerRow, RowType rowType, int y,  Cat cat, JLabel lCat, Cat cat2, JLabel lCat2) {
		carsMoving = false;
		rowArray = new Row[rows];
		this.y = y;
		
		int xSpacing,direction;
		
		//Loop through size of preferred rows and create those rows with according values
		for(int i = 0; i <= rows-1; i++) {
			xSpacing = Properties.catWidth + (Properties.catWidth + Properties.catWidth/2);
			direction = 1;
			
			//For every second row, swap the direction
			if((i+1) % 2 == 0) {
				direction = -1;
			}
			
			Row row = new Row(rowType, itemsPerRow, y, xSpacing, direction, i + 1, cat, lCat,cat2,lCat2);
			
			//negate the y by the height to add spacing between these rows
			y -= row.getSprites()[0].getHeight() + Properties.catHeight + Properties.ROW_SPACING;
			row.setY(y);

			rowArray[i] = row;
		}
		
	}
	
	
	
	
	public void stepOnce() {
		for(Row row : rowArray) {
			row.moveRow();
		}
	}
	
	
	public void stopMovingCars() {
		this.carsMoving = false;
	}

	public Boolean getMoving() {
		return carsMoving;
	}
	
	public void applyRows(Container container) {
		for(Row row : rowArray) {
			row.addRow(container);
		}
	}


	@Override
	public void run() {
		this.carsMoving = true;
		
		while(this.carsMoving) {
			
			for(Row row : rowArray) {
				row.moveRow();

			}
			
			outer: 
			for(Row row : rowArray) {
				if(row.getSprites()[1] instanceof Log) {
					for(Sprite sprite : row.getSprites()) {
						Log log = (Log)sprite;
						if(log.getOnLog()) {
							log.getCat().setOnLog(true);
							break outer;
						}else {
							log.getCat().setOnLog(false);
						}
					}
				}
			}
			
			Cat cat = rowArray[1].getSprites()[1].getCat();
			if(cat.getY() <= 400 && !cat.getOnLog()) {
				if(cat.getY() <=3 ) {
					FroggerCore.getInstance().endGame(true);
				}else {
					FroggerCore.getInstance().endGame(false);
				}
				
			}
			
			//Pause so movement is smooth
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	}
