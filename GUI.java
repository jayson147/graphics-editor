import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

class LayerGroup
{       
    public JPanel panel;
    public JFrame thumbnailFrame;
    public JButton selectLayer;
    public JButton moveUp;
    public JButton moveDown;
    public JButton mergeDown;
    public JButton visibleToggle;
    public JPanel upDownPanel;
    public JPanel selectVisPanel;
    public Layer representedLayer;
    public boolean isToggled;

    public LayerGroup(ActionListener actionListener)
    {
        panel = new JPanel();
        panel.setBackground(Color.lightGray);
        thumbnailFrame = new JFrame();
        selectLayer = new JButton();
        isToggled = false;

        ImageIcon visibleIcon = new ImageIcon("Icons\\eye-outline.png");
        Image visibleImage = visibleIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);

        ImageIcon upIcon = new ImageIcon("Icons\\chevron-up-outline.png");
        Image upImg = upIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);

        ImageIcon downIcon = new ImageIcon("Icons\\chevron-down-outline.png");
        Image downImg = downIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        
        moveDown = new JButton(new ImageIcon(upImg));
        moveUp = new JButton(new ImageIcon(downImg));

        visibleToggle = new JButton(new ImageIcon(visibleImage));

        selectLayer.addActionListener(actionListener);
        moveDown.addActionListener(actionListener);
        moveUp.addActionListener(actionListener);
        visibleToggle.addActionListener(actionListener);

        selectVisPanel = new JPanel();
        selectVisPanel.setLayout(new GridLayout(1,2));

        upDownPanel = new JPanel();
        upDownPanel.setLayout(new GridLayout(1,3));
        upDownPanel.add(visibleToggle);
        upDownPanel.add(moveUp);
        upDownPanel.add(moveDown);

        visibleToggle.setBorder(BorderFactory.createEmptyBorder());
        selectLayer.setBorder(BorderFactory.createEmptyBorder());
        moveDown.setBorder(BorderFactory.createEmptyBorder());
        moveUp.setBorder(BorderFactory.createEmptyBorder());

        selectVisPanel.add(selectLayer);
    }
}

public class GUI {
    private JToolBar toolBar = new JToolBar();
    private JPanel layerPanel = new JPanel();
    private JPanel toolsAndColoursPanel = new JPanel();
    private JPanel toolsPanel = new JPanel();
    private JPanel coloursPanel = new JPanel();
    private JPanel colourGrid = new JPanel();
    private JPanel colourGridWrapper = new JPanel();
    private JPanel toolsGrid = new JPanel();
    private JPanel layerButtons = new JPanel();
    private JPanel layerTools = new JPanel();
    private JScrollPane layerScroll = new JScrollPane();
    private JFrame mainFrame = new JFrame();

    private Icon colorIcon = new ImageIcon("Icons/colorIcon32.png");
    private MenuBar menuBar;
    private JButton newButton = new JButton("New");
    private JButton saveButton = new JButton("Save");
    private JButton exportButton = new JButton("Export");
    private JButton openButton = new JButton("Open");

    private JButton addLayerButton = new JButton("Add Layer");
    private JButton deleteLayerButton = new JButton("Delete Layer");
    private JButton clearButton = new JButton("Clear");
    private JButton drawButton = new JButton("Draw");
    private JButton eraseButton = new JButton("Erase");
    private JButton increaseBrushXButton = new JButton("Brush X +");
    private JButton decreaseBrushXButton = new JButton("Brush X -");
    private JButton increaseBrushYButton = new JButton("Brush Y +");
    private JButton decreaseBrushYButton = new JButton("Brush Y -");

    private JButton eyedropButton = new JButton("Select Colour");
    private JButton colourPickerButton = new JButton(colorIcon);
    private int[][] basicColoursValues = new int[][] {{0,0,0}, {255,255,255}, {255,0,0}, {0,255,0}, {0,0,255}, {255,255,0}, {255,0,255}, {0,255,255}, {255,191,0}};
    private JButton[] basicColours = new JButton[basicColoursValues.length];

