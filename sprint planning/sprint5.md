### DJ
When a hand finishes and while waiting for the user to click the "Deal Next Hand" button, the winning cards from the previous hand should be obviously displayed. This may also be displayed with a button.

4 points

Sidepots correctly calculate and distribute winnings, but the result is currently not printed to the game log.

2 points


### Ben
As a game administrator, I want the game to end once only one player has money, in accordance with the rules.
"Game should end when the player [i.e. the user] runs out of money" - Prof Laboon, D3 report

4 points

Fix End-Game issues:

Issue #84: Currently the end game screen is one large button that starts the game again. Refactor this to look nice and (optionally) display end game information, who won, etc.

Issue #53: As a player, whenever a game ends, I want a message to appear indicating who the winner is and ask if I would like to play again, so that it is clear and easy to tell when the game is over, and how to start a new game.

Issue #50: As a game administrator, whenever a player runs out of money, I want them to be considered out of the game and thus have no more cards dealt to them, and have them unable to take another turn, so that it is possible to determine when the game is over.

8 points


### Zack
Update title screen.

1 point

Training Mode: When starting a game, players can select to be in "training mode". In training mode, two things will hapen: (a) before the flop, will show the relative value of their hand (you may use http://www.preflophands.com/ or some other source for this) and (b) a value is shown to the player every time they have a chance to bet - the pot odds

8 points  

### Dylan - SCRUM MASTER
When applicable, the check button should be replaced with a call button instead of being disabled. At the end of each hand, a show winning hand button should appear next to the next hand button.

4 points

A new info panel object should be made to display the timer and training mode information. If none of this information is present, the panel should be invisible.

4 points

