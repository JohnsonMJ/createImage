/**
 * File name: CreateImage.java
 * Author: Matt Johnson
 * Date: 30/07/2017
 * Submission for SEQTA
 */

package createimage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class CreateImage {
    //Declares the width and height of the image.
    private static final int canvasWidth = 256;
    private static final int canvasHeight = 128;
    
    //Declares the total amount of unique colours.
    private static final int totalColours = 32768;
    
    public static void main(String[] args) {
        //Declares an array of integers and fills it with each unique colour.
        int[] pixelArray = generatePixelArray(); 
        
        //Declares an image object and paints it.
        BufferedImage img = paintCanvasSpiral(pixelArray);
        
        //Handles command line arguments
        boolean save = false;
        for (String s: args) {
            //If command line argument "s" or "S" is used,
            //saves the image locally
            if(s.equals("s") || s.equals("S")){
                save = true;
                saveImage(img);
            }
            //If the command line argument "d" or "D" is used,
            // checks the image for duplicate colours.
            if(s.equals("d") || s.equals("D")){
                if(checkDuplicates(img))
                    System.out.println("Image contains duplicate colours");
                else
                    System.out.println("Image contains no duplicate colours.");
            }
        }
        
        //Draws a window to display the image, if the image was not saved.
        if(!save)
            displayImage(img);
    }
    
    //Checks if an image repeats a colour.
    private static boolean checkDuplicates(BufferedImage img){
        int[] pixels = getPixels(img, canvasWidth, canvasHeight);
        int i, j;
        for(i = 0; i < pixels.length; i++){
            for(j = 0; j < pixels.length; j++){
                if(j != i && pixels[j] == pixels[i])
                    return(true);
            }
        }
        return(false);
    }
    
    //Creates a window with which to display the image.
    private static void displayImage(BufferedImage img){ 
        JFrame frame = new JFrame("Generated Image");
        
        //Makes the program exit after closing this window.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        
        frame.getContentPane().add(new JLabel(new ImageIcon(img)));
        
        //Resizes the window to fit the image.
        frame.pack();
        
        //Makes the window visable.
        frame.setVisible(true); 
}
    
    //Fills an array with all unique colours as integers.
    //Integers are formatted for BufferedImage.setRGB()
    private static int[] generatePixelArray(){ 
        int i, j, k; //iterators
        int r, b, g; //RGB colour values
        int total = 0; //current array index
        int[] pixelArray = new int[totalColours];
        
        //For each red value...
        for(i=1;i<=32;i++){
            r = ((i*8) -1);
            //For each blue value...
            for(j=1;j<=32;j++){ 
                b = ((j*8) -1);
                //For each green value...
                for(k=1;k<=32;k++){
                    g = ((k * 8) -1);
                    
                    //Convert the RGB values to an integer, then insert in array
                    pixelArray[total] =
                            (255<<24) |
                            (r<<16) |
                            (g<<8) |
                            b;
                    total++;
                }
            }
        }
        return(pixelArray); 
    }
    
    //Fills an array with the RBG values of a BufferedImage's pixels.
    private static int[] getPixels(BufferedImage img,int width, int height){
        int[] pixels = new int[totalColours];
        int i = 0;
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                pixels[i] = img.getRGB(x, y);
                i++;
            }
        }
        return(pixels);
    }
    
    //Paints the canvas' pixels in the order of the array.
    private static BufferedImage paintCanvasDefault(int[] pixelArray){
        BufferedImage img = new BufferedImage(
                canvasWidth,
                canvasHeight,
                BufferedImage.TYPE_INT_ARGB
        );
        int x, y;
        int p = 0;
        
        for(y = 0; y < canvasHeight; y++){ //For each row in the image...
            for(x = 0; x < canvasWidth; x++){ //Paint each pixel in the row.
                img.setRGB(x, y, pixelArray[p]);
                p++;
            }
        }
        return(img);
    }
    
    //Paints the canvas with a double spiral pattern.
    private static BufferedImage paintCanvasSpiral(int[] pixelArray){
        BufferedImage img = new BufferedImage(
                canvasWidth,
                canvasHeight,
                BufferedImage.TYPE_INT_ARGB
        );
        
        //If the canvas is the wrong shape, paints it with the default pattern.
        if(canvasWidth != (canvasHeight*2)){
            System.out.println("Canvas shape error, painting default pattern.");
            return(paintCanvasDefault(pixelArray));
        }
        
        int x, y, i, lastColumn;
        
        //Sets start point for painting the canvas to the former half.
        int p=((totalColours / 2)-1);
        
        //Creates a square spiralling pattern originating from the top left.
        for(x = 0; x < canvasHeight; x++){ //For each column on the canvas...
            
            //Paints the top pixel of the column.
            img.setRGB(x, 0, pixelArray[p]);
            p--;
            
            //Paints a line down to x, y.
            for(y = 1; y < x && canvasHeight > 1; y++){
                img.setRGB(x, y, pixelArray[p]);
                p--;
            }
            
            //Then paints a line back to 0, y.
            if(x > 0){
                y = x;
                for(i = x; i >=0;i--){
                    img.setRGB(i, y, pixelArray[p]);
                    p--;
                }
            }
        }
        
        //Records the final x coordinate for the left side of the canvas.
        lastColumn = x; 
        
        //Sets the array start point.
        p = (totalColours / 2);
        
        //Paints the lower right corner pixel.
        img.setRGB(canvasWidth-1, canvasHeight-1, pixelArray[p]);
        p++;
        
        //For each column...
        for(x = (canvasWidth-2); x >= lastColumn; x--){      
            //Paints the bottom pixel of the column.
            img.setRGB(x, canvasHeight-1, pixelArray[p]);
            p++;
            
            //Paints a line up
            for(
                    y = canvasHeight - 2;
                    (canvasHeight - y) < (canvasWidth - x);
                    y--
            ){
                img.setRGB(x, y, pixelArray[p]);
                p++;
            }
            
            //Paints a line right to the edge of the canvas.
            for(i = x; i < canvasWidth; i++){
                img.setRGB(i, y, pixelArray[p]);
                p++;
            }          
        }
        return(img);
    }
    
    //Saves a BufferedImage object as a .png locally.
    private static void saveImage(BufferedImage img){
        File f;
        try{
            //Creates the image in the same directory as the program.
            //This assumes the program has permissions for its own directory.
            f = new File("./outputImage.png");
            ImageIO.write(img, "png", f);
        }catch(IOException e){
            System.out.println("Error: " + e);
        }
    }
}
