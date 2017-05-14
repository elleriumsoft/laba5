package ru.elleriumsoft.xml.exchange.export;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 14.05.2017.
 */
public interface ExportHome extends EJBHome
{
    ru.elleriumsoft.xml.exchange.export.Export create() throws RemoteException, CreateException;
}
