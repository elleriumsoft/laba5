package ru.elleriumsoft.printstructure;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Dmitriy on 23.03.2017.
 */
public interface PrintStructure extends EJBObject
{
    void initStructureFromDb() throws RemoteException;

    /**
     * Формирование HTML страницы структуры для вывода из уже проинциализированной структуры из БД
     *
     * @param command Добавленная ссылка на команду действия с элементом
     * @return Сформированная HTML страница
     */
    String printStructure(String command) throws RemoteException;

    Integer getMaxId() throws RemoteException;

    ArrayList<StructureElement> getStructureFromDb() throws RemoteException;

    boolean checkNeedChangeState(String id) throws RemoteException;
}
