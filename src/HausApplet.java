import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.io.IOException;

/**
 * Die Klasse, die das Applet initialisiert und die Oberfläche aufruft.
 * Startet einen Thread.
 * 
 * @author Marius Junak
 *
 */
@SuppressWarnings("serial")
public class HausApplet extends Applet implements Runnable {
	
	private HausGUI myGUI;
	
	public HausApplet() throws FisNotLoadedException, IOException
	{
		myGUI = new HausGUI();
		new Thread(this).start();
	}
	
	public void init() {
		setSize(900, 600);
		this.setLayout(new BorderLayout());
		this.add(myGUI.mainPanel, BorderLayout.CENTER);
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		this.add(myGUI.mainPanel);
	}
	
	@Override
	public void run() 
	{
		while(true)
		{
			if(myGUI.run)
			{
				
				myGUI.update();
				this.repaint();
			}
			try 
			{
				Thread.sleep(500);
			} catch (InterruptedException e) 
			{
			}
		}
	}
}
