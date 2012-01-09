import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.data.general.DefaultValueDataset;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

/**
 *  Der "View" Teil des Applets, stellt die Simulierten werde
 *  der Fuzzylogik Grafisch dar.
 * 
 * @date 17.11.2011						
 * 
 * @author Marius Junak
 * 										
 * @version 0.1
 * 
 * History: 17.11.2011 Oberflaeche Grob angelegt
 * 
 * TODO: Grafische darstellung ueberlegen und implementieren.
 * 		// Start und Stop Button implementieren. done
 */
public class HausGUI
{
	private HausKalk kalk;
	
	JPanel graphik;
	JPanel mainPanel;
	JPanel leftPanel ;
	JPanel rightPanel;
	JLabel heizungLabel;
	JLabel innenLabel;
	JLabel aussenLabel;
	JLabel personenLabel;
	JLabel uhrzeitLabel;
	JLabel lichtLabel;
	JLabel rolloLabel;
	JLabel lueftungLabel;
	
	private JLabel lblLichtstrke;
	private JLabel lblTemperaturInnen;
	private JLabel lblTemperaturAussen;
	private JLabel lblPersonen;
	private JLabel lblUhrzeit;
	final JTabbedPane tabbedPane;
	private JMenuBar menuBar;
	private JMenu mnHelp;
	private JMenu mnSimulation;
	private JMenuItem mntmStart;
	private JMenuItem mntmStop;
	private JMenuItem mntmAbout;
	private JTextField txtPersonen;
	private JTextField txtLicht;
	private JTextField txtTempA;
	private JTextField txtTempI;

	private JTextField txtUhr;

