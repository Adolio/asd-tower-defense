package reseau;

/**
 * Cette classe dérivée de la classe RuntimeException représente une exception
 * pouvant être levée par une méthode de la classe Canal. A cause de la classe
 * parente, il n'est pas obligatoire de "catch" ce type d'exception.
 * 
 * @author lazhar
 * 
 */
public class CanalException extends Exception {
   
   private static final long serialVersionUID = 1L;
   
   /**
    * 
    */
   public CanalException() {
      super();
   }
   
   /**
    * 
    * @param message
    */
   public CanalException(String message) {
      super(message);
   }
   
   /**
    * 
    * @param message
    * @param cause
    */
   public CanalException(String message, Throwable cause) {
      super(message, cause);
   }
   
   /**
    * 
    * @param cause
    */
   public CanalException(Throwable cause) {
      super(cause);
   }
}
