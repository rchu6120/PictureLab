import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.text.*;
import java.util.*;
import java.util.List; // resolves problem with java.awt.List and java.util.List

/**
 * A class that represents a picture.  This class inherits from 
 * SimplePicture and allows the student to add functionality to
 * the Picture class.  
 * 
 * @author Barbara Ericson ericson@cc.gatech.edu
 */
public class Picture extends SimplePicture 
{
  ///////////////////// constructors //////////////////////////////////
  
  /**
   * Constructor that takes no arguments 
   */
  public Picture ()
  {
    /* not needed but use it to show students the implicit call to super()
     * child constructors always call a parent constructor 
     */
    super();  
  }
  
  /**
   * Constructor that takes a file name and creates the picture 
   * @param fileName the name of the file to create the picture from
   */
  public Picture(String fileName)
  {
    // let the parent class handle this fileName
    super(fileName);
  }
  
  /**
   * Constructor that takes the width and height
   * @param height the height of the desired picture
   * @param width the width of the desired picture
   */
  public Picture(int height, int width)
  {
    // let the parent class handle this width and height
    super(width,height);
  }
  
  /**
   * Constructor that takes a picture and creates a 
   * copy of that picture
   * @param copyPicture the picture to copy
   */
  public Picture(Picture copyPicture)
  {
    // let the parent class do the copy
    super(copyPicture);
  }
  
  /**
   * Constructor that takes a buffered image
   * @param image the buffered image to use
   */
  public Picture(BufferedImage image)
  {
    super(image);
  }
  
  ////////////////////// methods ///////////////////////////////////////
  
  /**
   * Method to return a string with information about this picture.
   * @return a string with information about the picture such as fileName,
   * height and width.
   */
  public String toString()
  {
    String output = "Picture, filename " + getFileName() + 
      " height " + getHeight() 
      + " width " + getWidth();
    return output;
    
  }
  
  /** Method to set the blue to 0 */
  public void zeroBlue()
  {
    Pixel[][] pixels = this.getPixels2D();
    for (Pixel[] rowArray : pixels)
    {
      for (Pixel pixelObj : rowArray)
      {
        pixelObj.setBlue(0);
      }
    }
  }

  public void keepOnlyBlue() {
    Pixel[][] pixels = this.getPixels2D();
    for (Pixel[] rowArray : pixels)
    {
      for (Pixel pixelObj : rowArray)
      {
        pixelObj.setRed(0);
        pixelObj.setGreen(0);
      }
    }
  }

  public void negate() {
    Pixel[][] pixels = this.getPixels2D();
    for (Pixel[] rowArray : pixels) {
      for (Pixel pixelObj : rowArray) {
        int value1 = pixelObj.getRed();
        int value2 = pixelObj.getGreen();
        int value3 = pixelObj.getBlue();
        pixelObj.setRed(255 - value1);
        pixelObj.setGreen(255 - value2);
        pixelObj.setBlue(255 - value3);
      }
    }
  }

  public void grayscale() {
    Pixel[][] pixels = this.getPixels2D();
    for (Pixel[] rowArray : pixels) {
      for (Pixel pixelObj : rowArray) {
        int average = (pixelObj.getRed() + pixelObj.getGreen() + pixelObj.getBlue()) / 3;
        pixelObj.setRed(average);
        pixelObj.setGreen(average);
        pixelObj.setBlue(average);
      }
    }
  }

  public void fixUnderwater() {
    Pixel[][] pixels = this.getPixels2D();
    int redAverage = 0;
    int greenAverage = 0;
    int blueAverage = 0;
    int totalPixels = 0;

    int maxRed = 0;
    int minRed = 255;
    int maxGreen = 0;
    int minGreen = 255;
    int maxBlue = 0;
    int minBlue = 255;

    // takes a sample from a fish and finds the average color value and range of colors
    for (int row = 26; row < 36; row++)
    {
      for (int col = 178; col < 198; col++)
      {
        totalPixels++;

        Pixel myPixel = pixels[row][col];

        redAverage += myPixel.getRed();
        greenAverage += myPixel.getGreen();
        blueAverage += myPixel.getBlue();

        if (myPixel.getRed() > maxRed) { maxRed = myPixel.getRed(); }
        if (myPixel.getRed() < minRed) { minRed = myPixel.getRed(); }
        if (myPixel.getGreen() > maxGreen) { maxGreen = myPixel.getGreen(); }
        if (myPixel.getGreen() < minGreen) { minGreen = myPixel.getGreen(); }
        if (myPixel.getBlue() > maxBlue) { maxBlue = myPixel.getBlue(); }
        if (myPixel.getGreen() < minBlue) { minBlue = myPixel.getBlue(); }

      }
    }

    redAverage = redAverage / totalPixels;
    greenAverage = greenAverage / totalPixels;
    blueAverage = blueAverage / totalPixels;

    Color averageColor = new Color(redAverage, greenAverage, blueAverage);

    // calculates the range
    int redRange = (maxRed - minRed);
    int greenRange = (maxGreen - minGreen);
    int blueRange = (maxBlue - minBlue);

    int redDistance = redRange;
    int greenDistance = greenRange;
    int blueDistance = blueRange;

    double maxDistance = Math.sqrt(redDistance * redDistance +
            greenDistance * greenDistance +
            blueDistance * blueDistance);

    double tolerance = 1.7; // higher tolerance means more pixels will be identified as "fish"

    // changes the image based on calculated distance from sample value
    for (int row = 0; row < pixels.length; row++) // Pixel[] rowArray : pixels)
    {
      for (int col = 0; col < pixels[0].length; col++) // Pixel pixelObj : rowArray)
      {
        Pixel myPixel = pixels[row][col]; //

        boolean closeEnough = myPixel.colorDistance(averageColor) < maxDistance * tolerance; // stopped here, define this***
        // System.out.println(myPixel.colorDistance(averageColor));
        if (closeEnough)
        {
          myPixel.setBlue(myPixel.getBlue() + 50);
        }
        else
        {
          myPixel.setBlue(myPixel.getBlue() - 50);
        }
      }
    }
  }

