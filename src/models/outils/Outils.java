package models.outils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Class utilitaire
 * <p>
 * Elle fournie plusieurs methodes pratiques et souvent inclassables.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 15 decembre 2009
 * @since jdk1.6.0_16
 */
public class Outils
{
	/**
	 * Methode utilitaire pour changer la taille d'une image
	 * 
	 * @param src l'image originale
	 * @param largeur largeur de l'image reduite
	 * @param hauteur hauteur de l'image reduite
	 * @return une copie redimentionnée de l'image originale
	 * @see Image
	 */
	public static Image redimentionner(Image src, int largeur, int hauteur) 
	{
        // creation d'une nouvelle image de la taille souhaitee
		BufferedImage dst 	= new BufferedImage(largeur, hauteur,BufferedImage.TYPE_INT_ARGB);
        
		// dessin de l'image originale dans le graphics de la nouvelle image
		Graphics2D g2 = dst.createGraphics();
        g2.drawImage(src, 0, 0, largeur, hauteur, null);
        g2.dispose(); // restauration memoire 
        
        return dst;
    }
	
	/**
	 * Permet d'arrondir une valeur reelle a un certain nombre de decimales
	 * @param nombre le nombre a arrondir
	 * @param nbDecimales le nombre de decimales souhaitees
	 * @return le nombre arrondi a nbDecimal decimales
	 */
	public static double arrondir(double nombre, int nbDecimales)
	{
	    int mult10 = (int) Math.pow(10,nbDecimales);
	    return (double) Math.round(nombre * mult10) / mult10;
	}
}
