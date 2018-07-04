import java.awt.*;
import javax.swing.*;

import java.io.*;
import java.util.*;
import javax.imageio.*;
import java.awt.event.*;
import java.text.*;
import java.awt.image.BufferedImage;

public class Game extends JPanel {
    private final Color POKER_GREEN = new Color(71, 113, 72);
    private final Color POKER_DARK  = new Color(47, 89, 49);
    private final Color WHITE       = new Color(255, 255, 255);

    private Player[] players    = null;
    private Deck     deck       = null;
    private Card[]   tableCards = null;
    private Card     cardBack   = null;
    private int      pot        = 0;
    private JPanel   _tablePanel = null;
    
	private int		 turnNum	= 1;
	private int		 dealerNum	= -1;
	private int		 sBlindNum	= -1;
	private int		 bBlindNum	= -1;
	private int		 sBlindVal  = 10;
	private int 	 bBlindVal  = 20;
    
    private CommunityCardsPanel _communityPanel = null;

    private JPanel   _aiPlayersPanel = new JPanel();
    private JPanel   _middlePanel    = new JPanel();
    private JPanel   _bottomPanel    = new JPanel(null);
    private JScrollPane _scrollPane  = new JScrollPane();
    private JLabel   _gameLogLabel   = new JLabel();

    // Action panel components
    private JPanel   _actionPanel = new JPanel();
    private JButton  _betButton   = new JButton();
    private JButton  _checkButton = new JButton();
    private JButton  _foldButton  = new JButton();
    private JSpinner _betSpinner  = null;


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
        
        _playerNameInput.setPreferredSize(new Dimension( 200, 50 ));
        _playerNameLabel.setFont(new Font("Courier", Font.PLAIN, 40));
        _playerNameInput.setFont(new Font("Courier", Font.PLAIN, 30));
        _playerNameInput.setHorizontalAlignment(SwingConstants.CENTER);
        _playerNameInput.setText("Player 1");
        _playerNamePanel.add(_playerNameInput);

        _numOppLabel.setForeground(WHITE);
        _numOppLabel.setFont(new Font("Courier", Font.PLAIN, 40));
        _numOppLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        _numOppLabel.setVerticalAlignment(SwingConstants.TOP);
        _numOppLabel.setText("Number of Opponents:");
        
        _numOpp.setModel(new DefaultComboBoxModel<>(new Integer[] { 1, 2, 3, 4, 5, 6, 7 }));        
        _numOpp.setPreferredSize(new Dimension( 100, 50 ));
        _numOpp.setSelectedItem(4);
        _numOpp.setFont(new Font("Courier", Font.PLAIN, 30));
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
                createNewGame(name, numOpponents);
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
    
    private void printGameConsole(String line) {
        String        currentText = _gameLogLabel.getText().replace("</html>", "<br/>");
        StringBuilder lineBuilder = new StringBuilder(currentText);
        JScrollBar    _scrollBar  = _scrollPane.getVerticalScrollBar();

        lineBuilder.append(line);
        lineBuilder.append("</html>");

        _gameLogLabel.setText(lineBuilder.toString());
        
        _scrollPane.revalidate();
        _scrollPane.repaint();

        _scrollBar.setValue(_scrollBar.getMaximum());
    }

