package org.proIII.appManejoImagenes.command;

import org.proIII.appManejoImagenes.image.ImagePixels;
import org.proIII.appManejoImagenes.operations.DrawLine;

public class DrawLineCommand implements CommandExcecute{
    private  DrawLine drawLine;
    private ImagePixels image;
    private int [][]oldImage;
    private int x0, y0, x1, y1, color;
    public DrawLineCommand(DrawLine drawLine, ImagePixels image, int x0, int y0, int x1, int y1, int color ){
        this.drawLine = drawLine;
        this.image = image;
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.color = color;
        this.oldImage =  copyImage(image.getImagePixels());
    }
    @Override
    public void execute() {
        drawLine.DrawLine(image, x0, y0, x1, y1, color);

    }

    @Override
    public void undo() {
        image.pixelsChange(oldImage);
    }


    private int[][] copyImage(int[][] image) {
        int[][] copia = new int[image.length][];
        for (int i = 0; i < image.length; i++) {
            copia[i] = image[i].clone();
        }
        return copia;
    }


}
