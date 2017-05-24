package ru.elleriumsoft.finder.object;

import ru.elleriumsoft.occupation.Occupation;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;


@XmlType(propOrder = { "sizeFinderData", "finderDatas", "occupations" }, name = "finder")
@XmlRootElement
public class StorageFinderData implements Serializable
{
    private Integer sizeFinderData;
    private ArrayList<FinderData> finderDatas;
    private ArrayList<Occupation> occupations;

    public ArrayList<FinderData> getFinderDatas()
    {
        return finderDatas;
    }

    public void setFinderDatas(ArrayList<FinderData> finderDatas)
    {
        this.finderDatas = finderDatas;
    }

    public ArrayList<Occupation> getOccupations()
    {
        return occupations;
    }

    public void setOccupations(ArrayList<Occupation> occupations)
    {
        this.occupations = occupations;
    }

    public Integer getSizeFinderData()
    {
        return sizeFinderData;
    }

    public void setSizeFinderData(Integer sizeFinderData)
    {
        this.sizeFinderData = sizeFinderData;
    }
}
