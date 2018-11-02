# java-this-escaping-constructor
Simple example of this reference escaping during construction.

_Reference_: https://stackoverflow.com/questions/1588420/how-does-this-escape-the-constructor-in-java  
_Reference_: https://stackoverflow.com/questions/14790478/final-vs-volatile-guaranntee-w-rt-to-safe-publication-of-objects  
_Reference_: https://www.amazon.com/Java-Concurrency-Practice-Brian-Goetz/dp/0321349601

# preface
An object is immutable if:
* its state cannot be modified after construction,
* all its fields are final,
* it is properly constructed (the this reference does not escape during 
construction).

To publish an object safely, both the reference to the object and the 
object's state must be made visible to other threads at the same time. 
A properly constructed object can be safely published by: 
* initializing an object reference from a static initializer,
* storing a reference to it into a volatile field or AtomicReference,
* storing a reference to it into a final field of a properly constructed 
object,
* storing a reference to it into a field that is properly guarded by a lock.

# project description
We provide simple and real-life example of `this` escaping during 
construction. **It is definitely not a proper way of programming!**

* `CustomerContainer`
    ```
    class CustomerContainer {
        private static final ConcurrentLinkedQueue<Customer> customers = new ConcurrentLinkedQueue<>();
    
        static void add(Customer customer) {
            customers.add(customer);
        }
    }
    ```
* `Customer`
    ```
    class Customer {
        String name;
    
        public Customer(String name) {
            CustomerContainer.add(this);
            this.name = name;
        }
    }
    ```
    _Remark_: we immediately add to `CustomerContainer` in constructor
    **before** setting the field.
    
So if other threads have access to `CustomerContainer` (one of 
many possible flows):
1. **thread A**: customer1 is added to container,
1. **thread B**: customer1 is retrieved from container,
1. **thread B**: local cache customer1,
1. **thread A**: name of customer1 is initialized,
1. customer1 is fully created and **thread B** has incoherent version.