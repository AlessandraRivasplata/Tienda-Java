package model;

import main.Logable;
import dao.Dao;
import dao.DaoImplJDBC;

public class Employee extends Person implements Logable {
    private int employeeId;
    private String name; 
    private Dao dao;

    public Employee() {
        super();
        this.dao = new DaoImplJDBC();
    }

    @Override
    public boolean login(int user, String password) {
        dao.connect();
        Employee employee = dao.getEmployee(user, password);
        dao.disconnect();
        return employee != null;
    }

    // Gets y Sets
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
        
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

   
    
}
