import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HotelReservationSystem extends JFrame {
    // Data storage
    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<Reservation> reservations = new ArrayList<>();
    
    // UI Components
    private JTabbedPane tabbedPane;
    private DefaultTableModel roomTableModel, reservationTableModel;
    private JTable roomTable, reservationTable;

    public HotelReservationSystem() {
        setupSampleRooms();
        setupUI();
        
        setTitle("Hotel Reservation System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void setupSampleRooms() {
        // Add some sample rooms
        rooms.add(new Room("101", "Single", 50.0));
        rooms.add(new Room("102", "Single", 50.0));
        rooms.add(new Room("201", "Double", 80.0));
        rooms.add(new Room("202", "Double", 80.0));
        rooms.add(new Room("301", "Suite", 120.0));
    }

    private void setupUI() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Add tabs
        tabbedPane.addTab("📋 View Rooms", createViewRoomsPanel());
        tabbedPane.addTab("✅ Book Room", createBookRoomPanel());
        tabbedPane.addTab("📝 My Bookings", createMyBookingsPanel());
        tabbedPane.addTab("⚙️ Admin", createAdminPanel());
        
        add(tabbedPane);
    }

    // TAB 1: VIEW ROOMS
    private JPanel createViewRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Available Rooms", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        
        // Create table
        String[] columns = {"Room Number", "Type", "Price per Night", "Status"};
        roomTableModel = new DefaultTableModel(columns, 0);
        roomTable = new JTable(roomTableModel);
        roomTable.setFont(new Font("Arial", Font.PLAIN, 14));
        roomTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(roomTable);

        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 14));
        refreshBtn.addActionListener(e -> updateRoomTable());

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(refreshBtn, BorderLayout.SOUTH);

        updateRoomTable();
        return panel;
    }

    // TAB 2: BOOK ROOM
    private JPanel createBookRoomPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Book a Room");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel nameLabel = new JLabel("Your Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField phoneField = new JTextField();
        phoneField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel roomLabel = new JLabel("Room Number:");
        roomLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField roomField = new JTextField();
        roomField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel nightsLabel = new JLabel("Number of Nights:");
        nightsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField nightsField = new JTextField();
        nightsField.setFont(new Font("Arial", Font.PLAIN, 14));

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);
        formPanel.add(roomLabel);
        formPanel.add(roomField);
        formPanel.add(nightsLabel);
        formPanel.add(nightsField);

        JButton bookBtn = new JButton("✅ Book This Room");
        bookBtn.setFont(new Font("Arial", Font.BOLD, 16));
        bookBtn.setBackground(new Color(76, 175, 80));
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setOpaque(true);
        bookBtn.setBorderPainted(false);
        bookBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        bookBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String roomNum = roomField.getText().trim();
            String nightsStr = nightsField.getText().trim();

            // Check if all fields are filled
            if (name.isEmpty() || phone.isEmpty() || roomNum.isEmpty() || nightsStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please fill in all fields!", 
                    "Missing Information", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Find the room
            Room selectedRoom = null;
            for (Room room : rooms) {
                if (room.roomNumber.equals(roomNum)) {
                    selectedRoom = room;
                    break;
                }
            }

            if (selectedRoom == null) {
                JOptionPane.showMessageDialog(this, 
                    "Room not found! Please check the room number.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!selectedRoom.available) {
                JOptionPane.showMessageDialog(this, 
                    "Sorry, this room is already booked!", 
                    "Room Unavailable", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int nights = Integer.parseInt(nightsStr);
                if (nights <= 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Number of nights must be greater than 0!", 
                        "Invalid Input", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double total = selectedRoom.price * nights;

                // Create reservation
                Reservation newBooking = new Reservation(name, phone, selectedRoom, nights, total);
                reservations.add(newBooking);
                selectedRoom.available = false;

                JOptionPane.showMessageDialog(this, 
                    "Booking Successful! 🎉\n\n" +
                    "Name: " + name + "\n" +
                    "Room: " + roomNum + " (" + selectedRoom.type + ")\n" +
                    "Nights: " + nights + "\n" +
                    "Total Cost: $" + total + "\n\n" +
                    "Booking ID: " + newBooking.bookingId, 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);

                // Clear fields
                nameField.setText("");
                phoneField.setText("");
                roomField.setText("");
                nightsField.setText("");

                updateRoomTable();
                updateReservationTable();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a valid number for nights!", 
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(Box.createVerticalStrut(20));
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(formPanel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(bookBtn);

        return panel;
    }

    // TAB 3: MY BOOKINGS
    private JPanel createMyBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("My Bookings", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        // Create table
        String[] columns = {"Booking ID", "Name", "Room", "Nights", "Total Cost"};
        reservationTableModel = new DefaultTableModel(columns, 0);
        reservationTable = new JTable(reservationTableModel);
        reservationTable.setFont(new Font("Arial", Font.PLAIN, 14));
        reservationTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(reservationTable);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 14));
        refreshBtn.addActionListener(e -> updateReservationTable());

        JButton cancelBtn = new JButton("❌ Cancel Booking");
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 14));
        cancelBtn.setBackground(new Color(244, 67, 54));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setOpaque(true);
        cancelBtn.setBorderPainted(false);
        cancelBtn.addActionListener(e -> {
            int selectedRow = reservationTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, 
                    "Please select a booking to cancel!", 
                    "No Selection", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            String bookingId = (String) reservationTableModel.getValueAt(selectedRow, 0);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel booking " + bookingId + "?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Find and remove reservation
                for (int i = 0; i < reservations.size(); i++) {
                    if (reservations.get(i).bookingId.equals(bookingId)) {
                        Reservation res = reservations.get(i);
                        res.room.available = true;
                        reservations.remove(i);
                        break;
                    }
                }

                JOptionPane.showMessageDialog(this, 
                    "Booking cancelled successfully!", 
                    "Cancelled", 
                    JOptionPane.INFORMATION_MESSAGE);

                updateReservationTable();
                updateRoomTable();
            }
        });

        JButton reportBtn = new JButton("📊 Generate Report");
        reportBtn.setFont(new Font("Arial", Font.BOLD, 14));
        reportBtn.setBackground(new Color(33, 150, 243));
        reportBtn.setForeground(Color.WHITE);
        reportBtn.setOpaque(true);
        reportBtn.setBorderPainted(false);
        reportBtn.addActionListener(e -> generateReport());

        buttonPanel.add(refreshBtn);
        buttonPanel.add(cancelBtn);
        buttonPanel.add(reportBtn);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // TAB 4: ADMIN PANEL
    private JPanel createAdminPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Admin Panel - Manage Rooms");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add Room Form
        JPanel addPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        addPanel.setBorder(BorderFactory.createTitledBorder("Add New Room"));

        JLabel roomNumLabel = new JLabel("Room Number:");
        JTextField roomNumField = new JTextField();

        JLabel typeLabel = new JLabel("Room Type:");
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Single", "Double", "Suite", "Deluxe"});

        JLabel priceLabel = new JLabel("Price per Night ($):");
        JTextField priceField = new JTextField();

        addPanel.add(roomNumLabel);
        addPanel.add(roomNumField);
        addPanel.add(typeLabel);
        addPanel.add(typeCombo);
        addPanel.add(priceLabel);
        addPanel.add(priceField);

        JButton addBtn = new JButton("➕ Add Room");
        addBtn.setFont(new Font("Arial", Font.BOLD, 14));
        addBtn.setBackground(new Color(76, 175, 80));
        addBtn.setForeground(Color.WHITE);
        addBtn.setOpaque(true);
        addBtn.setBorderPainted(false);
        addBtn.addActionListener(e -> {
            String roomNum = roomNumField.getText().trim();
            String type = (String) typeCombo.getSelectedItem();
            String priceStr = priceField.getText().trim();

            if (roomNum.isEmpty() || priceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please fill in all fields!", 
                    "Missing Information", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Check if room already exists
            for (Room room : rooms) {
                if (room.roomNumber.equals(roomNum)) {
                    JOptionPane.showMessageDialog(this, 
                        "Room number already exists!", 
                        "Duplicate Room", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            try {
                double price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Price must be greater than 0!", 
                        "Invalid Price", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                rooms.add(new Room(roomNum, type, price));
                
                JOptionPane.showMessageDialog(this, 
                    "Room added successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);

                roomNumField.setText("");
                priceField.setText("");
                updateRoomTable();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a valid price!", 
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        // Remove Room Button
        JButton removeBtn = new JButton("➖ Remove Selected Room");
        removeBtn.setFont(new Font("Arial", Font.BOLD, 14));
        removeBtn.setBackground(new Color(244, 67, 54));
        removeBtn.setForeground(Color.WHITE);
        removeBtn.setOpaque(true);
        removeBtn.setBorderPainted(false);
        removeBtn.addActionListener(e -> {
            int selectedRow = roomTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, 
                    "Please select a room from the 'View Rooms' tab!", 
                    "No Selection", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            String roomNum = (String) roomTableModel.getValueAt(selectedRow, 0);
            
            // Find the room
            Room roomToRemove = null;
            for (Room room : rooms) {
                if (room.roomNumber.equals(roomNum)) {
                    roomToRemove = room;
                    break;
                }
            }

            if (roomToRemove != null && !roomToRemove.available) {
                JOptionPane.showMessageDialog(this, 
                    "Cannot remove a room that is currently booked!", 
                    "Room Occupied", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove room " + roomNum + "?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                rooms.remove(roomToRemove);
                JOptionPane.showMessageDialog(this, 
                    "Room removed successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                updateRoomTable();
            }
        });

        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(addPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(addBtn);
        panel.add(Box.createVerticalStrut(20));
        panel.add(removeBtn);

        return panel;
    }

    private void updateRoomTable() {
        roomTableModel.setRowCount(0);
        for (Room room : rooms) {
            roomTableModel.addRow(new Object[]{
                room.roomNumber,
                room.type,
                "$" + room.price,
                room.available ? "✅ Available" : "❌ Booked"
            });
        }
    }

    private void updateReservationTable() {
        reservationTableModel.setRowCount(0);
        for (Reservation res : reservations) {
            reservationTableModel.addRow(new Object[]{
                res.bookingId,
                res.customerName,
                res.room.roomNumber + " (" + res.room.type + ")",
                res.nights,
                "$" + res.totalCost
            });
        }
    }

    private void generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("===== HOTEL BOOKING REPORT =====\n\n");
        
        report.append("📊 SUMMARY:\n");
        report.append("Total Rooms: ").append(rooms.size()).append("\n");
        
        int availableCount = 0;
        for (Room room : rooms) {
            if (room.available) availableCount++;
        }
        report.append("Available Rooms: ").append(availableCount).append("\n");
        report.append("Booked Rooms: ").append(rooms.size() - availableCount).append("\n");
        report.append("Total Bookings: ").append(reservations.size()).append("\n\n");
        
        double totalRevenue = 0;
        for (Reservation res : reservations) {
            totalRevenue += res.totalCost;
        }
        report.append("Total Revenue: $").append(totalRevenue).append("\n\n");
        
        report.append("📋 BOOKING DETAILS:\n");
        report.append("----------------------------------------\n");
        for (Reservation res : reservations) {
            report.append("Booking ID: ").append(res.bookingId).append("\n");
            report.append("Customer: ").append(res.customerName).append("\n");
            report.append("Phone: ").append(res.customerPhone).append("\n");
            report.append("Room: ").append(res.room.roomNumber).append(" (").append(res.room.type).append(")\n");
            report.append("Nights: ").append(res.nights).append("\n");
            report.append("Total: $").append(res.totalCost).append("\n");
            report.append("----------------------------------------\n");
        }

        JTextArea textArea = new JTextArea(report.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, 
            "Booking Report", JOptionPane.INFORMATION_MESSAGE);
    }

    // Simple Room class
    static class Room {
        String roomNumber;
        String type;
        double price;
        boolean available;

        Room(String roomNumber, String type, double price) {
            this.roomNumber = roomNumber;
            this.type = type;
            this.price = price;
            this.available = true;
        }
    }

    // Simple Reservation class
    static class Reservation {
        static int nextId = 1;
        String bookingId;
        String customerName;
        String customerPhone;
        Room room;
        int nights;
        double totalCost;

        Reservation(String customerName, String customerPhone, Room room, int nights, double totalCost) {
            this.bookingId = "BK" + nextId++;
            this.customerName = customerName;
            this.customerPhone = customerPhone;
            this.room = room;
            this.nights = nights;
            this.totalCost = totalCost;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new HotelReservationSystem().setVisible(true);
        });
    }
}