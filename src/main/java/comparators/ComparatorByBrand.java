package comparators;

import entity.Bicycle;

import java.util.Comparator;

public class ComparatorByBrand implements Comparator <Bicycle> {

        @Override
        public int compare(Bicycle b1, Bicycle b2) {
            return b1.getBrand().compareToIgnoreCase(b2.getBrand());
        }
}

