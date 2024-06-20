import java.awt.*;
import java.lang.Math;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;

public class ToolManager extends Frame{
    
    
    // Mouse coordinates
    private int currentX, currentY, oldX, oldY;

    private int frameWidth,frameHeight;

    private int brushSizeX;
    private int brushSizeY;

    public enum Tool{
        FREEHAND,
        ERASE,
        LINE,
        RECT,
        TRIANGLE,
        CIRCLE,
        EYEDROP,
        FILL
    }

    ArrayList<int[]> triangleList = new ArrayList<int[]>();

    public Tool currentTool;

    private MyCanvas myCanvas;
    private LayerManager layerManager;

    public ToolManager(int frameWidth, int frameHeight, MyCanvas myCanvas, LayerManager layerManager){
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        this.myCanvas = myCanvas;
        this.layerManager = layerManager;

        brushSizeX = 3;
        brushSizeY = 3;


        currentTool = Tool.FREEHAND;

        myCanvas.addMouseListener(new MouseAdapter() { 
            public void mousePressed(MouseEvent e) { 
                
                // save coord x,y when mouse is pressed
                oldX = e.getX();
                oldY = e.getY();

                if(currentTool == Tool.TRIANGLE && triangleList.size() < 3){
                    triangleList.add(new int[] {e.getX(),e.getY()});
                }
                else if(myCanvas.g2 != null){
                    switch(currentTool){
                        case EYEDROP:
                            toolEyedrop(oldX,oldY);
                            break;
                        case FILL:
                            myCanvas.setBackground(Color.BLUE);
                            myCanvas.setForeground(Color.BLUE);
                            Layer myLayer = new Layer(800,1000,4);
                            //myLayer.setLayerArray();
                            toolRect(0, 0, 1000, 800);
                            break;


                    }
                }
            } 
        }); 
        
        myCanvas.addMouseMotionListener(new MouseMotionAdapter() { 
            public void mouseDragged(MouseEvent e) { 
                
                // coord x,y when drag mouse
                currentX = e.getX();
                currentY = e.getY();

                if(myCanvas.g2 != null){

                    switch(currentTool){
                        case FREEHAND:
                            toolFreehand(oldX, oldY, currentX, currentY);
                            break;
                        case ERASE:
                            toolErase(oldX, oldY, currentX, currentY);
                            break;
                    }
                }

            } 
        }); 

        myCanvas.addMouseListener(new MouseAdapter() { 
            public void mouseReleased(MouseEvent e) { 

                // save coord x,y when mouse is released
                currentX = e.getX();
                currentY = e.getY();
                
                if(myCanvas.g2 != null){

                    switch(currentTool){
                        case LINE:
                            toolLine(oldX, oldY, currentX, currentY);
                            break;
                        case RECT:
                            //toolRect(oldX, oldY, currentX, currentY);
                            toolRectHollow(oldX, oldY, currentX, currentY);
                            break;
                        case CIRCLE:
                            toolCircle(oldX, oldY, currentX, currentY);
                            break;
                        case TRIANGLE:
                            toolTriangle(oldX, oldY, currentX, currentY);
                            break;
                    }
                }
            } 
        }); 
    }

    public void setTool(Tool type){
        currentTool = type;
    }

    public void changeBrushX(int x){
        if(brushSizeX + x >0){
            brushSizeX += x;
        }else{
            return;
        }
    }

    public void changeBrushY(int y){
        if(brushSizeY + y >0){
            brushSizeY += y;
        }else{
            return;
        }
    }


    public void toolFreehand(int x0, int y0, int x1, int y1){

        // calculate pixels to edit
        ArrayList<int[]> coordinates = calcLine(x0, y0, x1, y1);

        // set pixels in canvas layer
        layerManager.setPixelArray(coordinates, brushSizeX, brushSizeY);

        // refresh draw area to repaint
        myCanvas.repaint();

        // store current coords x,y as olds x,y
        oldX = currentX;
        oldY = currentY;
    }

    public void toolErase(int x0, int y0, int x1, int y1){

        // calculate pixels to edit
        ArrayList<int[]> coordinates = calcLine(x0, y0, x1, y1);

        // set pixels in canvas layer
        layerManager.setPixelArray(coordinates, brushSizeX, brushSizeY);

        // refresh draw area to repaint
        myCanvas.repaint();

        // store current coords x,y as olds x,y
        oldX = currentX;
        oldY = currentY;
    }

    public void toolLine(int x0, int y0, int x1, int y1){

        // calculate pixels to edit
        ArrayList<int[]> coordinates = calcLine(x0, y0, x1, y1);

        // set pixels in canvas layer
        layerManager.setPixelArray(coordinates, brushSizeX, brushSizeY);

        // refresh draw area to repaint
        myCanvas.repaint();

        // store current coords x,y as olds x,y
        oldX = currentX;
        oldY = currentY;
    }

    public void toolRect(int x0, int y0, int x1, int y1){
        // calculate pixels to edit
        ArrayList<int[]> coordinates = calcRect(x0, y0, x1, y1);

        // set pixels in canvas layer
        layerManager.setPixelArray(coordinates, 1, 1);

        // refresh draw area to repaint
        myCanvas.repaint();

        // store current coords x,y as olds x,y
        oldX = currentX;
        oldY = currentY;
    }

