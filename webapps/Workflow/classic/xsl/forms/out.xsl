<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../templates/form.xsl"/>
	<xsl:import href="../templates/sharedactions.xsl"/>
	<xsl:variable name="doctype" select="request/document/fields/title"/>
	<xsl:variable name="path" select="/request/@skin"/>
	<xsl:variable name="captions" select="/request/document/captions"/>
	<xsl:variable name="fields" select="/request/document/fields"/>
	<xsl:variable name="threaddocid" select="document/@docid"/>
	<xsl:variable name="skin" select="request/@skin"/>
	<xsl:variable name="editmode" select="/request/document/@editmode"/>
	<xsl:variable name="status" select="/request/document/@status"/>
	<xsl:output method="html" encoding="utf-8" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" indent="yes"/>
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
					 .printcoord  #docwrapper div.formwrapper div#tabs div#tabs-5 div#printCoordTitle{
					  	display:block !important;
					  }
					  .printdocument  #docwrapper div.formwrapper div#tabs div#tabs-6 div#printCoordTitle{
					  	display:none !important;
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
					     .ui-datepicker-trigger{
					  	display:none !important;
					  }
					  .fc IMG{
					  	display:none;
					  }
					     .printdocument .td_noteditable, .printdocument .td_editable, .printdocument .textarea_noteditable, .printdocument .textarea_editable, .printdocument .select_editable, .printdocument .select_noteditable{
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
					    #htmlcodenoteditable, #briefcontent, #cke_MyTextarea{
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
									     	window.location.href=$("#helpbtn").attr("href")
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
						<div  class="formtitle">
							<div style="float:left" class="title">
				  				<xsl:call-template name="doctitle"/><span id="whichreadblock">Прочтен</span>										
				   		 	</div>
				    		<div style="float:right; padding-right:5px"></div>
						</div>		
						<div class="button_panel">
							<span style="vertical-align:12px; float:left">
								<xsl:call-template name="showxml"/>
								<xsl:call-template name="get_document_accesslist"/>
								<xsl:call-template name="save"/>
								<xsl:call-template name="newkr"/>
								<xsl:call-template name="newki"/>
								<xsl:call-template name="acquaint"/>
<!-- 								<xsl:call-template name="ECPsign"/> -->
								<xsl:call-template name="printdocument"/>
							</span>					
				   			<span style="float:right" class="bar_right_panel">
								<xsl:call-template name="cancel"/>
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
									<a href="#tabs-3"><xsl:value-of select="document/captions/attachments/@caption"/></a>
									<img id="loading_attach_img" style="vertical-align:-8px; margin-left:-10px; padding-right:3px; visibility:hidden" src="/SharedResources/img/classic/ajax-loader-small.gif"></img>
								</li>
								<xsl:if test="document/fields/coordination">
									<li class="ui-state-default ui-corner-top">
										<a href="#tabs-5">
											<xsl:value-of select="$captions/coordination/@caption"/>
										</a>
									</li>
								</xsl:if>
								<li class="ui-state-default ui-corner-top">
									<a href="#tabs-4"><xsl:value-of select="document/captions/additional/@caption"/></a>
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
										<!-- поля "Исходящий номер" и "Дата исходящего"	-->
										<tr>
											<td  class="fc">
												<xsl:value-of select="document/captions/vn/@caption"/> № :
											</td>
									    	<td>
						                    	<input type="text" name="vn" value="{document/fields/vn}" size="10" class="td_editable" style="width:80px;">
													<xsl:if test="$editmode != 'edit' or document/fields/vn !=''">
														<xsl:attribute name="class" select="'td_noteditable'"/>
														<xsl:attribute name="readonly" select="'readonly'"/>
													</xsl:if>
												</input>
						                        &#xA0;
						                        <xsl:value-of select="document/captions/dvn/@caption"/>&#xA0;
						                       	<input type="text" class="td_noteditable" value="{substring(document/fields/dvn,1,10)}" readonly="readonly" onfocus="javascript: $(this).blur()" style="width:80px;"/>
						                   </td>   					
										</tr>
										<!-- Получатель -->
										<tr>
											<td class="fc">
												<font style="vertical-align:top">
													<xsl:value-of select="document/captions/corr/@caption"/>:
												</font>
												<!-- <xsl:if test="$editmode = 'edit'">
													<a href="">
														<xsl:attribute name="href">javascript:dialogBoxStructure('corrcat','false','corr','frm', 'corrtbl');</xsl:attribute>
														<img src="/SharedResources/img/iconset/report_magnify.png"/>
													</a>
												</xsl:if> -->
											</td>
										<td>
												<input type="text" name="corrstring" value="{document/fields/corrstring}" style="width:500px" class="td_editable">
													<xsl:if test="$editmode != 'edit'">
														<xsl:attribute name="class">td_noteditable</xsl:attribute>
													</xsl:if>
												</input>
												<!-- <table id="corrtbl" style="border-spacing:0px 3px;">
													<tr>
														<td class="td_editable" style="width:600px;">
															<xsl:if test="$editmode != 'edit'">
																<xsl:attribute name="class">td_noteditable</xsl:attribute>
															</xsl:if>
															<xsl:value-of select="document/fields/corr"/>&#xA0;
														</td>
													</tr>
												</table>
												<input type="hidden" id="corr" name="corr" value="{document/fields/corr/@attrval}" />
												<input type="hidden" id="corrcaption" value="{document/captions/corr/@caption}"/> -->
											</td>
										</tr>
										<!-- Адрес получателя -->
										<tr>
											<td class="fc">
												<font style="vertical-align:top">
													<xsl:value-of select="document/captions/addresscorr/@caption"/>:
												</font>
												<!-- <xsl:if test="$editmode = 'edit'">
													<a href="">
														<xsl:attribute name="href">javascript:dialogBoxStructure('corrcat','false','corr','frm', 'corrtbl');</xsl:attribute>
														<img src="/SharedResources/img/iconset/report_magnify.png"/>
													</a>
												</xsl:if> -->
											</td>
											<td>
												<input type="text" name="addresscorr" value="{document/fields/addresscorr}" style="width:600px" class="td_editable">
													<xsl:if test="$editmode != 'edit'">
														<xsl:attribute name="class">td_noteditable</xsl:attribute>
													</xsl:if>
												</input>
												<!-- <table id="corrtbl" style="border-spacing:0px 3px;">
													<tr>
														<td class="td_editable" style="width:600px;">
															<xsl:if test="$editmode != 'edit'">
																<xsl:attribute name="class">td_noteditable</xsl:attribute>
															</xsl:if>
															<xsl:value-of select="document/fields/corr"/>&#xA0;
														</td>
													</tr>
												</table>
												<input type="hidden" id="corr" name="corr" value="{document/fields/corr/@attrval}" />
												<input type="hidden" id="corrcaption" value="{document/captions/corr/@caption}"/> -->
											</td>
										</tr>
										<!-- Поле "Корреспондент" 
										<tr>
											<td class="fc">
												<font style="vertical-align:top">
													<xsl:value-of select="document/captions/corr/@caption"/> : 
												</font>
												<xsl:if test="$editmode = 'edit'">
													<img src="/SharedResources/img/iconset/report_magnify.png" style="cursor:pointer">
														<xsl:attribute name="onclick">javascript:dialogBoxStructure('corrcat','false','corr','frm', 'corrtbl');</xsl:attribute>
													</img>
												</xsl:if>
											</td>
											<td>
												<table id="corrtbl">
													<xsl:for-each select="document/fields/corr">
														<tr>
															<td class="td_editable" style="width:600px;">
																<xsl:if test="$editmode != 'edit'">
																	<xsl:attribute name="class">td_noteditable</xsl:attribute>
																</xsl:if>
																<xsl:value-of select="."/>&#xA0;
																<span style='float:right; border-left:1px solid #ccc; width:20px; padding-right:10px; padding-left:2px; padding-top:1px; color:#ccc; font-size:11px'><font><xsl:value-of select="document/fields/corr/@attrval"/></font></span>
																<input type="hidden" id="corr" name="corr" value="{./@attrval}"/>
															</td>
														</tr>
													</xsl:for-each>
													<xsl:if test="not(document/fields/corr)">
														<tr>
															<td class="td_editable" style="width:600px;">
																<xsl:if test="$editmode != 'edit'">
																	<xsl:attribute name="class">td_noteditable</xsl:attribute>
																</xsl:if>
																&#xA0;
															</td>
														</tr>
													</xsl:if>
												</table>
												<input type="hidden" id="corrcaption" value="{document/captions/corr/@caption}"/>
											</td>
										</tr>-->
										<!-- поле "Подписал" -->
										<tr>
											<td class="fc">
												<font style="vertical-align:top">
													<xsl:value-of select="document/captions/signedby/@caption"/> : 
												</font>
												<xsl:if test="$editmode ='edit'">
													<img src="/SharedResources/img/iconset/report_magnify.png" style="cursor:pointer">			
														<xsl:attribute name="onclick">javascript:dialogBoxStructure('signers','false','signedby','frm', 'signedbytbl');</xsl:attribute>								
													</img>
												</xsl:if>
											</td>
											<td>
												<table id="signedbytbl" >
													<tr>
														<td class="td_editable" style="width:600px;">
															<xsl:if test="$editmode != 'edit'">
																<xsl:attribute name="class">td_noteditable</xsl:attribute>
															</xsl:if>
															<xsl:value-of select="document/fields/signedby"/>&#xA0;
														</td>
													</tr>
												</table>
												<input type="hidden" id="signedby" name="signedby">
													<xsl:attribute name="value" select="document/fields/signedby/@attrval"/>
												</input>
												<input type="hidden" id="signedbycaption"  value="{document/captions/signedby/@caption}"/>	
											</td>
										</tr>
										<!-- Поле "Вид доставки" -->
										<tr>
											<td class="fc">
												<xsl:value-of select="document/captions/deliverytype/@caption"/> :
											</td>
											<td>
												<select name="deliverytype" style="width:612px;" class="select_editable">
													<xsl:if test="$editmode !='edit'">
						                        		<xsl:attribute name="disabled"/>
						                        		<xsl:attribute name="class">select_noteditable</xsl:attribute>
						                        		<option value="">
															<xsl:attribute name="selected">selected</xsl:attribute>
															<xsl:value-of select="document/fields/deliverytype"/>
														</option>
						                        	</xsl:if>
													<xsl:variable name="deliverytype" select="document/fields/deliverytype/@attrval"/>
													<xsl:for-each select="document/glossaries/deliverytype/query/entry">
														<option value="{@docid}">
															<xsl:if test="$deliverytype = @docid">
																<xsl:attribute name="selected">selected</xsl:attribute>
															</xsl:if>
															<xsl:value-of select="viewcontent/viewtext1"/>
														</option>
													</xsl:for-each>
												</select>	
											</td>
										</tr>
										<!-- Поле "Тип документа" -->
										<tr>
											<td class="fc">
												<xsl:value-of select="document/captions/vid/@caption"/> :
											</td>
											<td>
												<xsl:variable name="vid" select="document/fields/vid/@attrval"/>
												<select name="vid" class="select_editable" style="width:612px;">
													<xsl:if test="$editmode !='edit'">
						                    			<xsl:attribute name="disabled"/>
						                    			<xsl:attribute name="class">select_noteditable</xsl:attribute>
						                    			<option value="">
															<xsl:attribute name="selected">selected</xsl:attribute>
															<xsl:value-of select="document/fields/vid"/>
														</option>
						                    		</xsl:if>
													<xsl:for-each select="document/glossaries/typedoc/query/entry">
														<option value="{@docid}">
															<xsl:if test="$vid = @docid">
																<xsl:attribute name="selected">selected</xsl:attribute>
															</xsl:if>
															<xsl:value-of select="viewcontent/viewtext1"/>
														</option>
													</xsl:for-each>
												</select>	
											</td>
										</tr>	
										<!-- Поле "Язык документа" -->
										<tr>
											<td class="fc">
												<xsl:value-of select="document/captions/lang/@caption"/> :
											</td>
											<td>
												<xsl:variable name="lang" select="document/fields/lang/@attrval"/>
												<select name="lang" class="select_editable" style="width:612px;">
													<xsl:if test="$editmode !='edit'">
						                    			<xsl:attribute name="disabled"/>
						                    			<xsl:attribute name="class">select_noteditable</xsl:attribute>
						                    			<option value="">
															<xsl:attribute name="selected">selected</xsl:attribute>
															<xsl:value-of select="document/fields/lang"/>
														</option>
						                    		</xsl:if>
													<xsl:for-each select="document/glossaries/docslang/query/entry">
														<option value="{@docid}">
															<xsl:if test="$lang = @docid">
																<xsl:attribute name="selected">selected</xsl:attribute>
															</xsl:if>
															<xsl:value-of select="viewcontent/viewtext1"/>
														</option>
													</xsl:for-each>
												</select>	
											</td>
										</tr>
										<!-- Поле "Краткое содержание" -->
										<tr>
											<td class="fc">
												<xsl:value-of select="document/captions/summary/@caption"/> :
											</td>
											<td>
												<textarea name="briefcontent" rows="3" class="textarea_editable" style="width:760px">
													<xsl:if test="$editmode != 'edit'">
														<xsl:attribute name="onfocus">javascript:$(this).blur()</xsl:attribute>
														<xsl:attribute name="class">textarea_noteditable</xsl:attribute>
													</xsl:if>	
													<xsl:if test="$editmode = 'edit'">
														<xsl:attribute name="onfocus">javascript:fieldOnFocus(this)</xsl:attribute>
														<xsl:attribute name="onblur">javascript:fieldOnBlur(this)</xsl:attribute>
													</xsl:if>	
													<xsl:value-of select="document/fields/briefcontent"/>
												</textarea>							
											</td>
										</tr>
										<!-- Поле "Номенклатура дел" -->						
										<tr >
											<td class="fc" style ="padding-top:5px">
												<font style="vertical-align:top;">
													<xsl:value-of select="document/captions/nomentype/@caption"/> : 
												</font>
												<xsl:if test="$editmode ='edit'">
													<img src="/SharedResources/img/iconset/report_magnify.png" style="cursor:pointer">
														<xsl:attribute name="onclick">javascript:dialogBoxStructure('n','false','nomentype','frm', 'nomentypetbl');</xsl:attribute>								
													</img>
												</xsl:if>
											</td>
											<td>
												<table id="nomentypetbl">
													<tr>
														<td class="td_editable" style="width:600px;">
															<xsl:if test="$editmode != 'edit'">
																<xsl:attribute name="class">td_noteditable</xsl:attribute>
															</xsl:if>
															<xsl:value-of select="document/fields/nomentype"/>&#xA0;
														</td>
													</tr>
												</table>
												<input type="hidden" id="nomentype" name="nomentype" value="{document/fields/nomentype/@attrval}"/>
												<input type="hidden" id="nomentypecaption" value="{document/captions/nomentype/@caption}"/>
											</td>
										</tr>
										<!-- Поле "Номер бланка" -->
										<tr>
											<td  class="fc">
												<xsl:value-of select="document/captions/blanknumber/@caption"/> :
											</td>
							            	<td>
					                       		<input type="text" name="blanknumber" value="{document/fields/blanknumber}" style="width:149px;" onkeypress="javascript:Numeric(this)" class="td_editable">
					                       			<xsl:if test="$editmode !='edit'">
					                           			<xsl:attribute name="readonly">readonly</xsl:attribute>
					                           			<xsl:attribute name="class">td_noteditable</xsl:attribute>
					                           			<xsl:attribute name="onfocus">$(this).blur()</xsl:attribute>
					                            	</xsl:if> 
					                           		<xsl:if test="$editmode = 'edit'">
														<xsl:attribute name="onfocus">fieldOnFocus(this)</xsl:attribute>
														<xsl:attribute name="onblur">fieldOnBlur(this)</xsl:attribute>
													</xsl:if>
					                       		</input>
					                       	</td>   					
										</tr>
										<!-- Поля "количество листов" и "количество экземпляров" -->
										<tr>
											<td class="fc">
												<xsl:value-of select="document/captions/np/@caption"/><xsl:value-of select="document/fields/np2/@caption"/> :
											</td>
											<td>
												<input type="text" name="np" id="np" value="{document/fields/np}" onkeypress="javascript:Numeric(this)" style="width:62px;" class="td_editable">
													<xsl:if test="$editmode !='edit'">
							                   			<xsl:attribute name="readonly">readonly</xsl:attribute>
							                   			<xsl:attribute name="onfocus">$(this).blur()</xsl:attribute>
							                   			<xsl:attribute name="class">td_noteditable</xsl:attribute>
							                   		</xsl:if>                    			              		
													<xsl:if test="$editmode = 'edit'">
														<xsl:attribute name="onfocus">fieldOnFocus(this)</xsl:attribute>
														<xsl:attribute name="onblur">fieldOnBlur(this)</xsl:attribute>
													</xsl:if>
												</input>	
												/
												<input type="text" name="np2" value="{document/fields/np2}" style="width:61px;" id="np2" onkeypress="javascript:Numeric(this)" class="td_editable">
													<xsl:if test="$editmode !='edit'">
					                           			<xsl:attribute name="readonly">readonly</xsl:attribute>
					                           			<xsl:attribute name="onfocus">$(this).blur()</xsl:attribute>
					                           			<xsl:attribute name="class">td_noteditable</xsl:attribute>
					                           		</xsl:if>                    			              		
													<xsl:if test="$editmode = 'edit'">
														<xsl:attribute name="onfocus">fieldOnFocus(this)</xsl:attribute>
														<xsl:attribute name="onblur">fieldOnBlur(this)</xsl:attribute>
													</xsl:if>
												</input>	
											</td>
										</tr>
									</table>		
								</div>
								<input type="hidden" name="type" value="save"/>
								<input type="hidden" name="id" value="out"/>
								<input type="hidden" name="din" value="{document/fields/din}"/>
								<input type="hidden" name="department" value="{document/fields/department/@attrval}"/>
								<xsl:if test="document/@status !='new'">
									<input type="hidden" name="key" value="{document/@docid}"/>
									<input type="hidden" name="doctype" value="{document/@doctype}"/>
								</xsl:if>
								<xsl:call-template name="ECPsignFields"/>
								<div id="tabs-2">
									<br/>
									<table width="100%" border="0">
									<!-- Поле "Содержание" -->
										<tr>
											<td class="fc"><xsl:value-of select="document/captions/content/@caption"/> :</td>
											<td>
												<xsl:if test="$editmode = 'edit'">
													<script type="text/javascript">  
														$(document).ready(function($) {
												    		 CKEDITOR.config.width = "815px"
												    		 CKEDITOR.config.height = "450px"
												    	});
													</script>
													<textarea id="MyTextarea" name="contentsource">
														<xsl:if test="@useragent = 'ANDROID'">
															<xsl:attribute name="style">width:500px; height:300px</xsl:attribute>
														</xsl:if>
														<xsl:value-of select="document/fields/contentsource"/>
													</textarea>
												</xsl:if>
												<xsl:if test="$editmode != 'edit'">
													<div id="briefcontent">
														<xsl:attribute name="style">width:815px; height:450px; background:#EEEEEE; padding: 3px 5px; overflow-x:auto</xsl:attribute>
														<script>
															$("#briefcontent").html("<xsl:value-of select='document/fields/contentsource'/>")
														</script>
													</div>
													<input type="hidden" name="contentsource" value="{document/fields/contentsource}"/>
												</xsl:if>
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
							</form>
							<div id="tabs-3">
								<form action="Uploader" name="upload" id="upload" method="post" enctype="multipart/form-data">
									<input type="hidden" name="type" value="rtfcontent"/>
									<input type="hidden" name="formsesid" value="{formsesid}"/>
									<div display="block" id="att">
										<br/>	
										<xsl:call-template name="attach"/>
									</div>
								</form>
							</div>
							<div id="tabs-4">
								<xsl:call-template name="docinfo"/>
							</div>
							<xsl:if test="document/fields/coordination">
								<div id="tabs-5">
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
																					<xsl:call-template name="coord_comment_attach"/>
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
				<xsl:call-template name="formoutline"/>
			</body>	
		</html>
	</xsl:template>
</xsl:stylesheet>