package ru.elleriumsoft.printstructure.objectstructure;

import ru.elleriumsoft.printstructure.StateOfElements;
import ru.elleriumsoft.printstructure.StructureElement;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 02.04.2017.
 */
public interface ObjectOfStructure extends EJBObject
{
    void initStructureFromDb() throws RemoteException;

    Integer getMaxId() throws RemoteException;

    StateOfElements getStateOfElement(int id) throws RemoteException;

    void setStateOfElement(int id, int newState) throws RemoteException;

    //void removeStateElement(int id) throws RemoteException;

    void removeDeleted() throws RemoteException;

    void addStateElement(int id, int state) throws RemoteException;

    int getSizeStructure() throws RemoteException;

    StructureElement getStructureElement(int id) throws RemoteException;
}
