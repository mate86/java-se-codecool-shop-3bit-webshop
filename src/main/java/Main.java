import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

import com.codecool.shop.controller.*;
import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.model.*;

import spark.Request;
import spark.Response;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.sql.*;

public class Main {

    public static void main(String[] args) {

        // default server settings
        exception(Exception.class, (e, req, res) -> e.printStackTrace());
        staticFileLocation("/public");
        port(8888);

        // populate some data for the memory storage
        initDatabase();

        // Always start with more specific routes
        get("/hello", (req, res) -> "Hello World");

        // Always add generic routes to the end
        get("/", ProductController::renderProducts, new ThymeleafTemplateEngine());

        // Equivalent with above
        get("/index", (Request req, Response res) -> {
            return new ThymeleafTemplateEngine().render(ProductController.renderProducts(req, res));
        });

        //CART ROUTES
        get("/cart", CartController::renderProducts, new ThymeleafTemplateEngine());
        get("/cart/add/:id", CartController::addProduct);
        post("/cart/modify/:id", CartController::modifyProduct, new ThymeleafTemplateEngine());
        post("/cart/remove/:id", CartController::removeProduct, new ThymeleafTemplateEngine());

        //CHECKOUT ROUTES
        get("/order/checkout", OrderController::checkout, new ThymeleafTemplateEngine());
        get("/order/payment", OrderController::payment, new ThymeleafTemplateEngine());
        get("/order/confirmation", OrderController::confirmation, new ThymeleafTemplateEngine());

        // Add this line to your project to enable the debug screen
        enableDebugScreen();
    }

    public static void initDatabase() {
        String supplierQuery = "SELECT * FROM products;";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(supplierQuery);
        ) {
            if (!resultSet.next()) {
                populateData();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void populateData() {
        //setting up a new suppliers
//        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();
        SupplierDao supplierDataStore = SupplierDaoJdbc.getInstance();
        Supplier amazon = new Supplier("Amazon", "Digital content and services");
        Supplier lenovo = new Supplier("Lenovo", "Computers");
        Supplier nokia = new Supplier("Nokia", "Connecting people");
        Supplier westernelectric = new Supplier("Western Electric", "Century of progress");
        Supplier tinkertom = new Supplier("Tinker Tom", "The inventor");
        Supplier funfactory = new Supplier("Fun factory", "All you really need");
        supplierDataStore.add(amazon);
        supplierDataStore.add(lenovo);
        supplierDataStore.add(nokia);
        supplierDataStore.add(westernelectric);
        supplierDataStore.add(tinkertom);
        supplierDataStore.add(funfactory);

        //setting up a new product categories
//        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJdbc.getInstance();
        ProductCategory tablet = new ProductCategory("Tablet", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
        ProductCategory phone = new ProductCategory("Phone", "Hardware", "A phone.");
        ProductCategory gift = new ProductCategory("Gifts", "Accessories", "Fun stuff.");
        productCategoryDataStore.add(tablet);
        productCategoryDataStore.add(phone);
        productCategoryDataStore.add(gift);

        //setting up products and printing it
//        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductDao productDataStore = ProductDaoJdbc.getInstance();
        productDataStore.add(new Product("Amazon Fire", 49.9f, "USD", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", tablet, amazon));
        productDataStore.add(new Product("Lenovo IdeaPad Miix 700", 479, "USD", "Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.", tablet, lenovo));
        productDataStore.add(new Product("Amazon Fire HD 8", 89, "USD", "Amazon's latest Fire HD 8 tablet is a great value for media consumption.", tablet, amazon));
        productDataStore.add(new Product("Nokia 3410", 50, "USD", "Nokia's legend.", phone, nokia));
        productDataStore.add(new Product("Model 500", 20, "USD", "Phone of the century.", phone, westernelectric));
        productDataStore.add(new Product("Wirephone", 5, "USD", "For minimalists.", phone, tinkertom));
        productDataStore.add(new Product("Rubber duck", 5, "USD", "Necessity.", gift, funfactory));
        productDataStore.add(new Product("Towel", 5, "USD", "Never forget your towel.", gift, funfactory));
    }
}
