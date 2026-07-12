package org.proIII.appManejoImagenes.GUI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.proIII.appManejoImagenes.command.Invoker;
import org.proIII.appManejoImagenes.image.ImagePixels;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SecundaryPanel extends JPanel {
    private Border border;
    private JLabel infoImage;
    private static final Logger logger = LogManager.getRootLogger();
    private JButton jColorChooser, activeTool, undo;
    private ImagePixels image;
    private JColorChooser colorChooser;
    private Color colorSelected;
    private GridBagConstraints c;
    private boolean  isActivePaintSection,isActivePoint, isActiveSquare, isActiveLine, isSelectedColor;
    private Invoker invoker;
    private int range;
    
    public SecundaryPanel(ImagePixels image, Invoker invoker){
        this.image = image;
        this.invoker = invoker;
        this.setLayout(new GridBagLayout());
        this.isSelectedColor = false;
        c = new GridBagConstraints();
        infoImage = new JLabel("");
        colorSelection();
        redoLastAction();
        selectionRange();
        Bordes();
        this.setVisible(true);

    }
    public void Bordes() {
        border = new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(Color.BLACK);  
                int widthBorder = 5;
                g.fillRect(x, y, width, widthBorder);
                g.fillRect(x, y + height - widthBorder, width, widthBorder);
                g.fillRect(x, y, widthBorder, height);
                g.fillRect(x + width - widthBorder, y, widthBorder, height);
            }

            @Override
            public Insets getBorderInsets(Component c) {
                int grosor = 5;
                return new Insets(grosor, grosor, grosor, grosor);
            }

            @Override
            public boolean isBorderOpaque() {
                return true;
            }
        };
        this.setBorder(border);
    }
    @Override
    public Dimension getPreferredSize() {
            return new Dimension(250, image.getHeightImage());
    }

    public void infoImagen() {
        String altura = "High: " + String.valueOf(image.getHeightImage());
        String ancho = ", wight: " + String.valueOf(image.getWidthImage());
        infoImage.setText(altura + ancho);
        Font fontImagen = new Font("Calibri", Font.BOLD, 24);
        infoImage.setFont(fontImagen);
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(1,1,1,1);
        add(infoImage, c);
        this.repaint();
    }

    public void setImagen(ImagePixels image) {
        this.image = image;
        infoImagen();
    }//Para actualizar los datos de la nueva imagen

    private void colorSelection() {
        jColorChooser = new JButton();
        activeTool = new JButton("Off");
        activeTool.setPreferredSize(new Dimension(105,40));
        jColorChooser.setPreferredSize(new Dimension(80, 80));
        jColorChooser.setBackground(Color.LIGHT_GRAY);
        jColorChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colorChooser = new JColorChooser(jColorChooser.getBackground());
                JDialog dialog = JColorChooser.createDialog(null, "Select a color", true, colorChooser, e1 -> {
                    colorSelected = colorChooser.getColor();
                    jColorChooser.setBackground(colorSelected);
                    isSelectedColor = true;
                }, null);
                dialog.setVisible(true);}});


        activeTool.addActionListener(e -> {
            if(colorSelected == null){return;}

            if (!isActivePoint&& !isActiveSquare && !isActiveLine && !isActivePaintSection) {
                isActivePoint = true;
                activeTool.setText("Point");
                logger.info("Point activated");
            } else if (isActivePoint) {
                isActivePoint = false;
                isActiveSquare = true;
                activeTool.setText("Square");
                logger.info("Square activated");
            } else if (isActiveSquare) {
                isActiveSquare = false;
                isActiveLine = true;
                activeTool.setText("Line");
                logger.info("Line activated");
            } else if (isActiveLine) {
                isActiveLine = false;
                isActivePaintSection = true;
                activeTool.setText("Paint");
                logger.info("Paint section activated");
            } else if (isActivePaintSection) {
                isActivePaintSection = false;
                activeTool.setText("off");
                logger.info("tool turned off");
            }
        });


        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(20,50,50,50);
        add(jColorChooser, c);
        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(30,50,50,50);
        add(activeTool, c);
    }
    public void redoLastAction(){
        undo = new JButton("Undo");
        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(30,50,50,50);
        add(undo, c);
        undo.addActionListener(e -> {
            invoker.undo();
            logger.info("Invoker undo that section");
        });

    }
    public void selectionRange() {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1)); // Valor inicial, mínimo, máximo, incremento
        spinner.addChangeListener(e -> range= (int) spinner.getValue());
        c.gridx = 0;
        c.gridy =5;
        c.insets = new Insets(40,50,50,50);
        spinner.setPreferredSize(new Dimension(70,30));
        add(spinner, c);
    }

//Getter


    public boolean isSelectedColor() {
        return isSelectedColor;
    }

    public void setSelectedColor(boolean selectedColor) {
        isSelectedColor = selectedColor;
    }

    public boolean isActivePaintSection() {
        return isActivePaintSection;
    }

    public boolean isActivePoint() {
        return isActivePoint;
    }

    public boolean isActiveSquare() {
        return isActiveSquare;
    }

    public boolean isActiveLine() {
        return isActiveLine;
    }

    public JColorChooser getColorChooser() {
        return colorChooser;
    }

    public void setColorChooser(JColorChooser colorChooser) {
        this.colorChooser = colorChooser;
    }

    public int getColorSelected() {
        return colorSelected.getRGB();
    }

    public void setColorSelected(Color colorSelected) {
        this.colorSelected = colorSelected;
    }

    public int getRange() {
        return range;
    }
}
