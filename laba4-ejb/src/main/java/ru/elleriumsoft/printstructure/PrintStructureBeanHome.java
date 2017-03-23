package ru.elleriumsoft.printstructure;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 23.03.2017.
 */
public interface PrintStructureBeanHome extends EJBHome
{
    PrintStructureBean create() throws RemoteException, CreateException;
}
