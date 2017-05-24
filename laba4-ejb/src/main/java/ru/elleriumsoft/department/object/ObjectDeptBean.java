package ru.elleriumsoft.department.object;

import org.apache.log4j.Logger;
import ru.elleriumsoft.department.entity.EntityDept;
import ru.elleriumsoft.occupation.Occupation;

import javax.ejb.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Dmitriy on 16.04.2017.
 */
public class ObjectDeptBean implements SessionBean
{
    private Dept allDept;
    private static final Logger logger = Logger.getLogger(ObjectDeptBean.class.getName());
    private ConvertingDataForOutput convertingData = new ConvertingDataForOutput();

    public void readAllEmployeeFromDept(Integer idDepartment)
    {
        allDept.setIdDepartment(idDepartment);
        allDept.setEmployeeOfDepartment(new ArrayList());
        for (EntityDept element : readEmployeeFromDb(idDepartment))
        {
            try
            {
                allDept.getEmployeeOfDepartment().add(newDept(element.getId(), convertingData.convertingNameForOutput(element.getNameEmployee()), element.getNameProfession(), element.getEmploymentDate()));
            } catch (RemoteException e)
            {
                logger.info("remote error: " + e.getMessage());
            }
        }
        addDatesForOutput();
        if (allDept.getOccupations() == null)
        {
            allDept.setOccupations(new Occupation().readOccupations());
        }
    }

    private Employee newDept(Integer id, String name, String nameProfession, String employmentDate)
    {
        Employee dept = new Employee();
        dept.setId(id);
        dept.setNameEmployee(name);
        dept.setProfession(nameProfession);
        dept.setEmploymentDate(employmentDate);
        return dept;
    }

    public int getMaxId()
    {
        int maxId = 0;
        try
        {
            EntityDept entityDept = new Employee().getDeptHome().findByMaxId();
            maxId = entityDept.getId();
        } catch (Exception e)
        {
            logger.info("Error: " + e.getMessage());
        }
        logger.info("MAX_ID=" + maxId);
        return maxId;
    }

    private Collection<EntityDept> readEmployeeFromDb(Integer idDepartment)
    {
        Collection<EntityDept> entityDept = Collections.emptyList();
        try
        {
            entityDept = new Employee().getDeptHome().findAll(idDepartment);
        } catch (RemoteException e)
        {
            logger.info("remote error: " + e.getMessage());
        } catch (FinderException e)
        {
            logger.info(idDepartment + " не найден");
        }
        return entityDept;
    }

    private void addDatesForOutput()
    {
        for (Employee dept : getAllDept().getEmployeeOfDepartment())
        {
            dept.setDateForOutput(convertingData.convertingDateForOutput(dept.getEmploymentDate()));
        }
    }

    public String getNameDepartment()
    {
        return allDept.getNameDepartment();
    }

    public void setNameDepartment(String nameDepartment)
    {
        allDept.setNameDepartment(nameDepartment);
    }

    public Integer getIdDepartment()
    {
        return allDept.getIdDepartment();
    }

    public Integer getId(Integer idEmployee)
    {
        return allDept.getEmployeeOfDepartment().get(idEmployee).getId();
    }

    public Integer getSizeObject()
    {
        return allDept.getEmployeeOfDepartment().size();
    }

    public String getNameEmployee(Integer idEmployee)
    {
        return allDept.getEmployeeOfDepartment().get(idEmployee).getNameEmployee();
    }

    public String getProfession(Integer idEmployee)
    {
        return allDept.getEmployeeOfDepartment().get(idEmployee).getProfession();
    }

    public String getEmploymentDate(Integer idEmployee)
    {
        return convertingData.convertingDateForOutput(allDept.getEmployeeOfDepartment().get(idEmployee).getEmploymentDate());
    }

    public String getDateForEdit(Integer idEmployee)
    {
        return allDept.getEmployeeOfDepartment().get(idEmployee).getEmploymentDate();
    }

    public void setCommandForModification(String command)
    {
        allDept.setCommandForModification(command);
    }

    public String getCommandForModification()
    {
        return allDept.getCommandForModification();
    }

    public Dept getAllDept()
    {
        return allDept;
    }

    public void setIdForModification(Integer id)
    {
        allDept.setIdForModification(id);
    }

    public Integer getIdForModification()
    {
        return allDept.getIdForModification();
    }

    public void setPositionForModification(Integer positionForModification)
    {
        allDept.setPositionForModification(positionForModification);
    }

    public Integer getPositionForModification()
    {
        return allDept.getPositionForModification();
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException
    {
    }

    public void ejbCreate() throws CreateException
    {
        allDept = new Dept();
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