    /**
     * Create new game objects and and initialize players.
     * @param userName Player name.
     * @param numOfAI  Number of AI players to initialize.
     */
    private void createNewGame(String userName, int numOfAI) {
        JPanel     _top            = new JPanel();
        JPanel     _bot            = new JPanel();
        JPanel     _tablePotPanel     = new JPanel();

        JLabel     _tableLabel     = new JLabel("Pot: ");
        JLabel     _pot            = new JLabel("");

        GameUtils  utils           = new GameUtils();
        String[]   aiPlayerNames   = utils.getAINames(numOfAI, userName);
        
        try {
            Image back = ImageIO.read(this.getClass().getResource("/back.png"));
            cardBack = new Card("Back", back);
        } catch (IOException ioex) {
            System.exit(1);
        }

        deck       = new Deck();
        tableCards = new Card[5];
        players    = new Player[numOfAI + 1];
		
		dealerNum 	= -1;
		sBlindNum	= -1;
		bBlindNum	= -1;
		
		selectDealer();
		
		writeStartFile(userName, aiPlayerNames);
        
        setLayout(new GridLayout(3, 1));
        _top.setLayout(new GridLayout(1, numOfAI, 50, 100));
        _bot.setLayout(new GridLayout(1, 2, 50, 100));
        
        setBackground(POKER_GREEN);
        _top.setBackground(POKER_GREEN);
        _bot.setBackground(POKER_GREEN);

        // Make Labels and distribute starting cards
        _tableLabel.setForeground(WHITE);
        _tableLabel.setFont(new Font("Courier", Font.PLAIN, 28));
        _tablePotPanel.add(_tableLabel, BorderLayout.NORTH);
        
		//Add the blinds to the pot
		pot = pot + sBlindVal + bBlindVal;
		
        // Display the pot value
        _pot.setForeground(WHITE);
        _pot.setText("$" + String.valueOf(pot));
        _pot.setFont(new Font("Courier", Font.PLAIN, 28));
        _tablePotPanel.add(_pot, BorderLayout.NORTH);
        
        for(int i = 0; i < 5; i++) {
            tableCards[i] = deck.draw();
            // displayCard(_tableAndUserTB[0][1], tableCards[i]);
        }
        // Initialize table cards
        _communityPanel = new CommunityCardsPanel(tableCards, cardBack);
		
		//writeCardsFile(deck);
        
        //For the players section
        for(int i = 0; i < players.length; i++){
            Card[] playerHand   = new Card[2];
            JLabel _playerLabel = null;
            JLabel _playerCash  = null;
            int    playerCash   = 0;
			int		initPlayerCash = 1000;
			//for a description of playerRole check displayButton function
			int		playerRole	= 0;
			
			if (turnNum == 1){
				playerCash = initPlayerCash;
			}
			
			//player is dealer
			if(dealerNum == i){
				playerRole = 1;
			}
			//player is small blind
			else if(sBlindNum == i){
				playerRole = 2;
				playerCash = playerCash - sBlindVal;
			}
			//player is big blind
			else if(bBlindNum == i){
				playerRole = 3;
				playerCash = playerCash - bBlindVal;
			}
            // JLabel _playerLabel = null;
            // JLabel _playerCash  = null;
            PlayerPanel _playerPanel = null;

            playerHand[0] = deck.draw();
            playerHand[1] = deck.draw();

            if(i == 0) { // Initialize human player
                // _playerLabel = new JLabel(userName + ": ");
                // _playerCash  = new JLabel();
                players[i]   = new Player(userName, playerHand, playerRole, 1000, true);
                // playerCash   = players[i].getCash();
				
				writeCardsFile(players[i]);
            } else { // Initialize AI player
                String aiName = aiPlayerNames[i - 1];

                // _playerLabel = new JLabel(aiName + ": ");
                // _playerCash  = new JLabel("");
                players[i]   = new Player(aiName, playerHand, playerRole, 1000, true);
                // playerCash   = players[i].getCash();
                System.out.println(players[i]);
				
				writeCardsFile(players[i]);
                
                _playerPanel = new PlayerPanel(players[i], cardBack, false);
                players[i].setPlayerPanel(_playerPanel);

                _top.add(_playerPanel);
            }
        }
        
        _aiPlayersPanel.setBackground(POKER_GREEN);
        _aiPlayersPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        _middlePanel.setBackground(POKER_GREEN);
        _middlePanel.setLayout(new BorderLayout());

        // Add panels and repaint
        this.setLayout(new BorderLayout());
        _aiPlayersPanel.add(_top, BorderLayout.PAGE_START);
        _middlePanel.add(new PotPanel(), BorderLayout.LINE_START);
        _middlePanel.add(_communityPanel, BorderLayout.CENTER);

        _gameLogLabel.setText("<html>Game Log:<br/></html>");
        _gameLogLabel.setForeground(Color.WHITE);
        _gameLogLabel.setBackground(POKER_DARK);
        _gameLogLabel.setOpaque(true);
        _gameLogLabel.setVerticalAlignment(SwingConstants.TOP);

        _scrollPane = new JScrollPane(_gameLogLabel);
        _scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        _scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        _scrollPane.setBounds(0, 0, 500, 250);
        _scrollPane.setBackground(new Color(29, 56, 199));
        _scrollPane.setPreferredSize(new Dimension(600, 250));
        _scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        _bottomPanel.setLayout(new BorderLayout());
        _bottomPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 250));
        _bottomPanel.setBackground(POKER_GREEN);
        _bottomPanel.add(_scrollPane, BorderLayout.LINE_END);

        add(_aiPlayersPanel, BorderLayout.PAGE_START);
        add(_middlePanel, BorderLayout.CENTER);
        add(_bottomPanel, BorderLayout.PAGE_END);
        
        revalidate();
        repaint();

		writeHandFile();
		
        userPanel();

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
			
			turnNum++;
			
			selectDealer();
			
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

    /**
     * Central location to call all round methods based on current round number.
     * @param round Next round number.
     */
    private void playGameSwitch(int round) {
        printGameConsole("<br/>Round " + round + ":");

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

    /**
     * First round of betting logic.
     */
    private void playPreFlop() {
        setUserActions(2);
    }

    /**
     * Second round of betting logic.
     */
    private void playFlop() {
        _communityPanel.deal();
        //TODO: writeDeckCardsFile(tableCards[i], 0);

        if(!players[2].isPlayingHand()) {
            playGameSwitch(3);
        }
        else if(players[0].isPlayingHand()) {
            setUserActions(3);
        } else {
            // AI players
            playAI(null);

            // Reset
            playGameSwitch(3);
        }
    }

    /**
     * Third round of betting logic.
     */
    private void playTurn() {
        _communityPanel.deal();
        //TODO: writeDeckCardsFile(tableCards[3], 0);


        if(!players[2].isPlayingHand()) {
            playGameSwitch(4);
        }
        else if(players[0].isPlayingHand()) {
            setUserActions(4);
        } else {
            // AI players
            playAI(null);

            // Reset
            playGameSwitch(4);
        }
    }

    /**
     * Fourth round of betting logic. End of game compare hand logic handled in userPanel()
     * method based on round number.
     */
    private void playRiver() {
        _communityPanel.deal();
        //TODO: writeDeckCardsFile(tableCards[4], 0);

        if(!players[2].isPlayingHand()) {
			String endResult = new GameUtils().determineBestHand(players, tableCards, pot);
			
			writeEndFile(endResult);
			
            JLabel _results = new JLabel(endResult);
            _results.setForeground(WHITE);
            _results.setFont(new Font("Courier", Font.PLAIN, 28));
            _results.setHorizontalAlignment(SwingConstants.CENTER);
            add(_results, BorderLayout.SOUTH);

            printGameConsole("<br/>" + endResult);

            showAICards();

            nextHandButton();
        }
        else if(players[0].isPlayingHand()) {
            setUserActions(5);
        } else {
			String endResult = new GameUtils().determineBestHand(players, tableCards, pot);
            playAI(null);

			writeEndFile(endResult);
			
            JLabel _results = new JLabel(endResult);
            _results.setForeground(WHITE);
            _results.setFont(new Font("Courier", Font.PLAIN, 28));
            _results.setHorizontalAlignment(SwingConstants.CENTER);
            add(_results, BorderLayout.SOUTH);

            printGameConsole(endResult);

            showAICards();

            nextHandButton();
        }
    }

    /**
     * Add user panel components.
     */
    private void userPanel() {
        JPanel  _userPanel      = new JPanel();
        JPanel   _centeredPanel = new JPanel();
        PlayerPanel _playerPanel = new PlayerPanel(players[0], cardBack, true);

        JPanel   _btnPanel      = new JPanel(new FlowLayout());

        SpinnerModel betModel  = new SpinnerNumberModel(10, 10, players[0].getCash(), 1);
        
        _betSpinner   = new JSpinner(betModel);

        players[0].setPlayerPanel(_playerPanel); // Save panel to Player object

        _userPanel.setBackground(POKER_GREEN);
        _userPanel.setPreferredSize(new Dimension(600, Integer.MAX_VALUE));
        _userPanel.setLayout(new GridBagLayout());

        _centeredPanel.setLayout(new BorderLayout());

        _btnPanel.setBackground(POKER_GREEN);
        _betSpinner.setFont(new Font("Courier", Font.PLAIN, 25));
        _betSpinner.setPreferredSize(new Dimension(300, 50));

        _actionPanel.setBackground(POKER_GREEN);
        _actionPanel.setLayout(new BorderLayout());
        _actionPanel.setPreferredSize(new Dimension(340, 150));
        _actionPanel.setMaximumSize(new Dimension(340, 150));
        _actionPanel.setMinimumSize(new Dimension(340, 150));

        _betButton.setText("Bet");
        _betButton.setFont(new Font("Courier", Font.PLAIN, 22));
        _betButton.setPreferredSize(new Dimension(100, 60));
        
        _checkButton.setText("Check");
        _checkButton.setFont(new Font("Courier", Font.PLAIN, 22));
        _checkButton.setPreferredSize(new Dimension(100, 60));
        
        _foldButton.setText("Fold");
        _foldButton.setFont(new Font("Courier", Font.PLAIN, 22));
        _foldButton.setPreferredSize(new Dimension(100, 60));
        

        _btnPanel.add(_betButton);
        _btnPanel.add(_checkButton);
        _btnPanel.add(_foldButton);

        _actionPanel.add(_betSpinner, BorderLayout.PAGE_START);
        _actionPanel.add(_btnPanel, BorderLayout.PAGE_END);

        _centeredPanel.add(_playerPanel, BorderLayout.PAGE_START);
        _centeredPanel.add(_actionPanel, BorderLayout.PAGE_END);

        _userPanel.add(_centeredPanel);

        _middlePanel.add(_userPanel, BorderLayout.LINE_END);

        revalidate();
        repaint();
    }

    private Action setUserActions(int round) {
        Action playerAction = null;

        System.out.println(round);

        if(round < 5) {
            ActionListener[] al = _betButton.getActionListeners();
            for(int i = 0; i < al.length; i++) {
                _betButton.removeActionListener(al[i]);
            }
            _betButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int betAmount = (int) _betSpinner.getValue();
    
                    // AI players
                    playAI(new Action(betAmount));

                    // Reset
                    playGameSwitch(round);
    
                    revalidate();
                    repaint();  
                }
            });

            al = _checkButton.getActionListeners();
            for(int i = 0; i < al.length; i++) {
                _checkButton.removeActionListener(al[i]);
            }
            _checkButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {    
                    // AI players
                    playAI(new Action(0));

                    // Reset
                    playGameSwitch(round);
    
                    revalidate();
                    repaint();  
                }
            });

            al = _foldButton.getActionListeners();
            for(int i = 0; i < al.length; i++) {
                _foldButton.removeActionListener(al[i]);
            }
            _foldButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    players[0].setPlayingHand(false);

                    // AI players
                    playAI(new Action(-1));

                    // Reset
                    playGameSwitch(round);
                    
                    revalidate();
                    repaint();  
                }
            });
        } else { // End of game methods
            showAICards();
            endOfHand();
        }
        
        return playerAction;
    }

    /**
     * User input buttons, displayed on user's turn.
     * @param round Round number.
     */
    private void userInputPanel(int round) {
        
    }

    private void endOfHand() {
        String result   = new GameUtils().determineBestHand(players, tableCards, pot);
        JLabel _results = new JLabel(result);
        _results.setForeground(WHITE);
        _results.setFont(new Font("Courier", Font.PLAIN, 28));
        _results.setHorizontalAlignment(SwingConstants.CENTER);
        // add(_results, BorderLayout.SOUTH);

        printGameConsole(result);
        showAICards();
        nextHandButton();
    }

    private void nextHandButton() {
        JPanel  _buttonPanel    = new JPanel();
        JButton _nextHandButton = new JButton("Deal next hand");
        _buttonPanel.setBackground(POKER_GREEN);
        _nextHandButton.setFont(new Font("Courier", Font.PLAIN, 30));
        _nextHandButton.setPreferredSize(new Dimension(400, 100));
        _nextHandButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {

            }
        });

        _buttonPanel.add(_nextHandButton);

        System.out.println("nextHandButton");

        _actionPanel.removeAll();
        _actionPanel.add(_buttonPanel);
        
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
        try {
            printGameConsole(players[0].getName() + " " + userAction.toString());
        } catch(Exception e) {
            System.out.println(players[0].getName() + " didn't play");
            userAction = new Action();
        }
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
            players[i].getPlayerPanel().showCards(true);
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
	 * Displays the button for the specific player
	 * @param	_loc		Panel to display card in
	 * @param	playerNum	the player being looked at
	 * If playerRole = 0, the player does not get a button
	 * If playerRole = 1, the player is the Dealer
	 * If playerRole = 2, the player is the Small Blind
	 * If playerRole = 3, the player is the Big Blind
	*/
	private void displayButton(JPanel _loc, int playerNum){
		
		String dealerButton = "Dealer.png";
		String smallBlindButton = "Small-Blind.png";
		String bigBlindButton = "Big-Blind.png";
		
		int playerRole = players[playerNum].getRole();
		
		if(playerRole != 0){
			BufferedImage button = null;
			
			if(playerRole == 1){
				try {
					button = ImageIO.read(dealerButton.getClass().getResource("/Dealer.png"));
				} catch (IOException ioex) {
						System.exit(1);
				}
			}
			else if(playerRole == 2){
				try {
					button = ImageIO.read(smallBlindButton.getClass().getResource("/Small-Blind.png"));
				} catch (IOException ioex) {
						System.exit(1);
				}
			}
			else if(playerRole == 3){
				try {
					button = ImageIO.read(bigBlindButton.getClass().getResource("/Big-Blind.png"));
				} catch (IOException ioex) {
						System.exit(1);
				}
			}
			
			JLabel _buttonDisplay = new JLabel(new ImageIcon(button));
			
			_loc.add(_buttonDisplay, BorderLayout.SOUTH);
		}
	}
	
	
	/*
	 * Selects the dealer
	 * If dealerNum = -1 then the dealerNum is randomized
	 * If dealerNum = total length of players, reset it back to 0
	 * Otherwise, dealerNum is ++
	*/
	private void selectDealer(){
		//if there are only two players, there is only a big blind and small blind
		if (players.length != 2){
			//game has not started, randomize the dealer
			if (dealerNum == -1){
				Random rand = new Random();
			
				dealerNum = rand.nextInt(players.length);
				selectSmallBlind();
				selectBigBlind();
			}
			//resetting dealerNum since it reached the end
			else if (dealerNum == players.length){
				dealerNum = 0;
				
				selectSmallBlind();
				selectBigBlind();
			}
			//moving the dealer clockwise
			else{
				dealerNum++;
				
				selectSmallBlind();
				selectBigBlind();
			}
		}
		else{
			//starting the game with only 2 players, should randomize small blind
			if (sBlindNum == -1){
				Random rand = new Random();
				
				sBlindNum = rand.nextInt(2);
				selectBigBlind();
			}
			else{
				selectSmallBlind();
				selectBigBlind();
			}
		}
	}
	
	/*
	 * Selects the player with the small blind
	 * If dealerNum is the last player, sBlindNum = 0
	 * Otherwise sBlindNum is dealerNum + 1
	*/
	private void selectSmallBlind(){
		if (dealerNum == (players.length - 1)){
			sBlindNum = 0;
		}
		else{
			sBlindNum = dealerNum + 1;
		}
	}
	
	/*
	 *Selects the player with the big blind
	 *If sBlindNum is the last player, sBlindNum = 0
	 *Otherwise bBlindNum is sBlindNum + 1
	*/
	private void selectBigBlind(){
		if (sBlindNum == (players.length - 1)){
			bBlindNum = 0;
		}
		else{
			bBlindNum = sBlindNum + 1;
		}
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