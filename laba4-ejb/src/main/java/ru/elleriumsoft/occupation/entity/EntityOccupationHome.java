package ru.elleriumsoft.occupation.entity;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import java.util.Collection;

/**
 * Created by Dmitriy on 17.04.2017.
 */
public interface EntityOccupationHome extends EJBHome
{
    EntityOccupation findByPrimaryKey(Integer key) throws RemoteException, FinderException;
    Collection findAll() throws FinderException, RemoteException;
    EntityOccupation create(Integer id, String nameOccupation)  throws RemoteException, CreateException;
}
