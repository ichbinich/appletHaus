import java.awt.image.BufferedImage;
import java.util.Random;
import net.sourceforge.jFuzzyLogic.*;

/** 
 * Diese Klasse ist fuer die Verbindung mit der FuzzyLogic zustaendig. 
 * Sie liest eine spezielle FCL-Datei ein und kann die Werte der Inputvariablen setzen 
 * und die Werte der Outputvariabeln setzen. 
 * 
 * @date 16.11.2011
 * 						
 * @author Marius Junak
 * 
 * @version 0.4
 * 
 * 
 */
public class HausKalk 
{
	public boolean istNacht;
	private final double WIRKGRAD_HZG = 0.1;
	private final double WIRKGRAD_DAEMM = 0.3;
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
		istNacht = false;
	} 
	/**
	 * Laedt eine FCL Datei
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
	 * 
	 * @param io
	 * @return Gibt ein Chart zurueck 
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
	 * @return gibt den Wert der Variabeln zurueck
	 */
	public double getValue (IO io)
	{
		return fis.getVariable (io.toString()).getValue();
	}
	/**
	 * Berechnet neue Werte für die Eingangsvariablen, abhängig von den Vorherigen.
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
		this.setValue(IO.uhrzeit, (++tmpUhrzeit) % 24);
		/* Berechnung der neuen Innentemperatur 						*/

		/* Berechnung der Lichtstaerke 								*/
		/* If Tag													*/
		if(this.getValue(IO.uhrzeit) > 6 && this.getValue(IO.uhrzeit) <= 18){
			istNacht = false;
			this.setValue(IO.lichtStaerke, rand.nextInt(99000)+1000);
		}
		/* If Nachts  											*/	
		if(this.getValue(IO.uhrzeit) < 4 && this.getValue(IO.uhrzeit) > 22){
			this.setValue(IO.lichtStaerke, rand.nextInt(100));
			istNacht = true;
		}
		/* If Daemmerung											*/
		if((this.getValue(IO.uhrzeit) >= 4 && this.getValue(IO.uhrzeit) <= 6) ||
		(this.getValue(IO.uhrzeit) >= 18 && this.getValue(IO.uhrzeit) <= 22))
		{
			istNacht = false;
			this.setValue(IO.lichtStaerke, rand.nextInt(29500)+500);
		}
		/* Berechnung der Anzahl der Personen 						*/
		double pers = rand.nextInt(3);
		if (0 == rand.nextInt (100) % 2)
			tmpPersonen += pers;
		else
			tmpPersonen -= pers;
		
		if(tmpPersonen < 0)
			tmpPersonen *= -1;
		this.setValue(IO.personen, tmpPersonen);
		/* Berechnung der Innentemperatur 					TODO: Temperaturberechnung		*/
		this.setValue(IO.temperaturInnen, tmpInnen + (WIRKGRAD_HZG * 50) + (tmpAussen - tmpInnen) * WIRKGRAD_DAEMM);  
		/* Berechnung der Aussentemperatur
		 *  tmpAussentemperatur + Licht * c + uhrzeit	
		 */ 	
		double tempr = rand.nextInt(2);
		if (0 == rand.nextInt (100) % 2)
			tmpAussen += tempr;
		else 
			tmpAussen -= tempr;
		this.setValue(IO.temperaturAussen,tmpAussen);
		 fis.evaluate();
	}
}
