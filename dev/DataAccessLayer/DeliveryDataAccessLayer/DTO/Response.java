package DataAccessLayer.DeliveryDataAccessLayer.DTO;

public class Response<T> {
    T data;

    public Response(T data){
        this.data = data;

    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
