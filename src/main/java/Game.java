import java.awt.*;
import javax.swing.*;

import java.io.*;
import java.util.*;
import javax.imageio.*;
import java.awt.event.*;

import javax.swing.text.DefaultCaret;

public class Game extends JPanel implements Serializable {
    private static final long serialVersionUID = 42L;

    private final Color POKER_GREEN  = new Color(71, 113, 72);
    private final Color POKER_DARK   = new Color(47, 89, 49);
    private final Color WHITE        = new Color(255, 255, 255);
    private final int   sBlindVal    = 10;
    private final int   bBlindVal    = 20;
    private final int   startingCash = 1000;
    private final int   playerStart  = 1000;
    private final int   PLAYER_INDEX = 0;       //Index of Human Player

    private Player[] players      = null;
    private Deck     deck         = null;
    private Pot      pot          = null;
    private int      timerSetting = -1;
    private boolean  trainingGame = false;
    private Card[]   tableCards   = null;
    private Card     cardBack     = null;
    private Logging  gameLogging  = null;
    
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
    public  InfoPanel   _infoPanel      = new InfoPanel();

    private JTextArea   _scrollTextArea = new JTextArea();


    // Action panel components
    private JPanel   _actionPanel    = new JPanel();
    private JButton  _betButton      = new JButton();
    private JButton  _checkButton    = new JButton();
    private JButton  _foldButton     = new JButton();
    private JSpinner _betSpinner     = null;
    private JButton  _nextHandButton = null;


    public Game() {
        initializeStartGrid();
        setVisible(true);
    }
    

    public Game(File saveFile) {
      // initialize game settings from save file
    }

