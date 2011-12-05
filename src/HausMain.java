/**
 * Ausführende Klasse
 * 
 * @author Marius Junak
 *
 */
public class HausMain {
	public static void main(String[] args) throws FisNotLoadedException{
		HausApplet meinHaus = new HausApplet();	
		meinHaus.init();
		meinHaus.start();
	}
}
