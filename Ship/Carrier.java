package Ship;

public class Carrier extends Ship {
  private static final int SIZE = 5;
  private static final int ID = 5;

  /**
   * public Carrier()
   */
  public Carrier() {
    super(SIZE, ID);
  }
}

/*
// Variables
private int size = 5;
private int totalHealth = 5;
private  int currentHealth = 5;
private int id = 5;

// Constructor
public Carrier() {
super();
this.setSize(size);
this.setTotalHealth(totalHealth);
this.setCurrentHealth(currentHealth);
this.setId(id);
this.setShipName("Ship.Carrier");
}
*/
