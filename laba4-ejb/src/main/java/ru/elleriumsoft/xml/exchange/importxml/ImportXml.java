package ru.elleriumsoft.xml.exchange.importxml;

import ru.elleriumsoft.xml.exchange.Exchange;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 17.05.2017.
 */
public interface ImportXml extends EJBObject
{
    void importFromObjectToDatabase(boolean withOverwrite) throws RemoteException;
    void importFromXmlToObject() throws RemoteException;

    Exchange getExchange() throws RemoteException;

    boolean isErrorOnImport() throws RemoteException;
    String getTypeErrorImport() throws RemoteException;
    String getResultOfImport() throws RemoteException;
}
