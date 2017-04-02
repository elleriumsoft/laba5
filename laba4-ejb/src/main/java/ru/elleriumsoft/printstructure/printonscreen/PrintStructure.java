package ru.elleriumsoft.printstructure.printonscreen;

import ru.elleriumsoft.printstructure.objectstructure.ObjectOfStructure;

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
     * @param command Добавленная ссылка на команду действия с элементом
     * @return Сформированная HTML страница
     */
    String printStructure(String command, ObjectOfStructure objectOfStructure) throws RemoteException;


}
