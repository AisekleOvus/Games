package warships.gameplay;

import java.util.Scanner;
import java.util.Random;

import warships.map.Battlefield;
import warships.visualisation.Visualisation;

public class Gameplay {
		public static void startGame() {
		int mapSize=3;
		int cellsAmount = 0;
		String str = "";
		Scanner sc = new Scanner(System.in);
		Battlefield computerBf;
		Battlefield userBf;
		Visualisation visioComputer;
		Visualisation visioUser;
		
		System.out.println("\n  WELLCOME TO WARSHIPS GAME !\n\n  Well, let's do the game set up\n  So, please, type size of warships battlefield or type \"quit\" for exit\n");
		System.out.print("  (default 3X3 max. 26) :> ");

		while(true){
			str = sc.nextLine();
			Scanner scanner = new Scanner(str);
			if("quit".equals(str)) System.exit(-1);
			if("".equals(str)) break;
			else if(scanner.hasNextInt()) {
			    mapSize = scanner.nextInt();
		        mapSize = mapSize < 3 ? 3 : mapSize > 26 ? 26 : mapSize;
				break;
			}else{
				System.out.print("\n  Please type size(digital) of the game battlefield\n  (default 3X3 max. 26) :> ");
			}
		}
		System.out.println("\n  O'k the map size is "+mapSize+" cells.\n  Generating battlefield...\n");

	    userBf = new Battlefield(mapSize, true);
		visioUser = new Visualisation(userBf);
	    computerBf = new Battlefield(mapSize, false);
		visioComputer = new Visualisation(computerBf);
		
		System.out.println("  O'k .. Go, place your fleet !");

	    while(true) {			
		    System.out.println("\n  Maximum ship length : "+userBf.getMaxShipLength());
		    System.out.println("  You have "+userBf.getCellsLeft()+" cells to build you fleet\n");
			visioUser.showDisposition("V","R","X","~");
	        System.out.print("\n  Please enter start and end coordinates of the vessel (ex. a 2 c 2):> ");
			Scanner scanner = new Scanner(sc.nextLine()).useDelimiter(" ");			
			
		    if(scanner.hasNextInt()) {
		        int yStart = scanner.nextInt();
		        if(!scanner.hasNextInt()) continue;
		        int xStart = scanner.nextInt();
		        if(!scanner.hasNextInt()) continue;
			    int yStop = scanner.nextInt();
			    if(!scanner.hasNextInt()) continue;
			    int xStop = scanner.nextInt();
                
				cellsAmount = userBf.launchShip(yStart, xStart, yStop, xStop);

			    if(cellsAmount == 0) 
				    System.out.print("\n  Coordinates are occupied,please make another choice  \n");
				
			    if(userBf.getCellsLeft() == 0)
				    break;
			}
		    if(scanner.hasNext()) {
		        String sYStart = scanner.next().toUpperCase();
			    if("QUIT".equals(sYStart)) System.exit(-1);
			    if(!scanner.hasNextInt()) continue;
		        int xStart = scanner.nextInt();
			    if(scanner.hasNextInt()) continue;
			    String sYStop = scanner.next().toUpperCase();
			    if(!scanner.hasNextInt()) continue;
			    int xStop = scanner.nextInt();

                cellsAmount = userBf.launchShip(sYStart, xStart, sYStop, xStop);
			    
				if(cellsAmount == 0) 
				    System.out.print("\n  Coordinates are occupied,please make another choice  \n");
				
			    if(userBf.getCellsLeft() == 0)
				    break;
			}

	    }
		System.out.println();
        visioUser.showDisposition("V","X","~");
		System.out.println("\n  O'k let's begin : \n");
		visioComputer.showBattlefield();
		
		while(true) {
		    System.out.print("\n  Please enter coordinates to launch torpedo or type \"quit\" for exit");
		    System.out.print("\n  coordinates should consist of letter A-Z and number 1-26, separated with space\n");
		    System.out.print("  coordinates also might be two numbers 1-26, separated with space :> ");
		    Scanner scanner = new Scanner(sc.nextLine()).useDelimiter(" ");
			if(scanner.hasNextInt()) {
			    int y = scanner.nextInt();
				if(!scanner.hasNextInt()) continue;
				int x = scanner.nextInt();
				if(computerBf.launchTorpedo(x, y)){
					System.out.println("\n  You get one !\n");
					if(computerBf.isFinished()) {
						visioComputer.showBattlefield();
					    System.out.println("\n  Game Over");
						System.exit(-1);
					}				
				}
				else System.out.println("\n  You missed !\n");
				visioComputer.showBattlefield();
				
				if(computersTurn(mapSize, userBf))
				    System.out.println("\n  Ha-ha, I'm Top Gun !\n");
				else
					System.out.println("\n  F..k, I Missed !\n");
				System.out.println();
				visioUser.showDisposition("V","X","~");
			}
			if(scanner.hasNext()) {
			    String sY = scanner.next().toUpperCase();
				if("QUIT".equals(sY)) System.exit(-1);
				if("IDDQD".equals(sY)) visioComputer.showDisposition();
				if(!scanner.hasNextInt()) continue;
			    int x = scanner.nextInt();
				if(computerBf.launchTorpedo(sY, x)){
					System.out.println("\n  You get one !\n");
					if(computerBf.isFinished()) {
						visioComputer.showBattlefield();
					    System.out.println("\n  Game Over");
						System.exit(-1);
					}
				}
				else System.out.println("\n  You missed !\n");
				visioComputer.showBattlefield();
				
				if(computersTurn(mapSize, userBf))
				    System.out.println("\n  Ha-ha, I'm Top Gun !\n");
				else
					System.out.println("\n  F..k, I Missed !\n");
				System.out.println();
				visioUser.showDisposition("V","X","~");
				
			}
		}
		
	}
	private static boolean computersTurn(int mapSize, Battlefield bf) {
		Random r = new Random();
		int x = r.nextInt(mapSize)+1;
		int y = r.nextInt(mapSize)+1;
		return bf.launchTorpedo(x, y);
	}
}