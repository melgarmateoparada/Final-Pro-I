package org.proIII.appManejoImagenes.operations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PaintSection {
    private static final Logger logger = LogManager.getRootLogger();

    private int referenceColor;
    private int marginColor;
    private int [][]imagePainted;

    public PaintSection() {

    }

    public void floodFill(int[][]image, int x, int y, int newColor, int marginColor) {
        referenceColor = image[x][y];
        this.marginColor = marginColor;
        if (referenceColor == newColor) {
            imagePainted = image;
            return;
        }
        paintPixels(image, x, y, newColor);
        logger.debug("Change color" + referenceColor + " to " + newColor);
        imagePainted = image;
    }

    public void paintPixels(int[][]image, int x, int y, int newColor) {
        if (x < 0 || y < 0 || x >= image.length || y >= image[0].length) {
            return;
        } if(newColor == image[x][y]){
            return;
        }
        if(isInMargin(image[x][y])){
            image[x][y] = newColor;
            paintPixels(image, x + 1, y,  newColor);
            paintPixels(image, x - 1, y,  newColor);
            paintPixels(image, x, y + 1,  newColor);
            paintPixels(image, x, y - 1,  newColor);
        }
    }
    public boolean isInMargin(int currentColor) {
        int currentRed = (currentColor >> 16) & 0xFF;
        int currentGreen = (currentColor >> 8) & 0xFF;
        int currentBlue = currentColor & 0xFF;
        int referenceRed = (referenceColor >> 16) & 0xFF;
        int refernceGreen = (referenceColor >> 8) & 0xFF;
        int referenceBlue = referenceColor & 0xFF; //60

        int[] currentColors = {currentRed, currentGreen, currentBlue};
        int[] referenceColors = {referenceRed, refernceGreen, referenceBlue};
        for(int i=0; i < 3;i++){
            if(!(currentColors[i] < (referenceColors[i] + marginColor)
                    && currentColors[i] > (referenceColors[i] - marginColor))){
                return false;
            }
        }
        return true;

    }

    public int[][] getImagePainted() {
        return imagePainted;
    }
}
