package ru.elleriumsoft.xml.exchange.importxml;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 17.05.2017.
 */
public interface ImportXml extends EJBObject
{
    void importFromXmlToDatabase(String pathToXml, boolean withOverwrite) throws RemoteException;

    boolean isErrorOnImport() throws RemoteException;
    String getTypeErrorImport() throws RemoteException;
    String getResultOfImport() throws RemoteException;
}
