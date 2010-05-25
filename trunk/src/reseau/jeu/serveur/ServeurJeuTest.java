package reseau.jeu.serveur;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServeurJeuTest
{

	ServeurJeu serveurJeu;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	
	@Test
	public void testServeurJeu()
	{
		try
		{
			serveurJeu = new ServeurJeu(null);
			assertNotNull(serveurJeu);
		} catch (IOException e)
		{
			fail();
		}
		
	}

	@Test
	public void testInfos()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testLog()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testUpdate()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testCreatureArriveeEnZoneArrivee()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testCreatureBlessee()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testCreatureTuee()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testEtoileGagnee()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testPartieTerminee()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testVagueEntierementLancee()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testAnimationAjoutee()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testAnimationTerminee()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testCreatureAjoutee()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testJoueurAjoute()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testPartieDemarree()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testTourAmelioree()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testTourPosee()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testTourVendue()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSupprimerJoueur()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testLancerVague()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testPoserTour()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testChangementEtatJoueur()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testChangementEtatPartie()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testAmeliorerTour()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSupprimerTour()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testDireATous()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testDireAuClient()
	{
		fail("Not yet implemented");
	}

}
