import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class MultiFloorAccessControl {
    private static Map<String, String> accessCards = new HashMap<>(); // Card ID -> Access Level

    public static void main(String[] args) {
        // Predefined access levels
        accessCards.put("12345", "low");
        accessCards.put("67890", "medium");
        accessCards.put("11223", "high");

        // Create GUI
        JFrame frame = new JFrame("Multi-Floor Access Control");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(4, 1));

        JLabel label = new JLabel("Enter Card ID:", SwingConstants.CENTER);
        JTextField cardInput = new JTextField();
        String[] floors = {"low", "medium", "high"};
        JComboBox<String> floorSelection = new JComboBox<>(floors);
        JButton checkAccessButton = new JButton("Check Access");
        JLabel resultLabel = new JLabel("", SwingConstants.CENTER);

        checkAccessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cardId = cardInput.getText();
                String selectedFloor = (String) floorSelection.getSelectedItem();
                String accessLevel = accessCards.get(cardId);

                if (accessLevel != null && selectedFloor.equals(accessLevel)) {
                    resultLabel.setText("Access Granted to " + selectedFloor + " floor");
                    resultLabel.setForeground(Color.GREEN);
                } else {
                    resultLabel.setText("Access Denied");
                    resultLabel.setForeground(Color.RED);
                }
            }
        });

        frame.add(label);
        frame.add(cardInput);
        frame.add(floorSelection);
        frame.add(checkAccessButton);
        frame.add(resultLabel);

        frame.setVisible(true);
    }
}
