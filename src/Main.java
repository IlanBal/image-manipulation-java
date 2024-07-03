
import static Utils.Dimensions.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.*;

public class Main extends JPanel implements ActionListener, MouseListener {
    private final JLabel imageLabel = new JLabel(" ");
    private final JPanel buttonPanel = new JPanel();
    private final JPanel imagePanel = new JPanel();
    private String imagePath;
    private int clickCount = 0;

    private Point[] clickedPoints = new Point[4];
    private BufferedImage selectedImage;
    private BufferedImage bufferedImage;

    public Main() {
        JFrame frame = new JFrame("Image Viewer");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        createPanel(frame);
        frame.setVisible(true);

    }

    private void createPanel(JFrame frame) {
        this.setLayout(new BorderLayout());
        imagePanel.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JButton imagePickerButton = new JButton("Pick Image");
        String[] manipulations = {"B&W", "Grayscale", "Posterize", "Tint","Color Shift Right","Color Shift Left", "Mirror", "Pixelate", "Show Borders", "Eliminate R/G/B", "Negative", "Contrast", "Sepia", "Lighter", "Darker", "Vignette", "Add Noise", "Solarize", "Reset"};
        JButton manipulationButton[] = new JButton[manipulations.length];
        
        imagePickerButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
            fileChooser.setFileFilter(imageFilter);

            int rVal = fileChooser.showOpenDialog(this);
            if(rVal == JFileChooser.APPROVE_OPTION) {
                imagePath = fileChooser.getSelectedFile().getAbsolutePath();

                imagePickerButton.setVisible(false);
                for(int i=0; i<manipulationButton.length; i++) {
                    manipulationButton[i] = new JButton(manipulations[i]);
                    manipulationButton[i].addActionListener(this);  
                    buttonPanel.add(manipulationButton[i]);
                }

                ImageIcon imageIcon = new ImageIcon(imagePath);
                Image scaledImage = imageIcon.getImage().getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
                Image image = ((ImageIcon) imageLabel.getIcon()).getImage();
                bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

                Graphics2D g2d = bufferedImage.createGraphics();
                g2d.drawImage(image, 0, 0, null);
                g2d.dispose();
            }
        });

        imageLabel.addMouseListener(this);

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0,0,65,0));
        buttonPanel.add(imagePickerButton);

        imagePanel.add(imageLabel, BorderLayout.CENTER);

        this.add(buttonPanel, BorderLayout.SOUTH);
        this.add(imagePanel, BorderLayout.CENTER);

        frame.add(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(imageLabel.getIcon() != null) {
            if(clickCount < 4) {
                int x = e.getX();
                int y = e.getY();
                clickedPoints[clickCount] = new Point(x, y);
                clickCount++;

                drawClickIndication(imageLabel, x, y);

                if(clickCount == 4) {
                    drawRectangle(imageLabel);
                    saveSelectedArea(imageLabel);
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case "B&W" -> {
                if(selectedImage != null) {
                    BufferedImage bwImage = ConvertToBlackAndWhite(selectedImage);
                    selectedImage.createGraphics().drawImage(bwImage , selectedImage.getMinX(), selectedImage.getMinY(), null);
                    ImageIcon updatedImage = new ImageIcon(bufferedImage);
                    imageLabel.setIcon(updatedImage);
                } else {
                    BufferedImage bwImage = ConvertToBlackAndWhite(bufferedImage);
                    Image scaledImage = bwImage.getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                }

            }
            case "Grayscale" -> {
                if(selectedImage != null) {
                    BufferedImage grayscaleImage = ConvertToGrayscale(selectedImage);
                    selectedImage.getGraphics().drawImage(grayscaleImage , selectedImage.getMinX(), selectedImage.getMinY(), null);
                    ImageIcon updatedImage = new ImageIcon(bufferedImage);
                    imageLabel.setIcon(updatedImage);
                } else {
                    BufferedImage grayscaleImage = ConvertToGrayscale(bufferedImage);
                    Image scaledImage = grayscaleImage.getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                }
            }
            case "Posterize" -> {
                if(selectedImage != null) {
                    BufferedImage posterizedImage = PosterizeImage(selectedImage);
                    selectedImage.getGraphics().drawImage(posterizedImage , selectedImage.getMinX(), selectedImage.getMinY(), null);
                    ImageIcon updatedImage = new ImageIcon(bufferedImage);
                    imageLabel.setIcon(updatedImage);
                } else {
                    BufferedImage posterizedImage = PosterizeImage(bufferedImage);
                    Image scaledImage = posterizedImage.getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                }
            }
            case "Tint" -> {
                if(selectedImage != null) {
                    BufferedImage tintedImage = TintImage(selectedImage);
                    selectedImage.getGraphics().drawImage(tintedImage , selectedImage.getMinX(), selectedImage.getMinY(), null);
                    ImageIcon updatedImage = new ImageIcon(bufferedImage);
                    imageLabel.setIcon(updatedImage);
                } else {
                    BufferedImage tintedImage = TintImage(bufferedImage);
                    Image scaledImage = tintedImage.getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                }
            }
            case "Color Shift Right" -> {
                if(selectedImage != null) {
                    BufferedImage shiftedImage = ColorShiftImage(selectedImage, "right");
                    selectedImage.getGraphics().drawImage(shiftedImage , selectedImage.getMinX(), selectedImage.getMinY(), null);
                    ImageIcon updatedImage = new ImageIcon(bufferedImage);
                    imageLabel.setIcon(updatedImage);
                } else {
                    BufferedImage shiftedImage = ColorShiftImage(bufferedImage, "right");
                    Image scaledImage = shiftedImage.getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                }
            }
            case "Color Shift Left" -> {
                if(selectedImage != null) {
                    BufferedImage shiftedImage = ColorShiftImage(selectedImage, "left");
                    selectedImage.getGraphics().drawImage(shiftedImage , selectedImage.getMinX(), selectedImage.getMinY(), null);
                    ImageIcon updatedImage = new ImageIcon(bufferedImage);
                    imageLabel.setIcon(updatedImage);
                }else {
                    BufferedImage shiftedImage = ColorShiftImage(bufferedImage, "left");
                    Image scaledImage = shiftedImage.getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                }
            }
            case "Mirror" -> {
                if(selectedImage != null) {
                    BufferedImage mirroredImage = MirrorImage(selectedImage);
                    selectedImage.getGraphics().drawImage(mirroredImage , selectedImage.getMinX(), selectedImage.getMinY(), null);
                    ImageIcon updatedImage = new ImageIcon(bufferedImage);
                    imageLabel.setIcon(updatedImage);
                } else {
                    BufferedImage mirroredImage = MirrorImage(bufferedImage);
                    Image scaledImage = mirroredImage.getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                }
            }
            case "Pixelate" -> {
                if(selectedImage != null) {
                    BufferedImage pixelatedImage = PixelateImage(selectedImage, 5);
                    selectedImage.getGraphics().drawImage(pixelatedImage , selectedImage.getMinX(), selectedImage.getMinY(), null);
                    ImageIcon updatedImage = new ImageIcon(bufferedImage);
                    imageLabel.setIcon(updatedImage);
                } else {
                    BufferedImage pixelatedImage = PixelateImage(bufferedImage, 5);
                    Image scaledImage = pixelatedImage.getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                }
            }
            case "Show Borders" -> {
                if(selectedImage != null) {
                    BufferedImage borderedImage = showBorders(selectedImage, 3);
                    selectedImage.getGraphics().drawImage(borderedImage , selectedImage.getMinX(), selectedImage.getMinY(), null);
                    ImageIcon updatedImage = new ImageIcon(bufferedImage);
                    imageLabel.setIcon(updatedImage);
                } else {
                    BufferedImage borderedImage = showBorders(bufferedImage, 3);
                    Image scaledImage = borderedImage.getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                } 
            }
            case "Eliminate R/G/B" -> {
                if(selectedImage != null) {
                    BufferedImage eliminatedImage = EliminateRGB(selectedImage, "blue");
                    selectedImage.getGraphics().drawImage(eliminatedImage , selectedImage.getMinX(), selectedImage.getMinY(), null);
                    ImageIcon updatedImage = new ImageIcon(bufferedImage);
                    imageLabel.setIcon(updatedImage);
                } else {
                    BufferedImage eliminatedImage = EliminateRGB(bufferedImage, "blue");
                    Image scaledImage = eliminatedImage.getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                }
            }
            case "Negative" -> {
                if(selectedImage != null) {
                    BufferedImage negatedImage = NegateImage(selectedImage);
                    selectedImage.getGraphics().drawImage(negatedImage , selectedImage.getMinX(), selectedImage.getMinY(), null);
                    ImageIcon updatedImage = new ImageIcon(bufferedImage);
                    imageLabel.setIcon(updatedImage);
                } else {
                    BufferedImage negatedImage = NegateImage(bufferedImage);
                    Image scaledImage = negatedImage.getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                }
            }
            case "Contrast" -> {
                if(selectedImage != null) {
                    BufferedImage contrastedImage = ContrastImage(selectedImage);
                    selectedImage.getGraphics().drawImage(contrastedImage , selectedImage.getMinX(), selectedImage.getMinY(), null);
                    ImageIcon updatedImage = new ImageIcon(bufferedImage);
                    imageLabel.setIcon(updatedImage);
                } else {
                    BufferedImage contrastedImage = ContrastImage(bufferedImage);
                    Image scaledImage = contrastedImage.getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                }
            }
            case "Sepia" -> {
                if(selectedImage != null) {
                    BufferedImage sepiaImage = SepiaImage(selectedImage);
                    selectedImage.getGraphics().drawImage(sepiaImage , selectedImage.getMinX(), selectedImage.getMinY(), null);
                    ImageIcon updatedImage = new ImageIcon(bufferedImage);
                    imageLabel.setIcon(updatedImage);
                } else {
                    BufferedImage sepiaImage = SepiaImage(bufferedImage);
                    Image scaledImage = sepiaImage.getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                }
            }
            case "Lighter" -> {
                if(selectedImage != null) {
                    BufferedImage lighterImage = LightenImage(selectedImage);
                    selectedImage.getGraphics().drawImage(lighterImage , selectedImage.getMinX(), selectedImage.getMinY(), null);
                    ImageIcon updatedImage = new ImageIcon(bufferedImage);
                    imageLabel.setIcon(updatedImage);
                } else {
                    BufferedImage lighterImage = LightenImage(bufferedImage);
                    Image scaledImage = lighterImage.getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                }
            }
            case "Darker" -> {
                if(selectedImage != null) {
                    BufferedImage darkerImage = DarkenImage(selectedImage);
                    selectedImage.getGraphics().drawImage(darkerImage , selectedImage.getMinX(), selectedImage.getMinY(), null);
                    ImageIcon updatedImage = new ImageIcon(bufferedImage);
                    imageLabel.setIcon(updatedImage);
                } else {
                    BufferedImage darkerImage = DarkenImage(bufferedImage);
                    Image scaledImage = darkerImage.getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                }
            }
            case "Vignette" -> {
                if(selectedImage != null) {
                    BufferedImage vignetteImage = VignetteImage(selectedImage);
                    selectedImage.getGraphics().drawImage(vignetteImage , selectedImage.getMinX(), selectedImage.getMinY(), null);
                    ImageIcon updatedImage = new ImageIcon(bufferedImage);
                    imageLabel.setIcon(updatedImage);
                } else {
                    BufferedImage vignetteImage = VignetteImage(bufferedImage);
                    Image scaledImage = vignetteImage.getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                }
            }
            case "Add Noise" -> {
                if(selectedImage != null) {
                    BufferedImage noiseImage = AddNoise(selectedImage);
                    selectedImage.getGraphics().drawImage(noiseImage , selectedImage.getMinX(), selectedImage.getMinY(), null);
                    ImageIcon updatedImage = new ImageIcon(bufferedImage);
                    imageLabel.setIcon(updatedImage);
                } else {
                    BufferedImage noiseImage = AddNoise(bufferedImage);
                    Image scaledImage = noiseImage.getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                }
            }
            case "Solarize" -> {
                if(selectedImage != null) {
                    BufferedImage solarizedImage = SolarizeImage(selectedImage);
                    selectedImage.getGraphics().drawImage(solarizedImage, selectedImage.getMinX(), selectedImage.getMinY(), null);
                    ImageIcon updatedImage = new ImageIcon(bufferedImage);
                    imageLabel.setIcon(updatedImage);
                } else {
                    BufferedImage solarizedImage = SolarizeImage(bufferedImage);
                    Image scaledImage = solarizedImage.getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                }
            }
            case "Reset" -> {
                clickedPoints = new Point[4];
                selectedImage = null;
                clickCount = 0;
                ImageIcon imageIcon = new ImageIcon(imagePath);
                Image scaledImage = imageIcon.getImage().getScaledInstance(imagePanel.getWidth(), imagePanel.getHeight(), Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));

                Image image = ((ImageIcon) imageLabel.getIcon()).getImage();
                bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

                Graphics2D g2d = bufferedImage.createGraphics();
                g2d.drawImage(image, 0, 0, null);
                g2d.dispose();
            }
        }
    }

    private BufferedImage ConvertToBlackAndWhite(BufferedImage image) {    
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage BWImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = BWImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return BWImage;
    }
    private BufferedImage ConvertToGrayscale(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage grayscaleImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = grayscaleImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return grayscaleImage;

    }
    private BufferedImage PosterizeImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage posterizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int y=0; y<height; y++) {
            for(int x=0; x<width; x++) {
                int pixel = image.getRGB(x, y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;
                int newPixel;

                int grayAmount = (int) (red + green + blue)/3;
                if(grayAmount < 43) {
                    red = 25;
                    green = 25;
                    blue = 112;
                } else if(grayAmount < 85) {
                    red = 72;
                    green = 61;
                    blue = 139;
                } else if(grayAmount < 128) {
                    red = 65;
                    green = 105;
                    blue = 225;
                } else if(grayAmount < 171) {
                    red = 0;
                    green = 191;
                    blue = 255;
                } else if(grayAmount < 213) {
                    red = 175;
                    green = 238;
                    blue = 238;
                } else {
                    red = 224;
                    green = 255;
                    blue = 255;
                }
                newPixel = (red << 16) | (green << 8) | blue;
                posterizedImage.setRGB(x, y, newPixel);
            }
        }
        return posterizedImage;
    }
    private BufferedImage TintImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage tintedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Color tint = new Color(0,255,0);// Green tint
        double factor = 0.1; // Adjust the factor to adjust the tint intensity
        for(int y = 0; y<height; y++) {
            for(int x = 0; x<width; x++) {
                int pixel = image.getRGB(x, y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;
                
                int tintedRed = (int) (factor * tint.getRed() + (1-factor) * red);
                int tintedGreen = (int) (factor * tint.getGreen() + (1-factor) * green);
                int tintedBlue = (int) (factor * tint.getBlue() + (1-factor) * blue);

                int newPixel = (tintedRed << 16) | (tintedGreen << 8) | tintedBlue;
                tintedImage.setRGB(x, y, newPixel);
            }
        }
        return tintedImage;
    }
    private BufferedImage ColorShiftImage(BufferedImage image, String shift) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage shiftedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int y = 0; y<height; y++) {
            for(int x = 0; x<width; x++) {
                int pixel = image.getRGB(x, y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                int newRed, newGreen, newBlue, newPixel = 0;

                if("right".equals(shift)) {
                    newRed = blue;
                    newGreen = red;
                    newBlue = green;
                    newPixel = (newRed << 16) | (newGreen << 8) | newBlue;
                } else if("left".equals(shift)) {
                    newRed = green;
                    newGreen = blue;
                    newBlue = red;
                    newPixel = (newRed << 16) | (newGreen << 8) | newBlue;
                } 
                shiftedImage.setRGB(x, y, newPixel);
            }
        }
        return shiftedImage;
    }
    private BufferedImage MirrorImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage mirroredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int y=0; y < height; y++) {
            for(int x=0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                mirroredImage.setRGB(width - x - 1, y, pixel);
            }
        }
        return mirroredImage;
    }
    private BufferedImage PixelateImage(BufferedImage image, int n) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage pixelatedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    
        for (int y = 0; y < height; y += n) {
            for (int x = 0; x < width; x += n) {
                int pixel = image.getRGB(x, y); // Get the top-left pixel of the block
    
                // Set the same pixel value for all pixels in the block
                for (int i = y; i < Math.min(y + n, height); i++) {
                    for (int j = x; j < Math.min(x + n, width); j++) {
                        pixelatedImage.setRGB(j, i, pixel);
                    }
                }
            }
        }
    
        return pixelatedImage;
    }
    private BufferedImage showBorders(BufferedImage image, int n) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage borderedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                if( y <= n || y >= height-n || x<= n || x >= width-n){
                    borderedImage.setRGB(x, y, 0x000000);
                } else 
                    borderedImage.setRGB(x, y, pixel);
            }
        }
        return borderedImage;
    }
    private BufferedImage EliminateRGB(BufferedImage image, String rgb) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage eliminatedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        switch(rgb) {
            case("red") -> {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int pixel = image.getRGB(x, y);
                        int green = (pixel >> 8) & 0xff;
                        int blue = pixel & 0xff;

                        int newPixel = (0xff000000) | (green << 8) | blue;
                        eliminatedImage.setRGB(x, y, newPixel);
                    }
                }
                return eliminatedImage;
            }
            case("green") -> {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int pixel = image.getRGB(x, y);
                        int red = (pixel >> 16) & 0xff;
                        int blue = pixel & 0xff;
                        int newPixel = (red << 16) | (0xff000000) | blue;
                        eliminatedImage.setRGB(x, y, newPixel);
                    }
                }
                return eliminatedImage;
            }
            case("blue") -> {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int pixel = image.getRGB(x, y);
                        int red = (pixel >> 16) & 0xff;
                        int green = (pixel >> 8) & 0xff;
                        int newPixel = (red << 16) | (green << 8) | 0xff000000;
                        eliminatedImage.setRGB(x, y, newPixel);
                    }
                }
                return eliminatedImage;
            }
        }
        return eliminatedImage;
    }
    private BufferedImage NegateImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage negatedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int y=0; y<height; y++) {
            for(int x=0; x<width; x++) {
                int pixel = image.getRGB(x, y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                int newRed = 255 - red;
                int newGreen = 255 - green;
                int newBlue = 255 - blue;

                int newPixel = (newRed << 16) | (newGreen << 8) | newBlue;
                negatedImage.setRGB(x, y, newPixel);
            }
        }
        return negatedImage;
    }
    private BufferedImage ContrastImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage contrastImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        float contrastFactor = 1.5f;
        int middleValue = (255/2);
        for(int y=0; y<height; y++) {
            for(int x=0; x<width; x++) {
                int pixel = image.getRGB(x, y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                int redDiff = red - middleValue;
                int greenDiff = green - middleValue;
                int blueDiff = blue - middleValue;

                redDiff = (int) (redDiff * contrastFactor);
                greenDiff = (int) (greenDiff * contrastFactor);
                blueDiff = (int) (blueDiff * contrastFactor);

                red = Math.min(255, middleValue + redDiff);
                green = Math.min(255, middleValue + greenDiff);
                blue = Math.min(255, middleValue + blueDiff);

                red = Math.max(0, red);
                green = Math.max(0, green);
                blue = Math.max(0, blue);

                int newPixel = (red << 16) | (green << 8) | blue;
                contrastImage.setRGB(x, y, newPixel);
            }
        }
        return contrastImage;
    }
    private BufferedImage SepiaImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage sepiaImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int y=0; y<height; y++) {
            for(int x=0; x<width; x++) {
                int pixel = image.getRGB(x, y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                int newRed = (int) (0.393 * red + 0.769 * green + 0.189 * blue);
                int newGreen = (int) (0.349 * red + 0.686 * green + 0.168 * blue);
                int newBlue = (int) (0.272 * red + 0.534 * green + 0.131 * blue);

                if (newRed > 255) 
                    red = 255; 
                else
                    red = newRed; 
  
                if (newGreen > 255) 
                    green = 255; 
                else
                    green = newGreen; 
  
                if (newBlue > 255) 
                    blue = 255; 
                else
                    blue = newBlue; 

                int newPixel = (red << 16) | (green << 8) | blue;
                sepiaImage.setRGB(x, y, newPixel);
            }
        }
        return sepiaImage;
    }
    private BufferedImage LightenImage(BufferedImage image) {
        float lighteningFactor = 1.5f;
        RescaleOp op = new RescaleOp(lighteningFactor, 0, null);
        BufferedImage lightenedImage = op.filter(image, null);
        return lightenedImage;
    }
    private BufferedImage DarkenImage(BufferedImage image) {
        float darkeningFactor = 0.8f;
        RescaleOp op = new RescaleOp(darkeningFactor, 0, null);
        BufferedImage lightenedImage = op.filter(image, null);
        return lightenedImage;
    }
    private BufferedImage VignetteImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage vignetteImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int centerX = width / 2;
        int centerY = height / 2;

        float vignetteFactor = 0.8f;
        for(int y=0; y<height; y++) {
            for(int x=0; x<width; x++) {
                float distance = (float) Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
                float radius = (float) Math.max(width, height) / 2;

                float normalDistance = distance/radius;
                float darkening = 1.0f - (normalDistance * vignetteFactor);

                int pixel = image.getRGB(x, y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                red = Math.min(255, (int) (red * darkening));
                green = Math.min(255, (int) (green * darkening));
                blue = Math.min(255, (int) (blue * darkening));

                red = Math.max(0, red);
                green = Math.max(0, green);
                blue = Math.max(0, blue);

                int newPixel = (red << 16) | (green << 8) | blue;
                vignetteImage.setRGB(x, y, newPixel);
            }
        }
        return vignetteImage;
    }
    private BufferedImage AddNoise(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage noiseImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        float noise = 100.0f;
        for(int y=0; y<height; y++) {
            for(int x=0; x<width; x++) {
                int pixel = image.getRGB(x, y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                float noiseRed = (float) (Math.random() - 0.5) * noise;
                float noiseGreen = (float) (Math.random() - 0.5) * noise;
                float noiseBlue = (float) (Math.random() - 0.5) * noise;

                red = Math.min(255, Math.max(0, red + (int) noiseRed));
                green = Math.min(255, Math.max(0, green + (int) noiseGreen));
                blue = Math.min(255, Math.max(0, blue + (int) noiseBlue));

                int newPixel = (red << 16) | (green << 8) | blue;
                noiseImage.setRGB(x, y, newPixel);
            }
        }
        return noiseImage;
    }
    private BufferedImage SolarizeImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage solarizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int threshold = 128;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                // Invert for values above threshold
                if (red > threshold) {
                    red = 255 - red;
                }
                if (green > threshold) {
                    green = 255 - green;
                }
                if (blue > threshold) {
                    blue = 255 - blue;
                }

                int newPixel = (red << 16) | (green << 8) | blue;
                solarizedImage.setRGB(x, y, newPixel);
            }
        }
        return solarizedImage;
    }
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(Main::new);
    }

    private void drawClickIndication(JLabel imageLabel, int x, int y) {
        Graphics g = imageLabel.getGraphics();
        g.setColor(Color.YELLOW);
        g.fillOval(x-5, y-5, 5, 5);
        g.dispose();
    }

    private void drawRectangle(JLabel imageLabel) {
        Image image = ((ImageIcon) imageLabel.getIcon()).getImage();
        Graphics g = imageLabel.getGraphics();
        g.clearRect(imageLabel.getX(), imageLabel.getY(), imageLabel.getWidth(), imageLabel.getHeight());
        g.drawImage(image, 0,0, null);
        g.setColor(Color.BLACK);

          int minX = Math.min(clickedPoints[0].x, Math.min(clickedPoints[1].x, Math.min(clickedPoints[2].x, clickedPoints[3].x)));
          int minY = Math.min(clickedPoints[0].y, Math.min(clickedPoints[1].y, Math.min(clickedPoints[2].y, clickedPoints[3].y)));
          int maxX = Math.max(clickedPoints[0].x, Math.max(clickedPoints[1].x, Math.max(clickedPoints[2].x, clickedPoints[3].x)));
          int maxY = Math.max(clickedPoints[0].y, Math.max(clickedPoints[1].y, Math.max(clickedPoints[2].y, clickedPoints[3].y)));
  
          int width = maxX - minX;
          int height = maxY - minY;
  
          g.drawRect(minX, minY, width, height); // Draw the rectangle
          g.dispose();
    }

    private void saveSelectedArea(JLabel imageLabel) {
        Image image = ((ImageIcon) imageLabel.getIcon()).getImage();

        Graphics g = bufferedImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        int minX = Math.min(clickedPoints[0].x, Math.min(clickedPoints[1].x, Math.min(clickedPoints[2].x, clickedPoints[3].x)));
        int minY = Math.min(clickedPoints[0].y, Math.min(clickedPoints[1].y, Math.min(clickedPoints[2].y, clickedPoints[3].y)));
        int maxX = Math.max(clickedPoints[0].x, Math.max(clickedPoints[1].x, Math.max(clickedPoints[2].x, clickedPoints[3].x)));
        int maxY = Math.max(clickedPoints[0].y, Math.max(clickedPoints[1].y, Math.max(clickedPoints[2].y, clickedPoints[3].y)));

        int width = maxX - minX;
        int height = maxY - minY;

        width = Math.min(width, bufferedImage.getWidth() - minX);
        height = Math.min(height, bufferedImage.getHeight() - minY);

        selectedImage = bufferedImage.getSubimage(minX, minY, width, height);
    }
}

