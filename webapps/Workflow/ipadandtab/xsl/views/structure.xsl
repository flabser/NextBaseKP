<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:variable name="doctype">structure</xsl:variable>
	<xsl:output method="html" encoding="utf-8"/>	
	
	<xsl:template match="responses">
		<xsl:apply-templates mode="line"/>
	</xsl:template>

	<xsl:template match="*" mode="line">
		<xsl:if test="name(.) != 'userid'">
			<tr name="child" style="height:45px">
				<td>
					<table  width="100%" style="border-collapse:collapse">
						<xsl:attribute name="class">tbl<xsl:value-of select="@docid"/><xsl:value-of select="@doctype"/></xsl:attribute>
						<tr>
							<td>
								<xsl:if test="@doctype!=888">
									<xsl:if test="userid !=''">
										<xsl:attribute name="ondblclick">javascript:pickListSingleOk('<xsl:value-of select="userid"/>')</xsl:attribute>
									</xsl:if>
								</xsl:if>
								<xsl:call-template name="graft"/>
								<xsl:apply-templates select="." mode="item"/>
							</td>
							<xsl:if test="@hasresponse='true'">
								<xsl:apply-templates mode="line"/>
							</xsl:if>	
						</tr>
					</table>
				</td>
			</tr>
			<xsl:if test="@hasresponse !='true'">
				<xsl:apply-templates mode="line"/>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<xsl:template match="entry" mode="item">
		<xsl:if test="@hasresponse='true'">
			<xsl:choose>
        		<xsl:when test="entry[node()]">
					<a href="">
						<xsl:attribute name='id'>a<xsl:value-of select="@docid"/><xsl:value-of select="@doctype"/></xsl:attribute>
						<xsl:attribute name='href'>javascript:collapseChapter('responses',<xsl:value-of select="@docid"/>, <xsl:value-of select="@doctype"/>)</xsl:attribute>
						<img border='0'  style="width:20px" src="ipadandtab/img/toggle_minus_small.png">
							<xsl:attribute name='id'>img<xsl:value-of select="@docid"/><xsl:value-of select="@doctype"/></xsl:attribute>
						</img>
					</a>
				</xsl:when>
				<xsl:otherwise>
					<a href="">
						<xsl:attribute name='id'>a<xsl:value-of select="@docid"/><xsl:value-of select="@doctype"/></xsl:attribute>
						<xsl:attribute name='href'>javascript:expandChapter('responses',<xsl:value-of select="@docid"/>, <xsl:value-of select="@doctype"/>)</xsl:attribute>
						<img border='0' style="width:20px" src="ipadandtab/img/toggle_plus_small.png">
							<xsl:attribute name='id'>img<xsl:value-of select="@docid"/><xsl:value-of select="@doctype"/></xsl:attribute>
						</img>
					</a>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
		<xsl:if test="@doctype = 889">					
			<input type="checkbox" name="chbox">
				<xsl:attribute name="value"><xsl:value-of select="@viewtext"/></xsl:attribute>
				<xsl:attribute name="id"><xsl:value-of select="userid"/></xsl:attribute>
				<xsl:if test="userid =''">
					<xsl:attribute name="disabled">disabled</xsl:attribute>
				</xsl:if>
			</input>
		</xsl:if>
		<xsl:if test="@doctype = 892">					
			<input type="checkbox" name="chbox">
				<xsl:attribute name="value"><xsl:value-of select="@viewtext"/></xsl:attribute>
				<xsl:attribute name="id"><xsl:value-of select="userid"/></xsl:attribute>
				<xsl:if test="userid =''">
					<xsl:attribute name="disabled">disabled</xsl:attribute>
				</xsl:if>
			</input>
		</xsl:if>
		<xsl:choose>
			<xsl:when test="@doctype = 888"> 
				<img style="margin-left:5px" src="/SharedResources/img/iconset/reseller_programm_small.png"/>
			</xsl:when>
			<xsl:when test="@doctype = 889">
				<img style="margin-left:5px" src="/SharedResources/img/iconset/user_small.png">
					<xsl:if test="userid =''">
						<xsl:attribute name="style">filter: gray; margin-left:5px</xsl:attribute>
					</xsl:if>
				</img>
			</xsl:when>
			<xsl:when test="@doctype = 892">
				<img style="margin-left:5px" src="/SharedResources/img/classic/view_rvz.gif"/>
			</xsl:when>
		</xsl:choose>
		<font class="font"  style="font-family:verdana; font-size:15px; margin-left:3px">
			<xsl:attribute name="title"><xsl:value-of select="userid"/></xsl:attribute>
			<xsl:if test="userid =''">
				<xsl:attribute name="color">gray</xsl:attribute>
			</xsl:if>
			<xsl:value-of select="@viewtext"/> 
		</font>
	</xsl:template>

	<xsl:template name="graft">
		<xsl:apply-templates select="ancestor::entry" mode="tree"/>
		<xsl:choose>
			<xsl:when test="following-sibling::*">
				<img style="width:25px" src="/SharedResources/img/classic/tree_spacer.gif"/>
			</xsl:when>
			<xsl:otherwise>
				<img style="width:25px" src="/SharedResources/img/classic/tree_spacer.gif"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="*" mode="tree">
		<xsl:choose>
			<xsl:when test="following-sibling::*">
				<xsl:if test="not(following-sibling::*[@doctype=891])">
					<img style="width:25px" src="/SharedResources/img/classic/tree_spacer.gif"/>
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<img style="width:25px" src="/SharedResources/img/classic/tree_spacer.gif"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="/request">
		<xsl:for-each select="query/entry">
			<table width="100%"  style="border-collapse:collapse; margin-top:10px; ; margin-left:10px; line-height:25px">
				<xsl:attribute name="class">
					tbl<xsl:value-of select="@docid"/><xsl:value-of select="@doctype"/> 
				</xsl:attribute>
				<tr>
					<td>
						<xsl:if test="@hasresponse='true'">
							<xsl:choose>
          						<xsl:when test="responses[node()]">
									<a href="" style="margin-left:3px">
										<xsl:attribute name='id'>a<xsl:value-of select="@docid"/><xsl:value-of select="@doctype"/></xsl:attribute>
										<xsl:attribute name='href'>javascript:collapseChapter('responses',<xsl:value-of select="@docid"/>, <xsl:value-of select="@doctype"/>)</xsl:attribute>
										<img border='0' style="width:20px" src="ipadandtab/img/toggle_minus_small.png">
											<xsl:attribute name='id'>img<xsl:value-of select="@docid"/><xsl:value-of select="@doctype"/></xsl:attribute>
										</img>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="" style="margin-left:3px">
										<xsl:attribute name='id'>a<xsl:value-of select="@docid"/><xsl:value-of select="@doctype"/></xsl:attribute>
										<xsl:attribute name='href'>javascript:expandChapter('responses',<xsl:value-of select="@docid"/>, <xsl:value-of select="@doctype"/>)</xsl:attribute>
										<img border='0'  style="width:20px" src="ipadandtab/img/toggle_plus_small.png">
											<xsl:attribute name='id'>img<xsl:value-of select="@docid"/><xsl:value-of select="@doctype"/></xsl:attribute>
										</img>
									</a>
								</xsl:otherwise>
							</xsl:choose>
							<font class="font"  style="font-family:verdana; font-size:15px; margin-left:10px; vertical-align:3px ">
								<xsl:value-of select="@viewtext"/>
							</font>
						</xsl:if>
						<xsl:if test="not(@hasresponse='true')">
							<img style="margin-left:17px" src="/SharedResources/img/iconset/building_small.png"/><font class="font"  style="font-family:verdana; font-size:16px; margin-left:3px"><xsl:value-of select="@viewtext"/></font>
						</xsl:if>
					</td>
				</tr>
				<xsl:apply-templates select="responses" />
			</table>
		</xsl:for-each>
	</xsl:template>	
</xsl:stylesheet>