/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;

/**
 *
 * @author Stefan
 */
public class EdgeGenerator extends Task<ArrayList<Edge>> implements Observer
{
    
    private KochManager kochManager;
    private KochFractal kochFractal;
    private Object stefan;
    private int side;
    private ArrayList<Edge> edges;
    
    private CyclicBarrier cb;
    
    public EdgeGenerator(int side, KochManager kochManager, /*int level*/ KochFractal kochFractal, Object stefan, CyclicBarrier cb)
    {
        this.side = side;
        this.kochManager = kochManager;
        
        this.kochFractal = new KochFractal();//kochFractal;
        edges = new ArrayList<>();
        
        this.kochFractal.setLevel(kochFractal.getLevel());
        this.kochFractal.addObserver(this);
        this.stefan = stefan;
        this.kochFractal.setEdgeGenerator(this);
        
        this.cb = cb;
    }
  
    @Override
    public void update(Observable o, Object arg)
    {
       Edge edge = (Edge) arg;
       final Edge e2 = new Edge(edge.X1, edge.Y1, edge.X2, edge.Y2, Color.WHITE);
       edges.add(edge);
       
        try {
            Thread.sleep(5 + side);
        } catch (InterruptedException ex) {
            Logger.getLogger(EdgeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        Platform.runLater(
            new Runnable()
            {
                @Override
                public void run()
                {
                    kochManager.getApplication().drawEdge(e2);
                }
            }
        );
    }

    @Override
    public ArrayList<Edge> call()
    {
        switch(side)
        {
            case 0:
                kochFractal.generateLeftEdge();
                break;
            case 1:
                kochFractal.generateRightEdge();
                break;
            default:
                kochFractal.generateBottomEdge();
                break;
        }
        if(!isCancelled())
        {
            kochManager.addEdges(edges);
            try {
                if(cb.await() == 0)
                {
                    kochManager.endCalcTime();
                    kochManager.getApplication().requestDrawEdges();
                }
            } catch (InterruptedException | BrokenBarrierException ex) {
                Logger.getLogger(EdgeGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
        
        return null;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning)
    {
        kochFractal.cancel();
        return super.cancel(mayInterruptIfRunning); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateProgress(double workDone, double max) {
        super.updateProgress(workDone, max); //To change body of generated methods, choose Tools | Templates.
    }

    public KochManager getKochManager() {
        return kochManager;
    }

    public int getSide() {
        return side;
    }

    
    
}
