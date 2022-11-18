import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Frame extends JFrame {
    ChartPanel chartPanel;
    PicturePanel pp;

    public Frame() throws HeadlessException, IOException {
        setLayout(new GridLayout(1,2,0,0));
        setSize(new Dimension(1600, 800));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        pp = new PicturePanel();//kekw
        MenuBar menuBar = new MenuBar(pp,this);
        this.setJMenuBar(menuBar);
        add(pp);
        pp.initHistogramData();

        resetChartData();
    }

    public void resetChartData() throws IOException {
        HistogramDataset dataset = new HistogramDataset();
        pp.widenHistogram();
        pp.initHistogramData();
        dataset.addSeries("key", pp.histogram.redData, 255);
        dataset.addSeries("key", pp.histogram.blueData, 255);
        dataset.addSeries("key", pp.histogram.greenData, 255);


        JFreeChart histogram = ChartFactory.createHistogram(
                "JFreeChart Histogram",
                "Data",
                "Frequency",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false);
        histogram.setBackgroundPaint(Color.white);

        if (this.chartPanel == null){
            this.chartPanel = new ChartPanel(histogram);
            this.chartPanel.setPreferredSize(new Dimension(500, 500));
            this.chartPanel.setSize(new Dimension(500, 500));
            add(this.chartPanel);
            revalidate();
            repaint();
        }else{
            remove(this.chartPanel);
            this.chartPanel = new ChartPanel(histogram);
            this.chartPanel.setPreferredSize(new Dimension(500, 500));
            this.chartPanel.setSize(new Dimension(500, 500));
            add(this.chartPanel);
            revalidate();
            repaint();
        }
    }
}
