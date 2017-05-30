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

        <!-- Элементы управления для модификации, а также экспорта/импорта -->
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
            <xsl:when test="structure/commandForChangeStructure='export'">
                <div id = "export">
                    <form name="export" method="get" action="StructureServlet">
                        <input type= "submit" id= "Button11 " name= "export" value= "Экспорт в XML" ></input>
                        <br/><br/>
                        <input type="checkbox" name="withchildren" value="true" checked="checked">С подотделами</input>
                        <br/>
                        <input type="checkbox" name="withemployees" value="true" checked="checked">С сотрудниками</input>
                        <br/>
                        <input type="checkbox" name="withocc" value="true" >Выгрузить названия профессий</input>
                    </form>
                </div>
                <p><a href="/app/StructureServlet">
                    <img src="structure/images/exit.png" width="178" height="32" align="bottom"
                         alt="Вернуться"></img>
                </a></p>
            </xsl:when>
            <xsl:when test="structure/commandForChangeStructure='import'">
                <div id = "import">
                    <form name="import" enctype="multipart/form-data" method="post">
                        <p><b>Импорт данных в БД</b></p>
                        <input type="checkbox" name="withoverwrite" value="true" checked="checked">Перезаписывать данные</input>
                        <br/>
                        <p><input type="file" name="xmlfile" accept="*.xml"></input></p>
                        <input type="submit" value="Загрузить файл"></input>
                    </form>
                </div>
            </xsl:when>
            <xsl:otherwise>
                <!-- Кнопка поиска -->
                <div style= "position:absolute;left:5px;top:57px; ">
                    <a href="/app/FinderServlet"><img src="structure/images/find.png" width="33" height="33" align = "center" alt="Поиск" ></img></a>&#160;
                    <a href="/app/FinderServlet" style="color:#134096"><b>Поиск</b></a>
                </div>
                <!-- Кнопка импорта -->
                <div style= "position:absolute;left:110px;top:57px; ">
                    <a href="/app/StructureServlet?command=import&amp;element=0"><img src="structure/images/import.png" width="33" height="33" align = "center" alt="Поиск" ></img></a>&#160;
                    <a href="/app/StructureServlet?command=import&amp;element=0" style="color:#aa2f44;"><b>Импорт</b></a>
                </div>
                <br/>
                <div style="position:absolute;left:250px;top:37px;color:#aa2f44;">
                    <xsl:choose>
                        <!-- Сообщение что импорт прошел успешно -->
                        <xsl:when test="structure/errorOnImport = 'ok'">
                            <h3>Файл успешно импортирован в БД!<br/>
                                При импорте: <xsl:value-of select="structure/resultOfImport"/></h3>
                        </xsl:when>
                        <!-- Сообщение об ошибке импорта -->
                        <xsl:when test="structure/errorOnImport != 'no'">
                                <h3>Ошибка импорта файла:<br/>
                                    <xsl:value-of select="structure/errorOnImport"/></h3>
                        </xsl:when>
                    </xsl:choose>
                </div>
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
                &#160;
                <a href="StructureServlet?command=export&amp;element={id}"><img src="structure/images/export.png" width="17" height="17" align = "center" alt="Экспорт элемента"></img></a>

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