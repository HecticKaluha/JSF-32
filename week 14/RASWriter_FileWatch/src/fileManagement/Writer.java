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
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.Cleaner;
import sun.security.jca.GetInstance;
import timeutil.TimeStamp;

/**
 *
 * @author Stefan
 */
public class Writer
{    
    private static String FILEPATH = "C:\\Users\\Milton van de Sanden\\Documents\\GitHub\\JSF-32\\week 14\\RASReader_FileWatch\\edges.bin";
    private static final int EDGEBYTESIZE = 56;
    private static final int INTBYTESIZE = 4;
    
    
    public static void main(String[] args)
    {
        FileLock exclusiveLock = null;
        MappedByteBuffer out = null;
        RandomAccessFile raf = null;
        FileChannel ch = null;
        
        File file = new File(FILEPATH);
        
        try
        {
            List<Edge> edges = new ArrayList<>();
            int lvl = Integer.parseInt(args[0]);
            
            KochFractal kochFractal = new KochFractal();
            
            kochFractal.setLevel(lvl);
            
            edges.addAll(kochFractal.generateBottomEdge());
            edges.addAll(kochFractal.generateLeftEdge());
            edges.addAll(kochFractal.generateRightEdge());
            
            raf = new RandomAccessFile(file, "rw");
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
            GregorianCalendar calendar = new GregorianCalendar();
            //file.renameTo(new File("C:\\Users\\Milton van de Sanden\\Documents\\GitHub\\JSF-32\\week 14\\RASReader_FileWatch\\edges" + "BLUB" + ".bin"));

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
                Cleaner cleaner = ((sun.nio.ch.DirectBuffer)out).cleaner();
                cleaner.clean();
                File file2 = new File("C:\\Users\\Milton van de Sanden\\Documents\\GitHub\\JSF-32\\week 14\\RASReader_FileWatch\\edges" + "BLUB" + ".bin");
                System.out.println("Result of move:"+ file.renameTo(file2));
               //Files.move(file.toPath(), new File("C:\\Users\\Milton van de Sanden\\Documents\\GitHub\\JSF-32\\week 14\\RASReader_FileWatch\\edges" + "BLUB" + ".bin").toPath());
            } catch (Exception ex) {
                Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
