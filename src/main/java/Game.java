import java.awt.*;
import javax.swing.*;

import java.io.*;
import java.util.*;
import javax.imageio.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Game extends JPanel {
    private final Color POKER_GREEN  = new Color(71, 113, 72);
    private final Color POKER_DARK   = new Color(47, 89, 49);
    private final Color WHITE        = new Color(255, 255, 255);
    private final int   sBlindVal    = 10;
    private final int   bBlindVal    = 20;
    private final int   startingCash = 1000;
    private final int   playerStart  = 1000;


    private Player[] players     = null;
    private Deck     deck        = null;
    private Pot[]    pots        = null;
    private int      potCount    = 1;
    private Card[]   tableCards  = null;
    private Card     cardBack    = null;
    private Logging  gameLogging = null;
    private Random   random      = new Random();
    
	private int		 turnNum	= 1;
	private int		 dealerNum	= -1;
	private int		 sBlindNum	= -1;
	private int		 bBlindNum	= -1;
    
    private CommunityCardsPanel _communityPanel = null;

    private JPanel      _aiPlayersPanel = new JPanel();
    private JPanel      _middlePanel    = new JPanel();
    private JPanel      _bottomPanel    = new JPanel(null);
    private JScrollPane _scrollPane     = new JScrollPane();
    private JLabel      _gameLogLabel   = new JLabel();
    private JPanel      _userPanel      = new JPanel();
    private PotPanel    _potPanel       = new PotPanel();

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

        revalidate();
        repaint();
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

        JLabel     _tableLabel     = new JLabel("Pot: ");

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

        pots    = new Pot[numOfAI];
        pots[0] = new Pot(0, 0, players, false);
        potCount=1;
		
		dealerNum 	= -1;
		sBlindNum	= -1;
		bBlindNum	= -1;
		
		selectDealer();
        
        gameLogging = new Logging();        
		gameLogging.writeStartFile(userName, aiPlayerNames);
        
        setLayout(new GridLayout(3, 1));
        _top.setLayout(new GridLayout(1, numOfAI, 50, 100));
        _bot.setLayout(new GridLayout(1, 2, 50, 100));
        
        setBackground(POKER_GREEN);
        _top.setBackground(POKER_GREEN);
        _bot.setBackground(POKER_GREEN);

        // Make Labels and distribute starting cards
        _tableLabel.setForeground(WHITE);
        _tableLabel.setFont(new Font("Courier", Font.PLAIN, 28));
        
        for(int i = 0; i < 5; i++) {
            tableCards[i] = deck.draw();
        }
        // Initialize table cards
        _communityPanel = new CommunityCardsPanel(tableCards, cardBack);
		
		//writeCardsFile(deck);
        
        //For the players section
        for(int i = 0; i < players.length; i++){
            PlayerPanel _playerPanel = null;
            Card[]      playerHand   = new Card[2];
			int	        playerRole   = 0; //for a description of playerRole check displayButton function
            
			if(dealerNum == i) { //player is dealer
				playerRole = 1;
			} else if(sBlindNum == i) { //player is small blind
				playerRole = 2;
			} else if(bBlindNum == i) { //player is big blind
				playerRole = 3;
			}

            playerHand[0] = deck.draw();
            playerHand[1] = deck.draw();

            if(i == 0) { // Initialize human player
                players[i] = new Player(userName, playerHand, playerRole, playerStart, true);
                
                _userPanel = userPanel();

				gameLogging.writeCardsFile(players[i]);
            } else { // Initialize AI player
                String aiName = aiPlayerNames[i - 1];

                players[i]   = new Player(aiName, playerHand, playerRole, startingCash, true);
                				
				gameLogging.writeCardsFile(players[i]);

                
                _playerPanel = new PlayerPanel(players[i], cardBack, false);
                players[i].setPlayerPanel(_playerPanel);

                _top.add(_playerPanel);
            }

            // add blinds to pot
            if(playerRole == 2) {
                bet(players[i], sBlindVal, true);
            } else if(playerRole == 3) {
                bet(players[i], bBlindVal, true);
            }

            System.out.println(players[i]); // print player info to system
        }
        
        _aiPlayersPanel.setBackground(POKER_GREEN);
        _aiPlayersPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        _middlePanel.setBackground(POKER_GREEN);
        _middlePanel.setLayout(new BorderLayout());

        // Add panels and repaint
        this.setLayout(new BorderLayout());
        _aiPlayersPanel.add(_top, BorderLayout.PAGE_START);
        _middlePanel.add(_potPanel, BorderLayout.LINE_START);
        _middlePanel.add(_communityPanel, BorderLayout.CENTER);
        _middlePanel.add(_userPanel, BorderLayout.LINE_END);

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

        gameLogging.writeHandFile(turnNum);

        playGameSwitch(1);
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
        setUserActions(1);
    }

    /**
     * Second round of betting logic.
     */
    private void playFlop() {
        _communityPanel.deal();
        //TODO: writeDeckCardsFile(tableCards[i], 0);

        resetCurrentBet();

        if(playersPlaying() < 2) {
            playGameSwitch(3);
        }
        else if(players[0].isPlayingHand()) {
            setUserActions(2);
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

        resetCurrentBet();
        
        if(playersPlaying() < 2) {
            playGameSwitch(4);
        }
        else if(players[0].isPlayingHand()) {
            setUserActions(3);
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

        resetCurrentBet();
        if(playersPlaying() < 2) {
            for (int i=potCount-1; i>0; i--){
                new GameUtils().determineBestHand(pots[potCount-1].getValidPlayers(), tableCards, _potPanel.clearPot(i));
            }   //Distribute side pot winnings
            
            String endResult = new GameUtils().determineBestHand(players, tableCards, _potPanel.clearPot(0));


			
			gameLogging.writeEndFile(endResult);
			
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
            setUserActions(4);
        } else {
            for (int i=potCount-1; i>0; i--){
                new GameUtils().determineBestHand(pots[potCount-1].getValidPlayers(), tableCards, _potPanel.clearPot(i));
            }   //Distribute side pot winnings
			String endResult = new GameUtils().determineBestHand(players, tableCards, _potPanel.clearPot(0));
            playAI(null);


			gameLogging.writeEndFile(endResult);
			
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
    private JPanel userPanel() {
        JPanel  _userPanel      = new JPanel();
        JPanel   _centeredPanel = new JPanel();
        PlayerPanel _playerPanel = new PlayerPanel(players[0], cardBack, true);

        JPanel   _btnPanel      = new JPanel(new FlowLayout());

        SpinnerModel betModel  = new SpinnerNumberModel(20, 20, players[0].getCash(), 1);
        
        _betSpinner   = new JSpinner(betModel);

        players[0].setPlayerPanel(_playerPanel); // Save panel to Player object

        _userPanel.setBackground(POKER_GREEN);
        _userPanel.setPreferredSize(new Dimension(600, Integer.MAX_VALUE));
        _userPanel.setLayout(new GridBagLayout());

        _centeredPanel.setLayout(new BorderLayout());

        _btnPanel.setBackground(POKER_GREEN);
        _betSpinner.setFont(new Font("Courier", Font.PLAIN, 25));
        _betSpinner.setPreferredSize(new Dimension(300, 50));

        _actionPanel = new JPanel();
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

        return _userPanel;
    }

    private Action setUserActions(int round) {
        Action playerAction = null;

        if(round < 5) {
            ActionListener[] al = _betButton.getActionListeners();
            for(int i = 0; i < al.length; i++) {
                _betButton.removeActionListener(al[i]);
            }
            _betButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int betAmount = (int) _betSpinner.getValue();

                    if(betAmount <= players[0].getCash()) {
                        bet(players[0], betAmount, false);

                        // AI players
                        playAI(new Action(betAmount));

                        // Reset
                        if(round == 4) {
                            setUserActions(5);
                        } else {
                            playGameSwitch(round+1);
                        }
                    } else {
                        _betSpinner.setValue(players[0].getCash());
                    }

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

                    // playGameSwitch(round+1);
    
                    revalidate();
                    repaint(); 
                    
                    if(round == 4) {
                        setUserActions(5);
                    } else {
                        playGameSwitch(round+1);
                    }
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
                    
                    
                    revalidate();
                    repaint();

                    if(round == 4) {
                        setUserActions(5);
                    } else {
                        playGameSwitch(round+1);
                    }
                }
            });
        } else { // End of game methods
            // showAICards();
            endOfHand();
        }
        
        return playerAction;
    }

    private int playersRemaining() {
        int num = 0;

        for(int i =0; i < players.length; i++) {
            if(players[i].getCash() > 0) {
                num++;
            }
        }

        return num;
    }

    private int playersPlaying() {
        int num = 0;

        for(int i =0; i < players.length; i++) {
            if(players[i].isPlayingHand()) {
                num++;
            }
        }

        return num;
    }

    private void endOfHand() {
        for (int i=potCount-1; i>0; i--){
            new GameUtils().determineBestHand(pots[potCount-1].getValidPlayers(), tableCards, _potPanel.clearPot(i));
        }   //Distribute side pot winnings
        String result   = new GameUtils().determineBestHand(players, tableCards, _potPanel.clearPot(0));
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
                JPanel _top = new JPanel();

                deck = new Deck();

                for(int i = 0; i < 5; i++) {
                    tableCards[i] = deck.draw();
                }

                _communityPanel = new CommunityCardsPanel(tableCards, cardBack);

                System.out.printf("D: %d, S: %d, B: %d\n", dealerNum, sBlindNum, bBlindNum);
                selectDealer();
                System.out.printf("D: %d, S: %d, B: %d\n", dealerNum, sBlindNum, bBlindNum);


                for(int i = 0; i < players.length; i++) {
                    Card[] playerHand = new Card[2];

                    _top.setLayout(new GridLayout(1, players.length-1, 50, 100));
                    _top.setBackground(POKER_GREEN);

                    if(players[i].getCash() > 0) { // player is still in game
                        playerHand[0] = deck.draw();
                        playerHand[1] = deck.draw();

                        players[i].setCards(playerHand);

                        if(dealerNum == i) { //player is dealer
                            players[i].setRole(1);
                        } else if(sBlindNum == i) { //player is small blind
                            players[i].setRole(2);
                            bet(players[i], sBlindVal, true);
                        } else if(bBlindNum == i) { //player is big blind
                            players[i].setRole(3);
                            bet(players[i], bBlindVal, true);
                        } else {
                            players[i].setRole(0);
                        }

                        if(i == 0) {
                            players[i].getPlayerPanel().showCards(true);

                            _userPanel = userPanel();
                        } else {
                            PlayerPanel _playerPanel = new PlayerPanel(players[i], cardBack, false);
                            players[i].setPlayerPanel(_playerPanel);

                            _top.add(_playerPanel);
                        }

                        gameLogging.writeCardsFile(players[i]);

                        players[i].setPlayingHand(true);
                        System.out.println(players[i]);
                    } else { // player is out of cash, lost
                        players[i].setPlayingHand(false);
                        players[i].getPlayerPanel().setInactive();
                    }
                }

                _aiPlayersPanel.removeAll();
                _aiPlayersPanel.add(_top, BorderLayout.PAGE_START);
                // update panels
                _middlePanel.removeAll();
                _middlePanel.add(_potPanel, BorderLayout.LINE_START);
                _middlePanel.add(_communityPanel, BorderLayout.CENTER);
                _middlePanel.add(_userPanel, BorderLayout.LINE_END);

                revalidate();
                repaint();


                if(playersRemaining() > 1) {
                    playGameSwitch(1);
                } else { // find winner
                    Player winner = null;
                    for(int i =0; i < players.length; i++) {
                        if(players[i].getCash() > 0) {
                            winner = players[i];

                            winner(winner);

                        }
                    }
                }
            }
        });

        _buttonPanel.add(_nextHandButton);

        _actionPanel.removeAll();
        _actionPanel.add(_buttonPanel);

        //TODO: figure out why player hand gets hidden at end of turn, temp fix
        players[0].getPlayerPanel().showCards(true);
        
        if(playersRemaining() > 1) {
            playGameSwitch(1);
        } else { // find winner
            _nextHandButton.setText("End");
        }

        revalidate();
        repaint();
    }

    private void winner(Player player) {
        JLabel _winnerLabel = new JLabel();
        JButton _newgameButton = new JButton();

        JPanel _this = this;

        this.removeAll();

        _winnerLabel.setBackground(POKER_GREEN);
        _winnerLabel.setFont(new Font("Courier", Font.PLAIN, 60));
        _winnerLabel.setText(player.getName() + " has won the game!");

        _newgameButton.setFont(new Font("Courier", Font.PLAIN, 30));
        _newgameButton.setMaximumSize(new Dimension(400, 100));
        _newgameButton.setText("New game");
        _newgameButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                _this.removeAll();

                initalizeStartGrid();
            }
        });

        this.setLayout(new BorderLayout());
        this.add(_winnerLabel, BorderLayout.PAGE_START);
        this.add(_newgameButton, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    /**
     * Current very simple AI action, if user bets AI fold.
     * TODO: move to own class.
	 *
	 * If userAction is > 0, userAction = money bet by user, AI call.
	 * If userAction is = 0, userAction = user checked, AI checks.
	 * If userAction is = -1, userAction = user folded, AI folds.
     * @param userAction Action taken by user.
     */
    private void playAI(Action userAction) {
        Action aiAction = new Action();

        try { // If user is still in round, print their action
            printGameConsole(players[0].getName() + " " + userAction.toString());
        } catch(Exception e) {
            System.out.println(players[0].getName() + " didn't play");
            userAction = new Action();
        }

        for(int i = 1; i < players.length; i++) {
            
            if(players[i].isPlayingHand()) {
                // if user folds
                if(userAction.getValue() == -1) {
                    aiAction.setValue(0);
                }
                
                // if user bets 0 / checks
                if(userAction.getValue() == 0) {
                    aiAction.setValue(0);
                }
                
                // if user bets more than 0
                if(userAction.getValue() > 0) {
                    if(players[i].getCash() >= userAction.getValue()) {
                        bet(players[i], userAction.getValue(), false);
                        aiAction.setValue(userAction.getValue());
                    } else {
                        if (random.nextInt(5) <= 1){    //All in  (40% chance)
                            bet(players[i], players[i].getCash(), false);
                            aiAction.setValue(players[i].getCash());
                        } else {        //Fold
                        players[i].setPlayingHand(false);
                        aiAction.setValue(-1);
                        }
                    }
                }

                // print AI action
                gameLogging.writeActionFile(aiAction, players[i]);
                printGameConsole(players[i].getName() + " " + aiAction.toString());
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

    /**
     * Player bet, remove cash from player and add to pot.
     * @param player    Player who is betting.
     * @param amount    Amount of cash bet.
     * @param blindFlag Is this bet from the blind
     */
    private void bet(Player player, int amount, boolean blindFlag) {
        player.adjustCash(-amount);
        if (potCount == 1) {    //No side pots
            if (pots[0].getCurrentBet() <= amount || blindFlag) {     //Normal Bet
                pots[0].adjustPot(amount);
                player.setActivePot(0, true);
                pots[0].setHaveBetPot(0, true);
                _potPanel.adjustPot(0, amount);
            } else {                //create new sidepot
                createSidePot(player, amount);
                player.setActivePot(0, true);
            }

        } else {                //Side pots
            for (int i=0; i<potCount; i++){
                if (pots[i].getCurrentBet() <= amount || player.getActivePot(i)) {     //Normal Bet
                    pots[i].adjustPot(amount);
                    player.setActivePot(i, true);
                    pots[0].setHaveBetPot(i, true);
                    _potPanel.adjustPot(i, amount);
                } else {                //create new sidepot
                    createSidePot(player, amount);
                    player.setActivePot(i, true);
                }
            } 
        }       
    }

    /**
     * Create a side pot, since bet is less than minimum pot current bet,
     * which implies that the player is all in.
     * @param player Player who is betting.
     * @param amount Amount of cash bet.
     */
    private void createSidePot(Player player, int amount) {
        int numBet=0;
        int n=0;
        int sidePotVal;
        Player[] sidePotPlayers;
        potCount++;

        sidePotVal = pots[potCount-2].getCurrentBet() - amount;
        for (int i=0; i<players.length; i++){
            if ( pots[potCount-2].getHaveBetPot(i) ) {
                numBet++;
            }
        }
        sidePotPlayers = new Player[numBet];
        pots[potCount-2].adjustPot(-(sidePotVal*numBet));        //Remove excess money from main pot
        for (int i=0; i<players.length; i++){
            if ( pots[potCount-2].getHaveBetPot(i) ) {
                sidePotPlayers[n]=players[i];
                n++;
            }
        }
        pots[potCount-1] = new Pot(sidePotVal, sidePotVal-amount, sidePotPlayers, true);       //create new sidepot only certain players can win
        _potPanel.addPot(potCount-1, sidePotVal);
    }

    private void showAICards() {
        for(int i = 1; i < players.length; i++) {
            if(players[i].isPlayingHand() && players[i].getCash() >= 0) {
                players[i].getPlayerPanel().showCards(true);
                if(players[i].getCash() == 0) {
                    players[i].adjustCash(-1); // player lost, negative cash
                }
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
	private void selectDealer() {
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
			else if (dealerNum == players.length-1){
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
	private void selectSmallBlind() {
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
	private void selectBigBlind() {
		if (sBlindNum == (players.length - 1)){
			bBlindNum = 0;
		}
		else{
			bBlindNum = sBlindNum + 1;
        }
    }
    
    /*
	 * Selects the player with the small blind
	 * If dealerNum is the last player, sBlindNum = 0
	 * Otherwise sBlindNum is dealerNum + 1
	*/
	private void resetCurrentBet() {
		for (int i=0; i<potCount; i++){
            pots[i].adjustCurrentBet(0);
        }
	}
	
    
}