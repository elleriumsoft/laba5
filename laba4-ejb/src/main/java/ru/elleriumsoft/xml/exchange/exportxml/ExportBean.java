package ru.elleriumsoft.xml.exchange.exportxml;

import org.apache.log4j.Logger;
import ru.elleriumsoft.department.entity.EntityDept;
import ru.elleriumsoft.department.entity.EntityDeptHome;
import ru.elleriumsoft.department.object.Employee;
import ru.elleriumsoft.occupation.Occupation;
import ru.elleriumsoft.structure.Structure;
import ru.elleriumsoft.structure.entity.StructureProcessingFromDb;
import ru.elleriumsoft.structure.entity.StructureProcessingFromDbHome;
import ru.elleriumsoft.xml.exchange.DeptInfo;
import ru.elleriumsoft.xml.exchange.Exchange;

import javax.ejb.*;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT;

/**
 * Created by Dmitriy on 14.05.2017.
 */
public class ExportBean implements SessionBean
{
    private Exchange exchange;
    private static final Logger logger = Logger.getLogger(ExportBean.class.getName());
    private StructureProcessingFromDbHome entityStructureHome;
    private EntityDeptHome entityDeptHome;

    public void exportToXml(int idDept)
    {
        readDepartments(idDept);
        readEmployees();
        readOccupations();
    }

    private void readDepartments(int idDept)
    {
        exchange.setDepartments(new ArrayList<DeptInfo>());
        readDepartment(idDept);
        if (exchange.isWithChildrenDept())
        {
            readDepartmentsWithParentId(exchange.getDepartments().get(0).getIdDept());
        }
    }

    private void readDepartmentsWithParentId(int parentId)
    {
        try
        {
            Vector<StructureProcessingFromDb> children = (Vector) entityStructureHome.findParentKeys(parentId);

            if (children.size() == 0) { return; }

            for (StructureProcessingFromDb child : children)
            {
                addDept(child);

                readDepartmentsWithParentId(child.getId());
            }
        } catch (FinderException e)
        {
            logger.info("finder error: " + e.getMessage());
        } catch (RemoteException e)
        {
            logger.info("remote error: " + e.getMessage());
        }
    }

    private void readDepartment(int idDept)
    {
        try
        {
            addDept(entityStructureHome.findByPrimaryKey(idDept));
        } catch (SQLException e)
        {
            logger.info("sql error: " + e.getMessage());
        } catch (FinderException e)
        {
            logger.info("finder error: " + e.getMessage());
        } catch (RemoteException e)
        {
            logger.info("remote error: " + e.getMessage());
        }
    }

    private void addDept(StructureProcessingFromDb dept)
    {
        try
        {
            DeptInfo deptInfo = new DeptInfo();
            deptInfo.setIdDept(dept.getId());
            deptInfo.setNameDept(dept.getNameDepartment());
            deptInfo.setParentIdDept(dept.getParent_id());
            exchange.getDepartments().add(deptInfo);
        } catch (RemoteException e)
        {
            logger.info("remote error: " + e.getMessage());
        }
    }

    private void readEmployees()
    {
        if (exchange.isWithChildrenDept())
        {
            for (DeptInfo dept : exchange.getDepartments())
            {
                dept.setEmployees(readEmployee(dept.getIdDept()));
            }
        }
    }

    private ArrayList<Employee> readEmployee(int idDept)
    {
        try
        {
            Vector<EntityDept> empsFromDb = (Vector) entityDeptHome.findAll(idDept);
            ArrayList<Employee> emps = new ArrayList<>();
            for (EntityDept entity : empsFromDb)
            {
                emps.add(createEmployee(entity));
            }
            return emps;
        } catch (FinderException e)
        {
            logger.info("finder error in emp: " + e.getMessage());
        } catch (RemoteException e)
        {
            logger.info("remote error in emp: " + e.getMessage());
        }
        return null;
    }

    private Employee createEmployee(EntityDept entityDept) throws RemoteException
    {
        Employee emp = new Employee();
        emp.setId(entityDept.getId());
        emp.setNameEmployee(entityDept.getNameEmployee());
        emp.setEmploymentDate(entityDept.getEmploymentDate());
        emp.setIdProfession(entityDept.getIdProfession());
        return emp;

    }

    private void readOccupations()
    {
        if (exchange.getOccupations() == null)
        {
            exchange.setOccupations(new ArrayList<Occupation>());
        }
        if (exchange.isWithOccupations() && exchange.getOccupations().size() == 0)
        {
            exchange.setOccupations(new Occupation().readOccupations());
        }
    }

    public void setWithChildrenDept(boolean withChildrenDept)
    {
        exchange.setWithChildrenDept(withChildrenDept);
    }

    public void setWithEmployees(boolean withEmployees)
    {
        exchange.setWithEmployees(withEmployees);
    }

    public void setWithOccupations(boolean withOcc)
    {
        exchange.setWithOccupations(withOcc);
    }

    public void ejbCreate() throws CreateException
    {
        exchange = new Exchange();
        entityStructureHome = new Structure().getStructureHome();
        entityDeptHome = getDeptHome();
    }

    private EntityDeptHome getDeptHome()
    {
        EntityDeptHome entityDeptHome = null;
        try
        {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "EntityDeptEJB");
            entityDeptHome = (EntityDeptHome) PortableRemoteObject.narrow(remoteObject, EntityDeptHome.class);
        } catch (Exception e)
        {
            logger.info(e.getMessage());
        }
        return entityDeptHome;
    }

    public Exchange getExchange()
    {
        return exchange;
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException
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
