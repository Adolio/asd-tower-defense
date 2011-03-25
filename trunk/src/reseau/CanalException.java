/*
  Copyright (C) 2010 Aurelien Da Campo, Lazhar Farjallah
  
  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/

package reseau;

/**
 * Cette classe dérivée de la classe RuntimeException représente une exception
 * pouvant être levée par une méthode de la classe Canal. A cause de la classe
 * parente, il n'est pas obligatoire de "catch" ce type d'exception.
 * 
 * @author Lazhar Farjallah
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