	public boolean run = false;
	private BufferedImage aktImage;
	/**
	 * Erstellt das Applet.
	 * @return 
	 * @throws FisNotLoadedException 
	 * @throws IOException 
	 */
	public HausGUI() throws FisNotLoadedException, IOException 
	{
		/* lade die fisdatei und lege neue Hauslogik an				*/
		kalk = new HausKalk("../fcl/Haussteuerung.fcl");

		/* initialisiere Hauptkomponenten							*/
		mainPanel = new JPanel();
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		menuBar = new JMenuBar();
		leftPanel = new JPanel();
		rightPanel = new JPanel();

		mainPanel.setLayout(new BorderLayout(0, 0));
		mainPanel.add(menuBar, BorderLayout.PAGE_START);
		mainPanel.add(tabbedPane, BorderLayout.CENTER); 
		tabbedPane.addTab("Grafik", null, leftPanel, null);		
		tabbedPane.addTab("Tabellen", null, rightPanel, null);

		/* initialisiere andere Komponenten							*/
		initMenu();
		initRightPanel();
		initLeftPanel();
		initGraphikPanel();
		refreshTxtFields();
		refreshGraphik();
	}	
	/**
	 * Initialisiert das GrafikPanel
	 * @throws IOException
	 */
	private void initGraphikPanel() throws IOException{
		graphik = new JPanel();
		graphik.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		graphik.setBounds(10, 10, 600, 500);
		leftPanel.add(graphik);
		graphik.setLayout(new BorderLayout());
		
		this.aktImage = ImageIO.read(new File("../pic/hausTag.png"));
		graphik.add(new JLabel(new ImageIcon(this.aktImage)), BorderLayout.CENTER);
	}
	/**
	 * Initialisiert das linke Panel
	 */
	private void initLeftPanel(){
		leftPanel.setLayout(null);
		JButton btnStartStop = new JButton("Start / Stop Simulation");
		btnStartStop.setBounds(10, 517, 175, 23);
		btnStartStop.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				run = !run;
			}
		});
		leftPanel.add(btnStartStop);
		JButton btnExit = new JButton("Exit");
		btnExit.setBounds(710, 517, 175, 23);
		btnExit.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				System.exit(1);
			}
		});
		leftPanel.add(btnExit);
		JButton btnSetNewValues = new JButton("Setze neue Startwerte");
		btnSetNewValues.setBounds(670, 38, 185, 23);
		btnSetNewValues.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				kalk.setValue(HausKalk.IO.personen, Double.parseDouble(txtPersonen.getText()));
				kalk.setValue(HausKalk.IO.lichtStaerke, Double.parseDouble(txtLicht.getText()));
				kalk.setValue(HausKalk.IO.temperaturAussen, Double.parseDouble(txtTempA.getText()));
				kalk.setValue(HausKalk.IO.temperaturInnen, Double.parseDouble(txtTempI.getText()));
			}
		});
		leftPanel.add(btnSetNewValues);
		txtPersonen = new JTextField();
		txtPersonen.setBounds(782, 179, 86, 20);
		leftPanel.add(txtPersonen);
		txtPersonen.setColumns(10);
		txtLicht = new JTextField();
		txtLicht.setBounds(782, 210, 86, 20);
		leftPanel.add(txtLicht);
		txtLicht.setColumns(10);
		txtTempA = new JTextField();
		txtTempA.setBounds(782, 148, 86, 20);
		leftPanel.add(txtTempA);
		txtTempA.setColumns(10);
		txtTempI = new JTextField();
		txtTempI.setBounds(782, 117, 86, 20);
		leftPanel.add(txtTempI);
		txtTempI.setColumns(10);
		txtUhr = new JTextField();
		txtUhr.setEditable(false);
		txtUhr.setBounds(782, 86, 86, 20);

		txtUhr.setColumns(10);
		lblTemperaturInnen = new JLabel("Temperatur innen:");
		lblPersonen = new JLabel("Personen:");
		lblTemperaturAussen = new JLabel("Temperatur Aussen:");
		lblLichtstrke = new JLabel("Lichtst\u00E4rke:");	
		lblUhrzeit = new JLabel("Uhrzeit:");

		lblTemperaturAussen.setBounds(647, 151, 125, 14);
		lblTemperaturInnen.setBounds(656, 120, 116, 14);
		lblPersonen.setBounds(696, 182, 76, 14);
		lblLichtstrke.setBounds(689, 212, 83, 17);
		lblUhrzeit.setBounds(707, 89, 65, 14);

		leftPanel.add(lblUhrzeit);
		leftPanel.add(lblLichtstrke);
		leftPanel.add(lblPersonen);		
		leftPanel.add(lblTemperaturAussen);
		leftPanel.add(lblTemperaturInnen);
		leftPanel.add(txtUhr);
	}
	/**
	 * Initialisiert das rechte Panel
	 */
	private void initRightPanel(){
		innenLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.temperaturInnen)));
		innenLabel.setBounds(10, 5, 215, 250);
		aussenLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.temperaturAussen)));
		aussenLabel.setBounds(444, 5, 215, 250);
		personenLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.personen)));
		personenLabel.setBounds(219, 5, 215, 250);
		uhrzeitLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.uhrzeit)));
		uhrzeitLabel.setBounds(669, 290, 215, 250);
		lichtLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.lichtStaerke)));
		lichtLabel.setBounds(219, 290, 215, 250);
		rolloLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.rolladen)));
		rolloLabel.setBounds(10, 290, 215, 250);
		lueftungLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.lueftung)));
		lueftungLabel.setBounds(444, 290, 215, 250);
		heizungLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.heizung)));
		heizungLabel.setBounds(669, 5, 215, 250);
		rightPanel.setLayout(null);
		rightPanel.add(innenLabel);
		rightPanel.add(aussenLabel);
		rightPanel.add(personenLabel);
		rightPanel.add(uhrzeitLabel);
		rightPanel.add(lichtLabel);
		rightPanel.add(rolloLabel);
		rightPanel.add(lueftungLabel);
		rightPanel.add(heizungLabel);
	}
	/**
	 *  Initialisiert das Menü
	 */
	private void initMenu(){
		mnSimulation = new JMenu("Simulation");
		mntmStart = new JMenuItem("Start");
		mntmStart.setIcon(new ImageIcon(HausGUI.class.getResource("/org/antlr/works/icons/run.png")));
		mntmStart.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				run = true;
			}
		});
		mntmStop = new JMenuItem("Stop");
		mntmStop.setIcon(new ImageIcon(HausGUI.class.getResource("/org/antlr/works/icons/stop.png")));
		mntmStop.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				run = false;
			}
		});
		/* Fuege Komponenten dem Mainpanel hinzu  				*/
		mnHelp = new JMenu("Help");
		mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				JOptionPane.showMessageDialog(tabbedPane, "By: \n" + 	
	    				"Alexander Zeltner \n Marius Junak \n Marcel Grauel \n Martin Mogoluck", 
	    				"About", JOptionPane.WARNING_MESSAGE);
			}
		});
		mnSimulation.add(mntmStart);
		mnSimulation.add(mntmStop);	
		menuBar.add(mnSimulation);
		menuBar.add(mnHelp);
		mnHelp.add(mntmAbout);
	}
	/**
	 * Führt die einzelnen Zeichen-Funktionen aus.
	 */
	private void refreshGraphik(){
		/* Setze Hintergrund										*/
	try {
			zeichneHaus();
			zeichneAussenTemp();
			zeichneRollo();
			zeichneInnenTemp();
			zeichneHeizung();
			zeichneLicht();
			zeichnePersonen();
			zeichneLueftung();
		} catch (IOException e) {
		}
	}
	/**
	 * Stellt die aktuellen Werte in den JTextfields dar.
	 */
	private void refreshTxtFields(){
		txtPersonen.setText(Double.toString(kalk.getValue(HausKalk.IO.personen)));
		txtLicht.setText(Double.toString(kalk.getValue(HausKalk.IO.lichtStaerke)));
		txtTempI.setText(Double.toString(kalk.getValue(HausKalk.IO.temperaturInnen)));
		txtTempA.setText(Double.toString(kalk.getValue(HausKalk.IO.temperaturAussen)));
		txtUhr.setText(Double.toString(kalk.getValue(HausKalk.IO.uhrzeit)));
	}
	/**
	 * Aktualisiert die Charts
	 */
	private void refreshCharts(){
		innenLabel.setIcon(new ImageIcon(kalk.getChart(HausKalk.IO.temperaturInnen)));
		aussenLabel.setIcon(new ImageIcon(kalk.getChart(HausKalk.IO.temperaturAussen)));
		personenLabel.setIcon(new ImageIcon(kalk.getChart(HausKalk.IO.personen)));
		uhrzeitLabel.setIcon(new ImageIcon(kalk.getChart(HausKalk.IO.uhrzeit)));
		lichtLabel.setIcon(new ImageIcon(kalk.getChart(HausKalk.IO.lichtStaerke)));
		rolloLabel.setIcon(new ImageIcon(kalk.getChart(HausKalk.IO.rolladen)));
		lueftungLabel.setIcon(new ImageIcon(kalk.getChart(HausKalk.IO.lueftung)));
		heizungLabel.setIcon(new ImageIcon(kalk.getChart(HausKalk.IO.heizung)));
	}
	/**
	 * Ruft Funktionen zum Erneuern des Applets auf.
	 */
	public void update() {
		kalk.calcNewValues();
		refreshTxtFields();
		refreshCharts();
		refreshGraphik();
		}
	/**
	 * Zeichnet ein Bild, das am @param sourcePath liegt, an die uebergebenen Koordinaten.
	 * 
	 * @param sourcePath
	 * @param posX
	 * @param posY
	 * @throws IOException
	 */
	private void putImageInImage(String sourcePath, int posX, int posY) throws IOException
	{
		BufferedImage source;
		source = ImageIO.read(new File(sourcePath));
		
		for(int y = 0; y < source.getHeight(); y++)
			for(int x = 0; x < source.getWidth(); x++)
			{
				int rgb = source.getRGB(x, y);
				aktImage.setRGB(posX+x, posY+y, rgb);
			}
		return;
	}
	/**
	 * Zeichnet ein Bild an die Uebergebenen Koordinaten des Bildes.
	 * 
	 * @param source
	 * @param posX
	 * @param posY
	 * @throws IOException
	 */
	private void putImageInImage(BufferedImage source, int posX, int posY) throws IOException
	{
		for(int y = 0; y < source.getHeight(); y++)
			for(int x = 0; x < source.getWidth(); x++)
			{
				int rgb = source.getRGB(x, y);
				aktImage.setRGB(posX+x, posY+y, rgb);
			}
		return;
	}
	/**
	 * Zeichnet einen String als Schriftzug an die übergeben Koordinaten. 
	 * 
	 * @param text
	 * @param x
	 * @param y
	 * @param color
	 * @return
	 */
	private BufferedImage putStringInImage(String text,int x, int y, Color color)
	{
		Graphics test = aktImage.getGraphics();
		test.setFont(new Font("Serif",Font.BOLD,15));  //alt 12
		test.setColor(color);
		test.drawString(text,x,y);
		return aktImage;
	}
	private void zeichneHaus() throws IOException{
		if(!kalk.istNacht)
			this.putImageInImage("../pic/hausTag.png", 0, 0);
		else
				this.putImageInImage("../pic/hausNacht.png",0 , 0);
	}
	/**
	 * Zeichnet das Rollo.
	 * @throws IOException
	 */
	private void zeichneRollo() throws IOException{
		double tmpRollo = kalk.getValue(HausKalk.IO.lichtStaerke);
		// Koordinaten des Fensters
		int tmpX = 200, tmpY = 200;
		if(tmpRollo > 0 && tmpRollo < 25)
			this.putImageInImage("../pic/rolloViertel.png", tmpX, tmpY);
		else if(tmpRollo >= 25 && tmpRollo <50 )
			this.putImageInImage("../pic/rolloHalb.png", tmpX, tmpY);
		else if(tmpRollo >=50 && tmpRollo < 75)
			this.putImageInImage("../pic/rolloDreiviertel.png", tmpX, tmpY);
		else if(tmpRollo >= 75)
			this.putImageInImage("../pic/rolloZu.png", tmpX, tmpY);
		else
			this.putImageInImage("../pic/rolloOben.png", tmpX, tmpY);
			
	}
	/**
	 * Zeichnet die Lüftung 1-3 Symbole, je nach staerke.
	 * @throws IOException
	 */
	private void zeichneLueftung() throws IOException{
		double tmpLueftung = kalk.getValue(HausKalk.IO.lueftung);
		//
		if(tmpLueftung > 0)
			this.putImageInImage("../pic/wind.png", 390, 180);
		if(tmpLueftung >= 33)
			this.putImageInImage("../pic/wind.png", 390, 200);
		if(tmpLueftung >=66)
			this.putImageInImage("../pic/wind.png", 390, 220);
		
	}
	/**
	 * Zeichnet Personen ensprechend ihrer Anzahl. (bis maximal 6Personen)
	 * @throws IOException
	 */
	private void zeichnePersonen() throws IOException{
		double tmpPerson = kalk.getValue(HausKalk.IO.personen);
		
		if(tmpPerson >0){
			this.putImageInImage(("../pic/mensch.png"), 200, 265);
		}
		if(tmpPerson >1){
			this.putImageInImage(("../pic/mensch.png"), 240, 265);
		}
		if(tmpPerson >2){
			this.putImageInImage(("../pic/mensch.png"), 280, 265);
		}
		if(tmpPerson >3){
			this.putImageInImage(("../pic/mensch.png"), 320, 265);
		}
		if(tmpPerson >4){
			this.putImageInImage(("../pic/mensch.png"), 360, 265);
		}
		if(tmpPerson >5){
			this.putImageInImage(("../pic/mensch.png"), 400, 265);
		}
	}
	/**
	 * Zeichne den Hintergrund, sprich ob es Tag oder Nacht ist.
	 */
	private void zeichneLicht(){
		if(!kalk.istNacht)
			this.putStringInImage("Lichtstaerke: " + Double.toString(kalk.getValue(HausKalk.IO.lichtStaerke)) + " Lux", 25, 25, Color.BLACK);
		else
			this.putStringInImage("Lichtstaerke: " + Double.toString(kalk.getValue(HausKalk.IO.lichtStaerke)) + " Lux", 25, 25, Color.YELLOW);
	}
	/**
	 * Zeichnet das Thermometer, welches die Aussentemperaturanzeigt.
	 * @throws IOException
	 */
	private void zeichneAussenTemp() throws IOException{	
		final ThermometerPlot plot = new ThermometerPlot(new DefaultValueDataset(kalk.getValue(HausKalk.IO.temperaturAussen)));
		final JFreeChart chart = new JFreeChart(plot);
		this.putImageInImage(this.resizeImg(chart.createBufferedImage(100, 300), 50, 150), 540, 160);
	}
	/**
	 * Zeichnet das THermometer, welches die Innentemperaturanzeigt.
	 * @throws IOException
	 */
	private void zeichneInnenTemp() throws IOException{
		final ThermometerPlot plot = new ThermometerPlot(new DefaultValueDataset(kalk.getValue(HausKalk.IO.temperaturInnen)));
		final JFreeChart chart = new JFreeChart(plot);
		this.putImageInImage(this.resizeImg(chart.createBufferedImage(100, 300), 50, 150), 125, 170);
	}
	/**
	 * Zeichnet die Heizung
	 * @throws IOException
	 */
	private void zeichneHeizung() throws IOException{
		final MeterPlot meterplot = new MeterPlot(new DefaultValueDataset(kalk.getValue(HausKalk.IO.heizung)));
		final JFreeChart chart = new JFreeChart(meterplot);
		this.putImageInImage(chart.createBufferedImage(90, 80), 130, 360);
		double tmpHeizung = kalk.getValue(HausKalk.IO.heizung);
		if(tmpHeizung > 0 && tmpHeizung < 50)
			this.putImageInImage("../pic/heizungMittel.png", 220, 360);
		else if(tmpHeizung >= 50)
			this.putImageInImage("../pic/heizungStark.png", 220, 360);
		else
			this.putImageInImage("../pic/heizungAus.png", 220, 360);
	
	}
	/** Verkleinert das uebergebene Bild auf X x Y  Pixel					*/
	private BufferedImage resizeImg(BufferedImage source, int x, int y){
		BufferedImage tmp = new BufferedImage( x, y, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2D = tmp.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(source, 0, 0, x, y, null);
		return tmp;
	}
}
