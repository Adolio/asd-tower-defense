package vues;

import java.io.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.*;
import java.net.*;
import java.awt.event.*;
import java.awt.*;

/**
 * Fenetre premettant d'afficher une page HTML.
 * 
 * @author Aurelien Da Campo
 */
class Fenetre_HTML extends JFrame implements ActionListener
{

    private JEditorPane epHTML;
    private static final long serialVersionUID = 1L;
    private JButton bFermer = new JButton("Fermer");

    /**
     * Constructeur
     * 
     * @param titre le titre de la fenetre
     * @param fichier le fichier html local
     * @param parent la fenêtre parent
     */
    public Fenetre_HTML(String titre, File fichier, JFrame parent)
    {

        super(titre);
        setIconImage(parent.getIconImage());
        getContentPane().setBackground(LookInterface.COULEUR_DE_FOND);

        // contenu HTML
        epHTML = new JEditorPane();
        epHTML.setEditable(false);
        epHTML.setBorder(new EmptyBorder(-20, 0, 0, 0));

        URL url = null;

        try
        {
            url = new URL("file:" + fichier.getPath());
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        try
        {
            epHTML.setPage(url);
        } catch (Exception e)
        {
        }

        // ajout du fichier
        epHTML.addHyperlinkListener(new HyperlinkListener()
        {
            public void hyperlinkUpdate(HyperlinkEvent event)
            {
                try
                {
                    if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                    {
                        epHTML.setPage(event.getURL());
                    }
                } catch (IOException e)
                {
                }
            }
        });

        getContentPane().add(new JScrollPane(epHTML), BorderLayout.CENTER);

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

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == bFermer)
            this.dispose();
    }
}