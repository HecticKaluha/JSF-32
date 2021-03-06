/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileManagement;

import edgeManagement.Edge;
import edgeManagement.KochFractal;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import timeutil.TimeStamp;

/**
 *
 * @author Stefan
 */
public class Writer
{    
    private static final String FILEPATH = "C:\\Users\\Stefan\\Documents\\GitHub\\JSF-32\\week 12\\edges.txt";
    
    public static void main(String[] args)
    {
        FileWriter fileWriter;
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
            
            fileWriter = new FileWriter(new File(FILEPATH));
            
            timeStampWrite = new TimeStamp();
            timeStampWrite.setBegin("Schrijven");
            fileWriter.write( System.lineSeparator());
            fileWriter.write(args[0]);
            fileWriter.write( System.lineSeparator());
            
            for(Edge edge : edges)
            {
                fileWriter.write(String.valueOf(edge.X1));
                fileWriter.write( System.lineSeparator());
                
                fileWriter.write(String.valueOf(edge.Y1));
                fileWriter.write( System.lineSeparator());

                
                fileWriter.write(String.valueOf(edge.X2));
                fileWriter.write( System.lineSeparator());
                
                fileWriter.write(String.valueOf(edge.Y2));
                fileWriter.write( System.lineSeparator());

                fileWriter.write(String.valueOf(edge.color.getRed()));
                fileWriter.write( System.lineSeparator());
                fileWriter.write(String.valueOf(edge.color.getGreen()));
                fileWriter.write( System.lineSeparator());
                fileWriter.write(String.valueOf(edge.color.getBlue()));
                fileWriter.write( System.lineSeparator());
            }
            
            fileWriter.close();
            timeStampWrite.setEnd();
            System.out.println(timeStampWrite.toString());
        }
        catch(Exception exc)
        {
            System.out.println("given level: " + args[0] + "is invalid");
            //System.out.println(exc.getMessage());
        }
        finally
        {
            
        }
    }
}
