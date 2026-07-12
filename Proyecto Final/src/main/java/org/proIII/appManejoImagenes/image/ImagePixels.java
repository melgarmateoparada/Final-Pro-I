package org.proIII.appManejoImagenes.image;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.proIII.appManejoImagenes.exceptions.ExcepcionImagen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ImagePixels {
    private static final Logger log = LogManager.getLogger(ImagePixels.class);
    private int widthImage;
    private int heightImage;
    private int[][] imagePixels;
    private BufferedImage bi;
    private PropertyChangeSupport supportObserver;
    private static final String IMAGE_OBSERVER = "IMAGE";

    public ImagePixels(int widthImage, int heightImage){
        supportObserver = new PropertyChangeSupport(this);
        this.widthImage = widthImage;
        this.heightImage = heightImage;
        imagePixels = new int[widthImage][heightImage];
        bi = new BufferedImage(widthImage, heightImage, BufferedImage.TYPE_INT_RGB);
    }

    public ImagePixels(File image){
    try {
        bi = ImageIO.read(image);
    }catch (IOException e){e.printStackTrace();}
    widthImage = bi.getWidth();
    heightImage = bi.getHeight();
    imagePixels = new int[widthImage][heightImage];
        for (int i = 0; i < widthImage; i++) {
            for (int j = 0; j < heightImage; j++) {
                imagePixels[i][j] = bi.getRGB(i,j);
            }
        }
    supportObserver = new PropertyChangeSupport(this);
    }

    public void drawImage(Graphics g){

        try {
            if (image() != null) {
                g.drawImage(image(), 0, 0, null);
            }
        }catch (NullPointerException e){log.error("Error: " + e.getMessage());}
    }


    public BufferedImage image(){
        if(bi ==null){
            bi = new BufferedImage(widthImage, heightImage, BufferedImage.TYPE_INT_RGB);
        }
        for (int i = 0; i < widthImage; i++) {
            for (int j = 0; j < heightImage; j++) {
                int color = imagePixels[i][j];
                bi.setRGB(i,j, color);
            }
        }
     return bi;
    }


    public void addObserver(PropertyChangeListener observer){
        supportObserver.addPropertyChangeListener(observer);
    }

    public void pixelsChange(int [][] newImagePixels){
        int [][] previousImage = Arrays.copyOf(imagePixels, imagePixels.length) ;
        imagePixels = newImagePixels;
        supportObserver.firePropertyChange(IMAGE_OBSERVER, imagePixels, previousImage);
    }


    //Getter and setter

    public int getWidthImage() {
        return widthImage;
    }

    public void setWidthImage(int widthImage) {
        this.widthImage = widthImage;
    }

    public int getHeightImage() {
        return heightImage;
    }

    public void setHeightImage(int heightImage) {
        this.heightImage = heightImage;
    }

    public int[][] getImagePixels() {
        return imagePixels;
    }

    public void setImagePixels(int[][] imagePixels) {
        this.imagePixels = imagePixels;
    }

    public BufferedImage getBi() {
        return bi;
    }

    public void setBi(BufferedImage bi) {
        this.bi = bi;
    }

    public PropertyChangeSupport getSupportObserver() {
        return supportObserver;
    }

    public void setSupportObserver(PropertyChangeSupport supportObserver) {
        this.supportObserver = supportObserver;
    }
}
