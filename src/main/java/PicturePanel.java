import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.Float.NaN;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class PicturePanel extends JPanel {
    Histogram histogram;
    BufferedImage image;
    int r = 0;
    int g = 0;
    int b = 0;
    protected int RADIX = 256;
    int threshold;
    static int[] gr_histogram = null;
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

    public URL getImgPath() {
        return imgPath;
    }

    public void setImgPath(URL imgPath) {
        this.imgPath = imgPath;
    }

    //
    URL imgPath = getClass().getResource("/OtsuTest.png");
    int[][][] pixels;

    public PicturePanel() throws IOException {
        setPreferredSize(new Dimension(500, 500));
        setSize(new Dimension(500, 500));
        image = ImageIO.read((imgPath));
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

                pixels[j][i][0] = r;
                pixels[j][i][1] = g;
                pixels[j][i][2] = b;
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

    public void entropySelection(){
        makeGray(image);
        int total = 0;
        for (int value : gr_histogram) {
            total += value;
        }
        double sum = 0.0;
        for (int k : gr_histogram) {
            if (k > 0) {
                double p = k * 1.0 / total;
                sum += p * Math.log(p);
            }
        }
        int entropy = (int) Math.ceil(-total * sum / Math.log(2));
        entropy = entropy & 0x00ff0000 >> 16;
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int pixel = image.getRGB(i, j) & 0x00ff0000 >> 16;
                if (pixel > entropy)
                    image.setRGB(i, j, Color.WHITE.getRGB());
                else image.setRGB(i, j, Color.BLACK.getRGB());
            }
        }
        paintComponent(getGraphics());
    }

    public void fuzzyMinimumError(){
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

    public void dilatation() {
        loadPicture();
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int color = image.getRGB(i, j);
                this.b = color & 0xff;
                this.g = (color & 0xff00) >> 8;
                this.r = (color & 0xff0000) >> 16;
                image.setRGB(i, j, dilate(j, i).getRGB());
            }
        }
    }

    public Color dilate(int x, int y) {
        for (int i = -1; i < 1; i++) {
            for (int j = -1; j < 1; j++) {
                try {
                    if (pixels[x + i][y + j][0] == 0 &&
                            pixels[x + i][y + j][1] == 0 &&
                            pixels[x + i][y + j][2] == 0) {
                        return Color.BLACK;
                    }
                } catch (IndexOutOfBoundsException e) {
                    continue;
                }
            }
        }
        return Color.WHITE;
    }

    public void erosion() {
        loadPicture();
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int color = image.getRGB(i, j);
                this.b = color & 0xff;
                this.g = (color & 0xff00) >> 8;
                this.r = (color & 0xff0000) >> 16;
                image.setRGB(i, j, erode(j, i).getRGB());
            }
        }
    }

    public Color erode(int x, int y) {
        for (int i = -1; i < 1; i++) {
            for (int j = -1; j < 1; j++) {
                try {
                    if (pixels[x + i][y + j][0] == 255 &&
                            pixels[x + i][y + j][1] == 255 &&
                            pixels[x + i][y + j][2] == 255) {
                        return Color.WHITE;
                    }
                } catch (IndexOutOfBoundsException e) {
                    continue;
                }
            }
        }
        return Color.BLACK;
    }


    public int selection(int startingAvg) {
        float avg = startingAvg;
        int lowAvg = 0;
        int lowCounter = 0;
        int highAvg = 1;
        int highCounter = 0;

        while (lowCounter != highCounter) {
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
        image = ImageIO.read(imgPath);
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

    public static void makeGray(BufferedImage img){
        gr_histogram = new int[256];
        for (int x = 0; x < img.getWidth(); ++x)
            for (int y = 0; y < img.getHeight(); ++y){
                int rgb = img.getRGB(x, y);
                int b = rgb & 0xff;
                int g = (rgb & 0xff00) >> 8;
                int r = (rgb & 0xff0000) >> 16;

                // Normalize and gamma correct:
                double rr = Math.pow(r / 255.0, 2.2);
                double gg = Math.pow(g / 255.0, 2.2);
                double bb = Math.pow(b / 255.0, 2.2);

                // Calculate luminance:
                double lum = 0.2126 * rr + 0.7152 * gg + 0.0722 * bb;

                // Gamma compand and rescale to byte range:
                int grayLevel = (int) (255.0 * Math.pow(lum, 1.0 / 2.2));
                int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                gr_histogram[grayLevel]++;
                img.setRGB(x, y, gray);
            }
    }

    private void withTreshold(BufferedImage img){
        Color white = new Color(255, 255, 255);
        Color black = new Color(0, 0, 0);
        for (int x = 0; x < img.getWidth(); ++x)
            for (int y = 0; y < img.getHeight(); ++y){
                int rgb = img.getRGB(x, y);
                int r = (rgb & 0xff0000) >> 16;
                int grayLevel = r;
                img.setRGB(x, y, grayLevel > threshold ? white.getRGB() : black.getRGB());
            }
        System.out.println("Threshold: " + threshold);
    }
    public void otsu(){
        makeGray(image);
        initHistogramData();
        threshold();
        withTreshold(image);
        Graphics graphics = getGraphics();
        graphics.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }
    public void niblack(){
        niblack(15,-0.2);
    }
    public void sauvola(){
        sauvola(15,0.5);
    }

    public void phansalkar(){
        phansalkar(15,3,10,0.25,0.5);
    }

    public void bernsen(){
        bernsen(15, 15, "bright");
    }

    public void bernsen(int cmin, int N, String background) {
        int dx = image.getWidth();
        int dy = image.getHeight();
        makeGray(image);
        int K = background == "bright" ? 0 : 255;
        BufferedImage img = deepCopy(image);
        int radius = (N - 1) / 2;
        for (int i = 0; i < dx; i++) {
            for (int j = 0; j < dy; j++) {

                int low = Integer.MAX_VALUE;
                int high = Integer.MIN_VALUE;
                for (int ji = -radius; ji < radius; ji++) {
                    for (int jj = -radius; jj < radius; jj++) {
                        // Zabezpieczenie aby nie wyjsc poza obraz i uzyskiwanie sredniej
                        if (i + ji >= 0 && i + ji < dx)
                            if (j + jj >= 0 && j + jj < dy) {
                                int value = (image.getRGB(i + ji, j + jj) & 0x00ff0000) >> 16;
                                if (value < low) {
                                    low = value;
                                }
                                if (value > high) {
                                    high = value;
                                }
                            }
                    }
                }
                int bt = (int) ((low + high) / 2.0);
                // Local contrast measure
                int cl = high - low;
                int t = cl > cmin ? bt : K;
                int pixel = image.getRGB(i, j) & 0x00ff0000 >> 16;
                if (pixel < t)
                    img.setRGB(i, j, 0x00000000);
                else
                    img.setRGB(i, j, 0x00FFFFFF);
            }
        }
        image = img;
    }

    public void phansalkar(int N, int p, int q, double k, double R){
        int dx = image.getWidth();
        int dy = image.getHeight();
        makeGray(image);
        // Calculate the radius of the neighbourhood
        BufferedImage img = deepCopy(image);

        int radius = (N-1)/2;
        R = R * 256;
        for (int i = 0; i < dx; i++) {
            for (int j = 0; j < dy; j++) {

                double acc = 0;
                ArrayList elements = new ArrayList();
                for(int ji = -radius ; ji < radius ; ji++){
                    for(int jj = -radius ; jj < radius ; jj++){
                        // Zabezpieczenie aby nie wyjsc poza obraz i uzyskiwanie sredniej
                        if(i+ji >= 0 && i+ji < dx)
                            if(j+jj >= 0 && j+jj < dy){
                                elements.add((image.getRGB(i+ji, j+jj) & 0x00ff0000) >> 16);
                            }
                    }
                }
                double[] calcs = calculateSD(elements);
                acc = calcs[0];
                double sd = calcs[1];
                double ph = p * Math.exp(-q * acc);
                int t = (int) Math.ceil((acc * (1 + ph + k * ((sd/R) - 1))));

                int pixel = image.getRGB(i, j) & 0x00ff0000 >> 16;
                if(pixel < t )
                    img.setRGB(i, j, 0x00000000 );
                else
                    img.setRGB(i, j, 0x00FFFFFF );
            }
        }
        image = img;
    }
    public void niblack(int N, double K){
        int dx = image.getWidth();
        int dy = image.getHeight();
        makeGray(image);
        BufferedImage img = deepCopy(image);
        // Calculate the radius of the neighbourhood
        int radius = (N-1)/2;

        for (int i = 0; i < dx; i++) {
            for (int j = 0; j < dy; j++) {

                double acc = 0;
                ArrayList elements = new ArrayList();
                for(int ji = -radius ; ji < radius ; ji++){
                    for(int jj = -radius ; jj < radius ; jj++){
                        // Zabezpieczenie aby nie wyjsc poza obraz i uzyskiwanie sredniej
                        if(i+ji >= 0 && i+ji < dx)
                            if(j+jj >= 0 && j+jj < dy){
                                elements.add((image.getRGB(i+ji, j+jj) & 0x00ff0000) >> 16);
                            }
                    }
                }
                double[] calcs = calculateSD(elements);
                acc = calcs[0];
                double sd = calcs[1];
                double t =  Math.ceil((acc + (  K * sd )));

                int pixel = image.getRGB(i, j) & 0x00ff0000 >> 16;
                if(pixel < t )
                    img.setRGB(i, j, 0x00000000 );
                else
                    img.setRGB(i, j, 0x00FFFFFF );
            }
        }
        image = img;
    }

    public void sauvola(int N, double K){
        int R = 128;
        int dx = image.getWidth();
        int dy = image.getHeight();
        makeGray(image);
        BufferedImage img = deepCopy(image);
        // Calculate the radius of the neighbourhood
        int radius = (N-1)/2;

        for (int i = 0; i < dx; i++) {
            for (int j = 0; j < dy; j++) {

                double acc = 0;
                ArrayList elements = new ArrayList();
                for(int ji = -radius ; ji < radius ; ji++){
                    for(int jj = -radius ; jj < radius ; jj++){
                        // Zabezpieczenie aby nie wyjsc poza obraz i uzyskiwanie sredniej
                        if(i+ji >= 0 && i+ji < dx)
                            if(j+jj >= 0 && j+jj < dy){
                                elements.add((image.getRGB(i+ji, j+jj) & 0x00ff0000) >> 16);
                            }
                    }
                }
                double[] calcs = calculateSD(elements);
                acc = calcs[0];
                double sd = calcs[1];
                int t = (int) Math.ceil(acc * (1 + K * ((sd/R) - 1)));

                int pixel = image.getRGB(i, j) & 0x00ff0000 >> 16;
                if(pixel < t )
                    img.setRGB(i, j, 0x00000000 );
                else
                    img.setRGB(i, j, 0x00FFFFFF );
            }
        }

        image = img;
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static double[] calculateSD(ArrayList<Integer> numArray)
    {
        double[] calculations = new double[2];
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.size();

        for(double num : numArray) {
            sum += num;
        }

        double mean = sum/length;
        calculations[0] = mean;
        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        calculations[1] = Math.sqrt(standardDeviation/length);
        return calculations;
    }
    private int sumIntensities(int[] gr_histogram) {
        int sum = 0;
        for (int i = 0; i < gr_histogram.length; i++)
            sum += i * gr_histogram[i];
        return sum;
    }

    protected void threshold() {
        // get sum of all pixel intensities
        int sum = sumIntensities(gr_histogram);

        // perform Otsu's method
        calcThreshold(gr_histogram, pixels.length * pixels[0].length, sum);
    }
    private void calcThreshold(int[] gr_histogram, int N, int sum) {
        double variance;                                // objective function to maximize
        double bestVariance = Double.NEGATIVE_INFINITY;

        double mean_bg = 0;
        double weight_bg = 0;

        double mean_fg = (double) sum / (double) N;     // mean of population
        double weight_fg = N;                           // weight of population

        double diff_means;

        // loop through all candidate thresholds
        int t = 0;
        while (t < RADIX) {
            // calculate variance
            diff_means = mean_fg - mean_bg;
            variance = weight_bg * weight_fg * diff_means * diff_means;

            // store best threshold
            if (variance > bestVariance) {
                bestVariance = variance;
                threshold = t;
            }

            // go to next candidate threshold
            while (t < RADIX && gr_histogram[t] == 0)
                t++;

            mean_bg = (mean_bg * weight_bg + gr_histogram[t] * t) / (weight_bg + gr_histogram[t]);
            mean_fg = (mean_fg * weight_fg - gr_histogram[t] * t) / (weight_fg - gr_histogram[t]);
            weight_bg += gr_histogram[t];
            weight_fg -= gr_histogram[t];
            t++;
        }
    }
}