package ru.elleriumsoft.department.object;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 16.04.2017.
 */
public interface ObjectDeptHome extends EJBHome
{
    ru.elleriumsoft.department.object.ObjectDept create() throws RemoteException, CreateException;
}
