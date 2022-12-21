-- DROP TABLE Tbl_User;
-- DROP TABLE Tbl_Product;

CREATE TABLE IF NOT EXISTS Tbl_User
(
    `id`         BIGINT  NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`       VARCHAR NOT NULL COMMENT 'name',
    `password`   VARCHAR NOT NULL COMMENT 'password',
    `address`    VARCHAR COMMENT 'address',
    `bio`        VARCHAR COMMENT 'bio',
    `email`      VARCHAR NOT NULL COMMENT 'email',
    `mobile`     VARCHAR NOT NULL COMMENT 'mobile',
    `user_image` VARCHAR COMMENT 'image',
    `created_at`  VARCHAR COMMENT 'Profile created at',
    `updated_at`  VARCHAR COMMENT 'profile updated at',
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS Tbl_Product
(
    `product_id`       BIGINT  NOT NULL AUTO_INCREMENT COMMENT 'product ID',
    `product_name`     VARCHAR NOT NULL COMMENT 'Product Name',
    `product_image`    VARCHAR NOT NULL COMMENT 'Product Image',
    `product_type`     VARCHAR NOT NULL COMMENT 'Product Type',
    `user_id`          VARCHAR NOT NULL COMMENT 'Product added by user id',
    `price`            VARCHAR NOT NULL COMMENT 'Product Name',
    `price_type`       VARCHAR NOT NULL COMMENT 'Product price per type example -> price/type or price/gm or price/kg',
    `timestamp`        VARCHAR NOT NULL COMMENT 'product added time Format -> YYYY-MM-DD hh:mm:ss',
    `update_timestamp` VARCHAR NOT NULL COMMENT 'product update time Format -> YYYY-MM-DD hh:mm:ss',
    `min_buy`          VARCHAR NOT NULL COMMENT 'minimum buy by user',

    PRIMARY KEY (`product_id`)
);