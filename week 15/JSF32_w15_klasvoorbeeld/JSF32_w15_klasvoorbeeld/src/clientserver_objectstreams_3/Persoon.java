package clientserver_objectstreams_3;

import java.io.Serializable;

public class Persoon implements Serializable
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -7480552314584364590L;
String naam;
  int geboorteJaar;
  int geboorteMaand;


  public Persoon(String naam, int gebJaar, int gebMaand) {
      this.naam = naam;
      this.geboorteJaar = gebJaar;
      this.geboorteMaand = gebMaand;
  }

  public String toString()
  {
    return this.naam+","+this.geboorteJaar+","+this.geboorteMaand;
  }
  
}
