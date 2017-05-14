package ru.elleriumsoft.xml.creatingxml;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 30.04.2017.
 */
public interface CreatingXml extends EJBObject
{
    void generateXml(Object data, String nameXml) throws RemoteException;
    String transformXmlToHtml(String xmlData) throws RemoteException;
    boolean validateXml(String nameXml) throws RemoteException;
}
