package ru.elleriumsoft.actionforstucture;

import ru.elleriumsoft.printstructure.objectstructure.ObjectOfStructure;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 26.03.2017.
 */
public interface ActionForStructure extends EJBObject
{
    Integer getIdForAction() throws RemoteException;
    void setIdForAction(Integer idForAction) throws RemoteException;
    String getAction() throws RemoteException;
    void setAction(String action) throws RemoteException;
    void action(String param, int maxId, ObjectOfStructure objectOfStructure) throws RemoteException;
}
