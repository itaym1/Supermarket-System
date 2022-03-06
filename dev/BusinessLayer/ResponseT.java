package BusinessLayer;

public class ResponseT<T> extends Response {

    public T value;

    public ResponseT(String msg){
        super(msg);
    }

    public ResponseT(T value){
        super();
        this.value = value;
    }

    public ResponseT(T value, String msg){
        super(msg);
        this.value = value;
    }
}
