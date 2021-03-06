package ru.elleriumsoft.occupation;

import org.apache.log4j.Logger;
import ru.elleriumsoft.occupation.entity.EntityOccupation;
import ru.elleriumsoft.occupation.entity.EntityOccupationHome;

import javax.ejb.FinderException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT;

@XmlType(propOrder = { "id", "name" }, name = "occupations")
@XmlRootElement
public class Occupation implements Serializable
{
    private static final Logger logger = Logger.getLogger(Occupation.class.getName());

    private Integer id;
    private String name;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ArrayList<Occupation> readOccupations()
    {
        logger.info("readOccupations");
        ArrayList<Occupation> occupations = new ArrayList<>();
        try
        {
            Collection<EntityOccupation> entityOccupations = getEntityOccupationHome().findAll();
            for (EntityOccupation entityOccupation : entityOccupations)
            {
                occupations.add(newOccupation(entityOccupation.getId(), entityOccupation.getNameOccupation()));
            }
        } catch (RemoteException e)
        {
            logger.info("Remote error read occ = " + e.getMessage());
        } catch (FinderException e)
        {
            logger.info("Finder error read occ = " + e.getMessage());
        }
        return occupations;
    }


    public EntityOccupationHome getEntityOccupationHome()
    {
        try
        {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "EntityOccupationEJB");
            return (EntityOccupationHome) PortableRemoteObject.narrow(remoteObject, EntityOccupationHome.class);
        } catch (NamingException e)
        {
            logger.info("Naming error read occ = " + e.getMessage());
        }
        return null;
    }

    private Occupation newOccupation(Integer id, String nameOccupation)
    {
        Occupation occupation = new Occupation();
        occupation.setId(id);
        occupation.setName(nameOccupation);
        return occupation;
    }
}
