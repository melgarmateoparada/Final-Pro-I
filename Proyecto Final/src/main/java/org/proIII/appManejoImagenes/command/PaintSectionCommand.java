package org.proIII.appManejoImagenes.command;

import org.proIII.appManejoImagenes.image.ImagePixels;
import org.proIII.appManejoImagenes.operations.PaintSection;

public class PaintSectionCommand implements CommandExcecute{
    private PaintSection paintSection;
    private int [][]oldImage;
    private ImagePixels image;
    private int x, y,color, marge;

public PaintSectionCommand(PaintSection paintSection, ImagePixels image, int x, int y, int color, int marge){
    this.paintSection = paintSection;
    this.image = image;
    this.x = x;
    this.y = y;
    this.color = color;
    this.marge = marge;
    this.oldImage =  copyImage(image.getImagePixels());

}
    @Override
    public void execute() {
        paintSection.floodFill(image.getImagePixels(), x, y,color, marge);
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
