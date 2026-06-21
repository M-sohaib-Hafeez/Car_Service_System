-- ============================================
-- SAMPLE DATA FOR CAR SERVICE CENTER SYSTEM
-- ============================================

-- Clear existing data (optional - be careful with this!)
-- SET FOREIGN_KEY_CHECKS = 0;
-- TRUNCATE TABLE appointments;
-- TRUNCATE TABLE mechanic_requests;
-- TRUNCATE TABLE mechanics;
-- TRUNCATE TABLE vehicles;
-- TRUNCATE TABLE services;
-- TRUNCATE TABLE users;
-- TRUNCATE TABLE customers;
-- SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 1. CUSTOMERS TABLE
-- ============================================
INSERT INTO customers (name, email, phone, address, loyalty_points) VALUES
                                                                        ('Ahmed Ali', 'ahmed.ali@email.com', '0300-1234567', 'House 123, DHA Phase 5, Karachi', 150),
                                                                        ('Fatima Khan', 'fatima.khan@email.com', '0321-2345678', 'Flat 45, Gulshan-e-Iqbal, Karachi', 220),
                                                                        ('Usman Ahmed', 'usman.ahmed@email.com', '0333-3456789', 'Plot 67, Clifton Block 8, Karachi', 80),
                                                                        ('Ayesha Siddiqui', 'ayesha.siddiqui@email.com', '0345-4567890', 'House 89, North Nazimabad, Karachi', 300),
                                                                        ('Hassan Raza', 'hassan.raza@email.com', '0312-5678901', 'Apartment 12, Bahria Town, Karachi', 50);

-- ============================================
-- 2. USERS TABLE
-- ============================================
INSERT INTO users (username, email, password, role, customer_id) VALUES
-- Admin user
('admin', 'admin@carservice.com', 'admin123', 'ADMIN', NULL),

-- Customer users (linked to customers above)
('ahmed_ali', 'ahmed.ali@email.com', 'pass123', 'CUSTOMER', 1),
('fatima_khan', 'fatima.khan@email.com', 'pass123', 'CUSTOMER', 2),
('usman_ahmed', 'usman.ahmed@email.com', 'pass123', 'CUSTOMER', 3),
('ayesha_siddiqui', 'ayesha.siddiqui@email.com', 'pass123', 'CUSTOMER', 4),
('hassan_raza', 'hassan.raza@email.com', 'pass123', 'CUSTOMER', 5),

-- Mechanic users (will be linked to mechanics after approval)
('ali_mechanic', 'ali.mechanic@email.com', 'mech123', 'MECHANIC', NULL),
('hamza_mechanic', 'hamza.mechanic@email.com', 'mech123', 'MECHANIC', NULL),
('bilal_mechanic', 'bilal.mechanic@email.com', 'mech123', 'MECHANIC', NULL),
('zain_mechanic', 'zain.mechanic@email.com', 'mech123', 'MECHANIC', NULL);

-- ============================================
-- 3. MECHANICS TABLE
-- ============================================
INSERT INTO mechanics (user_id, name, specialization, status, hourly_rate, phone, email) VALUES
                                                                                             (7, 'Ali Hassan', 'Engine Repair', 'AVAILABLE', 1500.00, '0300-9876543', 'ali.mechanic@email.com'),
                                                                                             (8, 'Hamza Malik', 'Brake Services', 'AVAILABLE', 1200.00, '0321-8765432', 'hamza.mechanic@email.com'),
                                                                                             (9, 'Bilal Qureshi', 'Electrical Systems', 'BUSY', 1800.00, '0333-7654321', 'bilal.mechanic@email.com'),
                                                                                             (10, 'Zain Abbas', 'Oil & Maintenance', 'AVAILABLE', 1000.00, '0345-6543210', 'zain.mechanic@email.com');

