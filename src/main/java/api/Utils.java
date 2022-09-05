package api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Address;
import entity.Bicycle;
import entity.Customer;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class Utils {
    public static boolean systemMenu() {
        System.out.println("SYSTEM MENU");
        System.out.println("1 - Display all products list");
        System.out.println("2 - Display all users list");
        System.out.println("3 - Buy a product");
        System.out.println("4 - Display list of user's products by user id");
        System.out.println("5 - Display list of users that bought a product by product id");
        System.out.println("6 - Add new user");
        System.out.println("7 - Add new product");
        System.out.println("8 - Delete a user");
        System.out.println("9 - Delete a product");
        System.out.println("0 - Exit");
        System.out.print("Please, make your choice:");
        return true;
    }

    static boolean isCorrectInput(String input) {
        return input.equals("");
    }

    public static <K, V> void displayItemsList(HashMap<K, V> list) {

        list.entrySet().forEach(System.out::println);
    }

    public static <V> void displayCorrespondingItemsById(String message, HashMap<Integer, V> itemsList) {
        System.out.print(message);
        Scanner in = new Scanner(System.in);
        Integer id = in.nextInt();
        if (itemsList.containsKey(id)) System.out.println(itemsList.get(id).toString());
        else System.out.println("null");
    }

    public static Customer addNewCustomer() {

        System.out.println("Please enter User's  First Name and Last Name");
        Scanner in = new Scanner(System.in);
        String fullName = in.nextLine();
        while (isCorrectInput(fullName) || fullName.equals("[0-9]")) {
            System.out.println("Wrong input! Try again : ");
            fullName = in.nextLine();
        }
        System.out.println("Please enter a phone number: ");
        String phone = in.nextLine();
        while (isCorrectInput(phone)) {
            System.out.println("Wrong input! Try again : ");
            phone = in.nextLine();
        }
        System.out.println("Please enter address: ");
        String address = in.nextLine();
        while (isCorrectInput(address)) {
            System.out.println("Wrong input! Try again : ");
            address = in.nextLine();
        }
        System.out.println("Please enter amount of money: ");
        Double moneyTotal = in.nextDouble();
        String[] splitAddress = address.split(" ");
        Address address_new = new Address(splitAddress[0], splitAddress[1], splitAddress[2], splitAddress[3],
                splitAddress[4], splitAddress[5]);
        return new Customer(fullName, phone, address_new, moneyTotal);
    }

    public static Bicycle addNewBicycle() {
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter a bicycle data by the space separated : " +
                "brand, model, purpose, type, break_type, frame_material");
        String bicycleStringFields = in.nextLine();
        while (isCorrectInput(bicycleStringFields)) {
            System.out.println("Wrong input! Try again : ");
            bicycleStringFields = in.nextLine();
        }
        System.out.println("Please enter a bicycle frame_size, wheel_size and price : ");
        Double frame_size = in.nextDouble();
        Double wheel_size = in.nextDouble();
        Double price = in.nextDouble();
        System.out.println("Please enter speed number: ");
        int speed_number = in.nextInt();
        String[] bicycleFields = bicycleStringFields.split(" ");

        return new Bicycle(bicycleFields[0], bicycleFields[1], bicycleFields[2], bicycleFields[3], bicycleFields[4],
                bicycleFields[5], frame_size, wheel_size, price, speed_number);

    }

    public static <V> void deleteItemFromListById(@NonNull HashMap<Integer, V> recordList, Integer id) {

        if (recordList.containsKey(id)) {
            recordList.remove(id);
            System.out.println("Success!");
        } else {
            System.out.println("Wrong input!");
        }
    }

    public static void deleteIdFromList(@NonNull HashMap<Integer, HashSet<Integer>> recordList, Integer id) {
        for (Map.Entry<Integer, HashSet<Integer>> item : recordList.entrySet()) {
            item.getValue().remove(id);
        }
    }

    public static void buyBicycle(@NonNull HashMap<Integer, HashSet<Integer>> recordList,
                                  Integer key_id, Integer value_id) {
        if (!recordList.containsKey(key_id)) {
            HashSet<Integer> list = new HashSet<>();
            list.add(value_id);
            recordList.put(key_id, list);
        } else {
            recordList.get(key_id).add(value_id);
        }
    }

    public static <K, V> void writeJsonFromMapToFile(File file, HashMap<K, V> list) {
        try {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, list);}
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public static <T> T readJsonToMap(File file, TypeReference<T> typeRef) {
        ObjectMapper mapper = new ObjectMapper();
        T map = null;
        try {
            map = mapper.readValue(file, typeRef);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}


