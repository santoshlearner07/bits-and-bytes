-- Active: 1711711801069@@127.0.0.1@3306@cafe
USE cafe;

CREATE Table signup (
    id INT AUTO_INCREMENT PRIMARY KEY, firstName VARCHAR(255), lastName VARCHAR(255), userName VARCHAR(255), address VARCHAR(255), roleBox VARCHAR(255)
);

INSERT INTO signup (`firstName`,`lastName`,`userName`,address,`roleBox`)
VALUES (
    "Santa","Nandiyawar","santa7","186 high street","Manager"
);

INSERT INTO signup (`firstName`,`lastName`,`userName`,address,`roleBox`)
VALUES (
    "XYZ","XYZ","qwe7","186 high street","Waiter"
);

SELECT * from signup;

CREATE TABLE restaurant_tables (
    table_id INT AUTO_INCREMENT PRIMARY KEY, seats INT, is_available BOOLEAN DEFAULT true
);

INSERT INTO
    restaurant_tables (seats)
VALUES (2),
    (2),
    (2),
    (2), -- Four tables of 2 seats
    (4),
    (4),
    (4),
    (4), -- Four tables of 4 seats
    (8),
    (8), -- Two tables of 8 seats
    (10);
-- One table of 10 seats
SELECT * FROM restaurant_tables;
-- WHERE seats = 4 AND is_available = 1;
-- CREATE Table
--name, time,date,tableid,
CREATE TABLE tableBookingInfo (
    table_id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100), tableSeat int, tblBkDt DATE, tblBkTime VARCHAR(20), tableStatus VARCHAR(50)
);

UPDATE tablebookinginfo SET 
tableStatus = "Completed" 
WHERE name="Sai"

SELECT * FROM tablebookinginfo;

SELECT COUNT(*) AS available_tables_count
FROM restaurant_tables
WHERE
    seats = 4
    AND is_available = 1;

-- -- Mark a table with table_id 1 as unavailable
UPDATE restaurant_tables SET is_available = false WHERE table_id = 1;
-- And to retrieve available tables for a given party size, you can use a query like this:

-- -- Find available tables for a party size of 6
SELECT *
FROM restaurant_tables
WHERE
    seats >= 6
    AND is_available = true;

CREATE TABLE staff (
    ID INT PRIMARY KEY, FirstName VARCHAR(50), LastName VARCHAR(50), HoursToWork INT, TotalHoursWorked INT, Role VARCHAR(50)
);

INSERT INTO
    staff (
        ID, FirstName, LastName, HoursToWork, TotalHoursWorked, Role
    )
VALUES (
        1, 'Sai', 'sree', 40, 35, 'Chef'
    ),
    (
        2, 'Gopi', 'B', 35, 30, 'Delivery Driver'
    ),
    (
        3, 'Divya', 'D', 30, 25, 'Waiter'
    ),
    (
        4, 'Santosh', 'N', 45, 40, 'Manager'
    );

-- Order Menu
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY, custName VARCHAR(100), item_name VARCHAR(100), item_price VARCHAR(20), status VARCHAR(100),foodPrepStatus VARCHAR(100)
);


SELECT * from orders;

-- DELETE FROM orders;