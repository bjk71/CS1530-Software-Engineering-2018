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
    private Pot      pot         = null;
    private int      potCount    = 1;
    private Card[]   tableCards  = null;
    private Card     cardBack    = null;
    private Logging  gameLogging = null;
    private Random   random      = new Random();

    private Action  playerAction = null;
    private boolean userAction   = false;
    private boolean nextHand     = false;
    
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
    private JButton  _nextHandButton = null;
 

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
    
    /**
     * Create new game objects and and initialize players.
     * @param userName Player name.
     * @param numOfAI  Number of AI players to initialize.
     */
    private void createNewGame(String userName, int numOfAI) {
        JPanel     _top          = new JPanel();
        JPanel     _bot          = new JPanel();
        JLabel     _tableLabel   = new JLabel("Pot: ");
        GameUtils  utils         = new GameUtils();
        String[]   aiPlayerNames = utils.getAINames(numOfAI, userName);
        
        try {
            Image back = ImageIO.read(this.getClass().getResource("/back.png"));
            cardBack = new Card("Back", back);
        } catch (IOException ioex) {
            System.exit(1);
        }

        deck       = new Deck();
        tableCards = new Card[5];
        players    = new Player[numOfAI + 1];
        pot        = new Pot(_potPanel, numOfAI + 1);
		
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
                players[i] = new Player(userName, playerHand, playerRole, playerStart, i);
                
                _userPanel = userPanel();

				gameLogging.writeCardsFile(players[i]);
            } else { // Initialize AI player
                String aiName = aiPlayerNames[i - 1];

                players[i]   = new Player(aiName, playerHand, playerRole, startingCash, i);
                				
				gameLogging.writeCardsFile(players[i]);

                
                _playerPanel = new PlayerPanel(players[i], cardBack, false);
                players[i].setPlayerPanel(_playerPanel);

                _top.add(_playerPanel);
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

        play();
    }

    /**
     * Start PlayThread.
     */
    private void play() {
        PlayThread thread = new PlayThread();

        thread.start();
    }

    /**
     * Looping thread that blocks while waiting for user action without freezing Swing UI.
     */
    public class PlayThread extends Thread {
        Action    minAction   = null;
        TurnTimer timer       = new TurnTimer(60);
        int       startIndex  = -1;
        int       index       = -1;
        boolean   keepPlaying = true;

        public void run() {
            while(keepPlaying) {
                Action minAction = null;

                // run one hand
                for(int i = 0; i < 4; i++) {
                    printGameConsole("<br>Round " + i + ":");

                    if(i == 0) {
                        // add blinds to pot
                        pot.newRound(dealerNum);
                        pot.bet(players[sBlindNum], sBlindVal);
                        pot.bet(players[bBlindNum], bBlindVal);
                        
                        startIndex = bBlindNum + 1;
                        if(startIndex == players.length) {
                            startIndex = 0;
                        }

                        minAction  = new Action(20);
                        
                        // print blind actions
                        printGameConsole(players[sBlindNum].getName() + " played $" + sBlindVal + " ante");
                        printGameConsole(players[bBlindNum].getName() + " played $" + bBlindVal + " ante");
                    } else {
                        startIndex = sBlindNum;
                        minAction  = new Action(0);

                        pot.newRound(dealerNum);
                    }

                    if( i > 0 && i < 4) {
                        _communityPanel.deal();
                    }

                    index = startIndex;
                    System.out.println("start index: " + startIndex);

                    // TODO: let big blind get chance to check first round

                    while(index != pot.getSetBet()) {
                        if(players[index].isPlayingHand()) {
                            if(index == 0) {
                                userTurn(minAction);
                                if(playerAction.isGreater(minAction)) {
                                    minAction = playerAction;
                                }
                            } else {
                                minAction = aiTurn(minAction, index);
                            }
                        }
                        index++;
                        if(index >= players.length) {
                            index = 0;
                        }
                        
                        // sleep to watch user bets take place
                        try{
                            Thread.sleep(700);
                        } catch(InterruptedException ex){
                            Thread.currentThread().interrupt();
                        }
                    }
                }

                endOfHand();
                nextHand();

                if(playersRemaining() > 1) {
                    keepPlaying = true;; // Start next hand
                } else { // find winner
                    _nextHandButton.setText("End");
                    keepPlaying = false;
                }
            }
        }

        /**
         * Run simple AI logic based in the minimum action they must take and return
         * updated AI action.
         * TODO: pass in AI player object directly instead of playerIndex.
         * @param  minAction   Minimum action the AI must match (or fold).
         * @param  playerIndex Index of AI player, used to access player in players[] array.
         * @return             Action the AI took.
         */
        private Action aiTurn(Action minAction, int playerIndex) {
            Action aiAction = new Action();
            Action thisBet  = new Action(); // use only for printing
            Player aiPlayer = players[playerIndex];
            int    aiRandom = new Random().nextInt(20);
            int    betRemaining = minAction.getValue() - pot.getPlayerBet(playerIndex);

            if(aiPlayer.isPlayingHand()) {
                // if minAction is fold
                if(betRemaining == -1) {
                    // decide using aiRandom
                    if(aiRandom > 0) {
                        aiAction.setValue(0);
                    } else {
                        aiAction.setValue(20);
                    }
                }
                
                // if minAction is check
                if(betRemaining == 0) {
                    // decide using aiRandom
                    if(aiRandom > 7) {
                        aiAction.setValue(0);
                    } else {
                        aiAction.setValue(20);
                    }
                }
                
                // if minAction is bet 
                if(betRemaining > 0) {
                    // ai has enough cash to call
                    if(aiPlayer.getCash() >= betRemaining) {
                        // decide using aiRandom
                        if(aiRandom > 17) {
                            aiAction.setValue(betRemaining + 20);
                        } else if(betRemaining > 100 && aiRandom < 14) {
                            // ai quits! too expensive
                            aiPlayer.setPlayingHand(false);
                            aiAction.setValue(-1);
                        } else if(aiRandom == 0) {
                            aiAction.setValue(betRemaining + 100);
                        } else {
                            aiAction.setValue(betRemaining);
                        }
                    } else {
                        // ai doesn't have enough cash, fold
                        aiPlayer.setPlayingHand(false);
                        aiAction.setValue(-1);
                    }
                }

                thisBet  = aiAction;
                aiAction = pot.bet(aiPlayer, aiAction.getValue());

                if(aiAction.isGreater(minAction)) {
                    minAction = aiAction;
                }

                // print AI action
                gameLogging.writeActionFile(aiAction, aiPlayer);
                printGameConsole(aiPlayer.getName() + " " + thisBet.toString() + ", total " + aiAction.getValue());
            }

            return minAction;
        }

        private void userTurn(Action minAction) {
            addUserActionListeners(minAction);

            while(!userAction) { // loop until change
                try{
                    Thread.sleep(500);
                } catch(InterruptedException ex){
                    Thread.currentThread().interrupt();
                }
            }
            userAction = false;
        }

        /**
         * Add actionListeners to user input buttons.
         * TODO: disable buttons in between player's turns.
         */
        private void addUserActionListeners(Action minAction) {
            ActionListener[] alList   = null;
            SpinnerModel     betModel = null;
            int              minBet   = 20;

            // remove old action listeners
            alList = _betButton.getActionListeners();
            for(int i = 0; i < alList.length; i++) {
                _betButton.removeActionListener(alList[i]);
            }

            alList = _checkButton.getActionListeners();
            for(int i = 0; i < alList.length; i++) {
                _checkButton.removeActionListener(alList[i]);
            }

            alList = _foldButton.getActionListeners();
            for(int i = 0; i < alList.length; i++) {
                _foldButton.removeActionListener(alList[i]);
            }

            // enable buttons
            setButtons(true);

            System.out.printf("min action: %d, getPlayerBet: %d\n", minAction.getValue(), pot.getPlayerBet(0));

            if(minAction.getValue() >= minBet && minAction.getValue() <= players[0].getCash()) {
                minBet = minAction.getValue() - pot.getPlayerBet(0);
            } else if(minAction.getValue() > players[0].getCash()) { // player doesn't have enough cash to call, must go all in
                minBet = players[0].getCash();
            }
            betModel = new SpinnerNumberModel(minBet, minBet, players[0].getCash(), 10);

            _betSpinner.setModel(betModel);

            final int minimumBet = minBet;

            _betButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int betAmount = (int) _betSpinner.getValue();

                    if(betAmount < minimumBet) {
                        _betSpinner.setValue(minAction.getValue());
                    } else if(betAmount > players[0].getCash()) {
                        _betSpinner.setValue(players[0].getCash());
                    }else {
                        pot.bet(players[0], betAmount);

                        playerAction = new Action(pot.getPlayerBet(0));

                        userAction = true;

                        // disable buttons
                        setButtons(false);

                        // print user action
                        gameLogging.writeActionFile(playerAction, players[0]);
                        printGameConsole(players[0].getName() + " " + new Action(betAmount).toString() + ", total " + playerAction.getValue());

                    }
                    
                    revalidate();
                    repaint();
                }
            });

            if(minAction.getValue() == 0) {
                _checkButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        playerAction = new Action(0);

                        pot.bet(players[0], 0);
                        
                        userAction = true;

                        // disable buttons
                        setButtons(false);

                        // print user action
                        gameLogging.writeActionFile(playerAction, players[0]);
                        printGameConsole(players[0].getName() + " " + playerAction.toString());
                    }
                });

                _checkButton.setEnabled(true);
            } else {
                _checkButton.setEnabled(false);

                revalidate();
                repaint(); 
            }
            
            _foldButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    playerAction = new Action(-1);

                    pot.bet(players[0], -1);

                    userAction = true;

                    // disable buttons
                    setButtons(false);

                    // print user action
                    gameLogging.writeActionFile(playerAction, players[0]);
                    printGameConsole(players[0].getName() + " " + playerAction.toString());
                }
            });
        }

        /**
         * Enabled or disable buttons based on passed in boolean.
         * @param state Boolean value to set setEnabled of buttons to.
         */
        private void setButtons(boolean state) {
            _betButton.setEnabled(state);
            _checkButton.setEnabled(state);
            _foldButton.setEnabled(state);
        }
    }

    /**
     * Display the next hand button and block the PlayThread until pressed.
     */
    private void nextHand() {
        nextHandButton();

        while(!nextHand) {
            // block
            try{
                Thread.sleep(500);
            } catch(InterruptedException ex){
                Thread.currentThread().interrupt();
            }
        }

        nextHand = false;
    }

    /**
     * Add user panel components.
     * @return Created panel to be added to container.
     */
    private JPanel userPanel() {
        JPanel       _userPanel     = new JPanel();
        JPanel       _centeredPanel = new JPanel();
        JPanel       _btnPanel      = new JPanel(new FlowLayout());
        PlayerPanel  _playerPanel   = new PlayerPanel(players[0], cardBack, true);
        SpinnerModel betModel       = new SpinnerNumberModel(20, 20, players[0].getCash(), 1);
        
        _betSpinner = new JSpinner(betModel);

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
        _betButton.setEnabled(false);
        
        _checkButton.setText("Check");
        _checkButton.setFont(new Font("Courier", Font.PLAIN, 22));
        _checkButton.setPreferredSize(new Dimension(100, 60));
        _checkButton.setEnabled(false);
        
        _foldButton.setText("Fold");
        _foldButton.setFont(new Font("Courier", Font.PLAIN, 22));
        _foldButton.setPreferredSize(new Dimension(100, 60));
        _foldButton.setEnabled(false);
        
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
        // nextHandButton();
    }

    private void nextHandButton() {
        JPanel  _buttonPanel    = new JPanel();

        _nextHandButton = new JButton("Deal next hand");

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
                        } else if(bBlindNum == i) { //player is big blind
                            players[i].setRole(3);
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

                        // writeCardsFile(players[i]);

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

                // writeHandFile();

                if(playersRemaining() > 1) {
                    // playGameSwitch(1);
                } else { // find winner
                    Player winner = null;
                    for(int i =0; i < players.length; i++) {
                        if(players[i].getCash() > 0) {
                            winner = players[i];

                            winner(winner);

                        }
                    }
                }

                nextHand = true;
            }
        });

        _buttonPanel.add(_nextHandButton);

        _actionPanel.removeAll();
        _actionPanel.add(_buttonPanel);

        //TODO: figure out why player hand gets hidden at end of turn, temp fix
        players[0].getPlayerPanel().showCards(true);
        
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
     * Iterate through array of AI players and display the card stored 
     * in their PlayerPanel object.
     */
    private void showAICards() {
        for(int i = 1; i < players.length; i++) {
            if(players[i].isPlayingHand() && players[i].getCash() >= 0) {
                players[i].getPlayerPanel().showCards(true);
                if(players[i].getCash() == 0) {
                    players[i].adjustCash(-1, true); // player lost, negative cash
                }
            }
        }

        revalidate();
        repaint(); 
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

        System.out.printf("Dealer num: %d, sb num: %d, bb num: %d\n", dealerNum, sBlindNum, bBlindNum);
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
    
    /**
     * Print user actions and other game information to game console in
     * bottom right corner of Game panel.
     * @param line String to format and print.
     */
    private void printGameConsole(String line) {
        String        currentText = _gameLogLabel.getText().replace("</html>", "<br/>");
        StringBuilder lineBuilder = new StringBuilder(currentText);
        JScrollBar    _scrollBar  = _scrollPane.getVerticalScrollBar();
    
        lineBuilder.append(line);
        lineBuilder.append("</html>");
    
        _gameLogLabel.setText(lineBuilder.toString());
        
        _scrollPane.revalidate();
        _scrollPane.repaint();
    
        revalidate();
        repaint();
    
        _scrollBar.setValue(_scrollBar.getMaximum());
    }
}