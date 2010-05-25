package serveur.jeu;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class JoueurDistantTest
{
	
	JoueurDistant joueurDistant;
	static int ID = 10;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	@Test
	public void testFinalize()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testJoueurDistant()
	{
		joueurDistant = new JoueurDistant(ID, null, null);
		assertNotNull(joueurDistant);
	}

	@Test
	public void testRun()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetMessage()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSendString()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSendInt()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSignalerNouvelEtat()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testEnvoyerMessageTexte()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testAfficherObjet()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testCouperLeCanal()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testToString()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testMettreEnPause()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testReprendre()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testNomEtat()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testPartieTerminee()
	{
		fail("Not yet implemented");
	}

}
