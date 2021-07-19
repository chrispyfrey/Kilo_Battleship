import Ship.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Board implements Serializable {

  // NOTE: Set the size of the game boards here:
  public static final int BOARD_LENGTH = 10;
  public static final int BOARD_WIDTH = 10;


  private Integer[][] playerGameBoard;
  private Integer[][] opponentGameBoard;

  private Integer hitCountdown = null; // IMPORTANT

  // Possible values: {null: no winner, true: player is winner, false: player is loser}
  private Boolean isWinner = null;

  private List<Ship> ships;

  /**
   * Board()
   */
  Board() {
    playerGameBoard = new Integer[BOARD_LENGTH][BOARD_WIDTH];
    opponentGameBoard = new Integer[BOARD_LENGTH][BOARD_WIDTH];
    ships = new ArrayList<>();
  }

  /**
   * Board(Board _board)
   * @param _board Board to be copied
   */
  Board(Board _board) {
    hitCountdown = _board.getHitPoints();
    isWinner = _board.isWinner();

    playerGameBoard = _board.getPlayerGameBoard();
    opponentGameBoard = _board.getOpponentGameBoard();

    ships = new ArrayList<>(_board.getShips());
  }

  /**
   * getOpponentGameBoard()
   * @return opponentGameBoard
   */
  private synchronized Integer[][] getOpponentGameBoard() {
    return opponentGameBoard;
  }

  /**
   * setOpponentGameBoard(Integer[][] _opponentGameBoard)
   * @param _opponentGameBoard Opponent game board to be set
   */
  synchronized void setOpponentGameBoard(Integer[][] _opponentGameBoard) {
    opponentGameBoard = _opponentGameBoard;
  }

  /**
   * getPlayerGameBoard()
   * @return playerGameBoard
   */
  synchronized Integer[][] getPlayerGameBoard() {
    return playerGameBoard;
  }

  /**
   * decrementCountdown()
   */
  private synchronized void decrementCountdown() {
    --hitCountdown;
  }

  /**
   * isWinner()
   * @return isWinner
   */
  synchronized Boolean isWinner() {
    return isWinner;
  }

  /**
   * setWinner()
   */
  synchronized void setWinner() {
    isWinner = true;
  }

  /**
   * setLoser()
   */
  synchronized void setLoser() {
    isWinner = false;
  }

  /**
   * isGameOver()
   * @return Win condition
   */
  synchronized boolean isGameOver() {
    return hitCountdown == 0;
  }

  /**
   * getHitPoints()
   * @return hitCountdown
   */
  private synchronized int getHitPoints() {
    return hitCountdown;
  }

  /**
   * getShips()
   * @return ships
   */
  private synchronized List<Ship> getShips() {
    return ships;
  }

  /**
   * setShot(int _x, int _y)
   * @param _x x coordinate
   * @param _y y coordinate
   */
  void setShot(int _x, int _y) {
    if (this.getOpponentGameBoard()[_x][_y] == null) {
      this.getOpponentGameBoard()[_x][_y] = 10;
    }
    else {
      this.getOpponentGameBoard()[_x][_y] *= -1;
      this.decrementCountdown();
    }
  }

  /**
   * createBoard()
   */
  synchronized void createBoard() {
    Carrier carrier = new Carrier();
    ships.add(carrier);
    seedBoard(carrier.getSize(), carrier.getID());
    hitCountdown = carrier.getSize();

    Battleship battleship = new Battleship();
    ships.add(battleship);
    seedBoard(battleship.getSize(), battleship.getID());
    hitCountdown += battleship.getSize();

    Cruiser cruiser = new Cruiser();
    ships.add(cruiser);
    seedBoard(cruiser.getSize(), cruiser.getID());
    hitCountdown += cruiser.getSize();

    Submarine submarine = new Submarine();
    ships.add(submarine);
    seedBoard(submarine.getSize(), submarine.getID());
    hitCountdown += submarine.getSize();

    Destroyer destroyer = new Destroyer();
    ships.add(destroyer);
    seedBoard(destroyer.getSize(), destroyer.getID());
    hitCountdown += destroyer.getSize();
  }

  /**
   * seedBoard(int shipSize, int shipID)
   * @param shipSize Size of ship
   * @param shipID ID of ship
   */
  private synchronized void seedBoard(int shipSize, int shipID) {
    Random rand = new Random();
    int x;
    int y;
    int orientation;

    do {
      do{
        x = rand.nextInt(BOARD_WIDTH) - shipSize;
      } while (x < 0);

      do{
        y = rand.nextInt(BOARD_WIDTH) - shipSize;
      } while (y < 0);

      orientation = rand.nextInt(2);

    } while (!checkSpaceIsOpen(shipSize, orientation, x, y));

    // now actually assign the ship to the boardArray
    if (orientation == 0) {
      int ship = shipSize + x;
      for (int i = x; i < ship; i++) {
        playerGameBoard[y][i] = shipID;
      }
    }
    else{
      int ship = shipSize + y;
      for (int j = y; j < ship; j++) {
        playerGameBoard[j][x] = shipID;
      }
    }
    // fleetSize++;
  }

  /**
   * checkSpaceIsOpen(int shipSize, int orientation, int x, int y)
   * @param shipSize Size of ship
   * @param orientation Horizontal/vertical orientation
   * @param x x coordinate
   * @param y y coordinate
   * @return spaceCheck
   */
  private synchronized boolean checkSpaceIsOpen(int shipSize, int orientation, int x, int y) {
    boolean spaceCheck = true;

    //  if orientation is horizontal, just need to check x direction
    if (orientation == 0) {
      int ship = shipSize + x;
      for(; x < ship; x++){
        if (playerGameBoard[y][x] != null) {
          spaceCheck = false;
          break;
        }
      }
    }
    // if orientation is vertical, just need to check y direction
    else {
      int ship = shipSize + y;
      for(; y < ship; y++){
        if (playerGameBoard[y][x] != null) {
          spaceCheck = false;
          break;
        }
      }
    }

    return spaceCheck;
  }

  /**
   * printBoard(int x, int y)
   * @param x x coordinate
   * @param y y coordinate
   */
  synchronized void printBoard(int x, int y) {

    while(opponentGameBoard == null || playerGameBoard == null) {
      try {
        Thread.sleep(10);
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }

    System.out.println();
    System.out.print("\u001B[44m");
    System.out.print("\u001B[0m");

    for (int rows = 0; rows < BOARD_LENGTH; rows++) {

      System.out.print("\u001B[44m");
      System.out.print("|");

      for (int columns = 0; columns < BOARD_WIDTH; columns++) {
        // prints cursor
        if (rows == x && columns == y) {
          System.out.print("\u001B[0m");
          System.out.print("\u001B[41m");

          // display when cursor over a hit ship
          if (opponentGameBoard[rows][columns] == null) {
            System.out.print("  ");
          }
          else {
            if (opponentGameBoard[rows][columns] < 0) {
              System.out.print("**");
            }
            else {
              System.out.print("  ");
            }
          }

          System.out.print("\u001B[44m");
          System.out.print("|");
          // prints empty space
        }
        else if (opponentGameBoard[rows][columns] == null) {
          System.out.print("\u001B[44m");
          System.out.print("__|");
          // prints your ships
        }
        else if (opponentGameBoard[rows][columns] == 10) {
          System.out.print("\u001B[0m");
          System.out.print("\033[0;100m");
          System.out.print("  ");
          System.out.print("\u001B[44m");
          System.out.print("|");
        }
        else if (opponentGameBoard[rows][columns] > 0) {
          System.out.print("\u001B[44m");
          System.out.print("__|");

          // System.out.print("\033[38;5;7m");
          // System.out.print("__|");

        }
        else if (opponentGameBoard[rows][columns] < 0) {
          System.out.print("\u001B[0m");
          System.out.print("\u001B[31m");
          System.out.print("***");
        }
      }

      System.out.print("\u001B[0m");
      System.out.print("\u001B[30m");
      System.out.print("  ");
      System.out.print("\u001B[30m");
      System.out.print(" ");

      for (int columns = 0; columns < BOARD_WIDTH; columns++) {
        // prints water
        if (playerGameBoard[rows][columns] == null) {
          System.out.print("\u001B[44m");
          System.out.print("__|");
          // prints your ships
        }
        else if (playerGameBoard[rows][columns] == 10) {
            System.out.print("\u001B[0m");
            System.out.print("\033[0;100m");
            System.out.print("  ");
            System.out.print("\u001B[44m");
            System.out.print("|");
          }
          else if (playerGameBoard[rows][columns] > 0) {
            System.out.print("\033[38;5;7m");
            System.out.print("__|");
          }
          else if (playerGameBoard[rows][columns] < 0) {
            System.out.print("\u001B[0m");
            System.out.print("\u001B[31m");
            System.out.print("***");
          }
      }
      // reset color to clear and print newline
      System.out.print("\u001B[0m");
      System.out.println();
    }

    System.out.println("Please select from the following options.");
    System.out.println("   UP - W");
    System.out.println(" LEFT - A");
    System.out.println(" DOWN - S");
    System.out.println("RIGHT - D");
    System.out.println(" FIRE - F");
  }
}

/*
// Variables
private int fleetSize = 0;
private int sunkShips = 0;
private int playerScore = 0;
private int opponentScore = 0;

// Constructor
fleetSize = _board.getFleetSize();
sunkShips = _board.getSunkShips();
playerScore = _board.getPlayerScore();
opponentScore = _board.getOpponentScore();

// Functions
public synchronized void setFleetSize(int _fleetSize) {
  fleetSize = _fleetSize;
}

public synchronized void setSunkShips(int _sunkShips) {
  sunkShips = _sunkShips;
}

public synchronized int getFleetSize() {
  return fleetSize;
}

public synchronized int getSunkShips() {
  return sunkShips;
}

public synchronized List<Ship.Ship> returnShipList() {
  return ships;
}

public synchronized void setGameBoards(int[][] playerGameBoard, int[][] opponentGameBoard) {
  playerGameBoard = playerGameBoard;
  opponentGameBoard = opponentGameBoard;
}

public synchronized void setPlayerScore(int playerScore) {
  playerScore = playerScore;
}

public synchronized int getPlayerScore() {
  return playerScore;
}

public synchronized void setOpponentScore(int opponentScore) {
  opponentScore = opponentScore;
}

public synchronized int getOpponentScore() {
  return opponentScore;
}
*/