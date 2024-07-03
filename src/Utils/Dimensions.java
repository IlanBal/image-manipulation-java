package Utils;

import java.awt.*;

public class Dimensions {

    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int WINDOW_WIDTH;
    public static final int WINDOW_HEIGHT;

    public Dimensions() {

    }

    static {
        WINDOW_WIDTH = (int) (screenSize.getWidth() / 2);
        WINDOW_HEIGHT = (int) (screenSize.getHeight() / 2);

    }
}
