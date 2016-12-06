/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileManagement;

import edgeManagement.Edge;
import edgeManagement.KochFractal;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import timeutil.TimeStamp;

/**
 *
 * @author Stefan
 */
public class Writer
{    
    private static final String FILEPATH = "C:\\Users\\Stefan\\Documents\\GitHub\\JSF-32\\week 13\\edges.bin";
    
    public static void main(String[] args)
    {
        RandomAccessFile raf = null;
        try {
            //        ObjectOutputStream objectOutputStream
            raf = new RandomAccessFile(FILEPATH, "rw");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
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
//            objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(FILEPATH)));

//            objectOutputStream.writeInt(kochFractal.getLevel());
            raf.writeInt(kochFractal.getLevel());

            for(Edge edge : edges)
            {
                raf.writeDouble(edge.X1);
                raf.writeDouble(edge.Y1);
                raf.writeDouble(edge.X2);
                raf.writeDouble(edge.Y2);

                raf.writeDouble(edge.color.getRed());
                raf.writeDouble(edge.color.getGreen());
                raf.writeDouble(edge.color.getBlue());
            }
            
            timeStampWrite.setEnd();
            System.out.println(timeStampWrite.toString());
        }
        catch(Exception exc)
        {
            System.out.println("given level: " + args[0] + "is invalid");
            System.out.println(exc.getMessage());
        }
        finally
        {
            try {
                raf.close();
            } catch (IOException ex) {
                Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
