import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MenuBar  extends JMenuBar implements ActionListener {
    RgbInputPanel rgbInputPanel = new RgbInputPanel();
    Frame frame;
    JMenu histogramMenu;
    JMenu morfologyMenu;
    JMenu binaryzationMenu;
    JMenu resetMenu;
    JMenu tresholdMenu;
    PicturePanel pp;

    JMenuItem i1, i2, i3, i4, i5, i6;
    JMenuItem i7;
    JMenuItem i11, i12, i13, i14,i15;
    JMenuItem b1, b2, b3, b4;

    public MenuBar(PicturePanel pp,Frame frame) {
        this.frame = frame;
        this.pp = pp;

        histogramMenu = new JMenu("Filtr");
        morfologyMenu = new JMenu("Morfologia");
        binaryzationMenu = new JMenu("Binaryzacja");

        i1 = new JMenuItem("Rozszerzenie histogramu");
        i2 = new JMenuItem("Wyrównanie histogramu");
        i3 = new JMenuItem("Binaryzacja ręczna");
        i4 = new JMenuItem("Procentowa selekcja czarnego");
        i5 = new JMenuItem("Selekcja iteratywna średniej");
        i6 = new JMenuItem("Selekcja iteratywna średniej");

        i11 = new JMenuItem("Dylatacja");
        i12 = new JMenuItem("Erozja");
        i13 = new JMenuItem("Otwarcie");
        i14 = new JMenuItem("Domknięcie");

        i15 = new JMenuItem("Reset");

        i1.addActionListener(this);
        i2.addActionListener(this);
        i3.addActionListener(this);
        i4.addActionListener(this);
        i5.addActionListener(this);
        i6.addActionListener(this);

        i11.addActionListener(this);
        i12.addActionListener(this);
        i13.addActionListener(this);
        i14.addActionListener(this);

        i15.addActionListener(this);

        histogramMenu.add(i1);
        histogramMenu.add(i2);

        binaryzationMenu.add(i3);
        binaryzationMenu.add(i4);
        binaryzationMenu.add(i5);
        binaryzationMenu.add(i6);

        morfologyMenu.add(i11);
        morfologyMenu.add(i12);
        morfologyMenu.add(i13);
        morfologyMenu.add(i14);

        tresholdMenu = new JMenu("Zaawansowanych algorytmy binaryzacji");
        b1 = new JMenuItem("Otsu");
        b2 = new JMenuItem("Niblack");
        b3 = new JMenuItem("Sauvola");
        b4 = new JMenuItem("Phansalkar");

        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);

        tresholdMenu.add(b1);
        tresholdMenu.add(b2);
        tresholdMenu.add(b3);
        tresholdMenu.add(b4);

        this.add(histogramMenu);
        this.add(binaryzationMenu);
        this.add(tresholdMenu);
        this.add(morfologyMenu);
        this.add(i15);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == i1) {
            try {
                pp.widenHistogram();
                pp.initHistogramData();
                frame.resetChartData();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (e.getSource() == i2) {
            try {
                pp.equalizeHistogram();
                pp.initHistogramData();
                frame.resetChartData();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (e.getSource() == i3) {
            try {
                JOptionPane.showConfirmDialog(null, rgbInputPanel,
                        "Podaj wartości kolorów", JOptionPane.OK_CANCEL_OPTION);
                pp.binaryByInput(
                            Integer.parseInt(rgbInputPanel.r.getText()),
                            Integer.parseInt(rgbInputPanel.g.getText()),
                            Integer.parseInt(rgbInputPanel.b.getText()));
                pp.initHistogramData();
                frame.resetChartData();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (e.getSource() == i4) {
            try {
                int percentage = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter your message"));
                pp.percentageBlackSelection(percentage);
                pp.initHistogramData();
                frame.resetChartData();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (e.getSource() == i5) {
            try {
                pp.meanIterativeSelection();
                pp.initHistogramData();
                frame.resetChartData();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (e.getSource() == i6) {
            try {
                pp.percentageBlackSelection(50);
                pp.initHistogramData();
                frame.resetChartData();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (e.getSource() == i11) {
            try {
                pp.dilatation();
                pp.initHistogramData();
                frame.resetChartData();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (e.getSource() == i12) {
            try {
                pp.erosion();
                pp.initHistogramData();
                frame.resetChartData();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (e.getSource() == i13) {
            try {
                pp.erosion();
                pp.dilatation();
                pp.initHistogramData();
                frame.resetChartData();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (e.getSource() == i14) {
            try {
                pp.dilatation();
                pp.erosion();
                pp.initHistogramData();
                frame.resetChartData();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        //reset
        else if (e.getSource() == i15) {
            try {
                pp.reset();
                pp.initHistogramData();
                frame.resetChartData();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (e.getSource() == b1){
            try {
                pp.otsu();
                frame.resetChartDataWithoutInitHistogram();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (e.getSource() == b2){
            try{
                pp.niblack();
                frame.resetChartDataWithoutInitHistogram();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (e.getSource() == b3){
            try{
                pp.sauvola();
                frame.resetChartDataWithoutInitHistogram();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        if(e.getSource() == b4){
            try{
                pp.phansalkar();
                frame.resetChartDataWithoutInitHistogram();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }
}
