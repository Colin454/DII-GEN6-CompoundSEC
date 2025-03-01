import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HotelApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Hotel Management System");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // ปุ่มสำหรับเลือก guest หรือ admin
        JButton guestButton = new JButton("Guest");
        JButton adminButton = new JButton("Admin");

        // เพิ่มปุ่มลงใน frame
        frame.add(guestButton);
        frame.add(adminButton);

        // ฟังก์ชันที่ทำงานเมื่อคลิกปุ่ม Guest
        guestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Guest guest = new Guest();
                guest.showGuestOptions();  // เรียกใช้เมนู Guest
            }
        });

        // ฟังก์ชันที่ทำงานเมื่อคลิกปุ่ม Admin
        adminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String password = JOptionPane.showInputDialog("Enter Admin Password:");
                if (password != null && password.equals("1")) {
                    Admin admin = new Admin();
                    admin.showAdminOptions(frame);
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect password!");
                }
            }
        });

        // แสดงหน้าหลัก
        frame.setVisible(true);
    }
}

// Class สำหรับ Guest
class Guest {
    public void showGuestOptions() {
        // กรอกเลข 4 หลักจากบัตร
        String cardNumber = JOptionPane.showInputDialog("Enter your 4-digit Card Number:");

        // ตรวจสอบว่าเลขบัตรถูกต้องหรือไม่
        if (cardNumber.length() != 4 || !cardNumber.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(null, "Invalid Card Number! It must be 4 digits.");
            return;
        }

        // ค้นหาบัตรที่มีเลขตรงกับที่กรอก หรือเป็นบัตรพิเศษ 9999
        Card matchedCard = null;
        String guestName = "Guest";  // Default name
        if (cardNumber.equals("9999")) {
            guestName = "admin";  // admin can access all rooms
        } else {
            for (Card card : Admin.cards) {
                if (card.getCardNumber().equals(cardNumber)) {
                    matchedCard = card;
                    guestName = card.getGuestName();
                    break;
                }
            }
        }

        // หากไม่พบบัตรที่ตรงกับเลขที่กรอก
        if (guestName.equals("Guest") && matchedCard == null) {
            JOptionPane.showMessageDialog(null, "Card Number not found.");
            return;
        }

        // ถ้าเป็น admin หรือบัตรพิเศษ, ให้สามารถเลือกชั้นได้
        String floor = "any floor";  // Default for non-admins
        if (cardNumber.equals("9999")) {
            floor = (String) JOptionPane.showInputDialog(null, "Choose Floor", "Select Floor", JOptionPane.QUESTION_MESSAGE, null, new String[]{"Standard", "Premium", "Luxury"}, "Standard");
        } else {
            floor = matchedCard.getFloor();
        }

        // เปลี่ยนแปลงหมายเลขห้องตามชั้นที่เลือก
        String[] roomOptions = null;
        if (floor.equals("Standard")) {
            roomOptions = new String[]{"101", "102", "103", "104", "105"};
        } else if (floor.equals("Premium")) {
            roomOptions = new String[]{"201", "202", "203", "204", "205"};
        } else if (floor.equals("Luxury")) {
            roomOptions = new String[]{"301", "302", "303", "304", "305"};
        }

        // ให้ผู้ใช้เลือกห้อง
        String selectedRoom = (String) JOptionPane.showInputDialog(null, "Select Room", "Select Room", JOptionPane.QUESTION_MESSAGE, null, roomOptions, roomOptions[0]);

        // ตรวจสอบว่าเลือกห้องที่ตรงกับบัตรที่กรอกหรือไม่
        boolean isSuccessful = false;
        if (selectedRoom != null) {
            if (cardNumber.equals("9999") || (selectedRoom.equals(matchedCard.getRoomNumber()))) {
                isSuccessful = true;
                JOptionPane.showMessageDialog(null, "You have successfully entered your room: " + selectedRoom);
            } else {
                JOptionPane.showMessageDialog(null, "You cannot access a room that is not assigned to you.");
            }
        }

        // เพิ่ม log การเข้าใช้ห้องของ Guest หรือ admin
        logGuestEntry(guestName, selectedRoom, isSuccessful);
    }




    // ฟังก์ชันเพื่อบันทึก log การเข้าห้องของ Guest
    private void logGuestEntry(String guestName, String selectedRoom, boolean isSuccessful) {
        String logMessage;
        if (isSuccessful) {
            logMessage = "User: " + guestName + " successfully entered Room: " + selectedRoom + " on " + getCurrentDateTime();
        } else {
            logMessage = "User: " + guestName + " attempted to enter Room: " + selectedRoom + " (Incorrect Room) on " + getCurrentDateTime();
        }
        Admin.historyLog.add(logMessage);  // บันทึก log เข้าไปใน history ของ Admin
    }

    // ฟังก์ชันสำหรับการรับวันและเวลา
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}

// Class สำหรับ Admin
class Admin {
    public static ArrayList<Card> cards = new ArrayList<>(); // เก็บข้อมูลบัตร
    public static ArrayList<String> historyLog = new ArrayList<>(); // เก็บ log การสร้างและลบบัตร รวมถึง log การเข้าใช้ห้องของ Guest

