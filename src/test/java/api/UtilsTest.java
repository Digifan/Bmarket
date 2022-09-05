package api;

import entity.Bicycle;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

import static api.Utils.*;
import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {
    Bicycle bicycle = new Bicycle (
            "Ukraina","Dorozhnik","Roadrunner","without amortization",
            "drum", "steel", 20d, 26d, 150d, 1 );
    int productId = bicycle.hashCode();

    @Test
    void isCorrectInputTest() {
        assertTrue(isCorrectInput(""));
    }
    @Test
    void systemMenuTest() {
        assertTrue(systemMenu());
    }
    /*@Test
    void addNewCustomerTest() {

    }

    @Test
    void addNewBicycle() {
    }
*/
    @Test
    void deleteItemFromListByIdTest() {
        HashMap<Integer, Bicycle> sampleList = new HashMap<>();
        sampleList.put(productId, bicycle);
        deleteItemFromListById(sampleList, productId);
        assertTrue(sampleList.isEmpty());
    }

    @Test
    void deleteIdFromListTest() {
        HashMap <Integer, HashSet<Integer>> recordList = new HashMap<>();
        HashSet <Integer> idList = new HashSet<>();
        idList.add(32); idList.add(3);
        recordList.put(1, idList );
        deleteIdFromList(recordList, 32);
        assertEquals(1, recordList.size());
    }

    @Test
    void buyBicycleTest() {
        HashMap <Integer, HashSet<Integer>> recordList = new HashMap<>();
        buyBicycle(recordList, 1, 5 );
        assertEquals(1, recordList.size());
    }
}