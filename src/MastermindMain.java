import java.io.*;
import java.util.*;

public class MastermindMain {
	
	private static int numGuesses = 0;
	private static boolean testingAll = true; //gleda koji kod da pokrene, ako hocu da cikliram kroz sve kombinacije, da nadjem max/avg

	public static void main(String[] args) throws IOException {
		
		ArrayList<int[]> active = new ArrayList<int[]>(); //aktivna lista potencijalnih pogodaka (pocinje sa 6^4 elemenata)
		int[] guess = new int[4]; //ovde cuvam aktivan guess programa/AI

		//devTools
		int counter = 0;
		int average = 0;
		ArrayList<int[]> active2 = new ArrayList<int[]>(); //aktivna lista potencijalnih pogodaka (pocinje sa 6^4 elemenata)
		
		if(testingAll) {
			for(int i1 = 0;i1<6;i1++) {
				for(int i2 = 0;i2 < 6;i2++) {
					for(int i3 = 0;i3<6;i3++) {
						for(int i4 = 0;i4 < 6;i4++) {
							int tempList[] = {i1,i2,i3,i4};
							active2.add(tempList);
						}
					}
				}
			}
		}
		
		System.out.print("Welcome to skocko program.\nTo play this game, you must make up in your mind a list of 4, using the elements:\n\n"
				+ "skocko\n" //bice id=0
				+ "zvezda\n" //id=1
				+ "karo\n" //id=2
				+ "herc\n" //id=3
				+ "pik\n" //id=4
				+ "tref\n" //id=5
				+ "\n, using lowercase characters. I will try to guess your list in the shortest possible time using greedy search.\n"
				+ "My guess will be printed as a list of 4 strings, separated with a single space, all in a single line. Example:"
				+ "\n\nskocko skocko karo karo\n\nTo this, you must answer first the number of elements I guessed correctly with my last guess"
				+ ", including the position (as a number between 0-4), followed by a space, \nfollowed by the number of elements guessed, "
				+ "that are not in the correct position (as a number between 0-4). For example:\n\n4 0\n\nShould you input 4 0, the game will end.\n"
				+ "I will not be checking the validity of your input, and trust you will play the game fair and always input a logical answer. :)\n\n"
				+ "Once you made up your mind, press \"Enter\" key to begin the game:\n\n");
		System.in.read();
		
		//zapocnimo partiju

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
		reader.readLine(); //mora i ovde da stoji, neki bug
		while(numGuesses < 20 && counter < 1296) {
			//proci kroz sve active elemente, pronaci najmanju vrednost maxPartition
			if(numGuesses == 0) {
				guess[0] = 2;
				guess[1] = 4;
				guess[2] = 4;
				guess[3] = 3; 
				//nastimuj format prvog guess-a, da bi dobio za svaku kombinaciju max 6 guessova (kvari average na 4.475)
				//bez stimovanja, on sam uzme format: 1122, dobije average 4.361, ali 5 kombinacija resi tek u 7 tries.
				//optimal strategy source:
				//https://supermastermind.github.io/playonline/optimal_strategy.html#:~:text=For%20Master%20Mind%20games%20
				
				//pocetna inicijalizacija skupa resenja
				active.clear(); //ocistim, za novu partiju
				for(int i1 = 0;i1<6;i1++) {
					for(int i2 = 0;i2 < 6;i2++) {
						for(int i3 = 0;i3<6;i3++) {
							for(int i4 = 0;i4 < 6;i4++) {
								int tempList[] = {i1,i2,i3,i4};
								active.add(tempList);
							}
						}
					}
				}
				if(testingAll) { //devTools
					//System.out.print("\nArray element no. "+counter + " : " + intToGuess(active2.get(counter)[0]) + intToGuess(active2.get(counter)[1]) +
					//		intToGuess(active2.get(counter)[2]) +intToGuess(active2.get(counter)[3]));
				}
			}
			else{guess = doGuess(active,numGuesses);}
			
			numGuesses++;
			if(!testingAll) {
				System.out.print("\nMy " + numGuesses + ". guess is: " + 
				intToGuess(guess[0]) + " " +
				intToGuess(guess[1]) + " " +
				intToGuess(guess[2]) + " " +
				intToGuess(guess[3]));
			}
			
			int crveni,zuti;
			if(testingAll) {
				int[] tempArrr = compareTwoGuesses(active2.get(counter), guess);
				crveni = tempArrr[0];
				zuti = tempArrr[1];
				//System.out.print("\n Crveni: " + crveni + ", zuti: " + zuti); //devTools
			}else {
				System.out.print("\nYour input:\n");
				String s = reader.readLine();
				
				//parse input
				crveni = Integer.parseInt(s.substring(0, 1));
				zuti = Integer.parseInt(s.substring(2, 3));
			}
			
			if(crveni == 4 && zuti == 0) {
				if(!testingAll) {
					System.out.print("Guessed correctly in " + numGuesses + " attempts. Press enter to play again.");
				}else {
					if(numGuesses > 6) {
						System.out.print("Guessed combination " + intToGuess(guess[0]) + intToGuess(guess[1]) +
								intToGuess(guess[2]) +intToGuess(guess[3]) +" in " + numGuesses + " tries.\n");
						//reader.readLine();
					}
				}
				average += numGuesses;
				numGuesses = 0;
				counter++;
				if(!testingAll) reader.readLine();
				//System.exit(0);
				continue;
			}
			
			//setuj novu active listu (remove all elements that don't match the crveni-zuti combination)
			for(int i = active.size()-1;i>=0;i--) { //idem od nazad da ne bih menjao duzinu liste, tj. proveru
				int[] guessJ = active.get(i);
				int[] tempArr = compareTwoGuesses(guess,guessJ);
				if(tempArr[0] != crveni || tempArr[1] != zuti) {
					/*if(guessJ[0] == 0 && guessJ[1] == 0 && guessJ[2] == 0 && guessJ[3] == 1) {
						System.out.print("Obrisao sam ovo jer mislim da je crveni " + tempArr[0] + ", a zuti " + tempArr[1]);
					}*/
					active.remove(i);
				}
			}
			//ovde su izbaceni svi elementi koji trebaju
		}
		
		System.out.println("Average solved steps: " + (double)average/(double)(counter-1));
	}
	
