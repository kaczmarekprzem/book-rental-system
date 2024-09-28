CREATE TABLE rented_book (
                             id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                             title VARCHAR(255) NOT NULL,
                             author VARCHAR(255) NOT NULL,
                             isbn VARCHAR(13) UNIQUE NOT NULL,
                             borrower VARCHAR(255)
);
