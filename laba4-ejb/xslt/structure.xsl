<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html"/>

    <xsl:template match="/">

    <xsl:for-each select="structure/structureForPrint">
        <div>
            <xsl:value-of select="nameDepartment"/>
        </div>
        <br></br>
    </xsl:for-each>

    </xsl:template>

</xsl:stylesheet>