import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
		leftPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		tabbedPane.addTab("Tabellen", null, rightPanel, null);
		/* initialisiere Labels 										*/
		heizungLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.heizung)));
		innenLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.temperaturInnen)));
		aussenLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.temperaturAussen)));
		personenLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.personen)));
		uhrzeitLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.uhrzeit)));
		lichtLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.lichtStaerke)));
		rolloLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.rolladen)));
		lueftungLabel = new JLabel(new ImageIcon(kalk.getChart(HausKalk.IO.lueftung)));
		rightPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
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
	
		run = true;
	}

	@Override
	public void run() 
	{
		while(true)
		{
			if(run)
			{
				kalk.calcNewValues();
				this.repaint();
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
