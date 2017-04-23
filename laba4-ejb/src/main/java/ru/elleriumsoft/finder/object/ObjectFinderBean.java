package ru.elleriumsoft.finder.object;

import org.apache.log4j.Logger;
import ru.elleriumsoft.finder.entity.EntityFinder;
import ru.elleriumsoft.finder.entity.EntityFinderHome;

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
 * Created by Dmitriy on 23.04.2017.
 */
public class ObjectFinderBean implements SessionBean
{
    private ArrayList<FinderData> finderDatas;

    private static final Logger logger = Logger.getLogger(ObjectFinderBean.class.getName());

    public int size()
    {
        return finderDatas.size();
    }

    public FinderData getDatas(int number)
    {
        return finderDatas.get(number);
    }

    public void findByParameters(String name, String id_occ, String startDate, String endDate)
    {
        finderDatas = new ArrayList<>();
        if (name == null && id_occ == null && (startDate == null || endDate == null))
        {
            logger.info("нет параметров для поиска");
            return;
        }
        for (EntityFinder entityFinder : readEmployeeFromDb(name, id_occ, startDate, endDate))
        {
            FinderData fd = new FinderData();
            try
            {
                logger.info("name=" + entityFinder.getNameEmployee());
                fd.setIdDepartment(entityFinder.getIdDepartment());
                fd.setNameDepartment(entityFinder.getNameDepartment());
                fd.setNameEmployee(entityFinder.getNameEmployee());
                fd.setNameProfession(entityFinder.getNameProfession());
                fd.setEmploymentDate(entityFinder.getEmploymentDate());
                finderDatas.add(fd);
            } catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
    }

    private Collection<EntityFinder> readEmployeeFromDb(String name, String id_occ, String startDate, String endDate)
    {
        Collection<EntityFinder> entityFinders = Collections.emptyList();
        try
        {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "EntityFinderEJB");
            EntityFinderHome entityFinderHome = (EntityFinderHome) PortableRemoteObject.narrow(remoteObject, EntityFinderHome.class);
            entityFinders = entityFinderHome.finder(name, id_occ, startDate, endDate);
            logger.info("size=" + entityFinders.size());
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
            return entityFinders;
        }
    }

    public ObjectFinderBean()
    {
    }

    public void ejbCreate() throws CreateException
    {
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
