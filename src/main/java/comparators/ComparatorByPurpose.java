package comparators;

import entity.Bicycle;

import java.util.Comparator;

public class ComparatorByPurpose implements Comparator<Bicycle> {

    @Override
    public int compare(Bicycle b1, Bicycle b2) {
        return b1.getPurpose().compareTo(b2.getPurpose());
    }
}

