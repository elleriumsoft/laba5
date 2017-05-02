package ru.elleriumsoft.structure.objectstructure;

import ru.elleriumsoft.structure.StateOfElements;
import ru.elleriumsoft.structure.Structure;
import ru.elleriumsoft.structure.StructureElement;

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

    int getSelectedId() throws RemoteException;
    void setSelectedId(int selectedId) throws RemoteException;
    String getNameDeptForSelectedId() throws RemoteException;

    Structure getObjectStructure() throws RemoteException;
}