    private JButton lineButton = new JButton("Draw Line");
    private JButton rectButton = new JButton("Draw Rectangle");
    private JButton circButton = new JButton("Draw Circle");
    private JButton triaButton = new JButton("Draw Triangle");

    private JButton mirrorActiveLayerHorizontalButton = new JButton("Mirror Horizontal");
    private JButton mirrorActiveLayerVerticalButton = new JButton("Mirror Vertical");
    private JButton toggleAnimationButton = new JButton("Play/Pause");
    JSlider framesPerSecond = new JSlider(JSlider.HORIZONTAL, 1, 10, 3);
    private JButton greyScaleButton = new JButton("Grey Scale");
    private JButton fillButton = new JButton("Fill");

    private JLabel numLayersLabel = new JLabel("Num Layers: 1", SwingConstants.CENTER);
    private JLabel layersLabel = new JLabel("Layers - ", SwingConstants.CENTER);
    private JLabel toolsLabel = new JLabel("Tools", SwingConstants.CENTER);
    private JLabel coloursLabel = new JLabel("Colour", SwingConstants.CENTER);
    private int frameWidth, frameHeight;
    private ArrayList<LayerGroup> layerGroups;
    protected JColorChooser chooser = new JColorChooser();

    private LayerFilterer layerFilterer;
    private ToolManager toolManager;
    private MyCanvas myCanvas;
    private LayerManager layerManager;
    private int lastSelectedLayer = 0;

