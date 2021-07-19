import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerNotifier implements Runnable {
  private ObjectOutputStream outputStream;
  private final Socket socket;
  private Board newBoard = null;

  private boolean isGameOver = false;
  private boolean hasDisconnect = false;

  /**
   * ServerNotifier(ObjectOutputStream _outputStream)
   * @param _socket From containing Connection
   */
  ServerNotifier(Socket _socket) {
      socket = _socket;

      try {
        outputStream = new ObjectOutputStream(socket.getOutputStream());
      } catch (Exception e) {
        e.printStackTrace();
      }
  }

  /**
   * getNewBoard()
   * @return newBoard
   */
  private synchronized Board getNewBoard() {
    return newBoard;
  }

  /**
   * etNewBoard(Board _newBoard)
   * @param _newBoard From containing Connection
   */
  synchronized void setNewBoard(Board _newBoard) {
    newBoard = _newBoard;
  }

  /**
   * getSocket()
   * @return socket
   */
  private synchronized Socket getSocket() {
    return socket;
  }

  /**
   * killProcess()
   */
  synchronized void killProcess() {
    isGameOver = true;
  }

  /**
   * isGameOver()
   * @return isGameOver
   */
  private synchronized boolean isGameOver() {
    return  isGameOver;
  }

  /**
   * hasDisconnect()
   * @return hasDisconnect
   */
  synchronized boolean hasDisconnect() {
    return hasDisconnect;
  }

  /**
   * flagDisconnect()
   */
  private synchronized void flagDisconnect() {
    hasDisconnect = true;
  }

  /**
   * sendObject()
   */
  private void sendBoard() {
    try {
      if (!this.getSocket().isInputShutdown()) {
        outputStream.reset();
        outputStream.writeObject(getNewBoard());
        outputStream.flush();
      }

    } catch(Exception e) {
      this.flagDisconnect();
    }
  }

  /**
   * ClientNotifier Runnable
   */
  @Override
  public void run() {
    try {
      while (getNewBoard() == null && !this.hasDisconnect()) { Thread.sleep(50); }

      while (!this.isGameOver() && !this.hasDisconnect()) {
        this.sendBoard();
        Thread.sleep(50);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
