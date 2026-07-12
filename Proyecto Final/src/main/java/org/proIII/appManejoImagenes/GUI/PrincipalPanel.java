package org.proIII.appManejoImagenes.GUI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.proIII.appManejoImagenes.command.*;
import org.proIII.appManejoImagenes.image.ImagePixels;
import org.proIII.appManejoImagenes.operations.DrawLine;
import org.proIII.appManejoImagenes.operations.DrawPoint;
import org.proIII.appManejoImagenes.operations.DrawSquare;
import org.proIII.appManejoImagenes.operations.PaintSection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PrincipalPanel extends JPanel implements PropertyChangeListener, MouseListener, MouseMotionListener {
    private ImagePixels image;
    private SecundaryPanel secundaryPanel;
    private static final Logger logger = LogManager.getRootLogger();
    private DrawPoint drawPoint;
    private DrawSquare drawSquare;
    private DrawLine drawLine;
    private int [][] moldImage;
    private int xPrevious, yPrevious, xCurrent, yCurrent, xFinal, yFinal;
    private Invoker invoker;
    private DrawSquareCommand drawSquareCommand;
    private DrawLineCommand drawLineCommand;
    private DrawPointCommand drawPointCommand;

    public PrincipalPanel(ImagePixels image, SecundaryPanel ps, Invoker invoker) {
        secundaryPanel = ps;
        this.image = image;
        this.invoker = invoker;
        drawPoint = new DrawPoint();
        drawLine = new DrawLine();
        drawSquare = new DrawSquare();
        addMouseListener(this);
        image.addObserver(this);
        addMouseMotionListener(this);
        xPrevious = 0;
        yPrevious = 0;
        xCurrent = 0;
        yCurrent = 0;
        xFinal = 0;
        yFinal = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        image.drawImage(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(image.getWidthImage(), image.getHeightImage());
    }
    public void setImage(ImagePixels imagePixels) {
        this.image = imagePixels;
        this.image.addObserver(this);
    }



    @Override
    public void mousePressed(MouseEvent e) {


        if(!secundaryPanel.isSelectedColor()){return;}
        int x = e.getX();
        int y = e.getY();
        if(e.getX() < 0 || e.getY() <0 ||e.getX() >= image.getWidthImage() || e.getY() >= image.getHeightImage()){return;}
        xPrevious = x;
        yPrevious = y;
        try {
            if(secundaryPanel.isActivePoint()){
                drawPointCommand = new DrawPointCommand(drawPoint, image, x, y, secundaryPanel.getColorSelected());
                invoker.excecute(drawPointCommand);
                moldImage =  drawPoint.getDrawnImage();
                image.pixelsChange(moldImage);
            }


        } catch (Exception ex) {
            logger.error("Error to paint that image: " + ex.getMessage(), ex);
        }
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        if (!secundaryPanel.isSelectedColor()) return;
        int x =e.getX();
        int y = e.getY();
        if (e.getX() < 0 || e.getY() < 0 || e.getX() >= image.getWidthImage() || e.getY() >= image.getHeightImage()) return;
        try {
          xCurrent = x;
          yCurrent = y;

    if (secundaryPanel.isActivePoint()) {
        drawPoint.drawLineBetweenPoints(xPrevious, yPrevious, xCurrent, yCurrent, secundaryPanel.getColorSelected());
        moldImage = drawPoint.getDrawnImage();
        image.pixelsChange(moldImage);
        xPrevious = xCurrent;
        yPrevious = yCurrent;
    }
}catch (Exception ex){
    logger.info(ex.getMessage());
}
    }



    @Override
    public void mouseReleased(MouseEvent e) {
        xFinal = e.getX();
        yFinal = e.getY();
        if (e.getX() < 0 || e.getY() < 0 || e.getX() >= image.getWidthImage() || e.getY() >= image.getHeightImage()) {return;}

        if (secundaryPanel.isActiveLine()) {
            drawLineCommand = new DrawLineCommand(drawLine, image, xPrevious, yPrevious, xFinal, yFinal, secundaryPanel.getColorSelected());
            invoker.excecute(drawLineCommand);
            moldImage = drawLine.getDrawmImage();
            image.pixelsChange(moldImage);
        } else if (secundaryPanel.isActiveSquare()) {
            drawSquareCommand = new DrawSquareCommand(drawSquare, image, xPrevious, yPrevious, xFinal, yFinal, secundaryPanel.getColorSelected());
            invoker.excecute(drawSquareCommand);
            moldImage = drawSquare.getDrawnImage();
            image.pixelsChange(moldImage);
        }

    }


    public void mouseClicked(MouseEvent e) {
        try {
            if (secundaryPanel.isActivePaintSection() && secundaryPanel.getRange() > 0) {
                PaintSection paintSection = new PaintSection();
                CommandExcecute paintSectionCommand = new PaintSectionCommand(paintSection, image, e.getX(), e.getY(), secundaryPanel.getColorSelected(), secundaryPanel.getRange());
                invoker.excecute(paintSectionCommand);
                moldImage = paintSection.getImagePainted();
                image.pixelsChange(moldImage);
            }
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }
    }


    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        logger.debug("The image changed");
        this.repaint();
    }


}
