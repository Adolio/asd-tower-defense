package exceptions;

@SuppressWarnings("serial")
public class ZoneInaccessibleException extends Exception
{
	public ZoneInaccessibleException(String cause)
	{
		super(cause);
	}
}
