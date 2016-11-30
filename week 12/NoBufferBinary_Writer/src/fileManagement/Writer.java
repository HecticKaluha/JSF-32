/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileManagement;

import edgeManagement.Edge;
import edgeManagement.KochFractal;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import timeutil.TimeStamp;

/**
 *
 * @author Stefan
 */
public class Writer
{    
    private static final String FILEPATH = "C:\\Users\\Stefan\\Documents\\GitHub\\JSF-32\\week 12\\edges.bin";
    
    public static void main(String[] args)
    {
        ObjectOutputStream objectOutputStream;
         TimeStamp timeStampWrite;
        
        try
        {
            List<Edge> edges = new ArrayList<>();
            int lvl = Integer.parseInt(args[0]);
            
            KochFractal kochFractal = new KochFractal();
            
            kochFractal.setLevel(lvl);
            
            edges.addAll(kochFractal.generateBottomEdge());
            edges.addAll(kochFractal.generateLeftEdge());
            edges.addAll(kochFractal.generateRightEdge());
            
            timeStampWrite = new TimeStamp();
            timeStampWrite.setBegin("Schrijven");
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(FILEPATH)));

            objectOutputStream.writeInt(kochFractal.getLevel());

            objectOutputStream.writeObject(edges);
            timeStampWrite.setEnd();
            System.out.println(timeStampWrite.toString());
            
            objectOutputStream.close();
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
