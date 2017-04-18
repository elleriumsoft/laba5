package ru.elleriumsoft.department.action;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 18.04.2017.
 */
public interface ChangeItemDepartmentHome extends EJBHome
{
    ru.elleriumsoft.department.action.ChangeItemDepartment create() throws RemoteException, CreateException;
}
