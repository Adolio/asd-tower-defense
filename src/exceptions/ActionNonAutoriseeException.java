package exceptions;

@SuppressWarnings("serial")
public class ActionNonAutoriseeException extends Exception
{
    public ActionNonAutoriseeException(String message)
    {
        super(message);
    }
}
