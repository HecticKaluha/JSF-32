/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import jsf31kochfractalfx.JSF31KochFractalFX;
import timeutil.TimeStamp;

/**
 *
 * @author Stefan
 */
public class KochManager
{
    private JSF31KochFractalFX application;
    //private KochFractal kochFractal;
    private KochFractal kochFractal1 = new KochFractal();
    private KochFractal kochFractal2 = new KochFractal();
    private KochFractal kochFractal3 = new KochFractal();
    
    private EdgeGenerator gen1;
    private EdgeGenerator gen2;
    private EdgeGenerator gen3;
    
    private ExecutorService pool = Executors.newFixedThreadPool(3);
    private CyclicBarrier cb = new CyclicBarrier(3);

    
    private TimeStamp timeStampCalc;// = new TimeStamp();
    private TimeStamp timeStampDraw = new TimeStamp();
    private Object stefan;
    private int next;
    
    private ArrayList<Edge> edges = new ArrayList<>();
     
    public KochManager(JSF31KochFractalFX application, Object stefan)
    {
        this.application = application;
        this.stefan = stefan;
    }
        
    public JSF31KochFractalFX getApplication()
    {
        return application;
    }
    
    public void changeLevel(int nxt)
    {
        if(gen1 != null)
        {
            gen1.cancel(false);
        }
        if(gen2 != null)
        {
            gen2.cancel(false);
        }
        if(gen3 != null)
        {
            gen3.cancel(false);
        }
        
        cb.reset();
        
        kochFractal1.setLevel(nxt);
        kochFractal2.setLevel(nxt);
        kochFractal3.setLevel(nxt);
        this.next = nxt;
        
        edges.clear();
        Platform.runLater
        (
            new Runnable()
            {
                @Override
                public void run()
                {
                    application.clearKochPanel();
                }
            }
        );

        gen1 = new EdgeGenerator(0, this, kochFractal1, stefan, cb);
        gen2 = new EdgeGenerator(1, this, kochFractal1, stefan, cb);
        gen3 = new EdgeGenerator(2, this, kochFractal1, stefan, cb);
        
        application.getProgressbarleft().progressProperty().bind(gen1.progressProperty());
        application.getProgressbarright().progressProperty().bind(gen2.progressProperty());
        application.getProgressbarbottom().progressProperty().bind(gen3.progressProperty());
        
        timeStampCalc = new TimeStamp();
        timeStampCalc.setBegin("" + (next - 1));

        pool.submit(gen1);
        pool.submit(gen2);
        pool.submit(gen3);
        
//        try
//        {
//            edges.addAll((ArrayList<Edge>) fut0.get());
//            edges.addAll((ArrayList<Edge>) fut1.get());
//            edges.addAll((ArrayList<Edge>) fut2.get());
//        } catch (InterruptedException | ExecutionException ex)
//        {
//            Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        
        application.requestDrawEdges();
        application.setTextNrEdges("" + kochFractal1.getNrOfEdges());
    }
    
    public synchronized void drawEdges() 
    {
        timeStampDraw.init();
        timeStampDraw.setBegin("" + (kochFractal1.getLevel()-1));
        
        Platform.runLater
        (
            new Runnable()
            {
                @Override
                public void run()
                {
                    application.clearKochPanel();
        
                    for(Edge edge : edges)
                    {
                        application.drawEdge(edge);
                    }
                }
            }
        );
        
        timeStampDraw.setEnd("" + (kochFractal1.getLevel()));
        
        Platform.runLater
        (
            new Runnable()
            {
                @Override
                public void run()
                {
                    application.setTextDraw(timeStampDraw.toString());
                }
            }
        );
        
    }
    
    public void addEdges(ArrayList<Edge> edges)
    {
        this.edges.addAll(edges);
    }
    
    public void endCalcTime()
    {
        timeStampCalc.setEnd("level " + (next));
        Platform.runLater
        (
            new Runnable()
            {
                @Override
                public void run()
                {
                    application.setTextCalc(timeStampCalc.toString());
                }
            }
        );
        
        
    }
}
