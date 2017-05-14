package ru.elleriumsoft.xml.creatingxml;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 30.04.2017.
 */
public interface CreatingXmlHome extends EJBHome
{
    CreatingXml create() throws RemoteException, CreateException;
}
