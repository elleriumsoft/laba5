<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <h1 style="color:#191970">
            <b>Поиск сотрудников в структуре мэрии</b>
        </h1>
        <a href="/app/StructureServlet"><img src="finder/images/exit.png" width="33" height="33" align = "bottom" alt="Вернуться"></img></a>
        <br/><br/>

        <table border="0" width="400">
            <form name="find" method="get" action="FinderServlet">

                <b>Введите имя полностью или его часть</b>
                <br/>
                <input type="text" id="Editbox1" name="nameForFind" value=""  maxlength="125" width="300"></input>
                <br/><br/>

                <b>Выберите должность для поиска</b><br/>
                <select size="6" name="occForFind">
                    <option disabled="disabled">Выберите должность</option>
                    <xsl:for-each select="storageFinderData/occupations">
                        <option value="{id}"> <xsl:value-of select="name"/> </option>
                    </xsl:for-each>
                </select>
                <br/><br/>

                <b>Выберите интервал дат рождения для поиска</b>
                <br/>
                <input type="date" id="Editbox2" name="startDateForFind" value=""  maxlength="10"></input>
                &#160;-&#160;
                <input type="date" id="Editbox3" name="endDateForFind" value=""  maxlength="10"></input>
                <br/><br/>

                <input type="image" src="finder/images/find.png" width="92" height="35"></input>

            </form>
        </table>
        <br/><br/>

        Найдено записей = <xsl:value-of select="storageFinderData/sizeFinderData"/>
        <br/>

        <table border="1">
            <th>Отдел</th><th>Фамилия Имя Отчество</th> <th>Дата рождения</th> <th>Должность</th>

            <xsl:for-each select="storageFinderData/finderDatas">
                <tr>
                    <td>&#160; <a href="/app/DepartmentServlet?id={idDepartment}"> <xsl:value-of select="nameDepartment"/> </a> &#160;</td>
                    <td>&#160; <xsl:value-of select="nameEmployee"/>   &#160;</td>
                    <td>&#160; <xsl:value-of select="employmentDate"/> &#160;</td>
                    <td>&#160; <xsl:value-of select="nameProfession"/> &#160;</td>
                </tr>
            </xsl:for-each>

        </table>

    </xsl:template>

</xsl:stylesheet>