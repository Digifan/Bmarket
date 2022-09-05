package entity;

import lombok.*;

import static java.lang.CharSequence.compare;

@Data
@AllArgsConstructor
@RequiredArgsConstructor

public class Customer implements Comparable <Customer> {

    //int id;
    String full_name;
    String phone;
    Address address;
    Double moneyAmount;
    @Override
    public int compareTo( @NonNull Customer customer) {
        return compare(full_name, customer.getFull_name()) ;

    }
}
