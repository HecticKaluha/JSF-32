package socketclient_atomicclock_1;
/**
   @version 1.20 2004-08-03
   @author Cay Horstmann
*/

import java.io.*;
import java.net.*;
import java.util.*;

/**
   This program makes a socket connection to the atomic clock
   in Boulder, Colorado, and prints the time that the 
   server sends.
   * 
   * Also try this using Telnet
*/
public class SocketTest
{  
   public static void main(String[] args)
   {  
      try
      {  
         Socket s = new Socket("time-A.timefreq.bldrdoc.gov", 13);
         try
         {
             // only receives data from server, so only inputstream needed.
            InputStream inStream = s.getInputStream();
            Scanner in = new Scanner(inStream);
            
            while (in.hasNextLine())
            {  
               String line = in.nextLine();
               System.out.println(line);
            }
         }
         finally
         {
            s.close();
         }
      }
      catch (IOException e)
      {  
         e.printStackTrace();
      }
   }
}
