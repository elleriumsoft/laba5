package ru.elleriumsoft.department.action;

import org.apache.log4j.Logger;
import ru.elleriumsoft.department.entity.EntityDept;
import ru.elleriumsoft.department.entity.EntityDeptHome;

import javax.ejb.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import java.rmi.RemoteException;

import static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT;

/**
 * Created by Dmitriy on 18.04.2017.
 */
public class ChangeItemDepartmentBean implements SessionBean
{
    EntityDeptHome entityDeptHome;

    private static final Logger logger = Logger.getLogger(ChangeItemDepartmentBean.class.getName());

    public void changeItem(String action, int id, int id_dept, String name, String date, int id_occ)
    {
        if (name == null || name.equals("")) { name = "неизвестный"; }
        if (date == null || date.equals("")) { date = "1970-01-01"; }
        date = date.toLowerCase();

        switch (action)
        {
            case "add":
            {
                addItem(id, id_dept, name, date, id_occ);
                break;
            }
            case "edit":
            {
                editItem(id, id_dept, name, date, id_occ);
                break;
            }
            case "delete":
            {
                deleteItem(id);
                break;
            }
        }
    }

    private void addItem(int id, int id_dept, String name, String date, int id_occ)
    {
        try
        {
            entityDeptHome.create(id, id_dept, name, date, id_occ);
        } catch (RemoteException e)
        {
            logger.info("remote error in add: " + e.getMessage());
            e.printStackTrace();
        } catch (CreateException e)
        {
            logger.info("create error in add: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void editItem(int id, int id_dept, String name, String date, int id_occ)
    {
        try
        {
            EntityDept entityDept = entityDeptHome.findByPrimaryKey(id);
            entityDept.setIdDepartment(id_dept);
            entityDept.setNameEmployee(name);
            entityDept.setEmploymentDate(date);
            entityDept.setIdProfession(id_occ);
            entityDept.setNeedUpdate();
        } catch (RemoteException e)
        {
            e.printStackTrace();
        } catch (FinderException e)
        {
            e.printStackTrace();
        }

    }

    private void deleteItem(int id)
    {
        try
        {
            EntityDept entityDept = entityDeptHome.findByPrimaryKey(id);
            entityDept.remove();
        } catch (RemoteException e)
        {
            e.printStackTrace();
        } catch (FinderException e)
        {
            e.printStackTrace();
        } catch (RemoveException e)
        {
            e.printStackTrace();
        }
    }

    public ChangeItemDepartmentBean()
    {
    }

    public void ejbCreate() throws CreateException
    {
        InitialContext ic = null;
        try
        {
            ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "EntityDeptEJB");
            entityDeptHome = (EntityDeptHome) PortableRemoteObject.narrow(remoteObject, EntityDeptHome.class);
        } catch (NamingException e)
        {
            logger.info("error in create bean due entity bean");
            e.printStackTrace();
        }
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
