package interruptable_sockets_4;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.*;
import javax.swing.*;

/**
 * This program shows how to interrupt a socket channel.
 * @author Cay Horstmann, adapted by erik v.d. schriek
 * @version 1.01 2007-06-25
 */
public class InterruptibleSocketTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new InterruptibleSocketFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

class InterruptibleSocketFrame extends JFrame
{
   private Scanner in;
   private JButton interruptibleButton;
   private JButton blockingButton;
   private JButton cancelButton;
   private JTextArea messages;
   private TestServer server;
   private Thread connectThread;

   public static final int frameWIDTH = 600;
   public static final int frameHEIGHT = 600;

    public InterruptibleSocketFrame()
   {
      setSize(frameWIDTH, frameHEIGHT);
      setTitle("InterruptibleSocketTest");

      JPanel northPanel = new JPanel();
      add(northPanel, BorderLayout.NORTH);

      messages = new JTextArea();
      add(new JScrollPane(messages));

      interruptibleButton = new JButton("Interruptible");
      blockingButton = new JButton("Blocking");

      northPanel.add(interruptibleButton);
      northPanel.add(blockingButton);

      interruptibleButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               interruptibleButton.setEnabled(false);
               blockingButton.setEnabled(false);
               cancelButton.setEnabled(true);
               connectThread = new Thread(new InterruptableSocket());
               connectThread.start();
            }
         });

      blockingButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               interruptibleButton.setEnabled(false);
               blockingButton.setEnabled(false);
               cancelButton.setEnabled(true);
               connectThread = new Thread(new BlockingSocket());
               connectThread.start();
            }
         });

      cancelButton = new JButton("Cancel");
      cancelButton.setEnabled(false);
      northPanel.add(cancelButton);
      cancelButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               connectThread.interrupt();
               cancelButton.setEnabled(false);
            }
         });
      // start socketserver
      server = new TestServer();
      new Thread(server).start();
   }

/***************************************************************************
 * 
 * SERVER CLASSES
 * 
 ***************************************************************************/   

   /**
    * INNER CLASS:
    * A multithreaded server that listens to port 8189 and sends numbers to the client, simulating a
    * hanging server after 10 numbers.
    */
   class TestServer implements Runnable
   {
      public void run()
      {
         try
         {
            ServerSocket s = new ServerSocket(8189);

            while (true)
            {
               Socket incoming = s.accept();
               Runnable r = new TestServerHandler(incoming);
               Thread t = new Thread(r);
               t.start();
            }
         }
         catch (IOException e)
         {
            messages.append("\nTestServer.run: " + e);
         }
      }
   }

   /**
    * INNER CLASS:
    * This class handles the client input for one server socket connection.
    */
   class TestServerHandler implements Runnable
   {
      private Socket incommingSocket;
      private int counter;

      /**
       * Constructs a handler.
       * @param i the incoming socket
       */
      public TestServerHandler(Socket i)
      {
         incommingSocket = i;
      }

      public void run()
      {
         try
         {
            OutputStream outStream = incommingSocket.getOutputStream();
            PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
            while (counter < 10)
            {
               counter++;
               out.println(counter);
               Thread.sleep(300);
            }
            // sleep for some time
            while (counter < 40)
            {
               counter++;
               Thread.sleep(300);
            }
            //
            // continue producing numbers
            while (counter < 50)
            {
               counter++;
               out.println(counter);
               Thread.sleep(100);
            }
            incommingSocket.close();
            messages.append("\nClosing serverside socket\n");
         }
         catch (Exception e)
         {
            messages.append("\nTestServerHandler.run: " + e);
         }
      }

   }

/***************************************************************************
 * 
 * CLIENT CLASSES
 * 
 ***************************************************************************/   
   
   /**
    * INNER CLASS:
    * This class is a socket which uses blocking method calls. 
    * this type of socket is used mostly in all JAVA textbooks & examples.
    */
   
   class BlockingSocket implements Runnable {

    public void run() {
        try {
            connectBlocking();
        } catch (IOException e) {
            messages.append("\nInterruptibleSocketTest.connectBlocking: " + e);
        }
    }

   /**
    * Connects to the test server, using blocking I/O
    */
   public void connectBlocking() throws IOException
   {
      messages.append("Blocking:\n");
      Socket sock = new Socket("localhost", 8189);
      try
      {
         in = new Scanner(sock.getInputStream());
         while (!Thread.currentThread().isInterrupted())
         {
            messages.append("Reading ");
            if (in.hasNextLine())
            {
               String line = in.nextLine();
               messages.append(line);
               messages.append("\n");
            }
         }
      }
      finally
      {
          // close() method is blocking. code waits here until server
          // has closed the socket.
         sock.close();
         EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               messages.append("\nSocket closed\n");
               interruptibleButton.setEnabled(true);
               blockingButton.setEnabled(true);
            }
         });      
      }
   }

}

   /**
    * INNER CLASS:
    * This class is a socket which uses non-blocking method calls. 
    * this type of socket is hardly used in JAVA textbooks & examples.
    */
   
   class InterruptableSocket implements Runnable {

    public void run() {
        try {
            connectInterruptibly();
        } catch (IOException e) {
            messages.append("\nInterruptibleSocketTest.connectInterruptibly: " + e);
        }
    }

   /**
    * Connects to the test server, using interruptible I/O
    */
   public void connectInterruptibly() throws IOException
   {
      messages.append("Interruptible:\n");
      SocketChannel channel = SocketChannel.open(new InetSocketAddress("localhost", 8189));
      try
      {
         in = new Scanner(channel);
         while (!Thread.currentThread().isInterrupted())
         {
            messages.append("Reading ");
            if (in.hasNextLine())
            {
               String line = in.nextLine();
               messages.append(line);
               messages.append("\n");
            }
         }
      }
      finally
      {
         // close() method is non-blocking. channel is closed immediatly!
         channel.close();
         EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               messages.append("\nChannel closed\n");
               interruptibleButton.setEnabled(true);
               blockingButton.setEnabled(true);
            }
         });
      }
   }

}

   
   
}
