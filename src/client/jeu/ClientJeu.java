package client.jeu;

import java.net.ConnectException;

import reseau.CanalTCP;
import reseau.CanalException;
import reseau.jeu.serveur.ConstantesServeurJeu;

import org.json.*;


/* 
 * DES IDEES : (DE AURELIEN)
 * 
 * 1) THREAD D'ECOUTE ET NOTIFICATIONS
 * 
 * TU DEVRAS SUREMENT CREER UN THREAD DEDIE A L'ECOUTE DU SERVEUR QUI NE FAIT QUE 
 * D'ATTENDRE DES INFOS DU SERVEUR. 
 * 
 * UN FOIS UNE INFO RECUPEREE, TU TROUVE LA BONNE BRANCHE DANS UN GROS 
 * SWITCH ET LA TU VAS NOTIFIER TES ECOUTEURS (MOI J'AI BESOINS DE T'ECOUTER POUR
 * ETRE AU COURANT QUAND QQCHOSE CE PASSE...)
 * 
 * 
 * 2) PENSER QUE LE SERVEUR DOIT TE RETOURNER L'ID DE LA TOUR SI
 * ELLE EST POSABLE... ON EN A BESOINS POUR LA SUITE.
 */
public class ClientJeu implements ConstantesServeurJeu {
	//TODO final int ID;
	CanalTCP canal;
	
	
	/*
	 * FIXME (DE AURELIEN) NON L'ID DU JOUEUR JE NE LE CONNAIS PAS ENCORE
	 * C'EST A TOI DE LE DEMANDER AU SERVEUR (IL DOIT TE LE RETOURNER LORSQUE TU LUI
	 * DEMANDE DE REJOINDRE SA PARTIE...)
	 * 
	 * IP_SERVEUR SERA UN PARAMETRE...
	 * PORT_SERVEUR SERA UN PARAMETRE...
	 */
	public ClientJeu(String IPServeur, int portServeur) {
		
		try
		{
			canal = new CanalTCP(IPServeur, portServeur, true);
			//canal.envoyerInt(ID);
			canal.recevoirString();
			
			//TODO Recevoir l'id! :D
		} catch (ConnectException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CanalException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	//TODO controler les betises de l'expediteur (guillemets, etc..)
	public void envoyerMessage(String message, int cible) throws CanalException, JSONException{
		
		JSONObject json = new JSONObject();
		json.put("TYPE", MSG);
		JSONObject content = new JSONObject();
		content.put("CIBLE", A_TOUS);
		content.put("MESSAGE", "foo bar");
		json.put("CONTENU", content);
		
		canal.envoyerString(json.toString());
	}
	
	
	//TODO ID reelement necessaire? 
	// (DE AURELIEN) ... NON CAR LE SERVEUR CONNAIT LE CLIENT AVEC LEQUEL IL COMMUNIQUE
	public void envoyerEtatJoueur(int etat) throws JSONException, CanalException{
	
		JSONObject json = new JSONObject();
		json.put("TYPE", JOUEUR_ETAT);
		json.put("ETAT", etat);
		
		canal.envoyerString(json.toString());
	}
	
	//TODO revoir le nom des parametres
	// (DE AURELIEN) PARAMETRES :
	// int nbCreatures
	// int typeCreature
	public void envoyerVague(int nbCreature, int typeCreature) 
	    throws JSONException, CanalException
	{
		
		JSONObject json = new JSONObject();
		json.put("TYPE", VAGUE);
		json.put("TYPE_WAVE", typeCreature);
		json.put("SIZE_WAVE", nbCreature);
		
		canal.envoyerString(json.toString());
	}
	
	public void envoyerEtatPartie(int etat) throws JSONException, CanalException{
		
		JSONObject json = new JSONObject();
		//TODO GAME au lieu de PLAY?
		json.put("TYPE", PARTIE_ETAT);
		json.put("ETAT", etat);
		
		canal.envoyerString(json.toString());
		
	}
	
	public void demanderCreationTour(int x, int y, int type) 
	throws JSONException, CanalException
	{
		
		JSONObject json = new JSONObject();
		json.put("TYPE", TOUR_AJOUT);
		json.put("X", x);
		json.put("Y", y);
		//TODO regarder pour le doublon
		json.put("SORT", type);
		
		canal.envoyerString(json.toString());
	}
	
	public void demanderAmeliorationTour(int idTour) 
	throws JSONException, CanalException{
		
		JSONObject json = new JSONObject();
		json.put("TYPE", TOUR_AMELIORATION);
		json.put("ID_TOWER", idTour);
		
		canal.envoyerString(json.toString());
	}
	
	//TODO suppressionTour ou venteTour?
	// (DE AURELIEN) ... VENTE CAR ON REGAGNE DE LA TUNE COTE SERVEUR!
	// PAR CONTRE TU RECEVERA UNE SUPPRESSION DE TOUR DE LA PART SERVEUR.
	public void venteTour(int idTour) 
	throws JSONException, CanalException
	{
		
		JSONObject json = new JSONObject();
		//TODO TOWER_SELL au lieu de TOWER_DEL?
		// (DE AURELIEN) ... EFFECTIVEMENT! MAIS TOWER_DEL EN RETOUR DU SERVEUR
		json.put("TYPE", TOUR_SUPRESSION);
		json.put("ID_TOWER", idTour);
		
		canal.envoyerString(json.toString());
		
	}
	
	
	//TODO transformer en thread
	public void receptionMessages(){
		JSONObject mes;
		try {
			mes = new JSONObject(canal.recevoirString());
			
			switch(mes.getInt("TYPE")){
				case CHEMIN_BLOQUE :
					break;
				default :
						
			}
		} catch (CanalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
