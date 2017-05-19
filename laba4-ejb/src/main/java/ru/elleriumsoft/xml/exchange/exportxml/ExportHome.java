package ru.elleriumsoft.xml.exchange.exportxml;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 14.05.2017.
 */
public interface ExportHome extends EJBHome
{
    ru.elleriumsoft.xml.exchange.exportxml.Export create() throws RemoteException, CreateException;
}
