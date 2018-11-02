import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by mtumilowicz on 2018-11-02.
 */
public class CustomerTest {
    
    @Test
    public void check_if_customer_is_added_to_container_during_construction() {
//        when
        new Customer("test");
        
//        then
        Customer first = CustomerContainer.first();
        assertThat(first.getName(), is("test"));
    }
    

}