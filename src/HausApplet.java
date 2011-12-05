import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.MeterPlot;
import java.awt.FlowLayout;

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
 * History: 17.11.2011 Oberfläche Grob angelegt
 * 
 * TODO: Grafische darstellung überlegen und implementieren.
 * 		// Start und Stop Button implementieren. done
 */
@SuppressWarnings("serial")
public class HausApplet extends JApplet
{
	public HausApplet() {
	}
	private HausKalk kalk;
	JPanel leftPanel = new JPanel();
	JPanel rightPanel = new JPanel();
	/* Labels für die einzelnen charts 									*/
	JLabel heizungLabel;
	JLabel innenLabel;
	JLabel aussenLabel;
	JLabel personenLabel;
	JLabel uhrzeitLabel;
	JLabel lichtLabel;
	JLabel rolloLabel;
	JLabel lueftungLabel;
	private JMenuBar menuBar;
	private JMenu mnHelp;
	private JMenu mnSimulation;
	private JMenuItem mntmStart;
	private JMenuItem mntmStop;
	private JMenuItem mntmAbout;
	private boolean run = false;
	private JTextField txtPersonen;
	private JTextField txtLicht;
	private JTextField txtTempA;
	private JTextField txtTempI;
	private JLabel lblUhrzeit;
	private JTextField textField;
	/**
	 * Create the applet.
	 * @return 
	 */
	public void init() 
	{
		this.setSize(800,600);
		/* Laedt die fisdatei und legt neue Hauslogik an */
		try {
			kalk = new HausKalk("../fcl/Haussteuerung.fcl");
		} catch (FisNotLoadedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP); 
		getContentPane().add(tabbedPane, BorderLayout.CENTER); 
		 /** The meter plot (dial). */
		MeterPlot meterplot = new MeterPlot();
		/** The meter chart (dial). */
		JFreeChart meterchart = new JFreeChart("Meter Chart", JFreeChart.DEFAULT_TITLE_FONT, meterplot, false);
		/** The meter panel. */
		ChartPanel panelMeter = new ChartPanel(meterchart);
		panelMeter.setBounds(23, 27, 182, 170);	  
		BufferedImage image = meterchart.createBufferedImage(80, 75);
		/* initialisiere Labels 										*/
				
					tabbedPane.addTab("Tabellen", null, rightPanel, null);
					innenLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.temperaturInnen)));
					innenLabel.setBounds(27, 16, 253, 166);
					aussenLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.temperaturAussen)));
					aussenLabel.setBounds(306, 11, 253, 171);
					personenLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.personen)));
					personenLabel.setBounds(237, 5, 0, 0);
					uhrzeitLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.uhrzeit)));
					uhrzeitLabel.setBounds(242, 5, 0, 0);
					lichtLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.lichtStaerke)));
					lichtLabel.setBounds(247, 5, 0, 0);
					rolloLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.rolladen)));
					rolloLabel.setBounds(252, 5, 0, 0);
					lueftungLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.lueftung)));
					lueftungLabel.setBounds(257, 5, 0, 0);
					rightPanel.setLayout(null);
					rightPanel.add(innenLabel);
					rightPanel.add(aussenLabel);
					rightPanel.add(personenLabel);
					rightPanel.add(uhrzeitLabel);
					rightPanel.add(lichtLabel);
					rightPanel.add(rolloLabel);
					rightPanel.add(lueftungLabel);
					tabbedPane.addTab("Grafik", null, leftPanel, null);
					leftPanel.setLayout(null);
					
					JButton btnStartStop = new JButton("Start / Stop Simulation");
					btnStartStop.setBounds(10, 489, 175, 23);
					btnStartStop.addActionListener(new ActionListener() 
					{
						public void actionPerformed(ActionEvent arg0) 
						{
							run = !run;
						}
					});
					leftPanel.add(btnStartStop);
					
					JButton btnSetNewValues = new JButton("Setze neue Startwerte");
					btnSetNewValues.setBounds(586, 29, 185, 23);
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
					txtPersonen.setBounds(685, 196, 86, 20);
					leftPanel.add(txtPersonen);
					txtPersonen.setColumns(10);
					
					txtLicht = new JTextField();
					txtLicht.setBounds(685, 227, 86, 20);
					leftPanel.add(txtLicht);
					txtLicht.setColumns(10);
					
					txtTempA = new JTextField();
					txtTempA.setBounds(685, 165, 86, 20);
					leftPanel.add(txtTempA);
					txtTempA.setColumns(10);
					
					txtTempI = new JTextField();
					txtTempI.setBounds(685, 134, 86, 20);
					leftPanel.add(txtTempI);
					txtTempI.setColumns(10);
					
					JLabel lblTemperaturInnen = new JLabel("Temperatur innen:");
					lblTemperaturInnen.setBounds(586, 137, 89, 14);
					leftPanel.add(lblTemperaturInnen);
					
					JLabel lblTemperaturAussen = new JLabel("Temperatur Aussen:");
					lblTemperaturAussen.setBounds(577, 168, 98, 14);
					leftPanel.add(lblTemperaturAussen);
					
					JLabel lblPersonen = new JLabel("Personen:");
					lblPersonen.setBounds(626, 199, 49, 14);
					leftPanel.add(lblPersonen);
					
					JLabel lblLichtstrke = new JLabel("Lichtst\u00E4rke:");
					lblLichtstrke.setBounds(619, 229, 56, 17);
					leftPanel.add(lblLichtstrke);
					
					lblUhrzeit = new JLabel("Uhrzeit:");
					lblUhrzeit.setBounds(639, 106, 38, 14);
					leftPanel.add(lblUhrzeit);
					
					textField = new JTextField();
					textField.setEditable(false);
					textField.setBounds(685, 103, 86, 20);
					leftPanel.add(textField);
					textField.setColumns(10);
					
					JPanel panel = new JPanel();
					FlowLayout flowLayout = (FlowLayout) panel.getLayout();
					flowLayout.setAlignment(FlowLayout.LEFT);
					panel.setBounds(10, 11, 538, 467);
					leftPanel.add(panel);	
					JLabel lblChart = new JLabel("image");
					lblChart.setIcon(new ImageIcon(image));
					panel.add(lblChart);
					heizungLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.heizung)));
					tabbedPane.addTab("New tab", null, heizungLabel, null);
					

		menuBar = new JMenuBar();
		getContentPane().add(menuBar, BorderLayout.NORTH);
		
		mnSimulation = new JMenu("Simulation");
		menuBar.add(mnSimulation);
		
		/**
		 * Startbutton 
		*/
		mntmStart = new JMenuItem("Start");
		mntmStart.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				run = true;
			}
		});
		mnSimulation.add(mntmStart);
		
		mntmStop = new JMenuItem("Stop");
		mntmStop.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				run = false;
			}
		});
		mnSimulation.add(mntmStop);	
		
		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);	
		mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				JOptionPane.showMessageDialog(tabbedPane, "By: \n" + 	
	    				"Alexander Zeltner \n Marius Junak \n xxx \n yyy", 
	    				"About", JOptionPane.WARNING_MESSAGE);
			}
		});
		mnHelp.add(mntmAbout);
	
		//run = true;
		this.setVisible(true);
	}
	
	private void repaintThis(){
		this.refreshTxtFields();
		this.repaint();
	}

	/**
	 * stellt die aktuellen Werte in den JTextfields dar.
	 */
	private void refreshTxtFields(){
		txtPersonen.setText(Double.toString(kalk.getValue(HausKalk.IO.personen)));
		txtLicht.setText(Double.toString(kalk.getValue(HausKalk.IO.lichtStaerke)));
		txtTempI.setText(Double.toString(kalk.getValue(HausKalk.IO.temperaturInnen)));
		txtTempA.setText(Double.toString(kalk.getValue(HausKalk.IO.temperaturAussen)));
	}
	@Override
	public void start() 
	{
		while(true)
		{
			if(run)
			{
				kalk.calcNewValues(); 
				this.repaintThis();

			}
			try 
			{
				Thread.sleep(1000);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
