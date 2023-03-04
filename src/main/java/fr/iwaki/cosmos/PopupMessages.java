package fr.iwaki.cosmos;

import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

import static fr.theshark34.swinger.Swinger.getResourceIgnorePath;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class PopupMessages extends JPanel implements SwingerEventListener {

    private final JFrame frame = new JFrame();
    private JPanel panel = new JPanel();

    private final Image background = getResourceIgnorePath("/images/popupMessages/background.png");

    private final int ERROR_MESSAGE = 0;
    private final int NORMAL_MESSAGE = 1;
    private final int DONE_MESSAGE = 2;
    private final int YES_NO_QUESTION = 3;

    private final STexturedButton okButton = new STexturedButton(Swinger.getResourceIgnorePath("/images/popupMessages/okButton-normal.png"), Swinger.getResourceIgnorePath("/images/popupMessages/okButton-hover.png"));
    private final STexturedButton yesButton = new STexturedButton(Swinger.getResourceIgnorePath("/images/popupMessages/yesButton-normal.png"), Swinger.getResourceIgnorePath("/images/popupMessages/yesButton-hover.png"));
    private final STexturedButton noButton = new STexturedButton(Swinger.getResourceIgnorePath("/images/popupMessages/noButton-normal.png"), Swinger.getResourceIgnorePath("/images/popupMessages/noButton-hover.png"));

    private Thread ifYesThread = new Thread();
    private Thread ifNoThread = new Thread();
    private Thread whenOk = new Thread();

    public PopupMessages() {
        // Constructeur
    }

    private void initFrame(String title, String msg, int messageType) {
        Thread t = new Thread(() -> {
            BufferedImage icon;
            if (messageType == ERROR_MESSAGE) {
                icon = Swinger.getResourceIgnorePath("/images/popupMessages/errorIcon.png");
            } else if (messageType == DONE_MESSAGE) {
                icon = Swinger.getResourceIgnorePath("/images/popupMessages/doneIcon.png");
            } else if (messageType == YES_NO_QUESTION) {
                icon = Swinger.getResourceIgnorePath("/images/popupMessages/inWorkIcon.png");
            } else {
                icon = Swinger.getResourceIgnorePath("/images/popupMessages/newIcon.png");
            }

            frame.setTitle(title);
            frame.setSize(350, 225);
            frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setIconImage(icon);
            frame.setResizable(false);
            frame.setContentPane(panel = new PopupMessages(msg, messageType));
            frame.setVisible(true);
        });
        t.start();
    }

    private PopupMessages(String message, int messageType) {
        this.setLayout(null);

        Icon icon;
        if (messageType == ERROR_MESSAGE) {
            icon = new ImageIcon(Swinger.getResourceIgnorePath("/images/popupMessages/errorIcon.png"));
        } else if (messageType == DONE_MESSAGE) {
            icon = new ImageIcon(Swinger.getResourceIgnorePath("/images/popupMessages/doneIcon.png"));
        } else if (messageType == YES_NO_QUESTION) {
            icon = new ImageIcon(Swinger.getResourceIgnorePath("/images/popupMessages/inWorkIcon.png"));
        } else {
            icon = new ImageIcon(Swinger.getResourceIgnorePath("/images/popupMessages/newIcon.png"));
        }

        JLabel image = new JLabel();
        image.setBounds(30, 40, 64, 64);
        image.setIcon(icon);
        this.add(image);

        String message1;
        String message2;
        String message3;
        try {
            message1 = message.substring(0, 21);
            try {
                message2 = message.substring(21, 45);
                try {
                    message3 = message.substring(45);
                } catch (Exception e) {
                    message3 = "";
                }
            } catch (Exception e) {
                message2 = message.substring(21);
                message3 = "";
            }
        } catch (Exception e) {
            message1 = message;
            message2 = "";
            message3 = "";
        }


        JTextArea messageArea = new JTextArea();
        if (Objects.equals(message3, "")) {
            if (Objects.equals(message2, "")){
                messageArea.setBounds(125, 65, 180, 60);
            } else {
                messageArea.setBounds(125, 57, 180, 60);
            }
        } else {
            messageArea.setBounds(125, 48, 180, 60);
        }
        messageArea.setForeground(Color.WHITE);
        messageArea.setFont(new CustomFonts().kollektifFont.deriveFont(16f));
        messageArea.setCaretColor(Color.RED);
        messageArea.setOpaque(false);
        messageArea.setBorder(null);
        messageArea.setEditable(false);
        messageArea.setAlignmentX(SwingConstants.LEFT);
        messageArea.setAlignmentY(SwingConstants.CENTER);
        messageArea.setText(message1 + System.lineSeparator() + message2 + System.lineSeparator() + message3);
        this.add(messageArea);

        if (Objects.equals(messageType, YES_NO_QUESTION)) {
            yesButton.setBounds(112, 131);
            yesButton.addEventListener(this);
            this.add(yesButton);

            noButton.setBounds(185, 131);
            noButton.addEventListener(this);
            this.add(noButton);
        } else {
            okButton.setBounds(148, 131);
            okButton.addEventListener(this);
            this.add(okButton);
        }
    }

    public void normalMessage(String title, String message) {
        initFrame(title, message, NORMAL_MESSAGE);
    }

    public void normalMessage(String title, String message, Thread whenOkClicked) {
        initFrame(title, message, NORMAL_MESSAGE);
        whenOk = whenOkClicked;
    }

    public void errorMessage(String title, String message) {
        initFrame(title, message, ERROR_MESSAGE);
    }

    public void errorMessage(String title, String message, Thread whenOkClicked) {
        initFrame(title, message, ERROR_MESSAGE);
        whenOk = whenOkClicked;
    }

    public void doneMessage(String title, String message) {
        initFrame(title, message, DONE_MESSAGE);
    }

    public void yesNoMessage(String title, String message, Thread ifYes, Thread ifNo) {
        initFrame(title, message, YES_NO_QUESTION);

        ifYesThread = ifYes;
        ifNoThread = ifNo;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    @Override
    public void onEvent(SwingerEvent e) {
        if (e.getSource() == okButton) {
            if (whenOk != null) {
                whenOk.start();
                whenOk = null;
            }
            frame.dispose();
        } else if (e.getSource() == yesButton) {
            ifYesThread.start();
            ifYesThread = null;
            frame.dispose();
        } else if (e.getSource() == noButton) {
            ifNoThread.start();
            ifNoThread = null;
            frame.dispose();
        }
    }
}
