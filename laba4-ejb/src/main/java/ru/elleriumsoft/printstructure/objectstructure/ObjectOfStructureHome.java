package ru.elleriumsoft.printstructure.objectstructure;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 02.04.2017.
 */
public interface ObjectOfStructureHome extends EJBHome
{
    ru.elleriumsoft.printstructure.objectstructure.ObjectOfStructure create() throws RemoteException, CreateException;
}
