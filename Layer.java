public class Layer {
    private int[][][] myLayer;
    private int canvasWidth, canvasHeight;

    private int transparent;

    public Layer(int canvasWidth, int canvasHeight, int transparent){
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.transparent = transparent;
        myLayer = new int[canvasHeight][canvasWidth][4];
    }

    public int[][][] getLayerArray(){
        return myLayer;
    }

    public void setLayerArray(int[][][] newLayerArray){
        for(int i = 0; i < canvasHeight; i++){
            for(int j = 0; j < canvasWidth; j++){
                myLayer[i][j][0] = newLayerArray[i][j][0];
                myLayer[i][j][1] = newLayerArray[i][j][1];
                myLayer[i][j][2] = newLayerArray[i][j][2];
                myLayer[i][j][3] = newLayerArray[i][j][3];
            }
        }
    }

    public void setPixel(int xCor, int yCor, int colour[]){
        myLayer[yCor][xCor][0] = colour[0];
        myLayer[yCor][xCor][1] = colour[1];
        myLayer[yCor][xCor][2] = colour[2];
        myLayer[yCor][xCor][3] = colour[3];
    }

    public int[] getColour(int xCor, int yCor){
        int colour[] = new int[4];
        colour[0] = myLayer[yCor][xCor][0];
        colour[1] = myLayer[yCor][xCor][1];
        colour[2] = myLayer[yCor][xCor][2];
        colour[3] = myLayer[yCor][xCor][3];
        return colour;
    }
    public void clear(){
        for(int i = 0; i < canvasHeight; i++){
            for(int j = 0; j < canvasWidth; j++){
                myLayer[i][j][0] = transparent;
                myLayer[i][j][1] = transparent;
                myLayer[i][j][2] = transparent;
                myLayer[i][j][3] = 0;   //0 for transparent
            }
        }
    }
}