/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThreadManagement;

import calculate.Edge;
import calculate.KochFractal;
import calculate.KochManager;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author Stefan
 */
public class EdgeGenerator implements Callable<ArrayList<Edge>>, Observer
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
        
        this.cb = cb;
    }
        
//    @Override
//    public void run()
//    {
//        switch(side)
//        {
//            case 0:
//                kochFractal.generateLeftEdge();
//                break;
//            case 1:
//                kochFractal.generateRightEdge();
//                break;
//            default:
//                kochFractal.generateBottomEdge();
//                break;
//        }
//                    
//    }

    @Override
    public void update(Observable o, Object arg) {
       Edge edge = (Edge) arg;
       edges.add(edge);
//       synchronized(stefan)
//       {      
//           kochManager.addEdges(e);
//           if(kochManager.getEdges().size() == kochFractal.getNrOfEdges())
//           {
//                kochManager.getApplication().requestDrawEdges();
//                kochManager.setDone();
//                //kochManager.drawEdges();
//           }
//       }
    }

    @Override
    public ArrayList<Edge> call() throws Exception
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
        
        cb.await();
        return edges;
    }

    
}
