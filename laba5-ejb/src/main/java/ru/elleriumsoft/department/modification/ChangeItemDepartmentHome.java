package ru.elleriumsoft.department.modification;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 18.04.2017.
 */
public interface ChangeItemDepartmentHome extends EJBHome
{
    ru.elleriumsoft.department.modification.ChangeItemDepartment create() throws RemoteException, CreateException;
}
