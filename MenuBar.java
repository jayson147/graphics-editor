
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

//import static com.sun.beans.introspect.ClassInfo.clear;

public class MenuBar extends JFrame {

    private Image image;
    private Menu menu;
    private boolean edited;
    private LayerManager layerManager;

    private MyCanvas myCanvas;

    public Graphics2D g2;
    private GUI gui;


    JMenuBar menuBar;

    MenuBar() {

        menuBar = new JMenuBar();

        this.setJMenuBar(menuBar);
    }



    public BufferedImage resizeImage (BufferedImage image) {
        BufferedImage resized = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), 0,
        0, resized.getWidth(), resized.getHeight(), null);
        return resized;
    }



    public void createButton() {

        image = layerManager.getImage();
        g2 = (Graphics2D) image.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        clear();
        GUI mainFrame = new GUI();

    }
    public static BufferedImage convertRGBImage(int[][][] rgbValue) {
        int height = rgbValue.length;
        int width = rgbValue[0].length;
        int transparency = rgbValue[1].length;


        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //we either have to loop through all values, or convert to 1-d array
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int i = 0; i < transparency; i++) {

                    bufferedImage.setRGB(x, y, rgbValue[y][x][i]);
                }
            }
        }
        return bufferedImage;
    }


    public void openButton(){

        String file= null;

        if(file.endsWith(".png") || file.endsWith(".jpg")){

            BufferedImage image = null;
            myCanvas.g2 = layerManager.getImage().createGraphics();

            try{
                image = ImageIO.read(LayerManager.class.getClassLoader().getResourceAsStream("images" + file));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }


    }
    public void saveImage(){

        JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG(*.png)", "png"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            String filename = fileChooser.getSelectedFile().toString();

            if (!filename.endsWith(".png")){
                filename += ".png";
                fileChooser.setSelectedFile(new File(filename));
                fileChooser.getSelectedFile().getName();
            }

            try {
                ImageIO.write(layerManager.getImage(), "png", fileChooser.getSelectedFile()); // saving buffered image in format of png


            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error", "Failed to save file!", JOptionPane.ERROR_MESSAGE);
            }
        }

    }
    }




