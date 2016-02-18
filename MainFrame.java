/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daphne.s.drawing.designer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 *
 * @author Luke Aaron Reynolds - lreynolds188@gmail.com
 */
public class DaphnesDrawingDesigner extends JFrame implements WindowListener, ActionListener 
{
    // Declare important variables to be used through out the program.
    int intTotalX = 30;
    int intTotalY = 30;
    int arrayLocX = 0;
    int arrayLocY = 0;
    int panelLocX = 0;
    int panelLocY = 0;
    String strCurrentColor = "W";
    Color clrTempColor = Color.WHITE;
    Color clrCurrentColor = Color.WHITE;
    
    JTextField[][] grid = new JTextField[intTotalX][intTotalY]; 
    String[][] tempArray = new String[intTotalX][intTotalY];
    JLabel lblTitle, lblColor;
    JButton btnClear, btnRotate90, btnFlipHorizontal, btnFlipVertical, btnSaveImage, btnSaveRAF, btnQuit;
    JTextField jTxtBlue, jTxtGreen, jTxtRed, jTxtYellow, jTxtBlack, jTxtWhite;
    
    /// main method
    public static void main(String[] args) 
    {
        // Create a new JFrame and give it some values
        JFrame myJFrame = new DaphnesDrawingDesigner();
        myJFrame.setSize(1100, 600);
        myJFrame.setLocation(400, 200);
        myJFrame.setBackground(Color.WHITE);
        myJFrame.setResizable(false);
        myJFrame.setVisible(true);
    }
    
    /// initializes the layout, panel, and runs various methods
    public DaphnesDrawingDesigner()
    {
        setTitle("Daphne's Drawing Designer");
        
        // create layout using SpringLayout and set it.
        SpringLayout myLayout = new SpringLayout();
        setLayout(myLayout);
        
        // create a JPanel and add it to the JFrame 
        JPanel myJPanel = new JPanel();
        myJPanel.setBackground(Color.RED);
        myJPanel.setPreferredSize(new Dimension(900, 450));
        myJPanel.setLayout(myLayout);
        myJPanel.setVisible(false);
        add(myJPanel);
        
        // add constraints for the JPanel
        myLayout.putConstraint(SpringLayout.WEST, myJPanel, 170, SpringLayout.WEST, this);
        myLayout.putConstraint(SpringLayout.NORTH, myJPanel, 35, SpringLayout.NORTH, this);
        
        GenerateGrid(myLayout, myJPanel);
        CreateLabels(myLayout);
        CreateButtons(myLayout);
        CreateJTextFields(myLayout);
        
        LoadImage();
        
        // add window listener
        this.addWindowListener(this);
    }
    
    /// creates all labels by sending the required variables to the createLabel method
    public void CreateLabels(SpringLayout myLabelLayout)
    {
        Font fntTitle = new Font("Verdana", Font.BOLD, 20);
        Font fntColor = new Font("Verdana", Font.PLAIN, 15);
        lblTitle = CreateLabel(myLabelLayout, lblTitle, "Children's Drawing Program", fntTitle, 420, 10);
        lblColor = CreateLabel(myLabelLayout, lblColor, "Select Colour", fntColor, 33, 50);
    }
    
    /// creates a label using the information sent to it when it was called
    public JLabel CreateLabel(SpringLayout myLabelLayout, JLabel myLabel, String strLabel, Font myFont, int x, int y)
    {
        myLabel = new JLabel(strLabel);
        add(myLabel);
        myLabel.setFont(myFont);
        myLabelLayout.putConstraint(SpringLayout.WEST, myLabel, x, SpringLayout.WEST, this);
        myLabelLayout.putConstraint(SpringLayout.NORTH, myLabel, y, SpringLayout.NORTH, this);
        return myLabel;
    }
    
    /// creates all buttons by sending the required fields to the createButton method
    public void CreateButtons(SpringLayout myButtonLayout)
    {
        btnClear = CreateButton(myButtonLayout, btnClear, "Clear", Color.WHITE, 180, 510, 100, 30);
        btnRotate90 = CreateButton(myButtonLayout, btnRotate90, "Rotate 90°", Color.WHITE, 320, 510, 100, 30);
        btnFlipHorizontal = CreateButton(myButtonLayout, btnFlipHorizontal, "Flip Horizontally", Color.WHITE, 425, 510, 130, 30);
        btnFlipVertical = CreateButton(myButtonLayout, btnFlipVertical, "Flip Vertically", Color.WHITE, 560, 510, 120, 30);
        btnSaveImage = CreateButton(myButtonLayout, btnSaveImage, "Save Image", Color.WHITE, 725, 510, 100, 30);
        btnSaveRAF = CreateButton(myButtonLayout, btnSaveRAF, "Save RAF", Color.WHITE, 830, 510, 100, 30);
        btnQuit = CreateButton(myButtonLayout, btnQuit, "Quit", Color.WHITE, 980, 510, 100, 30);
    }
    
