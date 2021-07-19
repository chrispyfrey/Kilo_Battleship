import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientListener implements Runnable {
  private ObjectInputStream inputStream;
  private final Socket socket;
  private Board newBoard = null;

  private boolean isGameOver = false;
  private boolean hasDisconnect = false;

  /**
   * ServerListener(ObjectInputStream _inputStream)
   * @param _socket From containing Handler
   */
  ClientListener(Socket _socket) {
    socket = _socket;
  }

  /**
   * getNewBoard()
   * @return newBoard
   */
  synchronized Board getNewBoard() {
    return newBoard;
  }

  /**
   * setNewBoard(Board _incomingBoard)
   * @param _incomingBoard Board from server
   */
  private synchronized void setNewBoard(Board _incomingBoard) {
    newBoard = _incomingBoard;
  }

  /**
   * getSocket()
   * @return socket
   */
  private synchronized Socket getSocket() {
    return socket;
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
   * killProcess()
   */
  synchronized void killProcess() {
    isGameOver = true;
  }

  synchronized boolean isGameOver() {
    return isGameOver;
  }

  /**
   * updateBoard()
   */
  private void updateBoard() {
    try {
        Board incomingBoard = (Board) inputStream.readObject();
        this.setNewBoard(incomingBoard);

    } catch (SocketTimeoutException ste) {
      if (this.getNewBoard() != null) {
        System.out.println("\nConnection to server lost...");
        this.killProcess();
      }
    } catch (Exception ignoreRest) { }
  }

  /**
   * ClientListener Runnable
   */
  @Override
  public void run() {
    try {
      inputStream = new ObjectInputStream(socket.getInputStream());

      while (!this.isGameOver) { this.updateBoard(); }

      this.inputStream.close();

    } catch(Exception e) {
        e.printStackTrace();
    }
  }
}