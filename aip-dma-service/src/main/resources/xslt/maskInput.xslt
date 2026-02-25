<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:param name="lstMaskKey" />
	<xsl:param name="maskValue" />

	<xsl:template
		match="*[contains($lstMaskKey, local-name())]">
		<xsl:element name="{local-name()}">
			<xsl:value-of select="$maskValue" />
		</xsl:element>
	</xsl:template>

	<!-- copy all document -->
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>