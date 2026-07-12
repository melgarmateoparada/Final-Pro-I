package org.proIII.appManejoImagenes.command;

import org.proIII.appManejoImagenes.image.ImagePixels;
import org.proIII.appManejoImagenes.operations.DrawSquare;

public class DrawSquareCommand implements CommandExcecute{
    private DrawSquare drawSquare;
    private ImagePixels image;
    private int x0, y0, x1, y1, color;
    private int [][]oldImage;


    public DrawSquareCommand(DrawSquare drawSquare, ImagePixels image, int x0, int y0, int x1, int y1, int color) {
        this.drawSquare = drawSquare;
        this.image = image;
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.color = color;
        this.oldImage = copyImage(image.getImagePixels());
    }
    @Override
    public void execute() {
        drawSquare.DrawSquare(image, x0, y0, x1, y1, color);
    }

    private int[][] copyImage(int[][] image) {
        int[][] copia = new int[image.length][];
        for (int i = 0; i < image.length; i++) {
            copia[i] = image[i].clone();
        }
        return copia;
    }

    @Override
    public void undo() {
        image.pixelsChange(oldImage);
    }

}
