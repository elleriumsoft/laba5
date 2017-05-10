package ru.elleriumsoft.department.object;

import ru.elleriumsoft.occupation.object.Occupation;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dmitriy on 05.05.2017.
 */
@XmlType(propOrder = { "idDepartment", "nameDepartment", "commandForModification", "idForModification", "positionForModification", "employeeOfDepartment", "occupations" }, name = "dept")
@XmlRootElement
public class AllDepartments implements Serializable
{
    private Integer idDepartment;
    private String nameDepartment;
    private String commandForModification;
    private Integer idForModification;
    private Integer positionForModification;
    private ArrayList<Department> employeeOfDepartment;
    private ArrayList<Occupation> occupations;

    public Integer getIdDepartment()
    {
        return idDepartment;
    }

    public void setIdDepartment(Integer idDepartment)
    {
        this.idDepartment = idDepartment;
    }

    public String getNameDepartment()
    {
        return nameDepartment;
    }

    public void setNameDepartment(String nameDepartment)
    {
        this.nameDepartment = nameDepartment;
    }

    public String getCommandForModification()
    {
        return commandForModification;
    }

    public void setCommandForModification(String commandForModification)
    {
        this.commandForModification = commandForModification;
    }

    public Integer getIdForModification()
    {
        return idForModification;
    }

    public void setIdForModification(Integer idForModification)
    {
        this.idForModification = idForModification;
    }

    public Integer getPositionForModification()
    {
        return positionForModification;
    }

    public void setPositionForModification(Integer positionForModification)
    {
        this.positionForModification = positionForModification;
    }

    public ArrayList<Department> getEmployeeOfDepartment()
    {
        return employeeOfDepartment;
    }

    public void setEmployeeOfDepartment(ArrayList<Department> employeeOfDepartment)
    {
        this.employeeOfDepartment = employeeOfDepartment;
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