    /**
     * Reinitializes all images from a saved game
     */
    public void reinitImages() {
        _communityPanel.reinitImages();
        for (Player player : players){
            player.reinitImages();
        }
        reinitCardBack();

    }

    
    private void initializeStartGrid(){    
        JPanel             _startTitle        = new JPanel();
        JPanel             _playerName        = new JPanel();
        JPanel             _numOpponents      = new JPanel();
        JPanel             _timerMode         = new JPanel();
        JPanel             _trainingMode      = new JPanel();
        JPanel             _startButton       = new JPanel();
        JPanel             _playerNamePanel   = new JPanel();
        JPanel             _numOpponentsPanel = new JPanel();
        JPanel             _timerModePanel    = new JPanel();
        JPanel             _trainingModePanel = new JPanel();

        JLabel             _titleLabel        = new JLabel();        
        JButton            _startGame         = new JButton();
        JLabel             _playerNameLabel   = new JLabel();
        JTextField         _playerNameInput   = new JTextField();
        JLabel             _numOppLabel       = new JLabel();
        JComboBox<Integer> _numOpp            = new JComboBox<>();
        JLabel             _timerLabel        = new JLabel();
        JComboBox<String>  _timerOptions      = new JComboBox<>();
        JLabel             _trainingLabel     = new JLabel();
        JComboBox<String>  _trainingOptions   = new JComboBox<>();


        _playerNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        _numOpponentsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        _timerModePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        _trainingModePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        setLayout(new GridLayout(6,1));
        
        _playerName.setLayout(new GridLayout(1, 2, 50, 100));
        _numOpponents.setLayout(new GridLayout(1, 2, 50, 100));
        _timerMode.setLayout(new GridLayout(1, 2, 50, 100));
        _trainingMode.setLayout(new GridLayout(1, 2, 50, 100));
        
        _startTitle.setBackground(POKER_GREEN);
        _playerName.setBackground(POKER_GREEN);
        _numOpponents.setBackground(POKER_GREEN);
        _timerMode.setBackground(POKER_GREEN);
        _trainingMode.setBackground(POKER_GREEN);
        _startButton.setBackground(POKER_GREEN);
        _playerNamePanel.setBackground(POKER_GREEN);
        _numOpponentsPanel.setBackground(POKER_GREEN);
        _timerModePanel.setBackground(POKER_GREEN);
        _trainingModePanel.setBackground(POKER_GREEN);
        
        _titleLabel.setForeground(WHITE);
        _titleLabel.setFont(new Font("Courier", Font.PLAIN, 60));
        _titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        _titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        _titleLabel.setText("Start a New Game!");
        
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

        _timerLabel.setForeground(WHITE);
        _timerLabel.setFont(new Font("Courier", Font.PLAIN, 40));
        _timerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        _timerLabel.setVerticalAlignment(SwingConstants.TOP);
        _timerLabel.setText("Time limit to take your turn:");

        _timerOptions.setModel(new DefaultComboBoxModel<>(new String[] { "No Limit", "15 Seconds", "30 Seconds", "1 Minute" }));        
        _timerOptions.setPreferredSize(new Dimension( 215, 50 ));
        _timerOptions.setFont(new Font("Courier", Font.PLAIN, 30));
        _timerModePanel.add(_timerOptions);

        _trainingLabel.setForeground(WHITE);
        _trainingLabel.setFont(new Font("Courier", Font.PLAIN, 40));
        _trainingLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        _trainingLabel.setVerticalAlignment(SwingConstants.TOP);
        _trainingLabel.setText("Training Mode:");

        _trainingOptions.setModel(new DefaultComboBoxModel<>(new String[] { "No", "Yes" }));        
        _trainingOptions.setPreferredSize(new Dimension( 215, 50 ));
        _trainingOptions.setFont(new Font("Courier", Font.PLAIN, 30));
        _trainingModePanel.add(_trainingOptions);        
        
        _startGame.setText("Start Game");
        _startGame.setFont(new Font("Courier", Font.PLAIN, 30));
        _startGame.setPreferredSize(new Dimension( 300, 100 ));
        _startGame.setVerticalAlignment(SwingConstants.CENTER);
        _startGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: REGEX user input
                String  name            = _playerNameInput.getText();
                int     numOpponents    = (int) _numOpp.getSelectedItem();
                String  timerSelect     = (String) _timerOptions.getSelectedItem();
                String  trainingSelect  = (String) _trainingOptions.getSelectedItem();
                int     timerValue;
                boolean trainingMode;
                if ( timerSelect.equals("15 Seconds") ) {
                    timerValue = 15;
                } else if ( timerSelect.equals("30 Seconds") ) {
                    timerValue = 30;
                } else if ( timerSelect.equals("1 Minute") ) {
                    timerValue = 60;
                } else {
                    timerValue = -1;
                }
                if ( trainingSelect.equals("Yes")){
                    trainingMode = true;
                } else {
                    trainingMode = false;
                }

                removeAll();
                createNewGame(name, numOpponents, timerValue, trainingMode);
            }
        });
        
        _startTitle.add(_titleLabel);
        _playerName.add(_playerNameLabel);
        _playerName.add(_playerNamePanel);
        _numOpponents.add(_numOppLabel);
        _numOpponents.add(_numOpponentsPanel);
        _timerMode.add(_timerLabel);
        _timerMode.add(_timerModePanel);
        _trainingMode.add(_trainingLabel);
        _trainingMode.add(_trainingModePanel);

        _startButton.add(_startGame);
        
        add(_startTitle);
        add(_playerName);
        add(_numOpponents);
        add(_timerMode);
        add(_trainingMode);
        add(_startButton);       

        revalidate();
        repaint();
    }

    /**
     * Create new game objects and and initialize players.
     * @param userName   Player name.
     * @param numOfAI    Number of AI players to initialize.
     * @param timerValue No timer-mode if -1, else time limit in seconds
     */
    private void createNewGame(String userName, int numOfAI, int timerValue, boolean trainingMode) {
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

        deck         = new Deck();
        tableCards   = new Card[5];
        players      = new Player[numOfAI + 1];
        pot          = new Pot(_potPanel, numOfAI + 1);
        timerSetting = timerValue;
        trainingGame = trainingMode;
		
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

            if(i == PLAYER_INDEX) { // Initialize human player
                players[i] = new Player(userName, playerHand, playerRole, playerStart, i);
                
                _userPanel = userPanel();
                if (trainingGame) {     //Pre-flop hand strength
                    _infoPanel.setRelativeLabel(new GameUtils().getHandStrength(players[PLAYER_INDEX].getCards()));;
                }

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

        // check to add timer panel
        if(timerSetting > 0 || trainingGame) {
            JPanel _holderPanel   = new JPanel();
            JPanel _centeredPanel = new JPanel();

            _centeredPanel.setBackground(POKER_GREEN);
            _centeredPanel.setLayout(new GridBagLayout());

            _centeredPanel.add(_infoPanel);

            _holderPanel.setLayout(new BorderLayout());
            _holderPanel.add(_userPanel, BorderLayout.CENTER);
            _holderPanel.add(_centeredPanel, BorderLayout.LINE_END);

            _middlePanel.add(_holderPanel, BorderLayout.LINE_END);
        } else {
            JPanel _tempPanel    = new JPanel();
            JPanel _paddingPanel = new JPanel();

            _paddingPanel.setBackground(POKER_GREEN);
            _paddingPanel.setPreferredSize(new Dimension(150, Integer.MAX_VALUE));

            _tempPanel.setLayout(new BorderLayout());
            _tempPanel.add(_userPanel, BorderLayout.CENTER);
            _tempPanel.add(_paddingPanel, BorderLayout.LINE_END);

            _middlePanel.add(_tempPanel, BorderLayout.LINE_END);
        }


        _gameLogLabel.setText("<html>Game Log:<br/></html>");
        _gameLogLabel.setForeground(Color.WHITE);
        _gameLogLabel.setBackground(POKER_DARK);
        _gameLogLabel.setOpaque(true);
        _gameLogLabel.setVerticalAlignment(SwingConstants.TOP);

        _scrollPane = new JScrollPane(_scrollTextArea);

        _scrollTextArea.setBackground(POKER_DARK);
        _scrollTextArea.setForeground(Color.WHITE);
        _scrollTextArea.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        _scrollTextArea.setEditable(false);

        // _scrollPane = new JScrollPane(_gameLogLabel);
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
    public void play() {
        PlayThread thread = new PlayThread();

        thread.start();
    }

    /**
     * Looping thread that blocks while waiting for user action without freezing Swing UI.
     */
    public class PlayThread extends Thread {
        Action    minAction   = null;
        int       startIndex  = -1;
        int       index       = -1;
        boolean   keepPlaying = true;
        boolean   firstHand   = false;

        public void run() {
            while(keepPlaying) {
                Action minAction = null;

                // run one hand
                for(int i = 0; i < 4; i++) {

                    if(playersPlaying() == 1) {
                        break;
                    }

                    if(i > 0) {
                        printGameConsole("\n");
                    }

                    printGameConsole("Round " + (i+1) + ":");

                    if(i == 0) {
                        //initialize pot
                        pot.newHand(players);
                        pot.newRound(dealerNum);

                        //add blinds to pot
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

                        firstHand = true;
                    } else {
                        startIndex = sBlindNum;
                        minAction  = new Action(0);

                        pot.newRound(dealerNum);
                    }

                    if( i > 0 && i < 4) {
                        _communityPanel.deal();
                    }

                    index = startIndex;

                    while(index != pot.getSetBet()) {
                        if(players[index].isPlayingHand()) {
                            if(index == PLAYER_INDEX) {
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
                        if(players[0].isPlayingHand()) {
                            try{
                                Thread.sleep(500);
                            } catch(InterruptedException ex){
                                Thread.currentThread().interrupt();
                            }
                        }

                        // first round big blind can bet again
                        if(index == pot.getSetBet() && firstHand && players[index].getRole() == 3) {
                            Action startAction = minAction;

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

                            firstHand = false;

                            // if checked, break loop
                            if(startAction.getValue() == minAction.getValue()) {
                                break;
                            }
                        }
                    }
                }

                endOfHand();
                nextHand();

				if(players[0].getCash() == 0) {
					_nextHandButton.setText("End");
					keepPlaying = false;
				}
                else if(playersRemaining() > 1) {
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
                        if(aiRandom > 17 && aiPlayer.getCash() >= betRemaining + 20) {
                            aiAction.setValue(betRemaining + 20);
                        } else if(betRemaining > 100 && aiRandom < 14) {
                            // ai quits! too expensive
                            aiPlayer.setPlayingHand(false);
                            aiAction.setValue(-1);
                        } else if(aiRandom == 0 && aiPlayer.getCash() >= betRemaining + 100) {
                            aiAction.setValue(betRemaining + 100);
                        } else {
                            aiAction.setValue(betRemaining);
                        }
                    } else {
                        // ai calls, all in
                        if(aiRandom > 15) {
                            aiAction.setValue(aiPlayer.getCash());
                        } else {
                            // ai doesn't have enough cash, fold
                            aiPlayer.setPlayingHand(false);
                            aiAction.setValue(-1);
                        }
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

            TurnTimer timer = null;
            if(timerSetting > 0) {
                timer = new TurnTimer(timerSetting);
                timer.start();
            }

            if (trainingGame) {
                _infoPanel.setOddsLabel(new GameUtils().getPotOdds(pot.getValue(), minAction.getValue()-pot.getPlayerBet(PLAYER_INDEX)));
            }

            while(!userAction) { // loop until change
                try{
                    Thread.sleep(500);
                } catch(InterruptedException ex){
                    Thread.currentThread().interrupt();
                }
            }

            userAction = false;

            if( timer != null && !timer.isInterrupted() ) {
                timer.interrupt();
                _infoPanel.setTimerText("Time: --");
                _infoPanel.setOddsLabel("Pot Odds: --");
            }
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

            System.out.printf("min action: %d, getPlayerBet: %d\n", minAction.getValue(), pot.getPlayerBet(PLAYER_INDEX));

            if(minAction.getValue() >= minBet && minAction.getValue() <= players[PLAYER_INDEX].getCash()) {
                minBet = minAction.getValue() - pot.getPlayerBet(PLAYER_INDEX);
            } else if(minAction.getValue() > players[PLAYER_INDEX].getCash()) { // player doesn't have enough cash to call, must go all in
                minBet = players[PLAYER_INDEX].getCash();
            }
            betModel = new SpinnerNumberModel(minBet, minBet, players[PLAYER_INDEX].getCash(), 10);

            _betSpinner.setModel(betModel);
            _betSpinner.requestFocus();

            final int minimumBet = minBet;

            _betButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int betAmount = (int) _betSpinner.getValue();

                    if(betAmount < minimumBet) {
                        _betSpinner.setValue(minAction.getValue());
                    } else if(betAmount > players[PLAYER_INDEX].getCash()) {
                        _betSpinner.setValue(players[PLAYER_INDEX].getCash());
                    }else {
                        pot.bet(players[PLAYER_INDEX], betAmount);

                        playerAction = new Action(pot.getPlayerBet(PLAYER_INDEX));

                        userAction = true;

                        // disable buttons
                        setButtons(false);

                        // print user action
                        gameLogging.writeActionFile(playerAction, players[PLAYER_INDEX]);
                        printGameConsole(players[PLAYER_INDEX].getName() + " " + new Action(betAmount).toString() + ", total " + playerAction.getValue());

                    }
                    
                    revalidate();
                    repaint();
                }
            });

            if(minAction.getValue() == 0) {
                _checkButton.setText("Check");

                _checkButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        playerAction = new Action(0);

                        pot.bet(players[PLAYER_INDEX], 0);
                        
                        userAction = true;

                        // disable buttons
                        setButtons(false);

                        // print user action
                        gameLogging.writeActionFile(playerAction, players[PLAYER_INDEX]);
                        printGameConsole(players[PLAYER_INDEX].getName() + " " + playerAction.toString());
                    }
                });

                _checkButton.setEnabled(true);
            } else {
                _checkButton.setText("Call");

                _checkButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        playerAction = new Action(minimumBet);

                        pot.bet(players[0], minimumBet);
                        
                        userAction = true;

                        // disable buttons
                        setButtons(false);

                        // print user action
                        gameLogging.writeActionFile(playerAction, players[0]);
                        printGameConsole(players[0].getName() + " " + playerAction.toString() + ", total " + new Action(pot.getPlayerBet(0)).getValue());
                    }
                });

                revalidate();
                repaint(); 
            }
            
            _foldButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    playerAction = new Action(-1);

                    pot.bet(players[PLAYER_INDEX], -1);

                    userAction = true;

                    // disable buttons
                    setButtons(false);

                    // print user action
                    gameLogging.writeActionFile(playerAction, players[PLAYER_INDEX]);
                    printGameConsole(players[PLAYER_INDEX].getName() + " " + playerAction.toString());
                }
            });
        }
    }

    /**
     * Thread that lets the user know how much time they have left in their turn
     */
    public class TurnTimer extends Thread {
        private int timer = 0;
    
        public TurnTimer(int count) {
            timer = count;
        }
    
        public void run() {
            while(timer > 0) {
                if(timer >= 10) {
                    _infoPanel.setTimerText("Time: " + timer);
                } else {
                    _infoPanel.setTimerText("Time: 0" + timer);
                }

                if(timer == 1) {
                    try {
                        Thread.sleep(500);
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        return;
                    }

                    // disable buttons - technically user only has timer - 0.5 seconds to make decision
                    setButtons(false);

                    try {
                        Thread.sleep(500);
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                timer--;
            }

            _infoPanel.setTimerText("FOLD");

            try {
                Thread.sleep(2000);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
                return;
            }

            playerAction = new Action(-1);

            pot.bet(players[PLAYER_INDEX], -1);

            userAction = true;

            // print user action
            gameLogging.writeActionFile(playerAction, players[PLAYER_INDEX]);
            printGameConsole(players[PLAYER_INDEX].getName() + " " + playerAction.toString());
            return;
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
                Thread.sleep(250);
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
        PlayerPanel  _playerPanel   = new PlayerPanel(players[PLAYER_INDEX], cardBack, true);
        SpinnerModel betModel       = new SpinnerNumberModel(20, 20, players[0].getCash(), 1);
        
        _betSpinner = new JSpinner(betModel);

        players[PLAYER_INDEX].setPlayerPanel(_playerPanel); // Save panel to Player object

        _userPanel.setBackground(POKER_GREEN);
        _userPanel.setPreferredSize(new Dimension(400, Integer.MAX_VALUE));
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
        String result = "";
        // check for sidepots
        if(pot.numberOfPots() > 1) {
            // re-enable players who started a sidepot
            pot.addAllInPlayers(players);

            // distribute sidepots
            for(int i = 1; i < pot.numberOfPots(); i++) {
                // make players array
                int[] playersInPot = pot.getPlayersInPot(i);
                Player[] playerArray = new Player[playersInPot.length];
                for(int j = 0; j < playersInPot.length; j++) {
                    playerArray[j] = players[playersInPot[j]];
                }
                result = "Side Pot " + i + ": ";
                result += new GameUtils().determineBestHand(playerArray, tableCards, pot.clearPot(i), false);
                printGameConsole(result);
            }

            result = "Main Pot: ";

            try { // show remaining table cards
                _communityPanel.deal();
                _communityPanel.deal();
                _communityPanel.deal();
            } catch(Exception e) {}
        }

        result += new GameUtils().determineBestHand(players, tableCards, pot.clearPot(0), true);
        printGameConsole(result + "\n\n");

        //Display cards that made up winning hand (as well as show the opponents' cards in general)
        _communityPanel.showWinningCards();
        showAICards();
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

                        players[i].setCards(playerHand, true);

                        if(dealerNum == i) { //player is dealer
                            players[i].setRole(1);
                        } else if(sBlindNum == i) { //player is small blind
                            players[i].setRole(2);
                        } else if(bBlindNum == i) { //player is big blind
                            players[i].setRole(3);
                        } else {
                            players[i].setRole(0);
                        }

                        if(i == PLAYER_INDEX) {
                            players[i].getPlayerPanel().showCards(true);
                            if (trainingGame) {     //Pre-flop hand strength
                                _infoPanel.setRelativeLabel(new GameUtils().getHandStrength(players[PLAYER_INDEX].getCards()));;
                            }

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
                
                // check to add timer panel
                if(timerSetting > 0 || trainingGame) {
                    JPanel _holderPanel   = new JPanel();
                    JPanel _centeredPanel = new JPanel();

                    _centeredPanel.setBackground(POKER_GREEN);
                    _centeredPanel.setLayout(new GridBagLayout());

                    _centeredPanel.add(_infoPanel);

                    _holderPanel.setLayout(new BorderLayout());
                    _holderPanel.add(_userPanel, BorderLayout.CENTER);
                    _holderPanel.add(_centeredPanel, BorderLayout.LINE_END);

                    _middlePanel.add(_holderPanel, BorderLayout.LINE_END);
                } else {
                    JPanel _tempPanel    = new JPanel();
                    JPanel _paddingPanel = new JPanel();

                    _paddingPanel.setBackground(POKER_GREEN);
                    _paddingPanel.setPreferredSize(new Dimension(150, Integer.MAX_VALUE));

                    _tempPanel.setLayout(new BorderLayout());
                    _tempPanel.add(_userPanel, BorderLayout.CENTER);
                    _tempPanel.add(_paddingPanel, BorderLayout.LINE_END);

                    _middlePanel.add(_tempPanel, BorderLayout.LINE_END);
                }

                revalidate();
                repaint();

                // writeHandFile();
				
				if(players[0].getCash() == 0) {
					loser();
				}
                if(playersRemaining() > 1) {
                    // playGameSwitch(1);
                } 
				else { // find winner
                    Player winner = null;
                    for(int i =0; i < players.length; i++) {
                        if(players[i].getCash() > 0) {
                            winner = players[i];
							
                            winner();

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
        players[PLAYER_INDEX].getPlayerPanel().showCards(true);
        
		if(players[0].getCash() == 0) {
			_nextHandButton.setText("End");
		}
        else if(playersRemaining() > 1) {
            // playGameSwitch(1);
        } else { // find winner
            _nextHandButton.setText("End");
        }

        revalidate();
        repaint();
    }

    private void winner() {
		
		JPanel _this = this;

        this.removeAll();

		JPanel		_panel				= new JPanel();
		JPanel		_imgPanel			= new JPanel();
		JPanel		_buttonPanel		= new JPanel();
		
		
		JLabel 		_winnerLabel 		= new JLabel();
        JLabel 		_congratsLabel 		= new JLabel();
        JLabel 		_imgLabel   		= null;
		JButton 	_mainMenuButton 	= new JButton();
		JButton 	_exitButton 		= new JButton();
		

		//paints the new screen
		setLayout(new GridLayout(4, 1));

        try {
            Image img = ImageIO.read(this.getClass().getResource("/Winner.png"));
            _imgLabel = new JLabel(new ImageIcon(img.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH)));
        } catch (IOException ioex) {
            System.exit(1);
        }

        _imgLabel.setHorizontalAlignment(JLabel.CENTER);
        _imgLabel.setVerticalAlignment(JLabel.BOTTOM);
		_imgPanel.add(_imgLabel);

        _congratsLabel.setForeground(WHITE);
        _congratsLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 36));
        _congratsLabel.setHorizontalAlignment(JLabel.CENTER);
        _congratsLabel.setVerticalAlignment(JLabel.TOP);
        _congratsLabel.setText("You Did It!");
        _panel.add(_congratsLabel);

        _winnerLabel.setForeground(WHITE);
        _winnerLabel.setFont(new Font(Font.SERIF, Font.PLAIN, 60));
        _winnerLabel.setHorizontalAlignment(JLabel.CENTER);
        _winnerLabel.setVerticalAlignment(JLabel.BOTTOM);
        _winnerLabel.setText("You Have Won the Game!");

		_mainMenuButton.setText("Main Menu");
        _mainMenuButton.setFont(new Font("Courier", Font.PLAIN, 30));
        _mainMenuButton.setPreferredSize(new Dimension( 300, 100 ));
        _mainMenuButton.setVerticalAlignment(SwingConstants.CENTER);
        _mainMenuButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                
				_this.removeAll();
				
				//calls restartGame class from GameWindow that acts the same way as the Exit button
                GameWindow.restartGame();
            }
        });
		_buttonPanel.add(_mainMenuButton);
		
		_exitButton.setText("Exit");
		_exitButton.setFont(new Font("Courier", Font.PLAIN, 30));
        _exitButton.setPreferredSize(new Dimension( 300, 100 ));
        _exitButton.setVerticalAlignment(SwingConstants.CENTER);
        _exitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
				
                System.exit(0);
            }
        });
		_buttonPanel.add(_exitButton);
		
        setBackground(POKER_GREEN);
        _panel.setBackground(POKER_GREEN);
		_buttonPanel.setBackground(POKER_GREEN);
		_imgPanel.setBackground(POKER_GREEN);
		
        add(_winnerLabel, BorderLayout.PAGE_START);
		add(_imgPanel);
        add(_panel, BorderLayout.CENTER);
		add(_buttonPanel);

		
		
		
        revalidate();
        repaint();
    }
	
	private void loser() {
		
        JPanel _this = this;

        this.removeAll();

		JPanel		_panel				= new JPanel();
		JPanel		_imgPanel			= new JPanel();
		JPanel		_buttonPanel		= new JPanel();
		
		
		JLabel 		_loserLabel 		= new JLabel();
        JLabel 		_encourageLabel 	= new JLabel();
        JLabel 		_imgLabel   		= null;
		JButton 	_mainMenuButton 	= new JButton();
		JButton 	_exitButton 		= new JButton();
		
		//paints the new screen
		setLayout(new GridLayout(4, 1));

        try {
            Image img = ImageIO.read(this.getClass().getResource("/Loser.png"));
            _imgLabel = new JLabel(new ImageIcon(img.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH)));
        } catch (IOException ioex) {
            System.exit(1);
        }

        _imgLabel.setHorizontalAlignment(JLabel.CENTER);
        _imgLabel.setVerticalAlignment(JLabel.BOTTOM);
		_imgPanel.add(_imgLabel);

        _encourageLabel.setForeground(WHITE);
        _encourageLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 36));
        _encourageLabel.setHorizontalAlignment(JLabel.CENTER);
        _encourageLabel.setVerticalAlignment(JLabel.TOP);
        _encourageLabel.setText("Better Luck Next Time.");
        _panel.add(_encourageLabel);

        _loserLabel.setForeground(WHITE);
        _loserLabel.setFont(new Font(Font.SERIF, Font.PLAIN, 60));
        _loserLabel.setHorizontalAlignment(JLabel.CENTER);
        _loserLabel.setVerticalAlignment(JLabel.BOTTOM);
        _loserLabel.setText("You Have Lost the Game!");

		_mainMenuButton.setText("Main Menu");
        _mainMenuButton.setFont(new Font("Courier", Font.PLAIN, 30));
        _mainMenuButton.setPreferredSize(new Dimension( 300, 100 ));
        _mainMenuButton.setVerticalAlignment(SwingConstants.CENTER);
        _mainMenuButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
				
                _this.removeAll();
				
				//calls restartGame class from GameWindow that acts the same way as the Exit button
				GameWindow.restartGame();
			
            }
        });
		_buttonPanel.add(_mainMenuButton);
		
		_exitButton.setText("Exit");
		_exitButton.setFont(new Font("Courier", Font.PLAIN, 30));
        _exitButton.setPreferredSize(new Dimension( 300, 100 ));
        _exitButton.setVerticalAlignment(SwingConstants.CENTER);
        _exitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
				
                System.exit(0);
            }
        });
		_buttonPanel.add(_exitButton);
		
        setBackground(POKER_GREEN);
        _panel.setBackground(POKER_GREEN);
		_buttonPanel.setBackground(POKER_GREEN);
		_imgPanel.setBackground(POKER_GREEN);
		
        add(_loserLabel, BorderLayout.PAGE_START);
		add(_imgPanel);
        add(_panel, BorderLayout.CENTER);
		add(_buttonPanel);

		
        revalidate();
        repaint();	
	}

    /**
         * Enabled or disable buttons based on passed in boolean.
         * @param state Boolean value to set setEnabled of buttons to.
         */
        public void setButtons(boolean state) {
            _betButton.setEnabled(state);
            _checkButton.setEnabled(state);
            _foldButton.setEnabled(state);
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
	
	/**
     * Finds the next player that hasnt already lost
     * @param 	int start		 gives where to start looking for next
	 * @return	the next available player
     */
	private int nextPlayerIn(int start){
		//if the start is at the end
		if (start == players.length-1){
			for(int i = 0; i < start; i++){
				if(players[i].getCash() > 0) {
					return i;
				}
			}
		}
		else{
			for(int i = start + 1; i < players.length; i++){
				if(players[i].getCash() > 0) {
					return i;
				}
				else{
					return nextPlayerIn(i);
				}
			}
		}
		return 0;
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
				dealerNum = nextPlayerIn(dealerNum);
				sBlindNum = nextPlayerIn(dealerNum);
				bBlindNum = nextPlayerIn(sBlindNum);
			}
			//moving the dealer clockwise
			else{
				dealerNum = nextPlayerIn(dealerNum);
				sBlindNum = nextPlayerIn(dealerNum);
				bBlindNum = nextPlayerIn(sBlindNum);
			}
		}
		else{
			//starting the game with only 2 players, should randomize small blind
			if (sBlindNum == -1){
				Random rand = new Random();
				
				sBlindNum = rand.nextInt(2);
				if(sBlindNum == 0){
					sBlindNum = 1;
					bBlindNum = 0;
				}
				else{
					sBlindNum = 0;
					bBlindNum = 1;
				}
			}
			else{
				if(sBlindNum == 0){
					sBlindNum = 1;
					bBlindNum = 0;
				}
				else{
					sBlindNum = 0;
					bBlindNum = 1;
				}
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
     * Reinitialize Card after loading game from save file
     */
    private void reinitCardBack() {
        try {
            Image back = ImageIO.read(this.getClass().getResource("/back.png"));
            cardBack = new Card("Back", back);
        } catch (IOException ioex) {
            System.exit(1);
        }
    }
    
    
    /**
     * Print user actions and other game information to game console in
     * bottom right corner of Game panel.
     * @param line String to format and print.
     */
    private void printGameConsole(String line) {
        JScrollBar    _scrollBar  = _scrollPane.getVerticalScrollBar();

        _scrollBar  = _scrollPane.getVerticalScrollBar();

        DefaultCaret caret = (DefaultCaret)_scrollTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        _scrollTextArea.append(line + "\n");

        _scrollBar.setValue(_scrollBar.getMaximum());
    }
}