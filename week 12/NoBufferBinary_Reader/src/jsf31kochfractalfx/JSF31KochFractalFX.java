/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf31kochfractalfx;

import edgeManagement.Edge;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Nico Kuijpers
 */
public class JSF31KochFractalFX extends Application {
    
    // Zoom and drag
    private static double zoomTranslateX = 0.0;
    private static double zoomTranslateY = 0.0;
    private static double zoom = 1.0;
    private double startPressedX = 0.0;
    private double startPressedY = 0.0;
    private double lastDragX = 0.0;
    private double lastDragY = 0.0;
    
    // Current level of Koch fractal
    private static int currentLevel;
    private static List<Edge> edges = new ArrayList<>();
    
    // Labels for level, nr edges, calculation time, and drawing time
    private Label labelLevel;
    private Label labelNrEdges;
    private Label labelNrEdgesText;
    private Label labelCalc;
    private Label labelCalcText;
    private Label labelDraw;
    private Label labelDrawText;
    
    // Koch panel and its size
    private static Canvas kochPanel;
    private final int kpWidth = 500;
    private final int kpHeight = 500;
    
    private final static String PATH = "C:\\Users\\Stefan\\Documents\\GitHub\\JSF-32\\week 12\\edges.bin";
    
    @Override
    public void start(Stage primaryStage) {
        
        
       
        // Define grid pane
        GridPane grid;
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
                
        // Drawing panel for Koch fractal
        kochPanel = new Canvas(kpWidth,kpHeight);
        grid.add(kochPanel, 0, 3, 25, 1);
        
        // Labels to present number of edges for Koch fractal
        labelNrEdges = new Label("Nr edges:");
        labelNrEdgesText = new Label();
        grid.add(labelNrEdges, 0, 0, 4, 1);
        grid.add(labelNrEdgesText, 3, 0, 22, 1);
        
        // Labels to present time of calculation for Koch fractal
        labelCalc = new Label("Calculating:");
        labelCalcText = new Label();
        grid.add(labelCalc, 0, 1, 4, 1);
        grid.add(labelCalcText, 3, 1, 22, 1);
        
        // Labels to present time of drawing for Koch fractal
        labelDraw = new Label("Drawing:");
        labelDrawText = new Label();
        grid.add(labelDraw, 0, 2, 4, 1);
        grid.add(labelDrawText, 3, 2, 22, 1);
        
        // Label to present current level of Koch fractal
        labelLevel = new Label("Level: " + currentLevel);
        grid.add(labelLevel, 0, 6);
        
        // Button to fit Koch fractal in Koch panel
        Button buttonFitFractal = new Button();
        buttonFitFractal.setText("Fit Fractal");
        buttonFitFractal.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fitFractalButtonActionPerformed(event);
            }
        });
        grid.add(buttonFitFractal, 14, 6);
        
// Add mouse clicked event to Koch panel
        kochPanel.addEventHandler(MouseEvent.MOUSE_CLICKED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    kochPanelMouseClicked(event);
                }
            });
        
        // Add mouse pressed event to Koch panel
        kochPanel.addEventHandler(MouseEvent.MOUSE_PRESSED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    kochPanelMousePressed(event);
                }
            });
        
        // Add mouse dragged event to Koch panel
        kochPanel.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                kochPanelMouseDragged(event);
            }
        });
        
        // Create Koch manager and set initial level
        resetZoom();
        
        // Create the scene and add the grid pane
        Group root = new Group();
        Scene scene = new Scene(root, kpWidth+50, kpHeight+300);
        root.getChildren().add(grid);
        
        // Define title and assign the scene for main window
        primaryStage.setTitle("Koch Fractal");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        readEdges();
    }
    
    public void clearKochPanel() {
        GraphicsContext gc = kochPanel.getGraphicsContext2D();
        gc.clearRect(0.0,0.0,kpWidth,kpHeight);
        gc.setFill(Color.BLACK);
        gc.fillRect(0.0,0.0,kpWidth,kpHeight);
    }
    
    public static void drawEdge(Edge e) {
        // Graphics
        GraphicsContext gc = kochPanel.getGraphicsContext2D();
        
        // Adjust edge for zoom and drag
        Edge e1 = edgeAfterZoomAndDrag(e);
        
        // Set line color
        gc.setStroke(new javafx.scene.paint.Color(e1.color.r, e1.color.g, e1.color.b, 1.0));
        
        // Set line width depending on level
        if (currentLevel <= 3) {
            gc.setLineWidth(2.0);
        }
        else if (currentLevel <=5 ) {
            gc.setLineWidth(1.5);
        }
        else {
            gc.setLineWidth(1.0);
        }
        
        // Draw line
        gc.strokeLine(e1.X1,e1.Y1,e1.X2,e1.Y2);
    }
    
    public void setTextNrEdges(String text) {
        labelNrEdgesText.setText(text);
    }
    
    public void setTextCalc(String text) {
        labelCalcText.setText(text);
    }
    
    public void setTextDraw(String text) {
        
        labelDrawText.setText(text);
    }
    
//    public void requestDrawEdges()
//    {
//        
//    }
    
    public static void readEdges()
    {
        try
        {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File(PATH)));
                        
            int lvl = objectInputStream.readInt();
            
            for(Edge edge : (List<Edge>) objectInputStream.readObject())
            {
                drawEdge(edge);
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JSF31KochFractalFX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(JSF31KochFractalFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void fitFractalButtonActionPerformed(ActionEvent event) {
        resetZoom();
    }
    
    private void kochPanelMouseClicked(MouseEvent event) {
        if (Math.abs(event.getX() - startPressedX) < 1.0 && 
            Math.abs(event.getY() - startPressedY) < 1.0) {
            double originalPointClickedX = (event.getX() - zoomTranslateX) / zoom;
            double originalPointClickedY = (event.getY() - zoomTranslateY) / zoom;
            if (event.getButton() == MouseButton.PRIMARY) {
                zoom *= 2.0;
            } else if (event.getButton() == MouseButton.SECONDARY) {
                zoom /= 2.0;
            }
            zoomTranslateX = (int) (event.getX() - originalPointClickedX * zoom);
            zoomTranslateY = (int) (event.getY() - originalPointClickedY * zoom);
        }
    }                                      

    private void kochPanelMouseDragged(MouseEvent event) {
        zoomTranslateX = zoomTranslateX + event.getX() - lastDragX;
        zoomTranslateY = zoomTranslateY + event.getY() - lastDragY;
        lastDragX = event.getX();
        lastDragY = event.getY();        
    }

    private void kochPanelMousePressed(MouseEvent event) {
        startPressedX = event.getX();
        startPressedY = event.getY();
        lastDragX = event.getX();
        lastDragY = event.getY();
    }                                                                        

    private void resetZoom() {
        int kpSize = Math.min(kpWidth, kpHeight);
        zoom = kpSize;
        zoomTranslateX = (kpWidth - kpSize) / 2.0;
        zoomTranslateY = (kpHeight - kpSize) / 2.0;
    }

    private static Edge edgeAfterZoomAndDrag(Edge e) {
        return new Edge(
                e.X1 * zoom + zoomTranslateX,
                e.Y1 * zoom + zoomTranslateY,
                e.X2 * zoom + zoomTranslateX,
                e.Y2 * zoom + zoomTranslateY,
                e.color);
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}