  /** Method that mirrors the picture around a 
    * vertical mirror in the center of the picture
    * from left to right */
  public void mirrorVertical()
  {
    Pixel[][] pixels = this.getPixels2D();
    Pixel leftPixel = null;
    Pixel rightPixel = null;
    int width = pixels[0].length;
    for (int row = 0; row < pixels.length; row++)
    {
      for (int col = 0; col < width / 2; col++)
      {
        leftPixel = pixels[row][col];
        rightPixel = pixels[row][width - 1 - col];
        rightPixel.setColor(leftPixel.getColor());
      }
    } 
  }
  
  /** Mirror just part of a picture of a temple */
  public void mirrorTemple()
  {
    int mirrorPoint = 276;
    Pixel leftPixel = null;
    Pixel rightPixel = null;
    int count = 0;
    Pixel[][] pixels = this.getPixels2D();
    
    // loop through the rows
    for (int row = 27; row < 97; row++)
    {
      // loop from 13 to just before the mirror point
      for (int col = 13; col < mirrorPoint; col++)
      {
        
        leftPixel = pixels[row][col];      
        rightPixel = pixels[row]
                [mirrorPoint - col + mirrorPoint];
        rightPixel.setColor(leftPixel.getColor());
        count++;
      }
    }
    System.out.println(count);
  }

  public void mirrorVerticalRightToLeft() {
    Pixel[][] pixels = this.getPixels2D();
    Pixel leftPixel = null;
    Pixel rightPixel = null;
    int width = pixels[0].length;
    for (int row = 0; row < pixels.length; row++)
    {
      for (int col = 0; col < width / 2; col++)
      {
        leftPixel = pixels[row][col];
        rightPixel = pixels[row][width - 1 - col];
        leftPixel.setColor(rightPixel.getColor());
      }
    }
  }

  public void mirrorHorizontal() {
    Pixel[][] pixels = this.getPixels2D();
    Pixel topPixel = null;
    Pixel bottomPixel = null;
    int width = pixels[0].length;
    for (int row = 0; row < pixels.length / 2; row++) {
      for (int col = 0; col < width; col++) {
        topPixel = pixels[row][col];
        bottomPixel = pixels[pixels.length - 1 - row][col];
        bottomPixel.setColor(topPixel.getColor());
      }
    }
  }

  public void mirrorHorizontalBotToTop() {
    Pixel [][] pixels = this.getPixels2D();
    Pixel topPixel = null;
    Pixel bottomPixel = null;
    int width = pixels[0].length;
    for (int row = 0; row < pixels.length / 2; row++) {
      for (int col = 0; col < width; col++) {
        topPixel = pixels[row][col];
        bottomPixel = pixels[pixels.length - 1 - row][col];
        topPixel.setColor(bottomPixel.getColor());
      }
    }
  }

  public void mirrorDiagonal() {
    Pixel[][] pixels = this.getPixels2D();
    Pixel topRightPixel = null;
    Pixel bottomLeftPixel = null;
    int maxLength;
    if (pixels.length < pixels[0].length) {
      maxLength = pixels.length; }
    else {
      maxLength = pixels[0].length;
    }
    for (int row = 0; row < maxLength; row++)
    {
      for (int col = row; col < maxLength; col++)
      {
        topRightPixel = pixels[row][col];
        bottomLeftPixel = pixels[col][row];
        bottomLeftPixel.setColor(topRightPixel.getColor());
      }
    }
  }

  public void mirrorArms() {
    int mirrorPoint = 193;
    Pixel topPixel = null;
    Pixel bottomPixel = null;
    Pixel[][] pixels = this.getPixels2D();

    // Left arm
    for (int row = 158; row < mirrorPoint; row++)
    {
      // loop from 13 to just before the mirror point
      for (int col = 103; col < 170; col++)
      {
        topPixel = pixels[row][col];
        bottomPixel = pixels[mirrorPoint - row + mirrorPoint][col];
        bottomPixel.setColor(topPixel.getColor());
      }
    }

    int mirrorPoint2 = 191;
    Pixel topPixel2 = null;
    Pixel bottomPixel2 = null;

    // Right arm
    for (int row = 171; row < mirrorPoint2; row++)
    {
      // loop from 13 to just before the mirror point
      for (int col = 239; col < 294; col++)
      {
        topPixel2 = pixels[row][col];
        bottomPixel2 = pixels[mirrorPoint2 - row + mirrorPoint2][col];
        bottomPixel2.setColor(topPixel2.getColor());
      }
    }
  }

