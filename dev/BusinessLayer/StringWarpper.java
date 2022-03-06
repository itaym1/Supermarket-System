package BusinessLayer;

public class StringWarpper
{
    private String str;

    public StringWarpper(String str){
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    @Override
    public String toString() {
        return str;
    }
}
