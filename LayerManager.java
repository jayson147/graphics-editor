import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class LayerManager{
    //Layers and related variables
    public LinkedList<Layer> myLayers = new LinkedList<Layer>();
    private LinkedList<Integer> layersVisibility = new LinkedList<Integer>();
    private int numLayers = 0;
    private int activeLayer = -1;
    private int canvasWidth,canvasHeight;

    //Color variables
    private int transparent = 255;
    private int[] activeColor = {0,0,0,255};    // 0,0,0 is rgb for black, 255 means opaque

    public LayerManager(int canvasWidth, int canvasHeight){
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        addLayer();
        clearLayer();
    }

    /**
     * Takes an array of co-ordinates and sets each corresponding pixel in the active layer to the respective colour
     * @param coordinates an ArrayList<int[]> of {x,y} co-ordinates
     */
    public void setPixelArray(ArrayList<int[]> coordinates, int x, int y){
        Layer tempLayer =  myLayers.get(activeLayer);
        x--;
        y--;
        for (int i[] : coordinates) {
            for(int n=-x; n<=y; n++){
                for(int m=-x; m<=x; m++){
                    tempLayer.setPixel(i[0]+n, i[1]+m, activeColor);
                }
            }
        }
    }

    public BufferedImage getImage(){
        BufferedImage bufferedImage = new BufferedImage(canvasWidth, canvasHeight,
        BufferedImage.TYPE_INT_RGB);
        int tempLayer[][][] = combineLayers();
        for(int i = 0; i < canvasHeight; i++){
            for(int j = 0; j < canvasWidth; j++){
                //Color myColor = new Color(layers.get(activeLayer)[i][j][0], layers.get(activeLayer)[i][j][1], layers.get(activeLayer)[i][j][2]);
                Color myColor = new Color(tempLayer[i][j][0], tempLayer[i][j][1], tempLayer[i][j][2]);
                int rgb = myColor.getRGB();
                bufferedImage.setRGB(j, i, rgb);
            }
        }
        return bufferedImage;
    }

    public BufferedImage getThumbnail(int thumbnailLayer){
        BufferedImage bufferedImage = new BufferedImage(canvasWidth, canvasHeight,
        BufferedImage.TYPE_INT_RGB);
        int tempLayer[][][] = myLayers.get(thumbnailLayer).getLayerArray();
        for(int i = 0; i < canvasHeight; i++){
            for(int j = 0; j < canvasWidth; j++){
                Color myColor = new Color(tempLayer[i][j][0], tempLayer[i][j][1], tempLayer[i][j][2]);
                int rgb = myColor.getRGB();
                bufferedImage.setRGB(j, i, rgb);
            }
        }
        return bufferedImage;
    }


    public static byte[] toByteArray(BufferedImage bi, String format)
            throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;

    }

    public int[][][] combineLayers(){
        int[][][] finalLayer = new int[canvasHeight][canvasWidth][4];
        for(int i = 0; i < canvasHeight; i++){
            for(int j = 0; j < canvasWidth; j++){
                finalLayer[i][j][0] = transparent;
                finalLayer[i][j][1] = transparent;
                finalLayer[i][j][2] = transparent;
                finalLayer[i][j][3] = 0;   //0 for transparent
            }
        }
        
        for(int i=0; i < numLayers; i++){
            if(layersVisibility.get(i)>0){
                int tempLayer[][][] = myLayers.get(i).getLayerArray();
                for(int j=0; j<canvasHeight; j++){
                    for(int k=0; k<canvasWidth; k++){
                        if(tempLayer[j][k][3] > 0){
                            finalLayer[j][k][0] = tempLayer[j][k][0];
                            finalLayer[j][k][1] = tempLayer[j][k][1];
                            finalLayer[j][k][2] = tempLayer[j][k][2];
                            finalLayer[j][k][3] = tempLayer[j][k][3];
                        }
                    }
                }
            }

        }
        return finalLayer;
    }

    public void setColor(int r, int g, int b, int a){
        activeColor[0] = r;
        activeColor[1] = g;
        activeColor[2] = b;
        activeColor[3] = a;
    }

    public void eyedrop(int x, int y){
        Layer tempLayer =  myLayers.get(activeLayer);
        int colour[] = tempLayer.getColour(x, y);
        activeColor = colour;
    }
    
    public void setDraw(){
        activeColor[3] = 255;
    }

    public void setErase(){
        activeColor[3] = 0;
    }

    public void clearLayer(){
        myLayers.get(activeLayer).clear();
    }

    public Layer addLayer(){
        numLayers++;
        activeLayer++;
        Layer newLayer = new Layer(canvasWidth, canvasHeight, transparent);
        myLayers.add(newLayer);
        layersVisibility.add(1);
        clearLayer();
        return newLayer;
    }

    public void deleteLayer(){
        if(numLayers <= 1){
            return;
        }
        //remove from list
        myLayers.remove(activeLayer);
        //remove from visibility toggle
        layersVisibility.remove(activeLayer);
        if(activeLayer<=0) {
            activeLayer=0;
        }
        numLayers--;
    }

    public Layer getLayer(int index)
    {
        return myLayers.get(index);
    }

    public Layer getActiveLayer()
    {
        return myLayers.get(activeLayer);
    }

    public int getActiveLayerNumber(){
        return activeLayer;
    }

    public int getLayerCount()
    {
        return numLayers;
    }

    public int[] getLayerDimensions(){
        return new int[]{canvasWidth,canvasHeight};
    }

    /**
     * Removes the active layer from the linked list of layers
     */
    public void removeLayer(){
        myLayers.remove(activeLayer);
        activeLayer--;
        numLayers--;
    }

    public int getNumLayers(){
        return numLayers;
    }

    /**
     * Sets the current active layer to a new value
     * @param newActiveLayer an integer specifying which layer to set to the active layer
     */
    public void setActiveLayer(int newActiveLayer){
        if(newActiveLayer<0 || newActiveLayer>numLayers){
            System.out.println("ERROR: the active layer is being set to a value < 0 OR value > numLayers\n Error is present in Layer class");
            System.exit(0);
        }
        activeLayer = newActiveLayer;
    }

    public void setActiveLayerArray(int[][][] newLayerArray){
        myLayers.get(activeLayer).setLayerArray(newLayerArray);
    }

    public void openNewProject(LinkedList<Layer> myNewLayers){
        myLayers = myNewLayers;
        numLayers = myNewLayers.size();
        activeLayer = 0;
        layersVisibility = new LinkedList<Integer>();
        for(int i = 0; i<numLayers; i++){
            layersVisibility.add(1);
        }
    }

    /**
     * Shifts the active layer 1 up the linked list
     */
    public boolean moveLayerUp(int index){
        if(index>=numLayers-1){ //If the active layer is already at the top ie end of the linked list
            return false;
        }
        else{
            Collections.swap(myLayers, index, index+1);
            Collections.swap(layersVisibility, index, index+1);
            return true;
        }  
    }

    /**
     * Shifts the active layer 1 down the linked list
     */
    public boolean moveLayerDown(int index){
        if(index<=0){ //If the active layer is already at the top ie end of the linked list
            return false;
        }
        else{
            Collections.swap(myLayers, index, index-1);
            Collections.swap(layersVisibility, index, index-1);
            return true;
        }  
    }

    public void toggleVisibility(int index){
        if(layersVisibility.get(index) == 0){
            layersVisibility.set(index, 1);
        }
        else{
            layersVisibility.set(index, 0);
        }
    }
}

