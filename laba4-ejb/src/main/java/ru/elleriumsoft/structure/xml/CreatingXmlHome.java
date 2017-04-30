package ru.elleriumsoft.structure.xml;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 30.04.2017.
 */
public interface CreatingXmlHome extends EJBHome
{
    ru.elleriumsoft.structure.xml.CreatingXml create() throws RemoteException, CreateException;
}
