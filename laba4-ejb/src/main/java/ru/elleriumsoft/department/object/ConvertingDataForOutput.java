package ru.elleriumsoft.department.object;

/**
 * Created by Dmitriy on 11.05.2017.
 */
public class ConvertingDataForOutput
{
    public String convertingDateForOutput(String date)
    {
        if (date.length() < 10)
        {
            return date;
        }
        try
        {
            String bDate;
            if (Integer.valueOf(date.substring(8, 10)) < 10)
            {
                bDate = date.substring(9, 10);
            } else
            {
                bDate = date.substring(8, 10);
            }
            bDate = bDate + ' ' + getMonth(date.substring(5, 7)) + ' ';
            bDate = bDate + date.substring(0, 4) + "г.";
            return bDate;
        } catch (Exception ex)
        {
            return date;
        }
    }

    private String getMonth(String stMonth)
    {
        String[] months = {"ЯНВАРЯ", "ФЕВРАЛЯ", "МАРТА", "АПРЕЛЯ", "МАЯ", "ИЮНЯ", "ИЮЛЯ", "АВГУСТА", "СЕНТЯБРЯ", "ОКТЯБРЯ", "НОЯБРЯ", "ДЕКАБРЯ"};
        return months[Integer.valueOf(stMonth) - 1].toLowerCase();
    }

    public String convertingNameForOutput(String name)
    {
        StringBuilder bName;
        bName = new StringBuilder();
        bName.append(name.substring(0, 1).toUpperCase());
        int i = 1;
        while (i < name.length())
        {
            if (name.charAt(i - 1) == ' ' || name.charAt(i - 1) == '.')
            {
                bName.append(name.substring(i, i + 1).toUpperCase());
            } else
            {
                bName.append(name.substring(i, i + 1));
            }
            i++;
        }
        return bName.toString();
    }
}