    public void showAdminOptions(JFrame frame) {
        // สร้างหน้าต่างใหม่สำหรับ Admin
        JFrame adminFrame = new JFrame("Admin Options");
        adminFrame.setSize(300, 400);
        adminFrame.setLayout(new FlowLayout());

        // สร้างปุ่มสำหรับแต่ละฟังก์ชัน
        JButton createCardButton = new JButton("Create Card (Check-in)");
        JButton removeCardButton = new JButton("Remove Card (Check-out)");
        JButton editCardButton = new JButton("Edit Card");
        JButton viewHistoryButton = new JButton("View History");
        JButton homeButton = new JButton("Home");

        // เพิ่มปุ่มลงใน admin frame
        adminFrame.add(createCardButton);
        adminFrame.add(removeCardButton);
        adminFrame.add(editCardButton);
        adminFrame.add(viewHistoryButton);
        adminFrame.add(homeButton);

        // ฟังก์ชันสำหรับปุ่ม Home
        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adminFrame.dispose();  // ปิดหน้าต่าง admin
                frame.setVisible(true); // กลับไปที่หน้าหลัก
            }
        });

        // ฟังก์ชันสำหรับปุ่ม Create Card
        createCardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createCard();
            }
        });

        // ฟังก์ชันสำหรับปุ่ม Remove Card
        removeCardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeCard();
            }
        });

        // ฟังก์ชันสำหรับปุ่ม Edit Card
        editCardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editCard();
            }
        });

        // ฟังก์ชันสำหรับปุ่ม View History
        viewHistoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewHistory();  // แสดงประวัติการทำงานทั้งหมด
            }
        });

        // แสดงหน้าต่างสำหรับ Admin
        adminFrame.setVisible(true);
        frame.setVisible(false);  // ซ่อนหน้าหลัก
    }

    // ฟังก์ชันสำหรับแสดงประวัติการทำงานทั้งหมด
    public void viewHistory() {
        if (historyLog.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No history log available.");
            return;
        }

        StringBuilder history = new StringBuilder("History Log:\n");
        for (String log : historyLog) {
            history.append(log).append("\n");
        }
        JOptionPane.showMessageDialog(null, history.toString());
    }

    // ฟังก์ชันสำหรับการสร้างบัตร
    public void createCard() {
        // กรอกชื่อของแขก
        String guestName = JOptionPane.showInputDialog("Enter Guest Name:");

        // กรอกข้อมูลสำหรับ Floor
        String[] floorOptions = {"Standard", "Premium", "Luxury"};
        String floor = (String) JOptionPane.showInputDialog(null, "Choose Floor", "Select Floor", JOptionPane.QUESTION_MESSAGE, null, floorOptions, floorOptions[0]);

        if (floor == null) return; // หากผู้ใช้ปิดกล่องเลือกหรือไม่เลือกใดๆ

        // เลือกหมายเลขห้องตามประเภท
        String roomNumber = "";
        if (floor.equals("Standard")) {
            roomNumber = (String) JOptionPane.showInputDialog(null, "Select Room Number", "Standard Rooms", JOptionPane.QUESTION_MESSAGE, null, new String[]{"101", "102", "103", "104", "105"}, "101");
            if (roomNumber == null) return;  // หากผู้ใช้ไม่เลือกห้อง
        } else if (floor.equals("Premium")) {
            roomNumber = (String) JOptionPane.showInputDialog(null, "Select Room Number", "Premium Rooms", JOptionPane.QUESTION_MESSAGE, null, new String[]{"201", "202", "203", "204", "205"}, "201");
            if (roomNumber == null) return;  // หากผู้ใช้ไม่เลือกห้อง
        } else if (floor.equals("Luxury")) {
            roomNumber = (String) JOptionPane.showInputDialog(null, "Select Room Number", "Luxury Rooms", JOptionPane.QUESTION_MESSAGE, null, new String[]{"301", "302", "303", "304", "305"}, "301");
            if (roomNumber == null) return;  // หากผู้ใช้ไม่เลือกห้อง
        }

        // กรอกเลขบัตร 4 หลัก
        String cardNumber = JOptionPane.showInputDialog("Enter 4-digit Card Number:");
        if (cardNumber.length() != 4 || !cardNumber.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(null, "Invalid Card Number! It must be 4 digits.");
            return;
        }

        // สร้างบัตรและเก็บข้อมูล
        Card newCard = new Card(guestName, floor, roomNumber, cardNumber);
        cards.add(newCard);

        // บันทึกการสร้างบัตรใน log
        String logMessage = "Created Card: " + newCard + " on " + getCurrentDateTime();
        historyLog.add(logMessage);

        // แสดงข้อความยืนยัน
        JOptionPane.showMessageDialog(null, "Card Created: " + newCard);
    }

    // ฟังก์ชันสำหรับการลบบัตร
    public void removeCard() {
        if (cards.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No cards available to remove.");
            return;
        }

        // แสดงรายการบัตรที่สร้างขึ้น
        String[] cardList = new String[cards.size()];
        for (int i = 0; i < cards.size(); i++) {
            cardList[i] = cards.get(i).toString();
        }

        // ให้ผู้ใช้เลือกบัตรที่ต้องการลบ
        String cardToRemove = (String) JOptionPane.showInputDialog(null, "Select Card to Remove", "Remove Card", JOptionPane.QUESTION_MESSAGE, null, cardList, cardList[0]);

        if (cardToRemove == null) {
            return; // หากผู้ใช้ปิดกล่องเลือกหรือไม่เลือก
        }

        // ลบบัตรที่เลือก
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).toString().equals(cardToRemove)) {
                cards.remove(i);
                // บันทึกการลบบัตรใน log
                String logMessage = "Removed Card: " + cardToRemove + " on " + getCurrentDateTime();
                historyLog.add(logMessage);
                JOptionPane.showMessageDialog(null, "Card Removed Successfully!");
                return;
            }
        }
    }

    // ฟังก์ชันสำหรับการแก้ไขบัตร
    public void editCard() {
        if (cards.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No cards available to edit.");
            return;
        }

        // แสดงรายการบัตรที่สร้างขึ้น
        String[] cardList = new String[cards.size()];
        for (int i = 0; i < cards.size(); i++) {
            cardList[i] = cards.get(i).toString();
        }

        // ให้ผู้ใช้เลือกบัตรที่ต้องการแก้ไข
        String cardToEdit = (String) JOptionPane.showInputDialog(null, "Select Card to Edit", "Edit Card", JOptionPane.QUESTION_MESSAGE, null, cardList, cardList[0]);

        if (cardToEdit == null) {
            return; // หากผู้ใช้ไม่เลือกหรือปิดกล่องเลือก
        }

        // หาบัตรที่เลือกจากรายการ
        Card selectedCard = null;
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).toString().equals(cardToEdit)) {
                selectedCard = cards.get(i);
                break;
            }
        }

        // ถ้าพบบัตรที่เลือก
        if (selectedCard != null) {
            // ให้ผู้ใช้แก้ไขข้อมูล
            String newGuestName = JOptionPane.showInputDialog("Edit Guest Name:", selectedCard.getGuestName());
            String[] floorOptions = {"Standard", "Premium", "Luxury"};
            String newFloor = (String) JOptionPane.showInputDialog(null, "Select Floor", "Edit Floor", JOptionPane.QUESTION_MESSAGE, null, floorOptions, selectedCard.getFloor());

            if (newFloor == null) return; // หากผู้ใช้ปิดกล่องเลือกหรือไม่เลือกใดๆ

            String newRoomNumber = "";
            if (newFloor.equals("Standard")) {
                newRoomNumber = (String) JOptionPane.showInputDialog(null, "Select Room Number", "Standard Rooms", JOptionPane.QUESTION_MESSAGE, null, new String[]{"101", "102", "103", "104", "105"}, selectedCard.getRoomNumber());
            } else if (newFloor.equals("Premium")) {
                newRoomNumber = (String) JOptionPane.showInputDialog(null, "Select Room Number", "Premium Rooms", JOptionPane.QUESTION_MESSAGE, null, new String[]{"201", "202", "203", "204", "205"}, selectedCard.getRoomNumber());
            } else if (newFloor.equals("Luxury")) {
                newRoomNumber = (String) JOptionPane.showInputDialog(null, "Select Room Number", "Luxury Rooms", JOptionPane.QUESTION_MESSAGE, null, new String[]{"301", "302", "303", "304", "305"}, selectedCard.getRoomNumber());
            }

            // แก้ไขเลขบัตร 4 หลัก
            String newCardNumber = JOptionPane.showInputDialog("Edit 4-digit Card Number:", selectedCard.getCardNumber());
            if (newCardNumber.length() != 4 || !newCardNumber.matches("\\d{4}")) {
                JOptionPane.showMessageDialog(null, "Invalid Card Number! It must be 4 digits.");
                return;
            }

            // อัปเดตข้อมูลในบัตร
            selectedCard.setGuestName(newGuestName);
            selectedCard.setFloor(newFloor);
            selectedCard.setRoomNumber(newRoomNumber);
            selectedCard.setCardNumber(newCardNumber);

            // บันทึกการแก้ไขบัตรใน log
            String logMessage = "Edited Card: " + selectedCard + " on " + getCurrentDateTime();
            historyLog.add(logMessage);

            // แสดงข้อความยืนยันการแก้ไข
            JOptionPane.showMessageDialog(null, "Card Updated Successfully!");
        }
    }

    // ฟังก์ชันสำหรับการรับวันและเวลา
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}

// Class สำหรับ Card
class Card {
    private String guestName;
    private String floor;
    private String roomNumber;
    private String cardNumber;

    public Card(String guestName, String floor, String roomNumber, String cardNumber) {
        this.guestName = guestName;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.cardNumber = cardNumber;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String toString() {
        return "Guest Name: " + guestName + ", Floor: " + floor + ", Room: " + roomNumber + ", Card Number: " + cardNumber;
    }
}