	public static int[] doGuess(ArrayList<int[]> a1, int numGuesses) {
		
		int[] guess = new int[4]; //ovde cuvam aktivan guess programa/AI
		int maxPartition = 2147483647; //za aktivan guess, cuvam najvecu velicinu svih skupova resenja, po manjoj vrednosti menjam aktivan guess
		
		for(int i = 0;i<a1.size();i++) {
			int guessI[] = a1.get(i); //izvucem guess za koji trazim particije

			int numPartitions[] = new int[21]; //setujem sve particije na 0
			
			
			for(int j = 0;j<a1.size();j++) {
				int guessJ[] = a1.get(j); //izvucem guess koji comparujem
				
				int[] tempArr = compareTwoGuesses(guessI,guessJ);
				//nakon ove tacke, setovani su i CRVENI i ZUTI.
				numPartitions[tempArr[0]+5*tempArr[1]]++; //konacno povecati odgovarajucu particiju, parser je na 5* da se ne bi poklopili nizovi 4*crveni i 4*zuti
			}
			//nakon ove tacke, prosao sam kroz ceo niz
			
			Arrays.sort(numPartitions); //da bi element na 0-poziciji bio max, koji ja i trazim
			if(numPartitions[numPartitions.length-1] < maxPartition) {
				guess = guessI; //setuj guess niz
				maxPartition = numPartitions[numPartitions.length-1];
				
			}
		}
		//prosao sam kroz ceo niz, kvadartno
		if(!testingAll) System.out.print("\nnasao sam najvecu particiju velicine: " + maxPartition + "\n");
		return guess;
	}
	
	public static String intToGuess(int i) {
		if(i==0) return "skocko";
		else if (i==1) return "zvezda";
		else if (i==2) return "karo";
		else if (i==3) return "herc";
		else if (i==4) return "pik";
		return "tref"; //za i==5
	}
	
	public static int[] compareTwoGuesses(int[] guessI, int[] guessJ) {
		boolean[] usedI = new boolean[4];
		boolean[] usedJ = new boolean[4];
		
		int crveni = 0; //jer crvena u skocku oznacava - da je pogodak na dobrom mestu, analogno za zutu \n
		int zuti = 0; // kao u kvizu Slagalica (mislim da bi neko, ko gleda ovaj kod, a poreklom je nas, razumeo znacenje naziva)
		
		for(int k1 = 0;k1 < 4;k1++) {
			if(guessI[k1] == guessJ[k1]) { // ako ga nadje na bas toj lokaciji, pojacaj crveni
				crveni++;
				usedI[k1] = true;
				usedJ[k1] = true;
				//if(guessJ[0] == 0 && guessJ[1] == 0 && guessJ[2] == 0 && guessJ[3] == 1)
				//System.out.print("Used the element at "+ k1+" properly\n");
			}
		}
		
		for(int i = 0;i<4;i++) {
			if(usedI[i]) continue;
			for(int j = 0;j<4;j++) {
				if(!usedJ[j] && guessI[i] == guessJ[j]) {
					usedJ[j] = true;
					zuti++;
					//if(guessJ[0] == 0 && guessJ[1] == 0 && guessJ[2] == 0 && guessJ[3] == 1)
					//System.out.print("Used the element at "+ i+","+j+" improperly\n");
					break;
				}
			}
		}
		
		int[] sol = {crveni,zuti};
		
		return sol;
	}

	public static int[] tablicaPripadnosti(int[] guessI) { //old code, unused
		int[] pripadnostI = new int[6]; //tablica pripadnosti
		for(int k = 0;k < 4;k++) { //podesiti tablicu
			pripadnostI[guessI[k]]++;
		}
		return pripadnostI;
	}
}
