package ru.elleriumsoft.structure.xml;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 30.04.2017.
 */
public interface CreatingXml extends EJBObject
{
    void generateXml(Object data) throws RemoteException;
    String transformXmlToHtml(String xmlData) throws RemoteException;
}
