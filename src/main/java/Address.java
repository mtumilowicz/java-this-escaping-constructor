import lombok.Value;

/**
 * Created by mtumilowicz on 2018-11-02.
 */
@Value
public class Address {
    String city;

    public Address(String city) {
        CustomerContainer.add(new Customer(this));
        
        this.city = city;
    }
}
