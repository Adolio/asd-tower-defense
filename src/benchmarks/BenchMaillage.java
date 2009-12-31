package benchmarks;

import static java.lang.System.out;

/**
 * Fichier : BenchMaillage.java
 * 
 * <p>
 * But : Faire un benchmark du maillage sur les opérations : Création, Calcul de
 * chemin, Suppression et Ajout.
 * <p>
 * Remarques : Un test standard est proposé également sur des opérations
 * logiques et sur les accès mémoire pour donner un ordre de grandeur aux
 * performances observées. Le garbage collector est également appelé entre
 * chaque test.
 * 
 * @author Pierre-Dominique Putallaz
 * @version 21 déc. 2009
 * @since jdk1.6.0_16
 */
public class BenchMaillage
{
	public static final String VERSION = "0.1";
	
	private static final int BORNE = Integer.MAX_VALUE/4;

	private static double temps;
	private static double coeffAddition, coeffMemoire;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		/*
		 * En tête
		 */
		out.println("************************************");
		out.println("    Maillage : Benchmark v." + VERSION);
		out.println("************************************");
		System.gc();

		/*
		 * Calcul du coefficient de performance standard sur les addition
		 */
		out.println("************************************");
		out.println("Calcul du coefficient d'addition");
		out.println("************************************");
		coeffAddition = additionStandard();
		out.println("Coefficient d'addition : " + coeffAddition);
		System.gc();

		/*
		 * Calcul de l'accès standard à la mémoire
		 */
		out.println("************************************");
		out.println("Calcul du coefficient de mémoire");
		out.println("************************************");
		coeffMemoire = memoireStandard();
		out.println("Coefficient de mémoire : " + coeffMemoire);
		System.gc();

		/*
		 * Création du maillage
		 */
		out.println("************************************");
		out.println("************************************");
		System.gc();

		/*
		 * Dijstra
		 */
		out.println("************************************");
		out.println("************************************");
		System.gc();

		/*
		 * Suppression de zones.
		 */
		out.println("************************************");
		out.println("************************************");
		System.gc();

		/*
		 * Ajout de zones.
		 */
		out.println("************************************");
		out.println("************************************");
		System.gc();

	}

	/**
	 * Lance le chronomètre.
	 */
	private static void start()
	{
		temps = System.currentTimeMillis();
	}

	/**
	 * Arrête le chronomètre et retourne le temps mesuré.
	 * 
	 * @return Le temps en millisecondes
	 */
	private static double stop()
	{
		return System.currentTimeMillis() - temps;
	}

	private static double additionStandard()
	{
		start();
		for (int i = 0, j = 0, k = 0; i < BORNE; ++i)
			for (; k < BORNE; k++)
				for (; j < BORNE; ++j)
				{
					
				}

		return stop();
	}

	private static double memoireStandard()
	{
		start();
		int[][] temp = new int [BORNE][BORNE];
		for(int i = 0;i<BORNE;++i)
		for(int j = 0;j<BORNE;++j)
			temp[i][j] = 1;
		return stop();
	}

}
