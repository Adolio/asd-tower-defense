package models.maillage;
/**
 * Fichier : PathNotFoundException.java
 * 
 * <p> But : 
 * <p> Remarques :
 *  
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @author Pierre-Dominique Putallaz
 * @version 5 déc. 2009
 * @since jdk1.6.0_16 
 */

public class PathNotFoundException extends Exception
{
	/**
	 * Une exception pour un chemin non trouvé.
	 * @param message Le message voulant être contenu dans l'exception.
	 */
	public PathNotFoundException(final String message){
		super(message);
	}
}
