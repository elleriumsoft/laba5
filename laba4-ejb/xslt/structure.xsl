<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html"/>

    <xsl:template match="/">

        <xsl:for-each select="structure/structureForPrint">


            <div style="position:absolute;left: {level*35+25}px">

                <!-- Имя элемента и ссылка на его раскрытие в плане сотрудников -->
                <a href="/app/department/Department.jsp?id={id}&amp;name={nameDepartment}">
                    <xsl:value-of select="nameDepartment"/>
                </a>

                <!-- Кнопки добавления/редактирования/удаления элемента структуры -->
                &#160;&#160;
                <a href="Structure.jsp?command=add&amp;element={id}"><img src="images/create.png" width="17" height="17" align = "center" alt="Добавить элемент"></img></a>
                &#160;
                <a href="Structure.jsp?command=edit&amp;element={id}"><img src="images/edit.png" width="17" height="17" align = "center" alt="Редактировать элемент"></img></a>
                &#160;
                <a href="Structure.jsp?command=delete&amp;element={id}"><img src="images/delete.png" width="17" height="17" align = "center" alt="Удалить элемент"></img></a>
            </div>

            <!-- Кнопка + или - для раскрытия или сворачивания списка -->
            <div style="position:absolute;left: {level*35+5}px">
                <xsl:choose>
                    <xsl:when test="stateOfElement=1">
                        <a href="./Structure.jsp?open={id}"><img src="images/plus.png" width="14" height="14" align = "bottom" alt="Свернуть список"></img></a>
                    </xsl:when>
                    <xsl:when test="stateOfElement=2">
                        <a href="./Structure.jsp?open={id}"><img src="images/minus.png" width="14" height="14" align = "bottom" alt="Свернуть список"></img></a>
                    </xsl:when>
                    <xsl:when test="stateOfElement=3">
                        <img src="images/blank.png" width="14" height="14" align = "bottom" alt="Элемент"></img>
                    </xsl:when>
                </xsl:choose>
            </div>

            <br></br><br></br>

        </xsl:for-each>

    </xsl:template>

</xsl:stylesheet>