    /// creates a button using the variables sent to it when it was called
    public JButton CreateButton(SpringLayout myButtonLayout, JButton myButton, String strButton, Color color, int x, int y, int width, int height)
    {
        myButton = new JButton(strButton);
        add(myButton);
        myButton.addActionListener(this);
        myButton.setBackground(color);
        myButtonLayout.putConstraint(SpringLayout.WEST, myButton, x, SpringLayout.WEST, this);
        myButtonLayout.putConstraint(SpringLayout.NORTH, myButton, y, SpringLayout.NORTH, this);
        myButton.setPreferredSize(new Dimension (width, height));
        return myButton;
    }
    
    /// creates all JTextFields by sending the required variables to the CreateJTextField method
    public void CreateJTextFields(SpringLayout myJTextAreaLayout)
    {
        // Color pallets
        jTxtBlue = CreateJTextField(myJTextAreaLayout, jTxtBlue, Color.BLUE, 25, 100, 120, 50, "B");
        jTxtGreen = CreateJTextField(myJTextAreaLayout, jTxtGreen, Color.GREEN, 25, 175, 120, 50, "G");
        jTxtRed = CreateJTextField(myJTextAreaLayout, jTxtRed, Color.RED, 25, 250, 120, 50, "R");
        jTxtYellow = CreateJTextField(myJTextAreaLayout, jTxtYellow, Color.YELLOW, 25, 325, 120, 50, "Y");
        jTxtBlack = CreateJTextField(myJTextAreaLayout, jTxtBlack, Color.BLACK, 25, 400, 120, 50, "L");
        jTxtWhite = CreateJTextField(myJTextAreaLayout, jTxtWhite, Color.WHITE, 25, 475, 120, 50, "W");
    }
    
