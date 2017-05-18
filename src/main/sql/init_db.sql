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
  defaultPrice FLOAT,
  defaultCurrency VARCHAR(40),
  description VARCHAR(150),
  productCategory INTEGER,
  supplier INTEGER,
  FOREIGN KEY (productCategory) REFERENCES ProductCategories (id),
  FOREIGN KEY (supplier) REFERENCES Suppliers(id)
);

CREATE TABLE IF NOT EXISTS PaymentMethods
(
  id SERIAL PRIMARY KEY,
  name VARCHAR(40) not null
);

CREATE TABLE IF NOT EXISTS Statuses
(
  id SERIAL PRIMARY KEY,
  name VARCHAR(40) not null
);

CREATE TABLE IF NOT EXISTS Orders
(
  id serial primary key,
  name text not null,
  email text not null,
  phoneNumber text not null,
  billingAddress text not null,
  shippingAddress text not null,
  description text not null,
  date date not null,
  userid int,
  paymentMethod int not null,
  status int not null,
  FOREIGN KEY (paymentMethod) REFERENCES PaymentMethods(id),
  FOREIGN KEY (status) REFERENCES Statuses(id)
);
