package vues;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.ConnectException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import models.joueurs.Joueur;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import outils.fichierDeConfiguration;
import reseau.Canal;
import reseau.CanalException;
import serveur.enregistrement.CodeEnregistrement;
import serveur.enregistrement.RequeteEnregistrement;

@SuppressWarnings("serial")
public class Panel_RejoindrePartieMulti extends JPanel implements
        ActionListener, KeyListener, MouseListener
{
    private static int PORT_SE;
    private static int PORT_SJ;
    private static String IP_SE;
    
    private final int MARGES_PANEL = 40;
    private JFrame parent;

    private DefaultTableModel model = new DefaultTableModel();
    private JTable tbServeurs;
    private ArrayList<ServeurInfo> serveurs = new ArrayList<ServeurInfo>();

    private String filtre = "";
    private static final String FILTRE_DEFAUT = "Filtre";
    private JTextField tfFiltre = new JTextField(FILTRE_DEFAUT);

    private JLabel lblConnexionParIP = new JLabel("Connexion par IP : ");
    private JTextField tfConnexionParIP = new JTextField("127.0.0.1",10);

    private JLabel lblPseudo = new JLabel("Pseudo : ");
    private JTextField tfPseudo = new JTextField("Joueur",10);

    private JButton bRejoindre = new JButton("Rejoindre");
    private JButton bRafraichir = new JButton("Rafraichir");
    
    private JLabel lblEtat = new JLabel();

    private JButton bAnnuler = new JButton("Annuler");

    private Canal canalServeurEnregistrement;
    
    private fichierDeConfiguration config;
    
    
    /**
     * Classe interne pour stocker les informations d'un serveur
     */
    private class ServeurInfo
    {

        private String nom, IP, Mode, nomTerrain;
        private int port, nbPlaces = 0, placesLibres = 0;

        /**
         * Constructeur
         * 
         * @param nom le nom
         * @param IP l'adresse IP
         * @param port le numéro du port
         * @param Mode le mode de jeu
         * @param nomTerrain le nom du terrain
         * @param nbPlaces le nombre de joueurs
         * @param placesLibres les places restantes
         */
        public ServeurInfo(String nom, String IP, int port, String Mode,
                String nomTerrain, int nbPlaces, int placesLibres)
        {
            this.nom = nom;
            this.IP = IP;
            this.port = port;
            this.Mode = Mode;
            this.nomTerrain = nomTerrain;
            this.nbPlaces = nbPlaces;
            this.placesLibres = placesLibres;
        }

        /**
         * Permet de savoir si l'un des champs du serveur sontient une chaine
         * particulière.
         * 
         * @param s le pattern
         * @return true si le serveur contient bien la chaine, sinon false.
         */
        public boolean contientLaChaine(String s)
        {
            s = s.toLowerCase();

            if (nom.toLowerCase().indexOf(s) != -1)
                return true;
            if (IP.toLowerCase().indexOf(s) != -1)
                return true;
            if (Mode.toLowerCase().indexOf(s) != -1)
                return true;
            if (nomTerrain.toLowerCase().indexOf(s) != -1)
                return true;

            return false;
        }

        /**
         * Permet de recuperer les informations sous la forme d'un tableau de
         * String pour les mettre ensuite dans une JTable
         * 
         * @return un tableau de String pour une JTable
         */
        String[] toStringArray()
        {
            return new String[] { nom, IP, port+"", Mode, nomTerrain,
                    placesLibres + " / " + nbPlaces };
        }
    }

    /**
     * Constructeur
     * 
     * @param parent le fenetre parent
     */
    public Panel_RejoindrePartieMulti(JFrame parent)
    {
        // initialisation
        super(new BorderLayout());
        this.parent = parent;
        parent.setTitle("Rejoindre une partie multijoueurs");
        setBorder(new EmptyBorder(new Insets(MARGES_PANEL, MARGES_PANEL,
                MARGES_PANEL, MARGES_PANEL)));
        setBackground(LookInterface.COULEUR_DE_FOND);
        
        
        // recuperation des configurations
        config  = new fichierDeConfiguration("cfg/config.cfg");
        IP_SE   = config.getProprety("IP_SE");
        PORT_SE = Integer.parseInt(config.getProprety("PORT_SE"));
        PORT_SJ = Integer.parseInt(config.getProprety("PORT_SJ"));
            
            
        // ---------
        // -- TOP --
        // ---------
        JPanel pTop = new JPanel(new BorderLayout());
        pTop.setBackground(LookInterface.COULEUR_DE_FOND);
        
        JLabel titre = new JLabel("REJOINDRE UNE PARTIE");
        titre.setFont(GestionnaireDesPolices.POLICE_TITRE);
        pTop.add(titre, BorderLayout.NORTH);
        

        // filtre
        JPanel pADroite = new JPanel(new BorderLayout());
        pADroite.setBackground(LookInterface.COULEUR_DE_FOND);

        tfFiltre.setPreferredSize(new Dimension(100, 25));
        tfFiltre.addKeyListener(this);
        tfFiltre.addMouseListener(this);

        pADroite.add(tfFiltre, BorderLayout.WEST);
        pTop.add(pADroite, BorderLayout.CENTER);
        GestionnaireDesPolices.setStyle(bRafraichir);
        pTop.add(bRafraichir, BorderLayout.EAST);
        bRafraichir.addActionListener(this);
         
        add(pTop, BorderLayout.NORTH);

        // ------------
        // -- CENTER --
        // ------------

        // création de la table avec boquage des editions
        tbServeurs = new JTable(model)
        {
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                return false; // toujours désactivé
            }
        };

        // Simple selection
        tbServeurs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // nom de colonnes
        model.addColumn("Nom");
        model.addColumn("IP");
        model.addColumn("Port");
        model.addColumn("Mode");
        model.addColumn("Terrain");
        model.addColumn("Places dispo.");

        // Création du canal avec le serveur d'enregistrement
        try
        {
            canalServeurEnregistrement = new Canal(IP_SE,PORT_SE,true);
            
            mettreAJourListeDesServeurs();
        } 
        catch (ConnectException e)
        {
            connexionSEImpossible();          
        } 
        catch (CanalException e) 
        {
            connexionSEImpossible();   
        }
        
        // ajout dans le panel
        add(new JScrollPane(tbServeurs), BorderLayout.CENTER);

        // ------------
        // -- BOTTOM --
        // ------------
        JPanel pBottom = new JPanel(new BorderLayout());
        pBottom.setBackground(LookInterface.COULEUR_DE_FOND);
        
        bAnnuler.addActionListener(this);
        GestionnaireDesPolices.setStyle(bAnnuler);
        pBottom.add(bAnnuler, BorderLayout.WEST);

        JPanel bottomCenter = new JPanel();
        bottomCenter.setBackground(LookInterface.COULEUR_DE_FOND);
        
        // connexion par IP 
        lblConnexionParIP.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        lblConnexionParIP.setForeground(GestionnaireDesPolices.COULEUR_SOUS_TITRE);
        bottomCenter.add(lblConnexionParIP);
        tfConnexionParIP.setPreferredSize(new Dimension(100, 25));
        bottomCenter.add(tfConnexionParIP);
        tfConnexionParIP.addMouseListener(this);

        // pseudo
        JPanel pPseudo = new JPanel();
        JPanel pTmp = new JPanel();
        
        lblPseudo.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        lblPseudo.setForeground(GestionnaireDesPolices.COULEUR_SOUS_TITRE);
        bottomCenter.add(lblPseudo);
        bottomCenter.add(tfPseudo);
        pPseudo.add(pTmp, BorderLayout.EAST);
        pBottom.add(bottomCenter, BorderLayout.CENTER);

        // bouton rejoindre
        bRejoindre.setPreferredSize(new Dimension(100, 50));
        GestionnaireDesPolices.setStyle(bRejoindre);
        pBottom.add(bRejoindre, BorderLayout.EAST);
        bRejoindre.addActionListener(this);

        pBottom.add(lblEtat, BorderLayout.SOUTH);

        add(pBottom, BorderLayout.SOUTH);
    }

    /**
     * Permet d'informer l'utilisateur que la connexion n'a pas été établie
     */
    private void connexionSEImpossible()
    {
        tbServeurs.setEnabled(false);
        bRafraichir.setEnabled(false);
        tfFiltre.setEnabled(false);
        lblEtat.setForeground(GestionnaireDesPolices.COULEUR_ERREUR);
        lblEtat.setText("Connection au serveur central impossible! Entrez directement l'IP du serveur du jeu.");
    }

    /**
     * Permet de demander la liste des serveurs au serveur d'enregistrement
     * et de mettre a jour la liste des serveurs
     */
    private void mettreAJourListeDesServeurs()
    {
        if(canalServeurEnregistrement != null)
        {
            // vidage de la table
            viderTable();
            
            // envoie de la requete d'enregistrement
            canalServeurEnregistrement.envoyerString(RequeteEnregistrement.INFOS_PARTIES);
            
            // attente du résultat
            String resultat = canalServeurEnregistrement.recevoirString();
    
            // mise à jour de la liste des serveurs
            mettreAJourListeDepuisJSON(resultat);
        }
    }

    /**
     * Permet de mettre a jour la liste des serveurs avec une réponse JSON
     * 
     * @param resultatJSON le résultat du serveur d'enregistrement
     */
    private void mettreAJourListeDepuisJSON(String resultatJSON)
    {
        try
        {
            // Analyse de la réponse du serveur d'enregistrement
            JSONObject jsonResultat = new JSONObject(resultatJSON);
            
            // on vide la liste des serveurs
            serveurs.clear();
            
            if(jsonResultat.getInt("status") == CodeEnregistrement.OK)
            {
                // sélection des serveurs de jeu
                JSONArray jsonArray = jsonResultat.getJSONArray("parties");
                
                // ajout des serveurs de jeu
                for(int i=0;i < jsonArray.length(); i++)
                {
                    JSONObject serveur = jsonArray.getJSONObject(i);
                    
                    ajouterServeur(serveur.getString("nomPartie"), 
                                   serveur.getString("adresseIp"),
                                   serveur.getInt("numeroPort"), 
                                   serveur.getString("mode"), 
                                   serveur.getString("nomTerrain"),
                                   serveur.getInt("capacite"), 
                                   serveur.getInt("placesRestantes"));
  
                }
                
                lblEtat.setForeground(GestionnaireDesPolices.COULEUR_SUCCES);
                lblEtat.setText("Connexion au serveur central établie!");
            }
            else
            {
                lblEtat.setForeground(GestionnaireDesPolices.COULEUR_SUCCES);
                lblEtat.setText("Connexion au serveur central établie! [ Aucun serveur disponible pour le moment ]");
            }
        } 
        catch (JSONException e1)
        {
            lblEtat.setForeground(GestionnaireDesPolices.COULEUR_ERREUR);
            lblEtat.setText("Format de réponse du serveur incorrect!");
        }
    }

    /**
     * Permet d'ajouter un serveur
     * 
     * @param nom le nom
     * @param IP l'adresse IP
     * @param port le numéro du port
     * @param Mode le mode de jeu
     * @param nomTerrain le nom du terrain
     * @param nbPlaces le nombre de joueurs
     * @param placesLibres les places restantes
     */
    public void ajouterServeur(String nom, String IP, int port, String Mode,
            String nomTerrain, int nbPlaces, int placesLibres)
    {

        ServeurInfo srvInfo = new ServeurInfo(nom, IP, port, Mode, nomTerrain,
                nbPlaces, placesLibres);

        // ajout à la liste des serveurs
        serveurs.add(srvInfo);

        // ajout au tableau s'il correspond au filtre
        if (filtre.isEmpty() || !filtre.isEmpty()
                && srvInfo.contientLaChaine(filtre))
            model.addRow(srvInfo.toStringArray());
    }

    /**
     * Permet de mettre à jour la table en fonction de la liste des serveurs
     */
    private void miseAJourListe()
    {
        // nettoyage de la table
        viderTable();

        // recuperation du filtre
        filtre = tfFiltre.getText();

        // ajout des serveurs dans la table s'il respect le filtre
        for (ServeurInfo srvInfo : serveurs)
            if (filtre.equals(FILTRE_DEFAUT) || srvInfo.contientLaChaine(filtre))
                model.addRow(srvInfo.toStringArray());
    }

    /**
     * Permet de vider la table des serveurs
     */
    private void viderTable()
    {
        // nettoyage de la table
        while (model.getRowCount() != 0)
            model.removeRow(0);
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();

        if (src == bRejoindre)
        {
            lblEtat.setText("");

            try
            {
                if (tfPseudo.getText().isEmpty())
                    throw new Exception("Erreur : Pseudo vide.");
                
                connexion(recupererIP(),recupererPort());
            } 
            catch (Exception exception)
            {
                lblEtat.setForeground(GestionnaireDesPolices.COULEUR_ERREUR);
                lblEtat.setText(exception.getMessage());
            }
        } 
        else if(src == bRafraichir)
        {
            mettreAJourListeDesServeurs();  
        }
        else if (src == bAnnuler)
        {
            parent.getContentPane().removeAll();
            parent.getContentPane().add(new Panel_MenuPrincipal(parent),
                    BorderLayout.CENTER);
            parent.getContentPane().validate();
            
            // fermeture du canal s'il est ouvert
            if(canalServeurEnregistrement != null)
            {
                try
                {
                    // fermeture propre du canal
                    canalServeurEnregistrement.envoyerString(RequeteEnregistrement.STOP);
                    canalServeurEnregistrement.recevoirString();
                }
                // il y a eu une erreur... on quitte tout de même
                catch(CanalException ce){}
                
                canalServeurEnregistrement.fermer();
            }
        }
    }

    /**
     * Permet de recupérer l'IP en fonction de l'état des champs du formulaire
     * 
     * @return l'adresse IP du serveur selectionné par l'utilisateur
     * @throws Exception s'il y des erreurs de saisie
     */
    private String recupererIP() throws Exception
    {
        // si selectionné
        if (tbServeurs.getSelectedRow() != -1)
            return (String) model.getValueAt(tbServeurs.getSelectedRow(), 1);
        
        // sinon on retourne l'ip manuelle si elle est valide
        else if (tfConnexionParIP.getText().isEmpty())
            throw new Exception("Erreur : Selectionnez un serveur "
                    + "ou entrez directement l'IP du serveur.");
        
        else if(!checkIp(tfConnexionParIP.getText()))
            throw new Exception("Erreur : Format IP incorrect");
        else
            return tfConnexionParIP.getText();
    }
    
    /**
     * Permet de controler si une ip est valide
     * 
     * @param ip
     * @return true si elle est correcte false sinon
     */
    public static boolean checkIp (String ip)
    {
        String [] parts = ip.split("\\.");
        
        if(parts.length != 4)
            return false;
        
        for (String s : parts)
        {
            int i = Integer.parseInt (s);

            if (i < 0 || i > 255)
                return false;
        }
        return true;
    }

    /**
     * Permet de recupérer le port en fonction de l'état des champs du formulaire
     * 
     * @return le port du serveur selectionné par l'utilisateur
     * @throws Exception s'il y des erreurs de saisie
     */
    private int recupererPort() throws Exception
    {
        if (tbServeurs.getSelectedRow() != -1)
            return Integer.parseInt((String) model.getValueAt(tbServeurs.getSelectedRow(),2));  
        else
            return PORT_SJ;
    } 
    
    

    /**
     * Etablisssement d'une connexion avec le serveur
     * 
     * @param IP l'adresse ip du serveur
     */
    private void connexion(String IP, int port)
    {
        bRejoindre.setText("Connexion...");
        bRejoindre.setEnabled(false);

        // TODO connexion au serveur, demande d'acceptation dans la partie...
        try
        {
            lblEtat.setForeground(GestionnaireDesPolices.COULEUR_INFO);
            lblEtat.setText("Tentative de connexion au serveur "+IP+"...");
            
            Canal canalServeurJeu = new Canal(IP,port,true);
            
            
            
            
            
        } 
        catch (ConnectException e)
        {
            bRejoindre.setText("Rejoindre");
            bRejoindre.setEnabled(true);
            
            lblEtat.setForeground(GestionnaireDesPolices.COULEUR_ERREUR);
            lblEtat.setText("Connection au serveur de jeu impossible");
        } 
        catch (CanalException e)
        {
            bRejoindre.setText("Rejoindre");
            bRejoindre.setEnabled(true);
            
            lblEtat.setForeground(GestionnaireDesPolices.COULEUR_ERREUR);
            lblEtat.setText("Connection au serveur de jeu impossible");
        }
        
        
        // TODO dans le try !!!
        Joueur joueur = new Joueur(lblPseudo.getText());
        
        // connexion réussie
        parent.getContentPane().removeAll();
        parent.getContentPane().add(new Panel_AttendreJoueurs(parent, null, false, joueur),
                BorderLayout.CENTER);
        parent.getContentPane().validate();
        
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        miseAJourListe();
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        Object src = e.getSource();

        if (src == tfFiltre)
        {
            if (tfFiltre.getText().equals(FILTRE_DEFAUT))
                tfFiltre.setText("");
        } else if (src == tfConnexionParIP)
        {
            tbServeurs.clearSelection();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
    }
}
