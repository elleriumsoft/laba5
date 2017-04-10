package ru.elleriumsoft.structure.print.printonscreen;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 23.03.2017.
 */
public interface PrintStructureHome extends EJBHome
{
    PrintStructure create() throws RemoteException, CreateException;
}