-- ============================================
-- 4. VEHICLES TABLE
-- ============================================
INSERT INTO vehicles (customer_id, license_plate, vehicle_type, make, model, year, last_service_date) VALUES
-- Ahmed Ali's vehicles
(1, 'ABC-123', 'Sedan', 'Toyota', 'Corolla', 2020, '2025-01-15'),
(1, 'XYZ-789', 'SUV', 'Honda', 'CR-V', 2019, '2024-12-20'),

-- Fatima Khan's vehicles
(2, 'DEF-456', 'Hatchback', 'Suzuki', 'Swift', 2021, '2025-01-10'),

-- Usman Ahmed's vehicles
(3, 'GHI-789', 'Sedan', 'Honda', 'Civic', 2022, '2025-01-20'),
(3, 'JKL-012', 'SUV', 'Toyota', 'Fortuner', 2023, '2025-01-18'),

-- Ayesha Siddiqui's vehicles
(4, 'MNO-345', 'Sedan', 'Hyundai', 'Elantra', 2020, '2024-12-25'),

-- Hassan Raza's vehicles
(5, 'PQR-678', 'Hatchback', 'Suzuki', 'Cultus', 2018, '2025-01-05'),
(5, 'STU-901', 'Van', 'Toyota', 'Hiace', 2019, '2024-12-30');

-- ============================================
-- 5. SERVICES TABLE
-- ============================================
INSERT INTO services (service_name, description, base_price, estimated_time, category) VALUES
                                                                                           ('Oil Change', 'Complete engine oil replacement with filter change', 2500.00, 45, 'Oil & Maintenance'),
                                                                                           ('Brake Inspection', 'Full brake system check and pad replacement if needed', 3500.00, 60, 'Brake Services'),
                                                                                           ('Engine Diagnostics', 'Comprehensive engine health check using diagnostic tools', 5000.00, 90, 'Engine Repair'),
                                                                                           ('AC Servicing', 'AC gas refill and cooling system maintenance', 4000.00, 75, 'AC & Cooling'),
                                                                                           ('Tire Rotation', 'Complete tire rotation and alignment check', 1500.00, 30, 'General Diagnostics'),
                                                                                           ('Battery Replacement', 'Battery health check and replacement', 8000.00, 30, 'Electrical Systems'),
                                                                                           ('Transmission Service', 'Transmission fluid change and system check', 6000.00, 120, 'General Diagnostics'),
                                                                                           ('Wheel Alignment', 'Four-wheel computerized alignment', 2000.00, 45, 'General Diagnostics'),
                                                                                           ('Headlight Restoration', 'Professional headlight cleaning and restoration', 1800.00, 40, 'Electrical Systems'),
                                                                                           ('Coolant Flush', 'Complete coolant system flush and refill', 2800.00, 50, 'AC & Cooling');

-- ============================================
-- 6. APPOINTMENTS TABLE
-- ============================================
INSERT INTO appointments (mechanic_id, customer_id, vehicle_id, service_id, scheduled_time, status, priority_level, estimated_completion, actual_completion, total_cost, discount_applied, notes) VALUES
-- Completed appointments
(1, 1, 1, 1, '2025-01-15 09:00:00', 'COMPLETED', 3, '2025-01-15 09:45:00', '2025-01-15 09:40:00', 2500.00, 0.00, 'Regular oil change, no issues found'),
(2, 2, 3, 2, '2025-01-18 10:00:00', 'COMPLETED', 2, '2025-01-18 11:00:00', '2025-01-18 11:15:00', 3500.00, 175.00, 'Brake pads replaced on front wheels'),
(4, 5, 7, 5, '2025-01-20 14:00:00', 'COMPLETED', 3, '2025-01-20 14:30:00', '2025-01-20 14:25:00', 1500.00, 0.00, 'Tire rotation completed successfully'),

-- In Progress appointments
(3, 3, 4, 3, '2025-01-25 11:00:00', 'IN_PROGRESS', 1, '2025-01-25 12:30:00', NULL, 5000.00, 0.00, 'Engine making unusual noise, diagnostic in progress'),
(1, 4, 6, 4, '2025-01-25 13:00:00', 'IN_PROGRESS', 2, '2025-01-25 14:15:00', NULL, 4000.00, 0.00, 'AC not cooling properly'),

