<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
  <body>

    <style>
     @media print {
      .razr {
       page-break-after: always;
      }
     }
    </style>

    <table border="1">
      <tr>
        <th>Квартира</th>
        <th>Код</th>
        <th>Площадь</th>
        <th>Собственник</th>
        <th>Доля</th>
        <th>Голоса</th>
        <th>Реквизиты документа собственности</th>

      </tr>
<xsl:for-each select="data/element">
  <xsl:sort select="@kv" data-type="number"/>
     <tr>
      <td><xsl:value-of select="@kv"/></td>
      <td><xsl:value-of select="@id"/></td>
      <td><xsl:value-of select="@sq"/></td>
      <td><xsl:value-of select="@owner"/></td>
      <td><xsl:value-of select="@part"/></td>
      <td><xsl:value-of select="@voices"/></td>
      <td><xsl:value-of select="@reg"/></td>
    </tr>
  </xsl:for-each>

  </table>

  <p class="razr"/>
  </body>
  </html>

</xsl:template>

</xsl:stylesheet>
