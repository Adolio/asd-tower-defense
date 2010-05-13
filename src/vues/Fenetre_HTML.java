package vues;

import java.io.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.*;
import java.net.*;
import java.awt.event.*;
import java.awt.*;

/**
 * A basic help window, displaying a web page containing the User documentation
 * of the program
 * 
 * @author Da Silva Yvan
 * 
 */
class Fenetre_HTML extends JFrame implements ActionListener {

    private JEditorPane       editorPane;
    private static final long serialVersionUID = 1L;
    private JButton bFermer = new JButton("Fermer");
    
    
   /**
    * HelpWindow constructor
    * 
    * @param title The window title
    * @param url The web page link
    * @param parent The parent window
    */
   public Fenetre_HTML(String title, File fichier, JFrame parent) {
      
       super(title);
      setIconImage(parent.getIconImage());
      getContentPane().setBackground(LookInterface.COULEUR_DE_FOND);
      
      // contenu HTML
      editorPane = new JEditorPane();
      editorPane.setEditable(false);
      editorPane.setBorder(new EmptyBorder(-20,0,0,0));
      
      URL url = null;
      
      try{
          url = new URL("file:"+fichier.getPath());
      } 
      catch (MalformedURLException e){
          e.printStackTrace();
      }
      
      try {
         editorPane.setPage(url);
      } 
      catch (Exception e) {}
      
      // ajout du fichier
      editorPane.addHyperlinkListener(new HyperlinkListener() {
         public void hyperlinkUpdate(HyperlinkEvent event) {
            try {
               if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                  editorPane.setPage(event.getURL());
               }
            } catch (IOException e) {
            }
         }
      });
      
      getContentPane().add(new JScrollPane(editorPane), BorderLayout.CENTER);
      
      // bouton fermer
      bFermer.addActionListener(this);
      bFermer.setBackground(LookInterface.COULEUR_BOUTON);
      bFermer.setForeground(GestionnaireDesPolices.COULEUR_TXT_BOUTON);
      getContentPane().add(bFermer, BorderLayout.SOUTH);
      
      // dernier réglages
      setSize(550, 600);
      setLocationRelativeTo(null);
      setVisible(true);
   }

   /**
    * ActionPerformed read action from user on the window.
    * 
    * @param e The event performed
    */
   public void actionPerformed(ActionEvent e) {
      
       if (e.getSource() == bFermer)
         this.dispose();
       
   }
}