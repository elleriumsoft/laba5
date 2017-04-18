package ru.elleriumsoft.occupation.object;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 17.04.2017.
 */
public interface ObjectOccupation extends EJBObject
{
    String getOccupationWithId(Integer idOcc) throws RemoteException;
    Integer getSize() throws RemoteException;
    String getHtmlCodeForSelectOption() throws RemoteException;
    String getHtmlCodeForSelectOptionWithSelection(int selection) throws RemoteException;
}
