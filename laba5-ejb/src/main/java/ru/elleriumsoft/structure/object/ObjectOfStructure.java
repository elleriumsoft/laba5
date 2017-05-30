package ru.elleriumsoft.structure.object;

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

    boolean checkNeedUpdatePage() throws RemoteException;
    void changeStateOfElementStructure(String id) throws RemoteException;

    StateOfElements getStateOfElement(int id) throws RemoteException;

    void setStateOfElement(int id, int newState) throws RemoteException;

    void removeDeleted() throws RemoteException;

    void addStateElement(int id, int state) throws RemoteException;

    int getSizeStructure() throws RemoteException;

    StructureElement getStructureElement(int id) throws RemoteException;

    String getNameDeptForSelectedId(int selectedId) throws RemoteException;

    Structure getObjectStructure() throws RemoteException;

    void setCommandForChangeStructure(String command) throws RemoteException;
    void setIdForChangeByCommand(Integer id) throws RemoteException;
    Integer getIdForChangeByCommand() throws RemoteException;

    void modificationStructure(String newNameOfDepartment) throws RemoteException;

    void setErrorOnImport(String errorMessage) throws RemoteException;
    void setResultOfImport(String resultMessage) throws RemoteException;
}
