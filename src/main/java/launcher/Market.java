package launcher;

import com.fasterxml.jackson.core.type.TypeReference;
import comparators.ComparatorByBrand;
import comparators.ComparatorByPurpose;
import comparators.ComparatorByStringParameter;
import entity.Bicycle;
import entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import static api.Utils.*;
import static db.ReadFileSingleton.getInstance;
import static db.Rundb.saveToDb;

public class Market  {
    private static final Logger logger = LoggerFactory.getLogger(Market.class);
    public static void main(String[] args) throws SQLException {

        HashMap<Integer, Customer> customersList;
        HashMap<Integer, Bicycle> productsList;
        HashMap<Integer, HashSet<Integer>> orderBook;
        HashMap<Integer, HashSet<Integer>> buyerList;
        int productId, userId;

        /*Bicycle bicycle1 = new Bicycle(
                "Ukraina", "Dorozhnik", "Roadrunner", "without amortization",
                "drum", "steel", 20d, 26d, 150d, 1);
        productId = bicycle1.hashCode();
        productsList.put(productId, bicycle1);

        Bicycle bicycle2 = new Bicycle(
                "Ardis", "Titan", "usual", "hard tail",
                "disc", "aluminium", 19d, 27.5, 300d, 24);
        productId = bicycle2.hashCode();
        productsList.put(productId, bicycle2);

        Bicycle bicycle3 = new Bicycle("Corrado", "Kanio", "Mountain", "hard tail",
                "V-Brake", "aluminium", 20d, 26d, 200d, 21);
        productId = bicycle3.hashCode();
        productsList.put(productId, bicycle3);*/

       /* Customer user1 = new Customer(
                "Mykola Gagarin", "+380(50)345-50-12",
                new Address("Ukraine", "Kharkiv", "61000", "Gagarina", "100", "45b"), 560d);
        userId = user1.hashCode();
        customersList.put(userId, user1);

        Customer user2 = new Customer(
                "Alex Popov", "+380(67)234-67-87",
                new Address("Ukraine", "Kyiv", "10000", "Khreschatyk", "200", "44"), 3444d);
        userId = user2.hashCode();
        customersList.put(userId, user2);*/

        customersList = readJsonToMap(getInstance().getFileSingleton("src/main/resources/Customers.json"), new TypeReference<>() { });
        logger.info("Data from the file customers.json restored");
        productsList = readJsonToMap(getInstance().getFileSingleton("src/main/resources/Bicycles.json"), new TypeReference<>() { });
        logger.info("Data from the file bicycles.json restored");
        orderBook = readJsonToMap(getInstance().getFileSingleton("src/main/resources/Orders.json"), new TypeReference<>() { });
        logger.info("Data from the file orders.json restored");
        buyerList = readJsonToMap(getInstance().getFileSingleton("src/main/resources/Buyers.json"), new TypeReference<>() { });
        logger.info("Data from the file buyers.json restored");

        //sorting goods
        System.out.println("Сортировка велосипедов CompareTo по цене");
        productsList.values().stream().sorted().forEach(System.out::println);
        System.out.println();

        System.out.println("Сортировка велосипедов компаратором по назначению");
        productsList.values().stream().sorted(new ComparatorByPurpose()).forEach(System.out::println);
        System.out.println();

        System.out.println("Сортировка велосипедов компаратором по бренду");
        productsList.values().stream().sorted(new ComparatorByBrand()).forEach(System.out::println);
        System.out.println();

        System.out.println("Сортировка велосипедов компаратором по типу тормозов, ключ [ vd ]");
        productsList.values().stream().sorted(new ComparatorByStringParameter("vd", Bicycle::getBreak_type)).forEach(System.out::println);
        System.out.println();

        printBicycleByParameters(productsList, Bicycle::getBrand, Bicycle::getModel, Bicycle::getPrice);

        boolean stop = false;

        while (!stop) {
            if (systemMenu()) {
                System.out.print(" ");
            }

            Scanner in = new Scanner(System.in);
            int selector = in.nextInt();

            switch (selector) {
                case 1 -> displayItemsList(productsList);
                case 2 -> displayItemsList(customersList);
                case 3 -> {  //Buy a product by UserId and BicycleId
                    System.out.println("Please, input User ID and Product ID : ");
                    in = new Scanner(System.in);
                    userId = in.nextInt();
                    productId = in.nextInt();
                    Bicycle bicycle = productsList.get(productId);
                    Customer customer = customersList.get(userId);
                    Double wallet = customer.getMoneyAmount();
                    Double price = bicycle.getPrice();

                    if (customersList.containsKey(userId) && productsList.containsKey(productId) && wallet >= price) {

                        buyBicycle(orderBook, userId, productId);
                        buyBicycle(buyerList, productId, userId);
                        customer.setMoneyAmount(wallet - price);
                        System.out.println("Success");
                    } else {
                        System.out.println("Insufficient money or wrong input!");
                    }
                }
                case 4 -> // Display list of id of user's products by user id;

                        displayCorrespondingItemsById("Please, input User ID: ", orderBook);

                case 5 ->  // Display list of id of users that bought the bicycle by bicycle id

                        displayCorrespondingItemsById("Please, input Bicycle ID: ", buyerList);

                case 6 -> {// Add new user to the list
                    Customer newUser = addNewCustomer();
                    userId = newUser.hashCode();
                    customersList.put(userId, newUser);

                }
                case 7 -> {     //Add new product to the list
                    Bicycle newBicycle = addNewBicycle();
                    int bicycleId = newBicycle.hashCode();
                    productsList.put(bicycleId, newBicycle);
                }
                case 8 -> {                // Delete a user
                    System.out.print("Please enter corresponding Id: ");
                    in = new Scanner(System.in);
                    userId = in.nextInt();
                    deleteItemFromListById(customersList, userId);
                    deleteIdFromList(buyerList, userId);
                }
                case 9 -> {                  // Delete a product
                    System.out.print("Please enter Product's Id: ");
                    in = new Scanner(System.in);
                    productId = in.nextInt();
                    deleteItemFromListById(productsList, productId);
                    deleteIdFromList(orderBook, productId);
                }

                case 0 -> {
                    writeJsonFromMapToFile(getInstance().getFileSingleton("src/main/resources/Customers.json"), customersList);
                    writeJsonFromMapToFile(getInstance().getFileSingleton("src/main/resources/Bicycles.json"), productsList);
                    writeJsonFromMapToFile(getInstance().getFileSingleton("src/main/resources/Orders.json"), orderBook);
                    writeJsonFromMapToFile(getInstance().getFileSingleton("src/main/resources/Buyers.json"), buyerList);
                    if (saveToDb()) {
                    logger.info("Data saved to postgres db");}
                    stop = true;
                    in.close();
                }
            }
        }
    }

    @SafeVarargs
    public static void printBicycleByParameters(HashMap<Integer, Bicycle> list, Function<Bicycle, ?>... parameters ) {
        for (Function<Bicycle, ?> parameter : parameters) {

            String result = list.values().stream()
                    .map(parameter)
                    .map(String::valueOf)
                    .collect(Collectors.joining(", ", "[", "]"));
            System.out.println(result);
        }
        System.out.println();
    }
}


