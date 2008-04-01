package hsm.gui;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.Vector;
import java.util.StringTokenizer;

/* 
 * IconDemoApplet.java requires the following files:
 *    images/right.gif
 *    images/left.gif
 *    images/dimmedRight.gif
 *    images/dimmedLeft.gif
 *
 *  An applet tag for using it is in
 *  tutorial/uiswing/misc/IconDemoApplet.html
 *  It refers to the following files:
 *    images/stickerface.gif
 *    images/lainesTongue.gif
 *    images/kathyCosmo.gif
 *    images/ewanPumpkin.gif
 *    images/malmau.gif
 */    
public class IconDemoApplet extends JApplet 
                            implements ActionListener {
    class Photo {
        public String filename;
        public String caption;
        public int height;
        public int width;
        public ImageIcon icon = null;

        public Photo(String filename, String caption, int height, int width) {
            this.filename = filename;
            this.caption = caption;
            this.height = height;
            this.width = width;
        }
    }


    Vector<Photo> photos = new Vector<Photo>();

    JButton previousButton;
    JButton nextButton;
    JLabel photographLabel;
    JLabel captionLabel;
    JLabel numberLabel;

    int current = 0;
    int widthOfWidest = 0;
    int heightOfTallest = 0;

    String imagedir = null;

    private void initializeGUI() {
        //Parse the applet parameters.
        parseParameters();

        //If the applet tag doesn't provide an "IMAGE0" parameter,
        //display an error message.
        if (photos.size() == 0) {
            captionLabel = new JLabel("No images listed in applet tag.");
            captionLabel.setHorizontalAlignment(JLabel.CENTER);
            getContentPane().add(captionLabel);
            return;
        }

        //NOW CREATE THE GUI COMPONENTS

        //A label to identify XX of XX.
        numberLabel = new JLabel("Picture " + (current+1) +
                                 " of " + photos.size());
        numberLabel.setHorizontalAlignment(JLabel.LEFT);
        numberLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));

        //A label for the caption.
        final Photo first = photos.firstElement();
        captionLabel = new JLabel(first.caption);
        captionLabel.setHorizontalAlignment(JLabel.CENTER);
        captionLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        //A label for displaying the photographs.
        photographLabel = new JLabel("Loading first image...");
        photographLabel.setHorizontalAlignment(JLabel.CENTER);
        photographLabel.setVerticalAlignment(JLabel.CENTER);
        photographLabel.setVerticalTextPosition(JLabel.CENTER);
        photographLabel.setHorizontalTextPosition(JLabel.CENTER);
        photographLabel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLoweredBevelBorder(),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        photographLabel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(0, 0, 10, 0),
                        photographLabel.getBorder()
        ));

        //Set the preferred size for the picture,
        //with room for the borders.
        Insets i = photographLabel.getInsets();
        photographLabel.setPreferredSize(new Dimension(
                            widthOfWidest+i.left+i.right,
                            heightOfTallest+i.bottom+i.top));

        //Create the next and previous buttons.
        ImageIcon nextIcon = createAppletImageIcon("images/right.gif",
                                                   "a right arrow");
        ImageIcon dimmedNextIcon = createAppletImageIcon("images/dimmedRight.gif",
                                                         "a dimmed right arrow");
        ImageIcon previousIcon = createAppletImageIcon("images/left.gif",
                                                       "a left arrow");
        ImageIcon dimmedPreviousIcon = createAppletImageIcon("images/dimmedLeft.gif",
                                                             "a dimmed left arrow");

        nextButton = new JButton("Next Picture", nextIcon);
        nextButton.setDisabledIcon(dimmedNextIcon);
        nextButton.setVerticalTextPosition(AbstractButton.CENTER);
        nextButton.setHorizontalTextPosition(AbstractButton.LEFT);
        nextButton.setMnemonic(KeyEvent.VK_N);
        nextButton.setActionCommand("next");
        nextButton.addActionListener(this);

        previousButton = new JButton("Previous Picture", previousIcon);
        previousButton.setDisabledIcon(dimmedPreviousIcon);
        previousButton.setVerticalTextPosition(AbstractButton.CENTER);
        previousButton.setHorizontalTextPosition(AbstractButton.RIGHT);
        previousButton.setMnemonic(KeyEvent.VK_P);
        previousButton.setActionCommand("previous");
        previousButton.addActionListener(this);
        previousButton.setEnabled(false);

        //Lay out the GUI.
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        Container contentPane = getContentPane();
        contentPane.setLayout(layout);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        layout.setConstraints(numberLabel, c);
        contentPane.add(numberLabel);

        layout.setConstraints(captionLabel, c);
        contentPane.add(captionLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        layout.setConstraints(photographLabel, c);
        contentPane.add(photographLabel);

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        layout.setConstraints(previousButton, c);
        contentPane.add(previousButton);

        c.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(nextButton, c);
        contentPane.add(nextButton);

        //Start loading the image for the first photograph now.
        //The loadImage method uses a SwingWorker
        //to load the image in a separate thread.
        loadImage(imagedir + first.filename, current);
    }
    public void init() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initializeGUI();
            }
        });
    }

    //User clicked either the next or the previous button.
    public void actionPerformed(ActionEvent e) {
        //Show loading message.
        photographLabel.setIcon(null);
        photographLabel.setText("Loading image...");

        //Compute index of photograph to view.
        if (e.getActionCommand().equals("next")) {
            current += 1;
            if (!previousButton.isEnabled())
                previousButton.setEnabled(true);
            if (current == photos.size() - 1)
                nextButton.setEnabled(false);
        } else {
            current -= 1;
            if (!nextButton.isEnabled())
                nextButton.setEnabled(true);
            if (current == 0)
                previousButton.setEnabled(false);
        }

        //Get the photo object.
        Photo photo = photos.elementAt(current);

        //Update the caption and number labels.
        captionLabel.setText(photo.caption);
        numberLabel.setText("Picture " + (current+1) +
                            " of " + photos.size());

        //Update the photograph.
        if (photo.icon == null) {     //haven't viewed this photo before
            loadImage(imagedir + photo.filename, current);
        } else {
            updatePhotograph(photo);
        }
    }

    //Must be invoked from the event-dispatching thread.
    private void updatePhotograph(Photo photo) {
        ImageIcon icon = photo.icon;

        photographLabel.setToolTipText(photo.filename + ": " +
                                       icon.getIconWidth() + " X " +
                                       icon.getIconHeight());
        photographLabel.setIcon(icon);
        photographLabel.setText("");
    }

    //Load an image in a worker thread.
    private void loadImage(final String imagePath, final int index) {
        (new SwingWorker<ImageIcon, Void>() {

            @Override
            public ImageIcon doInBackground() {
                return createAppletImageIcon(imagePath, 
                    "photo #" + index);
            }

            @Override
            public void done() { 
                Photo photo = photos.elementAt(index);
                try {
                    photo.icon = get();
                } catch (InterruptedException ignore) {}
                catch (java.util.concurrent.ExecutionException e) {
                    String why = null;
                    Throwable cause = e.getCause();
                    if (cause != null) {
                        why = cause.getMessage();
                    } else {
                        why = e.getMessage();
                    }
                    System.err.println("Error retrieving file: " + why);
                }

                if (index == current) {
                    updatePhotograph(photo);
                }
            }
        }).execute();
    }

    private void parseParameters() {
        String paramName;
        String paramValue;

        for (int i = 0; 
             (paramValue = getParameter(paramName = "IMAGE" + i)) != null;
             i++) {
            Photo photo = new Photo(paramValue, getCaption(i),
                                  getHeight(i), getWidth(i));
            photos.addElement(photo);
        }

        //Get the name of the directory that contains the image files.
        imagedir = getParameter("IMAGEDIR");
        if (imagedir != null) {
            imagedir = imagedir + "/";
        }
    }

    protected String getCaption(int i) {
        return getParameter("CAPTION"+i);
    }

    protected int getWidth(int i) {
        int width = 0;
        String widthString = getParameter("WIDTH"+i);
        if (widthString != null) {
            try {
                width = Integer.parseInt(widthString);
            } catch (NumberFormatException e) {
                width = 0;
            }
        } else {
            width = 0;
        }
        if (width > widthOfWidest)
            widthOfWidest = width;
        return width;
    }

    protected int getHeight(int i) {
        int height = 0;
        String heightString = getParameter("HEIGHT"+i);
        if (heightString != null) {
            try {
                height = Integer.parseInt(heightString);
            } catch (NumberFormatException e) {
                height = 0;
            }
        } else {
            height = 0;
        }
        if (height > heightOfTallest)
            heightOfTallest = height;
        return height;
    }

    public String[][] getParameterInfo() {
        String[][] info = {
            {"IMAGEDIR", "string", "directory containing image files" },
            {"IMAGEN", "string", "filename" },
            {"CAPTIONN", "string", "caption" },
            {"WIDTHN", "integer", "width of image" },
            {"HEIGHTN", "integer", "height of image" },
        };
        return info;
    }

    //Returns an ImageIcon, or null if the path was invalid. 
    //When running an applet using Java Plug-in, 
    //getResourceAsStream is more efficient than getResource.
    protected static ImageIcon createAppletImageIcon(String path,
                                              String description) {
        int MAX_IMAGE_SIZE = 75000; //Change this to the size of
                                    //your biggest image, in bytes.
        int count = 0;
        BufferedInputStream imgStream = new BufferedInputStream(
           IconDemoApplet.class.getResourceAsStream(path));
        if (imgStream != null) {
            byte buf[] = new byte[MAX_IMAGE_SIZE];
            try {
                count = imgStream.read(buf);
            } catch (IOException ieo) {
                System.err.println("Couldn't read stream from file: " + path);
            }

            try {
                imgStream.close();
            } catch (IOException ieo) {
                 System.err.println("Can't close file " + path);
            }

            if (count <= 0) {
                System.err.println("Empty file: " + path);
                return null;
            }
            return new ImageIcon(Toolkit.getDefaultToolkit().createImage(buf),
                                 description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