  public void mirrorGull() {
    int mirrorPoint = 345;
    Pixel rightPixel = null;
    Pixel leftPixel = null;
    Pixel[][] pixels = this.getPixels2D();

    // Seagull
    for (int row = 235; row < 323; row++)
    {
      for (int col = 238; col < mirrorPoint; col++)
      {
        rightPixel = pixels[row][col];
        leftPixel = pixels[row][mirrorPoint - col + mirrorPoint/3];
        leftPixel.setColor(rightPixel.getColor());
      }
    }
  }

  /** copy from the passed fromPic to the
    * specified startRow and startCol in the
    * current picture
    * @param fromPic the picture to copy from
    * @param startRow the start row to copy to
    * @param startCol the start col to copy to
    */
  public void copy(Picture fromPic, 
                 int startRow, int startCol)
  {
    Pixel fromPixel = null;
    Pixel toPixel = null;
    Pixel[][] toPixels = this.getPixels2D();
    Pixel[][] fromPixels = fromPic.getPixels2D();
    for (int fromRow = 0, toRow = startRow; fromRow < fromPixels.length && toRow < toPixels.length; fromRow++, toRow++)
    {
      for (int fromCol = 0, toCol = startCol; fromCol < fromPixels[0].length && toCol < toPixels[0].length; fromCol++, toCol++)
      {
        fromPixel = fromPixels[fromRow][fromCol];
        toPixel = toPixels[toRow][toCol];
        toPixel.setColor(fromPixel.getColor());
      }
    }   
  }

  public void copy2(Picture fromPic, int startRow, int endRow, int startCol, int endCol) {
    Pixel fromPixel = null;
    Pixel toPixel = null;
    Pixel[][] toPixels = this.getPixels2D();
    Pixel[][] fromPixels = fromPic.getPixels2D();
    for (int fromRow = 0, toRow = startRow; fromRow < endRow && toRow < endRow; fromRow++, toRow++)
    {
      for (int fromCol = 0, toCol = startCol; fromCol < endCol && toCol < endCol; fromCol++, toCol++)
      {
        fromPixel = fromPixels[fromRow][fromCol];
        toPixel = toPixels[toRow][toCol];
        toPixel.setColor(fromPixel.getColor());
      }
    }
  }

  /** Method to create a collage of several pictures */
  public void createCollage()
  {
    Picture wall = new Picture("wall.jpg");
    Picture wallSmaller = wall.scale(0.25,0.25);
    Picture motorcycle = new Picture("blueMotorcycle.jpg");
    Picture motorcycleSmaller = motorcycle.scale(0.25,0.25);
    Picture barbara = new Picture("barbaraS.jpg");
    Picture wallGrayscale = new Picture(wall);
    wallGrayscale.grayscale();
    motorcycleSmaller.zeroBlue();
    barbara.mirrorVertical();
    this.copy(wallSmaller,58,104);
    this.copy(motorcycleSmaller,130,369);
    this.copy(barbara,300,200);
    this.write("collage.jpg");
  }
  
  /** Method to show large changes in color 
    * @param edgeDist the distance for finding edges
    */
  public void edgeDetection(int edgeDist)
  {
    Pixel leftPixel = null;
    Pixel rightPixel = null;
    Pixel topPixel = null;
    Pixel bottomPixel = null;
    Color rightColor = null;
    Color bottomColor = null;
    Pixel[][] pixels = this.getPixels2D();
    for (int row = 0; row < pixels.length; row++)
    {
      for (int col = 0; 
           col < pixels[0].length-1; col++)
      {
        leftPixel = pixels[row][col];
        rightPixel = pixels[row][col+1];
        rightColor = rightPixel.getColor();
        if (leftPixel.colorDistance(rightColor) > edgeDist)
          leftPixel.setColor(Color.BLACK);
        else
          leftPixel.setColor(Color.WHITE);
      }
    }
    for (int col = 0; col < pixels[0].length; col++) {
      for (int row = 0; row < pixels.length - 1; row++) {
        topPixel = pixels[row][col];
        bottomPixel = pixels[row+1][col];
        bottomColor = bottomPixel.getColor();
        if (topPixel.colorDistance(bottomColor) > edgeDist)
          topPixel.setColor(Color.BLACK);
        else
          topPixel.setColor(Color.WHITE);
      }
    }
  }
  
  
  /* Main method for testing - each class in Java can have a main 
   * method 
   */
  public static void main(String[] args) 
  {
    Picture beach = new Picture("beach.jpg");
    beach.explore();
    beach.zeroBlue();
    beach.explore();
  }
  
} // this } is the end of class Picture, put all new methods before this
