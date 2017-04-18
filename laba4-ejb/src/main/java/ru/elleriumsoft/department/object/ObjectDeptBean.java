package ru.elleriumsoft.department.object;

import org.apache.log4j.Logger;
import ru.elleriumsoft.department.entity.EntityDept;
import ru.elleriumsoft.department.entity.EntityDeptHome;

import javax.ejb.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT;

/**
 * Created by Dmitriy on 16.04.2017.
 */
public class ObjectDeptBean implements SessionBean
{
    private ArrayList<Department> employeeOfDepartment;
    private Integer idDepartment;
    private String nameDepartment;

    private static final Logger logger = Logger.getLogger(ObjectDeptBean.class.getName());

    public void readAllEmployeeFromDept(Integer idDepartment)
    {
        this.idDepartment = idDepartment;
        employeeOfDepartment = new ArrayList<>();
        for (EntityDept element : readEmployeeFromDb(idDepartment))
        {
            try
            {
                employeeOfDepartment.add(new Department(element.getId(), convertingNameForOutput(element.getNameEmployee()), element.getNameProfession(), element.getEmploymentDate()));
            } catch (RemoteException e)
            {
                logger.info(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public String getNameDepartment()
    {
        return nameDepartment;
    }

    public void setNameDepartment(String nameDepartment)
    {
        this.nameDepartment = nameDepartment;
    }

    public Integer getIdDepartment()
    {
        return idDepartment;
    }

    public Integer getId(Integer idEmployee)
    {
        return employeeOfDepartment.get(idEmployee).getId();
    }

    public Integer getSizeObject()
    {
        return employeeOfDepartment.size();
    }

    public String getNameEmployee(Integer idEmployee)
    {
        return employeeOfDepartment.get(idEmployee).getNameEmployee();
    }

    public String getProfession(Integer idEmployee)
    {
        return employeeOfDepartment.get(idEmployee).getProfession();
    }

    public String getEmploymentDate(Integer idEmployee)
    {
        return convertingDateForOutput(employeeOfDepartment.get(idEmployee).getEmploymentDate());
    }

    public String getDateForEdit(Integer idEmployee)
    {
        return employeeOfDepartment.get(idEmployee).getEmploymentDate();
    }

    public int getMaxId()
    {
        int maxId = 0;
        EntityDeptHome entityHome = null;
        try
        {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "EntityDeptEJB");//"laba4-ejb/ru.elleriumsoft.structureForPrint.StructureProcessingFromDbHome");
            entityHome = (EntityDeptHome) PortableRemoteObject.narrow(remoteObject, EntityDeptHome.class);
            EntityDept entityDept = entityHome.findByMaxId();
            maxId = entityDept.getId();
        } catch (Exception e)
        {

            logger.info(e.getStackTrace());
        }
        logger.info("MAX_ID=" + maxId);
        return maxId;
    }

    private Collection<EntityDept> readEmployeeFromDb(Integer idDepartment)
    {
        Collection<EntityDept> entityDept = Collections.emptyList();
        try
        {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "EntityDeptEJB");
            EntityDeptHome entityDeptHome = (EntityDeptHome) PortableRemoteObject.narrow(remoteObject, EntityDeptHome.class);
            entityDept = entityDeptHome.findAll(idDepartment);
            logger.info("for id = " + idDepartment);
            logger.info("size=" + entityDept.size());
        } catch (NamingException e)
        {
            logger.info(e.getMessage());
            e.printStackTrace();
        } catch (RemoteException e)
        {
            logger.info(e.getMessage());
            e.printStackTrace();
        } catch (FinderException e)
        {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            return entityDept;
        }
    }

    private String convertingDateForOutput(String date)
    {
        if (date.length() < 10)
        {
            return date;
        }
        try
        {
            String bDate;
            if (Integer.valueOf(date.substring(8, 10)) < 10)
            {
                bDate = date.substring(9, 10);
            } else
            {
                bDate = date.substring(8, 10);
            }
            bDate = bDate + ' ' + getMonth(date.substring(5, 7)) + ' ';
            bDate = bDate + date.substring(0, 4) + "г.";
            return bDate;
        } catch (Exception ex)
        {
            return date;
        }
    }

    private String getMonth(String stMonth)
    {
        String[] months = {"ЯНВАРЯ", "ФЕВРАЛЯ", "МАРТА", "АПРЕЛЯ", "МАЯ", "ИЮНЯ", "ИЮЛЯ", "АВГУСТА", "СЕНТЯБРЯ", "ОКТЯБРЯ", "НОЯБРЯ", "ДЕКАБРЯ"};
        return months[Integer.valueOf(stMonth) - 1].toLowerCase();
    }

    private String convertingNameForOutput(String name)
    {
        StringBuilder bName;
        bName = new StringBuilder();
        bName.append(name.substring(0,1).toUpperCase());
        int i = 1;
        while (i<name.length())
        {
            if (name.charAt(i-1) == ' ')
            {
                bName.append(name.substring(i, i+1).toUpperCase());
            }
            else
            {
                bName.append(name.substring(i, i+1));
            }
            i++;
        }
        return bName.toString();
    }

    public ObjectDeptBean()
    {
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException
    {
    }

    public void ejbCreate() throws CreateException
    {
    }

    public void ejbRemove() throws EJBException
    {
    }

    public void ejbActivate() throws EJBException
    {
    }

    public void ejbPassivate() throws EJBException
    {
    }
}
