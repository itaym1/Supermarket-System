package BusinessLayer.EmployeesBuisnessLayer;

import DataAccessLayer.EmployeesDataAccessLayer.DTOs.RoleDTO;

public class Role{
    private String name;
    private Integer licence = null;

    public Role(String _name) {name = _name;}

    public Role(String _name, Integer _licence) {
        name = _name;
        licence= _licence;
    }

    public String getName() {
        return name;
    }

    public Role clone() {
        return new Role(name);
    }

    //RoleDto to Role

    public Role(RoleDTO other){
        name = other.getName();
        licence = other.getLicense();
    }

    public boolean compare(String r2){
        return name.equals(r2);
    }

    public RoleDTO toDTO(){
        return new RoleDTO(name, licence);
    }

    public Integer getLicence() {
        return licence;
    }

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                '}';
    }
}
