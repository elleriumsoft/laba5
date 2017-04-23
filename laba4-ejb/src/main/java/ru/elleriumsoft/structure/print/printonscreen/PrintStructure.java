package ru.elleriumsoft.structure.print.printonscreen;

import ru.elleriumsoft.structure.objectstructure.ObjectOfStructure;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 23.03.2017.
 */
public interface PrintStructure extends EJBObject
{

    /**
     * Формирование HTML страницы структуры для вывода из уже проинциализированной структуры из БД
     *
     * @return Сформированная HTML страница
     */
    String printStructure(ObjectOfStructure objectOfStructure) throws RemoteException;


}
