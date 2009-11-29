package vues;

import models.tours.Tour;

public interface EcouteurOperationSurTour
{
	public void tourAjoutee(Tour tour);
	public void ameliorerTour(Tour tour);
	public void vendreTour(Tour tour);
}
