import java.io.Serializable;

public class Shot implements Serializable {
  private int x;
  private int y;

  /**
   * Shot(int _x, int _y)
   * @param _x x coordinate
   * @param _y y coordinate
   */
  Shot(int _x, int _y) {
    x = _x;
    y = _y;
  }

  /**
   * Shot(Shot _shot)
   * @param _shot Shot to be copied
   */
  Shot(Shot _shot) {
    x = _shot.getX();
    y = _shot.getY();
  }

  /**
   * setX(int x)
   * @param x x coordinate
   */
  public synchronized void setX(int x) {
      this.x = x;
  }

  /**
   * getX()
   * @return x coordinate
   */
  synchronized int getX() {
      return x;
  }

  /**
   * setY(int y)
   * @param y y coordinate
   */
  public synchronized void setY(int y) {
      this.y = y;
  }

  /**
   * getY()
   * @return y coordinate
   */
  synchronized int getY() { return y; }

  /**
   * getShotKey()
   * @return x & y coordinates concatenated as a String
   */
  synchronized String getShotKey() {
    return Integer.toString(x) + Integer.toString(y);
  }

}
