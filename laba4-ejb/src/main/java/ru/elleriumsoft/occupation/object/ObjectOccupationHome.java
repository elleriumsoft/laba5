package ru.elleriumsoft.occupation.object;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 17.04.2017.
 */
public interface ObjectOccupationHome extends EJBHome
{
    ru.elleriumsoft.occupation.object.ObjectOccupation create() throws RemoteException, CreateException;
}
