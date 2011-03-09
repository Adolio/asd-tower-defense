package vues.commun;

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
	private Insets Insets 				= new Insets(0,0,0,0);
   	private GridBagConstraints ct 	    = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, Insets, 0, 0);
	
	/**
	 * Constructor without params
	 */
	public Panel_Table()
	{
		setLayout(gbl);
	}

	/**
     * Constructor without params
     */
    public Panel_Table(GridBagConstraints ct)
    {
        this();
        this.ct = ct;
    }
	
    /**
     * add
     * 
     * @param comp
     * @param x
     * @param y
     */
	public void add(Component comp, int x, int y)
	{
		if(ct != null)
		{
		    ct.gridx = x;
		    ct.gridy = y;
		    ct.gridwidth = 1;
            ct.gridheight = 1;
            ct.anchor = GridBagConstraints.EAST;
    		gbl.setConstraints(comp, ct);
    		add(comp);
		}
	}
	
	/**
	 * add
	 * 
	 * @param comp
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
    public void add(Component comp, int x, int y, int width, int height)
    {
        if(ct != null)
        {
            ct.gridx = x;
            ct.gridy = y;
            ct.gridwidth = width;
            ct.gridheight = height;
            
            gbl.setConstraints(comp, ct);
            add(comp);
        }
    }
    
    
    public void addFieldTitle(Component comp, int y)
    {
        if(ct != null)
        {
            ct.gridx = 0;
            ct.gridy = y;
            ct.gridwidth = 1;
            ct.gridheight = 1;
            ct.anchor = GridBagConstraints.EAST;
            gbl.setConstraints(comp, ct);
            add(comp);
        }
    }
    
    public void addField(Component comp, int y)
    {
        if(ct != null)
        {
            ct.gridx = 1;
            ct.gridy = y;
            ct.gridwidth = 1;
            ct.gridheight = 1;
            ct.anchor = GridBagConstraints.WEST;
            gbl.setConstraints(comp, ct);
            add(comp);
        }
    }
    
	
	/**
     * add
     * 
     * @param cp1
     * @param cp2
     * @param cp3
     */
    public void ajouter(Component comp, GridBagConstraints ct)
    {
        if(ct != null)
        {
            gbl.setConstraints(comp, ct);
            add(comp);
        }
    }
	
}
