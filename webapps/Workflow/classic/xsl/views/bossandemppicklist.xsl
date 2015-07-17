<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:variable name="doctype">structure</xsl:variable>
	<xsl:output method="html" encoding="utf-8"/>	
	<xsl:variable name="skin" select="request/@skin"/>
	<xsl:template match="/request/history"/>
	<xsl:template match="/request[@id !='signers'][@id !='workdocsigners'][@id!='admdepemployers'][@id!='coordinatorsworkdocdept']/query/entry/viewtext"/>
	<xsl:template match="/request[@id !='signers'][@id !='workdocsigners'][@id!='admdepemployers'][@id!='coordinatorsworkdocdept']/query/entry/responses">
		<xsl:for-each select="descendant::entry">
			<xsl:sort select="@viewtext"/>
			<xsl:if test="@doctype = 889 and viewtext2 != '-1'">
				<div style="cursor:pointer; text-align:left" name="itemStruct">
						<xsl:if test="userid != ''">
							<xsl:attribute name="onmouseover">javascript:entryOver(this)</xsl:attribute>
							<xsl:attribute name="onmouseout">javascript:entryOut(this)</xsl:attribute>
							<xsl:attribute name="onclick">javascript:selectItemPicklist(this,event)</xsl:attribute>
							<xsl:attribute name="ondblclick">javascript:pickListSingleOk('<xsl:value-of select="userid"/>')</xsl:attribute>
						</xsl:if>
						<input class='chbox' type="hidden" name="chbox" id="{userid}" value="{@viewtext}"/>
						<input type="checkbox" name="chbox" id="{userid}" value="{@viewtext}">
							<xsl:if test="userid =''">
								<xsl:attribute name="disabled">disabled</xsl:attribute>
							</xsl:if>
						</input>	
						<font class="font" title="{userid}" style="font-family:verdana; font-size:13px; margin-left:2px; vertical-align:2px">
							<xsl:if test="userid = ''">
								<xsl:attribute name="color">gray</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="viewtext"/>
						</font>
					</div>
				</xsl:if>
			</xsl:for-each>
		</xsl:template>

		<xsl:template match="/request[@id='workdocsigners']/query">
			<xsl:if test="../@id = 'workdocsigners'">
				<xsl:for-each select="descendant::entry">
					<xsl:sort select="viewcontent/viewtext1"/>
					<xsl:if test="@doctype = 889  and viewtext2 != '-1'">
						<div style="display:block; width:100%; cursor:pointer; text-align:left" name="itemStruct">
							<xsl:if test="userid !=''">
								<xsl:attribute name="onmouseover">javascript:entryOver(this)</xsl:attribute>
								<xsl:attribute name="onmouseout">javascript:entryOut(this)</xsl:attribute>
								<xsl:attribute name="onclick">javascript:selectItemPicklist(this,event)</xsl:attribute>
								<xsl:attribute name="ondblclick">javascript:pickListSingleOk('<xsl:value-of select="userid"/>')</xsl:attribute>
							</xsl:if>
							<input class='chbox' type="hidden" name="chbox" id="{userid}" value="{viewcontent/viewtext}"/>
							<input type="checkbox" name="chbox" id="{userid}" value="{viewcontent/viewtext}">
								<xsl:if test="userid =''">
									<xsl:attribute name="disabled" select="'disabled'"/>
								</xsl:if>
							</input>	
							<font class="font" title="{userid}" style="font-family:verdana; font-size:13px; margin-left:2px;">
								<xsl:if test="userid = ''">
									<xsl:attribute name="color" select="'gray'"/>
								</xsl:if>
								<xsl:value-of select="viewcontent/viewtext"/>
							</font>
						</div>
					</xsl:if>
				</xsl:for-each>
			</xsl:if>
		</xsl:template>

		<xsl:template match="/request[@id='coordinatorsworkdocdept']/query">
			<xsl:if test="../@id = 'coordinatorsworkdocdept'">
				<xsl:for-each select="descendant::entry">
					<xsl:sort select="viewcontent/viewtext1"/>
					<xsl:if test="@doctype = 889  and viewcontent/viewtext2 != '-1'">
						<div style="display:block; width:100%; cursor:pointer; text-align:left" name="itemStruct">
							<xsl:if test="userid !=''">
								<xsl:attribute name="onmouseover">javascript:entryOver(this)</xsl:attribute>
								<xsl:attribute name="onmouseout">javascript:entryOut(this)</xsl:attribute>
								<xsl:attribute name="onclick">javascript:selectItemPicklist(this,event)</xsl:attribute>
								<xsl:attribute name="ondblclick">javascript:pickListSingleOk('<xsl:value-of select="userid"/>')</xsl:attribute>
							</xsl:if>
							<input class='chbox' type="hidden" name="chbox" id="{userid}" value="{viewcontent/viewtext}"/>
							<input type="checkbox" name="chbox" id="{userid}" value="{viewcontent/viewtext}">
								<xsl:if test="userid =''">
									<xsl:attribute name="disabled" select="'disabled'"/>
								</xsl:if>
							</input>
							<font class="font" title="{userid}" style="font-family:verdana; font-size:13px; margin-left:2px;">
								<xsl:if test="userid = ''">
									<xsl:attribute name="color" select="'gray'"/>
								</xsl:if>
								<xsl:value-of select="viewcontent/viewtext"/>
							</font>
						</div>
					</xsl:if>
				</xsl:for-each>
			</xsl:if>
		</xsl:template>

		<xsl:template match="/request[@id='signers']/query">
			<xsl:if test="../@id = 'signers'">
				<xsl:for-each select="descendant::entry">
					<xsl:sort select="viewcontent/viewtext1"/>
					<xsl:if test="@doctype = 889  and viewtext2 != '-1'">
						<div style="display:block; width:100%; cursor:pointer; text-align:left" name="itemStruct">
							<xsl:if test="userid !=''">
								<xsl:attribute name="onmouseover">javascript:entryOver(this)</xsl:attribute>
								<xsl:attribute name="onmouseout">javascript:entryOut(this)</xsl:attribute>
								<xsl:attribute name="onclick">javascript:selectItemPicklist(this,event)</xsl:attribute>
								<xsl:attribute name="ondblclick">javascript:pickListSingleOk('<xsl:value-of select="userid"/>')</xsl:attribute>
							</xsl:if>
							<input class='chbox' type="hidden" name="chbox" id="{userid}" value="{viewcontent/viewtext}"/>
							<input type="checkbox" name="chbox" id="{userid}" value="{viewcontent/viewtext}">
								<xsl:if test="userid =''">
									<xsl:attribute name="disabled" select="'disabled'"/>
								</xsl:if>
							</input>	
							<font class="font" title="{userid}" style="font-family:verdana; font-size:13px; margin-left:2px">
								<xsl:if test="userid = ''">
									<xsl:attribute name="color" select="'gray'"/>
								</xsl:if>
								<xsl:value-of select="viewcontent/viewtext"/>
							</font>
						</div>
					</xsl:if>
				</xsl:for-each>
			</xsl:if>
		</xsl:template>

		<xsl:template match="/request[@id='admdepemployers']/query">
            <xsl:for-each select="descendant::entry[ancestor-or-self::entry/@viewtext='Административно-правовой департамент']">
            <xsl:sort select="@viewtext"/>
                <xsl:if test="@doctype = 889  and viewtext2 != '-1'">
                    <div style="cursor:pointer; text-align:left" name="itemStruct">
                        <xsl:if test="userid != ''">
                            <xsl:attribute name="onmouseover">javascript:entryOver(this)</xsl:attribute>
                            <xsl:attribute name="onmouseout">javascript:entryOut(this)</xsl:attribute>
                            <xsl:attribute name="onclick">javascript:selectItemPicklist(this,event)</xsl:attribute>
                            <xsl:attribute name="ondblclick">javascript:pickListSingleOk('<xsl:value-of select="userid"/>')</xsl:attribute>
                        </xsl:if>
                        <input class='chbox' type="hidden" name="chbox" id="{userid}" value="{@viewtext}"/>
                        <input type="checkbox" name="chbox" id="{userid}" value="{@viewtext}">
                            <xsl:if test="userid =''">
                                <xsl:attribute name="disabled">disabled</xsl:attribute>
                            </xsl:if>
                        </input>
                        <font class="font" title="{userid}" style="font-family:verdana; font-size:13px; margin-left:2px; vertical-align:2px">
                            <xsl:if test="userid = ''">
                                <xsl:attribute name="color">gray</xsl:attribute>
                            </xsl:if>
                            <xsl:value-of select="viewtext"/>
                        </font>
                    </div>
                </xsl:if>
            </xsl:for-each>
		</xsl:template>
	</xsl:stylesheet>