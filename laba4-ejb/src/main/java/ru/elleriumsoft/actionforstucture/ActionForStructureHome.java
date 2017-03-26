package ru.elleriumsoft.actionforstucture;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 26.03.2017.
 */
public interface ActionForStructureHome extends EJBHome
{
    ru.elleriumsoft.actionforstucture.ActionForStructure create() throws RemoteException, CreateException;
}
