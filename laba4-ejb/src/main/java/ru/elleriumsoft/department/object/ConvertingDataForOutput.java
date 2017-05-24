package ru.elleriumsoft.department.object;

/**
 * Вспомогательные методы для конвертации данных в удобочитаемый вид
 */
public class ConvertingDataForOutput
{
    private final static int MAX_LEN_STRING_OF_DATE = 10;

    /**
     * Преобразование даты вида 2017-05-24 в 24 мая 2017г.
     */
    public String convertingDateForOutput(String date)
    {
        if (date.length() < MAX_LEN_STRING_OF_DATE)
        {
            return date;
        }
        try
        {
            String bDate;
            if (Integer.valueOf(date.substring(MAX_LEN_STRING_OF_DATE-2, MAX_LEN_STRING_OF_DATE)) < MAX_LEN_STRING_OF_DATE)
            {
                bDate = date.substring(MAX_LEN_STRING_OF_DATE-1, MAX_LEN_STRING_OF_DATE);
            } else
            {
                bDate = date.substring(MAX_LEN_STRING_OF_DATE-2, MAX_LEN_STRING_OF_DATE);
            }
            return bDate + ' ' + getMonth(date.substring(5, 7)) + ' ' + date.substring(0, 4) + "г.";
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

    /**
     * Преобразование имени вида иванов петр в Иванов Петр
     */
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
