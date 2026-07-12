package org.proIII.appManejoImagenes.operations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.proIII.appManejoImagenes.image.ImagePixels;

public class DrawLine {
    private static final Logger logger = LogManager.getRootLogger();
    private int [][] drawmImage;
    public void DrawLine(ImagePixels image, int x0, int y0, int x1, int y1, int color) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;
        int radio = 3;

        while (true) {
            for (int i = -radio; i <= radio; i++) {
                for (int j = -radio; j <= radio; j++) {
                    if (i * i + j * j <= radio * radio) {
                        int newX = x0 + i;
                        int newY = y0 + j;
                        if (newX >= 0 && newX < image.getWidthImage() && newY >= 0 && newY < image.getHeightImage()) {
                            if (image.getImagePixels()[newX][newY] != color) {
                                image.getImagePixels()[newX][newY] = color;
                            }
                        }
                    }
                }
            }

            if (x0 == x1 && y0 == y1) {
                break;
            }
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }

        drawmImage = image.getImagePixels();
        logger.info("Line drawn from point x: " + x0 + ", y: " + y0 + " to final point x: " + dx + ", y: " + dy);
    }

    public int[][] getDrawmImage() {
        return drawmImage;
    }
}

