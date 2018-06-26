### DJ - SCRUM MASTER
As a game administrator, I want ties to be handled by splitting the pot equally among the tied players, so that the game is fairly rewards everyone - finished during last sprint  
&nbsp;&nbsp;&nbsp;&nbsp;1 point  
TASK: Determine course of action in the event that splitting a pot would result in an un-even dollar amount (since the granularity of betting is a dollar)  
&nbsp;&nbsp;&nbsp;&nbsp;1 point  
TASK: Cleanup JPanel layout  
&nbsp;&nbsp;&nbsp;&nbsp;1 point  
TASK: Add unit tests for making sure the game currectly determines the best hand  
&nbsp;&nbsp;&nbsp;&nbsp;4 point  
TASK: Write a test report  
&nbsp;&nbsp;&nbsp;&nbsp;1 point  

### Ben
As a game administrator, I want all the events of the game to be logged to a new external file per game,so I can ensure proper game flow and create a record. (Finish from last sprint)  
&nbsp;&nbsp;&nbsp;&nbsp;2 points  
As a player, I want to see what moves the AI made, so I can make my decision accordingly. (Finish from last sprint)  
&nbsp;&nbsp;&nbsp;&nbsp;2 points  
As a game administrator, upon starting a new game, I want the dealer to be selected at random, and, for each hand there after,  whoever is the dealer should rotate clockwise, to ensure the game is dynamic.  
&nbsp;&nbsp;&nbsp;&nbsp;2 point  
As a player, I want the nominal dealer, small blind, and big blind to be indicated visually, so that I can tell who is who.  
&nbsp;&nbsp;&nbsp;&nbsp;2 point  
As a game administrator, I want blinds to be set to 10/20 (small blind/big blind), so that it is establised and clear to players.  
&nbsp;&nbsp;&nbsp;&nbsp;2 point 

### Zack
As a game administrator, if a player wants to call but does not have enough money, I want them to be able to do so through the implementation of side pots (See example scenarios below), in accordance with the rules.  
&nbsp;&nbsp;&nbsp;&nbsp;8 point  
As a player, I want side pots to be somehow indicated visually, so that I can easily tell how much is in them.  
&nbsp;&nbsp;&nbsp;&nbsp;4 point  

### Dylan
TASK: Cleanup JPanel layout  
&nbsp;&nbsp;&nbsp;&nbsp;1 point  
TASK: Set default username and ai opponents to "Player 1" and "3" respectively.  
&nbsp;&nbsp;&nbsp;&nbsp;1 point  
TASK: Change minimum betting amount from $1 to $10  
&nbsp;&nbsp;&nbsp;&nbsp;1 point  
As a game administrator, I want AI players to have at least a valid random response, so that they actually play the game. (They should never fold if no money is bet to them, however, if, for example, everybody before them just called, they should either raise or call themselves).  
&nbsp;&nbsp;&nbsp;&nbsp;4 point  
As a game administrator, whenever a player runs out of money, I want them to be considered out of the game and thus have no more cards dealt to them, and have them unable to take another turn, so that it is possible to determine when the game is over.  
&nbsp;&nbsp;&nbsp;&nbsp;1 point  
As a player, I want players who are out of the game to have some sort of visual indication, so that I can tell how many players are left in the game.  
&nbsp;&nbsp;&nbsp;&nbsp;2 point  
As a game administrator, I want the game to end once only one player has money, in accordance with the rules.  
&nbsp;&nbsp;&nbsp;&nbsp;1 point  
As a player, whenever a game ends, I want a message to appear indicating who the winner is and ask if I would like to play again, so that it is clear and easy to tell when the game is over, and how to start a new game.  
&nbsp;&nbsp;&nbsp;&nbsp;2 point  

#### Example Scenarios for Side pots
Scenario 1: Assume three players, A, B and C. Player A raises $100. Player B only has $50 but would like to call. Player C folds. Player B puts all of their money in, Player A is returned $50 and the game continues.  

Scenario 2: Assume three players, A, B, and C. Player A raises $100. Player B only has $50 but would like to call. Player C has $1000 and would like to call. There is now a "regular" pot with $50 from each player, and a "side" pot with $100 ($50 from Player A, $50 from Player C). Player B can no longer make any bets - they are committed and must wait until the end of the hand. Players A and C can continue to raise and bet but all additional money ends up in the side pot. At the end of the hand, if either Player A or C wins the hand, all of the money in the regular and side pots goes to them. If Player B wins the hand, they win all of the money in the regular pot only. Whoever has the best hand of Players A and C wins the side pot.  