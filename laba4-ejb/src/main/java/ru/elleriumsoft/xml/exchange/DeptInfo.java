package ru.elleriumsoft.xml.exchange;

import ru.elleriumsoft.department.object.Employee;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dmitriy on 14.05.2017.
 */
@XmlType(propOrder = { "idDept", "nameDept", "parentIdDept", "employees" }, name = "deptinfo")
@XmlRootElement
public class DeptInfo implements Serializable
{
    private int idDept;
    private String nameDept;
    private int parentIdDept;
    private ArrayList<Employee> employees;

    public int getIdDept()
    {
        return idDept;
    }

    public void setIdDept(int idDept)
    {
        this.idDept = idDept;
    }

    public String getNameDept()
    {
        return nameDept;
    }

    public void setNameDept(String nameDept)
    {
        this.nameDept = nameDept;
    }

    public int getParentIdDept()
    {
        return parentIdDept;
    }

    public void setParentIdDept(int parentIdDept)
    {
        this.parentIdDept = parentIdDept;
    }

    public ArrayList<Employee> getEmployees()
    {
        return employees;
    }

    public void setEmployees(ArrayList<Employee> employees)
    {
        this.employees = employees;
    }
}
