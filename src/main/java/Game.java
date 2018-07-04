import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import java.awt.event.*;
import java.text.*;

public class Game extends JPanel {
    private final Color POKER_GREEN = new Color(71, 113, 72);
    private final Color WHITE       = new Color(255, 255, 255);

    private Player[] players    = null;
    private Deck     deck       = null;
    private Card[]   tableCards = null;
    private Card     cardBack   = null;
    private int      pot        = 0;
    private JPanel   _tablePanel = null;
	private int		 turnNum	= 1;
    

    public Game() {
        initalizeStartGrid();
        setVisible(true);
    }
    

    public Game(File saveFile) {
      // initialize game settings from save file
    }
    
    
    private void initalizeStartGrid(){    
        JPanel             _startTitle        = new JPanel();
        JPanel             _playerName        = new JPanel();
        JPanel             _numOpponents      = new JPanel();
        JPanel             _startButton       = new JPanel();
        JPanel             _playerNamePanel   = new JPanel();
        JPanel             _numOpponentsPanel = new JPanel();

        JLabel             _titleLabel        = new JLabel();        
        JButton            _startGame         = new JButton();
        JLabel             _playerNameLabel   = new JLabel();
        JTextField         _playerNameInput   = new JTextField();
        JLabel             _numOppLabel       = new JLabel();
        JComboBox<Integer> _numOpp            = new JComboBox<>();

        _playerNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        _numOpponentsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        setLayout(new GridLayout(4,1));
        
        _playerName.setLayout(new GridLayout(1, 2, 50, 100));
        _numOpponents.setLayout(new GridLayout(1, 2, 50, 100));
        
        _startTitle.setBackground(POKER_GREEN);
        _playerName.setBackground(POKER_GREEN);
        _numOpponents.setBackground(POKER_GREEN);
        _startButton.setBackground(POKER_GREEN);
        _playerNamePanel.setBackground(POKER_GREEN);
        _numOpponentsPanel.setBackground(POKER_GREEN);
        
        _titleLabel.setForeground(WHITE);
        _titleLabel.setFont(new Font("Courier", Font.PLAIN, 60));
        _titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        _titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        _titleLabel.setText("Welcome to Pocket Rockets Poker!");
        
        _playerNameLabel.setForeground(WHITE);
        _playerNameLabel.setFont(new Font("Courier", Font.PLAIN, 40));
        _playerNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        _playerNameLabel.setVerticalAlignment(SwingConstants.TOP);
        _playerNameLabel.setText("Name:");
        
        _playerNameInput.setPreferredSize(new Dimension( 100, 50 ));
        _playerNameLabel.setFont(new Font("Courier", Font.PLAIN, 40));
        _playerNameInput.setHorizontalAlignment(SwingConstants.CENTER);
        _playerNamePanel.add(_playerNameInput);

        _numOppLabel.setForeground(WHITE);
        _numOppLabel.setFont(new Font("Courier", Font.PLAIN, 40));
        _numOppLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        _numOppLabel.setVerticalAlignment(SwingConstants.TOP);
        _numOppLabel.setText("Number of Opponents:");
        
        _numOpp.setModel(new DefaultComboBoxModel<>(new Integer[] { 1, 2, 3, 4, 5, 6, 7 }));        
        _numOpp.setPreferredSize(new Dimension( 100, 50 ));
        _numOpponentsPanel.add(_numOpp);
        
        _startGame.setText("Start Game");
        _startGame.setFont(new Font("Courier", Font.PLAIN, 30));
        _startGame.setPreferredSize(new Dimension( 300, 100 ));
        _startGame.setVerticalAlignment(SwingConstants.CENTER);
        _startGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: REGEX user input
                String name         = _playerNameInput.getText();
                int    numOpponents = (int) _numOpp.getSelectedItem();

                removeAll();
                createNewGame (name, numOpponents);
            }
        });
        
        _startTitle.add(_titleLabel);
        _playerName.add(_playerNameLabel);
        _playerName.add(_playerNamePanel);
        _numOpponents.add(_numOppLabel);
        _numOpponents.add(_numOpponentsPanel);
        _startButton.add(_startGame);
        
        add(_startTitle);
        add(_playerName);
        add(_numOpponents);
        add(_startButton);       
    }
    
    /**
     * Create new game objects and and initialize players.
     * @param userName Player name.
     * @param numOfAI  Number of AI players to initialize.
     */
    private void createNewGame(String userName, int numOfAI) {
        JPanel     _top            = new JPanel();
        JPanel     _bot            = new JPanel();
        JPanel[]   _aiPlayers      = new JPanel[numOfAI];
        JPanel[]   _tableAndUser   = new JPanel[2];
        JPanel[][] _aiPlayersTB    = new JPanel[numOfAI][2];
        JPanel[][] _tableAndUserTB = new JPanel[2][2];

        JLabel     _tableLabel     = new JLabel("Pot: ");
        JLabel     _pot            = new JLabel("");

        GameUtils  utils           = new GameUtils();
        String[]   aiPlayerNames   = utils.getAINames(numOfAI, userName);
		
		writeStartFile(userName, aiPlayerNames);
        
        try {
            Image back = ImageIO.read(this.getClass().getResource("/back.png"));
            cardBack = new Card("Back", back);
        } catch (IOException ioex) {
            System.exit(1);
        }

        deck       = new Deck();
        tableCards = new Card[5];
        players    = new Player[numOfAI + 1];
        
        setLayout(new GridLayout(3, 1));
        _top.setLayout(new GridLayout(1, numOfAI, 50, 100));
        _bot.setLayout(new GridLayout(1, 2, 50, 100));
        
        setBackground(POKER_GREEN);
        _top.setBackground(POKER_GREEN);
        _bot.setBackground(POKER_GREEN);
        
        for(int i = 0; i < numOfAI; i++) {
            _aiPlayers[i] = new JPanel();
            _top.add(_aiPlayers[i]);
            _aiPlayers[i].setBackground(POKER_GREEN);
            _aiPlayers[i].setLayout(new GridLayout(2, 1, 50, 50));
            
            _aiPlayersTB[i][0] = new JPanel();
            _aiPlayersTB[i][1] = new JPanel();
            _aiPlayers[i].add(_aiPlayersTB[i][0]);
            _aiPlayers[i].add(_aiPlayersTB[i][1]);
            _aiPlayersTB[i][0].setBackground(POKER_GREEN);
            _aiPlayersTB[i][1].setBackground(POKER_GREEN);
        }
        
        for(int i = 0; i < 2; i++) {
            _tableAndUser[i] = new JPanel();
            _bot.add(_tableAndUser[i]);
            _tableAndUser[i].setBackground(POKER_GREEN);
            _tableAndUser[i].setLayout(new GridLayout(2, 1, 50, 50));
            
            _tableAndUserTB[i][0] = new JPanel();
            _tableAndUserTB[i][1] = new JPanel();
            _tableAndUser[i].add(_tableAndUserTB[i][0]);
            _tableAndUser[i].add(_tableAndUserTB[i][1]);
            _tableAndUserTB[i][0].setBackground(POKER_GREEN);
            _tableAndUserTB[i][1].setBackground(POKER_GREEN);
        }

        // Make Labels and distribute starting cards
        _tableLabel.setForeground(WHITE);
        _tableLabel.setFont(new Font("Courier", Font.PLAIN, 28));
        _tableAndUserTB[0][0].add(_tableLabel, BorderLayout.NORTH);
        
        // Display the pot value
        _pot.setForeground(WHITE);
        _pot.setText("$" + String.valueOf(pot));
        _pot.setFont(new Font("Courier", Font.PLAIN, 28));
        _tableAndUserTB[0][0].add(_pot, BorderLayout.NORTH);
        
        for(int i = 0; i < 5; i++) {
            tableCards[i] = deck.draw();
            // displayCard(_tableAndUserTB[0][1], tableCards[i]);
        }
        // Save location of table cards
        _tablePanel = _tableAndUserTB[0][1];
		
		//writeCardsFile(deck);
        
        //For the players section
        for(int i=0; i<players.length; i++){
            Card[] playerHand   = new Card[2];
            JLabel _playerLabel = null;
            JLabel _playerCash  = null;
            int    playerCash   = 0;

            playerHand[0] = deck.draw();
            playerHand[1] = deck.draw();

            if(i == 0) { // Initialize human player
                _playerLabel = new JLabel(userName + ": ");
                _playerCash  = new JLabel();
                players[i]   = new Player(userName, playerHand, 1000, _playerCash, true);
                playerCash   = players[i].getCash();
				
				writeCardsFile(players[i]);

                _playerLabel.setForeground(WHITE);
                _playerLabel.setFont(new Font("Courier", Font.PLAIN, 28));
                _tableAndUserTB[1][0].add(_playerLabel, BorderLayout.NORTH);
                                
                // Display user's cash
                _playerCash.setForeground(WHITE);
                _playerCash.setText("$" + String.valueOf(players[i].getCash()));
                _playerCash.setFont(new Font("Courier", Font.PLAIN, 28));
                _tableAndUserTB[1][0].add(_playerCash, BorderLayout.NORTH);
                
                // Display player's cash
                _playerCash.setForeground(WHITE);
                _playerCash.setText("$" + String.valueOf(playerCash));
                _playerCash.setFont(new Font("Courier", Font.PLAIN, 28));
                _tableAndUserTB[1][0].add(_playerCash, BorderLayout.NORTH);
                
                displayCard(_tableAndUserTB[1][1], playerHand[0]);
                displayCard(_tableAndUserTB[1][1], playerHand[1]);
            } else { // Initialize AI player
                String aiName = aiPlayerNames[i - 1];

                _playerLabel = new JLabel(aiName + ": ");
                _playerCash  = new JLabel("");
                players[i]   = new Player(aiName, playerHand, 1000, _playerCash, true);
                playerCash   = players[i].getCash();
                System.out.println(players[i]);
				
				writeCardsFile(players[i]);
                
                _playerLabel.setForeground(WHITE);
                _playerLabel.setFont(new Font("Courier", Font.PLAIN, 28));
                _aiPlayersTB[i-1][0].add(_playerLabel, BorderLayout.NORTH);
                                
                // Display AI's cash
                _playerCash.setForeground(WHITE);
                _playerCash.setText("$" + String.valueOf(playerCash));
                _playerCash.setFont(new Font("Courier", Font.PLAIN, 28));
                _aiPlayersTB[i-1][0].add(_playerCash, BorderLayout.NORTH);
                
                displayCard(_aiPlayersTB[i-1][1], cardBack);
                displayCard(_aiPlayersTB[i-1][1], cardBack);

                players[i].setCardPanel(_aiPlayersTB[i-1][1]);

                //displayCard(_aiPlayersTB[i-1][1], playerHand[0]);
                //displayCard(_aiPlayersTB[i-1][1], playerHand[1]);
            }
        }
        
        // Add panels and repaint
        add(_top);
        add(_bot);
        
        revalidate();
        repaint();

        // playGame();
        // playPreFlop();

		writeHandFile();
		
        playGameSwitch(1);
		
    }

    private void playGame() {

        Action minAction = null;
        int i, j, k;
        int playersLeft = 0;

        while (playersLeft >= 2) {                    //play game while at least 2 players have chips
            // startHand(); //TODO: Reinit Deck + Give each player Cards
            minAction = null;
			
			writeHandFile();
			
            for (i=0; i<4; i++) {                       //pre-flop, flop, turn, river
                if (i==1){           //flop
                    for (k=0; k<3; k++){
                        tableCards[k] = deck.draw();
                    }
                } else if (i==2){    //turn
                    tableCards[3] = deck.draw();
                } else if (i==3) {   //river
                    tableCards[4] = deck.draw();
                }
                for (j=0; j<players.length; j++){    //number of players
                    if (!players[j].isPlayingHand()) continue;   //skip if not in hand (folded)
                    
                    // minAction = Turn.turn(players[j], minAction); // TODO
                    //Need to update Player.setPlayingHand(false) if they folded
                    //Send Player + Current minimum Action
                    //Return New minimum Action
                }

            }

            // WinObj.handCompare(tableCards, players); //TODO: determine who won
            //NEED TO UPDATE playersLeft variable if someone runs out of chips + remove them from players Array
            // endHand(); //TODO: update UI, pot, clear community cards, and hands; Reset all players to playing --> Player.setPlayingHand(true) 

        }

        //TODO: DISPLAY WINNER IN SWING

    }

    private void playGameSwitch(int round) {
        switch(round) {
            case 1:
                playPreFlop();
                break;
            case 2:
                playFlop();
                break;
            case 3:
                playTurn();
                break;
            case 4:
                playRiver();
                break;
        }
    }

    private Action userInput(Action minAction) {
        JPanel   _actionPanel = new JPanel(new BorderLayout());
        JPanel   _btnPanel    = new JPanel(new FlowLayout());
        JButton  _betButton   = new JButton();
        JButton  _foldButton  = new JButton();

        SpinnerModel betModel = new SpinnerNumberModel(0, 0, players[0].getCash(), 1);
        JSpinner _betSpinner  = new JSpinner(betModel);

        Action playerAction   = new Action();

        _betButton.setText("Bet");
        _betButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int betAmount = (int) _betSpinner.getValue();

                playerAction.setValue(betAmount);
            }
        });
        _foldButton.setText("Fold");
        _foldButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playerAction.setValue(-1);
            }
        });

        _btnPanel.add(_betButton);
        _btnPanel.add(_foldButton);

        _actionPanel.add(_betSpinner, BorderLayout.PAGE_START);
        _actionPanel.add(_btnPanel, BorderLayout.PAGE_END);

        add(_actionPanel);

        revalidate();
        repaint();

        return playerAction;
    }

    private void playPreFlop() {
        userInputPanel(2);
    }

    private void playFlop() {
        for (int i = 0; i < 3; i++) {
            displayCard(_tablePanel, tableCards[i]);
			writeDeckCardsFile(tableCards[i], 0);
        }

        if(!players[2].isPlayingHand()) {
            playGameSwitch(3);
        }
        else if(players[0].isPlayingHand()) {
            userInputPanel(3);
        } else {
            // AI players
            playAI(new Action(0));

            // Reset
            playGameSwitch(3);
        }
    }

    private void playTurn() {
        displayCard(_tablePanel, tableCards[3]);
		
		writeDeckCardsFile(tableCards[3], 1);

        if(!players[2].isPlayingHand()) {
            playGameSwitch(4);
        }
        else if(players[0].isPlayingHand()) {
            userInputPanel(4);
        } else {
            // AI players
            playAI(new Action(0));

            // Reset
            playGameSwitch(4);
        }
    }

    // TODO: Break into methods that can still access needed variables.
    private void playRiver() {
        displayCard(_tablePanel, tableCards[4]);
		
		writeDeckCardsFile(tableCards[4], 2);

        if(!players[2].isPlayingHand()) {
			String endResult = new GameUtils().determineBestHand(players, tableCards, pot);
			
			writeEndFile(endResult);
			
            JLabel _results = new JLabel(endResult);
            _results.setForeground(WHITE);
            _results.setFont(new Font("Courier", Font.PLAIN, 28));
            _results.setHorizontalAlignment(SwingConstants.CENTER);
            add(_results, BorderLayout.SOUTH);

            nextHandButton();

            revalidate();
            repaint();  
        }
        else if(players[0].isPlayingHand()) {
            userInputPanel(5);
        } else {
            playAI(new Action(0));
			
			String endResult = new GameUtils().determineBestHand(players, tableCards, pot);

			writeEndFile(endResult);
			
            JLabel _results = new JLabel(endResult);
            _results.setForeground(WHITE);
            _results.setFont(new Font("Courier", Font.PLAIN, 28));
            _results.setHorizontalAlignment(SwingConstants.CENTER);
            add(_results, BorderLayout.SOUTH);

            nextHandButton();
            
            revalidate();
            repaint();
        }
    }

    private void userInputPanel(int round) {
        JPanel   _wrapperPanel = new JPanel();
        JPanel   _actionPanel  = new JPanel(new BorderLayout());
        JPanel   _btnPanel     = new JPanel(new FlowLayout());
        JButton  _betButton    = new JButton();
        JButton  _foldButton   = new JButton();

        SpinnerModel betModel  = new SpinnerNumberModel(0, 0, players[0].getCash(), 1);
        JSpinner _betSpinner   = new JSpinner(betModel);

        Action   playerAction  = new Action();

        _wrapperPanel.setBackground(POKER_GREEN);
        _actionPanel.setBackground(POKER_GREEN);
        _btnPanel.setBackground(POKER_GREEN);
        _betSpinner.setFont(new Font("Courier", Font.PLAIN, 30));
        _betSpinner.setPreferredSize(new Dimension(300, 50));
        _actionPanel.setPreferredSize(new Dimension(300, 150));

        _betButton.setText("Bet");
        _betButton.setFont(new Font("Courier", Font.PLAIN, 30));
        _betButton.setPreferredSize(new Dimension(130, 70));
        _betButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int betAmount = (int) _betSpinner.getValue();

                playerAction.setValue(betAmount);
				
				writeActionFile(playerAction, 0);

                // AI players
                playAI(playerAction);
                
                // Reset
                remove(_wrapperPanel);
                
                if(round <= 4) {
                    // AI players
                    playAI(new Action(-2));

                    // Reset
                    playGameSwitch(round);
                } else {
					String endResult = new GameUtils().determineBestHand(players, tableCards, pot);
					
					writeEndFile(endResult);
					
                    JLabel _results = new JLabel(endResult);
                    _results.setForeground(WHITE);
                    _results.setFont(new Font("Courier", Font.PLAIN, 28));
                    _results.setHorizontalAlignment(SwingConstants.CENTER);
                    add(_results, BorderLayout.SOUTH);

                    showAICards();

                    nextHandButton();
                }

                revalidate();
                repaint();  
            }
        });
        _foldButton.setText("Fold");
        _foldButton.setFont(new Font("Courier", Font.PLAIN, 30));
        _foldButton.setPreferredSize(new Dimension(130, 70));
        _foldButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playerAction.setValue(-1);

				players[0].setPlayingHand(false);
				
				writeActionFile(playerAction, 0);
				
                // AI players
                playAI(playerAction);

                // Reset
                remove(_wrapperPanel);
                
                if(round <= 4) {
                    // AI players
                    playAI(new Action(-2));

                    // Reset
                    playGameSwitch(round);
                } else {
					String endResult = new GameUtils().determineBestHand(players, tableCards, pot);
					
					writeEndFile(endResult);
					
                    JLabel _results = new JLabel(endResult);
                    _results.setForeground(WHITE);
                    _results.setFont(new Font("Courier", Font.PLAIN, 20));
                    _results.setHorizontalAlignment(SwingConstants.CENTER);
                    add(_results, BorderLayout.SOUTH);

                    showAICards();

                    nextHandButton();
                }
                
                revalidate();
                repaint();  
            }
        });

        _btnPanel.add(_betButton);
        _btnPanel.add(_foldButton);

        _actionPanel.add(_betSpinner, BorderLayout.PAGE_START);
        _actionPanel.add(_btnPanel, BorderLayout.PAGE_END);

        _wrapperPanel.add(_actionPanel);

        add(_wrapperPanel);

        revalidate();
        repaint();

        // return _actionPanel;
    }

    private void nextHandButton() {
        JPanel  _buttonPanel    = new JPanel();
        JButton _nextHandButton = new JButton("Deal next hand");
        _buttonPanel.setBackground(POKER_GREEN);
        _nextHandButton.setFont(new Font("Courier", Font.PLAIN, 30));
        _nextHandButton.setPreferredSize(new Dimension(400, 100));
        // _nextHandButton.setVerticalAlignment(SwingConstants.CENTER);
        // _nextHandButton.setHorizontalAlignment(SwingConstants.CENTER);
        _nextHandButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                
            }
        });

        _buttonPanel.add(_nextHandButton);

        add(_buttonPanel);
        
        revalidate();
        repaint();
    }

    /**
     * Current very simple AI action, if user bets AI fold.
     * TODO: move to own class.
	 *
	 *
	 * If userAction is > 0, userAction = money bet by user, AI folds
	 * If userAction is = 0, userAction = user checked, AI checks
	 * If userAction is = -1, userAction = user folded, AI folds
     */
    private void playAI(Action userAction) {
        for(int i = 1; i < players.length; i++) {
			
			//if user folds
			if(userAction.getValue() == -1 && players[i].isPlayingHand()) {
				players[i].setPlayingHand(false);
				System.out.println("user " + userAction.toString() + " , ai " + i + " folded");
				writeActionFile(userAction, i);
			}
			
			//if user bets 0 / checks
			if(userAction.getValue() == 0 && players[i].isPlayingHand()) {
				players[i].setPlayingHand(true);
				System.out.println("user " + userAction.toString() + ", ai " + i + " checks");
				writeActionFile(userAction, i);
			}
			
			//if user bets more than 0
            if(userAction.getValue() > 0 && players[i].isPlayingHand()) {
				userAction.setValue(-1);
                players[i].setPlayingHand(false);
                System.out.println("user " + userAction.toString() + ", ai " + i + " folded");
				writeActionFile(userAction, i);
            }
			
			//makes program wait a second after each AI move
			//it works, just commented out for quicker testing
			/*
			try{
				Thread.sleep(500);
			}
			catch(InterruptedException ex){
				Thread.currentThread().interrupt();
			}
			*/
			
        }
    }

    private void showAICards() {
        for(int i = 1; i < players.length; i++) {
            if(players[i].isPlayingHand()) {
                JPanel _cardPanel = players[i].getCardPanel();
                Card[] playerHand = players[i].getCards();
                System.out.println(players[i]);

                _cardPanel.removeAll();

                displayCard(_cardPanel, playerHand[0]);
                displayCard(_cardPanel, playerHand[1]);
            }
        }

        revalidate();
        repaint(); 
    }
    
    /**
     * Display card in JPanel container.
     * @param _loc Panel to display card in.
     * @param card Card to display.
     */
    private void displayCard(JPanel _loc, Card card){
        Image  resizedCard  = card.getFace().getScaledInstance(75, 105,  java.awt.Image.SCALE_SMOOTH);
        JLabel _cardDisplay = new JLabel(new ImageIcon(resizedCard));

        _loc.add(_cardDisplay, BorderLayout.SOUTH);
    }
	
	/*
	 * Opens the external file and writes the intro.
	 * File that is opened is called "output.txt".
	 * @param	UserName		The input name from the player.
	 * @param	aiPlayerNames	The selected AI names.
	*/
	private void writeStartFile(String userName, String[] aiPlayerNames){
		//Write the intro to the External File
		File outputFile = new File("output.txt");
		
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
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
	private void writeHandFile(){
		try{
			File outputFile = new File("output.txt");
			
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
	private void writeCardsFile(Player players){
		try{
			File outputFile = new File("output.txt");
			
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
	private void writeDeckCardsFile(Card cards, int when){
		try{
			File outputFile = new File("output.txt");
			
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
	private void writeActionFile(Action userAction, int playerNum){
		
		try{
			File outputFile = new File("output.txt");
			
			//Here true is to append the content to file
			FileWriter fw = new FileWriter(outputFile,true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write(players[playerNum].getName() + " " + userAction.toString());
			
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
	private void writeEndFile(String endResult){
		
		try{
			File outputFile = new File("output.txt");
			
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