package org.proIII.appManejoImagenes.command;

import org.proIII.appManejoImagenes.image.ImagePixels;
import org.proIII.appManejoImagenes.operations.DrawPoint;

public class DrawPointCommand implements CommandExcecute{
    private DrawPoint drawPoint;
    private ImagePixels image;
    private int x, y, color;
    private int [][]oldImage;
    public DrawPointCommand(DrawPoint drawPoint, ImagePixels image, int x, int y, int color){
        this.drawPoint = drawPoint;
        this.color =color;
        this.x = x;
        this.y = y;
        this.image = image;
        this.oldImage = copyImage(image.getImagePixels());
    }
    @Override
    public void execute() {
        drawPoint.drawPoint(image, x,y, color);
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
