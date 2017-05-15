DROP TABLE IF EXISTS Products;
DROP TABLE IF EXISTS ProductCategories;
DROP TABLE IF EXISTS Suppliers;

CREATE TABLE IF NOT EXISTS ProductCategories
(
  id SERIAL PRIMARY KEY,
  name VARCHAR(40),
  description VARCHAR(150),
  department VARCHAR(40)
);

CREATE TABLE IF NOT EXISTS Suppliers
(
  id SERIAL PRIMARY KEY,
  name VARCHAR(40),
  description VARCHAR(150)
);

CREATE TABLE IF NOT EXISTS Products
(
  id SERIAL PRIMARY KEY,
  name VARCHAR(40),
  description VARCHAR(150),
  defaultPrice FLOAT,
  defaultCurrency VARCHAR(40),
  productCategory INTEGER,
  supplier INTEGER,
  FOREIGN KEY (productCategory) REFERENCES ProductCategories (id),
  FOREIGN KEY (supplier) REFERENCES Suppliers(id)
);
