import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by mtumilowicz on 2018-11-02.
 */
class CustomerContainer {
    private static final ConcurrentLinkedQueue<Customer> customers = new ConcurrentLinkedQueue<>();

    static void add(Customer customer) {
        customers.add(customer);
    }
    
    static Customer first() {
        return customers.peek();
    }
}
