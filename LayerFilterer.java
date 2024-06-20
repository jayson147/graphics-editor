import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.image.BufferedImage;
import java.awt.Color;
public class LayerFilterer{
    private LayerManager layerManager;

    public LayerFilterer(LayerManager layerManager){
        this.layerManager = layerManager;
    }

    public void mirrorLayerVertical(){
        int[][][] myOldLayer = layerManager.getActiveLayer().getLayerArray();
        int[] layerDimensions = layerManager.getLayerDimensions();
        int[][][] myNewLayer = new int[layerDimensions[1]][layerDimensions[0]][4];

        for(int x = 0; x < layerDimensions[0]; x++){
            for(int y = 0; y < layerDimensions[1]; y++){
                myNewLayer[y][x][0] = myOldLayer[layerDimensions[1]-1-y][x][0];
                myNewLayer[y][x][1] = myOldLayer[layerDimensions[1]-1-y][x][1];
                myNewLayer[y][x][2] = myOldLayer[layerDimensions[1]-1-y][x][2];
                myNewLayer[y][x][3] = myOldLayer[layerDimensions[1]-1-y][x][3];
            }
        }

        layerManager.getActiveLayer().setLayerArray(myNewLayer);
    }

    public void mirrorLayerHorizontal(){
        int[][][] myOldLayer = layerManager.getActiveLayer().getLayerArray();
        int[] layerDimensions = layerManager.getLayerDimensions();
        int[][][] myNewLayer = new int[layerDimensions[1]][layerDimensions[0]][4];

        for(int x = 0; x < layerDimensions[0]; x++){
            for(int y = 0; y < layerDimensions[1]; y++){
                myNewLayer[y][x][0] = myOldLayer[y][layerDimensions[0]-1-x][0];
                myNewLayer[y][x][1] = myOldLayer[y][layerDimensions[0]-1-x][1];
                myNewLayer[y][x][2] = myOldLayer[y][layerDimensions[0]-1-x][2];
                myNewLayer[y][x][3] = myOldLayer[y][layerDimensions[0]-1-x][3];
            }
        }

        layerManager.getActiveLayer().setLayerArray(myNewLayer);
    }

    public void greyScale(){
        int[][][] myOldLayer = layerManager.getActiveLayer().getLayerArray();
        int[] layerDimensions = layerManager.getLayerDimensions();
        int[][][] myNewLayer = new int[layerDimensions[1]][layerDimensions[0]][4];

        for(int x = 0; x < layerDimensions[0]; x++){
            for(int y = 0; y < layerDimensions[1]; y++){
                int greyscale = (int)((0.3*myOldLayer[y][x][0])+(0.59*myOldLayer[y][x][1])+(0.11*myOldLayer[y][x][2]));
                myNewLayer[y][x][0] = greyscale;
                myNewLayer[y][x][1] = greyscale;
                myNewLayer[y][x][2] = greyscale;
                myNewLayer[y][x][3] = myOldLayer[y][x][3];
            }
        }

        layerManager.getActiveLayer().setLayerArray(myNewLayer);
    }
    public void fillLayer(){
        int[][][] myOldLayer = layerManager.getActiveLayer().getLayerArray();
        int[] layerDimensions = layerManager.getLayerDimensions();
        int[][][] myNewLayer = new int[layerDimensions[1]][layerDimensions[0]][4];

        for(int x = 0; x < layerDimensions[0]; x++){
            for(int y = 0; y < layerDimensions[1]; y++){
                int fill = (int)((myOldLayer[y][x][0])+(myOldLayer[y][x][1])+(myOldLayer[y][x][2]));
                myNewLayer[y][x][0] = fill;
                myNewLayer[y][x][1] = fill;
                myNewLayer[y][x][2] = fill;
                myNewLayer[y][x][3] = myOldLayer[y][x][3];
            }
        }

        layerManager.getActiveLayer().setLayerArray(myNewLayer);
    }
}