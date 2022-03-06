package DataAccessLayer.EmployeesDataAccessLayer.DTOs;

public class RoleDTO {
    private String name;
    private Integer license = null;

    public RoleDTO(String name, Integer license){
        this.name = name;
        this.license= license;
    }

    public RoleDTO(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public Integer getLicense() {
        return license;
    }

    @Override
    public String toString() {
        return "RoleDTO{" +
                "name='" + name + '\'' +
                ", license='" + license + '\'' +
                '}';
    }
}
