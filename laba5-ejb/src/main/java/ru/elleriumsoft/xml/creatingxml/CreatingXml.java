package ru.elleriumsoft.xml.creatingxml;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 30.04.2017.
 */
public interface CreatingXml extends EJBObject
{
    /**
     * Создает из любого класса с аннотациями JAXB файл xml
     * @param data - объект класса аннотированного JAXB
     * @param nameXml - имя файла xml
     */
    void generateXml(Object data, String nameXml) throws RemoteException;

    /**
     * Преобразование xml данных с помощью xlst в html страницу
     * @param xmlData - определяет имя файла (без расширения) xslt и имя файла xml, которые должны быть одинаковыми
     * @return - возвращает строку содержащую html код, который можно вывести или сохранить в файл
     */
    String transformXmlToHtml(String xmlData) throws RemoteException;

    /**
     * Проверяет xml файл с помощью шаблона xsd
     * @param nameXml - определяет имя файла xml и xsd без раширения, которые должны быть одинаковыми
     */
    boolean validateXml(String nameXml) throws RemoteException;
}
