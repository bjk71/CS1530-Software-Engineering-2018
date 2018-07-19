import java.io.*;
import java.util.*;
import java.text.*;

public class Logging implements Serializable {    
    private final String outputFileName;

    public Logging(){
        //ISO 8601 Date Format
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'_T'HHmmss");
        Date date = new Date();

        this.outputFileName = "gamelog" + File.separator + "gamelog_" + dateFormat.format(date) + ".txt";
    }

    /*
	 * Opens the external file and writes the intro.
	 * File that is opened is generated in constructor using ISO 8601 format.
	 * @param	UserName		The input name from the player.
	 * @param	aiPlayerNames	The selected AI names.
	*/
	public void writeStartFile(String userName, String[] aiPlayerNames){
		//Write the intro to the External File
        File outputFile = new File(outputFileName);
		
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			outputFile.getParentFile().mkdirs();
            outputFile.createNewFile();
			String firstLine = "Game Started - ";
			String secondLine = "Player name is: ";
			String thirdLine = "AI player names: ";
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();

			fw = new FileWriter(outputFile);
			bw = new BufferedWriter(fw);
			
			//writes first line - gives date and time
			bw.write(firstLine);
			bw.write(dateFormat.format(date));
			bw.newLine();
			
			//second line - gives player name
			bw.write(secondLine);
			bw.write(userName);
			bw.newLine();
			
			//third line - gives ai players names
			bw.write(thirdLine);
			bw.write(Arrays.toString(aiPlayerNames));
			bw.newLine();
			
		}  catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
	}
	
	/*
	 * Opens the external file and writes Hand #
	*/
	public void writeHandFile(int turnNum){
		try{
			File outputFile = new File(outputFileName);
			
			//Here true is to append the content to file
			FileWriter fw = new FileWriter(outputFile,true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			//Hand #
			bw.write("Hand: " + turnNum);
			bw.newLine();
			
			//Closing BufferedWriter Stream
			bw.close();

		}  catch (IOException e) {

			e.printStackTrace();

		}
	}
	
	/*
	 * Opens the external file and writes the Cards Dealt and each players cash
	 * @param	players			The info for the current player.
	*/
	public void writeCardsFile(Player players){
		try{
			File outputFile = new File(outputFileName);
			
			//Here true is to append the content to file
			FileWriter fw = new FileWriter(outputFile,true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			//System.out.println(players);
			
			String playersToString = players.toString();
			
			bw.write(playersToString);
			bw.newLine();
			
			
			//Closing BufferedWriter Stream
			bw.close();

		}  catch (IOException e) {

			e.printStackTrace();

		}		
	}
	
	/*
	 * Opens the external file and writes the Cards Dealt in flop.
	 * @param	cards			cards in the deck
	 * @param	when		 	flop, turn, or river
	 * If when = 0, writing flop
	 * If when = 1, writing turn
	 * If when = 2, writing river
	*/
	public void writeDeckCardsFile(Card cards, int when){
		try{
			File outputFile = new File(outputFileName);
			
			//Here true is to append the content to file
			FileWriter fw = new FileWriter(outputFile,true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			
			if(when == 0){
				bw.write("A flop card is: " + cards.getName());
				bw.newLine();
			}
			else if(when == 1){
				bw.write("The turn card is : " + cards.getName());
				bw.newLine();
			}
			else if(when == 2){
				bw.write("The river card is : " + cards.getName());
				bw.newLine();
			}
			else{
				bw.write("I messed up.");
				bw.newLine();
			}
			
			//Closing BufferedWriter Stream
			bw.close();

		}  catch (IOException e) {

			e.printStackTrace();

		}		
	}
	
	/*
	 * Opens the external file and writes the Player action.
	 * @param	userAction		The action taken by the user
	 * @param	playerNum		the number of the player who made the move
	 * If userAction is > 0, userAction = money bet by user, AI folds
	 * If userAction is = 0, userAction = user checked, AI checks
	 * If userAction is = -1, userAction = user folded, AI folds
	*/
	public void writeActionFile(Action userAction, Player player){
		
		try{
			File outputFile = new File(outputFileName);
			
			//Here true is to append the content to file
			FileWriter fw = new FileWriter(outputFile,true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write(player.getName() + " " + userAction.toString());
			
			bw.newLine();
			
			//Closing BufferedWriter Stream
			bw.close();

		}  catch (IOException e) {

			e.printStackTrace();

		}	
	}
	
	/*
	 * Opens the external file and writes the Ending of Hand.
	 * @param	endResult		The ending result for the game
	*/
	public void writeEndFile(String endResult){
		
		try{
			File outputFile = new File(outputFileName);
			
			//Here true is to append the content to file
			FileWriter fw = new FileWriter(outputFile,true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write(endResult);
			bw.newLine();
			
			
			//Closing BufferedWriter Stream
			bw.close();

		}  catch (IOException e) {

			e.printStackTrace();

		}
    }
    
}