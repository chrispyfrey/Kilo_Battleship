import java.util.concurrent.ExecutorService;

public class GameServer implements Runnable {
  private static int gameNumTracker;
  private int gameNumber;

  private ExecutorService threadManager;
  private ServerHandler player1 = null;
  private ServerHandler player2 = null;

  /**
   * GameServer(ExecutorService _threadManager)
   * @param _threadManager Thread pool from Server
   */
  GameServer(ExecutorService _threadManager) {
    ++gameNumTracker;
    gameNumber = gameNumTracker;

    threadManager = _threadManager;
  }

  /**
   * getGameNumTracker()
   * @return gameNumTracker
   */
  static int getGameNumTracker() {
    return gameNumTracker;
  }

  /**
   * setPlayer1(Handler _player1)
   * @param _player1 From containing Server
   */
  synchronized void setPlayer1(ServerHandler _player1) {
    player1 = _player1;
  }

  /**
   * setPlayer2(Handler _player2)
   * @param _player2 From containing Server
   * */
  synchronized void setPlayer2(ServerHandler _player2) {
    player2 = _player2;
  }

  /**
   * isGameFull()
   * @return boolean Returns if there are two players or not
   */
  private synchronized boolean isGameFull() {
    return player1 != null && player2 != null;
  }

  /**
   * GameServer Runnable
   */
  @Override
  public void run() {
    int lastSecond = Server.getSeconds();
    int printCycle;

    threadManager.execute(player1);

    while(!isGameFull()) {
      try {
        printCycle = Server.getSeconds();

        if ((printCycle != lastSecond) && (printCycle == 0 || (printCycle) % 5 == 0)) {
          System.out.println(Server.getTimeStamp() + "GameServer #" + gameNumber +
              " is running. Awaiting Connection #2");
          lastSecond = printCycle;
        }

        Thread.sleep(500);
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }

    threadManager.execute(player2);

    player1.initializeBoard();
    player2.initializeBoard();

    player1.getCurrentBoard().setOpponentGameBoard(player2.getCurrentBoard().getPlayerGameBoard());
    player2.getCurrentBoard().setOpponentGameBoard(player1.getCurrentBoard().getPlayerGameBoard());

    player1.startGame();
    player2.startGame();

    try {

      while (!player1.isGameOver() && !player2.isGameOver()) {
        printCycle = Server.getSeconds();

        if ((printCycle != lastSecond) && (printCycle == 0 || printCycle % 10 == 0)) {
          System.out.println(Server.getTimeStamp() + "Game server #" + gameNumber + " is running...");
          lastSecond = printCycle;
        }
      }

      if (player1.hasWinner()) {
        player1.getCurrentBoard().setWinner();
        player2.getCurrentBoard().setLoser();
      }
      else if (player2.hasWinner()) {
        player1.getCurrentBoard().setLoser();
        player2.getCurrentBoard().setWinner();
      }
      else if (player1.isGameOver()) {
        player2.flagDisconnect();
      }
      else if (player2.isGameOver()) {
        player1.flagDisconnect();
      }

      Thread.sleep(2000);
      System.out.println(Server.getTimeStamp() + "Game server #" + gameNumber + " is shutting down...");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
