package serveur.enregistrement;

public class RequeteEnregistrement
{
    public final static String STOP = "{\"donnees\" :{\"code\" : "
            + CodeEnregistrement.STOP + "}}";

    public final static String TEST = "{\"donnees\" :{\"code\" : "
            + CodeEnregistrement.TEST + "}}";

    public final static String NOMBRE_PARTIES = "{\"donnees\" :{\"code\" : "
            + CodeEnregistrement.NOMBRE_PARTIES + "}}";

    public final static String INFOS_PARTIES = "{\"donnees\" :{\"code\" : "
            + CodeEnregistrement.INFOS_PARTIES + "}}";

    public final static String DESENREGISTRER = "{\"donnees\" :{\"code\" : "
            + CodeEnregistrement.DESENREGISTRER + "}}";

    /**
     * Permet de generer la requete d'enregistrement
     * 
     * @return la requete
     */
    public static String getRequeteEnregistrer(String nomServeur,
            String adresseIP, int numeroPort, int nbJoueurs, String nomTerrain,
            String mode)
    {
        // Création de la requete d'enregistrement
        return "{\"donnees\" :{\"code\" : " + CodeEnregistrement.ENREGISTRER
                + ",\"contenu\" : " + "{" + "\"nomPartie\" :\"" + nomServeur
                + "\"," + "\"adresseIp\" :\"" + adresseIP + "\","
                + "\"numeroPort\" :" + numeroPort + "," + "\"capacite\" :"
                + nbJoueurs + "," + "\"nomTerrain\" :\"" + nomTerrain + "\","
                + "\"mode\" :\"" + mode + "\"" + "}}}";
    }

}
