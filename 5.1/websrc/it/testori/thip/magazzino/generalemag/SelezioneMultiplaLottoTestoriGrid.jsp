<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"
                      "file:///D:\3rdParty\Panthera4.0.4\websrc\dtd/xhtml1-transitional.dtd">
<%@page import="it.thera.thip.vendite.generaleVE.web.SelezioneMultiplaLottoTestori"%>
<html>
<!-- WIZGEN Therm 2.0.0 as Griglia - multiBrowserGen = true -->
<%=WebGenerator.writeRuntimeInfo()%>
<head>
<%@ page contentType="text/html; charset=Cp1252"%>
<%@ page import= " 
  java.sql.*, 
  java.util.*, 
  java.lang.reflect.*, 
  javax.naming.*, 
  com.thera.thermfw.common.*, 
  com.thera.thermfw.type.*, 
  com.thera.thermfw.web.*, 
  com.thera.thermfw.security.*, 
  com.thera.thermfw.base.*, 
  com.thera.thermfw.ad.*, 
  com.thera.thermfw.persist.*, 
  com.thera.thermfw.gui.cnr.*, 
  com.thera.thermfw.setting.*, 
  com.thera.thermfw.collector.*, 
  com.thera.thermfw.batch.web.*, 
  com.thera.thermfw.batch.*, 
  com.thera.thermfw.pref.* 
