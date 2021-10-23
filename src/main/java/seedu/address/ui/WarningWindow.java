package seedu.address.ui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.*;

/**
 * This class create a warning window. Future upgrade include adding an extension to
 * UIPart to utilize fxml and standardise to the rest of the class
 * under ui package.
 */
public class WarningWindow {
    private String message = "";
    private Frame frame = new Frame("");

    public WarningWindow(String presetMessage) {
        message = presetMessage;
        frame.setAlwaysOnTop(true);
    }

    /**
     * Creates the pop up warning window with the indicated frame and message.
     * @return true if yes to chose, else return false
     */
    public boolean isChoiceYes() {
        // if yes is picked, the address book data in model will be reset
        return JOptionPane.showConfirmDialog(frame, message, "WARNING!", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }
}
