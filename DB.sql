﻿
create table OGN_USER
(
    USERID       VARCHAR2(36)   not null
        primary key,
    USERNAME     NVARCHAR2(50)  not null,
    PASSWORDHASH NVARCHAR2(255) not null,
    EMAIL        NVARCHAR2(100) not null
        unique,
    CREATEDDATE  TIMESTAMP(6) default CURRENT_TIMESTAMP,
    ROLE         VARCHAR2(255),
    PHONE        VARCHAR2(15)

create table OGN_CATEGORY
(
    CATEGORYID     NUMBER generated as identity
        primary key,
    CATEGORYNAME   NVARCHAR2(100) not null,
    CATEGORYIMAGES NVARCHAR2(2000),
    STATUS         NVARCHAR2(50)
)
/

create table OGN_PRODUCT
(
    PRODUCTID   NUMBER generated as identity
        primary key,
    CATEGORYID  NUMBER
        references OGN_CATEGORY,
    PRODUCTNAME NVARCHAR2(100) not null,
    SHORTDESC   NVARCHAR2(500),
    DESCRIPTION NVARCHAR2(2000),
    PRICE       NUMBER(18, 2)  not null,
    STOCK       NUMBER         not null,
    UNIT        NVARCHAR2(50),
    IMAGE       NVARCHAR2(255),
    LISTIMAGE   CLOB
)
/

create table OGN_ORDERS
(
    ORDERID   NUMBER       default "SYSTEM"."ISEQ$$_80729".nextval generated as identity
		primary key,
    USERID    VARCHAR2(36)
        references OGN_USER,
    ORDERDATE TIMESTAMP(6) default CURRENT_TIMESTAMP,
    STATUS    NVARCHAR2(50)
)
/

create table OGN_ORDERDETAILS
(
    ORDERDETAILID NUMBER generated as identity
        primary key,
    ORDERID       NUMBER
        references OGN_ORDERS,
    PRODUCTID     NUMBER
        references OGN_PRODUCT,
    QUANTITY      NUMBER        not null,
    PRICE         NUMBER(18, 2) not null
)
/

create table OGN_SALEPRICE
(
    SALEID         NUMBER generated as identity
        primary key,
    PRODUCTID      NUMBER
        constraint UNIQUE_PRODUCTID
            unique
        references OGN_PRODUCT,
    SALEPERCENTAGE NUMBER(5, 2) not null,
    STARTDATE      DATE         not null,
    ENDDATE        DATE         not null,
    CATEGORYID     NUMBER
        constraint FK_CATEGORYID
            references OGN_CATEGORY
)
/

create table OGN_CART
(
    CARTID      NUMBER       default "SYSTEM"."ISEQ$$_80738".nextval generated as identity
		primary key,
    USERID      VARCHAR2(36)
        references OGN_USER,
    PRODUCTID   NUMBER
        references OGN_PRODUCT,
    QUANTITY    NUMBER not null,
    CREATEDDATE TIMESTAMP(6) default CURRENT_TIMESTAMP
)
/

create table OGN_WISHLIST
(
    WISHLISTID NUMBER       default "SYSTEM"."ISEQ$$_80741".nextval generated as identity
		primary key,
    USERID     VARCHAR2(36)
        references OGN_USER,
    PRODUCTID  NUMBER
        references OGN_PRODUCT,
    ADDEDDATE  TIMESTAMP(6) default CURRENT_TIMESTAMP
)
/

create table OGN_STARRATING
(
    RATINGID   NUMBER       default "SYSTEM"."ISEQ$$_80744".nextval generated as identity
		primary key,
    PRODUCTID  NUMBER
        references OGN_PRODUCT,
    USERID     VARCHAR2(36)
        references OGN_USER,
    RATING     NUMBER
        check (Rating BETWEEN 1 AND 5),
    REVIEW     VARCHAR2(1000),
    RATINGDATE TIMESTAMP(6) default CURRENT_TIMESTAMP,
    STATUS     NVARCHAR2(50)
)
/

create table OGN_ADMIN
(
    ADMINID NUMBER generated as identity
        primary key,
    USERID  VARCHAR2(36)
        references OGN_USER,
    ROLE    NVARCHAR2(50) not null
)
/

create table OGN_SLIDE
(
    SLIDEID  NUMBER generated as identity
        primary key,
    IMAGEURL NVARCHAR2(255) not null,
    DESCR    VARCHAR2(255),
    STATUS   VARCHAR2(20)
)
/

create table OGN_BILLING_DETAILS
(
    BILLINGID      NUMBER generated as identity
        primary key,
    NAME           NVARCHAR2(100) not null,
    STREETADDRESS  NVARCHAR2(255) not null,
    CITY           NVARCHAR2(100) not null,
    PHONE          NVARCHAR2(20),
    DISTRICT       VARCHAR2(100),
    WARD           VARCHAR2(100),
    SHIPPING_FEE   NUMBER,
    PAYMENT_METHOD VARCHAR2(50),
    USERID         VARCHAR2(36)
        constraint FK_USERID_BILLING
            references OGN_USER,
    TOTALPRICE     NUMBER,
    ORDERID        NUMBER
        constraint FK_ORDER_BILLING
            references OGN_ORDERS
)
/

create table OGN_BLOG
(
    ID            NUMBER       default "SYSTEM"."ISEQ$$_81000".nextval generated by default on null as identity
		primary key,
    BLOGNAME      VARCHAR2(255) not null,
    IMAGE         VARCHAR2(255),
    BLOGDATE      TIMESTAMP(6) default CURRENT_TIMESTAMP,
    BLOGSHORTDESC VARCHAR2(500),
    BLOGDESC      CLOB,
    TAG           VARCHAR2(255)
)
/

create table PASSWORD_RESET_TOKENS
(
    ID         NUMBER       default "SYSTEM"."ISEQ$$_81816".nextval generated as identity
		primary key,
    EMAIL      VARCHAR2(255) not null,
    TOKEN      VARCHAR2(255) not null,
    CREATED_AT TIMESTAMP(6) default CURRENT_TIMESTAMP,
    EXPIRES_AT TIMESTAMP(6)

)

-- Bảng OGN_USER
INSERT INTO OGN_USER (USERID, USERNAME, PASSWORDHASH, EMAIL, ROLE, PHONE) VALUES
('a8a53d70-f1b1-4234-9f70-123456789abc', 'john_doe', 'hashed_password_123', 'john@example.com', 'USER', '0123456789');
INSERT INTO OGN_USER (USERID, USERNAME, PASSWORDHASH, EMAIL, ROLE, PHONE) VALUES
('b2c52f81-c1d3-4f5e-9b8d-987654321def', 'jane_doe', 'hashed_password_456', 'jane@example.com', 'ADMIN', '0987654321');
INSERT INTO OGN_USER (USERID, USERNAME, PASSWORDHASH, EMAIL, ROLE, PHONE) VALUES
('c7d93e92-d2e4-5g6f-1h9i-123456789ghi', 'mike_smith', 'hashed_password_789', 'mike@example.com', 'USER', '0112233445');

-- Bảng OGN_CATEGORY
INSERT INTO OGN_CATEGORY (CATEGORYID, CATEGORYNAME, CATEGORYIMAGES, STATUS) VALUES
(1, 'Electronics', 'image_electronics.jpg', 'Home');
INSERT INTO OGN_CATEGORY (CATEGORYID, CATEGORYNAME, CATEGORYIMAGES, STATUS) VALUES
(2, 'Books', 'image_books.jpg', 'Home');
INSERT INTO OGN_CATEGORY (CATEGORYID, CATEGORYNAME, CATEGORYIMAGES, STATUS) VALUES
(3, 'Clothing', 'image_clothing.jpg', 'Hidden');

-- Bảng OGN_PRODUCT
INSERT INTO OGN_PRODUCT (PRODUCTID, CATEGORYID, PRODUCTNAME, SHORTDESC, DESCRIPTION, PRICE, STOCK, UNIT, IMAGE) VALUES
(1, 1, 'Smartphone', 'Latest model', 'A high-quality smartphone with 128GB storage.', 799.99, 50, 'pcs', 'smartphone.jpg');
INSERT INTO OGN_PRODUCT (PRODUCTID, CATEGORYID, PRODUCTNAME, SHORTDESC, DESCRIPTION, PRICE, STOCK, UNIT, IMAGE) VALUES
(2, 1, 'Laptop', 'Lightweight and powerful', '14-inch laptop with Intel i7.', 1199.99, 30, 'pcs', 'laptop.jpg');
INSERT INTO OGN_PRODUCT (PRODUCTID, CATEGORYID, PRODUCTNAME, SHORTDESC, DESCRIPTION, PRICE, STOCK, UNIT, IMAGE) VALUES
(3, 2, 'Fiction Book', 'Bestselling novel', 'A must-read fiction book of 2024.', 19.99, 100, 'pcs', 'book.jpg');

-- Bảng OGN_ORDERS
INSERT INTO OGN_ORDERS (ORDERID, USERID, STATUS) VALUES
(1, 'a8a53d70-f1b1-4234-9f70-123456789abc', N'Đang xử lý');
INSERT INTO OGN_ORDERS (ORDERID, USERID, STATUS) VALUES
(2, 'b2c52f81-c1d3-4f5e-9b8d-987654321def', N'Đã hủy');
INSERT INTO OGN_ORDERS (ORDERID, USERID, STATUS) VALUES
(3, 'c7d93e92-d2e4-5g6f-1h9i-123456789ghi', N'Hoàn thành');

-- Bảng OGN_ORDERDETAILS
INSERT INTO OGN_ORDERDETAILS (DETAILID, ORDERID, PRODUCTID, QUANTITY, PRICE) VALUES
(1, 1, 1, 2, 1599.98);
INSERT INTO OGN_ORDERDETAILS (DETAILID, ORDERID, PRODUCTID, QUANTITY, PRICE) VALUES
(2, 1, 2, 1, 1199.99);
INSERT INTO OGN_ORDERDETAILS (DETAILID, ORDERID, PRODUCTID, QUANTITY, PRICE) VALUES
(3, 2, 3, 3, 59.97);

-- Bảng OGN_CART
INSERT INTO OGN_CART (CARTID, USERID, PRODUCTID, QUANTITY) VALUES
(1, 'a8a53d70-f1b1-4234-9f70-123456789abc', 1, 1);
INSERT INTO OGN_CART (CARTID, USERID, PRODUCTID, QUANTITY) VALUES
(2, 'b2c52f81-c1d3-4f5e-9b8d-987654321def', 2, 2);
INSERT INTO OGN_CART (CARTID, USERID, PRODUCTID, QUANTITY) VALUES
(3, 'c7d93e92-d2e4-5g6f-1h9i-123456789ghi', 3, 3);

-- Bảng OGN_STARRATING
INSERT INTO OGN_STARRATING (RATINGID, PRODUCTID, USERID, RATING, REVIEW, STATUS) VALUES
(1, 1, 'a8a53d70-f1b1-4234-9f70-123456789abc', 5, 'Excellent product!', 'Home');
INSERT INTO OGN_STARRATING (RATINGID, PRODUCTID, USERID, RATING, REVIEW, STATUS) VALUES
(2, 2, 'b2c52f81-c1d3-4f5e-9b8d-987654321def', 4, 'Good value for money.', 'Hidden');
INSERT INTO OGN_STARRATING (RATINGID, PRODUCTID, USERID, RATING, REVIEW, STATUS) VALUES
(3, 3, 'c7d93e92-d2e4-5g6f-1h9i-123456789ghi', 3, 'Average quality.', 'Home');

-- Bảng OGN_BILLING_DETAILS
INSERT INTO OGN_BILLING_DETAILS (BILLID, NAME, STREETADDRESS, CITY, PHONE, DISTRICT, WARD, SHIPPING_FEE, PAYMENT_METHOD, USERID, TOTALPRICE, ORDERID) VALUES
(1, 'John Doe', '123 Main St', 'New York', '0123456789', 'Manhattan', 'Ward A', 5.99, 'Credit Card', 'a8a53d70-f1b1-4234-9f70-123456789abc', 1605.97, 1);
INSERT INTO OGN_BILLING_DETAILS (BILLID, NAME, STREETADDRESS, CITY, PHONE, DISTRICT, WARD, SHIPPING_FEE, PAYMENT_METHOD, USERID, TOTALPRICE, ORDERID) VALUES
(2, 'Jane Smith', '456 Elm St', 'Los Angeles', '0987654321', 'Hollywood', 'Ward B', 7.99, 'PayPal', 'b2c52f81-c1d3-4f5e-9b8d-987654321def', 59.97, 2);

-- Thêm bài viết đầu tiên
INSERT INTO OGN_BLOG (BLOGNAME, IMAGE, BLOGSHORTDESC, BLOGDESC, TAG) 
VALUES (
    'Hướng dẫn sử dụng Oracle SQL', 
    'images/oracle_sql_guide.jpg', 
    'Một hướng dẫn cơ bản về Oracle SQL dành cho người mới bắt đầu.', 
    'Oracle SQL là một công cụ mạnh mẽ để quản lý và truy vấn cơ sở dữ liệu. Trong bài viết này, chúng ta sẽ tìm hiểu các lệnh cơ bản và một số mẹo hay để làm việc với Oracle SQL.', 
    'Oracle'
);

-- Thêm bài viết thứ hai
INSERT INTO OGN_BLOG (BLOGNAME, IMAGE, BLOGSHORTDESC, BLOGDESC, TAG) 
VALUES (
    'Tối ưu hóa cơ sở dữ liệu',
    'images/db_optimization_tips.jpg',
    'Những mẹo và kỹ thuật để tối ưu hóa hiệu suất cơ sở dữ liệu.',
    'Hiệu suất cơ sở dữ liệu là một yếu tố quan trọng trong các hệ thống lớn. Bài viết này sẽ chia sẻ các cách tối ưu hóa cơ sở dữ liệu, từ thiết kế bảng, lập chỉ mục, đến tối ưu câu lệnh SQL.',
    'Database'
);

-- Thêm bài viết thứ ba
INSERT INTO OGN_BLOG (BLOGNAME, IMAGE, BLOGSHORTDESC, BLOGDESC, TAG) 
VALUES (
    'Cách triển khai ứng dụng Spring Boot',
    'images/spring_boot_deployment.jpg',
    'Hướng dẫn từng bước triển khai ứng dụng Spring Boot lên server.',
    'Spring Boot giúp phát triển ứng dụng nhanh chóng và dễ dàng. Trong bài viết này, bạn sẽ học cách cấu hình và triển khai ứng dụng Spring Boot trên các server như Tomcat hoặc Cloud.',
    'Spring Boot'
);
