package ru.elleriumsoft.structure;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 18.03.2017.
 */
public interface StructureProcessingFromDb extends EJBObject
{
    String getId() throws RemoteException;
    void setId(String id) throws RemoteException;
    String getNameDepartment() throws RemoteException;
    String getParent_id() throws RemoteException;
}
