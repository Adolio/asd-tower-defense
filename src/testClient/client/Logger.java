/**
 * Logger : Cette classe est une collection de méthodes statiques
 * déstinnées à logger des événements dans une console UNIX.
 */
package testClient.client;

public class Logger {

    /**
     * Constructeur privé, méthode non instanciable
     */
    private Logger(){}
    
    /**
     * Log un message simple
     * @param message Le message à logger
     */
    public static void log(String message) {
	_log(message);
    }

    /**
     * Log une erreur avec un code de couleur rouge
     * @param message Le message d'erreur
     */
    public static void err(String message) {
	log("[Error] " + message);
    }

    /**
     * La méthode de log en soit : affiche simplement en vert le nom du 
     * programme suivit du message à afficher. Méthode privée.
     * @param message Le message à afficher
     */
    private static void _log(String message) {
	System.out.println("[Client]" + message);
    }

}
