import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

public class MyCanvas extends JComponent {
// Image in which we're going to draw
    private Image image;
    // Graphics2D object ==> used to draw on
    public Graphics2D g2;
    // Canvas size
    public int canvasWidth, canvasHeight;

    private LayerManager layerManager;

    //animation variables
    private boolean isAnimating = false;
    private Timer myTimer;
    private RemindTask myRemindTask;
    public int currentAnimFrame;

    public MyCanvas(int canvasWidth, int canvasHeight, LayerManager layerManager) {
        this.layerManager = layerManager;

        setDoubleBuffered(false);

        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (image == null) {        
            // image to draw null ==> we create
            image = layerManager.getImage();
            g2 = (Graphics2D) image.getGraphics();
            // enable antialiasing
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            // clear draw area
            layerManager.clearLayer();
        }
        if(isAnimating == false){
            g.drawImage(layerManager.getImage(), 0, 0, null);
        }
        else{
            g.drawImage(layerManager.getThumbnail(myRemindTask.getCurrentAnimFrame()), 0, 0, null);
        }
    }

    public void toggleAnimation(){
        if(isAnimating == false){
            isAnimating = true;
            playAnimation();
        }
        else{
            isAnimating = false;
            stopAnimation();
        }
    }
    private void playAnimation() {
        myTimer = new Timer();
        myRemindTask = new RemindTask(layerManager.getLayerCount(), this);
        myTimer.scheduleAtFixedRate(myRemindTask, 0,500);  //subsequent rate
    }

    private void stopAnimation(){
        myTimer.cancel();
    }

    class RemindTask extends TimerTask {
        int numFrames;
        int currentAnimFrame = 0;

        MyCanvas myCanvas;
        public RemindTask(int numFrames, MyCanvas myCanvas){
            this.numFrames = numFrames-1;
            this.myCanvas = myCanvas;
        }
        public int getCurrentAnimFrame(){
            return currentAnimFrame;
        }
        public void run() {
            if (currentAnimFrame >= numFrames){
                currentAnimFrame = 0;
            }
            else {
                currentAnimFrame++;
            }
            myCanvas.repaint();
        }
    }
}