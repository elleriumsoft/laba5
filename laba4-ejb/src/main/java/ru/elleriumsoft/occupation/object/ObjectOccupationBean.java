package ru.elleriumsoft.occupation.object;

import ru.elleriumsoft.occupation.entity.EntityOccupation;
import ru.elleriumsoft.occupation.entity.EntityOccupationHome;

import javax.ejb.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT;

/**
 * Created by Dmitriy on 17.04.2017.
 */
public class ObjectOccupationBean implements SessionBean
{
    private ArrayList<Occupation> occupations;

    public String getOccupationWithId(Integer idOcc)
    {
        return occupations.get(idOcc).getName();
    }

    public Integer getSize()
    {
        return occupations.size();
    }

    public String getHtmlCodeForSelectOption()
    {
        StringBuilder html = new StringBuilder();
        for (Occupation occElement : occupations)
        {
            html.append("<option value=\"" + occElement.getId() + "\">" + occElement.getName() + "</option>");
        }
        return html.toString();
    }

    public String getHtmlCodeForSelectOptionWithSelection(int selection)
    {
        StringBuilder html = new StringBuilder();
        for (int i = 0; i < occupations.size(); i++)
        {
            if (i == selection)
            {
                html.append("<option value=\"" + occupations.get(i).getId() + "\" selected>" + occupations.get(i).getName() + "</option>");
            }
            else
            {
                html.append("<option value=\"" + occupations.get(i).getId() + "\">" + occupations.get(i).getName() + "</option>");
            }
        }
        return html.toString();
    }

    public ObjectOccupationBean()
    {
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException
    {
    }

    public void ejbCreate() throws CreateException
    {
        occupations = new ArrayList<>();
        InitialContext ic = null;
        try
        {
            ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "EntityOccupationEJB");
            EntityOccupationHome entityOccupationHome = (EntityOccupationHome) PortableRemoteObject.narrow(remoteObject, EntityOccupationHome.class);
            Collection<EntityOccupation> entityOccupations= entityOccupationHome.findAll();
            for (EntityOccupation entityOccupation : entityOccupations)
            {
                occupations.add(new Occupation(entityOccupation.getId(), entityOccupation.getNameOccupation()));
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
