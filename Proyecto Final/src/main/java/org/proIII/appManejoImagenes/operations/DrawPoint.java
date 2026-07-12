package org.proIII.appManejoImagenes.operations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.proIII.appManejoImagenes.image.ImagePixels;


public class DrawPoint {
    private static final Logger logger = LogManager.getRootLogger();
    private ImagePixels image;
    private int x,y, color;
    private int [][] drawnImage;

    public void drawPoint(ImagePixels image, int x, int y, int color) {
        this.image = image;
        if (image == null || image.getImagePixels() == null) {return;}
        this.x = x;
        this.y = y;
        this.color = color;
        if (x < 0 || y < 0 || x >= image.getWidthImage() || y >= image.getHeightImage()) {return;}
       this.drawnImage = image.getImagePixels();
        if (drawnImage[x][y] == color) {
            return;
        }

        int radius = 3;
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                if (i * i + j * j <= radius * radius) { // Dentro del círculo
                    int newX = x + i;
                    int newY = y + j;

                    if (newX >= 0 && newX < image.getWidthImage() && newY >= 0 && newY < image.getHeightImage()) {
                        if (drawnImage[newX][newY] != color) {
                            drawnImage[newX][newY] = color;

                            String msg = String.format("Drawn x: %d, y: %d", newX, newY);
                            logger.info(msg);
                        }
                    }
                }
            }
        }
    }

    public void drawLineBetweenPoints(int x1, int y1, int x2, int y2, int color) {
        // Bresenham
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;

        while (true) {
            drawPoint(image, x1, y1, color);
            if (x1 == x2 && y1 == y2) {
                break;
            }
            int e2 = err * 2;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
    }

    public int[][]getDrawnImage(){
        return drawnImage;
    }
}

