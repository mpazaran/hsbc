CREATE DATABASE IF NOT EXISTS restaurant_db;

USE restaurant_db;

CREATE TABLE IF NOT EXISTS customer (
    customer_id INT AUTO_INCREMENT,  -- Auto-incremental ID
    name VARCHAR(100) NOT NULL,      -- Customer name
    email VARCHAR(100) NOT NULL UNIQUE,  -- Unique email for each customer
    phone_number VARCHAR(15),        -- Customer's phone number
    PRIMARY KEY (customer_id)        -- Primary key on customer_id
);

CREATE TABLE IF NOT EXISTS booking (
    booking_id INT AUTO_INCREMENT,  -- Auto-incremental ID for each booking
    customer_id INT NOT NULL,       -- Foreign key linking to customers table
    booking_date DATE NOT NULL,     -- Date of the booking
    booking_time TIME NOT NULL,     -- Time of the booking
    table_size INT NOT NULL,        -- Number of seats requested
    PRIMARY KEY (booking_id),       -- Primary key on booking_id
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
    ON DELETE CASCADE               -- Cascade delete on customer deletion
);
