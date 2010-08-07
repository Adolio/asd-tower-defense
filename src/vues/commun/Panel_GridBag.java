package vues.commun;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;

/**
 * Use to create vertical form.
 * 
 * @author Aurelien Da Campo
 */
public class Panel_GridBag extends JPanel
{
	private static final long serialVersionUID = 1L;
	private GridBagLayout gbl 			= new GridBagLayout();
   	private GridBagConstraints ct;
   	
	/**
	 * Constructor without params
	 */
	public Panel_GridBag(Insets insets)
	{
		setLayout(gbl);

		ct = new GridBagConstraints(0, 0, 3, 1, 1, 1, 
		                            GridBagConstraints.CENTER, 
		                            GridBagConstraints.NONE,
		                            insets, 0, 0);
	}

	
	public void add(Component composant, 
	                int gridx, int gridy, 
	                int gridwidth)
    {
	    ct.fill        = GridBagConstraints.HORIZONTAL;
	    ct.insets      = new Insets(1, 8, 1, 8);
	    ct.gridx       = gridx;
	    ct.gridy       = gridy;
	    ct.gridwidth   = gridwidth;
        
        add(composant,ct);
    }
}
