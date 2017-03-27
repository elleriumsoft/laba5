package ru.elleriumsoft.structure;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 18.03.2017.
 */
public interface StructureProcessingFromDb extends EJBObject
{
    Integer getId() throws RemoteException;
    void setId(Integer id) throws RemoteException;
    String getNameDepartment() throws RemoteException;
    Integer getParent_id() throws RemoteException;
    void setNameDepartment(String nameDepartment) throws RemoteException;
    void setParent_id(Integer parent_id) throws RemoteException;
    void setNeedUpdate() throws RemoteException;
}
