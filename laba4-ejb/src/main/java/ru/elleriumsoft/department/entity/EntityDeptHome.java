package ru.elleriumsoft.department.entity;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import java.util.Collection;

/**
 * Created by Dmitriy on 12.04.2017.
 */
public interface EntityDeptHome extends EJBHome
{
    EntityDept findByPrimaryKey(Integer key) throws RemoteException, FinderException;
    Collection findAll(Integer idDeptartment) throws FinderException, RemoteException;
    EntityDept findByMaxId() throws RemoteException, FinderException;

    EntityDept create(Integer id, Integer id_dept, String name, String date, Integer occ_id) throws RemoteException, CreateException;
}
