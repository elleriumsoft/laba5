package ru.elleriumsoft.department.object;

import org.apache.log4j.Logger;
import ru.elleriumsoft.department.entity.EntityDeptHome;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

import static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT;

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

    private static final Logger logger = Logger.getLogger(Employee.class.getName());

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

    public EntityDeptHome getDeptHome()
    {
        try
        {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "EntityDeptEJB");
            return  (EntityDeptHome) PortableRemoteObject.narrow(remoteObject, EntityDeptHome.class);
        } catch (NamingException e)
        {
            logger.info("Naming Error!");
        }
        return null;
    }
}
