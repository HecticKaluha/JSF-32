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
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import timeutil.TimeStamp;

/**
 *
 * @author Stefan
 */
public class Writer
{    
    private static final String FILEPATH = "C:\\Users\\Stefan\\Documents\\GitHub\\JSF-32\\week 14\\edges\\edges.bin";
    private static final int EDGEBYTESIZE = 56;
    private static final int INTBYTESIZE = 4;
    
    
    public static void main(String[] args)
    {
        FileLock exclusiveLock = null;
        MappedByteBuffer out = null;
        RandomAccessFile raf = null;
        FileChannel ch = null;
        
        try
        {
            List<Edge> edges = new ArrayList<>();
            int lvl = Integer.parseInt(args[0]);
            
            KochFractal kochFractal = new KochFractal();
            
            kochFractal.setLevel(lvl);
            
            edges.addAll(kochFractal.generateBottomEdge());
            edges.addAll(kochFractal.generateLeftEdge());
            edges.addAll(kochFractal.generateRightEdge());
            
            raf = new RandomAccessFile(new File(FILEPATH), "rw");
            ch = raf.getChannel();

            out = ch.map(FileChannel.MapMode.READ_WRITE, 0, INTBYTESIZE + (kochFractal.getNrOfEdges() * EDGEBYTESIZE));

            TimeStamp timeStampWrite = new TimeStamp();
            timeStampWrite.setBegin("Schrijven");
            
            exclusiveLock = ch.lock(0, INTBYTESIZE , false);
            out.putInt(lvl);
            exclusiveLock.release();
            
            for(int i = 0; i < kochFractal.getNrOfEdges(); i++)
            {
                Edge edge = edges.get(i);
                
                exclusiveLock = ch.lock(i * EDGEBYTESIZE + 5, EDGEBYTESIZE , false);
                
                out.putDouble(edge.X1);
                out.putDouble(edge.Y1);
                out.putDouble(edge.X2);
                out.putDouble(edge.Y2); 

                out.putDouble(edge.color.getRed());
                out.putDouble(edge.color.getGreen());
                out.putDouble(edge.color.getBlue());
                
                exclusiveLock.release();
            }
                
            timeStampWrite.setEnd();
            System.out.println(timeStampWrite.toString());            
        }
        catch(NumberFormatException | IOException exc)
        {
            System.out.println("given level: " + args[0] + "is invalid");
            System.out.println(exc.getMessage());
        }
        finally
        {
            try
            {
               if(exclusiveLock != null)
               {
                   exclusiveLock.release();
               }
               
               ch.close();
               raf.close();
            } catch (Exception ex) {
                Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
