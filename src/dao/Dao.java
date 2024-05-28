package dao;

import model.Employee;

public interface Dao {
    void connect(); // No devuelve nada
    Employee getEmployee(int employeeId, String password); // Recibe employeeId y password, devuelve Employee
    void disconnect();
}
