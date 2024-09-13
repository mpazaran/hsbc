INSERT INTO customer (name, email, phone_number)
VALUES
('John Doe', 'john.doe@example.com', '1234567890'),
('Jane Smith', 'jane.smith@example.com', '0987654321'),
('Alice Johnson', 'alice.johnson@example.com', '5551234567'),
('Bob Williams', 'bob.williams@example.com', '4445556666'),
('Charlie Brown', 'charlie.brown@example.com', '3332221111');

INSERT INTO booking (customer_id, booking_date, booking_time, table_size)
VALUES
(1, '2024-09-15', '18:00:00', 4),
(1, '2024-09-16', '19:00:00', 2),
(2, '2024-09-17', '20:00:00', 6),
(2, '2024-09-18', '17:00:00', 3),
(3, '2024-09-19', '18:30:00', 5),
(3, '2024-09-20', '19:30:00', 2),
(4, '2024-09-21', '18:00:00', 4),
(4, '2024-09-22', '20:00:00', 2),
(5, '2024-09-23', '18:00:00', 3),
(5, '2024-09-24', '19:00:00', 6),
(1, '2024-09-25', '18:00:00', 4),
(2, '2024-09-26', '19:00:00', 2),
(3, '2024-09-27', '20:00:00', 6),
(4, '2024-09-28', '18:00:00', 3),
(5, '2024-09-29', '19:00:00', 4),
(1, '2024-09-30', '18:00:00', 4),
(2, '2024-10-01', '19:00:00', 2),
(3, '2024-10-02', '20:00:00', 5),
(4, '2024-10-03', '17:00:00', 3),
(5, '2024-10-04', '18:30:00', 6),
(4, '2024-09-30', '18:00:00', 3),
(5, '2024-09-30', '19:00:00', 4),
(1, '2024-09-30', '18:00:00', 4),
(2, '2024-09-30', '19:00:00', 2),
(3, '2024-09-30', '20:00:00', 5),
(4, '2024-09-30', '17:00:00', 3);

