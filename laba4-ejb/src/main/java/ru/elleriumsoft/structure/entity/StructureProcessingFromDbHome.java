package ru.elleriumsoft.structure.entity;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by Dmitriy on 18.03.2017.
 */
public interface StructureProcessingFromDbHome extends EJBHome
{
    StructureProcessingFromDb findByPrimaryKey(Integer key) throws RemoteException, SQLException, FinderException;
    Collection findAll() throws FinderException, RemoteException;
    Collection findParentKeys(Integer key) throws FinderException, RemoteException;
    StructureProcessingFromDb findByMaxId() throws RemoteException, FinderException;

    StructureProcessingFromDb create(Integer id, String name, Integer parent_id) throws RemoteException, CreateException;
}
