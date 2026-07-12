package org.proIII.appManejoImagenes.operations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.proIII.appManejoImagenes.image.ImagePixels;

public class DrawSquare {
    private static final Logger logger = LogManager.getRootLogger();
    private int drawnImage [][];


    public void DrawSquare(ImagePixels image, int x0, int y0, int x1, int y1, int color) {

        int ancho = Math.abs(x1 - x0);
        int alto = Math.abs(y1 - y0);

        int xRect = (x1 < x0) ? x0 - ancho : x0;
        int yRect = (y1 < y0) ? y0 - alto : y0;

        int maxX = image.getImagePixels().length;
        int maxY = image.getImagePixels()[0].length;

        for (int x = xRect; x <= xRect + ancho && x < maxX; x++) {
            for (int y = yRect; y <= yRect + alto && y < maxY; y++) {
                image.getImagePixels()[x][y] = color;
            }
        }
        logger.debug("The square have been drawing from: x " + x0 + ", y " + y0 + ", to: x " + x1 + ", y " + y1);
        drawnImage = image.getImagePixels();
    }

    public int[][] getDrawnImage() {
        return drawnImage;
    }
}
