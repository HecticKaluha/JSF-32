/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverManagement;

import edges.Edge;
import edges.KochFractal;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefan
 */
public class ServerManager
{
    private static ServerSocket serverSocket;
    private String serverIP;
    
    public static void main(String[] args)
    {
        try
        {
            serverSocket = new ServerSocket(8189);
            
            while(true)
            {
                System.out.println("waiting for connection");
                Socket socket = serverSocket.accept();
                
                Thread thread = new Thread
                (
                    new Runnable()
                    {
                        private ObjectInputStream objectInputStream;
                        private ObjectOutputStream objectOutputStream;
                        private KochFractal kochFractal;

                        private List<Edge> edges = new ArrayList<>();

                        @Override
                        public void run()
                        {
                            System.out.println("Start reading");
                            try
                            {
                                objectInputStream = new ObjectInputStream(socket.getInputStream());
                                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                                System.out.println("waiting for data");
                                Object data = objectInputStream.readObject();
                                System.out.println((String) data);

                                if(data.getClass() == String.class)
                                {
                                    String text = (String) data;

                                    int lvl = Integer.parseInt(text.substring(0, text.indexOf("|")));
                                    System.out.println("LVL: " + lvl);
                                    boolean individual = Boolean.parseBoolean(text.substring(text.indexOf("|") + 1, text.length()));
                                    System.out.println(text.substring(text.indexOf("|") + 1, text.length() - 1));
                                    System.out.println("Individual: " + individual);

                                    kochFractal = new KochFractal(lvl, individual, objectOutputStream);

                                    System.out.println("start calculation");
                                    edges.addAll(kochFractal.generateBottomEdge());
                                    edges.addAll(kochFractal.generateLeftEdge());
                                    edges.addAll(kochFractal.generateRightEdge());
                                    System.out.println("end calculation");

                                    if(!individual)
                                    {
                                        System.out.println("send LIST edge");
                                        objectOutputStream.writeObject(edges);
                                    }
                                }
                            }
                            catch (IOException | ClassNotFoundException ex)
                            {
                                System.out.println("#Failure");
                                Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                );
                
                thread.start();
            }
        } catch (IOException ex)
        {
            Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try
        {       
//            List<Edge> edges = new ArrayList<>();
//            int lvl = Integer.parseInt(args[0]);
//            
//            KochFractal kochFractal = new KochFractal();
//            
//            kochFractal.setLevel(lvl);
//            
//            edges.addAll(kochFractal.generateBottomEdge());
//            edges.addAll(kochFractal.generateLeftEdge());
//            edges.addAll(kochFractal.generateRightEdge());
//            
//            objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(FILEPATH))));
//            
//            timeStampWrite = new TimeStamp();
//            timeStampWrite.setBegin("Schrijven");
//            objectOutputStream.writeInt(kochFractal.getLevel());
//            
//
//            objectOutputStream.writeObject(edges);
//            
//            timeStampWrite.setEnd();
//            System.out.println(timeStampWrite.toString());
//            
//            objectOutputStream.close();
        }
        catch(Exception exc)
        {
            System.out.println("given level: " + args[0] + "is invalid");
            System.out.println(exc.getMessage());
        }
        finally
        {
            
        }
    }
}
