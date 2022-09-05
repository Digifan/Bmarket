package db;

import com.fasterxml.jackson.core.type.TypeReference;
import connection.ConnectionPool;
import connection.ConnectionPoolImplementation;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static api.Utils.readJsonToMap;
import static db.DatabaseOperations.*;
import static db.ReadFileSingleton.getInstance;

public class Rundb {

    private static final String DB_NAME = "admin";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "admin";

    private static final String CONNECTION_URL = String.format(
            "jdbc:postgresql:%s", DB_NAME);
    public static boolean saveToDb () throws SQLException {
        ConnectionPool connectionPool = ConnectionPoolImplementation.create(CONNECTION_URL, DB_USER, DB_PASSWORD);
        Connection connection = connectionPool.getConnection();
        Statement statement = connection.createStatement();
        //Save Customers list to db
        createTable(statement, generateTableQuery(CUSTOMER_TABLE_NAME, createCustomerTableRequest()));
        fillCustomerTableData(statement, readJsonToMap(getInstance().
                getFileSingleton("src/main/resources/Customers.json"), new TypeReference<>() { }));
        System.out.println("Customers DB: ");
        browseData(statement.executeQuery(SELECT_FROM_CUSTOMER_QUERY), 6).forEach(System.out::println);
        System.out.println();
        //Save Bicycles list to db
        createTable(statement, generateTableQuery(BICYCLE_TABLE_NAME, createBicycleTableRequest()));
        fillBicycleTableData(statement, readJsonToMap(getInstance().
                getFileSingleton("src/main/resources/Bicycles.json"), new TypeReference<>() { }));
        System.out.println("Bicycles DB: ");
        browseData(statement.executeQuery(SELECT_FROM_BICYCLE_QUERY), 11).forEach(System.out::println);
        System.out.println();
        // Save order book to db
        System.out.println("Order book DB: ");
        createTable(statement, generateTableQuery(ORDERS_TABLE_NAME, createMapToTableRequest()));
        fillMapToTableData(connection, readJsonToMap(getInstance().
                getFileSingleton("src/main/resources/Orders.json"), new TypeReference<>() { }), ORDERS_TABLE_NAME);
        browseData(statement.executeQuery(SELECT_FROM_ORDERS_QUERY), 3).forEach(System.out::println);
        System.out.println();
        // Save buyers list of the bicycle id to db
        System.out.println("Buyers DB: ");
        createTable(statement, generateTableQuery(BUYERS_TABLE_NAME, createMapToTableRequest()));
        fillMapToTableData(connection, readJsonToMap(getInstance().
                getFileSingleton("src/main/resources/Buyers.json"), new TypeReference<>() { }), BUYERS_TABLE_NAME);
        browseData(statement.executeQuery(SELECT_FROM_BUYERS_QUERY), 3).forEach(System.out::println);
        System.out.println();
        return true;
    }
}
