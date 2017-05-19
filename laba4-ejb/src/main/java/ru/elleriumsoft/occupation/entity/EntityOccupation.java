package ru.elleriumsoft.occupation.entity;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 17.04.2017.
 */
public interface EntityOccupation extends EJBObject
{
    Integer getId() throws RemoteException;
    String getNameOccupation() throws RemoteException;
    void setNameOccupation(String nameOccupation) throws RemoteException;
    void setNeedUpdate(boolean needUpdate) throws RemoteException;
}
