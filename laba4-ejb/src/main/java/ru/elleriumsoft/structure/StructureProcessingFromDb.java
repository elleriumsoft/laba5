package ru.elleriumsoft.structure;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 18.03.2017.
 */
public interface StructureProcessingFromDb extends EJBObject
{
    public String getId() throws RemoteException;
    public void setId(String id) throws RemoteException;
    public String getNameDepartment() throws RemoteException;
}
