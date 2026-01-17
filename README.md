# Hotel Reservation System – Java Swing Application

# Overview
The Hotel Reservation System is a desktop-based application developed using Java Swing that simulates a real-world hotel booking workflow. It allows users to view available rooms, book rooms, manage reservations, and provides an admin panel for room management. The system is designed with a tab-based interface for clarity, usability, and modular functionality.
This project demonstrates strong fundamentals in Java GUI development, object-oriented design, and event-driven programming.

# Features

Room Management
•	View all rooms with:
o	Room number
o	Room type
o	Price per night
o	Availability status
•	Real-time status updates when rooms are booked or canceled

Booking System
•	Book available rooms by providing:
o	Customer name
o	Phone number
o	Room number
o	Number of nights
•	Automatic cost calculation
•	Unique booking ID generation
•	Input validation and user-friendly error handling

Reservation Management
•	View all bookings in a structured table
•	Cancel existing reservations
•	Automatically frees room availability upon cancellation
•	Refresh booking data on demand

Admin Panel
•	Add new rooms with type and pricing
•	Prevent duplicate room numbers
•	Remove rooms (only if not currently booked)
•	Real-time update of room listings

Reporting
•	Generate a comprehensive booking report including:
o	Total rooms
o	Available vs booked rooms
o	Total bookings
o	Total revenue
o	Detailed booking breakdown

•	View reports in a scrollable dialog

# Technologies Used
•	Java (JDK 8 or higher)
•	Java Swing
•	AWT Event Handling
•	Collections Framework (ArrayList)

# Usage Guide
1.	View Rooms
Navigate to the View Rooms tab to see available and booked rooms.
2.	Book a Room
Enter customer details, room number, and number of nights, then confirm booking.
3.	My Bookings
View, refresh, cancel bookings, or generate a booking report.
4.	Admin Panel
Add or remove rooms while ensuring booking constraints are respected.

# Validation & Error Handling
•	Mandatory field checks
•	Numeric validation for price and nights
•	Prevents booking unavailable rooms
•	Prevents deleting booked rooms
•	Confirmation dialogs for destructive actions

# Learning Outcomes
•	Java Swing UI design with tabs and layouts
•	Event-driven programming using action listeners
•	Real-world simulation of booking workflows
•	Managing shared state between UI components
•	Structuring medium-sized Java desktop applications
