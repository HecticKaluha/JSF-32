/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileManagement;

import edgeManagement.Edge;
import edgeManagement.KochFractal;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Stefan
 */
public class Writer
{    
    private static final String FILEPATH = "C:\\Users\\Stefan\\Documents\\GitHub\\JSF-32\\week 12\\edges.txt";
    
    public static void main(String[] args)
    {
        BufferedWriter bufferedWriter;
        
        try
        {
            List<Edge> edges = new ArrayList<>();
            int lvl = Integer.parseInt(args[0]);
            
            KochFractal kochFractal = new KochFractal();
            
            kochFractal.setLevel(lvl);
            
            edges.addAll(kochFractal.generateBottomEdge());
            edges.addAll(kochFractal.generateLeftEdge());
            edges.addAll(kochFractal.generateRightEdge());
            bufferedWriter = new BufferedWriter(new FileWriter(new File(FILEPATH)));
            
            bufferedWriter.write(System.lineSeparator());
            bufferedWriter.write(args[0]);
            bufferedWriter.write(System.lineSeparator());
            
//            fileWriter.write( System.lineSeparator());
//            fileWriter.write(args[0]);
//            fileWriter.write( System.lineSeparator());
//            
            for(Edge edge : edges)
            {
      
                bufferedWriter.write(String.valueOf(edge.X1));
                bufferedWriter.write( System.lineSeparator());
                
                bufferedWriter.write(String.valueOf(edge.Y1));
                bufferedWriter.write( System.lineSeparator());
                
                bufferedWriter.write(String.valueOf(edge.X2));
                bufferedWriter.write( System.lineSeparator());
                
                bufferedWriter.write(String.valueOf(edge.Y2));
                bufferedWriter.write( System.lineSeparator());

                bufferedWriter.write(String.valueOf(edge.color.getRed()));
                bufferedWriter.write( System.lineSeparator());
                bufferedWriter.write(String.valueOf(edge.color.getGreen()));
                bufferedWriter.write( System.lineSeparator());
                bufferedWriter.write(String.valueOf(edge.color.getBlue()));
                bufferedWriter.write( System.lineSeparator());
            }
            
            bufferedWriter.close();
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
