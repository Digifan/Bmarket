package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Address {
    private String country;
    private String city;
    private String zip;
    private String street;
    private String houseNumber;
    private String apartment;

}
