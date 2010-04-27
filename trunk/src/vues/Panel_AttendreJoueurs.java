package vues;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import reseau.Canal;

@SuppressWarnings("serial")
public class Panel_AttendreJoueurs extends JPanel implements ActionListener
{
    private final int MARGES_PANEL = 40;
    private final boolean ADMIN;
    private JFrame parent;
    private JLabel lblPseudo = new JLabel("Pseudo : ");
    private JTextField tfPseudo = new JTextField(10);
    private JButton bDemarrerMaintenant = new JButton("Démarrer maintenant");
    private JLabel lblEtat = new JLabel();
    private JButton bDeconnecter = new JButton("Se Deconnecter");
    private Canal canalServeurEnregistrement;

    
    @SuppressWarnings("serial")
    public Panel_AttendreJoueurs(JFrame parent, Canal canalServeurEnregistrement)
    {
        this.parent = parent;
        this.canalServeurEnregistrement = canalServeurEnregistrement;
        this.ADMIN  = true;
        
        initialiserForm();
    }
    
    @SuppressWarnings("serial")
    public Panel_AttendreJoueurs(JFrame parent)
    {
        this.parent = parent;
        this.ADMIN  = false;
        
        initialiserForm();
    }

    private void initialiserForm()
    {
        // initialisation
        setLayout(new BorderLayout());
        
        parent.setTitle("Attendre des joueurs");
        setBorder(new EmptyBorder(new Insets(MARGES_PANEL, MARGES_PANEL,
                MARGES_PANEL, MARGES_PANEL)));
        
        //---------
        //-- TOP --
        //---------
        JPanel pTop = new JPanel(new BorderLayout());
        pTop.add(new JLabel("ATTENTE DE JOUEURS"), BorderLayout.NORTH);
        

        add(pTop, BorderLayout.NORTH);

        
        //------------
        //-- CENTER --
        //------------
        
        
        //------------
        //-- BOTTOM --
        //------------
        JPanel pBottom = new JPanel(new BorderLayout());
        
        // bouton démarrer
        if(ADMIN)
        {
            bDemarrerMaintenant.setPreferredSize(new Dimension(100,50));
            pBottom.add(bDemarrerMaintenant,BorderLayout.EAST);
            bDemarrerMaintenant.addActionListener(this);
        }
       
        bDeconnecter.addActionListener(this);
        pBottom.add(bDeconnecter,BorderLayout.WEST);
        
         
        if(ADMIN)
            if(canalServeurEnregistrement == null)
            {
                lblEtat.setForeground(Color.RED);
                lblEtat.setText("La connexion avec le serveur central à échouée");
            }
            else
            {
                lblEtat.setForeground(Color.GREEN);
                lblEtat.setText("La connexion avec le serveur central à réussie");
            }
        pBottom.add(lblEtat,BorderLayout.SOUTH);
        
        
        add(pBottom, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        
        if(src == bDemarrerMaintenant)
        {
            //parent.getContentPane().removeAll();
            //parent.getContentPane().add(new Panel_JeuMulti(parent, new Jeu(),new Joueur(new Equipe()), BorderLayout.CENTER); 
            //parent.getContentPane().validate();
        }
        else if(src == bDeconnecter)
        {
            parent.getContentPane().removeAll();
            parent.getContentPane().add(new Panel_MenuPrincipal(parent), BorderLayout.CENTER); 
            parent.getContentPane().validate();
        }
    }
}
