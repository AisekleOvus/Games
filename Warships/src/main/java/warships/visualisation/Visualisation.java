package warships.visualisation;

import warships.ships.Cell;
import warships.map.Battlefield;

public class Visualisation {
	private Battlefield bf;
	private Cell[] disposition;
	private Integer mapSize;
	
	public Visualisation(Battlefield bf){
		this.bf = bf;
		this.mapSize = bf.getMapSize();
		disposition = bf.getBattlefield();
	}
	public void showDisposition(String ... flags) { // "V" - vessel, "R" - reserved, "X" - ruined, "~" - blue sea
        printBattlefield(flags);
	}
	public void showBattlefield() {
	    printBattlefield();
	}
	private void printBattlefield(String ... args) { // String signs of Vessel, Reserved, Sea
		int rows = 66;
		
		System.out.print("    ");
		for(int i = 1; i <= mapSize; i++)
			System.out.print(i < 10 ? " "+i+" " : i+" ");
		
        System.out.print("\n  A ");		
	    
		for(int i = 1; i < disposition.length; i++) {
		    Cell cell = disposition[i];                                                                                                           
		    int x = cell.getX();  
            int usn = cell.getUsn();
            String ship = "";
			
			if(args.length == 4)			
		        ship = cell.isOccupied() ? " "+args[0]+" " : cell.isReserved() ? " "+args[1]+" " : cell.isRuined() ? " "+args[2]+" " : " "+args[3]+" ";  
            if(args.length == 3)
				ship = cell.isOccupied() ? " "+args[0]+" " : cell.isRuined() ? " "+args[1]+" " : " "+args[2]+" ";  
			if(args.length == 0)
			    ship = cell.isRuined() ? " X " : " ~ ";	
		    System.out.print(x != bf.getMapSize() ? ship : usn != (disposition.length-1) ? ship+"\n  "+(char)(rows++)+" " : ship);                                                                                    
	    }
		System.out.println();
	}
}