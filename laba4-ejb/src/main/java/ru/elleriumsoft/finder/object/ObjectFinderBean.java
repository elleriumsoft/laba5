package ru.elleriumsoft.finder.object;

import org.apache.log4j.Logger;
import ru.elleriumsoft.department.object.ConvertingDataForOutput;
import ru.elleriumsoft.finder.entity.EntityFinder;
import ru.elleriumsoft.finder.entity.EntityFinderHome;
import ru.elleriumsoft.occupation.entity.EntityOccupation;
import ru.elleriumsoft.occupation.entity.EntityOccupationHome;
import ru.elleriumsoft.occupation.Occupation;

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
    private StorageFinderData storageFinderData;

    private static final Logger logger = Logger.getLogger(ObjectFinderBean.class.getName());

    public int size()
    {
        return storageFinderData.getFinderDatas().size();
    }

    public void findByParameters(String name, String id_occ, String startDate, String endDate)
    {
        storageFinderData.setFinderDatas(new ArrayList<FinderData>());
        storageFinderData.setSizeFinderData(0);
        if (name == null && id_occ == null && (startDate == null || endDate == null))
        {
            logger.info("нет параметров для поиска");
            return;
        }
        ConvertingDataForOutput convertingData = new ConvertingDataForOutput();
        for (EntityFinder entityFinder : readEmployeeFromDb(name, id_occ, startDate, endDate))
        {
            FinderData fd = new FinderData();
            try
            {
                logger.info("name=" + entityFinder.getNameEmployee());
                fd.setIdDepartment(entityFinder.getIdDepartment());
                fd.setNameDepartment(entityFinder.getNameDepartment());
                fd.setNameEmployee(convertingData.convertingNameForOutput(entityFinder.getNameEmployee()));
                fd.setNameProfession(entityFinder.getNameProfession());
                fd.setEmploymentDate(convertingData.convertingDateForOutput(entityFinder.getEmploymentDate()));
                storageFinderData.getFinderDatas().add(fd);
                storageFinderData.setSizeFinderData(size());
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
        storageFinderData = new StorageFinderData();
        readOccupations();
    }

    private void readOccupations()
    {
        logger.info("readOccupations");
        storageFinderData.setOccupations(new ArrayList<Occupation>());
        InitialContext ic = null;
        try
        {
            ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "EntityOccupationEJB");
            EntityOccupationHome entityOccupationHome = (EntityOccupationHome) PortableRemoteObject.narrow(remoteObject, EntityOccupationHome.class);
            Collection<EntityOccupation> entityOccupations= entityOccupationHome.findAll();

            for (EntityOccupation entityOccupation : entityOccupations)
            {
                logger.info("loadOcc=" + entityOccupation.getNameOccupation());
                storageFinderData.getOccupations().add(newOccupation(entityOccupation.getId(), entityOccupation.getNameOccupation()));
            }
        } catch (NamingException e)
        {
            e.printStackTrace();
        } catch (FinderException e)
        {
            e.printStackTrace();
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    private Occupation newOccupation(Integer id, String nameOccupation)
    {
        Occupation occupation = new Occupation();
        occupation.setId(id);
        occupation.setName(nameOccupation);
        return occupation;
    }

    public StorageFinderData getStorageFinderData()
    {
        return storageFinderData;
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
