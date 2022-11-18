import java.awt.*;
import java.awt.image.BufferedImage;

public class Histogram {
    double[] redData;
    double[] greenData;
    double[] blueData;

    int r = 0;
    int g = 0;
    int b = 0;

    BufferedImage image;

    public Histogram(BufferedImage image) {
        this.image = image;
        redData = new double[image.getHeight() * image.getWidth()];
        greenData = new double[image.getHeight() * image.getWidth()];
        blueData = new double[image.getHeight() * image.getWidth()];
    }

    public void createData() {
        int counter = 0;
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int color = image.getRGB(i, j);
                b = color & 0xff;
                g = (color & 0xff00) >> 8;
                r = (color & 0xff0000) >> 16;

                redData[counter] = r;
                greenData[counter] = g;
                blueData[counter] = b;

                counter++;
            }
        }
    }
}