"%> 
<%
  ServletEnvironment se = (ServletEnvironment)Factory.createObject("com.thera.thermfw.web.ServletEnvironment"); 
  List errors = new ArrayList(); 
  ErrorMessage dolError = null;//Mod.2777 
  WebJSTypeList jsList = new WebJSTypeList(); 
  String errorMessage; 
  WebFormForGrid gridForm =  
     new com.thera.thermfw.web.WebFormForGrid(request, response, "gridForm", "com.thera.thermfw.web.servlet.GridActionAdapter", true, true, true); 
  gridForm.setServletEnvironment(se); 
  gridForm.setJSTypeList(jsList); 
  gridForm.write(out); 
  try 
  {
     se.initialize(request, response); 
     if(se.begin()) 
     { 
        dolError = (ErrorMessage)request.getAttribute(com.thera.thermfw.web.servlet.ShowGrid.DOL_ERROR);//Mod.2777 
        gridForm.outTraceInfo(getClass().getName()); 
        gridForm.writeHeadElements(out); 
%> 

<title>Lista</title>
<% 
  WebSettingBar settBarTB = new com.thera.thermfw.web.WebSettingBar("settBar", "24", "24", "16", "16", "#f7fbfd","#C8D6E1"); 
  settBarTB.setParent(gridForm); 
   request.setAttribute("settingBar", settBarTB); 
%> 
<jsp:include page="/com/thera/thermfw/common/defGridMenu.jsp" flush="true"> 
<jsp:param name="partRequest" value="settingBar"/> 
</jsp:include> 
<% 
  settBarTB.write(out); 
  settBarTB.writeFunctions(out, false); 
%> 
<% 
  WebToolBar gridToolbarTB = new com.thera.thermfw.web.WebToolBar("gfToolbardiv", "24", "24", "16", "16", "#f7fbfd","#C8D6E1"); 
  gridToolbarTB.setParent(gridForm); 
   request.setAttribute("toolBar", gridToolbarTB); 
%> 
<jsp:include page="/com/thera/thermfw/common/defGridMenu.jsp" flush="true"> 
<jsp:param name="partRequest" value="toolBar"/> 
</jsp:include> 
<% 
   gridToolbarTB.write(out); 
%> 
<% 
  WebMenuBar menuBar = new com.thera.thermfw.web.WebMenuBar("HM_Array1", "150", "#000000","#000000","#A5B6CE","#E4EAEF","#FFFFFF","#000000"); 
  menuBar.setParent(gridForm); 
   request.setAttribute("menuBar", menuBar); 
%> 
<jsp:include page="/com/thera/thermfw/common/defGridMenu.jsp" flush="true"> 
<jsp:param name="partRequest" value="menuBar"/> 
</jsp:include> 
<% 
  menuBar.write(out); 
  menuBar.writeChildren(out); 
%> 
</head>
<body onload="<%=gridForm.getBodyOnLoad()%>" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0" onunload="<%=gridForm.getBodyOnUnload()%>" onbeforeunload="<%=gridForm.getBodyOnBeforeUnload()%>" style="overflow: hidden;"><%
   gridForm.writeBodyStartElements(out); 
%> 

<table width="100%" height="0" cellspacing="0" cellpadding="0">
<tr>
<td style="height:0">
<% String hdr = gridForm.getCompleteHeader();
 if (hdr != null) { 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= hdr %>" flush="true"/> 
<% } %> 
</td>
</tr>

<tr>
<td>
<form name="defGrid" action="<%=gridForm.getServlet()%>" style="height:100%" method="post"><%
  gridForm.writeFormStartElements(out); 
%>
<input type="hidden" name="thTarget" value><input type="hidden" name="thAction" value>
<input type="hidden" name="<%=SelezioneMultiplaLottoTestori.SELEZIONE_MULTIPLA_LOTTI%>" value=<%=request.getParameter(SelezioneMultiplaLottoTestori.SELEZIONE_MULTIPLA_LOTTI) %>>
<input type="hidden" name="rigaKey" value=<%=request.getParameter("ChiaveLottoProdotto2") %>>
<table id="emptyborder" border="0" style="border:0px;margin:0px" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td>
		<span id="defGrid" class="gridform"><% menuBar.writeElements(out); %> 
<% gridToolbarTB.writeChildren(out); %> 
<% settBarTB.writeChildren(out); %> 
<% 
   WebGridForm grid = new com.thera.thermfw.web.WebGridForm("", true,true,true, 0, null, null); 
   grid.setParent(gridForm); 
   grid.completeData(); 
   grid.write(out); 
%> 
</span>
		
<%grid.writeImportLibraries(out);%>
<%grid.setHeaderProperties(new String[]{"", "", "", " class=\"header\"", "", "", "", " class=\"header\""});%>
<%grid.setBodyProperties(new String[]{"", "", " class=\"cell\""});%>
<div name="divGridElement" id="divGridElement" class="grid-container">
<div name="grid-header" id="grid-header" class="grid-header">
<div name="grid-header-frozen" id="grid-header-frozen" class="grid-header-frozen">
<%grid.writeFrozenHeaderPart(out);%>
</div>
<div name="grid-header-scrollable" id="grid-header-scrollable" class="grid-header-scrollable">
<%grid.writeScrollableHeaderPart(out);%>
</div>
</div>
<div name="grid-body" id="grid-body" class="grid-body">
<div name="grid-body-frozen" id="grid-body-frozen" class="grid-body-frozen" onscroll="fireFrozenBodyScrolled(event, this)">
<%grid.writeFrozenBodyPart(out);%>
</div>
<div name="grid-body-scrollable" id="grid-body-scrollable" class="grid-body-scrollable" onscroll="fireScrollableBodyScrolled(event, this)">
<%grid.writeScrollableBodyPart(out);%>
</div>
</div>
</div>
		<div align="left">
		<table class="footer_table" cellspacing="0" id="footer" width="100%">
			<tr>
 			    <td align="center" id="first" width="26" class="footer"><%grid.writeFirstButton(out);%></td>
				<td align="center" id="prev" width="26" class="footer"><%grid.writePrevButton(out);%></td>
				<td align="center" id="status" class="footer"><%grid.writePages(out);%></td>
				<td align="center" id="page" width="200" class="footer"><%grid.writeNumberPage(out);%></td>
				<td align="center" id="next" width="26" class="footer"><%grid.writeNextButton(out);%></td>
 			    <td align="center" id="last" width="26" class="footer"><%grid.writeLastButton(out);%></td>				
			</tr>
		</table>
		</div>
	</td>
</tr>
</table>
<%
  gridForm.writeFormEndElements(out); 
%>
<% 
  WebErrorListForGrid errorListForGrid = new WebErrorListForGrid(); 
  errorListForGrid.setParent(gridForm); 
  errorListForGrid.setRowNumber(1); 
  errorListForGrid.write(out); 
%>
</form></td>
</tr>

<tr>
<td style="height:0">
<% String ftr = gridForm.getCompleteFooter();
 if (ftr != null) { 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= ftr %>" flush="true"/> 
<% } %> 
</td>
</tr>
</table>


<%
        gridForm.writeBodyEndElements(out); 
     } 
     else 
        errors.add(new ErrorMessage("BATCH015")); 
  } 
  catch(NamingException e) { 
     errorMessage = e.getMessage(); 
     errors.add(new ErrorMessage("CBS000025", errorMessage));  } 
  catch(SQLException e) {
     errorMessage = e.getMessage(); 
     errors.add(new ErrorMessage("BAS0000071", errorMessage));  } 
  finally 
  {
     try 
     { 
        se.end(); 
     }
     catch(IllegalArgumentException e) { 
        e.printStackTrace(); 
     } 
     catch(SQLException e) { 
        e.printStackTrace(); 
     } 
  } 
  if(!errors.isEmpty())
  { 
     request.setAttribute("ErrorMessages", errors); 
     String errorPage = gridForm.getErrorPage(); 
%> 
     <jsp:include page="<%=errorPage%>" flush="true"/> 
<% 
   } 
  //Mod.2777 
  else if(dolError != null) 
  { 
    String longTextError = dolError.getLongText(); 
%> 
<script language="JavaScript1.2">alert("<%=longTextError%>"); 
</script> 
<% 
   } //fine mod.2777 
%> 
</body>
</html>