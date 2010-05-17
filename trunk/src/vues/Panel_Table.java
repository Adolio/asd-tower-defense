package vues;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;

/**
 * Use to create vertical form.
 * 
 * @author Aurélien Da Campo
 */
public class Panel_Table extends JPanel
{
	private static final long serialVersionUID = 1L;
	private GridBagLayout gbl 			= new GridBagLayout();
	private Insets Insets 				= new Insets(1,1,1,1);
   	private GridBagConstraints ct 	    = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, Insets, 0, 0);
	
	/**
	 * Constructor without params
	 */
	public Panel_Table()
	{
		setLayout(gbl);
	}

	/**
	 * add
	 * 
	 * @param cp1
	 * @param cp2
	 * @param cp3
	 */
	public void add(Component comp, int x, int y)
	{
		if(ct != null)
		{
		    ct.gridx = x;
		    ct.gridy = y;
    		gbl.setConstraints(comp, ct);
    		add(comp);
		}
	}
}
