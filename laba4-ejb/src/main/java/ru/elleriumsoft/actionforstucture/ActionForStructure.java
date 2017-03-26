package ru.elleriumsoft.actionforstucture;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 26.03.2017.
 */
public interface ActionForStructure extends EJBObject
{
    Integer getIdForAction() throws RemoteException;
    void setIdForAction(Integer idForAction) throws RemoteException;
    void action(String param, int maxId) throws RemoteException;
    String getAction() throws RemoteException;
    void setAction(String action) throws RemoteException;
}
