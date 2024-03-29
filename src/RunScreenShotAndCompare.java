import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class RunScreenShotAndCompare {

    public static void main(String args[]){
        defineScreenShot("https://www.tutorialspoint.com/how-to-compare-two-images-using-java-opencv-library", "BBCScreenShot");
        compareScreenShots("BBCScreenShot", "BBCScreenShot");
        destoryWindow();
        defineScreenShot("https://www.tutorialspoint.com/how-to-compare-two-images-using-java-opencv-library", "RandomWebsite");
        destoryWindow();
    }

    public static void findMousePosition(){
        int counter = 0;
        while(true){
            counter++;
            try {
                Thread.sleep(4000);
                int mouseY = MouseInfo.getPointerInfo().getLocation().y;
                int mouseX = MouseInfo.getPointerInfo().getLocation().x;
                System.out.println("MouseY: " + mouseY + " MouseX: " + mouseX);
                counter = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void clickOnWebPage(int x, int y) throws AWTException{
        Robot bot = new Robot();
        bot.mouseMove(x, y);
        bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    private static void defineScreenShot(String url, String fileName){
            navigateToWebPage(url, fileName);
    }

    private static void captureScreenShot(String fileName){
        try {
            BufferedImage screencapture = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            File outputfile = new File("output/" +fileName+".png");
            ImageIO.write(screencapture, "png", outputfile);
        } catch (IOException | AWTException e) {
            e.printStackTrace();
        }
    }

    private static Process p;

    private static void navigateToWebPage(String url, String fileName){
        try {
            p = Runtime.getRuntime().exec("C:/Program Files (x86)/Google/Chrome/Application/chrome.exe -incognito "+ url);
            Thread.sleep(4000);
            clickOnWebPage(579,89);
            Thread.sleep(4000);
            captureScreenShot(fileName);

        } catch (IOException | InterruptedException | AWTException e) {
            e.printStackTrace();
        }
    }

    private static void destoryWindow(){
        p.destroy();
    }

    private static void compareScreenShots(String orgionalFile, String screenShotFile){
        BufferedImage img1 = null;
        BufferedImage img2 = null;
        try {
            img1 = ImageIO.read(new File("original/"+orgionalFile+".png"));
            img2 = ImageIO.read(new File("output/"+screenShotFile+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int w1 = img1.getWidth();
        int w2 = img2.getWidth();
        int h1 = img1.getHeight();
        int h2 = img2.getHeight();
        if ((w1!=w2)||(h1!=h2)) {
            System.out.println("Both images should have same dimwnsions");
        } else {
            long diff = 0;
            for (int j = 0; j < h1; j++) {
                for (int i = 0; i < w1; i++) {
                    //Getting the RGB values of a pixel
                    int pixel1 = img1.getRGB(i, j);
                    Color color1 = new Color(pixel1, true);
                    int r1 = color1.getRed();
                    int g1 = color1.getGreen();
                    int b1 = color1.getBlue();
                    int pixel2 = img2.getRGB(i, j);
                    Color color2 = new Color(pixel2, true);
                    int r2 = color2.getRed();
                    int g2 = color2.getGreen();
                    int b2= color2.getBlue();
                    //sum of differences of RGB values of the two images
                    long data = Math.abs(r1-r2)+Math.abs(g1-g2)+ Math.abs(b1-b2);
                    diff = diff+data;
                }
            }
            double avg = diff/(w1*h1*3);
            double percentage = (avg/255)*100;
            System.out.println("Difference: "+percentage);
        }
    }

}
