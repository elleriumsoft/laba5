package ru.elleriumsoft.finder.entity;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 23.04.2017.
 */
public interface EntityFinder extends EJBObject
{
    String getNameEmployee() throws RemoteException;
    Integer getIdDepartment() throws RemoteException;
    String getNameDepartment() throws RemoteException;
    String getNameProfession() throws RemoteException;
    String getEmploymentDate() throws RemoteException;
}
