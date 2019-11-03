import java.util.function.IntSupplier;

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
