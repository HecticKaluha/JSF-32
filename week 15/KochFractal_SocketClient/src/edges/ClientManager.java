/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edges;

import gui.KochFractalGui;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author Milton van de Sanden
 */
public class ClientManager
{
    KochFractalGui kochFractalGui;
    
    private String serverIP;
    
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    
    private List<Edge> edges = new ArrayList<>();
    
    public List<Edge> getEdges()
    {
        return edges;
    }
    
    public ClientManager(String serverIP, KochFractalGui kochFractalGui)
    {
        if(serverIP == null)
        {
            throw new IllegalArgumentException();
        }
        
        if(serverIP.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        if(kochFractalGui == null)
        {
            throw new IllegalArgumentException();
        }
        
        this.serverIP = serverIP;
        this.kochFractalGui = kochFractalGui;
        
        try
        {
            socket = new Socket(serverIP, 8189);
            
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        }
        catch (UnknownHostException ex)
        {
            Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void requestEdges(int lvl, boolean individual)
    {
        try
        {
            System.out.println(String.valueOf(individual));
            objectOutputStream.writeObject("calculate/" + lvl + "|" + String.valueOf(individual));
            Thread thread = new Thread
            (            
                new Runnable()
                {
                    private boolean isDone = false;

                    @Override
                    public void run()
                    {
                        while(!isDone)
                        {
                            try
                            {
                                Object object = objectInputStream.readObject();

                                if(object.getClass() == Edge.class)
                                {
                                    receiveData((Edge) object);
                                }
                                else if(object.getClass() == String.class)
                                {
                                    isDone = true;
                                }
                                else
                                {
                                    receiveData((List<Edge>) object);
                                }
                            }
                            catch (IOException | ClassNotFoundException ex)
                            {
                                Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            );
            
            thread.start();
        }
        catch (IOException ex)
        {
            Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void receiveData(final Edge edge)
    {
        edges.add(edge);
        
        Platform.runLater
        (
            new Runnable()
            {
                @Override
                public void run()
                {
                    kochFractalGui.drawEdge(edge);
                }
            }
        );
    }
    
    public void receiveData(List<Edge> edges)
    {
        for(Edge edge : edges)
        {
            receiveData(edge);
        }
    }
    
    public void zoom(double zoom, double zoomTranslateX, double zoomTranslateY)
    {
        System.out.println("ZOOM2");
        try
        {
            objectOutputStream.writeObject("zoom/");
//             + edges + "!" + zoom + "@" + zoomTranslateX + "#" + zoomTranslateY
            objectOutputStream.writeObject(edges);
//            objectOutputStream.writeObject(zoom + "!" + zoomTranslateX + "@" + zoomTranslateY);
            objectOutputStream.writeDouble(zoom);
            objectOutputStream.writeDouble(zoomTranslateX);
            objectOutputStream.writeDouble(zoomTranslateY);
            
            Thread thread = new Thread
            (            
                new Runnable()
                {
                    private boolean isDone = false;

                    @Override
                    public void run()
                    {
                        while(!isDone)
                        {
                            try
                            {
                                Object object = objectInputStream.readObject();

                                if(object.getClass() == Edge.class)
                                {
                                    System.out.println("receivedEdge");
                                    receiveData((Edge) object);
                                }
                                else if(object.getClass() == String.class)
                                {
                                    System.out.println("Done");
                                    isDone = true;
                                }
                                else
                                {
                                    System.out.println("Received list 2");
                                    receiveData((List<Edge>) object);
                                }
                            }
                            catch (IOException | ClassNotFoundException ex)
                            {
                                 System.out.println("Fail");
                                Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            );
            System.out.println("starting thread");
            thread.start();

        }
        catch (IOException ex)
        {
            System.out.println("ioexception");
            Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
////    public void readEdges()
////    {
////        try
////        {
////            ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(PATH))));
////                        
////            int lvl = objectInputStream.readInt();
////            
////            for(Edge edge : (List<Edge>) objectInputStream.readObject())
////            {
////                drawEdge(edge);
////            }
////            
////        } catch (FileNotFoundException ex) {
////            Logger.getLogger(KochFractalGui.class.getName()).log(Level.SEVERE, null, ex);
////        } catch (IOException | ClassNotFoundException ex) {
////            Logger.getLogger(KochFractalGui.class.getName()).log(Level.SEVERE, null, ex);
////        }
//    }
}
