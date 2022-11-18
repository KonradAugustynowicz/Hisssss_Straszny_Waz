import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;


public class RgbInputPanel extends JPanel {
    NumberFormat format = NumberFormat.getInstance();
    NumberFormatter formatter = new NumberFormatter(format);

    JFormattedTextField r;
    JFormattedTextField g;
    JFormattedTextField b;

    public RgbInputPanel() {
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(255);
        formatter.setAllowsInvalid(false);

        r = new JFormattedTextField(formatter);
        r.setColumns(3);
        r.setValue(0);
        g = new JFormattedTextField(formatter);
        g.setColumns(3);
        g.setValue(0);
        b = new JFormattedTextField(formatter);
        b.setColumns(3);
        b.setValue(0);

        this.add(new JLabel("Czerwony:"));
        this.add(r);
        this.add(Box.createHorizontalStrut(15)); // a spacer
        this.add(new JLabel("Zielony:"));
        this.add(g);
        this.add(Box.createHorizontalStrut(15)); // a spacer
        this.add(new JLabel("Niebieski:"));
        this.add(b);
    }
}
