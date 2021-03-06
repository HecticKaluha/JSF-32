package clientserver_objectstreams_3;
/*
 * run in cmd in bin directory: java clientserver/ObjectStreamClient
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ObjectStreamClient
{
    public static void main(String[] args)
    {  
       try
       {  
          Socket s = new Socket("localhost", 8189);
          try
          {
             OutputStream outStream = s.getOutputStream();
             InputStream inStream = s.getInputStream();

             // Let op: volgorde is van belang!
             ObjectOutputStream out = new ObjectOutputStream(outStream);
             ObjectInputStream in = new ObjectInputStream(inStream);
             //
             // Simulatie clientsessie
             //
             // ontvang welkomsboodschap
            String result = (String)in.readObject();
             System.out.println(result.toString());


             // send String
             System.out.println("sending string");
             out.writeObject("Zomaar een String");
             out.flush();
             result = (String)in.readObject();
             System.out.println("ontvangen antwoord: "+ result.toString());

             // send object 
             Persoon me = new Persoon("Erik", 1966, 11);
             System.out.println("sending ME: "+me.toString());
             out.writeObject(me);
             Persoon reply = (Persoon)in.readObject();
             System.out.println("ontvangen antwoord: "+ reply.toString());

             // close
             out.writeObject("BYE");
             out.flush();
             result = (String)in.readObject();
             System.out.println("ontvangen antwoord: "+ result.toString());


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
       catch (ClassNotFoundException nfe){
           nfe.printStackTrace();
       }
    }
 }
