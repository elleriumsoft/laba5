package ru.elleriumsoft.finder.entity;

import javax.ejb.EJBHome;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import java.util.Collection;

/**
 * Created by Dmitriy on 23.04.2017.
 */
public interface EntityFinderHome extends EJBHome
{
    EntityFinder findByPrimaryKey(Integer key) throws RemoteException, FinderException;
    Collection finder(String nameForFind, String occForFind, String startDateForFind, String endDateForFind) throws FinderException, RemoteException;
}
