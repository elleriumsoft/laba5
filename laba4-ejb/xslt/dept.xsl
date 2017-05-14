<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html"/>

    <xsl:variable name="idDept" select="dept/idDepartment"/>
    <xsl:variable name="posForMod" select="dept/positionForModification"/>
    <xsl:variable name="command" select="dept/commandForModification"/>

    <xsl:template match="/">
        <h1>
            <xsl:value-of select="dept/nameDepartment"/>
        </h1>

        <!-- Кнопочки -->
        &#160;
        <a href="/app/StructureServlet">
            <img src="department/images/exit.png" width="33" height="33" align="bottom" alt="Вернуться"></img>
        </a>
        &#160;&#160;
        <a href="/app/DepartmentServlet?id={$idDept}&amp;command=add">
            <img src="department/images/create.png" width="33" height="33" align="bottom" alt="Добавить элемент"></img>
        </a>

        <!-- Таблица работников-->
        <table border="2">
            <th>Фамилия Имя Отчество</th>
            <th>Дата рождения</th>
            <th>Должность</th>

            <xsl:if test="$command='add' or $command='edit'">
                <tr>
                    <form name="add" method="get" action="/app/DepartmentServlet">
                        <td>
                            <xsl:choose>
                                <xsl:when test="$command='edit'">
                                    <xsl:for-each select="dept/employeeOfDepartment">
                                        <xsl:if test="position()=$posForMod">
                                            <input type="text" id="Editbox1" name="NewName" value="{nameEmployee}" maxlength="125"></input>
                                        </xsl:if>
                                    </xsl:for-each>
                                </xsl:when>
                                <xsl:when test="$command='add'">
                                    <input type="text" id="Editbox1" name="NewName" value="" maxlength="125"></input>
                                </xsl:when>
                            </xsl:choose>
                        </td>
                        <td>
                            <xsl:choose>
                                <xsl:when test="$command='edit'">
                                    <xsl:for-each select="dept/employeeOfDepartment">
                                        <xsl:if test="position()=$posForMod">
                                            <input type="date" id="Editbox2" name="NewDate" value="{employmentDate}" maxlength="10"></input>
                                        </xsl:if>
                                    </xsl:for-each>
                                </xsl:when>
                                <xsl:when test="$command='add'">
                                    <input type="date" id="Editbox2" name="NewDate" value="" maxlength="10"></input>
                                </xsl:when>
                            </xsl:choose>
                        </td>
                        <td>
                            <select size="2" name="NewOcc">
                                <option disabled="disabled">Выберите должность</option>
                                <xsl:for-each select="dept/occupations">
                                    <option value="{id}"> <xsl:value-of select="name"/> </option>
                                </xsl:for-each>
                            </select>
                        </td>
                        <td>
                            <input type="submit" id="Button1" name="" value="Готово!"></input>
                        </td>
                    </form>
                </tr>
            </xsl:if>

            <xsl:for-each select="dept/employeeOfDepartment">
                <tr>
                    <xsl:if test="$command != 'edit' or position() != $posForMod">
                            <td>&#160;
                                <xsl:value-of select="nameEmployee"/> &#160;
                            </td>
                            <td>&#160;
                                <xsl:value-of select="dateForOutput"/> &#160;
                            </td>
                            <td>&#160;
                                <xsl:value-of select="profession"/> &#160;
                            </td>

                            <xsl:choose>
                                <xsl:when test="$command='delete' and position()=$posForMod">
                                    <td>
                                        Уверены?
                                    </td>
                                    <td>
                                        <a href="/app/DepartmentServlet?command=delete&amp;idElement={id}">ДА</a>
                                    </td>
                                    <td>
                                        <a href="/app/DepartmentServlet?id={$idDept}">НЕТ</a>
                                    </td>
                                </xsl:when>
                                <xsl:otherwise>
                                    <td>&#160;
                                        <a href="/app/DepartmentServlet?id={$idDept}&amp;command=edit&amp;idElement={id}&amp;position={position()}">
                                            <img src="department/images/edit.png" width="16" height="16" align="center"
                                                 alt="Редактировать элемент"></img>
                                        </a>
                                        &#160;
                                    </td>
                                    <td>&#160;
                                        <a href="/app/DepartmentServlet?id={$idDept}&amp;command=delete&amp;idElement={id}&amp;position={position()}">
                                            <img src="department/images/delete.png" width="16" height="16" align="center"
                                                 alt="Удалить элемент"></img>
                                        </a>
                                        &#160;
                                    </td>
                                </xsl:otherwise>
                            </xsl:choose>
                    </xsl:if>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>


</xsl:stylesheet>