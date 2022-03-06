package serviceObjects;

public class ResponseT<T> extends Response{
    private final T value;

    public ResponseT(T _value, String msg) {
        super(msg);
        value = _value;
    }

    public ResponseT(T _value){
        super();
        value = _value;
    }

    public T getValue() {
        return value;
    }
}
