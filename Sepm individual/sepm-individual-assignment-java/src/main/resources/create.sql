DROP TABLE IF EXISTS Food;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS OrderedProducts;

CREATE TABLE Food (
  pid INTEGER AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  nettoprice INTEGER NOT NULL,
  timeOfCreation TIMESTAMP NOT NULL,
  category VARCHAR (255) NOT NULL,
  tax VARCHAR (255) NOT NULL,
  edit_date TIMESTAMP NOT NULL,
  isProductDeleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE Orders (
  orderId INTEGER AUTO_INCREMENT PRIMARY KEY,
  tableNumber INTEGER NOT NULL,
  timeOfCreation TIMESTAMP NOT NULL,
  status VARCHAR(20) DEFAULT 'open' CHECK (status IN ('open', 'billed')),
  billDate TIMESTAMP,
  payMethod VARCHAR (20),
  billNumber INTEGER DEFAULT NULL
);

CREATE TABLE OrderedProducts (
  id INTEGER  AUTO_INCREMENT,
  pid INTEGER REFERENCES Food(pid),
  orderId INTEGER REFERENCES Orders(orderId),
  amount INTEGER,
  name VARCHAR(48) NOT NULL,
  bruttoPrice FLOAT NOT NULL
);



INSERT INTO Food (name,description,nettoprice,timeOfCreation,category,tax,edit_date) VALUES
  ('Wiener Snitzel','Mit kartofel',10,'2015-05-05 15:15:15' ,'Main dish','20%','2018-10-5 14:11:28'),
  ('Pommes', 'Mit ketchup',4,'2016-06-06 16:16:16' ,'Starter','20%','2018-12-6 16:15:11'),
  ('Hamburger','Mit pommes und salat',10,'2017-07-07 17:17:17' ,'Main dish','10%','2018-10-5 11:15:12'),
  ('Chickenburger', 'Mit ketchup,mayo,salat',4,'2018-08-08 18:18:18' ,'Starter','20%','2018-12-12 15:12:16'),
   ('Salat', null,2,'2018-08-08 11:11:18' ,'Starter','10%','2018-12-12 11:12:11');



INSERT INTO Orders (orderId,tableNumber, timeOfCreation, status,billDate,payMethod,billNumber) VALUES
(1,1, '2012-05-05 12:13:15', 'open',null,null,null),
(2,2, '2017-05-05 17:12:05', 'open',null,null,null),
(3,3, '2016-05-05 15:12:03', 'open',null,null,null),
(4,4, '2014-05-05 11:11:09', 'open',null,null,null),
(5,5, '2015-05-05 12:14:15', 'open',null,null,null),
(6,6, '2015-01-01 11:14:15','billed','2015-01-01 11:14:15','card',1);

INSERT INTO OrderedProducts (id,pid, orderId, amount, name, bruttoPrice) VALUES
(1,1,1,2,'Wiener Snitzel',11.0),
(2,2,2,3,'Pommes',4.8),
(3,3,3,4,'Hamburger',11.0),
(4,4,4,5,'Chickenburger',4.8),
(5,5,6,2,'Salat',2.2),
(6,4,5,5,'Chickenburger',4.8);