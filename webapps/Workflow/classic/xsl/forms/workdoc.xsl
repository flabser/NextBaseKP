<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../templates/form.xsl"/>
	<xsl:import href="../templates/sharedactions.xsl"/>
	<xsl:variable name="doctype" select="request/document/captions/doctypemultilang/@caption"/>
	<xsl:variable name="threaddocid" select="document/@docid"/>
	<xsl:variable name="captions" select="/request/document/captions"/>
	<xsl:variable name="fields" select="/request/document/fields"/>
	<xsl:variable name="editmode">readonly</xsl:variable>
	<xsl:variable name="status" select="/request/document/@status"/>
	<xsl:variable name="path" select="/request/@skin"/>
	<xsl:output method="html" encoding="utf-8" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" indent="yes"/>
	<xsl:variable name="skin" select="request/@skin"/>
	<xsl:template match="/request">
		<html>
			<head>
				<title>
					<xsl:value-of select="concat('Workflow документооборот - ',document/fields/title)"/>
				</title>
				<xsl:call-template name="cssandjs"/>
				<style>
					@media print { 
					   #docwrapper{position: static !important}
						
					  .printcoord  .ui-tabs-nav,.printcoord .formtitle,.printcoord #resizer,.printcoord button,.printcoord #outline,.printcoord #documentheader{
					  	display:none;
					  } 
					 .printcoord  #docwrapper div.formwrapper div#tabs div#tabs-6 div#printCoordTitle{
					  	display:block !important;
					  }
					  .printdocument  #docwrapper div.formwrapper div#tabs div#tabs-6 div#printCoordTitle{
					  	display:none !important;
					  }
					  #cke_MyTextarea{
					  	display:none;
					  }
				    .printdocument FORM{
				    	width:100%;
				    	font-size: 11px !important;
				    }
					     .printdocument  .ui-tabs-nav,.printdocument #resizer,.printdocument button,.printdocument #outline{
					     	display:none;
					     }
					     .printdocument .formtitle{
					     	margin: 90px 0 0 10px;
					     }
					     #currentuser,#logout,#helpbtn{
					     	display:none;
					     }
					     .doclink{
					     	color:black !important;
					     }
					     .printdocument .td_noteditable, .printdocument .td_editable, .printdocument .textarea_noteditable, .printdocument .textarea_editable{
					     	color:black !important;
					     	background:#fff !important;
					     	border:none !important;
					     	width:100% !important;
					     }
					     .printdocument .fc{
					     	width:26% !important;
					     }
					     .printdocument .ui-tabs-panel{
					     	display:block !important;
					     	height:auto !important;
					     	padding-top:5px !important;
					     }
					     .printdocument #coordTableView, #printcoord{
					     	display:none;
					     }
					     #htmlcodenoteditable{
					     	display:none !important;
					     }
					     #textprintpreview{
					     	height:auto !important;
					     	display:block !important;
					     	overflow: visible!important;
					     }
					      .printdocument #tabs-1{
					     	height:auto !important;
					     }
					     .printdocument #tabs-2{
					     	height:auto !important;
					     }
					    .printdocument  #tabs-3{
					     	display:none !important;
					     }
					     .printdocument  #tabs-4{
					     	display:none !important;
					     }
					     
				    }
  				</style>
   				<xsl:call-template name="markisread"/>
   				<xsl:if test="$editmode = 'edit'">
					<xsl:call-template name="htmlareaeditor"/>
				</xsl:if>
   				<script type="text/javascript">
   					$(document).ready(function(){hotkeysnav()})
   					<![CDATA[
   						function hotkeysnav() {
							$(document).bind('keydown', function(e){
			 					if (e.ctrlKey) {
			 						switch (e.keyCode) {
									   case 66:
									   		<!-- клавиша b -->
									     	e.preventDefault();
									     	$("#canceldoc").click();
									      	break;
									   case 69:
									   		<!-- клавиша e -->
									     	e.preventDefault();
									     	$("#btnexecution").click();
									      	break;
									   case 71:
									  		<!-- клавиша g -->
									     	e.preventDefault();
									     	$("#btngrantaccess").click();
									      	break;
									   case 87:
									  		<!-- клавиша w -->
									     	e.preventDefault();
									     	$("#btnremind").click();
									      	break;
									   case 83:
									   		<!-- клавиша s -->
									     	e.preventDefault();
									     	$("#btnsavedoc").click();
									      	break;
									   case 84:
									   		<!-- клавиша t -->
									     	e.preventDefault();
									     	$("#btnnewkr").click();
									      	break;
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
									   case 73:
									   		<!-- клавиша i -->
									     	e.preventDefault();
									     	$("#btnnewish").click();
									      	break;
									   default:
									      	break;
									}
			   					}
							});
							$("#canceldoc").hotnav({keysource:function(e){ return "b"; }});
							$("#btncoordyes").hotnav({keysource:function(e){ return "y"; }});
							$("#btngrantaccess").hotnav({keysource:function(e){ return "g"; }});
							$("#btnremind").hotnav({keysource:function(e){ return "w"; }});
							$("#btnsavedoc").hotnav({keysource:function(e){ return "s"; }});
							$("#btnexecution").hotnav({keysource:function(e){ return "e"; }});
							$("#btnnewkr").hotnav({keysource:function(e){ return "t"; }});
							$("#currentuser").hotnav({ keysource:function(e){ return "u"; }});
							$("#logout").hotnav({keysource:function(e){ return "q"; }});
							$("#helpbtn").hotnav({keysource:function(e){ return "h"; }});
							$("#btnnewish").hotnav({keysource:function(e){ return "i"; }});
						}
					]]>
				</script>
			</head>
			<body>
				<div id="docwrapper">
					<xsl:call-template name="documentheader"/>	
					<div class="formwrapper">
						<div class="formtitle">
							<div style="float:left" class="title">
								<xsl:value-of select="document/fields/title"/><span id="whichreadblock">Прочтен</span>											
				   			</div>
				   			<div style="float:right; padding-right:5px"></div>	
						</div>	
						<div class="button_panel">
							<span style="width:82% ; vertical-align:12px; float:left">
								<xsl:call-template name="showxml"/>
								<xsl:call-template name="get_document_accesslist"/>
								<xsl:call-template name="newkr"/>
								<xsl:call-template name="newki"/>
								<xsl:call-template name="newworkdocprj"/>
								<xsl:call-template name="acquaint"/>
								<xsl:call-template name="ECPsign"/>
								<xsl:call-template name="printdocument"/>
							</span>
							<span style="float:right">
								<xsl:call-template name="cancelwithjson"/>
			     			</span>	
						</div>
						<div style="clear:both"/>
						<div style="-moz-border-radius:0px;height:1px; width:100%; margin-top:10px;"/>
						<div style="clear:both"/>
						<div id="tabs">
							<ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
								<li class="ui-state-default ui-corner-top">
									<a href="#tabs-1"><xsl:value-of select="document/captions/properties/@caption"/></a>
								</li>
								<li class="ui-state-default ui-corner-top">
									<a href="#tabs-2"><xsl:value-of select="document/captions/content/@caption"/></a>
								</li>
								<li class="ui-state-default ui-corner-top">
									<a href="#tabs-3"><xsl:value-of select="document/captions/progress/@caption"/></a>
								</li>
								<li class="ui-state-default ui-corner-top">
									<a href="#tabs-4"><xsl:value-of select="document/captions/attachments/@caption"/></a>
									<img id="loading_attach_img" style="vertical-align:-8px; margin-left:-10px; padding-right:3px; visibility:hidden" src="/SharedResources/img/classic/ajax-loader-small.gif"></img>
								</li>
								<xsl:if test="document/fields/coordination">
									<li class="ui-state-default ui-corner-top">
										<a href="#tabs-6">
											<xsl:value-of select="$captions/coordination/@caption"/>
										</a>
									</li>
								</xsl:if>
								<li class="ui-state-default ui-corner-top">
									<a href="#tabs-5"><xsl:value-of select="document/captions/additional/@caption"/></a>
								</li>
								<xsl:call-template name="authorInfo"/>
							</ul>
							<form action="Provider" name="frm" method="post" id="frm" enctype="application/x-www-form-urlencoded">
								<div class="ui-tabs-panel" id="tabs-1">
									<br/>
									<table width="100%" border="0">
										<xsl:if test="document/fields/link/entry !=''">
											<tr>
												<td width="30%" class="fc"><xsl:value-of select="document/captions/projectdoc/@caption"/> :</td>
							            		<td>
													<a class="doclink" href="{document/fields/link/entry/@url}">
														<xsl:value-of select="document/fields/link/entry"/>
													</a>
				                           		</td>   					
											</tr>
										</xsl:if>	
										<tr>
											<td width="30%" class="fc">№ :</td>
						            		<td>
			                                	<input type="text" name="vn" value="{document/fields/vn}" size="10" class="td_noteditable" onfocus="javascript:$(this).blur()" readonly="readonly" style="width:80px;"/>
			                                		&#xA0;
			                                    <xsl:value-of select="document/captions/dvn/@caption"/>&#xA0;
			                                    <input type="text" name="dvn" class="td_noteditable" value="{substring(document/fields/dvn,1,10)}" onfocus="javascript: $(this).blur()" readonly="readonly" style="width:80px;"/>
			                           		</td>   					
										</tr>
										<tr>
											<td class="fc">
												<xsl:value-of select="document/captions/signer/@caption"/> :
											</td>
											<td>
												<input size="53" type="text" value="{document/fields/signer}" id="signer" onfocus="javascript:$(this).blur()" class="td_noteditable" readonly="readonly" style="width:600px;"/>                   			              		
												<input type="hidden" id="signer"/>                   			              		
											</td>
									    </tr>
									 	<!-- <tr>
											<td class="fc">
												<xsl:value-of select="document/captions/recipient/@caption"/> :
											</td>
											<td>
												<table id="recipienttbl" style="border-spacing:0px 3px;">
													<xsl:for-each select="document/fields/recipient/employer">
														<tr>
															<td class="td_editable" style="width:600px;">
																<xsl:if test="$editmode != 'edit'">
																	<xsl:attribute name="class">td_noteditable</xsl:attribute>
																</xsl:if>
																<xsl:value-of select="fullname"/>
																&#xA0;
																<input type="hidden" id="recipient" name="recipient" value="{userid}" />
															</td>
														</tr>
													</xsl:for-each>
												</table>
											</td>
										</tr> -->
										<tr>
											<td class="fc">
												<xsl:value-of select="document/captions/briefcontent/@caption"/> :
											</td>
											<td>
												<div>						
													<textarea name="briefcontent" style="width:748px;" rows="3" class="textarea_noteditable" readonly="readonly">
														<xsl:value-of select="document/fields/briefcontent"/>
													</textarea>								
												</div>
											</td>
										</tr>
										<tr>
											<td class="fc">
												<xsl:value-of select="document/captions/vnish/@caption"/> :
											</td>
											<td>
												<a>
													<xsl:attribute name="href">Provider?type=edit&amp;element=document&amp;id=out&amp;docid=<xsl:value-of select="document/vnish"/></xsl:attribute>
													<xsl:value-of select="vnish/item/@alt"/>
												</a>																
											</td>
										</tr>
										<tr>
											<td class="fc">
												<xsl:value-of select="document/captions/ctrldate/@caption"/> :
											</td>
											<td>
												<input type="text" id="ctrldate" value="{substring(document/fields/ctrldate,1,10)}" name="ctrldate" readonly="readonly" class="td_noteditable" onfocus="javascript: $(this).blur()" style="background:#eee; width:80px; padding:3px 3px 3px 5px; border:1px solid #eee"/>
											</td>
										</tr>						
									</table>		
								</div>
								<div id="tabs-2" style="height:500px">
									<br/>
									<table>
										<tr>
											<td class="fc"><xsl:value-of select="document/captions/content/@caption"/> :</td>
											<td>
												<div id="htmlcodenoteditable" class="textarea_noteditable" style="width:748px; height:500px">
												</div>
												<script>
													$("#htmlcodenoteditable").html("<xsl:value-of select='document/fields/contentsource'/>");
												</script>
											</td>
										</tr>
										<tr>
											<td colspan="2">
											<div id="textprintpreview" style="display:none; overflow:visible">
												</div>
												<script>
													$("#textprintpreview").html("<xsl:value-of select='document/fields/contentsource'/>");
												</script>
											</td>
										</tr>
									</table>
								</div>
								<div id="tabs-3" style="height:500px">
									<xsl:if test="document/@status !='new'">
										<div display="block" style="display:block; width:90%" id="execution">
											<br/>
											<font style="font-style:arial; font-size:13px">
												<b><xsl:value-of select="document/@viewtext"/></b>
											</font>
											<table id="executionTbl" style="width:90%"/>
											<script>
												$.ajax({
													url: 'Provider?type=view&amp;id=docthread&amp;parentdocid=<xsl:value-of select="document/@docid"/>&amp;parentdoctype=<xsl:value-of select="document/@doctype"/>',
													datatype:'html',
													async:'true',
													success: function(data) {
														$("#executionTbl").append(data)
														$("#executionTbl a").css("font-size","12px")
														$("#executionTbl tr").css("width","600px")
													}
												});
											</script>
											<br/>
										</div>
									</xsl:if>
								</div>
							</form>
							<div id="tabs-4" style="height:500px">
								<form action="Uploader" name="upload" id="upload" method="post" enctype="multipart/form-data">
									<input type="hidden" name="type" value="rtfcontent"/>
									<input type="hidden" name="formsesid" value="formsesid"/>
									<xsl:call-template name="attach"/>
								</form>
							</div>
							<div id="tabs-5">
								<xsl:call-template name="docinfo"/>
							</div>
							<xsl:if test="document/fields/coordination">
								<div id="tabs-6">
										<div id='printCoordTitle' style="display:none; width:100%">Ход согласования документа: <br/><br/>
										 <b style='margin-left:50px;'><xsl:value-of select="document/fields/title"/></b><br/>
										 <font style='margin-left:50px'>Краткое содержание: </font><b><xsl:value-of select="document/fields/briefcontent"/></b>
										 </div>
										 <button id="printcoord" title="Печать согласования" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"  style="margin-right:5px" autocomplete="off">
											<xsl:attribute name="onclick">javascript:PrintCoord()</xsl:attribute>
											<span>
												<img src="/SharedResources/img/iconset/printer.png" class="button_img"/>
												<font class="button_text">Печать согласования</font>
											</span>
										</button>
										<br/>
										<br/>
										<table id="coordTableView" style="border-collapse:collapse; margin-left:3px; width:100%" class="table-border-gray">
											<tr style="text-align:center;">
												<td width="2%">№</td>
												<td width="10%">
													<xsl:value-of select="$captions/type/@caption"/>
												</td>
												<td width="61%">
													<xsl:value-of select="$captions/contributors/@caption"/>
												</td>
												<td width="9%">
													<xsl:value-of select="$captions/waittime/@caption"/>
												</td>
												<td width="10%">
													<xsl:value-of select="$captions/statuscoord/@caption"/>
												</td>
											</tr>
											<xsl:for-each select="$fields/coordination/blocks/entry">
													<tr class="trblockCoord">
														<td style="text-align:center; border-bottom: 1px solid lightgray">
															<xsl:value-of select="position()"/>
															<xsl:for-each select="coordinators/entry">
																<br/>
															</xsl:for-each>
														</td>
														<td style="border-bottom: 1px solid lightgray; text-align:center">
															<xsl:choose>
																<xsl:when test="type = 'PARALLEL_COORDINATION'">
																	<xsl:value-of select="$captions/parcoord/@caption"/>
																</xsl:when>
																<xsl:when test="type = 'SERIAL_COORDINATION'">
																	<xsl:value-of select="$captions/sercoord/@caption"/>
																</xsl:when>
																<xsl:when test="type = 'TO_SIGN'">
																	<xsl:value-of select="$captions/tosign/@caption"/>
																</xsl:when>
																<xsl:otherwise>
																	<xsl:value-of select="$captions/typenotdefined/@caption"/>
																</xsl:otherwise>
															</xsl:choose>
															<xsl:for-each select="coordinators/entry">
																<br/>
															</xsl:for-each>
														</td>
														<td style="border-bottom: 1px solid lightgray">
															<table style="width:100%">
																<xsl:for-each select="coordinators/entry">
																	<tr>
																		<td style="border:none; padding:5px 0px !important; vertical-align:top">
																			<xsl:value-of select="employer/fullname"/>
																			<p style="margin:0px">
																				<xsl:choose>
																					<xsl:when test="decision='AGREE'">
																						<xsl:value-of select="concat(decisiondate,' ')"/>
																						<b><xsl:value-of select="$captions/agree/@caption"/></b>
																					</xsl:when>
																					<xsl:when test="decision='DISAGREE'">
																						<xsl:value-of select="concat(decisiondate,' ')"/>
																						<b><xsl:value-of select="$captions/disagree/@caption"/></b>
																					</xsl:when>
																					<xsl:when test="iscurrent='true'">
																						<xsl:value-of select="$captions/awairesponse/@caption"/>
																					</xsl:when>
																				</xsl:choose>
																			</p>
																			<div class="coord_comment_box close">
																				<div class='coord_comment_txt' style="font-style:italic">
																					<xsl:choose>
																						<xsl:when test="comment = 'null'">
																						</xsl:when>
																						<xsl:when test="string-length(comment)!= 0">
			                                                                                 комментарий: <xsl:value-of select="comment"/>
																						</xsl:when>
																					</xsl:choose>
																				</div>
																			</div>
																		</td>
																	</tr>
																</xsl:for-each>
															</table>
														</td>
														<td style="border-bottom: 1px solid lightgray; text-align:center">
															<xsl:choose>
																<xsl:when test="delaytime = 0">
																	<xsl:value-of select="$captions/unlimtimecoord/@caption"/>
																</xsl:when>
																<xsl:otherwise>
																	<xsl:value-of select="delaytime"/>
																</xsl:otherwise>
															</xsl:choose>
															<xsl:for-each select="coordinators/entry">
																<br/>
															</xsl:for-each>
														</td>
														<td style="border-bottom: 1px solid lightgray; text-align:center">
															<xsl:if test="type !='TO_SIGN'">
																<xsl:choose>
																	<xsl:when test="status ='COORDINATING'">
																		<xsl:value-of select="$captions/oncoordinating/@caption"/>
																	</xsl:when>
																	<xsl:when test="status ='COORDINATED'">
																		<xsl:value-of select="$captions/complete/@caption"/>
																	</xsl:when>
																	<xsl:when test="status ='AWAITING'">
																		<xsl:value-of select="$captions/expectbegincoord/@caption"/>
																	</xsl:when>
																	<xsl:when test="status ='EXPIRED'">
																		<xsl:value-of select="$captions/prosrochen/@caption"/>
																	</xsl:when>
																	<xsl:when test="status ='UNDEFINED'">
																		<xsl:value-of select="$captions/undefined/@caption"/>
																	</xsl:when>
																	<xsl:otherwise>
																		<xsl:value-of select="$captions/undefined/@caption"/>
																	</xsl:otherwise>
																</xsl:choose>
															</xsl:if>
															<xsl:if test="type ='TO_SIGN'">
																<xsl:choose>
																	<xsl:when test="$fields/coordination/status = 'SIGNING'">
																		<xsl:value-of select="$captions/waitingsign/@caption"/>
																	</xsl:when>
																	<xsl:when test="$fields/coordination/status = 'SIGNED'">
																		<xsl:value-of select="$captions/signed/@caption"/>
																	</xsl:when>
																	<xsl:when test="$fields/coordination/status = 'REJECTED'">
																		<xsl:value-of select="$captions/rejected/@caption"/>
																	</xsl:when>
																</xsl:choose>
															</xsl:if>
															<xsl:for-each select="coordinators/entry">
																<br/>
																<input type="hidden" value="{employer/userid}" class="{employer/userid}"/>
															</xsl:for-each>
															<xsl:if test="type !='TO_SIGN'">
																<input type="hidden" name="coordblock">
																	<xsl:attribute name="value"><xsl:value-of select="id"/>`<xsl:choose><xsl:when test="type='PARALLEL_COORDINATION'">par</xsl:when><xsl:when
																		test="type='SERIAL_COORDINATION'">ser</xsl:when></xsl:choose>`<xsl:value-of
																		select="delaytime" />`<xsl:for-each select="coordinators/entry"><xsl:value-of
																		select="employer/userid"/><xsl:if test="following-sibling::*">^</xsl:if></xsl:for-each>`<xsl:value-of
																		select="status"/></xsl:attribute>
																</input>
															</xsl:if>
														</td>
													</tr>
											</xsl:for-each>
										</table>
										<br/>
									</div>
								</xsl:if>
						</div>
						<div style="height:10px"/>
	    	   		</div>
	    	    </div>
	    	    <xsl:call-template name="ECPsignFields"/>
	    	   	<xsl:call-template name="formoutline"/>
	    	   	<input type="hidden" id="titlefield" value="{document/fields/title}"/>
			</body>	
		</html>
	</xsl:template>
</xsl:stylesheet>