package ru.elleriumsoft.finder.object;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 23.04.2017.
 */
public interface ObjectFinderHome extends EJBHome
{
    ru.elleriumsoft.finder.object.ObjectFinder create() throws RemoteException, CreateException;
}
