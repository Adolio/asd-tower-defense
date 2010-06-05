package vues;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Console extends JPanel
{
    private static final long serialVersionUID = 1L;
    private JEditorPane taConsole = new JEditorPane("text/html","");
    private JScrollPane scrollConsole;
    
    
    public Console(int largeur,int hauteur)
    {
        super(new BorderLayout());
        
        // style
        taConsole.setFont(GestionnaireDesPolices.POLICE_CONSOLE);
        taConsole.setEditable(false);
        
        scrollConsole = new JScrollPane(taConsole);
        scrollConsole.setPreferredSize(new Dimension(largeur,hauteur));
        
 
        add(scrollConsole,BorderLayout.CENTER);
    }
    
    /**
     * Permet d'ajouter du text HTML dans la console
     * 
     * @param texte le texte a ajouter
     */
    public void ajouterTexteHTMLDansConsole(String texte)
    {
        String s = taConsole.getText();
        taConsole.setText( s.substring(0,s.indexOf("</body>")) 
                           + texte + 
                           s.substring(s.indexOf("</body>")));
        
        // reposition le curseur en fin 
        taConsole.setCaretPosition( taConsole.getDocument().getLength() - 1 );
    }
}
