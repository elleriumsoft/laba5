package ru.elleriumsoft.xml.exchange;

import ru.elleriumsoft.occupation.Occupation;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dmitriy on 14.05.2017.
 */
@XmlType(propOrder = { "withChildrenDept", "withEmployees", "withOccupations", "departments", "occupations" }, name = "exchange")
@XmlRootElement
public class Exchange implements Serializable
{
    private boolean withChildrenDept;
    private boolean withEmployees;
    private boolean withOccupations;

    private ArrayList<DeptInfo> departments;

    private ArrayList<Occupation> occupations;

    public boolean isWithChildrenDept()
    {
        return withChildrenDept;
    }

    public void setWithChildrenDept(boolean withChildrenDept)
    {
        this.withChildrenDept = withChildrenDept;
    }

    public boolean isWithEmployees()
    {
        return withEmployees;
    }

    public void setWithEmployees(boolean withEmployees)
    {
        this.withEmployees = withEmployees;
    }

    public boolean isWithOccupations()
    {
        return withOccupations;
    }

    public void setWithOccupations(boolean withOccupations)
    {
        this.withOccupations = withOccupations;
    }

    public ArrayList<DeptInfo> getDepartments()
    {
        return departments;
    }

    public void setDepartments(ArrayList<DeptInfo> departments)
    {
        this.departments = departments;
    }

    public ArrayList<Occupation> getOccupations()
    {
        return occupations;
    }

    public void setOccupations(ArrayList<Occupation> occupations)
    {
        this.occupations = occupations;
    }
}
