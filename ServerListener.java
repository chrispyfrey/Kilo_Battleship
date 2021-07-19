import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerListener implements Runnable {
  private ObjectInputStream inputStream;
  private final Socket socket;
  private Shot newShot = null;

  private boolean isNewObject = false;
  private boolean isGameOver = false;
  private boolean hasDisconnect = false;

  /**
   * ServerListener(ObjectInputStream _inputStream)
   * @param _socket From containing Handler
   */
  ServerListener(Socket _socket) {
    socket = _socket;
  }

  /**
   * getNewShot()
   * @return Shot
   */
  synchronized Shot getNewShot() {
    return newShot;
  }

  /**
   * setNewShot(Shot _shot)
   * @param _shot New Shot reference
   */
  private synchronized void setNewShot(Shot _shot) {
    newShot = _shot;
  }

  /**
   * getSocket()
   * @return socket
   */
  private synchronized Socket getSocket() {
    return socket;
  }

  /**
   * hasNewObject()
   * @return isNewObject
   */
  synchronized boolean hasNewObject() {
    return isNewObject;
  }

  /**
   * flagNewObject()
   */
  private synchronized void flagNewObject() {
    isNewObject = true;
  }

  /**
   * flagOldObject()
   */
  synchronized void flagOldObject() {
    isNewObject = false;
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
  synchronized void flagDisconnect() {
    hasDisconnect = true;
  }

  /**
   * killProcess()
   */
  synchronized void killProcess() {
    isGameOver = true;
  }

  /**
   * updateShot()
   */
  private void updateShot() {
    try {
      if (!this.getSocket().isOutputShutdown()) {
        Shot tmpShot = (Shot) inputStream.readObject();
        setNewShot(tmpShot);
        flagNewObject();
      }
      else {
        throw new IOException();
      }

    } catch (Exception e) {
      this.flagDisconnect();
    }
  }

  /**
   * ServerListener Runnable
   */
  @Override
  public void run() {
    try {
      inputStream = new ObjectInputStream(socket.getInputStream());

      while (!this.isGameOver() && !this.hasDisconnect()) { this.updateShot(); }

      this.inputStream.close();

    } catch(Exception e) {
        e.printStackTrace();
    }
  }
}