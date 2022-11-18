import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PicturePanel extends JPanel {
    Histogram histogram;
    BufferedImage image;
    int r = 0;
    int g = 0;
    int b = 0;

    //
    int lowestR = 255;
    int lowestG = 255;
    int lowestB = 255;
    int highestR = 0;
    int highestG = 0;
    int highestB = 0;
    int[] red = new int[256];
    int[] blue = new int[256];
    int[] green = new int[256];
    //

    int[][][] pixels;

    public PicturePanel() throws IOException {
        setPreferredSize(new Dimension(500, 500));
        setSize(new Dimension(500, 500));
        image = ImageIO.read((getClass().getResource("/apple_noise.png")));
        JLabel label = new JLabel("", new ImageIcon(image), 0);
        pixels = new int[image.getHeight()][image.getWidth()][3];
    }

    public void displayImage(Graphics g) {
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    @Override
    public void paintComponent(Graphics g) {
        this.displayImage(g);
    }

    public void loadPicture() {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int color = image.getRGB(i, j);
                Color c = new Color(color);
                b = color & 0xff;
                g = (color & 0xff00) >> 8;
                r = (color & 0xff0000) >> 16;

                pixels[i][j][0] = r;
                pixels[i][j][1] = g;
                pixels[i][j][2] = b;
                image.setRGB(i, j, c.getRGB());
            }
        }
    }

    public void widenHistogram() throws IOException {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int color = image.getRGB(i, j);
                Color c = new Color(color);
                b = color & 0xff;
                g = (color & 0xff00) >> 8;
                r = (color & 0xff0000) >> 16;
                if (r < lowestR) lowestR = r;
                if (g < lowestG) lowestG = g;
                if (b < lowestB) lowestB = b;
                if (r > highestR) highestR = r;
                if (g > highestG) highestG = g;
                if (b > highestB) highestB = b;

            }
        }
        widen();

    }

    public void widen() {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int color = image.getRGB(i, j);
                b = color & 0xff;
                g = (color & 0xff00) >> 8;
                r = (color & 0xff0000) >> 16;

                image.setRGB(i, j, math().getRGB());
            }
        }
        Graphics graphics = getGraphics();
        graphics.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    public void equalizeHistogram() {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int color = image.getRGB(i, j);
                b = color & 0xff;
                g = (color & 0xff00) >> 8;
                r = (color & 0xff0000) >> 16;
                if (r < lowestR) lowestR = r;
                if (g < lowestG) lowestG = g;
                if (b < lowestB) lowestB = b;
                if (r > highestR) highestR = r;
                if (g > highestG) highestG = g;
                if (b > highestB) highestB = b;
                red[r]++;
                green[g]++;
                blue[b]++;
            }
        }
        cumulativeDistribuition();
        equalize();
    }

    public void equalize() {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int color = image.getRGB(i, j);
                b = color & 0xff;
                g = (color & 0xff00) >> 8;
                r = (color & 0xff0000) >> 16;
                image.setRGB(i, j, moreMath().getRGB());
            }
        }
        Graphics graphics = getGraphics();
        graphics.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    public void binaryByInput(int r, int g, int b) {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int color = image.getRGB(i, j);
                this.b = color & 0xff;
                this.g = (color & 0xff00) >> 8;
                this.r = (color & 0xff0000) >> 16;
                if ((color & 0xff) <= b || ((color & 0xff00) >> 8) <= g || ((color & 0xff0000) >> 16) <= r)
                    image.setRGB(i, j, Color.BLACK.getRGB());
                else image.setRGB(i, j, Color.WHITE.getRGB());
            }
        }
        Graphics graphics = getGraphics();
        graphics.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    public void percentageBlackSelection(int percentage) {
        red = new int[256];
        green = new int[256];
        blue = new int[256];
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int color = image.getRGB(i, j);
                this.b = color & 0xff;
                this.g = (color & 0xff00) >> 8;
                this.r = (color & 0xff0000) >> 16;
                red[r]++;
                green[g]++;
                blue[b]++;
            }
        }

        int rgb[] = cumulativeDistribuitionWithASpin(percentage);

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int color = image.getRGB(i, j);
                this.b = color & 0xff;
                this.g = (color & 0xff00) >> 8;
                this.r = (color & 0xff0000) >> 16;
                if ((color & 0xff) <= rgb[2] || ((color & 0xff00) >> 8) <= rgb[1] || ((color & 0xff0000) >> 16) <= rgb[0])
                    image.setRGB(i, j, Color.BLACK.getRGB());
                else image.setRGB(i, j, Color.WHITE.getRGB());
            }
        }

        Graphics graphics = getGraphics();
        graphics.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    public void cumulativeDistribuition() {
        int currentRed = 0;
        int currentGreen = 0;
        int currentBlue = 0;
        for (int i = 0; i <= 255; i++) {
            currentRed += red[i];
            red[i] = currentRed;
            currentGreen += green[i];
            green[i] = currentGreen;
            currentBlue += blue[i];
            blue[i] = currentBlue;
        }
    }

    public int[] cumulativeDistribuitionWithASpin(int percentage) {
        int currentRed = 0;
        int currentGreen = 0;
        int currentBlue = 0;
        int rValue = -1;
        int gValue = -1;
        int bValue = -1;
        for (int i = 0; i <= 255; i++) {
            if (currentRed < percentage * image.getHeight() * image.getHeight() / 100) {
                currentRed += red[i];
                red[i] = currentRed;
            } else if (rValue == -1) rValue += i + 1;

            if (currentGreen < percentage * image.getHeight() * image.getHeight() / 100) {
                currentGreen += green[i];
                green[i] = currentGreen;
            } else if (gValue == -1) gValue += i + 1;

            if (currentBlue < percentage * image.getHeight() * image.getHeight() / 100) {
                currentBlue += blue[i];
                blue[i] = currentBlue;
            } else if (bValue == -1) bValue += i + 1;
        }
        return new int[]{rValue, gValue, bValue};
    }

    public void meanIterativeSelection() {
        float avg = 0;
        int[][] pixels = new int[image.getWidth()][image.getHeight()];
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int color = image.getRGB(i, j);
                this.b = color & 0xff;
                this.g = (color & 0xff00) >> 8;
                this.r = (color & 0xff0000) >> 16;
                avg += (this.r + this.g + this.b) / 3;
            }
        }
        avg = avg / (image.getWidth() * image.getHeight());

        avg = this.selection((int) avg);

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int color = image.getRGB(i, j);
                this.b = color & 0xff;
                this.g = (color & 0xff00) >> 8;
                this.r = (color & 0xff0000) >> 16;
                if ((this.r + this.g + this.b) / 3 > avg)
                    image.setRGB(i, j, Color.WHITE.getRGB());
                else image.setRGB(i, j, Color.BLACK.getRGB());
            }
        }
    }

    public int selection(int startingAvg) {
        float avg = startingAvg;
        int lowAvg = 0;
        int lowCounter = 0;
        int highAvg = 1;
        int highCounter = 0;

        while (lowAvg != highAvg) {
            lowAvg = 0;
            lowCounter = 0;
            highAvg = 0;
            highCounter = 0;

            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    int color = image.getRGB(i, j);
                    this.b = color & 0xff;
                    this.g = (color & 0xff00) >> 8;
                    this.r = (color & 0xff0000) >> 16;
                    if ((this.r + this.g + this.b) / 3 > avg) {
                        lowAvg += (this.r + this.g + this.b) / 3;
                        lowCounter++;
                    } else {
                        highAvg += (this.r + this.g + this.b) / 3;
                        highCounter++;
                    }
                }
            }
            lowAvg = lowAvg / lowCounter;
            highAvg = highAvg / highCounter;
            if (avg == (lowAvg + highAvg) / 2) return (int) avg;
            avg = (lowAvg + highAvg) / 2;
        }

        return (int) avg;
    }

    public void initHistogramData() {
        histogram = new Histogram(image);
        histogram.createData();
    }

    public Color math() {
        float red, green, blue;
        red = ((float) (r - lowestR) / (highestR - lowestR)) * 255;
        green = ((float) (g - lowestG) / (highestG - lowestG)) * 255;
        blue = ((float) (b - lowestB) / (highestB - lowestB)) * 255;
        return this.validateColor(red, green, blue);
    }

    public Color moreMath() {
        float red, green, blue;
        red = (((float) this.red[r] / ((float) image.getWidth() * image.getHeight())) * 255);
        green = (((float) this.red[g] / ((float) image.getWidth() * image.getHeight())) * 255);
        blue = (((float) this.red[b] / ((float) image.getWidth() * image.getHeight())) * 255);
        return this.validateColor(red, green, blue);
    }

    public void reset() throws IOException {
        image = ImageIO.read((getClass().getResource("problem.jpg")));
        paintComponent(getGraphics());
    }

    public Color validateColor(float red, float green, float blue) {
        if (red >= 255) {
            red = 255;
        } else if (red < 0) {
            red = 0;
        }
        if (green >= 255) {
            green = 255;
        } else if (green < 0) {
            green = 0;
        }
        if (blue >= 255) {
            blue = 255;
        } else if (blue < 0) {
            blue = 0;
        }
        return new Color((int) red, (int) green, (int) blue);
    }
}