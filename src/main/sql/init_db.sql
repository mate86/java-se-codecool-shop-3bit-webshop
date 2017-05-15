-- DROP TABLE IF EXISTS Products;
-- DROP TABLE IF EXISTS ProductCategories;
-- DROP TABLE IF EXISTS Suppliers;

CREATE TABLE ProductCategories
(
  id VARCHAR(8) PRIMARY KEY,
  name VARCHAR(40),
  description VARCHAR(150),
  department VARCHAR(40)
);

CREATE TABLE Suppliers
(
  id VARCHAR(8) PRIMARY KEY,
  name VARCHAR(40),
  description VARCHAR(150)
);

CREATE TABLE Products
(
  id VARCHAR(8) PRIMARY KEY,
  name VARCHAR(40),
  description VARCHAR(150),
  defaultPrice FLOAT,
  defaultCurrency VARCHAR(40),
  productCategory VARCHAR(8) REFERENCES ProductCategories (id),
  supplier VARCHAR(8) REFERENCES Suppliers(id)
);
