package warships.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import warships.ships.Cell;

public class Battlefield {
	private int mapSize;
	private int maxShipLength;
	private int maxShipCellsAmount;
	private int shipCellsAmount;
	private int cellsLeft;
	private Random r;
    private Cell[] battlefield;
	private String[] shipTypes = { null,
	                              "Patrol boat",	                              
								  "Attack submarine",
								  "Littoral combat ship",
								  "Destroyer",
								  "Ballistic missile submarine",
								  "Cruiser",
								  "Aircraft Carrier",
	                              "Amphibious assault ship"};

	public Battlefield(int mapSize, boolean isUserBattlefiled){
 		this.mapSize = mapSize < 3 ? 3 : mapSize > 26 ? 26 : mapSize;
		maxShipLength = this.mapSize * 1/2; // Max amount of not empty shipCells in heap
		maxShipCellsAmount = this.mapSize * this.mapSize * 1/4; // Max amount of not empry shipCells at battlefield
		cellsLeft = maxShipCellsAmount;
		battlefield = new Cell[this.mapSize*this.mapSize+1];
		r = new Random();
		
		generateBattlefield(isUserBattlefiled);
  
	}
	private void generateBattlefield(boolean isUserBattlefiled){
      	for(int indx = 1; indx <= mapSize*mapSize; indx++) {
			int x = 1;
			int y = 1;
			if( indx > mapSize ) {
			    y = indx%mapSize > 0 ? (indx/mapSize)+1 : (indx/mapSize);
				x = indx%mapSize > 0 ? indx%mapSize : mapSize;
			} else {
			    x = indx;
			}
			battlefield[indx] = new Cell(indx, y, x);
		}
		if(!isUserBattlefiled) {
		    int cellsCounter = 0;
            while(cellsCounter <= maxShipCellsAmount) {
			    int occupied = launchShip(cellsCounter);
                if(occupied > 0) cellsCounter += occupied;
			    else break;
			}
		    shipCellsAmount = cellsCounter;	
		}
		
	}
	public int launchShip(String sYStart, int xStart, String sYStop, int xStop) {
		int yStart = (int)sYStart.getBytes()[0]-64;
		int yStop = (int)sYStop.getBytes()[0]-64;
//		System.out.println("  Convert coords from "+sYStart+" "+xStart+" to "+yStart+" "+xStart);                                      // TEST
//		System.out.println("  and coords from "+sYStop+" "+xStop+" to "+yStop+" "+xStop);                                              // TEST
		return launchShip(yStart, xStart, yStop, xStop);		
	}
	public int launchShip(int yStart, int xStart, int yStop, int xStop){
		int shipBowX = xStart < xStop ? xStart : xStop;
		int shipBowY = yStart < yStop ? yStart : yStop; 
		int yDiff = yStop-yStart < 0 ? (yStop-yStart)*-1 : (yStop-yStart) == 0 ? 0 : yStop-yStart;
		int xDiff = xStop-xStart < 0 ? (xStop-xStart)*-1 : (xStop-xStart) == 0 ? 0 : xStop-xStart;;
		int shipLength = yDiff <= 0 ? xDiff+1 : yDiff+1;
		    shipLength = shipLength < maxShipLength ? shipLength : maxShipLength;
			shipLength = (cellsLeft - shipLength) < 0 ? cellsLeft : shipLength;
		ArrayList<Cell> shipBody = new ArrayList<> ();
		boolean isVertical = yDiff != 0 ? true : false;
        
//		System.out.println("  Ship length "+shipLength);                                                                                // TEST
		
		for(int i = 0; i < shipLength; i++) {
		    Cell shipCell = isVertical ? getCellByCoordinates(shipBowY+i, shipBowX) : getCellByCoordinates(shipBowY, shipBowX+i);
			if(!shipCell.isOccupied() & !shipCell.isReserved()) shipBody.add(shipCell);
		}
        if(shipBody.size() == shipLength) {
			for(Cell cell : shipBody) {
			    cell.Occupy();
				makeReservation(cell);
			}
			cellsLeft -= shipLength;
		    return shipLength;
		}
		
		
		return 0;
	}
	public int launchShip(int cellsCounter){
		ArrayList<Integer> freeCellsSequence = new ArrayList<> ();
		int shipLength = generateShipLength(cellsCounter);
		Cell shipBow;
		String shipName;
		
		for(int i = 1; i < battlefield.length; i++)
		    if(!battlefield[i].isOccupied() & !battlefield[i].isReserved())
				freeCellsSequence.add(battlefield[i].getUsn());
			
		if(freeCellsSequence.size() > 1) {
		    shipBow = battlefield[freeCellsSequence.get(r.nextInt(freeCellsSequence.size()-1)+1)];
			
		    Optional< Map.Entry<String, Integer> > wind = getRoseOfWind(shipBow).entrySet()
		                                                                        .stream()
                                                                                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                                                                                .findFirst();
																			
            shipLength = shipLength <= wind.get().getValue() ? shipLength : wind.get().getValue(); 
			shipName = shipTypes[shipLength < 9 ? shipLength : 8]+" U.S.S. "+(char)(r.nextInt(25)+65)+(r.nextInt(998)+1);

            for(int i = 0; i < shipLength; i++) {
			    int usn = shipBow.getUsn();
		        if(wind.get().getKey().equals("North")) {
				    battlefield[usn-i*mapSize].Occupy();
					battlefield[usn-i*mapSize].setName(shipName);
				    makeReservation(battlefield[usn-i*mapSize]);
			    }
			    if(wind.get().getKey().equals("South")) {
				    battlefield[usn+i*mapSize].Occupy();
					battlefield[usn+i*mapSize].setName(shipName);
					makeReservation(battlefield[usn+i*mapSize]);
			    }
			    if(wind.get().getKey().equals("East")) {
				    battlefield[usn+i].Occupy();
					battlefield[usn+i].setName(shipName);
				    makeReservation(battlefield[usn+i]);
			    }
			    if(wind.get().getKey().equals("West")) {
				    battlefield[usn-i].Occupy();
					battlefield[usn-i].setName(shipName);
					makeReservation(battlefield[usn-i]);
			    }
		    }
		
		}
		if(freeCellsSequence.size() == 1) {
		    battlefield[freeCellsSequence.get(0)].Occupy();
            shipLength = 1;
		}
        if(freeCellsSequence.size() == 0)
			shipLength = 0;

		return shipLength;
	}
	private void makeReservation(Cell cell) {
        if( getCellByCoordinates(cell.getY()-1,cell.getX()) != null )   getCellByCoordinates(cell.getY()-1,cell.getX()).Reserve();
		if( getCellByCoordinates(cell.getY()-1,cell.getX()+1) != null ) getCellByCoordinates(cell.getY()-1,cell.getX()+1).Reserve();
		if( getCellByCoordinates(cell.getY(),cell.getX()+1) != null )   getCellByCoordinates(cell.getY(),cell.getX()+1).Reserve();
		if( getCellByCoordinates(cell.getY()+1,cell.getX()+1) != null ) getCellByCoordinates(cell.getY()+1,cell.getX()+1).Reserve();
		if( getCellByCoordinates(cell.getY()+1,cell.getX()) != null )   getCellByCoordinates(cell.getY()+1,cell.getX()).Reserve();
		if( getCellByCoordinates(cell.getY()+1,cell.getX()-1) != null ) getCellByCoordinates(cell.getY()+1,cell.getX()-1).Reserve();
		if( getCellByCoordinates(cell.getY(),cell.getX()-1) != null )   getCellByCoordinates(cell.getY(),cell.getX()-1).Reserve();
		if( getCellByCoordinates(cell.getY()-1,cell.getX()-1) != null ) getCellByCoordinates(cell.getY()-1,cell.getX()-1).Reserve();
	}
	private HashMap<String, Integer> getRoseOfWind(Cell cell) {
		HashMap<String, Integer> roseOfWind = new HashMap<> ();
        int cellCount = 0;    
		
		for(int y = cell.getY(); y >= 1; y--)
            if(!getCellByCoordinates(y, cell.getX()).isReserved()) cellCount += 1;
		    else break;
		roseOfWind.put("North", cellCount);

        cellCount = 0;
		for(int y = cell.getY(); y <= mapSize; y++)
			if(!getCellByCoordinates(y, cell.getX()).isReserved()) cellCount +=1;
		    else break;
		roseOfWind.put("South", cellCount);

        cellCount = 0;
		for(int x = cell.getX(); x >= 1; x--)
			if(!getCellByCoordinates(cell.getY(), x).isReserved()) cellCount += 1;
		    else break;
		roseOfWind.put("West", cellCount);

        cellCount = 0;
		for(int x = cell.getX(); x <= mapSize; x++)
			if(!getCellByCoordinates(cell.getY(), x).isReserved()) cellCount += 1;
		    else break;
		roseOfWind.put("East", cellCount);
    
	    return roseOfWind;
	}
	private Integer generateShipLength(int cellsCounter) {
		int shipLength;
		int delta = maxShipCellsAmount-cellsCounter;
		if(delta > 0) return r.nextInt(maxShipLength <= delta ? maxShipLength : delta)+1;
	    else return 0;
	}
	public boolean launchTorpedo(String sY, int x) {
		byte[] bY = sY.getBytes();
		int y = (int)bY[0]-64;
		return launchTorpedo(x, y);
	}
	public boolean launchTorpedo(int x, int y) {
		Cell target = getCellByCoordinates(y, x);
		if(!target.isOccupied()) return false;
		else target.shipRuine();
		return true;
	}
	public int getMapSize(){
	    return mapSize;
	}
	public Cell getCellByCoordinates(int y, int x) {

		for(int i = 1; i < battlefield.length; i++)
			if(battlefield[i].getY() == y && battlefield[i].getX() == x)
				return battlefield[i];

		return null;
	}

	public int getMaxShipCellsAmount() {
	    return maxShipCellsAmount;
	}
	public int getMaxShipLength() {
	    return maxShipLength;
	}
	public Cell[] getBattlefield() {
		return battlefield;
	}
	public boolean isFinished() {
	    return --shipCellsAmount <= 0 ? true : false;
	}
	public int getCellsLeft() {
	    return cellsLeft > 0 ? cellsLeft : 0;
	}
}