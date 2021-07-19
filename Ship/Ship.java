package Ship;

import java.io.Serializable;

public abstract class Ship implements Serializable {
  private final int SIZE;
  private final int ID;

  /**
   * Ship(int _SIZE, int _ID)
   * @param _SIZE Size of ship
   * @param _ID ID of ship
   */
  Ship(int _SIZE, int _ID) {
    SIZE = _SIZE;
    ID = _ID;
  }

  /**
   * getSize()
   * @return SIZE
   */
  public int getSize() {
    return SIZE;
  }

  /**
   * getID()
   * @return ID
   */
  public int getID() {
    return ID;
  }


}

/*
// Variables
private int _totalHealth;
private int _currentHealth;
private int healthDecrement = 1;
private String _shipName;

// Functions
public void setSize(int size){
this._size = size;
}

public void setTotalHealth(int health) {
this._totalHealth = health;
}

public void setCurrentHealth(int health) {
this._currentHealth = health;
}

public void setId(int id){
this._id = id;
}

public void setShipName(String shipName){
this._shipName = shipName;
}

public int getHealth(){
return this._totalHealth;
}

public int getCurrentHealth(){
return this._currentHealth;
}

public String getShipName(){return _shipName;}

public void decrementHealth(){
_currentHealth -= healthDecrement;
}

public void printShipSunk(){
System.out.println(this.getShipName() + " sunk!");
}
*/
