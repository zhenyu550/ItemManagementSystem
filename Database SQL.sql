CREATE DATABASE IF NOT EXISTS itemdb;

CREATE TABLE CUSTOMER (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL UNIQUE,
  ic varchar(12) NOT NULL UNIQUE,
  contact_no varchar(20) NOT NULL,
  email varchar(100) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE EMPLOYEE (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL UNIQUE,
  ic varchar(12) NOT NULL UNIQUE,
  contact_no varchar(20) NOT NULL,
  email varchar(100) NOT NULL,
  username varchar(50) NOT NULL UNIQUE,
  password varchar(100) NOT NULL,
  level varchar(20) NOT NULL,
  address varchar(200) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE ITEMTYPE (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL UNIQUE,
  code varchar(50) NOT NULL UNIQUE,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE ITEM (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL UNIQUE,
  code varchar(50) NOT NULL UNIQUE,
  price decimal NOT NULL DEFAULT 0,
  quantity int(10) NOT NULL DEFAULT 0,
  description varchar(1000),
  type_id int(10) unsigned NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE TRANSACTION (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  code varchar(50) NOT NULL UNIQUE,
  employee_id int(10) unsigned NOT NULL,
  customer_id int(10) unsigned NOT NULL,
  total_price decimal NOT NULL DEFAULT 0,
  date_time datetime,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE TRANSACTIONITEM (
  transaction_id int(10) unsigned NOT NULL,
  item_id int(10) unsigned NOT NULL,
  amount int(10) unsigned DEFAULT 0,
  PRIMARY KEY (transaction_id, item_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