    public void toolRectHollow(int x0, int y0, int x1, int y1){
        // calculate pixels to edit
        ArrayList<int[]> coordinates1 = calcLine(x0,y0,x1,y0);
        ArrayList<int[]> coordinates2 = calcLine(x0,y0,x0,y1);
        ArrayList<int[]> coordinates3 = calcLine(x0,y1,x1,y1);
        ArrayList<int[]> coordinates4 = calcLine(x1,y1,x1,y0);

        // set pixels in canvas layer
        layerManager.setPixelArray(coordinates1, 1, 1);
        layerManager.setPixelArray(coordinates2, 1, 1);
        layerManager.setPixelArray(coordinates3, 1, 1);
        layerManager.setPixelArray(coordinates4, 1, 1);

        // refresh draw area to repaint
        myCanvas.repaint();

        // store current coords x,y as olds x,y
        oldX = currentX;
        oldY = currentY;
    }

    public void toolCircle(int x0, int y0, int x1, int y1){
        // calculate pixels to edit
        ArrayList<int[]> coordinates = calcCircle(x0, y0, x1, y1);

        // set pixels in canvas layer
        layerManager.setPixelArray(coordinates, 1, 1);

        // refresh draw area to repaint
        myCanvas.repaint();

        // store current coords x,y as olds x,y
        oldX = currentX;
        oldY = currentY;
    }

    public void toolTriangle(int x0, int y0, int x1, int y1){

        if(triangleList.size() != 3){
            return;
        }

        // calculate pixels to edit
        ArrayList<int[]> coordinates = calcTriangle();

        // set pixels in canvas layer
        layerManager.setPixelArray(coordinates, 1, 1);

        // refresh draw area to repaint
        myCanvas.repaint();

        // store current coords x,y as olds x,y
        oldX = currentX;
        oldY = currentY;
    }

    public void toolEyedrop(int x, int y){
        layerManager.eyedrop(x,y);
    }
    public void toolFill(){
        ArrayList<int[]> coordinates = calcRect(0, 0, 1000, 800);

        // set pixels
        layerManager.setPixelArray(coordinates, 1, 1);

        // refresh draw area to repaint
        myCanvas.repaint();

    }
    public ArrayList calcFill(){
        ArrayList<int[]> coordinates = new ArrayList<int[]>();
//
        return coordinates;
    }
    public ArrayList calcLine(int x0, int y0, int x1, int y1) 
    { 
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
 
        int sx = x0 < x1 ? 1 : -1; 
        int sy = y0 < y1 ? 1 : -1; 
 
        int err = dx-dy;
        int e2;

        ArrayList<int[]> coordinates = new ArrayList<int[]>();
 
        while (true) 
        {
            coordinates.add(new int[] {x0,y0});
 
            if (x0 == x1 && y0 == y1) 
                break;
 
            e2 = 2 * err;
            if (e2 > -dy) 
            {
                err = err - dy;
                x0 = x0 + sx;
            }
 
            if (e2 < dx) 
            {
                err = err + dx;
                y0 = y0 + sy;
            }
        }
        
        return coordinates;
    }

    public ArrayList calcRect(int x0, int y0, int x1, int y1){

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        int sx = x0 < x1 ? 1 : -1; 
        int sy = y0 < y1 ? 1 : -1; 

        ArrayList<int[]> coordinates = new ArrayList<int[]>();

        for(int ix = x0; ix != x1; ix += sx){
            for(int iy = y0; iy != y1; iy += sy){
                coordinates.add(new int[] {ix, iy});
            }
        }

        return coordinates;
    }

    public ArrayList calcCircle(int x0, int y0, int x1, int y1){

        int h = (x0 + x1)/2;
        int k = (y0 + y1)/2;

        int a = Math.abs(x1 - x0)/2;
        int b = Math.abs(y1 - y0)/2;

        // if(a<b){
        //     int t = a;
        //     a = b;
        //     b = t;
        // }

        int sx = x0 < x1 ? 1 : -1; 
        int sy = y0 < y1 ? 1 : -1; 

        ArrayList<int[]> coordinates = new ArrayList<int[]>();

        for(int ix = x0; ix != x1; ix += sx){
            for(int iy = y0; iy != y1; iy += sy){
                if(((Math.pow(ix-h, 2)/Math.pow(a, 2)) + (Math.pow(iy-k, 2)/Math.pow(b, 2))) <= 1){
                    coordinates.add(new int[] {ix, iy});
                }

            }
        }

        return coordinates;
    }

    /**
     * Triangle drawing function below
     * Calculated using forumulas provided by @author Arnav Kr. Mandal via {@link https://www.geeksforgeeks.org/check-whether-a-given-point-lies-inside-a-triangle-or-not/}
     */
    private double area(int x1, int y1, int x2, int y2, int x3, int y3){

        double A = Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0);

        return A;
    }

    public ArrayList calcTriangle(){
    
        int x1 = triangleList.get(0)[0];
        int y1 = triangleList.get(0)[1];
        int x2 = triangleList.get(1)[0];
        int y2 = triangleList.get(1)[1];
        int x3 = triangleList.get(2)[0];
        int y3 = triangleList.get(2)[1];
        
        double A = area(x1, y1, x2, y2, x3, y3);

        ArrayList<int[]> coordinates = new ArrayList<int[]>();
        
        for(int x = 0; x < myCanvas.canvasWidth; x++){
            for(int y = 0; y < myCanvas.canvasHeight; y++){

                double A1 = area(x, y, x2, y2, x3, y3);
                double A2 = area(x1, y1, x, y, x3, y3);
                double A3 = area(x1, y1, x2, y2, x, y);

                if(A == A1 + A2 + A3){
                    coordinates.add(new int[] {x, y});
                }
            }
        }

        triangleList.clear();
        
        return coordinates;
    }
    
}
