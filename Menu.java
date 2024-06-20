import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Menu implements ActionListener{
    private JFrame frame;
    private JPanel menuPanel;
    private JPanel formPanel;
    private JPanel formWrapper;
    private JLabel widthLabel;
    private JLabel heightLabel;
    private JTextField widthField;
    private JTextField heightField;
    private JButton enterButton;
    private int width,height;
    
    public Menu(){
        try{
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch(Exception e)
        {
        }

        frame = new JFrame();
        frame.setTitle("Pixel Editor");
        frame.setSize(250,130);

        menuPanel = new JPanel();
        formPanel = new JPanel();
        formWrapper = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));
        formPanel.setLayout(new GridLayout(2,2));

        formPanel.add(widthLabel = new JLabel("Width", SwingConstants.CENTER));
        formPanel.add(widthField = new JTextField());
        formPanel.add(heightLabel = new JLabel("Height", SwingConstants.CENTER));
        formPanel.add(heightField = new JTextField());

        Dimension fieldSize = new Dimension(100, 20);

        heightField.setMinimumSize(fieldSize);
        heightField.setMaximumSize(fieldSize);
        heightField.setPreferredSize(fieldSize);
        
        widthField.setMinimumSize(fieldSize);
        widthField.setMaximumSize(fieldSize);
        widthField.setPreferredSize(fieldSize);


        formWrapper.add(formPanel);
        menuPanel.add(formWrapper);
        menuPanel.add(enterButton = new JButton("Start"));

        widthField.setToolTipText("Enter Width");
        heightField.setToolTipText("Enter Height");

        enterButton.addActionListener(this);

        frame.setContentPane(menuPanel);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == enterButton){
            GUI mainFrame = new GUI();
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }
}
