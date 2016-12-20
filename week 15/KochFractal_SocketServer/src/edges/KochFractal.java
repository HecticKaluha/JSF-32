/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edges;

import java.io.IOException;
import java.io.ObjectOutputStream;
import utils.SerializableColor;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter Boots
 */
public class KochFractal
{

    private int level;      // The current level of the fractal
    private int nrOfEdges;  // The number of edges in the current level of the fractal
    private float hue;          // Hue value of color for next edge
    private List<Edge> edges = new ArrayList<>();
    
    private boolean individual;
    
    private ObjectOutputStream objectOutputStream;
    
    public KochFractal(int level, boolean individual, ObjectOutputStream objectOutputStream)
    {
        if(level < 0)
        {
            throw new IllegalArgumentException();
        }
        
        if(objectOutputStream == null)
        {
            throw new IllegalArgumentException();
        }
        
        this.level = level;
        nrOfEdges = (int) (3 * Math.pow(4, level - 1));
        this.individual = individual;
        this.objectOutputStream = objectOutputStream;
    }
    
    private void drawKochEdge(double ax, double ay, double bx, double by, int n)
    {
            if (n == 1) 
            {
                hue = hue + 1.0f / nrOfEdges;
                
                javafx.scene.paint.Color color = javafx.scene.paint.Color.hsb(hue*360.0, 1.0, 1.0);
                
                Edge edge = new Edge(ax, ay, bx, by, new SerializableColor((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 1.0f));
                edges.add(edge);
                
                if(individual)
                {
                    try
                    {
                        System.out.println("Send single edge");
                        objectOutputStream.writeObject(edge);
                    }
                    catch (IOException ex)
                    {
                        Logger.getLogger(KochFractal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } 
            else
            {
                double angle = Math.PI / 3.0 + Math.atan2(by - ay, bx - ax);
                double distabdiv3 = Math.sqrt((bx - ax) * (bx - ax) + (by - ay) * (by - ay)) / 3;
                double cx = Math.cos(angle) * distabdiv3 + (bx - ax) / 3 + ax;
                double cy = Math.sin(angle) * distabdiv3 + (by - ay) / 3 + ay;
                final double midabx = (bx - ax) / 3 + ax;
                final double midaby = (by - ay) / 3 + ay;
                drawKochEdge(ax, ay, midabx, midaby, n - 1);
                drawKochEdge(midabx, midaby, cx, cy, n - 1);
                drawKochEdge(cx, cy, (midabx + bx) / 2, (midaby + by) / 2, n - 1);
                drawKochEdge((midabx + bx) / 2, (midaby + by) / 2, bx, by, n - 1);
            }
    }

    public List<Edge> generateLeftEdge() 
    {
        edges.clear();
        
        hue = 0f;
        drawKochEdge(0.5, 0.0, (1 - Math.sqrt(3.0) / 2.0) / 2, 0.75, level);
        
        return edges;
    }

    public List<Edge> generateBottomEdge() 
    {
        edges.clear();
        
        hue = 1f / 3f;
        drawKochEdge((1 - Math.sqrt(3.0) / 2.0) / 2, 0.75, (1 + Math.sqrt(3.0) / 2.0) / 2, 0.75, level);
        
        return edges;
    }

    public  List<Edge> generateRightEdge() 
    {
        edges.clear();
        
        hue = 2f / 3f;
        drawKochEdge((1 + Math.sqrt(3.0) / 2.0) / 2, 0.75, 0.5, 0.0, level);
        
        return edges;
    }
    
    public void setLevel(int lvl) 
    {
        level = lvl;
        nrOfEdges = (int) (3 * Math.pow(4, level - 1));
    }

    public int getLevel() 
    {
        return level;
    }

    public int getNrOfEdges() 
    {
        return nrOfEdges;
    }    
}
