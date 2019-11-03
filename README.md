[![Build Status](https://travis-ci.com/mtumilowicz/java-this-escaping-constructor.svg?token=PwyvjePQ7aiAX51hSYLE&branch=master)](https://travis-ci.com/mtumilowicz/java-this-escaping-constructor)

# java-this-escaping-constructor
Simple example of this reference escaping during construction.

_Reference_: https://stackoverflow.com/questions/1588420/how-does-this-escape-the-constructor-in-java  
_Reference_: https://stackoverflow.com/questions/14790478/final-vs-volatile-guaranntee-w-rt-to-safe-publication-of-objects  
_Reference_: https://www.amazon.com/Java-Concurrency-Practice-Brian-Goetz/dp/0321349601  
_Reference_: https://www.ibm.com/developerworks/library/j-jtp0618/index.html  
_Reference_: https://www.youtube.com/watch?v=pS5dPQwgnYo

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
## explicit escape
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

### remark
Please note that we could write:
```
class Customer {
    String name;

    public Customer(String name) {
        this.name = name;
        CustomerContainer.add(this);
    }
}
```
adding to collection is the last statement in constructor - 
but it is also extremely dangerous - suppose inheritance:
```
class VIPCustomer extends Customer {
    // fields

    public VIPCustomer(String name) {
        super(name);
        // fields init

    }
}
```
Java language specification requires that a call to 
`super()` be the first statement in a subclass constructor, 
our not-yet-constructed `VIPCustomer` is already registered 
before we can finish the initialization of the 
subclass fields.

## implicit
* It is possible to create the escaped reference problem 
without using the `this` reference at all.
* Note that non-static inner classes maintain an implicit copy of the `this` reference of 
  their parent object, so creating an anonymous inner class 
  instance and passing it to an object visible from outside 
  the current thread has all the same risks as exposing the 
  `this` reference itself.
```
class ImplicitEscape {
    volatile static IntSupplier supplier;
    static ImplicitEscape escape = new ImplicitEscape();
    final int x;

    public ImplicitEscape() {
        supplier = () -> getX();
        x = 5;
    }

    int getX() {
        return x;
    }
}
```