    /// creates a JTextField with the variables sent to it when it was called, and adds a mouseListener
    public JTextField CreateJTextField(SpringLayout myJTextFieldLayout, JTextField myJTextField, Color color, int x, int y, int width, int height, String setStrColor)
    {
        myJTextField = new JTextField();
        add(myJTextField);
        myJTextField.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                strCurrentColor = setStrColor;
                SetColor();
            }
        });
        myJTextField.setEnabled(false);
        myJTextField.setBackground(color);
        myJTextFieldLayout.putConstraint(SpringLayout.WEST, myJTextField, x, SpringLayout.WEST, this);
        myJTextFieldLayout.putConstraint(SpringLayout.NORTH, myJTextField, y, SpringLayout.NORTH, this);
        myJTextField.setPreferredSize(new Dimension (width, height));
        return myJTextField;
    }
    
    /// generate the grid fields to the JPanel
    public void GenerateGrid(SpringLayout myGridLayout, JPanel myJPanel)
    {
        // for each index on the Y Axis
        for (arrayLocY = 0; arrayLocY < intTotalY; arrayLocY++)
        {
            if(arrayLocX != 29)
            {
                panelLocX = 0;
                panelLocY += 15;
            }

            // for each index on the X Axis
            for (arrayLocX = 0; arrayLocX < intTotalX; arrayLocX++)
            {
                
                grid[arrayLocX][arrayLocY] = CreateGridLayout(myGridLayout, myJPanel, arrayLocX, arrayLocY);
                panelLocX += 30;
            }
        }
    }
    
    /// creates a new JTextField (GridField) at the corresponding co-ordinants, gives it default values and a mouse listener
    public JTextField CreateGridLayout(SpringLayout myGridLayout, JPanel myJPanel, int gridX, int gridY)
    {
        grid[arrayLocX][arrayLocY] = new JTextField();
        add(grid[arrayLocX][arrayLocY]);
        grid[arrayLocX][arrayLocY].setPreferredSize(new Dimension(30, 15));
        grid[arrayLocX][arrayLocY].setEnabled(false);
        grid[arrayLocX][arrayLocY].setText("W");
        grid[arrayLocX][arrayLocY].addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                    {
                        for (arrayLocY = 0; arrayLocY < intTotalY; arrayLocY++)
                        {
                            for(arrayLocX = 0; arrayLocX < intTotalX; arrayLocX++)
                            {
                                if(e.getSource().equals(grid[arrayLocX][arrayLocY]))
                                {
                                    grid[arrayLocX][arrayLocY].setText(strCurrentColor);
                                    grid[arrayLocX][arrayLocY].setBackground(clrCurrentColor);
                                }
                            }
                        }
                    }
            });
        myGridLayout.putConstraint(SpringLayout.WEST, grid[gridX][gridY], panelLocX, SpringLayout.WEST, myJPanel);
        myGridLayout.putConstraint(SpringLayout.NORTH, grid[gridX][gridY], panelLocY, SpringLayout.NORTH, myJPanel);
        return grid[arrayLocX][arrayLocY];
    }
    
    /// takes the strCurrentColor and matches it with its corresponding color and returns the color to clrCurrentColor.
    public Color SetColor()
    {
        switch (strCurrentColor)
        {
            case "B" : clrCurrentColor = Color.BLUE;
                break;
            case "G" : clrCurrentColor = Color.GREEN;
                break;
            case "R" : clrCurrentColor = Color.RED;
                break;
            case "Y" : clrCurrentColor = Color.YELLOW;
                break;
            case "L" : clrCurrentColor = Color.BLACK;
                break;
            case "W" : clrCurrentColor = Color.WHITE;
                break;
            default : clrCurrentColor = Color.WHITE;
                break;
        }
        return clrCurrentColor;
    }
    
    /// takes the string sent to the method and matches it with its corresponding color and returns the color to clrTempColor.
    public Color SetColor(String myString)
    {
        switch (myString)
        {
            case "B" : clrTempColor = Color.BLUE;
                break;
            case "G" : clrTempColor = Color.GREEN;
                break;
            case "R" : clrTempColor = Color.RED;
                break;
            case "Y" : clrTempColor = Color.YELLOW;
                break;
            case "L" : clrTempColor = Color.BLACK;
                break;
            case "W" : clrTempColor = Color.WHITE;
                break;
            default : clrTempColor = Color.WHITE;
                break;
        }
        return clrTempColor;
    }
    
    /// loads a temporary array containing
    public void LoadTempArray()
    {
        for (arrayLocY = 0; arrayLocY < intTotalY; arrayLocY++)
            {
                for(arrayLocX = 0; arrayLocX < intTotalX; arrayLocX++)
                {
                    grid[arrayLocX][arrayLocY].setBackground(SetColor(tempArray[arrayLocX][arrayLocY]));
                    grid[arrayLocX][arrayLocY].setText(tempArray[arrayLocX][arrayLocY]);
                }
            }
    }
    
    /// saves the image 3 times into 3 seperate files, each with its own format type.
    public void SaveImage()
    {
        String strLastTextIndex = grid[0][0].getText();
        int intMatches = 0;
        
        try
        {
            // create a new printWriter with the Filename "MyPicture" in 3 types ".csv", ".txt", ".abc"
            PrintWriter pWriterCSV = new PrintWriter(new FileWriter("MyPicture.csv"));
            PrintWriter pWriterTXT = new PrintWriter(new FileWriter("MyPicture.txt"));
            PrintWriter pWriterABC = new PrintWriter(new FileWriter("MyPicture.abc"));
            
            // for each index on the Y axis
            for (arrayLocY = 0; arrayLocY < intTotalY; arrayLocY++)
            {
                // for each index on the X axis
                for(arrayLocX = 0; arrayLocX < intTotalX; arrayLocX++)
                {
                    // if the current index matches the previous index
                    if (grid[arrayLocX][arrayLocY].getText().equals(strLastTextIndex))
                    {
                        // match counter
                        intMatches++;
                        // if at the last index in the line
                        if (arrayLocX == 29)
                        {
                            if (intMatches != 0)
                            {
                                pWriterABC.print(intMatches + strLastTextIndex);
                            }
                            strLastTextIndex = grid[arrayLocX][arrayLocY].getText();
                            intMatches = 0;
                            pWriterABC.print("\n");
                        }
                    }
                    else 
                    {
                        if (intMatches != 0)
                        {
                            pWriterABC.print(intMatches + strLastTextIndex + ",");
                        }
                        strLastTextIndex = grid[arrayLocX][arrayLocY].getText();
                        intMatches = 1;
                    }
                    
                    // if at the end of the line dont print a comma
                    if (arrayLocX == 29)
                    {
                        // print each index's text into a .csv file
                        pWriterCSV.print(grid[arrayLocX][arrayLocY].getText());
                    } 
                    else
                    {
                        pWriterCSV.print(grid[arrayLocX][arrayLocY].getText() + ",");
                    }
                    
                    // write the X Loc, Y Loc, and the containing String
                    pWriterTXT.println(arrayLocX + "," + arrayLocY + "," + grid[arrayLocX][arrayLocY].getText());
                }
                // at the end of each Y axis add a new line to the CSV and TXT files
                pWriterCSV.print("\n");
                pWriterTXT.print("\n");
            }
            // close all the writers
            pWriterCSV.close();
            pWriterTXT.close();
            pWriterABC.close();
        } 
        catch(Exception errorTxt)
        {
            System.err.println("Something messed up -_-" + errorTxt.getMessage());
        }
    }
    
    /// saves the image to a random access file
    public void SaveRAF ()
    {
        try
        {
            // create a new RandomAccessFile called "raf" and make it read/write
            RandomAccessFile raf = new RandomAccessFile("RAF.txt", "rw");
        
            // for each location on the Y axis
            for (arrayLocY = 0; arrayLocY < intTotalY; arrayLocY++)
            {
                // for each location on the X axis
                for(arrayLocX = 0; arrayLocX < intTotalX; arrayLocX++)
                {
                    // write the array Locations and the field text to the random access file
                    raf.writeUTF(arrayLocX + "," + arrayLocY + "," + grid[arrayLocX][arrayLocY].getText().toString());
                }
            }
            // close the RandomAccessFile
            raf.close();
        }
        catch (Exception errorTxt)
        {
            
        }
    }
    
    /// loads the image onto the screen
    public void LoadImage()
    {
        try
        {
            // create a buffered reader with a new input stream reader from a new data input stream using a new file input stream with the filename "MyPicture.csv"
            BufferedReader bReader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream("MyPicture.csv"))));
            
            String strLine;
            String[] tempAry = new String[30];
            arrayLocY = 0;
            
            while ((strLine = bReader.readLine()) != null)
            {
                tempAry = strLine.split(",");
                
                for(arrayLocX = 0; arrayLocX < intTotalX; arrayLocX++)
                {
                    grid[arrayLocX][arrayLocY].setText(tempAry[arrayLocX]);
                    grid[arrayLocX][arrayLocY].setBackground(SetColor(tempAry[arrayLocX]));
                }
                arrayLocY++;
            }
        }
        catch(Exception errorTxt)
        {
            
        }
    }
    
    /// clears the image from the screen
    public void Clear()
    {
        for (arrayLocY = 0; arrayLocY < intTotalY; arrayLocY++)
            {
                for(arrayLocX = 0; arrayLocX < intTotalX; arrayLocX++)
                {
                    grid[arrayLocX][arrayLocY].setText("W");
                    grid[arrayLocX][arrayLocY].setBackground(Color.WHITE);
                }
            }
    }
    
    /// rotate image 90 degrees
    public void Rotate90()
    {
        int arrayXChange = 29;
            int arrayYChange = 0;
            
            for (arrayLocY = 0; arrayLocY < intTotalY; arrayLocY++)
            {
                for (arrayLocX = 0; arrayLocX < intTotalX; arrayLocX++)
                {
                    tempArray[arrayLocX+arrayXChange][arrayLocY+arrayYChange] = grid[arrayLocX][arrayLocY].getText();
                    arrayYChange++;
                    arrayXChange--;
                }
                arrayYChange -= 31;
                arrayXChange += 29;
            }
            LoadTempArray();
    }
    
    /// flip image horizontally
    public void FlipHorizontal()
    {
        int arrayXChange = 29;
            for (arrayLocY = 0; arrayLocY < intTotalY; arrayLocY++)
            {
                for (arrayLocX = 0; arrayLocX < intTotalX; arrayLocX++)
                {
                    tempArray[arrayXChange][arrayLocY] = grid[arrayLocX][arrayLocY].getText();
                    arrayXChange--;
                }
                arrayXChange = 29;
            }
            LoadTempArray();
    }
    
    /// flip image vertically
    public void FlipVertical()
    {
        int arrayYChange = 29;
            for (arrayLocY = 0; arrayLocY < intTotalY; arrayLocY++)
            {
                for (arrayLocX = 0; arrayLocX < intTotalX; arrayLocX++)
                {
                    tempArray[arrayLocX][arrayYChange] = grid[arrayLocX][arrayLocY].getText();
                }
                arrayYChange--;
            }
            LoadTempArray();
    }
    
    /// handles event actions
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btnClear)
        {
            Clear();
        }
        
        if (e.getSource() == btnRotate90)
        {   
            Rotate90();
        }
        
        if (e.getSource() == btnFlipHorizontal)
        {   
            FlipHorizontal();
        }
        
        if (e.getSource() == btnFlipVertical)
        {   
            FlipVertical();
        }
        
        if (e.getSource() == btnSaveImage)
        {
            SaveImage();
        }
        
        if (e.getSource() == btnSaveRAF)
        {
            SaveRAF();
        }
        
        if (e.getSource() == btnQuit)
        {
            System.exit(0);
        }
    }

    public void windowOpened(WindowEvent e) {}

    public void windowClosing(WindowEvent e) { System.exit(0); }

    public void windowClosed(WindowEvent e) {}

    public void windowIconified(WindowEvent e) {}

    public void windowDeiconified(WindowEvent e) {}

    public void windowActivated(WindowEvent e) {}

    public void windowDeactivated(WindowEvent e) {}
    
}
