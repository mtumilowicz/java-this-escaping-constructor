import lombok.Value;

/**
 * Created by mtumilowicz on 2018-11-02.
 */
@Value
class Customer {
    String name;

    public Customer(String name) {
        CustomerContainer.add(this);
        
        this.name = name;
    }
}
