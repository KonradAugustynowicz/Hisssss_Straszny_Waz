import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MenuBar  extends JMenuBar implements ActionListener {
    RgbInputPanel rgbInputPanel = new RgbInputPanel();
    Frame frame;
    JMenu filterMenu;
    JMenu menu;
    PicturePanel pp;

    JMenuItem i1, i2, i3, i4, i5, i6;

    public MenuBar(PicturePanel pp,Frame frame) {
        this.frame = frame;
        this.pp = pp;

        filterMenu = new JMenu("Filtr");

        i1 = new JMenuItem("Rozszerzenie histogramu");
        i2 = new JMenuItem("Wyrównanie histogramu");
        i3 = new JMenuItem("Binaryzacja ręczna");
        i4 = new JMenuItem("Procentowa selekcja czarnego");
        i5 = new JMenuItem("Selekcja iteratywna średniej");
        i6 = new JMenuItem("Selekcja iteratywna średniej");

        i1.addActionListener(this);
        i2.addActionListener(this);
        i3.addActionListener(this);
        i4.addActionListener(this);
        i5.addActionListener(this);
        i6.addActionListener(this);

        filterMenu.add(i1);
        filterMenu.add(i2);
        filterMenu.add(i3);
        filterMenu.add(i4);
        filterMenu.add(i5);
        filterMenu.add(i6);


        this.add(filterMenu);
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

    }
}
