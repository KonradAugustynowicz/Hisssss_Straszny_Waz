import com.formdev.flatlaf.FlatDarculaLaf;

import java.io.IOException;

public class Main {
    static Frame frame;

    public static void main(String[] args) throws IOException {
        FlatDarculaLaf.setup();
        frame = new Frame();
        frame.setVisible (true);
    }
}
