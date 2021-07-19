import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;

public class ClientNotifier implements Runnable {
  private ObjectOutputStream outputStream;
  private final Socket socket;
  private Shot newShot;
  private HashSet<String> shotCache = new HashSet<>();

  private boolean isGameOver = false;
  private boolean hasDisconnect = false;

  /**
   * (ObjectOutputStream _outputStream)
   * @param _socket From instantiating process
   */
  ClientNotifier(Socket _socket) {
    socket = _socket;

    try {
      outputStream = new ObjectOutputStream(socket.getOutputStream());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * getNewShot()
   * @return newShot
   */
  private synchronized Shot getNewShot() {
    return newShot;
  }

  /**
   * setObject()
   * @param _newShot From containing Client
   */
  synchronized void setNewShot(Shot _newShot) {
    newShot = _newShot;
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
   * sendShot()
   * Sends currentShot to server
   */
  private synchronized void sendShot() {
    try {
      outputStream.reset();
      outputStream.writeObject(newShot);
      outputStream.flush();

    } catch (Exception e) {
      this.killProcess();
    }
  }

  /**
   * ClientNotifier Runnable
   */
  @Override
  public void run() {
    try {
      while (this.getNewShot() == null && !this.getSocket().isInputShutdown()) { Thread.sleep(50); }

      while (!this.isGameOver() && !this.hasDisconnect()) {
        if (!shotCache.contains(getNewShot().getShotKey())) {
          this.sendShot();
          shotCache.add(getNewShot().getShotKey());
        }
        Thread.sleep(50);
      }

      this.outputStream.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
