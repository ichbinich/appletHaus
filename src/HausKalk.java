import java.awt.image.BufferedImage;
import java.util.Random;
import net.sourceforge.jFuzzyLogic.*;

/** 
 * Diese Klasse ist für die Verbindung mit der FuzzyLogic zuständig. 
 * Sie ließt eine spezielle FCL-Datei ein und kann die Werte der Inputvariablen setzen 
 * und die Werte der Outputvariabeln setzen. 
 * 
 * @date 16.11.2011
 * 						
 * @author Marius Junak
 * 
 * @version 0.1
 * 
 * TODO: Berechnung der Innen und Aussentemperatur, evtl. verfeinerung der Abhängigkeit des Lichts von der Uhrzeit
 */
public class HausKalk 
{
	public static enum IO { uhrzeit, lichtStaerke, temperaturAussen, temperaturInnen, personen, heizung, lueftung, rolladen }
	String dateiname;
	FIS fis;
	/**
	 * Konstruktor
	 */
	public HausKalk (String dateiname) throws FisNotLoadedException
	{
		this.dateiname = dateiname;		
		this.loadFis();
		this.setValue (IO.lichtStaerke, 500);
		this.setValue (IO.temperaturAussen, -5);
		this.setValue (IO.temperaturInnen, 10);
		this.setValue (IO.uhrzeit, 12);
		this.setValue (IO.personen, 3);
		fis.evaluate();
	} 
	/**
	 * Lädt eine FCL Datei
	 */
	private void loadFis() throws FisNotLoadedException
	{
		fis = FIS.load (dateiname, true);
		if (fis == null)
		{ 
			throw new FisNotLoadedException();
		}
	} 
	/**
	 * @return gibt ein Chart zurueck 
	 */
	public BufferedImage getChart (IO io)
	{
		return this.fis.getVariable(io.toString()).chart(false).createBufferedImage(200, 200);
	}
	public void setValue (IO io, double i) 
	{
		fis.setVariable (io.toString(), i);
	}
	/**
	 * @return gibt den Wert der varuabeln zurueck
	 */
	public double getValue (IO io)
	{
		return fis.getVariable (io.toString()).getValue();
	}
	/**
	 * Berechnung neuer Werte für die Inputvariablen
	 */
	@SuppressWarnings("unused")
	public void calcNewValues(){
		Random rand = new Random();
		/* speichere Werte zwischen um nebeneffekte zu vermeiden 		*/
		double tmpAussen = this.getValue(IO.temperaturAussen);
		double tmpLicht = this.getValue(IO.lichtStaerke);
		double tmpUhrzeit = this.getValue(IO.uhrzeit); 
		double tmpPersonen = this.getValue(IO.personen);
		double tmpInnen = this.getValue(IO.temperaturInnen);
		/* Berechnungn einer neuen Uhrzeit. 							*/
		this.setValue(IO.uhrzeit, (tmpUhrzeit++) % 24);
		/* Berechnung der neuen Innentemperatur 						*/
		if(true) /* TODO: WEGMACHEN */ 				
		{
			/* Berechnung der Lichtstaerke 								*/
			/* If Tag													*/
			if(this.getValue(IO.uhrzeit) > 6 && this.getValue(IO.uhrzeit) <= 18){
				
			}
			/* If Dämmerung  											*/	
			if(this.getValue(IO.uhrzeit) < 4 && this.getValue(IO.uhrzeit) > 22){
			
			}
			/* If Nachts 												*/
			if((this.getValue(IO.uhrzeit) >= 4 && this.getValue(IO.uhrzeit) <= 6) ||
			(this.getValue(IO.uhrzeit) >= 18 && this.getValue(IO.uhrzeit) <= 22))
			{
			}
			/* Berechnung der Anzahl der Personen 						*/
			double pers = rand.nextInt(5);
			if (0 == rand.nextInt (100) % 2)
				tmpPersonen += pers;
			tmpPersonen -= pers;
			if(tmpPersonen < 0)
				tmpPersonen *= -1;
			this.setValue(IO.personen, tmpPersonen);
			/** Berechnung der Innentemperatur 					TODO: Temperaturberechnung		*/
				// c * Heizung * 1 /  Lüftung * Rolladen_Licht * Aussentemperatur * Innentemperaturvorher * Leute
			/* Berechnung der Aussentemperatur
			 * // tmpAussentemperatur + Licht * c + uhrzeit	
			 */ 			
		}
	}
}
