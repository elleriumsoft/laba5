<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html"/>

    <xsl:template match="/">
        <xsl:for-each select="structure/structureForPrint">
                <xsl:value-of select="nameDepartment"/>
                <br></br>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>