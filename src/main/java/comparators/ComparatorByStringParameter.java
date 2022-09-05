package comparators;

import entity.Bicycle;

import java.util.Comparator;
import java.util.function.Function;

public record ComparatorByStringParameter(String sortKey, Function<Bicycle, String> bicycleMethod) implements Comparator<Bicycle> {

    @Override
    public int compare(Bicycle b1, Bicycle b2) {
        String o1 = bicycleMethod.apply(b1);
        String o2 = bicycleMethod.apply(b2);
        int pos1 = 0;
        int pos2 = 0;

        for (int i = 0; i < Math.min(o1.length(), o2.length()) && pos1 == pos2; i++) {
            pos1 = sortKey.indexOf(o1.charAt(i));
            pos2 = sortKey.indexOf(o2.charAt(i));
        }

        if (pos1 == pos2 && o1.length() != o2.length()) {
            return o1.length() - o2.length();
        }

        return pos1 - pos2;
    }
}