-- Pending appointments
(2, 1, 2, 6, '2025-01-26 09:00:00', 'PENDING', 1, '2025-01-26 09:30:00', NULL, 8000.00, 0.00, 'Battery completely dead, needs urgent replacement'),
(4, 2, 3, 7, '2025-01-26 10:00:00', 'PENDING', 3, '2025-01-26 12:00:00', NULL, 6000.00, 0.00, 'Regular transmission service'),
(1, 3, 5, 8, '2025-01-27 11:00:00', 'PENDING', 3, '2025-01-27 11:45:00', NULL, 2000.00, 0.00, 'Car pulling to left side'),
(3, 5, 8, 10, '2025-01-27 14:00:00', 'PENDING', 3, '2025-01-27 14:50:00', NULL, 2800.00, 0.00, 'Scheduled coolant service'),

-- Cancelled appointment
(2, 4, 6, 9, '2025-01-22 15:00:00', 'CANCELLED', 3, '2025-01-22 15:40:00', NULL, 1800.00, 0.00, 'Customer cancelled - rescheduled for next week');

-- ============================================
-- 7. MECHANIC REQUESTS TABLE (Pending approvals)
-- ============================================
INSERT INTO mechanic_requests (user_id, name, email, specialization, experience, phone, hourly_rate, status) VALUES
-- Pending requests
(NULL, 'Kashif Ahmed', 'kashif.ahmed@email.com', 'Engine Repair', 8, '0300-1112233', 2000.00, 'PENDING'),
(NULL, 'Rizwan Ali', 'rizwan.ali@email.com', 'Brake Services', 5, '0321-2223344', 1300.00, 'PENDING'),
(NULL, 'Imran Shah', 'imran.shah@email.com', 'AC & Cooling', 6, '0333-3334455', 1600.00, 'PENDING'),

-- Approved request (already added to mechanics table)
(7, 'Ali Hassan', 'ali.mechanic@email.com', 'Engine Repair', 10, '0300-9876543', 1500.00, 'APPROVED'),

-- Rejected request
(NULL, 'Rejected Person', 'rejected@email.com', 'General Diagnostics', 2, '0345-5556677', 800.00, 'REJECTED');

-- ============================================
-- VERIFICATION QUERIES (Run these to check data)
-- ============================================

-- Check customers
SELECT * FROM customers;

-- Check users
SELECT * FROM users;

-- Check mechanics
SELECT * FROM mechanics;

-- Check vehicles per customer
SELECT c.name as customer, v.license_plate, v.make, v.model, v.year
FROM customers c
         JOIN vehicles v ON c.customer_id = v.customer_id
ORDER BY c.name;

-- Check services
SELECT * FROM services;

-- Check appointments with details
SELECT
    a.appointment_id,
    c.name as customer,
    v.license_plate,
    s.service_name,
    m.name as mechanic,
    a.status,
    a.scheduled_time,
    a.total_cost
FROM appointments a
         JOIN customers c ON a.customer_id = c.customer_id
         JOIN vehicles v ON a.vehicle_id = v.vehicle_id
         JOIN services s ON a.service_id = s.service_id
         JOIN mechanics m ON a.mechanic_id = m.mechanic_id
ORDER BY a.scheduled_time DESC;

-- Check mechanic requests
SELECT * FROM mechanic_requests ORDER BY created_at DESC;

-- ============================================
-- USEFUL LOGIN CREDENTIALS FOR TESTING
-- ============================================
/*
ADMIN:
Email: admin@carservice.com
Password: admin123

CUSTOMERS:
Email: ahmed.ali@email.com | Password: pass123
Email: fatima.khan@email.com | Password: pass123
Email: usman.ahmed@email.com | Password: pass123

MECHANICS:
Email: ali.mechanic@email.com | Password: mech123
Email: hamza.mechanic@email.com | Password: mech123
Email: bilal.mechanic@email.com | Password: mech123
Email: zain.mechanic@email.com | Password: mech123
*/