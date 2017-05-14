package ru.elleriumsoft.department.object;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(propOrder = { "id", "nameEmployee", "idProfession", "profession", "employmentDate", "dateForOutput" }, name = "employeeOfDepartment")
@XmlRootElement
public class Employee implements Serializable
{
    private Integer id;
    private String nameEmployee;
    private Integer idProfession;
    private String profession;
    private String employmentDate;
    private String dateForOutput;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getNameEmployee()
    {
        return nameEmployee;
    }

    public void setNameEmployee(String nameEmployee)
    {
        this.nameEmployee = nameEmployee;
    }

    public String getProfession()
    {
        return profession;
    }

    public void setProfession(String profession)
    {
        this.profession = profession;
    }

    public String getEmploymentDate()
    {
        return employmentDate;
    }

    public void setEmploymentDate(String employmentDate)
    {
        this.employmentDate = employmentDate;
    }

    public String getDateForOutput()
    {
        return dateForOutput;
    }

    public void setDateForOutput(String dateForOutput)
    {
        this.dateForOutput = dateForOutput;
    }

    public Integer getIdProfession()
    {
        return idProfession;
    }

    public void setIdProfession(Integer idProfession)
    {
        this.idProfession = idProfession;
    }
}
