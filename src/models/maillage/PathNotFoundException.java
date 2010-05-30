package models.maillage;

/**
 * Fichier : PathNotFoundException.java
 * 
 * <p>
 * But : Une classe qui représente une exception levée si le maillage ne trouve
 * pas de chemin valide entre deux points.
 * <p>
 * Remarques : Peut être utile aux classes utilisant le maillage.
 * 
 * @author Lazhar Farjallah
 * @author Pierre-Dominique Putallaz
 * @version 5 déc. 2009
 * @since jdk1.6.0_16
 */
public class PathNotFoundException extends Exception
{
	/**
	 * ID de serialisation.
	 */
	private static final long serialVersionUID = 733621750580911443L;

	/**
	 * Une exception pour un chemin non trouvé.
	 * 
	 * @param message
	 *            Le message voulant être contenu dans l'exception.
	 */
	public PathNotFoundException(final String message)
	{
		super(message);
	}
}
