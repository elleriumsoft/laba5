package ru.elleriumsoft.xml.exchange.importxml;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 17.05.2017.
 */
public interface ImportXmlHome extends EJBHome
{
    ru.elleriumsoft.xml.exchange.importxml.ImportXml create() throws RemoteException, CreateException;
}
