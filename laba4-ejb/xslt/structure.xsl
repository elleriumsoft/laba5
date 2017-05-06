<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html"/>

    <xsl:template match="/">

        <head>
            <title>>Структура мэрии</title>
        </head>

        <body>

        <h1 style="color:#191970">
            <b>Структура мэрии</b>
        </h1>

        <!-- Элементы управления для модификации -->
        <xsl:choose>
            <xsl:when test="structure/commandForChangeStructure='add'">
                <div id = "addaction">
                    Добавить элемент
                    <form name="add" method="get" action="StructureServlet">
                        <input type="text" id="Editbox1" style="position:absolute;" name="newname" value=""  maxlength="200"></input>
                        <input type="submit" id="Button5" name="" value="ОК" style="position:absolute;left:240px;top:83px;width:104px;height:23px;z-index:0;"></input>
                        <input type="submit" id="Button6" onclick="window.location.href='StructureServlet';return false;" name="" value="Отмена" style="position:absolute;left:350px;top:83px;width:104px;height:23px;z-index:0;"></input>
                    </form>
                </div>
            </xsl:when>
            <xsl:when test="structure/commandForChangeStructure='edit'">
                <div id = "editaction">
                    Редактирование элемента
                    <form name="add" method="get" action="StructureServlet">
                        <input type="text" id="Editbox2" style="position:absolute;" name="newname" maxlength="200" value="" ></input>
                        <input type="submit" id="Button7" name="" value="ОК" style="position:absolute;left:240px;top:83px;width:104px;height:23px;z-index:0;"></input>
                        <input type="submit" id="Button8" onclick="window.location.href='StructureServlet';return false;" name="" value="Отмена" style="position:absolute;left:350px;top:83px;width:104px;height:23px;z-index:0;"></input>
                    </form>
                </div>
            </xsl:when>
            <xsl:when test="structure/commandForChangeStructure='delete'">
                <div id = "deleteaction">
                    Вы уверены что хотите удалить?
                    <br></br><br></br>
                    <input type= "submit" id= "Button9 " onclick= "window.location.href='StructureServlet?newname=delete';return false; " name= " " value= "ДА" style= "position:absolute;left:9px;top:83px;width:104px;height:25px;color:#FF0000; "></input>
                    <input type= "submit" id= "Button10 " onclick= "window.location.href='StructureServlet';return false; " name= " " value= "НЕТ" style= "position:absolute;left:109px;top:83px;width:104px;height:25px;color:#FF0000; "></input>
                </div>
            </xsl:when>
            <xsl:otherwise>
                <!-- Кнопка поиска -->
                <a href="/app/finder/Finder.jsp"><img src="structure/images/find.png" width="33" height="33" align = "center" alt="Поиск" text="fdfd"></img></a>
                <div style= "position:absolute;left:50px;top:70px;color:#134096; ">
                    <a href="/app/finder/Finder.jsp"><b>Поиск</b></a>
                </div>
                <br/>
            </xsl:otherwise>
        </xsl:choose>

        <br/>

        <xsl:for-each select="structure/structureForPrint">
            <div style="position:absolute;left: {level*35+25}px">
                <!-- Имя элемента и ссылка на его раскрытие в плане сотрудников -->
                <a href="/app/DepartmentServlet?id={id}">
                    <xsl:value-of select="nameDepartment"/>
                </a>

                <!-- Кнопки добавления/редактирования/удаления элемента структуры -->
                &#160;&#160;
                <a href="StructureServlet?command=add&amp;element={id}"><img src="structure/images/create.png" width="17" height="17" align = "center" alt="Добавить элемент"></img></a>
                &#160;
                <a href="StructureServlet?command=edit&amp;element={id}"><img src="structure/images/edit.png" width="17" height="17" align = "center" alt="Редактировать элемент"></img></a>
                <xsl:if test="id != 1">
                    &#160;
                    <a href="StructureServlet?command=delete&amp;element={id}"><img src="structure/images/delete.png" width="17" height="17" align = "center" alt="Удалить элемент"></img></a>
                </xsl:if>
            </div>

            <!-- Кнопка + или - для раскрытия или сворачивания списка -->
            <div style="position:absolute;left: {level*35+5}px">
                <xsl:choose>
                    <xsl:when test="stateOfElement=1">
                        <a href="./StructureServlet?open={id}"><img src="structure/images/plus.png" width="14" height="14" align = "bottom" alt="Свернуть список"></img></a>
                    </xsl:when>
                    <xsl:when test="stateOfElement=2">
                        <a href="./StructureServlet?open={id}"><img src="structure/images/minus.png" width="14" height="14" align = "bottom" alt="Свернуть список"></img></a>
                    </xsl:when>
                    <xsl:when test="stateOfElement=3">
                        <img src="structure/images/blank.png" width="14" height="14" align = "bottom" alt="Элемент"></img>
                    </xsl:when>
                </xsl:choose>
            </div>
            <br></br><br></br>
        </xsl:for-each>

        </body>

    </xsl:template>

</xsl:stylesheet>