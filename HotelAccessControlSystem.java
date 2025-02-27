import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HotelAccessControlSystem extends JFrame {
    private static final String ADMIN_PASSWORD = "1";
    private Map<String, String[]> guestCards = new HashMap<>();
    private Map<String, String[]> accessCards = new HashMap<>();
    private Map<String, String[]> historyLogs = new HashMap<>();
    private JTextArea logArea;

    public HotelAccessControlSystem() {
        // Set up the frame
        setTitle("Hotel Access Control System");
        setLayout(new BorderLayout());

        // Create a button panel for the login page
        JPanel buttonPanel = new JPanel();
        JButton guestButton = new JButton("Guest");
        JButton adminButton = new JButton("Admin");

        buttonPanel.add(guestButton);
        buttonPanel.add(adminButton);

        add(buttonPanel, BorderLayout.CENTER);

        guestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guestLogin();
            }
        });

        adminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adminLogin();
            }
        });

        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void guestLogin() {
        String code = JOptionPane.showInputDialog(this, "Enter 6-Digit Code to access your room:");

        // ตรวจสอบว่าโค้ดเป็น 6 หลักและเป็นตัวเลขทั้งหมด
        if (code != null && code.length() == 6 && code.matches("\\d{6}")) {
            System.out.println("Entered code: " + code);  // แสดงโค้ดที่กรอก
            // ใช้ guestCards เพื่อค้นหาค่าที่ตรงกัน
            for (Map.Entry<String, String[]> entry : guestCards.entrySet()) {
                if (entry.getValue()[3].equals(code)) {  // ใช้ code จาก guestCards เพื่อตรวจสอบ
                    String[] cardInfo = entry.getValue();
                    String floor = cardInfo[2];  // รับข้อมูลชั้นที่แขกสามารถเข้าถึง
                    JOptionPane.showMessageDialog(this, "Welcome! You are assigned to the " + floor + " floor.");
                    showRoomSelection(entry.getKey(), floor);
                    return;  // ถ้าพบโค้ดให้หยุด
                }
            }
            // ถ้าไม่พบ
            JOptionPane.showMessageDialog(this, "Invalid 6-Digit Code.");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid 6-Digit Code. Please enter a valid 6-digit number.");
        }
    }








    private void showRoomSelection(String code, String floor) {
        JPanel roomSelectionPanel = new JPanel(new GridLayout(2, 5)); // 2 rows, 5 rooms per row
        roomSelectionPanel.setBorder(BorderFactory.createTitledBorder("Select a Room"));

        int startRoom = 0;

        // Determine the range of rooms based on the floor
        if ("Luxury".equals(floor)) {
            startRoom = 301;
        } else if ("Premium".equals(floor)) {
            startRoom = 201;
        } else if ("Standard".equals(floor)) {
            startRoom = 101;
        }

        // Create buttons for room selection
        for (int i = 0; i < 10; i++) {
            int roomNumber = startRoom + i;
            JButton roomButton = new JButton(String.valueOf(roomNumber));
            roomButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    checkRoom(code, roomNumber);
                }
            });
            roomSelectionPanel.add(roomButton);
        }

        int option = JOptionPane.showConfirmDialog(this, roomSelectionPanel, "Select Your Room", JOptionPane.OK_CANCEL_OPTION);
    }


    private void checkRoom(String code, int selectedRoom) {
        String[] cardInfo = accessCards.get(code);
        int assignedRoom = Integer.parseInt(cardInfo[1]);

        if (selectedRoom == assignedRoom) {
            JOptionPane.showMessageDialog(this, "Room " + selectedRoom + " is assigned to you. Enjoy your stay!");
        } else {
            JOptionPane.showMessageDialog(this, "This room is not assigned to you. Please select the correct room.");
        }
    }




    private void adminLogin() {
        String password = JOptionPane.showInputDialog(this, "Enter Admin Password:");
        if (ADMIN_PASSWORD.equals(password)) {
            showAdminMenu();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid password.");
        }
    }

    private void showAdminMenu() {
        JPanel adminPanel = new JPanel(new GridLayout(6, 1));  // เพิ่มแถวให้พอสำหรับปุ่มใหม่

        JButton createCardButton = new JButton("Create Card (Check-in)");
        JButton removeCardButton = new JButton("Remove Card (Check-out)");
        JButton editCardButton = new JButton("Edit Card");
        JButton viewHistoryButton = new JButton("View Card History");
        JButton homeButton = new JButton("Home");  // ปุ่มกลับไปหน้าแรก
        JButton closeButton = new JButton("Close");

        adminPanel.add(createCardButton);
        adminPanel.add(removeCardButton);
        adminPanel.add(editCardButton);
        adminPanel.add(viewHistoryButton);
        adminPanel.add(homeButton);  // เพิ่มปุ่ม Home
        adminPanel.add(closeButton);

        createCardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createCard();
            }
        });

        removeCardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeCard();
            }
        });

        editCardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editCard();
            }
        });

        viewHistoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showHistoryDialog();
            }
        });

        homeButton.addActionListener(new ActionListener() {  // เพิ่มการทำงานของปุ่ม Home
            public void actionPerformed(ActionEvent e) {
                showHomePage();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        getContentPane().removeAll();
        add(adminPanel);
        revalidate();
        repaint();
    }

    // ฟังก์ชันแสดงหน้าแรก (Guest / Admin)
    private void showHomePage() {
        getContentPane().removeAll();

        JPanel buttonPanel = new JPanel();
        JButton guestButton = new JButton("Guest");
        JButton adminButton = new JButton("Admin");

        buttonPanel.add(guestButton);
        buttonPanel.add(adminButton);

        add(buttonPanel, BorderLayout.CENTER);

        guestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guestLogin();
            }
        });

        adminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adminLogin();
            }
        });

        revalidate();
        repaint();
    }


    // ฟังก์ชันสำหรับแก้ไขการ์ด
    private void editCard() {
        if (guestCards.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No cards available to edit.");
            return;
        }

        // แสดงรายชื่อผู้จองทั้งหมดใน guestCards
        String[] guestNames = guestCards.values().stream()
                .map(cardInfo -> cardInfo[0]) // cardInfo[0] คือชื่อผู้จอง
                .toArray(String[]::new);

        String selectedGuestName = (String) JOptionPane.showInputDialog(
                null,
                "Select Guest to Edit:",
                "Edit Card",
                JOptionPane.QUESTION_MESSAGE,
                null,
                guestNames,
                guestNames[0]
        );

        if (selectedGuestName != null) {
            // ค้นหาการ์ดที่ตรงกับชื่อผู้จอง
            String selectedCard = null;
            for (Map.Entry<String, String[]> entry : guestCards.entrySet()) {
                if (entry.getValue()[0].equals(selectedGuestName)) {
                    selectedCard = entry.getKey();
                    break;
                }
            }

            String[] cardInfo = guestCards.get(selectedCard);

            // แสดงข้อมูลปัจจุบันของการ์ด
            JTextField nameField = new JTextField(cardInfo[0]);
            JTextField roomField = new JTextField(cardInfo[1]);
            String[] floorOptions = {"Standard", "Premium", "Luxury"};
            JComboBox<String> floorCombo = new JComboBox<>(floorOptions);
            floorCombo.setSelectedItem(cardInfo[2]);

            // เพิ่มช่องกรอกรหัส 6 หลัก
            JTextField codeField = new JTextField(cardInfo[3]);

            JPanel editPanel = new JPanel(new GridLayout(4, 2)); // เพิ่มแถวสำหรับรหัส 6 หลัก
            editPanel.add(new JLabel("Guest Name:"));
            editPanel.add(nameField);
            editPanel.add(new JLabel("Room Number:"));
            editPanel.add(roomField);
            editPanel.add(new JLabel("Floor:"));
            editPanel.add(floorCombo);
            editPanel.add(new JLabel("6-Digit Code:"));
            editPanel.add(codeField);

            int option = JOptionPane.showConfirmDialog(this, editPanel, "Edit Card", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String newGuestName = nameField.getText();
                String newRoomNumber = roomField.getText();
                String newFloor = (String) floorCombo.getSelectedItem();
                String newCode = codeField.getText(); // ดึงรหัส 6 หลักใหม่

                // ตรวจสอบห้องว่ามีการจองหรือไม่ในชั้นเดียวกัน
                boolean roomBooked = false;
                for (String[] cardInfoCheck : guestCards.values()) {
                    // ถ้าห้องในชั้นเดียวกัน (floor) ซ้ำกับห้องใหม่
                    if (cardInfoCheck[2].equals(newFloor) && cardInfoCheck[1].equals(newRoomNumber) && !cardInfoCheck[3].equals(newCode)) {
                        roomBooked = true;
                        break;
                    }
                }

                if (roomBooked) {
                    JOptionPane.showMessageDialog(this, "This room is already booked on the same floor. Please choose a different room.");
                    return;
                }

                // ตรวจสอบรหัส 6 หลักว่ามีการซ้ำกับการ์ดอื่นหรือไม่
                boolean codeExists = false;
                for (String[] cardInfoCheck : guestCards.values()) {
                    if (cardInfoCheck[3].equals(newCode) && !cardInfoCheck[1].equals(newRoomNumber)) {
                        codeExists = true;
                        break;
                    }
                }

                if (codeExists) {
                    JOptionPane.showMessageDialog(this, "This 6-digit code is already in use. Please choose a different code.");
                    return;
                }

                // อัพเดทข้อมูลของการ์ด
                guestCards.put(selectedCard, new String[]{newGuestName, newRoomNumber, newFloor, newCode});
                accessCards.put(selectedCard, new String[]{newGuestName, newRoomNumber, newFloor, newCode});

                // เพิ่มบันทึกการแก้ไขในประวัติ
                historyLogs.put(selectedCard, new String[]{"Edited", newGuestName, newRoomNumber, newFloor, newCode});
                logAccess(selectedCard, "Card Edited for " + newGuestName + " (Room " + newRoomNumber + ", Floor " + newFloor + ", Code " + newCode + ")");

                JOptionPane.showMessageDialog(this, "Card edited successfully!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No guest selected for editing.");
        }
    }




    private void createCard() {
        JTextField nameField = new JTextField(10);
        JTextField roomField = new JTextField(10);
        String[] floorOptions = {"Standard", "Premium", "Luxury"};
        JComboBox<String> floorCombo = new JComboBox<>(floorOptions);
        JComboBox<String> roomCombo = new JComboBox<>();

        floorCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedFloor = (String) floorCombo.getSelectedItem();
                roomCombo.removeAllItems();
                if ("Standard".equals(selectedFloor)) {
                    for (int i = 101; i <= 110; i++) {
                        roomCombo.addItem(String.valueOf(i));
                    }
                } else if ("Premium".equals(selectedFloor)) {
                    for (int i = 201; i <= 210; i++) {
                        roomCombo.addItem(String.valueOf(i));
                    }
                } else if ("Luxury".equals(selectedFloor)) {
                    for (int i = 301; i <= 310; i++) {
                        roomCombo.addItem(String.valueOf(i));
                    }
                }
            }
        });

        for (int i = 101; i <= 110; i++) {
            roomCombo.addItem(String.valueOf(i));
        }

        JPanel panel = new JPanel();
        panel.add(new JLabel("Guest Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Floor:"));
        panel.add(floorCombo);
        panel.add(new JLabel("Room Number:"));
        panel.add(roomCombo);

        JTextField codeField = new JTextField(6);
        panel.add(new JLabel("Enter 6-Digit Code:"));
        panel.add(codeField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Create Card (Check-in)", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String guestName = nameField.getText();
            String roomNumber = (String) roomCombo.getSelectedItem();
            String floor = (String) floorCombo.getSelectedItem();
            String code = codeField.getText();

            boolean roomBooked = false;
            for (String[] cardInfo : guestCards.values()) {
                if (cardInfo[1].equals(roomNumber) && cardInfo[2].equals(floor)) {
                    roomBooked = true;
                    break;
                }
            }

            if (roomBooked) {
                JOptionPane.showMessageDialog(this, "This room is already booked. Please choose a different room.");
                return;
            }

            if (code.length() != 6 || !code.matches("\\d{6}")) {
                JOptionPane.showMessageDialog(this, "Invalid code. Please enter a 6-digit number.");
                return;
            }

            String cardId = generateCardId();
            guestCards.put(cardId, new String[]{guestName, roomNumber, floor, code});
            accessCards.put(cardId, new String[]{guestName, roomNumber, floor, code});

            historyLogs.put(cardId, new String[]{"Created", guestName, roomNumber, floor});
            logAccess(cardId, "Card Created for " + guestName + " (Room " + roomNumber + ", Floor " + floor + ")");

            JOptionPane.showMessageDialog(this, "Card created successfully!\nCard ID: " + cardId);
        }
    }




    private void removeCard() {
        if (guestCards.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No cards available to remove.");
            return;
        }

        // สร้างรายการบัตรที่มี พร้อมแสดง Floor, Room และ Guest Name ในรูปแบบที่กำหนด
        String[] cardList = guestCards.entrySet().stream()
                .map(entry -> entry.getKey() + " | floor : " + entry.getValue()[2] + " | room : " + entry.getValue()[1] + " | Name : " + entry.getValue()[0])
                .toArray(String[]::new);

        String selectedCard = (String) JOptionPane.showInputDialog(
                this,
                "Select Card to Remove (Check-out):",
                "Remove Card",
                JOptionPane.QUESTION_MESSAGE,
                null,
                cardList,
                cardList[0]
        );

        if (selectedCard != null) {
            // ดึง Card ID จากรายการที่เลือก (ส่วนแรกของ String ก่อนเจอ ' | ')
            String cardId = selectedCard.split(" \\| ")[0];

            // ดึงข้อมูลการ์ดที่ต้องการลบ
            String[] cardInfo = guestCards.get(cardId);

            int option = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to remove this card?\n" +
                            "Card ID: " + cardId + "\n" +
                            "Guest Name: " + cardInfo[0] + "\n" +
                            "Room: " + cardInfo[1] + "\n" +
                            "Floor: " + cardInfo[2],
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                guestCards.remove(cardId);
                accessCards.remove(cardId);

                // บันทึกประวัติการลบ
                historyLogs.put(cardId, new String[]{"Removed", cardInfo[0], cardInfo[1], cardInfo[2]});
                logAccess(cardId, "Card Removed for " + cardInfo[0] + " (Room " + cardInfo[1] + ", Floor " + cardInfo[2] + ")");

                JOptionPane.showMessageDialog(this, "Card " + cardId + " removed successfully (Check-out).");

                // แสดงประวัติที่อัปเดตหลังจากลบ
                showHistoryDialog();
            }
        } else {
            JOptionPane.showMessageDialog(this, "No card selected for removal.");
        }
    }



    private void showHistoryDialog() {
        JDialog historyDialog = new JDialog(this, "View History", true);
        historyDialog.setLayout(new BorderLayout());

        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false);

        // แสดงประวัติการ์ด พร้อมเวลาที่บันทึก
        for (Map.Entry<String, String[]> entry : historyLogs.entrySet()) {
            String[] history = entry.getValue();
            String cardId = entry.getKey();
            String[] cardInfo = guestCards.get(cardId);  // ดึงข้อมูลจาก guestCards

            String guestName = cardInfo[0];
            String roomNumber = cardInfo[1];
            String floor = cardInfo[2];
            String code = cardInfo[3];  // รหัส 6 หลัก

            // แสดงประวัติและรหัส 6 หลัก
            historyArea.append("Card ID: " + cardId +
                    " | Name: " + guestName +
                    " | Room: " + roomNumber +
                    " | Floor: " + floor +
                    " | Code: " + code +  // แสดงรหัส 6 หลัก
                    " | Action: " + history[0] +
                    " | Time: " + history[1] + "\n");
        }

        historyDialog.add(new JScrollPane(historyArea), BorderLayout.CENTER);
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                historyDialog.dispose();
            }
        });
        historyDialog.add(closeButton, BorderLayout.SOUTH);

        historyDialog.setSize(500, 300);
        historyDialog.setLocationRelativeTo(this);
        historyDialog.setVisible(true);
    }



    private void logAccess(String cardId, String message) {
        LocalDateTime now = LocalDateTime.now();  // ดึงเวลาปัจจุบัน
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        String timestamp = now.format(formatter);  // จัดรูปแบบเวลา

        historyLogs.put(cardId, new String[]{message, timestamp});  // บันทึกเวลาในประวัติ
        System.out.println(timestamp + " - " + message);  // แสดงข้อความใน Console
    }

    private String generateCardId() {
        return "card" + (guestCards.size() + 1);
    }

    public static void main(String[] args) {
        new HotelAccessControlSystem();
    }
}
