# Architecture

GameWindow
 - Main JFrame, starts on launch. Automatically populated with Title JPanel object. On creating new game, replaced Title with Game object.
 
 Title
 - Main screen, can add menu options and/or open file options that are currently in dropdown menu.
 
 Game
 - Main game class. Initialized with save file or with user input.
