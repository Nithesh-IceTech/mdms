package za.co.spsi.toolkit.util;

public class Container<E> {
    private E value;

    public boolean isPresent() {return value != null;}

    public Container<E> set(E value) {
        this.value = value;
        return this;
    }

    public E get() {
        return value;
    }


}