    public GUI() {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameWidth = 1000;
        frameHeight = 800;

        layerManager = new LayerManager(frameWidth, frameHeight);
        myCanvas = new MyCanvas(frameWidth, frameHeight, layerManager);
        toolManager = new ToolManager(frameWidth, frameHeight, myCanvas, layerManager);
        layerFilterer = new LayerFilterer(layerManager);

        layerGroups = new ArrayList<LayerGroup>();

        addLayerButton.addActionListener(actionListener);
        deleteLayerButton.addActionListener(actionListener);
        clearButton.addActionListener(actionListener);
        drawButton.addActionListener(actionListener);
        eraseButton.addActionListener(actionListener);
        colourPickerButton.addActionListener(actionListener);
        eyedropButton.addActionListener(actionListener);
        
        lineButton.addActionListener(actionListener);
        rectButton.addActionListener(actionListener);
        circButton.addActionListener(actionListener);
        triaButton.addActionListener(actionListener);

        increaseBrushXButton.addActionListener(actionListener);
        decreaseBrushXButton.addActionListener(actionListener);
        increaseBrushYButton.addActionListener(actionListener);
        decreaseBrushYButton.addActionListener(actionListener);

        newButton.addActionListener(actionListener);
        saveButton.addActionListener(actionListener);
        exportButton.addActionListener(actionListener);
        openButton.addActionListener(actionListener);

        mirrorActiveLayerHorizontalButton.addActionListener(actionListener);
        mirrorActiveLayerVerticalButton.addActionListener(actionListener);
        greyScaleButton.addActionListener(actionListener);
        fillButton.addActionListener(actionListener);
        toggleAnimationButton.addActionListener(actionListener);

        layerPanel.setLayout(new BoxLayout(layerPanel, BoxLayout.PAGE_AXIS));
        layerPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new LineBorder(Color.black, 2, true)));
        layerPanel.setSize(layerPanel.getPreferredSize());

        JPanel layersLabelsPanel = new JPanel(new FlowLayout());
        layersLabelsPanel.add(layersLabel);
        layersLabelsPanel.add(numLayersLabel);
        layerPanel.add(layersLabelsPanel);
        layerPanel.add(layerScroll);

        layerTools.setLayout(new GridBagLayout());
        GridBagConstraints layerToolConstraints = new GridBagConstraints();
        layerToolConstraints.gridwidth = 3;
        layerToolConstraints.gridheight = 1;
        layerToolConstraints.anchor = GridBagConstraints.PAGE_END;
        layerToolConstraints.fill = GridBagConstraints.HORIZONTAL;

        layerTools.add(addLayerButton);
        layerTools.add(deleteLayerButton);
        layerPanel.add(layerTools, layerToolConstraints);


        layerScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        layerScroll.setViewportView(layerButtons);
        layerButtons.setBackground(Color.lightGray);

        toolBar.add(newButton);
        toolBar.add(saveButton);
        toolBar.add(exportButton);
        toolBar.add(openButton);

        toolBar.add(mirrorActiveLayerVerticalButton);
        toolBar.add(mirrorActiveLayerHorizontalButton);
        toolBar.add(greyScaleButton);
        toolBar.add(toggleAnimationButton);

        toolsAndColoursPanel.setLayout(new GridLayout(2, 1));
        toolsAndColoursPanel.add(toolsPanel);
        toolsAndColoursPanel.add(coloursPanel);

        toolsPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new LineBorder(Color.black, 2, true)));
        toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.PAGE_AXIS));

        JPanel toolsLabPanel = new JPanel(new FlowLayout());
        toolsLabPanel.add(toolsLabel);
        toolsPanel.add(toolsLabPanel);

        JScrollPane toolsGridScrollPane= new JScrollPane();
        toolsGridScrollPane.setViewportView(toolsGrid);
        toolsGridScrollPane.setHorizontalScrollBarPolicy(toolsGridScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        toolsPanel.add(toolsGridScrollPane);
        toolsGrid.setBorder(new EmptyBorder(10, 10, 10, 10));
        toolsGrid.setLayout(new GridLayout(0, 1));

        ImageIcon drawIcon = new ImageIcon("Icons/edit-outline.png");
        Image drawImage = drawIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        drawButton.setIcon(new ImageIcon(drawImage));
        toolsGrid.add(drawButton);

        ImageIcon eraseIcon = new ImageIcon("Icons/close-outline.png");
        Image eraseImage = eraseIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        eraseButton.setIcon(new ImageIcon(eraseImage));
        toolsGrid.add(eraseButton);

        ImageIcon clearIcon = new ImageIcon("Icons/trash-2-outline.png");
        Image clearImage = clearIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        clearButton.setIcon(new ImageIcon(clearImage));
        toolsGrid.add(clearButton);

        ImageIcon LineIcon = new ImageIcon("Icons/trending-up-outline.png");
        Image LineImage = LineIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        lineButton.setIcon(new ImageIcon(LineImage));
        toolsGrid.add(lineButton);

        ImageIcon rectIcon = new ImageIcon("Icons/square-outline.png");
        Image rectImage = rectIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        rectButton.setIcon(new ImageIcon(rectImage));
        toolsGrid.add(rectButton);

        ImageIcon circIcon = new ImageIcon("Icons/radio-button-off-outline.png");
        Image circImage = circIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        circButton.setIcon(new ImageIcon(circImage));
        toolsGrid.add(circButton);

        ImageIcon triIcon = new ImageIcon("Icons/arrow-up-outline.png");
        Image triImage = triIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        triaButton.setIcon(new ImageIcon(triImage));
        toolsGrid.add(triaButton);

        ImageIcon eyedropIcon = new ImageIcon("Icons/color-picker-outline.png");
        Image eyedropImage = eyedropIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        eyedropButton.setIcon(new ImageIcon(eyedropImage));
        toolsGrid.add(eyedropButton);

        ImageIcon fillIcon = new ImageIcon("Icons/droplet-outline.png");
        Image fillImage = fillIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        fillButton.setIcon(new ImageIcon(fillImage));
        toolsGrid.add(fillButton);

        JPanel sizeGrid = new JPanel(new GridLayout(2, 2));
        sizeGrid.add(increaseBrushXButton);
        sizeGrid.add(decreaseBrushXButton);
        sizeGrid.add(increaseBrushYButton);
        sizeGrid.add(decreaseBrushYButton);
        toolsGrid.add(sizeGrid);

        coloursPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new LineBorder(Color.black, 2, true)));
        coloursPanel.setLayout(new GridLayout(0, 1));
        
        JPanel colourLabPanel = new JPanel(new FlowLayout());
        colourLabPanel.add(coloursLabel);
        coloursPanel.add(colourLabPanel);
        coloursPanel.add(colourGrid);

        colourGrid.setLayout(new GridLayout(0, 3));
        colourGrid.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(0, 35, 20, 35), new LineBorder(Color.black, 1, true)));
        
        JPanel colourPickerPanel = new JPanel(new FlowLayout());
        colourPickerPanel.add(colourPickerButton);
        coloursPanel.add(colourPickerPanel);

        for (int i = 0; i < basicColours.length; i++) {
            basicColours[i] = new JButton();
            basicColours[i].addActionListener(actionListener);
            basicColours[i].setBackground(new Color(basicColoursValues[i][0], basicColoursValues[i][1], basicColoursValues[i][2]));
            basicColours[i].setMinimumSize(new Dimension(40, 40));
            basicColours[i].setMaximumSize(new Dimension(40, 40));
            basicColours[i].setPreferredSize(new Dimension(40, 40));
            colourGrid.add(basicColours[i]);
            basicColours[i].setContentAreaFilled(false);
            basicColours[i].setOpaque(true);
        }

        mainFrame.setTitle("Paint");
        ImageIcon windowIcon = new ImageIcon("Icons/brush-outline.png");
        mainFrame.setIconImage(windowIcon.getImage());
        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(layerPanel, BorderLayout.WEST);
        mainFrame.add(toolsAndColoursPanel, BorderLayout.EAST);
        mainFrame.add(toolBar, BorderLayout.NORTH);
        mainFrame.add(myCanvas, BorderLayout.CENTER);

        mainFrame.setSize(1000 + 100, 800 - 160);
        mainFrame.setBackground(Color.yellow);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        AddLayerGUI(0);
        layerGroups.get(0).panel.setBackground(Color.white);
        //removing tabs in the JColorChooser
        AbstractColorChooserPanel[] tabs = chooser.getChooserPanels();
        for (int i = 0; i < tabs.length; i++) {
            System.out.println(tabs[i]);
            String tabName = tabs[i].getDisplayName();
            switch (tabName) {
                case "Swatches":
                    chooser.removeChooserPanel(tabs[i]);
                    break;
                case "HSV":
                    chooser.removeChooserPanel(tabs[i]);
                    break;
                case "HSL":
                    chooser.removeChooserPanel(tabs[i]);
                    break;
                case "RGB":
                    break;
                case "CMYK":
                    chooser.removeChooserPanel(tabs[i]);
                    break;
            }
        }
        menuBar = new MenuBar();

        myCanvas.addMouseListener(new MouseAdapter() { 
            public void mouseReleased(MouseEvent e) {
                updateActiveThumbnail();}});
        
    }


    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == drawButton) {
                layerManager.setDraw();
                toolManager.setTool(toolManager.currentTool.FREEHAND);
            } else if (e.getSource() == eraseButton) {
                layerManager.setErase();
                toolManager.setTool(toolManager.currentTool.ERASE);
            } else if (e.getSource() == clearButton) {
                layerManager.clearLayer();
                myCanvas.repaint();
                updateActiveThumbnail();
            } else if (e.getSource() == lineButton) {
                layerManager.setDraw();
                toolManager.setTool(toolManager.currentTool.LINE);
            } else if (e.getSource() == rectButton) {
                layerManager.setDraw();
                toolManager.setTool(toolManager.currentTool.RECT);
            } else if (e.getSource() == circButton) {
                layerManager.setDraw();
                toolManager.setTool(toolManager.currentTool.CIRCLE);
            } else if (e.getSource() == triaButton) {
                layerManager.setDraw();
                toolManager.setTool(toolManager.currentTool.TRIANGLE);
            } else if (e.getSource() == addLayerButton) {
                layerManager.addLayer();
                numLayersLabel.setText("Num Layers: " + layerManager.getLayerCount());
                AddLayerGUI(layerManager.getLayerCount()-1);
            } else if (e.getSource() == deleteLayerButton) {
                layerManager.deleteLayer();
                numLayersLabel.setText("Num Layers: " + layerManager.getLayerCount());
                layerGroups.remove(layerManager.getActiveLayerNumber());
                layerButtons.remove(layerManager.getActiveLayerNumber());
                layerGroups.get(layerManager.getActiveLayerNumber()).selectLayer.setText("Layer " + String.valueOf(layerManager.getActiveLayerNumber()+1));
                layerButtons.revalidate();
                myCanvas.repaint();
            } else if (e.getSource() == eyedropButton) {
                layerManager.setDraw();
                toolManager.setTool(toolManager.currentTool.EYEDROP);
            } else if(e.getSource() == colourPickerButton) {
                    class okListener implements ActionListener {
                            public void actionPerformed(ActionEvent e) {
                                Color chosenColor = chooser.getColor();
                                int red = chosenColor.getRed();
                                int green = chosenColor.getGreen();
                                int blue = chosenColor.getBlue();
                                int alpha = chosenColor.getAlpha();
                                coloursPanel.setBackground(chosenColor);
                                layerManager.setColor(red, green, blue, alpha);
                            }
                        }
    
                    JDialog dialog = chooser.createDialog(coloursPanel, "Choose a colour", false, chooser, new okListener(), null);
                    dialog.setVisible(true); 
            } else if (e.getSource() == increaseBrushXButton) {
                toolManager.changeBrushX(1);
            } else if (e.getSource() == decreaseBrushXButton) {
                toolManager.changeBrushX(-1);
            } else if (e.getSource() == increaseBrushYButton) {
                toolManager.changeBrushY(1);
            } else if (e.getSource() == decreaseBrushYButton) {
                toolManager.changeBrushY(-1);
            }
            else if(e.getSource() == fillButton){
                layerManager.setDraw();
                toolManager.setTool(toolManager.currentTool.FILL);
                //layerFilterer.fillLayer();
                //myCanvas.repaint();
            } else if (e.getSource() == openButton) {
                JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                fileChooser.setFileFilter(new FileNameExtensionFilter("text(*.txt)", "txt"));
                fileChooser.setAcceptAllFileFilterUsed(false);

                if (fileChooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
                    String filename = fileChooser.getSelectedFile().getAbsolutePath();
                    Scanner scanner;
                    try {
                        scanner = new Scanner(new File(filename));
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }

                    //remove all current UI buttons
                    layerGroups.removeAll(layerGroups);
                    layerButtons.removeAll();

                    //get number of layers in project
                    int numLayers = scanner.nextInt();

                    //get canvas dimensions
                    int height = scanner.nextInt();
                    int width = scanner.nextInt();

                    //new linked list of layers for new project
                    LinkedList<Layer> newProject = new LinkedList<Layer>();

                    for (int layerNum = 0; layerNum < numLayers; layerNum++) {

                        Layer tempLayer = new Layer(width, height, 255);
                        int[][][] tempLayerArray = new int[height][width][4];
                        for (int i = 0; i < height; i++) {
                            for (int j = 0; j < width; j++) {
                                for (int k = 0; k < 4; k++) {
                                    //get next int value
                                    tempLayerArray[i][j][k] = scanner.nextInt();
                                }
                            }
                        }
                        tempLayer.setLayerArray(tempLayerArray);
                        newProject.add(tempLayer);

                    }
                    //close scanner
                    scanner.close();
                    //set active layer to the new layer
                    layerManager.openNewProject(newProject);

                    for(int i = 0; i < numLayers; i++){
                        AddLayerGUI(i);
                    }

                    myCanvas.repaint();
                }
            }

            else if(e.getSource() == saveButton) {
                JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir") + "/SavedLayers/"));
                fileChooser.setFileFilter(new FileNameExtensionFilter("text(*.txt)", "txt"));
                fileChooser.setAcceptAllFileFilterUsed(false);

                if (fileChooser.showSaveDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {

                    String filename = fileChooser.getSelectedFile().toString();

                    StringBuilder builder = new StringBuilder();

                    //add num layers
                    builder.append(layerManager.getNumLayers() + "\n");
                    //add canvas dimensions
                    builder.append(layerManager.getLayerDimensions()[1] + "\n");
                    builder.append(layerManager.getLayerDimensions()[0] + "\n");

                    //for every layer
                    for (int numLayer = 0; numLayer < layerManager.getNumLayers(); numLayer++) {

                        //get layer array
                        int[][][] myLayer = layerManager.getLayer(numLayer).getLayerArray();

                        for (int i = 0; i < myLayer.length; i++)//for height
                        {
                            for (int j = 0; j < myLayer[0].length; j++)//for width
                            {
                                for (int k = 0; k < 4; k++) { //for r,g,b and transparency

                                    builder.append(myLayer[i][j][k] + "\n");//append to the output string
                                }
                            }
                        }
                    }

                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                        writer.write(builder.toString());
                        writer.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    }
            }
            else if(e.getSource() == newButton)
            {
                GUI mainFrame = new GUI();

            }
            else if(e.getSource()== exportButton){
                JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
                fileChooser.setFileFilter(new FileNameExtensionFilter("PNG(*.png)", "png"));
                fileChooser.setAcceptAllFileFilterUsed(false);

                if (fileChooser.showSaveDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {

                    String filename = fileChooser.getSelectedFile().toString();

                    if (!filename.endsWith(".png")){
                        filename += ".png";
                        fileChooser.setSelectedFile(new File(filename));
                        fileChooser.getSelectedFile().getName();
                    }
                    try {
                        ImageIO.write(layerManager.getImage(), "png", fileChooser.getSelectedFile());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error", "Failed to save file!", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
                else if (e.getSource() == mirrorActiveLayerHorizontalButton) {
                    layerFilterer.mirrorLayerHorizontal();
                    myCanvas.repaint();
                } else if (e.getSource() == mirrorActiveLayerVerticalButton) {
                    layerFilterer.mirrorLayerVertical();
                    myCanvas.repaint();
                } else if (e.getSource() == greyScaleButton) {
                    layerFilterer.greyScale();
                    myCanvas.repaint();
                } else if(e.getSource() == toggleAnimationButton){
                    myCanvas.toggleAnimation();
                    myCanvas.repaint();
                }
                else {
                    for (int i = 0; i < layerGroups.size(); i++) {
                        LayerGroup interactedLayerGroup = layerGroups.get(i);

                        if (e.getSource() == interactedLayerGroup.selectLayer) {
                            layerManager.setActiveLayer(i);
                            interactedLayerGroup.panel.setBackground(Color.white);
                            if (lastSelectedLayer != i) {
                                layerGroups.get(lastSelectedLayer).panel.setBackground(Color.LIGHT_GRAY);
                            }
                            lastSelectedLayer = i;
                        }
                        else if (e.getSource() == interactedLayerGroup.moveUp) {
                            //swap order of layer in linked list
                            if (layerManager.moveLayerUp(i) == true) {    //if it's possible to move layer up then function returns true
                                //swap the "Layer Number: X"  text on the GUI button
                                LayerGroup layerGroupAbove = layerGroups.get(i + 1);
                                String aboveLayer = layerGroupAbove.selectLayer.getText();
                                layerGroupAbove.selectLayer.setText(interactedLayerGroup.selectLayer.getText());
                                interactedLayerGroup.selectLayer.setText(aboveLayer);
                                //update thumbnail
                                layerGroupAbove.selectLayer.setIcon(new ImageIcon(layerManager.getThumbnail(i+1).getScaledInstance(myCanvas.canvasWidth/11, myCanvas.canvasHeight/11, Image.SCALE_SMOOTH)));
                                interactedLayerGroup.selectLayer.setIcon(new ImageIcon(layerManager.getThumbnail(i).getScaledInstance(myCanvas.canvasWidth/11, myCanvas.canvasHeight/11, Image.SCALE_SMOOTH)));

                                //update canvas
                                myCanvas.repaint();
                            }
                        }
                        else if (e.getSource() == interactedLayerGroup.moveDown) {
                            //if it's possible to move the layer down
                            if (layerManager.moveLayerDown(i) == true) {
                                LayerGroup layerGroupBelow = layerGroups.get(i - 1);
                                String belowLayer = layerGroupBelow.selectLayer.getText();
                                layerGroupBelow.selectLayer.setText(interactedLayerGroup.selectLayer.getText());
                                interactedLayerGroup.selectLayer.setText(belowLayer);
                                //update thumbnail
                                layerGroupBelow.selectLayer.setIcon(new ImageIcon(layerManager.getThumbnail(i-1).getScaledInstance(myCanvas.canvasWidth/11, myCanvas.canvasHeight/11, Image.SCALE_SMOOTH)));
                                interactedLayerGroup.selectLayer.setIcon(new ImageIcon(layerManager.getThumbnail(i).getScaledInstance(myCanvas.canvasWidth/11, myCanvas.canvasHeight/11, Image.SCALE_SMOOTH)));

                                myCanvas.repaint();
                            }
                        }
                        else if(e.getSource() == interactedLayerGroup.visibleToggle){
                            layerManager.toggleVisibility(i);
                            myCanvas.repaint();

                            if(layerGroups.get(i).isToggled)
                            {
                                ImageIcon visibleIcon = new ImageIcon("Icons\\eye-outline.png");
                                Image visibleImage = visibleIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                                layerGroups.get(i).visibleToggle.setIcon(new ImageIcon(visibleImage));
                                layerGroups.get(i).isToggled = !(layerGroups.get(i).isToggled);
                            }
                            else
                            {
                                ImageIcon visibleIcon = new ImageIcon("Icons\\eye-off-outline.png");
                                Image visibleImage = visibleIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                                layerGroups.get(i).visibleToggle.setIcon(new ImageIcon(visibleImage));
                                layerGroups.get(i).isToggled = !(layerGroups.get(i).isToggled);
                            }
                        }
                    }
                    
                    for(int i = 0; i < basicColours.length; i++){
                        if(e.getSource() == basicColours[i]){
                            layerManager.setColor(basicColoursValues[i][0],basicColoursValues[i][1],basicColoursValues[i][2],255);
                        }
                    }
                }
            }
        };

        public void updateActiveThumbnail()
        {
            layerGroups.get(layerManager.getActiveLayerNumber()).selectLayer.setIcon(new ImageIcon(layerManager.getThumbnail(layerManager.getActiveLayerNumber()).getScaledInstance(myCanvas.canvasWidth/11, myCanvas.canvasHeight/11, Image.SCALE_SMOOTH)));;
        }

        public void AddLayerGUI(int indexOfLayer) {
            numLayersLabel.setText("Num Layers: " + layerManager.getLayerCount());
            layerGroups.add(new LayerGroup(actionListener));
            JPanel layerGroupPanel = layerGroups.get(indexOfLayer).panel;
            layerGroupPanel.setLayout(new BorderLayout());
            layerGroupPanel.setBackground(Color.white);
            layerGroupPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 5, 10, 5), new LineBorder(Color.black, 2, true)));
            layerGroups.get(indexOfLayer).selectLayer.setText("Layer " + (indexOfLayer+1));
            layerGroupPanel.add(layerGroups.get(indexOfLayer).selectVisPanel, BorderLayout.CENTER);
            layerGroupPanel.add(layerGroups.get(indexOfLayer).upDownPanel, BorderLayout.SOUTH);

            layerButtons.setLayout(new GridLayout(0, 1));
            layerButtons.setBorder(new EmptyBorder(0, 0, 1000, 0));
            layerButtons.add(layerGroups.get(indexOfLayer).panel);
            layerGroups.get(indexOfLayer).panel.setMaximumSize(new Dimension(220, 100));
            layerGroups.get(indexOfLayer).selectLayer.setIcon(new ImageIcon(layerManager.getThumbnail(indexOfLayer).getScaledInstance(myCanvas.canvasWidth/11, myCanvas.canvasHeight/11, Image.SCALE_SMOOTH)));
            layerGroups.get(indexOfLayer).selectLayer.setContentAreaFilled(false);
        }
    }
