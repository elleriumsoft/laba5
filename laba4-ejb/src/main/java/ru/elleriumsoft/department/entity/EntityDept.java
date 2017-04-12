package ru.elleriumsoft.department.entity;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 12.04.2017.
 */
public interface EntityDept extends EJBObject
{
    String getNameOccupation() throws RemoteException;
    void setIdDepartment(Integer idDepartment) throws RemoteException;
}
