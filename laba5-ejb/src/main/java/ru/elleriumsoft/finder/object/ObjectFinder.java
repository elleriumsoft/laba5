package ru.elleriumsoft.finder.object;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 23.04.2017.
 */
public interface ObjectFinder extends EJBObject
{
    void findByParameters(String name, String id_occ, String startDate, String endDate) throws RemoteException;

    StorageFinderData getStorageFinderData() throws RemoteException;
}
