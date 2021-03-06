<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../templates/view.xsl"/>	
	<xsl:output method="html" encoding="utf-8" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" indent="yes"/>
	<xsl:variable name="skin" select="request/@skin"/>
	<xsl:variable name="actionbar" select="//actionbar"/>
	<xsl:variable name="query" select="//query"/>
	<xsl:variable name="useragent" select="/request/@useragent"/>
	<xsl:template match="/request">
		<html>
			<head>
				<title>
					Workflow документооборот - <xsl:value-of select="outline/*/entry[@current]/@caption"/>
				</title>
				<link type="text/css" rel="stylesheet" href="classic/css/outline.css"/>
				<link type="text/css" rel="stylesheet" href="classic/css/main.css"/>
				<link type="text/css" rel="stylesheet" href="/SharedResources/jquery/jquery-ui-1.11.2.custom/jquery-ui.min.css"/>
				<link type="text/css" rel="stylesheet" href="/SharedResources/jquery/jquery-ui-1.11.2.custom/jquery-ui.theme.min.css"/>
				<link type="text/css" rel="stylesheet" href="/SharedResources/jquery/js/hotnav/jquery.hotnav.css"/>
				<script type="text/javascript" src="/SharedResources/jquery/js/jquery-2.1.3.js"/>
				<script type="text/javascript" src="/SharedResources/jquery/jquery-ui-1.11.2.custom/jquery-ui.min.js"/>
				<script type="text/javascript" src="/SharedResources/jquery/js/cookie/jquery.cookie.js"/>
				<script type="text/javascript" src="/SharedResources/jquery/js/hotnav/jquery.hotkeys.js"/>
				<script type="text/javascript" src="/SharedResources/jquery/js/hotnav/jquery.hotnav.js"/>
				<script type="text/javascript" src="/SharedResources/jquery/js/scrollTo/scrollTo.js"/>
				<script type="text/javascript" src="/SharedResources/jquery/js/tiptip/jquery.tipTip.js"/>
				<script type="text/javascript" src="classic/scripts/outline.js"/>
				<script type="text/javascript" src="classic/scripts/view.js"/>
				<script type="text/javascript" src="classic/scripts/form.js"/>
				<script type="text/javascript" src="classic/scripts/page.js"/>
				<script type="text/javascript">
					function onLoadActions(){
						<xsl:choose>
							<xsl:when test="currentview/@type='search'">
								outline.type = 'search'; 
								outline.keyword = '<xsl:value-of select="currentview/@keyword"/>';
							</xsl:when>
							<xsl:when test="current/@type='edit'">
								outline.type = '<xsl:value-of select="current/@type"/>'; 
								outline.viewid = '<xsl:value-of select="current/@id"/>';
								outline.docid = <xsl:value-of select="current/@key"/>;
								outline.element = 'project';
								outline.command='<xsl:value-of select="current/@command"/>';
								outline.category = '';
							</xsl:when>
							<xsl:otherwise>
								outline.type = '<xsl:value-of select="currentview/@type"/>'; 
								outline.viewid = '<xsl:value-of select="currentview/@id"/>';
								outline.command='<xsl:value-of select="currentview/@command"/>';
								outline.filterid = '<xsl:value-of select="currentview/@id"/>';
							</xsl:otherwise>
						</xsl:choose>
							outline.curlangOutline = '<xsl:value-of select="@lang"/>';
							outline.curPage = '<xsl:value-of select="currentview/@page"/>'; 
							refreshAction(); 
						$(document).bind('keydown', function(e){
 							if (e.ctrlKey) {
 								switch (e.keyCode) {
								   case 85:
								   		<!-- клавиша u -->
								     	e.preventDefault();
								     	window.location.href=$("#currentuser").attr("href")
								      	break;
								   case 81:
								   		<!-- клавиша q -->
								     	e.preventDefault();
								     	window.location.href=$("#logout").attr("href")
								      	break;
								   case 72:
								   		<!-- клавиша h -->
								     	e.preventDefault();
								     	window.location.href=$("#helpbtn").attr("href")
								      	break;
								   default:
								      	break;
								}

	    					}
    					});
						<![CDATA[
							$(document).ready(function(){
								$("#currentuser").hotnav({ keysource:function(e){ return "u"; }});
								$("#logout").hotnav({keysource:function(e){ return "q"; }});
								$("#helpbtn").hotnav({keysource:function(e){ return "h"; }});
							});
						]]>
					}
				</script>
			</head>
			<body onload="javascript:onLoadActions()" style="cursor:wait" onUnload="javascript:endLoadingOutline()">
				<div id="blockWindow"/>
				<div id="wrapper">
					<xsl:call-template name="loadingpage"/>
					<div id="outline">
						<div id="outline-header" >
							<img src="classic/img/logo_small.png" style="height:50px; margin-top:5px"/>
							<br/>
							<font style="font-size:0.80em; color:#1D5987; position:relative; top:14px">
								<xsl:value-of select="concat(outline/fields/documentmanager/@caption,' ',outline/fields/orgtitle)"/>
							</font>
							<br/>
						</div>
						<div id="outline-container">
							<xsl:for-each select="outline/entry">
								<div style="margin-bottom:10px;">
									<span style="height:10px; margin-top:5px; width:240px">								
										<img src="/SharedResources/img/classic/minus.gif" style="margin-left:6px; cursor:pointer" alt="">
											<xsl:attribute name="onclick">javascript:ToggleCategory(this)</xsl:attribute>
										</img>
										<font style="font-family:arial; font-size:0.9em; margin-left:5px">											
											<xsl:value-of select="@hint"/>
										</font>
									</span>
									<div style="clear:both;"/>
									<div class="outlineEntry">
										<xsl:for-each select="entry">
											<div class="entry" style="width:250px">
												<div class="viewlink" style="height:18px">
													<xsl:if test="@current = '1'">
														<xsl:attribute name="class">viewlink_current</xsl:attribute>										
													</xsl:if>	
													<div style="float:left">
														<xsl:choose>
															<xsl:when test="following-sibling::entry">
																<img src="/SharedResources/img/classic/tree_tee_big.gif"/>
															</xsl:when>
															<xsl:otherwise>
																<img src="/SharedResources/img/classic/tree_corner_big.gif"/>
															</xsl:otherwise>
														</xsl:choose>	
														<a href="{@url}" style="width:90%; vertical-align:top;">
															<xsl:if test="../@id = 'filters'">
																<xsl:attribute name="href"><xsl:value-of select="@url"/>&amp;filterid=<xsl:value-of select="@id"/></xsl:attribute>
															</xsl:if>
															<font class="viewlinktitle">	
																 <xsl:value-of select="@caption"/>
															</font>
														</a>
													</div>
													<xsl:if test="../@id = 'mydocs'">
														<span style=" text-align:left; float:right; ">
															<font class="countSpan" style="vertical-align:top">
																<xsl:if test="@id!=''">	
																	<xsl:attribute name="id" select="@id"/>
																</xsl:if>	
																<xsl:if test="string-length(@count)!=0">
																	<xsl:value-of select="@count"/>
																</xsl:if>												
															</font>
														</span>
													</xsl:if>
												</div>
											</div>
										</xsl:for-each>
									</div>
								</div>
							</xsl:for-each>
						</div>
					</div>
					<div style="border-left:3px solid;  width:2px; height:100%; position:absolute; left:274px; border-color:#CCCCCC"/>
					<span id="view" class="viewframe{outline/category[entry[@current=1]]/@id}">
						<div id="noserver" style="display:none; text-align:center; margin-top:10%">
							<font style='font-size:1.5em'>Отсутствует соединение с сервером</font>
							<br/>
							<img style="margin-top:10%" src='/SharedResources/img/classic/noserver.gif'/>
						</div>
					</span>
					<div class="clearfloat"/>
					<div class="empty"/>
				</div>
				<div id="footer">
					<div style="padding:2px 10px 0px 10px; color: #444444; width:600px; margin-top:5px; float:left">
						<a target="_parent" id="logout"  href="Logout" title="{outline/fields/logout/@caption}">
							<img src="/SharedResources/img/iconset/door_in.png" style="width:15px; height:15px" alt=""/>						
							<font style="margin-left:5px;font-size:11px; vertical-align:3px">
								<xsl:value-of select="outline/fields/logout/@caption"/>
							</font> 
						</a>&#xA0;
						<a target="_parent" title="Посмотреть свойства текущего пользователя"  id="currentuser" href="Provider?type=document&amp;id=userprofile" style="color: #444444 ;   font: 85%/2 Arial">
							<img src="/SharedResources/img/iconset/user_edit.png" border="none" style="width:15px; height:15px"/>								
							<font style="margin-left:5px;font-size:11px; vertical-align:3px">
								<xsl:value-of select="currentuser"/>
							</font>
						</a>
					</div>
					<div style="padding:5px 20px 0px 10px; font-color: #444444; width:300px; float:right">
						<div id="langview" style=" float:right; margin-top:1px">
							<a class="actionlink" target="blank" href="http://4ms.kz" style="color:#444444; font-size:11px;">
								<font style="margin-left:5px; font-size:11px; vertical-align:3px">Workflow документооборот © 2014</font>
							</a>  
						</div>
					</div>
				</div>
				<div id="gadget" display="none" style="display:none; width:264px; height:194px; background:url(classic/img/BLUE-base.png) no-repeat ; font-size:12px">
 					<table style="width:220px; height:154px; border-collapse:collapse; margin-top:5px; margin-left:18px; font-size:12px; font-family:Segoe UI, Tahoma;" valign="top">
						<xsl:for-each select="outline/entry[1]/entry">
							<tr id="str" style="height:10px">
								<td id="td1" style="height:10px">
									<span class="span" style="color:lightgray; height:10px">
										<a id="glink" target="blank" href="Avanti/{@url}">
											<xsl:value-of select="@caption"/>
										</a>
									</span>
								</td>
								<td id="td2" style="color:blue;">
									<xsl:value-of select="@count"/>
								</td>
								<td id="td3" style="color:orange;">
									0
								</td>
							</tr>
						</xsl:for-each>
					</table>
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>