package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor

public class Bicycle implements Comparable<Bicycle> {

    //int id;
    String brand, model, purpose, type, break_type, frame_material;
    Double frame_size, wheel_size, price;
    int speed_number;


    @Override
    public int compareTo(@NonNull Bicycle bicycle) {
        return Double.compare(price, bicycle.getPrice());
    }



}