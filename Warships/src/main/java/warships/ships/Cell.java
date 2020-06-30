package warships.ships;

/**
* 
*/

public class Cell {
	private boolean occupied;   // is Sea or Ship
	private boolean reserved; // It's buffer area between vessels 
	private boolean ruined;
	private final Integer y;    // Y coordinate
	private final Integer x;    // X coordinate
	private final Integer usn;  // Unique Serial Number
	private String shipName;    // How you name your boat so it will go..
	
	public Cell(Integer usn, Integer y, Integer x) {
        this.y = y;
		this.x = x;
		this.usn = usn;
		occupied = false;
		ruined = false;
		shipName = "~";
	}
	
	public Integer getX() {
	    return x;
	} 	
	public Integer getY() {
	    return y;
	}
	public void shipRuine() {
		occupied = false;
	    ruined = true;
		shipName = "X";
	}
	public void blueSea() {
	    occupied = false;
		ruined = false;
	    shipName = "~";
	}
	public boolean isOccupied() {
	   return occupied;
	}
	public boolean isRuined() {
	   return ruined;
	}
	public void Occupy() {
	   occupied = true;
	}	
	public boolean isReserved() {
	   return reserved;
	}
	public void Reserve() {
	   reserved = true;
	}
	public void setName(String name) {
	    shipName = name;
	}
	public String getName() {
	    return shipName;
	}
	public Integer getUsn() {
	    return usn;
	}
}