import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class HausApplet extends JApplet implements Runnable
{
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
	 * @throws FisNotLoadedException 
	 */
	public HausApplet() throws FisNotLoadedException 
	{
		kalk = new HausKalk("steuerung.fis");
		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		tabbedPane.addTab("Grafik", null, leftPanel, null);
		leftPanel.setLayout(null);
		
		JButton btnStartStop = new JButton("Start / Stop Simulation");
		btnStartStop.setBounds(30, 219, 175, 23);
		btnStartStop.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				run = !run;
			}
		});
		leftPanel.add(btnStartStop);
		
		JButton btnSetNewValues = new JButton("Setze neue Startwerte");
		btnSetNewValues.setBounds(250, 62, 185, 23);
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
		txtPersonen.setBounds(349, 189, 86, 20);
		leftPanel.add(txtPersonen);
		txtPersonen.setColumns(10);
		
		txtLicht = new JTextField();
		txtLicht.setBounds(349, 220, 86, 20);
		leftPanel.add(txtLicht);
		txtLicht.setColumns(10);
		
		txtTempA = new JTextField();
		txtTempA.setBounds(349, 158, 86, 20);
		leftPanel.add(txtTempA);
		txtTempA.setColumns(10);
		
		txtTempI = new JTextField();
		txtTempI.setBounds(349, 127, 86, 20);
		leftPanel.add(txtTempI);
		txtTempI.setColumns(10);
		
		JLabel lblTemperaturInnen = new JLabel("Temperatur innen:");
		lblTemperaturInnen.setBounds(250, 130, 89, 14);
		leftPanel.add(lblTemperaturInnen);
		
		JLabel lblTemperaturAussen = new JLabel("Temperatur Aussen:");
		lblTemperaturAussen.setBounds(241, 161, 98, 14);
		leftPanel.add(lblTemperaturAussen);
		
		JLabel lblPersonen = new JLabel("Personen:");
		lblPersonen.setBounds(290, 192, 49, 14);
		leftPanel.add(lblPersonen);
		
		JLabel lblLichtstrke = new JLabel("Lichtst\u00E4rke:");
		lblLichtstrke.setBounds(283, 222, 56, 17);
		leftPanel.add(lblLichtstrke);
		
		lblUhrzeit = new JLabel("Uhrzeit:");
		lblUhrzeit.setBounds(301, 99, 38, 14);
		leftPanel.add(lblUhrzeit);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(349, 96, 86, 20);
		leftPanel.add(textField);
		textField.setColumns(10);
		tabbedPane.addTab("Tabellen", null, rightPanel, null);
		/* initialisiere Labels 										*/
		heizungLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.heizung)));
		heizungLabel.setBounds(222, 63, -80, -58);
		innenLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.temperaturInnen)));
		innenLabel.setBounds(227, 63, 0, 0);
		aussenLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.temperaturAussen)));
		aussenLabel.setBounds(232, 5, 0, 0);
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
		
		rightPanel.add(heizungLabel);
		rightPanel.add(innenLabel);
		rightPanel.add(aussenLabel);
		rightPanel.add(personenLabel);
		rightPanel.add(uhrzeitLabel);
		rightPanel.add(lichtLabel);
		rightPanel.add(rolloLabel);
		rightPanel.add(lueftungLabel);
	
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
	public void run() 
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
				wait(1000);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
