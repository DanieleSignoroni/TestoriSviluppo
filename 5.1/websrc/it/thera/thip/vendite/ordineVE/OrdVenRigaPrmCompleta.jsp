<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"
                      "file:///C:/pthsvil/deps/T_Thip_40/Thip/4.0/websrcsvil/dtd/xhtml1-transitional.dtd">
<!-- 34124 -->
<html>
<!-- WIZGEN Therm 2.0.0 as Form - multiBrowserGen = true -->
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
  BODataCollector OrdineVenditaRigaPrmBODC = null; 
  List errors = new ArrayList(); 
  WebJSTypeList jsList = new WebJSTypeList(); 
  WebForm OrdineVenditaRigaPrmForm =  
     new com.thera.thermfw.web.WebForm(request, response, "OrdineVenditaRigaPrmForm", "OrdineVenditaRigaPrm", null, "it.thera.thip.vendite.ordineVE.web.OrdineVenditaRigaPrmFormActionAdapter", false, false, true, true, true, true, "it.thera.thip.vendite.ordineVE.web.OrdineVenditaRigaPrmDataCollector", 1, true, "it/thera/thip/vendite/ordineVE/OrdineVenditaRiga.js"); 
  OrdineVenditaRigaPrmForm.setServletEnvironment(se); 
  OrdineVenditaRigaPrmForm.setJSTypeList(jsList); 
  OrdineVenditaRigaPrmForm.setHeader("it.thera.thip.cs.Header.jsp"); 
  OrdineVenditaRigaPrmForm.setFooter("it.thera.thip.cs.Footer.jsp"); 
  OrdineVenditaRigaPrmForm.setWebFormModifierClass("it.thera.thip.vendite.ordineVE.web.OrdineVenditaRigaPrmCompletaFormModifier"); 
  OrdineVenditaRigaPrmForm.setDeniedAttributeModeStr("hideNone"); 
  int mode = OrdineVenditaRigaPrmForm.getMode(); 
  String key = OrdineVenditaRigaPrmForm.getKey(); 
  String errorMessage; 
  boolean requestIsValid = false; 
  boolean leftIsKey = false; 
  boolean conflitPresent = false; 
  String leftClass = ""; 
  try 
  {
     se.initialize(request, response); 
     if(se.begin()) 
     { 
        OrdineVenditaRigaPrmForm.outTraceInfo(getClass().getName()); 
        String collectorName = OrdineVenditaRigaPrmForm.findBODataCollectorName(); 
                OrdineVenditaRigaPrmBODC = (BODataCollector)Factory.createObject(collectorName); 
        if (OrdineVenditaRigaPrmBODC instanceof WebDataCollector) 
            ((WebDataCollector)OrdineVenditaRigaPrmBODC).setServletEnvironment(se); 
        OrdineVenditaRigaPrmBODC.initialize("OrdineVenditaRigaPrm", true, 1); 
        OrdineVenditaRigaPrmForm.setBODataCollector(OrdineVenditaRigaPrmBODC); 
        int rcBODC = OrdineVenditaRigaPrmForm.initSecurityServices(); 
        mode = OrdineVenditaRigaPrmForm.getMode(); 
        if (rcBODC == BODataCollector.OK) 
        { 
           requestIsValid = true; 
           OrdineVenditaRigaPrmForm.write(out); 
           if(mode != WebForm.NEW) 
              rcBODC = OrdineVenditaRigaPrmBODC.retrieve(key); 
           if(rcBODC == BODataCollector.OK) 
           { 
              OrdineVenditaRigaPrmForm.writeHeadElements(out); 
           // fine blocco XXX  
           // a completamento blocco di codice YYY a fine body con catch e gestione errori 
%> 

	<title>Ordine - Riga</title>
<% 
  WebMenuBar menuBar = new com.thera.thermfw.web.WebMenuBar("HM_Array1", "150", "#000000","#000000","#A5B6CE","#E4EAEF","#FFFFFF","#000000"); 
  menuBar.setParent(OrdineVenditaRigaPrmForm); 
   request.setAttribute("menuBar", menuBar); 
%> 
<jsp:include page="/it/thera/thip/cs/defObjMenu.jsp" flush="true"> 
<jsp:param name="partRequest" value="menuBar"/> 
</jsp:include> 
<% 
  menuBar.write(out); 
  menuBar.writeChildren(out); 
%> 
<% 
  WebToolBar myToolBarTB = new com.thera.thermfw.web.WebToolBar("myToolBar", "24", "24", "16", "16", "#f7fbfd","#C8D6E1"); 
  myToolBarTB.setParent(OrdineVenditaRigaPrmForm); 
   request.setAttribute("toolBar", myToolBarTB); 
%> 
<jsp:include page="/it/thera/thip/cs/defObjMenu.jsp" flush="true"> 
<jsp:param name="partRequest" value="toolBar"/> 
</jsp:include> 
<% 
   myToolBarTB.write(out); 
%> 
</head>


<body bottommargin="0" leftmargin="0" onbeforeunload="<%=OrdineVenditaRigaPrmForm.getBodyOnBeforeUnload()%>" onload="<%=OrdineVenditaRigaPrmForm.getBodyOnLoad()%>" onunload="<%=OrdineVenditaRigaPrmForm.getBodyOnUnload()%>" rightmargin="0" topmargin="0"><%
   OrdineVenditaRigaPrmForm.writeBodyStartElements(out); 
%> 



	<table width="100%" height="100%" cellspacing="0" cellpadding="0">
<tr>
<td style="height:0" valign="top">
<% String hdr = OrdineVenditaRigaPrmForm.getCompleteHeader();
 if (hdr != null) { 
   request.setAttribute("dataCollector", OrdineVenditaRigaPrmBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= hdr %>" flush="true"/> 
<% } %> 
</td>
</tr>

<tr>
<td valign="top" height="100%">
<form action="<%=OrdineVenditaRigaPrmForm.getServlet()%>" method="post" name="OrdineVenditaRigaForm" style="height:100%"><%
  OrdineVenditaRigaPrmForm.writeFormStartElements(out); 
%>

		<table cellpadding="1" cellspacing="1" height="100%" width="100%">

			<!-- **************************************************************************************************** -->
			<!-- Menubar -->
			<tr>
				<td style="height:0">
					<% menuBar.writeElements(out); %> 

				</td>
			</tr>

			<!-- **************************************************************************************************** -->
			<!-- Toolbar -->
			<tr>
				<td style="height:0">
					<% myToolBarTB.writeChildren(out); %> 

				</td>
			</tr>

			<!-- **************************************************************************************************** -->
			<!-- Pannello tabulare principale -->
			<tr>
				<td height="100%">
					<!--<span class="tabbed" id="MainTabbed">-->
<table width="100%" height="100%" cellpadding="0" cellspacing="0" style="padding-right:1px">
   <tr valign="top">
     <td><% 
  WebTabbed MainTabbed = new com.thera.thermfw.web.WebTabbed("MainTabbed", "100%", "100%"); 
  MainTabbed.setParent(OrdineVenditaRigaPrmForm); 
 MainTabbed.addTab("GeneraleTab", "it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "GeneraleTab", "OrdineVenditaRigaPrm", null, null, null, null); 
 MainTabbed.addTab("PrezziScontiTab", "it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "PrezziScontiTab", "OrdineVenditaRigaPrm", null, null, null, "verificaCondizioniRecuperoDatiVendita()"); 
 MainTabbed.addTab("AgentiTab", "it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "AgentiTab", "OrdineVenditaRigaPrm", null, null, null, "verificaCondizioniRecuperoDatiVendita()"); 
 MainTabbed.addTab("CondEvasioneTab", "it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "CondEvasioneTab", "OrdineVenditaRigaPrm", null, null, null, null); 
 MainTabbed.addTab("ContabilTab", "it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "ContabilTab", "OrdineVenditaRigaPrm", null, null, null, null); 
 MainTabbed.addTab("CommentiMultimediaTab", "it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "CommentiMultimediaTab", "OrdineVenditaRigaPrm", null, null, null, null); 
 MainTabbed.addTab("LottiTab", "it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "LottiTab", "OrdineVenditaRigaPrm", null, null, null, null); 
 MainTabbed.addTab("SpeditoTab", "it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "SpeditoTab", "OrdineVenditaRigaPrm", null, null, null, null); 
 MainTabbed.addTab("RiepilogoTab", "it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "RiepilogoTab", "OrdineVenditaRigaPrm", null, null, null, null); 
  MainTabbed.write(out); 
%>

     </td>
   </tr>
   <tr>
     <td height="100%"><div class="tabbed_pagine" id="tabbedPagine" style="position: relative; width: 100%; height: 100%;">
						<!-- **************************************************************************************** -->
						<!-- Cartella Generale -->
						<div class="tabbed_page" id="<%=MainTabbed.getTabPageId("GeneraleTab")%>" style="width:100%;height:100%;overflow:auto;"><% MainTabbed.startTab("GeneraleTab"); %>
							<!-- INIZIO TABELLA PRINCIPALE -->
							<table>
								<!-- INIZIO RIGA 1 TABELLA PRINCIPALE -->
								<tr>
									<td>
										<!-- Fix 39280 - inizio modifiche grafiche -->
										<!-- <table cellspacing="1" cellpadding="1"> -->
										<table cellpadding="2" cellspacing="2" style="padding: 3px; width: 100%;">
<!--            <tr> -->
												
<!--             Combo Stato Avanzamento -->
<!--             <td nowrap="true" style="width: 150px"> -->
<!--              <label for="StatoAvanzamentoCombo"> -->
<!--               Stato Avanzamento -->
<!--              </label> -->
<!--             </td> -->
<!--             <td colspan="9"> -->
<!--              <select name="StatoAvanzamentoCombo" id="StatoAvanzamentoCombo"> -->
<!--               <option></option> -->
<!--              </select> -->
<!--             </td> -->
											
<!--             <td></td> -->
<!--             <td style="display: none;"></td> -->
<!--             <td style="display: none;"></td> -->
<!--             <td style="display: none;"></td> -->
<!--            </tr> -->
											<tr>
											<!-- Campo Sequenza Riga -->
												<td colspan="2" nowrap style="width: 100px">
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "SequenzaRiga", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="SequenzaRigaField"><%label.write(out);%></label><%}%>
												</td>
												<!-- Fix 3045 inizio -->
												<!-- <td></td> -->
												<!-- Fix 3045 fine -->
												<td colspan="2" style="width: 100px">
													<% 
  WebTextInput OrdineVenditaRigaPrmSequenzaRiga =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "SequenzaRiga"); 
  OrdineVenditaRigaPrmSequenzaRiga.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmSequenzaRiga.getClassType()%>" id="<%=OrdineVenditaRigaPrmSequenzaRiga.getId()%>" maxlength="<%=OrdineVenditaRigaPrmSequenzaRiga.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmSequenzaRiga.getName()%>" size="5" type="text"><% 
  OrdineVenditaRigaPrmSequenzaRiga.write(out); 
%>

												</td>
<!--             <td> -->
<!--             </td> -->
												<td nowrap style="width: 150px">
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "StatoAvanzamento", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="StatoAvanzamentoCombo"><%label.write(out);%></label><%}%>
													<% 
  WebComboBox OrdineVenditaRigaPrmStatoAvanzamento =  
     new com.thera.thermfw.web.WebComboBox("OrdineVenditaRigaPrm", "StatoAvanzamento", null); 
  OrdineVenditaRigaPrmStatoAvanzamento.setParent(OrdineVenditaRigaPrmForm); 
%>
<select id="<%=OrdineVenditaRigaPrmStatoAvanzamento.getId()%>" name="<%=OrdineVenditaRigaPrmStatoAvanzamento.getName()%>"><% 
  OrdineVenditaRigaPrmStatoAvanzamento.write(out); 
%> 

														
													</select>
												</td>
												<!-- Riallineamento Intellimag al 5.0 -->
												<!-- 41316 -->
												<td nowrap><div id="StatoIntellimagTD"><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "StatoIntellimag", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="StatoIntellimag"><%label.write(out);%></label><%}%><% 
  WebComboBox OrdineVenditaRigaPrmStatoIntellimag =  
     new com.thera.thermfw.web.WebComboBox("OrdineVenditaRigaPrm", "StatoIntellimag", null); 
  OrdineVenditaRigaPrmStatoIntellimag.setParent(OrdineVenditaRigaPrmForm); 
%>
<select id="<%=OrdineVenditaRigaPrmStatoIntellimag.getId()%>" name="<%=OrdineVenditaRigaPrmStatoIntellimag.getName()%>"><% 
  OrdineVenditaRigaPrmStatoIntellimag.write(out); 
%> 
</select></div></td><!-- 36324V2 -->
												
											</tr>
											<td id="IdNumeratoreArtIntestTD" style="display: none">
												<input id="IdNumeratoreArtIntestField" name="IdNumeratoreArtIntestField" type="hidden">
											</td>
											<tr>
												<!-- Proxy Causale -->
												<td colspan="2" nowrap>
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdCausaleRigaVendita", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="CausaleProxy"><%label.write(out);%></label><%}%>
												</td>
												<!-- Fix 3045 inizio -->
												<!-- <td></td> -->
												<!-- Fix 3045 fine -->
												<td colspan="2" nowrap>
													<% 
  WebMultiSearchForm OrdineVenditaRigaPrmRCausaleRigaVendita =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "RCausaleRigaVendita", false, false, true, 1, null, null); 
  OrdineVenditaRigaPrmRCausaleRigaVendita.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRCausaleRigaVendita.write(out); 
%>
<!--<span class="multisearchform" id="CausaleProxy"></span>-->
												</td>

												<!-- Check Non Fatturare -->
<!--             <td> -->
<!--             </td> -->
												<td>
													<% 
  WebCheckBox OrdineVenditaRigaPrmNonFatturare =  
     new com.thera.thermfw.web.WebCheckBox("OrdineVenditaRigaPrm", "NonFatturare"); 
  OrdineVenditaRigaPrmNonFatturare.setParent(OrdineVenditaRigaPrmForm); 
%>
<input id="<%=OrdineVenditaRigaPrmNonFatturare.getId()%>" name="<%=OrdineVenditaRigaPrmNonFatturare.getName()%>" type="checkbox" value="Y"><%
  OrdineVenditaRigaPrmNonFatturare.write(out); 
%>

												</td>	
												<td nowrap>
													<div id="AbilitaTxIntellimagTD">
													<% 
  WebCheckBox OrdineVenditaRigaPrmAbilitaTxIntellimag =  
     new com.thera.thermfw.web.WebCheckBox("OrdineVenditaRigaPrm", "AbilitaTxIntellimag"); 
  OrdineVenditaRigaPrmAbilitaTxIntellimag.setParent(OrdineVenditaRigaPrmForm); 
%>
<input id="<%=OrdineVenditaRigaPrmAbilitaTxIntellimag.getId()%>" name="<%=OrdineVenditaRigaPrmAbilitaTxIntellimag.getName()%>" type="checkbox" value="Y"><%
  OrdineVenditaRigaPrmAbilitaTxIntellimag.write(out); 
%>

													</div>
												</td>
											</tr>
											<tr>
												<!-- Proxy Magazzino -->
												<td colspan="2" nowrap>
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdMagazzino", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="MagazzinoProxy"><%label.write(out);%></label><%}%>
												</td>
												<!-- Fix 3045 inizio -->
												<!-- <td></td> -->
												<!-- Fix 3045 fine -->
												<td colspan="3" nowrap>
													<% 
  WebMultiSearchForm OrdineVenditaRigaPrmRMagazzino =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "RMagazzino", false, false, true, 1, null, null); 
  OrdineVenditaRigaPrmRMagazzino.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRMagazzino.write(out); 
%>
<!--<span class="multisearchform" id="MagazzinoProxy"></span>-->
												</td>
												<td style="display: none;">&nbsp;</td>
												<td style="display: none;">&nbsp;</td>
											</tr>
											<!-- Fix 1918 inizio -->
											<tr id="SezCatalogoEsterno" name="SezCatalogoEsterno" style="display:none">
												<!-- Proxy Catalogo Esterno -->
												<td colspan="2" nowrap>
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdArticoloFornitoreCatal", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="CatalogoEsternoProxy"><%label.write(out);%></label><%}%>
												</td>
												<!-- Fix 3045 inizio -->
												<!-- <td></td> -->
												<!-- Fix 3045 fine -->
												<td colspan="4" nowrap>
													<% 
  WebMultiSearchForm OrdineVenditaRigaPrmRCatalogoEsterno =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "RCatalogoEsterno", false, false, true, 2, "15", null); 
  OrdineVenditaRigaPrmRCatalogoEsterno.setExtraRelatedClassAD("IdCatalogo"); 
  OrdineVenditaRigaPrmRCatalogoEsterno.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRCatalogoEsterno.setOnKeyFocus("memorizzaCodiciCatalogoEsterno()"); 
  OrdineVenditaRigaPrmRCatalogoEsterno.setOnKeyBlur("confrontaCodiciCatalogoEsterno()"); 
  OrdineVenditaRigaPrmRCatalogoEsterno.setOnSearchBack("recuperaDatiCatalogoEsterno()"); 
  OrdineVenditaRigaPrmRCatalogoEsterno.write(out); 
%>
<!--<span class="multisearchform" id="CatalogoEsternoProxy"></span>-->
												</td>
											</tr>
											<!-- Fix 1918 fine -->
											
																						
    									  <!--Fix 35595 inizio-->
												<tr id="TRRichiestaServizio">
													<td colspan="2" nowrap><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdNumeroRichServ", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="RichServ"><%label.write(out);%></label><%}%></td>
													<!-- <td></td> -->
													<td nowrap style="width: 315px;"><% 
  WebMultiSearchForm OrdineVenditaRigaPrmRichServ =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "RichServ", false, false, true, 2, null, "10"); 
  OrdineVenditaRigaPrmRichServ.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRichServ.setOnKeyFocus("memorizzaRichiestaServizio()"); 
  OrdineVenditaRigaPrmRichServ.setOnKeyBlur("variaRichiestaServizio()"); 
  OrdineVenditaRigaPrmRichServ.setOnKeyChange("confrontaRichiestaServizio()"); 
  OrdineVenditaRigaPrmRichServ.write(out); 
%>
<!--<span class="multisearchform" id="RichServ"></span>--></td>
													<td colspan="3" style="width: 450px;"><% 
  WebTextInput OrdineVenditaRigaPrmRichServRichiedente =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "RichServRichiedente"); 
  OrdineVenditaRigaPrmRichServRichiedente.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmRichServRichiedente.getClassType()%>" id="<%=OrdineVenditaRigaPrmRichServRichiedente.getId()%>" maxlength="<%=OrdineVenditaRigaPrmRichServRichiedente.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmRichServRichiedente.getName()%>" size="<%=OrdineVenditaRigaPrmRichServRichiedente.getSize()%>" style="width: 450px;"><% 
  OrdineVenditaRigaPrmRichServRichiedente.write(out); 
%>
</td>
												</tr>
												<tr id="TRBeneServizio">
													<td colspan="2" nowrap><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdBene", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="Bene"><%label.write(out);%></label><%}%></td>
													<!-- <td></td> -->
													<td colspan="3" nowrap><% 
  WebMultiSearchForm OrdineVenditaRigaPrmBene =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "Bene", false, false, true, 1, "15", null); 
  OrdineVenditaRigaPrmBene.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmBene.write(out); 
%>
<!--<span class="multisearchform" id="Bene"></span>--></td>
												</tr>
											<!--Fix 35595 fine -->		
											
											<tr>
												<!-- Fix 3045 inizio -->
												<!-- Proxy Articolo Intestatario Label -->
												<td id="SezArtIntLabel" nowrap style="display:none">
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdArticoloIntestatario", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="ArticoloIntestatarioProxy"><%label.write(out);%></label><%}%>
												</td>

<!-- Fix 35595           Fix 33428 inizio             <tr id="TRRichiestaServizio">              <td nowrap="true"><label for="RichServ"></label></td>              <td></td>              <td nowrap="true"><span class="multisearchform" id="RichServ"></span></td>              <td colspan="6"><input id="RichServRichiedente" name="RichServRichiedente"/></td>             </tr>             <tr id="TRBeneServizio">              <td nowrap="true"><label for="Bene"></label></td>              <td></td>              <td nowrap="true"><span class="multisearchform" id="Bene"></span></td>             </tr>              Fix 33428 fine  Fix 35595 fine -->
												<!-- Proxy Articolo Label -->
												<td id="SezArticoloLabel" nowrap>
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdArticolo", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="ArticoloProxy"><%label.write(out);%></label><%}%>
												</td>
												<!-- Check Visualizza Articolo Intestatario -->
												<td id="SezCheckVisulArtInt" style="display:none; text-align: end; vertical-align: middle;">
													<% 
  WebCheckBox OrdineVenditaRigaPrmVisualizzaArtInt =  
     new com.thera.thermfw.web.WebCheckBox("OrdineVenditaRigaPrm", "VisualizzaArtInt"); 
  OrdineVenditaRigaPrmVisualizzaArtInt.setParent(OrdineVenditaRigaPrmForm); 
%>
<input id="<%=OrdineVenditaRigaPrmVisualizzaArtInt.getId()%>" name="<%=OrdineVenditaRigaPrmVisualizzaArtInt.getName()%>" type="checkbox" value="Y"><%
  OrdineVenditaRigaPrmVisualizzaArtInt.write(out); 
%>

												</td>
												<td style="text-align: end; vertical-align: middle;">
													<input id="VisualizzaArtInt2" name="VisualizzaArtInt2" onclick="visualizzaArticoloIntestatarioOnClick()" type="checkbox">
												</td>
												<!-- Proxy Articolo Intestatario Multisearch -->
												<td colspan="3" id="SezArtIntProxy" nowrap style="display:none">
													<% 
  WebMultiSearchForm OrdineVenditaRigaPrmRArticoloIntestatario =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "RArticoloIntestatario", false, false, true, 1, "15", null); 
  OrdineVenditaRigaPrmRArticoloIntestatario.setExtraRelatedClassAD("IdArticolo,IdConfigurazione,IdArticoloCliente"); 
  OrdineVenditaRigaPrmRArticoloIntestatario.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRArticoloIntestatario.setOnKeyFocus("memorizzaCodiceArticoloIntestatario()"); 
  OrdineVenditaRigaPrmRArticoloIntestatario.setOnKeyBlur("confrontaCodiceArticoloIntestatario()"); 
  OrdineVenditaRigaPrmRArticoloIntestatario.setOnKeyChange("ripulisciNumeratoreArticoloIntestatario()"); 
  OrdineVenditaRigaPrmRArticoloIntestatario.setOnSearchBack("recuperaDatiArticoloIntestatario()"); 
  OrdineVenditaRigaPrmRArticoloIntestatario.write(out); 
%>
<!--<span class="multisearchform" id="ArticoloIntestatarioProxy"></span>-->
												</td>
												<!-- Proxy Articolo Multisearch -->
												<td colspan="3" id="SezArticoloProxy" nowrap>
													<% 
  WebMultiSearchForm OrdineVenditaRigaPrmRArticolo =  
     new it.thera.thip.base.articolo.web.ArticoloMultiSearchForm("OrdineVenditaRigaPrm", "RArticolo", false, false, true, 1, "20", null); 
  OrdineVenditaRigaPrmRArticolo.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRArticolo.setOnKeyFocus("memorizzaCodiceArticolo()"); 
  OrdineVenditaRigaPrmRArticolo.setOnKeyBlur("confrontaCodiceArticolo()"); 
  OrdineVenditaRigaPrmRArticolo.setOnKeyChange("variazioneArticolo()"); 
  OrdineVenditaRigaPrmRArticolo.setOnSearchClick("ricercaArticolo()"); 
  OrdineVenditaRigaPrmRArticolo.setOnSearchBack("recuperaDatiArticolo()"); 
  OrdineVenditaRigaPrmRArticolo.setFixedRestrictConditions("DatiComuniEstesi.Stato,V"); 
  ((it.thera.thip.base.articolo.web.ArticoloMultiSearchForm)OrdineVenditaRigaPrmRArticolo).setArticoliAlternButton(true); 
  OrdineVenditaRigaPrmRArticolo.write(out); 
%>
<!--<span class="articolomultisearchform" id="ArticoloProxy"></span>-->
												</td>

												<!-- Fix 3045 fine -->
												<!-- Campo Descrizione Articolo -->
												<td colspan="2" id="SezArticoloDescr" nowrap>
													<% 
  WebTextInput OrdineVenditaRigaPrmDescrizioneArticolo =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "DescrizioneArticolo"); 
  OrdineVenditaRigaPrmDescrizioneArticolo.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmDescrizioneArticolo.getClassType()%>" id="<%=OrdineVenditaRigaPrmDescrizioneArticolo.getId()%>" maxlength="<%=OrdineVenditaRigaPrmDescrizioneArticolo.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmDescrizioneArticolo.getName()%>" size="30" type="text"><% 
  OrdineVenditaRigaPrmDescrizioneArticolo.write(out); 
%>

												</td>
												<!-- <td>&nbsp;</td> -->
											</tr>
											<tr>
												<!-- Proxy Versione Richiesta -->
												<td colspan="2" nowrap style="height: 22px;">
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdVersioneRichiesta", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="VersioneRichiestaProxy"><%label.write(out);%></label><%}%>
												</td>
												<!-- Fix 3045 inizio -->
												<!-- <td></td> -->
												<!-- Fix 3045 fine -->
												<td colspan="3" nowrap>
													<% 
  WebMultiSearchForm OrdineVenditaRigaPrmRVersioneRichiesta =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "RVersioneRichiesta", false, false, true, 1, "3", null); 
  OrdineVenditaRigaPrmRVersioneRichiesta.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRVersioneRichiesta.setOnKeyFocus("memorizzaCodiceVersione()"); 
  OrdineVenditaRigaPrmRVersioneRichiesta.setOnKeyBlur("confrontaCodiceVersione()"); 
  OrdineVenditaRigaPrmRVersioneRichiesta.setOnSearchBack("recuperaCostoUnitario()"); 
  OrdineVenditaRigaPrmRVersioneRichiesta.write(out); 
%>
<!--<span class="multisearchform" id="VersioneRichiestaProxy"></span>-->
												</td>
												<!-- Fix14727 Inizio RA -->
												<td colspan="2" id="SezDescrExtArticolo" rowspan="3">
													<% 
  WebTextInput OrdineVenditaRigaPrmDescrizioneExtArticolo =  
     new com.thera.thermfw.web.WebTextArea("OrdineVenditaRigaPrm", "DescrizioneExtArticolo"); 
  OrdineVenditaRigaPrmDescrizioneExtArticolo.setParent(OrdineVenditaRigaPrmForm); 
%>
<textarea class="<%=OrdineVenditaRigaPrmDescrizioneExtArticolo.getClassType()%>" cols="41" id="<%=OrdineVenditaRigaPrmDescrizioneExtArticolo.getId()%>" maxlength="<%=OrdineVenditaRigaPrmDescrizioneExtArticolo.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmDescrizioneExtArticolo.getName()%>" rows="3" size="<%=OrdineVenditaRigaPrmDescrizioneExtArticolo.getSize()%>"></textarea><% 
  OrdineVenditaRigaPrmDescrizioneExtArticolo.write(out); 
%>

												</td>
												<!-- Fix14727 Fine RA -->
												<td>&nbsp;</td>
												<td>&nbsp;</td>
											</tr>
											<tr>
												<!-- Proxy Configurazione -->
												<td colspan="2" nowrap style="height: 22px;">
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdEsternoConfig", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="ConfigurazioneProxy"><%label.write(out);%></label><%}%>
												</td>
												<!-- Fix 3045 inizio -->
												<!-- <td></td> -->
												<!-- Fix 3045 fine -->
												<td colspan="3" nowrap style="width: 560px">
													<div id="confdiv">
														<% 
  WebMultiSearchForm OrdineVenditaRigaPrmRConfigurazione =  
     new it.thera.thip.datiTecnici.configuratore.web.ConfigurazioneMultiSearchForm("OrdineVenditaRigaPrm", "RConfigurazione", false, false, true, 1, "20", null); 
  OrdineVenditaRigaPrmRConfigurazione.setExtraRelatedClassAD("IdAzienda,IdConfigurazione"); 
  OrdineVenditaRigaPrmRConfigurazione.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRConfigurazione.setOnKeyFocus("memorizzaCodiceConfigurazione()"); 
  OrdineVenditaRigaPrmRConfigurazione.setOnKeyBlur("confrontaCodiceConfigurazione()"); 
  OrdineVenditaRigaPrmRConfigurazione.setOnKeyChange("abilitaValorizzaPrezzo()"); 
  OrdineVenditaRigaPrmRConfigurazione.setOnSearchBack("recuperaCostoUnitario()"); 
  OrdineVenditaRigaPrmRConfigurazione.setAdditionalRestrictConditions("IdArticolo,IdArticolo"); 
  OrdineVenditaRigaPrmRConfigurazione.write(out); 
%>
<!--<span class="configurazionemultisearchform" id="ConfigurazioneProxy"></span>-->
													</div>
												</td>
												<!-- Fix 39280 - fine -->
												<td align="left" id="valorizzaPrezzoTd" nowrap>
													
<% 
  WebButton valorizzaPrezzoButWebButton = new WebButton(); 
  valorizzaPrezzoButWebButton.setImage("it/thera/thip/datiTecnici/configuratore/images/ValorizzaPrezzo.gif"); 
  valorizzaPrezzoButWebButton.setResourceFile(null); 
  valorizzaPrezzoButWebButton.setResourceId(null); 
  valorizzaPrezzoButWebButton.setResourceTooltipId(null); 
%>
<button class=" <%=valorizzaPrezzoButWebButton.getButtonCSSClass()%>" name="valorizzaPrezzoBut" onclick="valorizzaPrezzo('OrdineVenditaRigaPrm')" title="<%=valorizzaPrezzoButWebButton.getTitle()%>" type="button"><%valorizzaPrezzoButWebButton.getBtnContent(out);%></button> <!--12091-->
												</td>
											</tr>
											<tr><td colspan="5"></td></tr>
										</table>
									</td>
								</tr>
								<!-- FINE RIGA 1 TABELLA PRINCIPALE -->
								<!-- INIZIO RIGA 2 TABELLA PRINCIPALE -->
								<tr>
									<td>
										<table cellpadding="1" cellspacing="1">
											<!-- Fix 39280 - inizio modifiche grafiche -->
											<td align="left" valign="top" width="450px">
												<fieldset>
													<legend><label class="thLabel" id="LabelGroupBoxQuantita">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "QuantitaGroupBox", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></legend>
													<table cellpadding="1" cellspacing="1" style="margin-left: 2px;">
														<tr style="height: 22px;">
															<!-- Campo Qta Vendita -->
															<td nowrap style="width: 102px;">
																<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "QtaInUMVen", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="QtaVenditaField"><%label.write(out);%></label><%}%>
															</td>
															<td>
																<% 
  WebTextInput OrdineVenditaRigaPrmQtaInUMVen =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "QtaInUMVen"); 
  OrdineVenditaRigaPrmQtaInUMVen.setOnChange("variazioneQuantVenditaRiga(false)"); 
  OrdineVenditaRigaPrmQtaInUMVen.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmQtaInUMVen.getClassType()%>" id="<%=OrdineVenditaRigaPrmQtaInUMVen.getId()%>" maxlength="<%=OrdineVenditaRigaPrmQtaInUMVen.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmQtaInUMVen.getName()%>" size="<%=OrdineVenditaRigaPrmQtaInUMVen.getSize()%>" type="text"><% 
  OrdineVenditaRigaPrmQtaInUMVen.write(out); 
%>

															</td>
															<!-- Combo UM Vendita -->
															<td>
																<% 
  WebSearchComboBox OrdineVenditaRigaPrmRUMRif =  
     new com.thera.thermfw.web.WebSearchComboBox("OrdineVenditaRigaPrm", "RUMRif", null, 2, "20", false, "getListaUMRiferimento"); 
  OrdineVenditaRigaPrmRUMRif.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRUMRif.setOnChange("variazioneUnitaMisura()"); 
  OrdineVenditaRigaPrmRUMRif.write(out); 
%>
<!--<span class="searchcombobox" id="UMVenCombo" name="UMVenCombo"></span>-->
															</td>
														</tr>
														<tr style="height: 26.6px;">
															<!-- Campo Qta Primaria -->
															<td nowrap>
																<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "QtaInUMPrmMag", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="QtaPrimariaField"><%label.write(out);%></label><%}%>
															</td>
															<td>
																<% 
  WebTextInput OrdineVenditaRigaPrmQtaInUMPrmMag =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "QtaInUMPrmMag"); 
  OrdineVenditaRigaPrmQtaInUMPrmMag.setOnChange("variazioneQuantPrimariaMagazzinoRiga(false)"); 
  OrdineVenditaRigaPrmQtaInUMPrmMag.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmQtaInUMPrmMag.getClassType()%>" id="<%=OrdineVenditaRigaPrmQtaInUMPrmMag.getId()%>" maxlength="<%=OrdineVenditaRigaPrmQtaInUMPrmMag.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmQtaInUMPrmMag.getName()%>" size="<%=OrdineVenditaRigaPrmQtaInUMPrmMag.getSize()%>" type="text"><% 
  OrdineVenditaRigaPrmQtaInUMPrmMag.write(out); 
%>

															</td>
															<!-- Campo Servizio UM Primaria -->
															<td colspan="2" nowrap>
																<% 
  WebTextInput OrdineVenditaRigaPrmServizioUMPrmMag =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ServizioUMPrmMag"); 
  OrdineVenditaRigaPrmServizioUMPrmMag.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmServizioUMPrmMag.getClassType()%>" id="<%=OrdineVenditaRigaPrmServizioUMPrmMag.getId()%>" maxlength="<%=OrdineVenditaRigaPrmServizioUMPrmMag.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmServizioUMPrmMag.getName()%>" size="20" type="text"><% 
  OrdineVenditaRigaPrmServizioUMPrmMag.write(out); 
%>

															</td>
															<td>&nbsp;</td>
														</tr>
														<tr id="SezUMSecondaria" name="SezUMSecondaria" style="display:none; height: 26.6px;">
															<!-- Campo Qta Secondaria -->
															<td nowrap>
																<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "QtaInUMSecMag", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="QtaSecondariaField"><%label.write(out);%></label><%}%>
															</td>
															<td>
																<% 
  WebTextInput OrdineVenditaRigaPrmQtaInUMSecMag =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "QtaInUMSecMag"); 
  OrdineVenditaRigaPrmQtaInUMSecMag.setOnChange("variazioneQuantSecondariaMagazzinoRiga(false)"); 
  OrdineVenditaRigaPrmQtaInUMSecMag.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmQtaInUMSecMag.getClassType()%>" id="<%=OrdineVenditaRigaPrmQtaInUMSecMag.getId()%>" maxlength="<%=OrdineVenditaRigaPrmQtaInUMSecMag.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmQtaInUMSecMag.getName()%>" size="<%=OrdineVenditaRigaPrmQtaInUMSecMag.getSize()%>" type="text"><% 
  OrdineVenditaRigaPrmQtaInUMSecMag.write(out); 
%>

															</td>
															<!-- Campo Servizio UM Secondaria -->
															<td colspan="2" nowrap>
																<% 
  WebTextInput OrdineVenditaRigaPrmServizioUMSecMag =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ServizioUMSecMag"); 
  OrdineVenditaRigaPrmServizioUMSecMag.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmServizioUMSecMag.getClassType()%>" id="<%=OrdineVenditaRigaPrmServizioUMSecMag.getId()%>" maxlength="<%=OrdineVenditaRigaPrmServizioUMSecMag.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmServizioUMSecMag.getName()%>" size="20" type="text"><% 
  OrdineVenditaRigaPrmServizioUMSecMag.write(out); 
%>

															</td>
															<td>&nbsp;</td>
														</tr>
														<!-- fix 12572 > -->
														<tr id="SezNumeroImballo" name="SezNumeroImballo" style="display:none; height: 26.6px;">
															<td nowrap><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "NumeroImballo", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="NumeroImballoField"><%label.write(out);%></label><%}%></td>
															<td><% 
  WebTextInput OrdineVenditaRigaPrmNumeroImballo =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "NumeroImballo"); 
  OrdineVenditaRigaPrmNumeroImballo.setOnChange("numeroImballoManuale()"); 
  OrdineVenditaRigaPrmNumeroImballo.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmNumeroImballo.getClassType()%>" id="<%=OrdineVenditaRigaPrmNumeroImballo.getId()%>" maxlength="<%=OrdineVenditaRigaPrmNumeroImballo.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmNumeroImballo.getName()%>" size="<%=OrdineVenditaRigaPrmNumeroImballo.getSize()%>" type="text"><% 
  OrdineVenditaRigaPrmNumeroImballo.write(out); 
%>
</td>
															<td><input id="UMRifFattoreConvNI" name="UMRifFattoreConvNI" type="hidden"></td>
														</tr>
														<!-- fix 12572 < -->

																												<!-- Fix 3246 inizio -->
														<!-- Check Ricalcolo Quantita Fattore Conversione -->
														<tr style="height: 26.6px;">
															<td>&nbsp;</td>
															<td colspan="2">
																<% 
  WebCheckBox OrdineVenditaRigaPrmRicalcoloQtaFattoreConv =  
     new com.thera.thermfw.web.WebCheckBox("OrdineVenditaRigaPrm", "RicalcoloQtaFattoreConv"); 
  OrdineVenditaRigaPrmRicalcoloQtaFattoreConv.setParent(OrdineVenditaRigaPrmForm); 
%>
<input id="<%=OrdineVenditaRigaPrmRicalcoloQtaFattoreConv.getId()%>" name="<%=OrdineVenditaRigaPrmRicalcoloQtaFattoreConv.getName()%>" type="checkbox" value="Y"><%
  OrdineVenditaRigaPrmRicalcoloQtaFattoreConv.write(out); 
%>

															</td>
														</tr>
														<!-- Fix 3246 fine -->
														<!-- <tr><td style="height: 17px;"></td></tr> -->
													</table>
												</fieldset>
											</td>
											<td style="width: 10px;"></td>
											<td align="left" valign="top" width="400px">
												<fieldset>
													<legend><label class="thLabel" id="LabelGroupBoxDateConsegna">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "DateConsegnaGroupBox", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></legend>
													<table cellpadding="1" cellspacing="1" style="margin-left: 2px;">
														  <% { 
   WebFormCustomization fc = new com.thera.thermfw.web.WebFormCustomization("FORM", "gancioPers_19041"); 
  fc.setParent(OrdineVenditaRigaPrmForm); 
  fc.customize(); } 
%>
<!--<span class="formCustomization" id="gancioPers_19041"></span>--><!-- Fix 19041 -->
                                                          <tr>
															<!-- Campo Data Consegna Richiesta -->
															<td nowrap>
																<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "DataConsegnaRichiesta", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="DataConsegnaRichiestaField"><%label.write(out);%></label><%}%>
															</td>
															<td style="width: 15px;"></td>
															<td nowrap>
																<% 
  WebTextInput OrdineVenditaRigaPrmDataConsegnaRichiesta =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "DataConsegnaRichiesta"); 
  OrdineVenditaRigaPrmDataConsegnaRichiesta.setOnChange("gestDataConsegnaRichiesta()"); 
  OrdineVenditaRigaPrmDataConsegnaRichiesta.setShowCalendarBtn(true); 
  OrdineVenditaRigaPrmDataConsegnaRichiesta.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmDataConsegnaRichiesta.getClassType()%>" id="<%=OrdineVenditaRigaPrmDataConsegnaRichiesta.getId()%>" maxlength="<%=OrdineVenditaRigaPrmDataConsegnaRichiesta.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmDataConsegnaRichiesta.getName()%>" size="12" type="text"><% 
  OrdineVenditaRigaPrmDataConsegnaRichiesta.write(out); 
%>

															</td>
															<td style="width: 30px;"></td>
															<!-- Campo Settimana Consegna Richiesta -->
															<td nowrap><!-- Fix 39280 - fine -->
																<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "SettConsegnaRichiesta", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="SettConsegnaRichiestaField"><%label.write(out);%></label><%}%>
															</td>
															<td style="width: 15px;"></td>
															<td>
																<% 
  WebTextInput OrdineVenditaRigaPrmSettConsegnaRichiesta =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "SettConsegnaRichiesta"); 
  OrdineVenditaRigaPrmSettConsegnaRichiesta.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmSettConsegnaRichiesta.getClassType()%>" id="<%=OrdineVenditaRigaPrmSettConsegnaRichiesta.getId()%>" maxlength="<%=OrdineVenditaRigaPrmSettConsegnaRichiesta.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmSettConsegnaRichiesta.getName()%>" size="8" type="text"><% 
  OrdineVenditaRigaPrmSettConsegnaRichiesta.write(out); 
%>

															</td>
														</tr>
														<tr>
															<!-- Campo Data Consegna Confermata -->
															<td nowrap>
																<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "DataConsegnaConfermata", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="DataConsegnaConfermataField"><%label.write(out);%></label><%}%>
															</td>
															<td style="width: 15px;"></td>
															<td>
																<% 
  WebTextInput OrdineVenditaRigaPrmDataConsegnaConfermata =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "DataConsegnaConfermata"); 
  OrdineVenditaRigaPrmDataConsegnaConfermata.setOnChange("gestDataConsegnaConfermata()"); 
  OrdineVenditaRigaPrmDataConsegnaConfermata.setShowCalendarBtn(true); 
  OrdineVenditaRigaPrmDataConsegnaConfermata.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmDataConsegnaConfermata.getClassType()%>" id="<%=OrdineVenditaRigaPrmDataConsegnaConfermata.getId()%>" maxlength="<%=OrdineVenditaRigaPrmDataConsegnaConfermata.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmDataConsegnaConfermata.getName()%>" size="12" type="text"><% 
  OrdineVenditaRigaPrmDataConsegnaConfermata.write(out); 
%>

															</td>
															<td style="width: 30px;"></td>
															<!-- Campo Settimana Consegna Confermata -->
															<td>
																<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "SettConsegnaConfermata", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="SettConsegnaConfermataField"><%label.write(out);%></label><%}%>
															</td>
															<td style="width: 15px;"></td>
															<td>
																<% 
  WebTextInput OrdineVenditaRigaPrmSettConsegnaConfermata =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "SettConsegnaConfermata"); 
  OrdineVenditaRigaPrmSettConsegnaConfermata.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmSettConsegnaConfermata.getClassType()%>" id="<%=OrdineVenditaRigaPrmSettConsegnaConfermata.getId()%>" maxlength="<%=OrdineVenditaRigaPrmSettConsegnaConfermata.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmSettConsegnaConfermata.getName()%>" size="8" type="text"><% 
  OrdineVenditaRigaPrmSettConsegnaConfermata.write(out); 
%>

															</td>
														</tr>
														<tr>
															<!-- Campo Data Consegna Produzione -->
															<td nowrap>
																<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "DataConsegnaProduzione", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="DataConsegnaProduzioneField"><%label.write(out);%></label><%}%>
															</td>
															<td style="width: 15px;"></td>
															<td>
																<% 
  WebTextInput OrdineVenditaRigaPrmDataConsegnaProduzione =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "DataConsegnaProduzione"); 
  OrdineVenditaRigaPrmDataConsegnaProduzione.setOnChange("gestDataConsegnaProduzione()"); 
  OrdineVenditaRigaPrmDataConsegnaProduzione.setShowCalendarBtn(true); 
  OrdineVenditaRigaPrmDataConsegnaProduzione.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmDataConsegnaProduzione.getClassType()%>" id="<%=OrdineVenditaRigaPrmDataConsegnaProduzione.getId()%>" maxlength="<%=OrdineVenditaRigaPrmDataConsegnaProduzione.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmDataConsegnaProduzione.getName()%>" size="12" type="text"><% 
  OrdineVenditaRigaPrmDataConsegnaProduzione.write(out); 
%>

															</td>
															<td style="width: 30px;"></td>
															<!-- Campo Settimana Consegna Produzione -->
															<td>
																<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "SettConsegnaProduzione", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="SettConsegnaProduzioneField"><%label.write(out);%></label><%}%>
															</td>
															<td style="width: 15px;"></td>
															<td>
																<% 
  WebTextInput OrdineVenditaRigaPrmSettConsegnaProduzione =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "SettConsegnaProduzione"); 
  OrdineVenditaRigaPrmSettConsegnaProduzione.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmSettConsegnaProduzione.getClassType()%>" id="<%=OrdineVenditaRigaPrmSettConsegnaProduzione.getId()%>" maxlength="<%=OrdineVenditaRigaPrmSettConsegnaProduzione.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmSettConsegnaProduzione.getName()%>" size="8" type="text"><% 
  OrdineVenditaRigaPrmSettConsegnaProduzione.write(out); 
%>

															</td>
														</tr>
													</table>
												</fieldset>
											</td>
										</table>
									</td>
								</tr>
								<!-- FINE RIGA 2 TABELLA PRINCIPALE -->
								<!-- INIZIO RIGA 3 TABELLA PRINCIPALE -->
								<tr>
									<td>
										<table>
											<tr>
												<!-- Dati Comuni Estesi - Stato -->
												<td nowrap>
													<table>
														<tr>
															<td style="width: 105px">
																<% 
   request.setAttribute("parentForm", OrdineVenditaRigaPrmForm); 
   String CDForDatiComuniEstesi$it$thera$thip$cs$DatiComuniEstesi$jsp = "DatiComuniEstesi"; 
%>
<jsp:include page="/it/thera/thip/cs/DatiComuniEstesi.jsp" flush="true"> 
<jsp:param name="CDName" value="<%=CDForDatiComuniEstesi$it$thera$thip$cs$DatiComuniEstesi$jsp%>"/> 
</jsp:include> 
<!--<span class="subform" id="StatoDCECombo" name="StatoDCECombo"></span>-->
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<!-- FINE RIGA 3 TABELLA PRINCIPALE -->
							</table>
							<!-- FINE TABELLA PRINCIPALE -->
						<% MainTabbed.endTab(); %> 
</div>

						<!-- **************************************************************************************** -->
						<!-- Cartella Prezzi/Sconti -->
						<div class="tabbed_page" id="<%=MainTabbed.getTabPageId("PrezziScontiTab")%>" style="width:100%;height:100%;overflow:auto;"><% MainTabbed.startTab("PrezziScontiTab"); %>
							<!-- Fix 39280 - inizio modifiche grafiche -->
							<!-- <table cellspacing="1" cellpadding="1"> -->
							<table cellpadding="2" cellspacing="2" style="padding: 3px; width: 100%;">
								<tr>
									<!-- Proxy Listino -->
									<td style="width: 150px">
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdListinoPrezzi", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="ListinoProxy"><%label.write(out);%></label><%}%>
									</td>
									<td colspan="3" nowrap style="width: 300px">
										<% 
  WebMultiSearchForm OrdineVenditaRigaPrmRListino =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "RListino", false, false, true, 1, null, null); 
  OrdineVenditaRigaPrmRListino.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRListino.setOnKeyChange("variazioneListino()"); 
  OrdineVenditaRigaPrmRListino.setAdditionalRestrictConditions("ServizioValutaTestata,RValuta"); 
  OrdineVenditaRigaPrmRListino.write(out); 
%>
<!--<span class="multisearchform" id="ListinoProxy"></span>-->
									</td>

									<!-- Pulsante Ricalcola dati vendita -->
															<!-- Fix 2333 aggiunto l'ID al bottone-->
									<td colspan="3" id="SezBottoneRicalcola" nowrap>
									<!-- Fine Fix 2333 -->

										
<% 
  WebButton RicalcolaDatiVenditaPulsanteWebButton = new WebButton(); 
  RicalcolaDatiVenditaPulsanteWebButton.setImage(null); 
  RicalcolaDatiVenditaPulsanteWebButton.setResourceFile("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga"); 
  RicalcolaDatiVenditaPulsanteWebButton.setResourceId("RicalcolaPulsanteLabel"); 
  RicalcolaDatiVenditaPulsanteWebButton.setResourceTooltipId(null); 
%>
<button class=" <%=RicalcolaDatiVenditaPulsanteWebButton.getButtonCSSClass()%>" id="RicalcolaDatiVenditaPulsante" name="RicalcolaDatiVenditaPulsante" onclick="ricalcolaDatiVendita()" style="width: 130px" title="<%=RicalcolaDatiVenditaPulsanteWebButton.getTitle()%>" type="button"><%RicalcolaDatiVenditaPulsanteWebButton.getBtnContent(out);%></button>
									</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<!-- Campo Prezzo -->
									<td>
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "Prezzo", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="PrezzoField"><%label.write(out);%></label><%}%>
									</td>
									<td style="width: 200px">
										<% 
  WebTextInput OrdineVenditaRigaPrmPrezzo =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "Prezzo"); 
  OrdineVenditaRigaPrmPrezzo.setOnFocus("memorizzaPrezzo()"); 
  OrdineVenditaRigaPrmPrezzo.setOnChange("settaggioManualeProvenienzaPrezzo()"); 
  OrdineVenditaRigaPrmPrezzo.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmPrezzo.getClassType()%>" id="<%=OrdineVenditaRigaPrmPrezzo.getId()%>" maxlength="<%=OrdineVenditaRigaPrmPrezzo.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmPrezzo.getName()%>" size="12" type="text"><% 
  OrdineVenditaRigaPrmPrezzo.write(out); 
%>

										<label id="LabelValuta"></label>
									</td>

									<!-- Fix 3769 gancio per personalizzazione -->
									<% { 
   WebFormCustomization fc = new com.thera.thermfw.web.WebFormCustomization("FORM", "Stabila"); 
  fc.setParent(OrdineVenditaRigaPrmForm); 
  fc.customize(); } 
%>
<!--<span class="formCustomization" id="campiPersonalizzati"></span>-->
									<!-- fine Fix 3769 -->

									<!--          <td>&nbsp;</td>          <td>&nbsp;</td>          -->
									<!-- Campo Prezzo Extra -->
									<td style="width: 110px">
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "PrezzoExtra", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="PrezzoExtraField"><%label.write(out);%></label><%}%>
									</td>
									<td style="width: 150px">
										<% 
  WebTextInput OrdineVenditaRigaPrmPrezzoExtra =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "PrezzoExtra"); 
  OrdineVenditaRigaPrmPrezzoExtra.setOnChange("attivaRicalcoloProvv2OnSave()"); 
  OrdineVenditaRigaPrmPrezzoExtra.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmPrezzoExtra.getClassType()%>" id="<%=OrdineVenditaRigaPrmPrezzoExtra.getId()%>" maxlength="<%=OrdineVenditaRigaPrmPrezzoExtra.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmPrezzoExtra.getName()%>" size="12" type="text"><% 
  OrdineVenditaRigaPrmPrezzoExtra.write(out); 
%>

									</td>
									<!-- Fix 2333 -->
									<td id="SezDettaglioPrezzi" style="display:none; width: 120px">
										<button id="DettaglioPrezzi" name="DettaglioPrezzi" onclick="apriDettaglioPrezzi('RecuperaPrezziExtraOrdVen','false', 'Y')" style="width: 95px" type="button"> <!--12091-->
											<label class="thLabel" id="LabelDettaglioPrezzi">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.base.prezziExtra.resources.DocOrdRiga", "DettaglioPrezzi", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label>
										</button>
									</td>
									<!-- Fine Fix 2333 -->
									<!-- Fix 1495 -->
									<td id="SezDataInizioValiditaLstLbl" style="display:none; width: 75px"><!-- Fix 39280 - fine -->
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "DataInizioValiditaLst", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="DataInizioValiditaLstField"><%label.write(out);%></label><%}%>
									</td>
									<td id="SezDataInizioValiditaLst" style="display:none">
										<% 
  WebTextInput OrdineVenditaRigaPrmDataInizioValiditaLst =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "DataInizioValiditaLst"); 
  OrdineVenditaRigaPrmDataInizioValiditaLst.setShowCalendarBtn(true); 
  OrdineVenditaRigaPrmDataInizioValiditaLst.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmDataInizioValiditaLst.getClassType()%>" id="<%=OrdineVenditaRigaPrmDataInizioValiditaLst.getId()%>" maxlength="<%=OrdineVenditaRigaPrmDataInizioValiditaLst.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmDataInizioValiditaLst.getName()%>" size="12" type="text"><% 
  OrdineVenditaRigaPrmDataInizioValiditaLst.write(out); 
%>

									</td>
									<!-- Fine fix 1495 -->

									<!-- <td>&nbsp;</td> -->
								</tr>
								<tr>
									<!-- Campo Sconto 1 Articolo -->
									<td>
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "ScontoArticolo1", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="ScontoArticolo1Field"><%label.write(out);%></label><%}%>
									</td>
									<td>
										<% 
  WebTextInput OrdineVenditaRigaPrmScontoArticolo1 =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ScontoArticolo1"); 
  OrdineVenditaRigaPrmScontoArticolo1.setOnChange("attivaRicalcoloProvv2OnSave()"); 
  OrdineVenditaRigaPrmScontoArticolo1.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmScontoArticolo1.getClassType()%>" id="<%=OrdineVenditaRigaPrmScontoArticolo1.getId()%>" maxlength="<%=OrdineVenditaRigaPrmScontoArticolo1.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmScontoArticolo1.getName()%>" size="7" type="text"><% 
  OrdineVenditaRigaPrmScontoArticolo1.write(out); 
%>

									</td>
									<!--<td>&nbsp;</td>-->
									<!-- Campo Sconto 2 Articolo -->
									<td>
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "ScontoArticolo2", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="ScontoArticolo2Field"><%label.write(out);%></label><%}%>
									</td>
									<td>
										<% 
  WebTextInput OrdineVenditaRigaPrmScontoArticolo2 =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ScontoArticolo2"); 
  OrdineVenditaRigaPrmScontoArticolo2.setOnChange("attivaRicalcoloProvv2OnSave()"); 
  OrdineVenditaRigaPrmScontoArticolo2.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmScontoArticolo2.getClassType()%>" id="<%=OrdineVenditaRigaPrmScontoArticolo2.getId()%>" maxlength="<%=OrdineVenditaRigaPrmScontoArticolo2.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmScontoArticolo2.getName()%>" size="7" type="text"><% 
  OrdineVenditaRigaPrmScontoArticolo2.write(out); 
%>

									</td>
									<!--<td>&nbsp;</td>-->
									<!-- Campo Maggiorazione -->
									<td style="width: 120px">
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "Maggiorazione", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="MaggiorazioneField"><%label.write(out);%></label><%}%>
									</td>
									<td>
										<% 
  WebTextInput OrdineVenditaRigaPrmMaggiorazione =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "Maggiorazione"); 
  OrdineVenditaRigaPrmMaggiorazione.setOnChange("attivaRicalcoloProvv2OnSave()"); 
  OrdineVenditaRigaPrmMaggiorazione.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmMaggiorazione.getClassType()%>" id="<%=OrdineVenditaRigaPrmMaggiorazione.getId()%>" maxlength="<%=OrdineVenditaRigaPrmMaggiorazione.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmMaggiorazione.getName()%>" size="7" type="text"><% 
  OrdineVenditaRigaPrmMaggiorazione.write(out); 
%>

									</td>
								</tr>
								<tr>
									<!-- Proxy Sconto -->
									<td nowrap>
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdSconto", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="ScontoProxy"><%label.write(out);%></label><%}%>
									</td>
									<td colspan="3" nowrap>
										<% 
  WebMultiSearchForm OrdineVenditaRigaPrmRSconto =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "RSconto", false, false, true, 1, null, null); 
  OrdineVenditaRigaPrmRSconto.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRSconto.setOnKeyChange("attivaRicalcoloProvv2OnSave()"); 
  OrdineVenditaRigaPrmRSconto.setOnSearchBack("attivaRicalcoloProvv2OnSave()"); 
  OrdineVenditaRigaPrmRSconto.write(out); 
%>
<!--<span class="multisearchform" id="ScontoProxy"></span>-->
									</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<!-- Campo Sconto Cliente -->
									<td>
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "PrcScontoIntestatario", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="ScontoClienteField"><%label.write(out);%></label><%}%>
									</td>
									<td>
										<% 
  WebTextInput OrdineVenditaRigaPrmPrcScontoIntestatario =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "PrcScontoIntestatario"); 
  OrdineVenditaRigaPrmPrcScontoIntestatario.setOnChange("attivaRicalcoloProvv2OnSave()"); 
  OrdineVenditaRigaPrmPrcScontoIntestatario.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmPrcScontoIntestatario.getClassType()%>" id="<%=OrdineVenditaRigaPrmPrcScontoIntestatario.getId()%>" maxlength="<%=OrdineVenditaRigaPrmPrcScontoIntestatario.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmPrcScontoIntestatario.getName()%>" size="7" type="text"><% 
  OrdineVenditaRigaPrmPrcScontoIntestatario.write(out); 
%>

									</td>
									<!--<td>&nbsp;</td>-->
									<!-- Campo Sconto Modal. -->
									<td>
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "PrcScontoModalita", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="ScontoModalitaField"><%label.write(out);%></label><%}%>
									</td>
									<td>
										<% 
  WebTextInput OrdineVenditaRigaPrmPrcScontoModalita =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "PrcScontoModalita"); 
  OrdineVenditaRigaPrmPrcScontoModalita.setOnChange("attivaRicalcoloProvv2OnSave()"); 
  OrdineVenditaRigaPrmPrcScontoModalita.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmPrcScontoModalita.getClassType()%>" id="<%=OrdineVenditaRigaPrmPrcScontoModalita.getId()%>" maxlength="<%=OrdineVenditaRigaPrmPrcScontoModalita.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmPrcScontoModalita.getName()%>" size="7" type="text"><% 
  OrdineVenditaRigaPrmPrcScontoModalita.write(out); 
%>

									</td>
								</tr>
								<tr>
									<!-- Proxy Sconto Modal -->
									<td>
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdScontoModalita", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="ScontoModalitaProxy"><%label.write(out);%></label><%}%>
									</td>
									<td colspan="3" nowrap>
										<% 
  WebMultiSearchForm OrdineVenditaRigaPrmRScontoModalita =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "RScontoModalita", false, false, true, 1, null, null); 
  OrdineVenditaRigaPrmRScontoModalita.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRScontoModalita.setOnKeyChange("attivaRicalcoloProvv2OnSave()"); 
  OrdineVenditaRigaPrmRScontoModalita.setOnSearchBack("attivaRicalcoloProvv2OnSave()"); 
  OrdineVenditaRigaPrmRScontoModalita.write(out); 
%>
<!--<span class="multisearchform" id="ScontoModalitaProxy"></span>-->
									</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr rowspan="2">
									<!-- Proxy Assoggettamento IVA -->
									<td>
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdAssoggettamentoIVA", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="AssoggettamentoIVAProxy"><%label.write(out);%></label><%}%>
									</td>
									<td colspan="3" nowrap>
										<% 
  WebMultiSearchForm OrdineVenditaRigaPrmRAssoggettamentoIVA =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "RAssoggettamentoIVA", false, false, true, 1, null, null); 
  OrdineVenditaRigaPrmRAssoggettamentoIVA.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRAssoggettamentoIVA.write(out); 
%>
<!--<span class="multisearchform" id="AssoggettamentoIVAProxy"></span>-->
									</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr style="display: none;">
									<td>&nbsp;</td>
								</tr>
								<tr>
									<!-- Combo Riferimento Prezzo -->
									<td>
 										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "RiferimentoUMPrezzo", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="RiferimentoPrezzoCombo"><%label.write(out);%></label><%}%>
									</td>
									<td>
										<% 
  WebComboBox OrdineVenditaRigaPrmRiferimentoUMPrezzo =  
     new com.thera.thermfw.web.WebComboBox("OrdineVenditaRigaPrm", "RiferimentoUMPrezzo", null); 
  OrdineVenditaRigaPrmRiferimentoUMPrezzo.setParent(OrdineVenditaRigaPrmForm); 
%>
<select id="<%=OrdineVenditaRigaPrmRiferimentoUMPrezzo.getId()%>" name="<%=OrdineVenditaRigaPrmRiferimentoUMPrezzo.getName()%>"><% 
  OrdineVenditaRigaPrmRiferimentoUMPrezzo.write(out); 
%> 

											
										</select>
									</td>
								</tr>
								<tr>
									<!-- Combo Tipo Prezzo -->
									<td>
 										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "TipoPrezzo", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="TipoPrezzoCombo"><%label.write(out);%></label><%}%>
									</td>
									<td colspan="2">
										<% 
  WebComboBox OrdineVenditaRigaPrmTipoPrezzo =  
     new com.thera.thermfw.web.WebComboBox("OrdineVenditaRigaPrm", "TipoPrezzo", null); 
  OrdineVenditaRigaPrmTipoPrezzo.setParent(OrdineVenditaRigaPrmForm); 
%>
<select id="<%=OrdineVenditaRigaPrmTipoPrezzo.getId()%>" name="<%=OrdineVenditaRigaPrmTipoPrezzo.getName()%>"><% 
  OrdineVenditaRigaPrmTipoPrezzo.write(out); 
%> 

											
										</select>
									</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<!-- Combo Provenienza Prezzo -->
									<td>
 										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "ProvenienzaPrezzo", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="ProvenienzaPrezzoCombo"><%label.write(out);%></label><%}%>
									</td>
									<td>
										<% 
  WebComboBox OrdineVenditaRigaPrmProvenienzaPrezzo =  
     new com.thera.thermfw.web.WebComboBox("OrdineVenditaRigaPrm", "ProvenienzaPrezzo", null); 
  OrdineVenditaRigaPrmProvenienzaPrezzo.setParent(OrdineVenditaRigaPrmForm); 
%>
<select id="<%=OrdineVenditaRigaPrmProvenienzaPrezzo.getId()%>" name="<%=OrdineVenditaRigaPrmProvenienzaPrezzo.getName()%>"><% 
  OrdineVenditaRigaPrmProvenienzaPrezzo.write(out); 
%> 

											
										</select>
									</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<!-- Check Blocco Ricalcolo Prezzi -->
									<td colspan="2">
										<% 
  WebCheckBox OrdineVenditaRigaPrmBloccoRicPrezzoScontiFatt =  
     new com.thera.thermfw.web.WebCheckBox("OrdineVenditaRigaPrm", "BloccoRicPrezzoScontiFatt"); 
  OrdineVenditaRigaPrmBloccoRicPrezzoScontiFatt.setParent(OrdineVenditaRigaPrmForm); 
%>
<input id="<%=OrdineVenditaRigaPrmBloccoRicPrezzoScontiFatt.getId()%>" name="<%=OrdineVenditaRigaPrmBloccoRicPrezzoScontiFatt.getName()%>" type="checkbox" value="Y"><%
  OrdineVenditaRigaPrmBloccoRicPrezzoScontiFatt.write(out); 
%>

									</td>
									<td>&nbsp;</td>
								</tr>
							</table>

						<% MainTabbed.endTab(); %> 
</div>




						<!-- **************************************************************************************** -->
						<!-- Cartella Agenti -->
						<div class="tabbed_page" id="<%=MainTabbed.getTabPageId("AgentiTab")%>" style="width:100%;height:100%;overflow:auto;"><% MainTabbed.startTab("AgentiTab"); %>
							<!-- Fix 39280 - inizio modifiche grafiche -->
							<!-- <table cellspacing="1" cellpadding="1"> -->
							<table cellpadding="2" cellspacing="2" style="padding: 3px; width: 100%;">
								<tr>
									<!-- Proxy Agente -->
									<td nowrap style="width: 150px"><!-- Fix 39280 - fine -->
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdAgente", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="AgenteProxy"><%label.write(out);%></label><%}%>
									</td>
									<td colspan="4" nowrap>
										<% 
  WebMultiSearchForm OrdineVenditaRigaPrmRAgente =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "RAgente", false, false, true, 1, null, null); 
  OrdineVenditaRigaPrmRAgente.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRAgente.setOnKeyFocus("memorizzaCodiceAgente()"); 
  OrdineVenditaRigaPrmRAgente.setOnKeyBlur("confrontaCodiceAgente()"); 
  OrdineVenditaRigaPrmRAgente.setOnKeyChange("variazioneCodiceAgente()"); 
  OrdineVenditaRigaPrmRAgente.setOnSearchBack("recuperaProvvigioniAgente()"); 
  OrdineVenditaRigaPrmRAgente.write(out); 
%>
<!--<span class="multisearchform" id="AgenteProxy"></span>-->
									</td>
								</tr>
								<tr>
									<!-- Campo Provvigione 1 Agente -->
									<td>
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "Provvigione1Agente", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="Provvigione1AgenteField"><%label.write(out);%></label><%}%>
									</td>
									<td>
										<% 
  WebTextInput OrdineVenditaRigaPrmProvvigione1Agente =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "Provvigione1Agente"); 
  OrdineVenditaRigaPrmProvvigione1Agente.setOnChange("cambiataProvv1Agente()"); 
  OrdineVenditaRigaPrmProvvigione1Agente.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmProvvigione1Agente.getClassType()%>" id="<%=OrdineVenditaRigaPrmProvvigione1Agente.getId()%>" maxlength="<%=OrdineVenditaRigaPrmProvvigione1Agente.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmProvvigione1Agente.getName()%>" size="5" type="text"><% 
  OrdineVenditaRigaPrmProvvigione1Agente.write(out); 
%>

										<% 
  WebCheckBox OrdineVenditaRigaPrmDifferenzaPrezzoAgente =  
     new com.thera.thermfw.web.WebCheckBox("OrdineVenditaRigaPrm", "DifferenzaPrezzoAgente"); 
  OrdineVenditaRigaPrmDifferenzaPrezzoAgente.setParent(OrdineVenditaRigaPrmForm); 
%>
<input id="<%=OrdineVenditaRigaPrmDifferenzaPrezzoAgente.getId()%>" name="<%=OrdineVenditaRigaPrmDifferenzaPrezzoAgente.getName()%>" style="margin-left: 10px" type="checkbox" value="Y"><%
  OrdineVenditaRigaPrmDifferenzaPrezzoAgente.write(out); 
%>
<!-- Fix 39280 - aggiunto style -->
									</td>
								</tr>
								<tr>
									<!-- Campo Provvigione 2 Agente -->
									<td>
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "Provvigione2Agente", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="Provvigione2AgenteField"><%label.write(out);%></label><%}%>
									</td>
									<td>
										<% 
  WebTextInput OrdineVenditaRigaPrmProvvigione2Agente =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "Provvigione2Agente"); 
  OrdineVenditaRigaPrmProvvigione2Agente.setOnChange("cambiataProvv2Agente()"); 
  OrdineVenditaRigaPrmProvvigione2Agente.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmProvvigione2Agente.getClassType()%>" id="<%=OrdineVenditaRigaPrmProvvigione2Agente.getId()%>" maxlength="<%=OrdineVenditaRigaPrmProvvigione2Agente.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmProvvigione2Agente.getName()%>" size="5" type="text"><% 
  OrdineVenditaRigaPrmProvvigione2Agente.write(out); 
%>

									</td>
								</tr>
								<tr>
									<!-- Proxy Subagente -->
									<td nowrap>
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdSubagente", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="SubagenteProxy"><%label.write(out);%></label><%}%>
									</td>
									<td colspan="4" nowrap>
										<% 
  WebMultiSearchForm OrdineVenditaRigaPrmRSubagente =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "RSubagente", false, false, true, 1, null, null); 
  OrdineVenditaRigaPrmRSubagente.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRSubagente.setOnKeyFocus("memorizzaCodiceSubagente()"); 
  OrdineVenditaRigaPrmRSubagente.setOnKeyBlur("confrontaCodiceSubagente()"); 
  OrdineVenditaRigaPrmRSubagente.setOnKeyChange("variazioneCodiceSubagente()"); 
  OrdineVenditaRigaPrmRSubagente.setOnSearchBack("recuperaProvvigioniSubagente()"); 
  OrdineVenditaRigaPrmRSubagente.write(out); 
%>
<!--<span class="multisearchform" id="SubagenteProxy"></span>-->
									</td>
								</tr>
								<tr>
									<!-- Campo Provvigione 1 Subagente -->
									<td>
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "Provvigione1Subagente", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="Provvigione1SubagenteField"><%label.write(out);%></label><%}%>
									</td>
									<td>
										<% 
  WebTextInput OrdineVenditaRigaPrmProvvigione1Subagente =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "Provvigione1Subagente"); 
  OrdineVenditaRigaPrmProvvigione1Subagente.setOnChange("cambiataProvv1Subagente()"); 
  OrdineVenditaRigaPrmProvvigione1Subagente.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmProvvigione1Subagente.getClassType()%>" id="<%=OrdineVenditaRigaPrmProvvigione1Subagente.getId()%>" maxlength="<%=OrdineVenditaRigaPrmProvvigione1Subagente.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmProvvigione1Subagente.getName()%>" size="5" type="text"><% 
  OrdineVenditaRigaPrmProvvigione1Subagente.write(out); 
%>

										<% 
  WebCheckBox OrdineVenditaRigaPrmDifferenzaPrezzoSubagente =  
     new com.thera.thermfw.web.WebCheckBox("OrdineVenditaRigaPrm", "DifferenzaPrezzoSubagente"); 
  OrdineVenditaRigaPrmDifferenzaPrezzoSubagente.setParent(OrdineVenditaRigaPrmForm); 
%>
<input id="<%=OrdineVenditaRigaPrmDifferenzaPrezzoSubagente.getId()%>" name="<%=OrdineVenditaRigaPrmDifferenzaPrezzoSubagente.getName()%>" style="margin-left: 10px" type="checkbox" value="Y"><%
  OrdineVenditaRigaPrmDifferenzaPrezzoSubagente.write(out); 
%>
<!-- Fix 39280 - aggiunto style -->
									</td>
								</tr>
								<tr>
									<!-- Campo Provvigione 2 Subagente -->
									<td>
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "Provvigione2Subagente", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="Provvigione2SubagenteField"><%label.write(out);%></label><%}%>
									</td>
									<td>
										<% 
  WebTextInput OrdineVenditaRigaPrmProvvigione2Subagente =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "Provvigione2Subagente"); 
  OrdineVenditaRigaPrmProvvigione2Subagente.setOnChange("cambiataProvv2Subagente()"); 
  OrdineVenditaRigaPrmProvvigione2Subagente.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmProvvigione2Subagente.getClassType()%>" id="<%=OrdineVenditaRigaPrmProvvigione2Subagente.getId()%>" maxlength="<%=OrdineVenditaRigaPrmProvvigione2Subagente.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmProvvigione2Subagente.getName()%>" size="5" type="text"><% 
  OrdineVenditaRigaPrmProvvigione2Subagente.write(out); 
%>

									</td>
								</tr>
								<tr>
									<!-- Proxy Responsabile Vendite -->
									<td nowrap>
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdResponVendite", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="ResponsabileVenditeProxy"><%label.write(out);%></label><%}%>
									</td>
									<td colspan="4" nowrap>
										<% 
  WebMultiSearchForm OrdineVenditaRigaPrmRResponsabileVendite =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "RResponsabileVendite", false, false, true, 1, null, null); 
  OrdineVenditaRigaPrmRResponsabileVendite.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRResponsabileVendite.write(out); 
%>
<!--<span class="multisearchform" id="ResponsabileVenditeProxy"></span>-->
									</td>
								</tr>
							</table>
						<% MainTabbed.endTab(); %> 
</div>

						<!-- **************************************************************************************** -->
						<!-- Cartella Cond. evasione -->
						<div class="tabbed_page" id="<%=MainTabbed.getTabPageId("CondEvasioneTab")%>" style="width:100%;height:100%;overflow:auto;"><% MainTabbed.startTab("CondEvasioneTab"); %>
							<!-- Fix 39280 - inizio modifiche grafiche -->
							<!-- <table cellspacing="1" cellpadding="1"> -->
							<table cellpadding="2" cellspacing="2" style="padding: 3px; width: 100%;">
								<tr>
									<td>
										<table cellpadding="1" cellspacing="1" style="width: 100%;">
											<tr>
												<td align="left" valign="top">
													<fieldset>
													<legend><label class="thLabel" id="LabelGroupBoxOffertaCliente">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "LabelGroupBoxOffertaCliente", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></legend>
														<table cellpadding="1" cellspacing="1" style="width: 100%;">
															<tr>
																<td nowrap style="width: 151px"><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "NumOffertaCliFormattato", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="NumOffertaCliFormattato"><%label.write(out);%></label><%}%></td>
																<td style="width: 200px"><% 
  WebTextInput OrdineVenditaRigaPrmNumOffertaCliFormattato =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "NumOffertaCliFormattato"); 
  OrdineVenditaRigaPrmNumOffertaCliFormattato.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmNumOffertaCliFormattato.getClassType()%>" id="<%=OrdineVenditaRigaPrmNumOffertaCliFormattato.getId()%>" maxlength="<%=OrdineVenditaRigaPrmNumOffertaCliFormattato.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmNumOffertaCliFormattato.getName()%>" size="20" type="text"><% 
  OrdineVenditaRigaPrmNumOffertaCliFormattato.write(out); 
%>
</td>

																<td nowrap style="width: 85px; padding-left: 20px"><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "DataOffertaCliente", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="DataOffertaCliente"><%label.write(out);%></label><%}%></td>
																<td nowrap style="width: 140px"><% 
  WebTextInput OrdineVenditaRigaPrmDataOffertaCliente =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "DataOffertaCliente"); 
  OrdineVenditaRigaPrmDataOffertaCliente.setShowCalendarBtn(true); 
  OrdineVenditaRigaPrmDataOffertaCliente.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmDataOffertaCliente.getClassType()%>" id="<%=OrdineVenditaRigaPrmDataOffertaCliente.getId()%>" maxlength="<%=OrdineVenditaRigaPrmDataOffertaCliente.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmDataOffertaCliente.getName()%>" size="20" type="text"><% 
  OrdineVenditaRigaPrmDataOffertaCliente.write(out); 
%>
</td>

																<td nowrap style="width: 85px"><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "RigaOffertaCliente", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="RigaOffertaCliente"><%label.write(out);%></label><%}%></td>
																<td><% 
  WebTextInput OrdineVenditaRigaPrmRigaOffertaCliente =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "RigaOffertaCliente"); 
  OrdineVenditaRigaPrmRigaOffertaCliente.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmRigaOffertaCliente.getClassType()%>" id="<%=OrdineVenditaRigaPrmRigaOffertaCliente.getId()%>" maxlength="<%=OrdineVenditaRigaPrmRigaOffertaCliente.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmRigaOffertaCliente.getName()%>" size="20" type="text"><% 
  OrdineVenditaRigaPrmRigaOffertaCliente.write(out); 
%>
</td>
															</tr>
														</table>
													</fieldset>
												</td>
											</tr>
										</table>
								   </td>
								</tr>

								<tr>
									<td>
										<table>
											<tr>
												<!-- Campo Priorita -->
												<td style="width: 153px">
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "Priorita", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="PrioritaField"><%label.write(out);%></label><%}%>
												</td>
												<td style="width: 100px">
													<% 
  WebTextInput OrdineVenditaRigaPrmPriorita =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "Priorita"); 
  OrdineVenditaRigaPrmPriorita.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmPriorita.getClassType()%>" id="<%=OrdineVenditaRigaPrmPriorita.getId()%>" maxlength="<%=OrdineVenditaRigaPrmPriorita.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmPriorita.getName()%>" size="5" type="text"><% 
  OrdineVenditaRigaPrmPriorita.write(out); 
%>

												</td>
											</tr>
											<tr>
												<!-- Campo Perdita Residuo -->
												<td>
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "PrcPerditaResiduo", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="PerditaResiduoField"><%label.write(out);%></label><%}%>
												</td>
												<td>
													<% 
  WebTextInput OrdineVenditaRigaPrmPrcPerditaResiduo =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "PrcPerditaResiduo"); 
  OrdineVenditaRigaPrmPrcPerditaResiduo.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmPrcPerditaResiduo.getClassType()%>" id="<%=OrdineVenditaRigaPrmPrcPerditaResiduo.getId()%>" maxlength="<%=OrdineVenditaRigaPrmPrcPerditaResiduo.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmPrcPerditaResiduo.getName()%>" size="5" type="text"><% 
  OrdineVenditaRigaPrmPrcPerditaResiduo.write(out); 
%>

												</td>
												<!-- <td>&nbsp;</td> -->
												<td colspan="2"><!-- Fix 39280 - fine -->
													<% 
  WebCheckBox OrdineVenditaRigaPrmRigaNonFrazionabile =  
     new com.thera.thermfw.web.WebCheckBox("OrdineVenditaRigaPrm", "RigaNonFrazionabile"); 
  OrdineVenditaRigaPrmRigaNonFrazionabile.setParent(OrdineVenditaRigaPrmForm); 
%>
<input id="<%=OrdineVenditaRigaPrmRigaNonFrazionabile.getId()%>" name="<%=OrdineVenditaRigaPrmRigaNonFrazionabile.getName()%>" type="checkbox" value="Y"><%
  OrdineVenditaRigaPrmRigaNonFrazionabile.write(out); 
%>

												</td>
											</tr>
											<tr>
												<!-- Campo Gruppo Consegna -->
												<td>
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "GruppoConsegna", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="GruppoConsegnaField"><%label.write(out);%></label><%}%>
												</td>
												<td>
													<% 
  WebTextInput OrdineVenditaRigaPrmGruppoConsegna =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "GruppoConsegna"); 
  OrdineVenditaRigaPrmGruppoConsegna.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmGruppoConsegna.getClassType()%>" id="<%=OrdineVenditaRigaPrmGruppoConsegna.getId()%>" maxlength="<%=OrdineVenditaRigaPrmGruppoConsegna.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmGruppoConsegna.getName()%>" size="5" type="text"><% 
  OrdineVenditaRigaPrmGruppoConsegna.write(out); 
%>

												</td>
											</tr>
											<tr>
												<!-- Proxy Blocco Evasione -->
												<td nowrap>
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdMotivoBloccoEvasioneOrd", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="BloccoEvasProxy"><%label.write(out);%></label><%}%>
												</td>
												<td colspan="3" nowrap>
													<% 
  WebMultiSearchForm OrdineVenditaRigaPrmRBloccoEvas =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "RBloccoEvas", false, false, true, 1, null, null); 
  OrdineVenditaRigaPrmRBloccoEvas.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRBloccoEvas.write(out); 
%>
<!--<span class="multisearchform" id="BloccoEvasProxy"></span>-->
												</td>
												<td>&nbsp;</td>
												<td>&nbsp;</td>
											</tr>
											<tr rowspan="2">
												<!-- Proxy Blocco Pianif. -->
												<td nowrap>
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdMotivoBloccoPianif", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="BloccoPianifProxy"><%label.write(out);%></label><%}%>
												</td>
												<td colspan="3" nowrap>
													<% 
  WebMultiSearchForm OrdineVenditaRigaPrmRBloccoPianif =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "RBloccoPianif", false, false, true, 1, null, null); 
  OrdineVenditaRigaPrmRBloccoPianif.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRBloccoPianif.write(out); 
%>
<!--<span class="multisearchform" id="BloccoPianifProxy"></span>-->
												</td>
												<td>&nbsp;</td>
												<td>&nbsp;</td>
											</tr>
											<tr>
												<!-- Proxy Fornitore -->
												<td height="0" nowrap>
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdFornitore", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="FornitoreProxy"><%label.write(out);%></label><%}%>
												</td>
												<td colspan="3" height="0" nowrap>
													<% 
  WebMultiSearchForm OrdineVenditaRigaPrmRFornitore =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "RFornitore", false, false, true, 1, null, null); 
  OrdineVenditaRigaPrmRFornitore.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRFornitore.write(out); 
%>
<!--<span class="multisearchform" id="FornitoreProxy"></span>-->
												</td>
												<td height="0"> &nbsp;</td>
												<td height="0">&nbsp;</td>
											</tr>
											<tr>
												<!-- Combo Rilascio Ordine Acquisto -->
												<td height="0">
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "RilascioOrdineAcq", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="RilascioOrdineAcquistoCombo"><%label.write(out);%></label><%}%>
												</td>
												<td height="0">
													<% 
  WebComboBox OrdineVenditaRigaPrmRilascioOrdineAcq =  
     new com.thera.thermfw.web.WebComboBox("OrdineVenditaRigaPrm", "RilascioOrdineAcq", null); 
  OrdineVenditaRigaPrmRilascioOrdineAcq.setParent(OrdineVenditaRigaPrmForm); 
%>
<select id="<%=OrdineVenditaRigaPrmRilascioOrdineAcq.getId()%>" name="<%=OrdineVenditaRigaPrmRilascioOrdineAcq.getName()%>"><% 
  OrdineVenditaRigaPrmRilascioOrdineAcq.write(out); 
%> 

														
													</select>
												</td>
											</tr>
											<tr>
												<!-- Combo Rilascio Ordine Lav. Esterna -->
												<td height="0">
 													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "RilascioOrdineLavEsterna", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="RilascioOrdineLavEsternaCombo"><%label.write(out);%></label><%}%>
												</td>
												<td height="0">
													<% 
  WebComboBox OrdineVenditaRigaPrmRilascioOrdineLavEsterna =  
     new com.thera.thermfw.web.WebComboBox("OrdineVenditaRigaPrm", "RilascioOrdineLavEsterna", null); 
  OrdineVenditaRigaPrmRilascioOrdineLavEsterna.setParent(OrdineVenditaRigaPrmForm); 
%>
<select id="<%=OrdineVenditaRigaPrmRilascioOrdineLavEsterna.getId()%>" name="<%=OrdineVenditaRigaPrmRilascioOrdineLavEsterna.getName()%>"><% 
  OrdineVenditaRigaPrmRilascioOrdineLavEsterna.write(out); 
%> 

														
													</select>
												</td>
											</tr>
											<tr>
												<!-- Combo Rilascio Ordine Prod. -->
												<td>
 													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "RilascioOrdinePrd", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="RilascioOrdinePrdCombo"><%label.write(out);%></label><%}%>
												</td>
												<td>
													<% 
  WebComboBox OrdineVenditaRigaPrmRilascioOrdinePrd =  
     new com.thera.thermfw.web.WebComboBox("OrdineVenditaRigaPrm", "RilascioOrdinePrd", null); 
  OrdineVenditaRigaPrmRilascioOrdinePrd.setParent(OrdineVenditaRigaPrmForm); 
%>
<select id="<%=OrdineVenditaRigaPrmRilascioOrdinePrd.getId()%>" name="<%=OrdineVenditaRigaPrmRilascioOrdinePrd.getName()%>"><% 
  OrdineVenditaRigaPrmRilascioOrdinePrd.write(out); 
%> 

														
													</select>
												</td>
											</tr>
											<tr rowspan="2">
												<!-- Combo Stato Accantonamento Prenotazione -->
												<td>
 													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "StatoAccantonamentoPrenot", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="StatoAccantonamentoPrenotCombo"><%label.write(out);%></label><%}%>
												</td>
												<td>
													<% 
  WebComboBox OrdineVenditaRigaPrmStatoAccantonamentoPrenot =  
     new com.thera.thermfw.web.WebComboBox("OrdineVenditaRigaPrm", "StatoAccantonamentoPrenot", null); 
  OrdineVenditaRigaPrmStatoAccantonamentoPrenot.setParent(OrdineVenditaRigaPrmForm); 
%>
<select id="<%=OrdineVenditaRigaPrmStatoAccantonamentoPrenot.getId()%>" name="<%=OrdineVenditaRigaPrmStatoAccantonamentoPrenot.getName()%>"><% 
  OrdineVenditaRigaPrmStatoAccantonamentoPrenot.write(out); 
%> 

														
													</select>
												</td>
												<td>&nbsp;</td>
												<td>
													<div id="StatoConfermaATPDiv" style="display:none">
														<table border="0" cellpadding="0" cellspacing="0">
																<tr>
																	<td nowrap><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "StatoConfermaATP", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="StatoConfermaATPCombo"><%label.write(out);%></label><%}%></td>
																	<td width="0"><% 
  WebComboBox OrdineVenditaRigaPrmStatoConfermaATP =  
     new com.thera.thermfw.web.WebComboBox("OrdineVenditaRigaPrm", "StatoConfermaATP", null); 
  OrdineVenditaRigaPrmStatoConfermaATP.setParent(OrdineVenditaRigaPrmForm); 
%>
<select id="<%=OrdineVenditaRigaPrmStatoConfermaATP.getId()%>" name="<%=OrdineVenditaRigaPrmStatoConfermaATP.getName()%>"><% 
  OrdineVenditaRigaPrmStatoConfermaATP.write(out); 
%> 
</select></td>
																</tr>
														</table>
												</div>
											</td>
											</tr>
										</table>
								   </td>
								</tr>

								<tr>
									<td>
										<table cellpadding="1" cellspacing="1" width="100%">
											<tr>
												<td align="left" valign="top" width="100%">
													<fieldset>
													<legend><label class="thLabel" id="DatiBolla">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.base.comuniVenAcq.resources.ComuniVenAcq", "DatiBolla", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></legend>
													<table cellpadding="1" cellspacing="1">
														<!-- Fix 39280 - inizio modifiche grafiche -->
														<tr>
															<td nowrap style="width: 151px">
																<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "DataBolla", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="DataBolla"><%label.write(out);%></label><%}%>
															</td>
															<td nowrap style="width: 150px">
																<% 
  WebTextInput OrdineVenditaRigaPrmDataBolla =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "DataBolla"); 
  OrdineVenditaRigaPrmDataBolla.setShowCalendarBtn(true); 
  OrdineVenditaRigaPrmDataBolla.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmDataBolla.getClassType()%>" id="<%=OrdineVenditaRigaPrmDataBolla.getId()%>" maxlength="<%=OrdineVenditaRigaPrmDataBolla.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmDataBolla.getName()%>" size="<%=OrdineVenditaRigaPrmDataBolla.getSize()%>" type="text"><% 
  OrdineVenditaRigaPrmDataBolla.write(out); 
%>

															</td>
															<td nowrap style="width: 100px">
																<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "NumeroBolla", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="NumeroBolla"><%label.write(out);%></label><%}%>
															</td>
															<td>
																<% 
  WebTextInput OrdineVenditaRigaPrmNumeroBolla =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "NumeroBolla"); 
  OrdineVenditaRigaPrmNumeroBolla.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmNumeroBolla.getClassType()%>" id="<%=OrdineVenditaRigaPrmNumeroBolla.getId()%>" maxlength="<%=OrdineVenditaRigaPrmNumeroBolla.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmNumeroBolla.getName()%>" size="<%=OrdineVenditaRigaPrmNumeroBolla.getSize()%>" type="text"><% 
  OrdineVenditaRigaPrmNumeroBolla.write(out); 
%>

															</td>
														</tr>
													</table>
													</fieldset>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<table cellpadding="1" cellspacing="1" width="100%">
											<tr>
												<td align="left" valign="top" width="100%">
													<fieldset>
													<legend><label class="thLabel" id="DatiDiTrasporto">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "DatiDiTrasporto", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></legend>
													<table cellpadding="1" cellspacing="1">
														<tr>
															<td nowrap style="width: 151px">
																<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "PesoLordo", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="PesoLordo"><%label.write(out);%></label><%}%>
															</td>
															<td style="width: 150px">
																<% 
  WebTextInput OrdineVenditaRigaPrmPesoLordo =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "PesoLordo"); 
  OrdineVenditaRigaPrmPesoLordo.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmPesoLordo.getClassType()%>" id="<%=OrdineVenditaRigaPrmPesoLordo.getId()%>" maxlength="<%=OrdineVenditaRigaPrmPesoLordo.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmPesoLordo.getName()%>" size="<%=OrdineVenditaRigaPrmPesoLordo.getSize()%>" type="text"><% 
  OrdineVenditaRigaPrmPesoLordo.write(out); 
%>

															</td>
															<td nowrap style="width: 100px">
																<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "PesoNetto", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="PesoNetto"><%label.write(out);%></label><%}%>
															</td>
															<td style="width: 120px">
																<% 
  WebTextInput OrdineVenditaRigaPrmPesoNetto =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "PesoNetto"); 
  OrdineVenditaRigaPrmPesoNetto.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmPesoNetto.getClassType()%>" id="<%=OrdineVenditaRigaPrmPesoNetto.getId()%>" maxlength="<%=OrdineVenditaRigaPrmPesoNetto.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmPesoNetto.getName()%>" size="<%=OrdineVenditaRigaPrmPesoNetto.getSize()%>" type="text"><% 
  OrdineVenditaRigaPrmPesoNetto.write(out); 
%>

															</td>
															<td nowrap style="width: 80px">
																<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "Volume", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="Volume"><%label.write(out);%></label><%}%>
															</td>
															<td>
																<% 
  WebTextInput OrdineVenditaRigaPrmVolume =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "Volume"); 
  OrdineVenditaRigaPrmVolume.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmVolume.getClassType()%>" id="<%=OrdineVenditaRigaPrmVolume.getId()%>" maxlength="<%=OrdineVenditaRigaPrmVolume.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmVolume.getName()%>" size="<%=OrdineVenditaRigaPrmVolume.getSize()%>" type="text"><% 
  OrdineVenditaRigaPrmVolume.write(out); 
%>

															</td>
															<td style="padding-left: 20px"><!-- Fix 39280 - fine -->
																<% 
  WebCheckBox OrdineVenditaRigaPrmRicalcolaPesiEVolume =  
     new com.thera.thermfw.web.WebCheckBox("OrdineVenditaRigaPrm", "RicalcolaPesiEVolume"); 
  OrdineVenditaRigaPrmRicalcolaPesiEVolume.setParent(OrdineVenditaRigaPrmForm); 
%>
<input id="<%=OrdineVenditaRigaPrmRicalcolaPesiEVolume.getId()%>" name="<%=OrdineVenditaRigaPrmRicalcolaPesiEVolume.getName()%>" type="checkbox" value="Y"><%
  OrdineVenditaRigaPrmRicalcolaPesiEVolume.write(out); 
%>

															</td>
														</tr>
													</table>
													</fieldset>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						<% MainTabbed.endTab(); %> 
</div>


						<!-- **************************************************************************************** -->
						<!-- Cartella Contabil. -->
						<div class="tabbed_page" id="<%=MainTabbed.getTabPageId("ContabilTab")%>" style="width:100%;height:100%;overflow:auto;"><% MainTabbed.startTab("ContabilTab"); %>
							<!-- Fix 39280 - inizio modifiche grafiche -->
							<!-- <table cellspacing="1" cellpadding="1"> -->
							<table cellpadding="2" cellspacing="2" style="padding: 3px; width: 100%;">
								<tr>
									<!-- Proxy Commessa -->
									<td nowrap style="width: 150px">
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdCommessa", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="CommessaProxy"><%label.write(out);%></label><%}%>
									</td>
									<td colspan="5" nowrap>
										<% 
  WebMultiSearchForm OrdineVenditaRigaPrmRCommessa =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "RCommessa", false, false, true, 1, null, null); 
  OrdineVenditaRigaPrmRCommessa.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRCommessa.write(out); 
%>
<!--<span class="multisearchform" id="CommessaProxy"></span>-->
									</td>
								</tr>
								<tr>
									<!-- Proxy Centro Ricavo -->
									<td nowrap>
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdCentroRicavo", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="CentroRicavoProxy"><%label.write(out);%></label><%}%>
									</td>
									<td colspan="5" nowrap>
										<% 
  WebMultiSearchForm OrdineVenditaRigaPrmRCentroCosto =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "RCentroCosto", false, false, true, 1, null, null); 
  OrdineVenditaRigaPrmRCentroCosto.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRCentroCosto.write(out); 
%>
<!--<span class="multisearchform" id="CentroRicavoProxy"></span>-->
									</td>
								</tr>
								<tr>
									<!-- Proxy Gruppo Conti Analitica -->
									<td nowrap>
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdGrpCntCa", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="GruppoContiAnaliticaProxy"><%label.write(out);%></label><%}%>
									</td>
									<td nowrap style="width: 500px">
										<% 
  WebMultiSearchForm OrdineVenditaRigaPrmRGruppoContiCA =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "RGruppoContiCA", false, false, true, 1, null, null); 
  OrdineVenditaRigaPrmRGruppoContiCA.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRGruppoContiCA.write(out); 
%>
<!--<span class="multisearchform" id="GruppoContiAnaliticaProxy"></span>-->
									</td>
									<!-- 8450 - Inizio -->
              		<td nowrap><!--12091-->
									<button id="bottoneRicalcolaDatiCA" onclick="recuperaDatiCA()" style="width: 110px" type="button"><!--12091--><!-- Fix 39280 - fine -->
									<label class="thLabel" id="RicalcolaDatiCA">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "RicalcolaDatiCA", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label>
									</button>
              		</td>
						  		<!-- 8450 - Fine -->
								</tr>
								<!-- 8450 - Inizio -->
								<% 
   request.setAttribute("parentForm", OrdineVenditaRigaPrmForm); 
   String CDForDatiCA$it$thera$thip$acquisti$generaleAC$DatiCA$jsp = "DatiCA"; 
%>
<jsp:include page="/it/thera/thip/acquisti/generaleAC/DatiCA.jsp" flush="true"> 
<jsp:param name="CDName" value="<%=CDForDatiCA$it$thera$thip$acquisti$generaleAC$DatiCA$jsp%>"/> 
</jsp:include> 
<!--<span class="subform" id="DatiCA"></span>-->
								<!-- 8450 - Fine -->
							<!-- 24907 - Inizio -->
							<tr id="DatiDichiarIntentoTD">
							   <td colspan="3">
								 <fieldset>
								   <legend><label class="thLabel" id="DatiDichIntento">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.documentoVE.resources.DocumentoVenditaRiga", "DatiDichIntento", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></legend>
							          <table align="center" width="100%">
							           <tr>
									    <!-- Proxy AssoggettementoIVA  dich intento-->
											 <!-- Fix 39280 - aggiunto style e label usa resources -->
									         <td nowrap style="width: 147px"><label class="thLabel" id="LblAssoggIVADichiarIntento">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "LblAssoggIVADichiarIntento", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
									         <td nowrap><% 
  WebMultiSearchForm OrdineVenditaRigaPrmAssoggIVADichiarIntento =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "AssoggIVADichiarIntento", false, false, true, 1, null, null); 
  OrdineVenditaRigaPrmAssoggIVADichiarIntento.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmAssoggIVADichiarIntento.write(out); 
%>
<!--<span class="multisearchform" id="AssoggIVADichiarIntento"></span>--></td>
							           </tr>
							           <tr>
								        	<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "DataDichiarazione", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="DataDichiarazione"><%label.write(out);%></label><%}%></td>
									        <td><% 
  WebTextInput OrdineVenditaRigaPrmDataDichiarazione =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "DataDichiarazione"); 
  OrdineVenditaRigaPrmDataDichiarazione.setShowCalendarBtn(true); 
  OrdineVenditaRigaPrmDataDichiarazione.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmDataDichiarazione.getClassType()%>" id="<%=OrdineVenditaRigaPrmDataDichiarazione.getId()%>" maxlength="<%=OrdineVenditaRigaPrmDataDichiarazione.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmDataDichiarazione.getName()%>" size="<%=OrdineVenditaRigaPrmDataDichiarazione.getSize()%>" type="text"><% 
  OrdineVenditaRigaPrmDataDichiarazione.write(out); 
%>
</td>
							      </tr>
							      <tr>
											<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "NumProtocoloOp", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="NumProtocoloOp"><%label.write(out);%></label><%}%></td>
											<td><% 
  WebTextInput OrdineVenditaRigaPrmNumProtocoloOp =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "NumProtocoloOp"); 
  OrdineVenditaRigaPrmNumProtocoloOp.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmNumProtocoloOp.getClassType()%>" id="<%=OrdineVenditaRigaPrmNumProtocoloOp.getId()%>" maxlength="<%=OrdineVenditaRigaPrmNumProtocoloOp.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmNumProtocoloOp.getName()%>" size="<%=OrdineVenditaRigaPrmNumProtocoloOp.getSize()%>" type="text"><% 
  OrdineVenditaRigaPrmNumProtocoloOp.write(out); 
%>
</td>
							      </tr>
							     </table>
							     </fieldset>
							   </td>
							</tr>
						<!-- 24907 - fine -->
							</table>

						<% MainTabbed.endTab(); %> 
</div>

						<!-- **************************************************************************************** -->
						<!-- Cartella Commenti/Multimedia -->
						<div class="tabbed_page" id="<%=MainTabbed.getTabPageId("CommentiMultimediaTab")%>" style="width:100%;height:100%;overflow:auto;"><% MainTabbed.startTab("CommentiMultimediaTab"); %>
							<table height="20%" width="100%">
								<tr>
									<!-- Area Commenti -->
									<td nowrap>
										<table cellpadding="1" cellspacing="1" width="100%">
											<tr>
												<td>
													<% 
   request.setAttribute("parentForm", OrdineVenditaRigaPrmForm); 
   String CDForCommentHandler$com$thera$thermfw$cbs$CommentHandler$jsp = "CommentHandler"; 
%>
<jsp:include page="/com/thera/thermfw/cbs/CommentHandler.jsp" flush="true"> 
<jsp:param name="CDName" value="<%=CDForCommentHandler$com$thera$thermfw$cbs$CommentHandler$jsp%>"/> 
</jsp:include> 
<!--<span class="subform" id="CommentiArea"></span>-->
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<!-- Fix 39280 - inizio modifiche grafiche -->
							<!-- <table> -->
							<table cellpadding="2" cellspacing="2" style="padding: 3px; width: 100%;">
								<tr>
									<!-- Proxy Documento MM -->
									<td nowrap style="width: 150px"><!-- Fix 39280 - fine -->
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "IdDocumentoMM", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="DocumentoMMProxy"><%label.write(out);%></label><%}%>
									</td>
									<td nowrap>
										<% 
  WebMultiSearchForm OrdineVenditaRigaPrmRDocumentoMM =  
     new com.thera.thermfw.web.WebMultiSearchForm("OrdineVenditaRigaPrm", "RDocumentoMM", false, false, true, 1, null, null); 
  OrdineVenditaRigaPrmRDocumentoMM.setParent(OrdineVenditaRigaPrmForm); 
  OrdineVenditaRigaPrmRDocumentoMM.write(out); 
%>
<!--<span class="multisearchform" id="DocumentoMMProxy"></span>-->
									</td>
								</tr>
								<tr>
									<!-- Area Note -->
									<td nowrap>
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "Nota", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="NoteArea"><%label.write(out);%></label><%}%>
									</td>
									<td colspan="3">
										<% 
  WebTextInput OrdineVenditaRigaPrmNota =  
     new com.thera.thermfw.web.WebTextArea("OrdineVenditaRigaPrm", "Nota"); 
  OrdineVenditaRigaPrmNota.setParent(OrdineVenditaRigaPrmForm); 
%>
<textarea class="<%=OrdineVenditaRigaPrmNota.getClassType()%>" cols="50" id="<%=OrdineVenditaRigaPrmNota.getId()%>" maxlength="<%=OrdineVenditaRigaPrmNota.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmNota.getName()%>" rows="2" size="<%=OrdineVenditaRigaPrmNota.getSize()%>"></textarea><% 
  OrdineVenditaRigaPrmNota.write(out); 
%>

									</td>
								</tr>
							</table>
						<% MainTabbed.endTab(); %> 
</div>

						<!-- **************************************************************************************** -->
						<!-- Cartella Dettaglio lotti -->
						<div class="tabbed_page" id="<%=MainTabbed.getTabPageId("LottiTab")%>" style="width:100%;height:100%;overflow:auto;"><% MainTabbed.startTab("LottiTab"); %>
						<!-- 72XXX Softre Inizio -->
							<table cellpadding="2" cellspacing="2" style="padding: 3px; width: 100%">
							    <tr>
								<td>&nbsp;</td>
								</tr>
								<tr>
									<td nowrap="nowrap" colspan ="1" id="SezSelMultiplaLottiTestori">
										<button style="width: 100px" name="SelezionaLottiMultipliTestori" id="SelezionaLottiMultipliTestori" 
										type="button" onClick="selezionaLottiMultipliTestori()"><%= ResourceLoader.getString("it.testori.thip.magazzino.generalemag.resources.LottiTestori", "btnSelLottiMultiplo")%></button>
									</td>
								</tr>
							</table>
						<!-- 72XXX Softre Fine -->
              <!-- Griglia Lotti -->
              <!--<span class="editgrid" id="LottiGrid">--><% 
  WebEditGrid OrdineVenditaRigaPrmLotti =  
     new com.thera.thermfw.web.WebEditGrid("OrdineVenditaRigaPrm", "Lotti", 6, new String[]{"IdAzienda", "IdArticolo", "CodiceFornitore", "LottoAcquisto", "IdLotto", "RLotto.IdLottoProdotto", "RLotto.IdLottoPrdFormattato", "QtaInUMVen", "QtaInUMPrmMag", "QtaInUMSecMag", "QtaPropostaEvasione.QuantitaInUMRif", "QtaPropostaEvasione.QuantitaInUMPrm", "QtaPropostaEvasione.QuantitaInUMSec", "QtaAttesaEvasione.QuantitaInUMRif", "QtaAttesaEvasione.QuantitaInUMPrm", "QtaAttesaEvasione.QuantitaInUMSec", "QuantitaSpedita.QuantitaInUMRif", "QuantitaSpedita.QuantitaInUMPrm", "QuantitaSpedita.QuantitaInUMSec", "StatoEvasione"}, 1, null, null,false,"com.thera.thermfw.web.servlet.GridActionAdapterForIndependentRow"); 
 OrdineVenditaRigaPrmLotti.setParent(OrdineVenditaRigaPrmForm); 
 OrdineVenditaRigaPrmLotti.setNoControlRowKeys(false); 
 OrdineVenditaRigaPrmLotti.addHideAsDefault("CodiceFornitore"); 
 OrdineVenditaRigaPrmLotti.addHideAsDefault("QuantitaSpedita.QuantitaInUMSec"); 
 OrdineVenditaRigaPrmLotti.addHideAsDefault("QuantitaSpedita.QuantitaInUMRif"); 
 OrdineVenditaRigaPrmLotti.addHideAsDefault("QtaAttesaEvasione.QuantitaInUMPrm"); 
 OrdineVenditaRigaPrmLotti.addHideAsDefault("QtaPropostaEvasione.QuantitaInUMPrm"); 
 OrdineVenditaRigaPrmLotti.addHideAsDefault("QuantitaSpedita.QuantitaInUMPrm"); 
 OrdineVenditaRigaPrmLotti.addHideAsDefault("QtaPropostaEvasione.QuantitaInUMSec"); 
 OrdineVenditaRigaPrmLotti.addHideAsDefault("QtaPropostaEvasione.QuantitaInUMRif"); 
 OrdineVenditaRigaPrmLotti.addHideAsDefault("QtaAttesaEvasione.QuantitaInUMSec"); 
 OrdineVenditaRigaPrmLotti.addHideAsDefault("LottoAcquisto"); 
 OrdineVenditaRigaPrmLotti.addHideAsDefault("RLotto.IdLottoProdotto"); 
 OrdineVenditaRigaPrmLotti.addHideAsDefault("QtaAttesaEvasione.QuantitaInUMRif"); 
 OrdineVenditaRigaPrmLotti.addHideAsDefault("IdLotto"); 
 OrdineVenditaRigaPrmLotti.includeAction("DeleteRow",new WebMenuItem("GestioneMatricole", "action_submit", "new", "no", "it.thera.thip.magazzino.matricole.resources.LottoMatricola", "GestioneMatricoleBtn", "gestioneMatricole()", "none", true, null, "")); 
 OrdineVenditaRigaPrmLotti.write(out); 
%>
<BR><% 
   request.setAttribute("parentForm", OrdineVenditaRigaPrmForm); 
   String CDForLotti = "Lotti"; 
%>
<jsp:include page="/it/thera/thip/vendite/ordineVE/OrdVenRigaPrmLotto.jsp" flush="true"> 
<jsp:param name="EditGridCDName" value="<%=CDForLotti%>"/> 
<jsp:param name="Mode" value="NEW"/> 
</jsp:include> 
<!--</span>-->
						<% MainTabbed.endTab(); %> 
</div>

						<!-- **************************************************************************************** -->
						<!-- Cartella Spedito -->
						<div class="tabbed_page" id="<%=MainTabbed.getTabPageId("SpeditoTab")%>" style="width:100%;height:100%;overflow:auto;"><% MainTabbed.startTab("SpeditoTab"); %>
							<!-- Fix 39280 - inizio modifiche grafiche -->
							<!-- <table cellspacing="3" cellpadding="3" width="100%"> -->
							<table cellpadding="2" cellspacing="2" style="padding: 3px; width: 100%;">
								<tr>
									<td>&nbsp;</td>
									<td>
										<table height="100%" width="100%">
											<tr>
												<td style="width: 150px"><label class="thLabel" id="UMVenLabel">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "UMVenditaLabel", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
												<td style="width: 150px"><label class="thLabel" id="UMPrmLabel">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "UMPrmLabel", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
												<td><label class="thLabel" id="UMSecLabel">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "UMSecLabel", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td nowrap style="width: 150px"><label class="thLabel" id="QtaOrdinataLabel">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "QtaOrdinataLabel", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
									<td><% 
   request.setAttribute("parentForm", OrdineVenditaRigaPrmForm); 
   String CDForQuantitaOrdinata$it$thera$thip$base$comuniVenAcq$QuantitaInUMRif$jsp = "QuantitaOrdinata"; 
%>
<jsp:include page="/it/thera/thip/base/comuniVenAcq/QuantitaInUMRif.jsp" flush="true"> 
<jsp:param name="CDName" value="<%=CDForQuantitaOrdinata$it$thera$thip$base$comuniVenAcq$QuantitaInUMRif$jsp%>"/> 
</jsp:include> 
<!--<span class="subform" id="QuantitaOrdinataField" name="QuantitaOrdinataField"></span>--></td>
								</tr>

								<% { 
   WebFormCustomization fc = new com.thera.thermfw.web.WebFormCustomization("FORM", "gancioPersQta_A"); 
  fc.setParent(OrdineVenditaRigaPrmForm); 
  fc.customize(); } 
%>
<!--<span class="formCustomization" id="gancioPersQta_A"></span>-->	<!-- Fix 28846 -->

								<tr>
									<td nowrap><label class="thLabel" id="QtaPropostaEvasioneLabel">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "QtaPropostaEvasioneLabel", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
									<td><% 
   request.setAttribute("parentForm", OrdineVenditaRigaPrmForm); 
   String CDForQtaPropostaEvasione$it$thera$thip$base$comuniVenAcq$QuantitaInUMRif$jsp = "QtaPropostaEvasione"; 
%>
<jsp:include page="/it/thera/thip/base/comuniVenAcq/QuantitaInUMRif.jsp" flush="true"> 
<jsp:param name="CDName" value="<%=CDForQtaPropostaEvasione$it$thera$thip$base$comuniVenAcq$QuantitaInUMRif$jsp%>"/> 
</jsp:include> 
<!--<span class="subform" id="QtaPropostaEvasioneField" name="QtaPropostaEvasioneField"></span>--></td>
								</tr>

								<% { 
   WebFormCustomization fc = new com.thera.thermfw.web.WebFormCustomization("FORM", "gancioPersQta_B"); 
  fc.setParent(OrdineVenditaRigaPrmForm); 
  fc.customize(); } 
%>
<!--<span class="formCustomization" id="gancioPersQta_B"></span>-->	<!-- Fix 28846 -->

								<tr>
									<td nowrap><label class="thLabel" id="QtaAttesaEvasioneLabel">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "QtaAttesaEvasioneLabel", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
									<td><% 
   request.setAttribute("parentForm", OrdineVenditaRigaPrmForm); 
   String CDForQtaAttesaEvasione$it$thera$thip$base$comuniVenAcq$QuantitaInUMRif$jsp = "QtaAttesaEvasione"; 
%>
<jsp:include page="/it/thera/thip/base/comuniVenAcq/QuantitaInUMRif.jsp" flush="true"> 
<jsp:param name="CDName" value="<%=CDForQtaAttesaEvasione$it$thera$thip$base$comuniVenAcq$QuantitaInUMRif$jsp%>"/> 
</jsp:include> 
<!--<span class="subform" id="QtaAttesaEvasioneField" name="QtaAttesaEvasioneField"></span>--></td>
								</tr>

								<% { 
   WebFormCustomization fc = new com.thera.thermfw.web.WebFormCustomization("FORM", "gancioPersQta_C"); 
  fc.setParent(OrdineVenditaRigaPrmForm); 
  fc.customize(); } 
%>
<!--<span class="formCustomization" id="gancioPersQta_C"></span>-->	<!-- Fix 28846 -->

								<tr>
									<td nowrap><label class="thLabel" id="QtaSpeditaLabel">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "QtaSpeditaLabel", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
									<td><% 
   request.setAttribute("parentForm", OrdineVenditaRigaPrmForm); 
   String CDForQuantitaSpedita$it$thera$thip$base$comuniVenAcq$QuantitaInUMRif$jsp = "QuantitaSpedita"; 
%>
<jsp:include page="/it/thera/thip/base/comuniVenAcq/QuantitaInUMRif.jsp" flush="true"> 
<jsp:param name="CDName" value="<%=CDForQuantitaSpedita$it$thera$thip$base$comuniVenAcq$QuantitaInUMRif$jsp%>"/> 
</jsp:include> 
<!--<span class="subform" id="QuantitaSpeditaField" name="QuantitaSpeditaField"></span>--></td>
								</tr>

								<% { 
   WebFormCustomization fc = new com.thera.thermfw.web.WebFormCustomization("FORM", "gancioPersQta_D"); 
  fc.setParent(OrdineVenditaRigaPrmForm); 
  fc.customize(); } 
%>
<!--<span class="formCustomization" id="gancioPersQta_D"></span>-->	<!-- Fix 28846 -->

								<tr>
									<td nowrap><label class="thLabel" id="QtaResiduoLabel">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "QtaResiduoLabel", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
									<td><% 
   request.setAttribute("parentForm", OrdineVenditaRigaPrmForm); 
   String CDForQuantitaResiduo$it$thera$thip$base$comuniVenAcq$QuantitaInUMRif$jsp = "QuantitaResiduo"; 
%>
<jsp:include page="/it/thera/thip/base/comuniVenAcq/QuantitaInUMRif.jsp" flush="true"> 
<jsp:param name="CDName" value="<%=CDForQuantitaResiduo$it$thera$thip$base$comuniVenAcq$QuantitaInUMRif$jsp%>"/> 
</jsp:include> 
<!--<span class="subform" id="QuantitaResiduoField" name="QuantitaResiduoField"></span>--></td>
								</tr>

								<% { 
   WebFormCustomization fc = new com.thera.thermfw.web.WebFormCustomization("FORM", "gancioPersQta_E"); 
  fc.setParent(OrdineVenditaRigaPrmForm); 
  fc.customize(); } 
%>
<!--<span class="formCustomization" id="gancioPersQta_E"></span>-->	<!-- Fix 28846 -->

								<tr>
									<td nowrap><label class="thLabel" id="QtaAccantonataLabel">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "QtaAccantonataLabel", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
									<td><% 
   request.setAttribute("parentForm", OrdineVenditaRigaPrmForm); 
   String CDForQuantitaAccantonata$it$thera$thip$base$comuniVenAcq$QuantitaInUM$jsp = "QuantitaAccantonata"; 
%>
<jsp:include page="/it/thera/thip/base/comuniVenAcq/QuantitaInUM.jsp" flush="true"> 
<jsp:param name="CDName" value="<%=CDForQuantitaAccantonata$it$thera$thip$base$comuniVenAcq$QuantitaInUM$jsp%>"/> 
</jsp:include> 
<!--<span class="subform" id="QuantitaAccantonataField" name="QuantitaAccantonataField"></span>--></td>
								</tr><!-- Fix 39280 - fine -->

								<% { 
   WebFormCustomization fc = new com.thera.thermfw.web.WebFormCustomization("FORM", "gancioPersQta_F"); 
  fc.setParent(OrdineVenditaRigaPrmForm); 
  fc.customize(); } 
%>
<!--<span class="formCustomization" id="gancioPersQta_F"></span>-->	<!-- Fix 28846 -->

							</table>
							<!-- <table cellspacing="4" cellpadding="4"> -->
							<table cellpadding="2" cellspacing="2" style="padding: 3px; width: 100%;"><!-- Fix 39280 -->
								<tr>
									<!-- Combo Stato Evasione -->
									<td nowrap style="width: 150px"><!-- Fix 39280 - aggiunto style -->
										<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "StatoEvasione", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="StatoEvasioneCombo"><%label.write(out);%></label><%}%>
									</td>
									<td style="width: 148px; padding-left: 5px"><!-- Fix 39280 - aggiunto style -->
										<% 
  WebComboBox OrdineVenditaRigaPrmStatoEvasione =  
     new com.thera.thermfw.web.WebComboBox("OrdineVenditaRigaPrm", "StatoEvasione", null); 
  OrdineVenditaRigaPrmStatoEvasione.setParent(OrdineVenditaRigaPrmForm); 
%>
<select id="<%=OrdineVenditaRigaPrmStatoEvasione.getId()%>" name="<%=OrdineVenditaRigaPrmStatoEvasione.getName()%>"><% 
  OrdineVenditaRigaPrmStatoEvasione.write(out); 
%> 

											
										</select>
									</td>
									<!-- Check Saldo Manuale -->
									<td nowrap>
										<% 
  WebCheckBox OrdineVenditaRigaPrmSaldoManuale =  
     new com.thera.thermfw.web.WebCheckBox("OrdineVenditaRigaPrm", "SaldoManuale"); 
  OrdineVenditaRigaPrmSaldoManuale.setParent(OrdineVenditaRigaPrmForm); 
%>
<input id="<%=OrdineVenditaRigaPrmSaldoManuale.getId()%>" name="<%=OrdineVenditaRigaPrmSaldoManuale.getName()%>" type="checkbox" value="Y"><%
  OrdineVenditaRigaPrmSaldoManuale.write(out); 
%>

									</td>
								</tr>
								<tr>
									<!-- Pulsante Salda/Riapri -->
									<td>
										
<% 
  WebButton SaldaRiapriPulsanteWebButton = new WebButton(); 
  SaldaRiapriPulsanteWebButton.setImage(null); 
  SaldaRiapriPulsanteWebButton.setResourceFile(null); 
  SaldaRiapriPulsanteWebButton.setResourceId(null); 
  SaldaRiapriPulsanteWebButton.setResourceTooltipId(null); 
%>
<button class=" <%=SaldaRiapriPulsanteWebButton.getButtonCSSClass()%>" id="SaldaRiapriPulsante" name="SaldaRiapriPulsante" onclick="gestioneSaldaRiapri()" style="width: 50px" title="<%=SaldaRiapriPulsanteWebButton.getTitle()%>" type="button"><%SaldaRiapriPulsanteWebButton.getBtnContent(out);%></button>
									</td>
								</tr>
							</table>
						<% MainTabbed.endTab(); %> 
</div>

						<!-- ************************************************************************************************ -->
						<!-- Cartella Riepilogo -->
						<div class="tabbed_page" id="<%=MainTabbed.getTabPageId("RiepilogoTab")%>" style="width:100%;height:100%;overflow:auto;"><% MainTabbed.startTab("RiepilogoTab"); %>
							<!-- Fix 39280 - inizio modifiche grafiche -->
							<!-- <table cellspacing="0" cellpadding="0"> -->
							<table cellpadding="2" cellspacing="2" style="padding: 3px; width: 100%;">
								<tr id="SezValoreValutaAzienda" name="SezValoreValutaAzienda" style="display:none">
									<td>
										<fieldset>
											<legend><label class="thLabel" id="LabelValutaOrdineVO">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "LabelValutaOrdine", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></legend>
											<table cellspacing="5">
												<!-- Intestazioni -->
												<tr>
													<td style="width: 150px">&nbsp;</td>
													<td align="center" colspan="2" style="width: 170px"><label class="thLabel" id="LabelValoreOrdinato">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "ValoreOrdinatoLabel", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
													<td style="display: none">&nbsp;</td>
													<td align="center" colspan="2" style="width: 170px"><label class="thLabel" id="LabelValoreInSpedizione">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "ValoreInSpedizioneLabel", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
													<td style="display: none">&nbsp;</td>
													<td align="center" colspan="2" style="width: 170px"><label class="thLabel" id="LabelValoreConsegnato">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "ValoreConsegnatoLabel", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
													<td style="display: none">&nbsp;</td>
												</tr>
												<tr>
													<td nowrap style="width: 150px"><label class="thLabel" id="LabelGroupBoxValore">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "ValoreGroupBox", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
													<!-- Campo Valore Ordine Ordinato in Valuta Ordine -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmValoreOrdinato =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ValoreOrdinato"); 
  OrdineVenditaRigaPrmValoreOrdinato.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmValoreOrdinato.getClassType()%>" id="<%=OrdineVenditaRigaPrmValoreOrdinato.getId()%>" maxlength="<%=OrdineVenditaRigaPrmValoreOrdinato.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmValoreOrdinato.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmValoreOrdinato.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
													<!-- Campo Valore Ordine In Entrata in Valuta Ordine -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmValoreInSpedizione =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ValoreInSpedizione"); 
  OrdineVenditaRigaPrmValoreInSpedizione.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmValoreInSpedizione.getClassType()%>" id="<%=OrdineVenditaRigaPrmValoreInSpedizione.getId()%>" maxlength="<%=OrdineVenditaRigaPrmValoreInSpedizione.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmValoreInSpedizione.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmValoreInSpedizione.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
													<!-- Campo Valore Ordine ricevuto in Valuta Ordine -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmValoreConsegnato =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ValoreConsegnato"); 
  OrdineVenditaRigaPrmValoreConsegnato.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmValoreConsegnato.getClassType()%>" id="<%=OrdineVenditaRigaPrmValoreConsegnato.getId()%>" maxlength="<%=OrdineVenditaRigaPrmValoreConsegnato.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmValoreConsegnato.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmValoreConsegnato.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
												</tr>
												<tr>
													<td nowrap><label class="thLabel" id="LabelGroupBoxCosto">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "CostoGroupBox", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
													<!-- Campo Costo Ordine Ordinato in Valuta Ordine -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmCostoOrdinato =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "CostoOrdinato"); 
  OrdineVenditaRigaPrmCostoOrdinato.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmCostoOrdinato.getClassType()%>" id="<%=OrdineVenditaRigaPrmCostoOrdinato.getId()%>" maxlength="<%=OrdineVenditaRigaPrmCostoOrdinato.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmCostoOrdinato.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmCostoOrdinato.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
													<!-- Campo Costo Ordine In Entrata in Valuta Ordine -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmCostoInSpedizione =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "CostoInSpedizione"); 
  OrdineVenditaRigaPrmCostoInSpedizione.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmCostoInSpedizione.getClassType()%>" id="<%=OrdineVenditaRigaPrmCostoInSpedizione.getId()%>" maxlength="<%=OrdineVenditaRigaPrmCostoInSpedizione.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmCostoInSpedizione.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmCostoInSpedizione.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
													<!-- Campo Costo Ordine ricevuto in Valuta Ordine -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmCostoConsegnato =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "CostoConsegnato"); 
  OrdineVenditaRigaPrmCostoConsegnato.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmCostoConsegnato.getClassType()%>" id="<%=OrdineVenditaRigaPrmCostoConsegnato.getId()%>" maxlength="<%=OrdineVenditaRigaPrmCostoConsegnato.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmCostoConsegnato.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmCostoConsegnato.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
												</tr>
												<tr>
													<td nowrap><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "ValoreImpostaOrd", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="ValoreImpostaOrdField"><%label.write(out);%></label><%}%></td>
													<!-- Campo Valore Ordine Ordinato in Valuta Ordine -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmValoreImpostaOrd =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ValoreImpostaOrd"); 
  OrdineVenditaRigaPrmValoreImpostaOrd.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmValoreImpostaOrd.getClassType()%>" id="<%=OrdineVenditaRigaPrmValoreImpostaOrd.getId()%>" maxlength="<%=OrdineVenditaRigaPrmValoreImpostaOrd.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmValoreImpostaOrd.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmValoreImpostaOrd.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
													<!-- Campo Valore Ordine In Entrata in Valuta Ordine -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmValoreImpostaInSp =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ValoreImpostaInSp"); 
  OrdineVenditaRigaPrmValoreImpostaInSp.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmValoreImpostaInSp.getClassType()%>" id="<%=OrdineVenditaRigaPrmValoreImpostaInSp.getId()%>" maxlength="<%=OrdineVenditaRigaPrmValoreImpostaInSp.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmValoreImpostaInSp.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmValoreImpostaInSp.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
													<!-- Campo Valore Ordine ricevuto in Valuta Ordine -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmValoreImpostaCons =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ValoreImpostaCons"); 
  OrdineVenditaRigaPrmValoreImpostaCons.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmValoreImpostaCons.getClassType()%>" id="<%=OrdineVenditaRigaPrmValoreImpostaCons.getId()%>" maxlength="<%=OrdineVenditaRigaPrmValoreImpostaCons.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmValoreImpostaCons.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmValoreImpostaCons.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
												</tr>
												<!-- FIX 5518 LP inizio -->
												<tr id="tr_ValoreTotaleRiga">
													<td nowrap><label class="thLabel" id="LabelTotale">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.base.comuniVenAcq.resources.DocumentoRiga", "ValoreTotaleRiga", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
													<!-- Campo Valore Totale Ordinato in Valuta Ordine -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmValoreTotaleRiga =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ValoreTotaleRiga"); 
  OrdineVenditaRigaPrmValoreTotaleRiga.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmValoreTotaleRiga.getClassType()%>" id="<%=OrdineVenditaRigaPrmValoreTotaleRiga.getId()%>" maxlength="<%=OrdineVenditaRigaPrmValoreTotaleRiga.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmValoreTotaleRiga.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmValoreTotaleRiga.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
													<!-- Campo Valore Totale In Spedizione in Valuta Ordine -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmValoreInSpedTotaleRiga =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ValoreInSpedTotaleRiga"); 
  OrdineVenditaRigaPrmValoreInSpedTotaleRiga.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmValoreInSpedTotaleRiga.getClassType()%>" id="<%=OrdineVenditaRigaPrmValoreInSpedTotaleRiga.getId()%>" maxlength="<%=OrdineVenditaRigaPrmValoreInSpedTotaleRiga.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmValoreInSpedTotaleRiga.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmValoreInSpedTotaleRiga.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
													<!-- Campo Valore Totale consegnato in Valuta Ordine -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmValoreConsTotaleRiga =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ValoreConsTotaleRiga"); 
  OrdineVenditaRigaPrmValoreConsTotaleRiga.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmValoreConsTotaleRiga.getClassType()%>" id="<%=OrdineVenditaRigaPrmValoreConsTotaleRiga.getId()%>" maxlength="<%=OrdineVenditaRigaPrmValoreConsTotaleRiga.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmValoreConsTotaleRiga.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmValoreConsTotaleRiga.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
												</tr>
												<!-- FIX 5518 LP fine -->
											</table>
										</fieldset>
									</td>
								</tr>
								<tr>
									<td>
										<fieldset>
											<legend><label class="thLabel" id="LabelValutaAziendaVO">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "LabelValutaAzienda", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></legend>
											<table cellspacing="5">
												<!-- Intestazioni -->
												<tr>
													<td style="width: 150px">&nbsp;</td>
													<td align="center" colspan="2" style="width: 170px"><label class="thLabel" id="LabelCostoOrdinato">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "CostoOrdinatoLabel", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
													<td style="display: none">&nbsp;</td>
													<td align="center" colspan="2" style="width: 170px"><label class="thLabel" id="LabelCostoInSpedizione">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "CostoInSpedizioneLabel", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
													<td style="display: none">&nbsp;</td>
													<td align="center" colspan="2" style="width: 170px"><label class="thLabel" id="LabelCostoConsegnato">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "CostoConsegnatoLabel", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
													<td style="display: none">&nbsp;</td>
												</tr>
												<tr>
													<td nowrap><label class="thLabel" id="LabelGroupBoxValoreVA">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.acquisti.ordineAC.resources.OrdineAcquistoRiga", "ValoreGroupBox", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
													<!-- Campo Valore Ordine Ordinato in Valuta Azienda -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmValoreOrdinatoVA =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ValoreOrdinatoVA"); 
  OrdineVenditaRigaPrmValoreOrdinatoVA.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmValoreOrdinatoVA.getClassType()%>" id="<%=OrdineVenditaRigaPrmValoreOrdinatoVA.getId()%>" maxlength="<%=OrdineVenditaRigaPrmValoreOrdinatoVA.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmValoreOrdinatoVA.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmValoreOrdinatoVA.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
													<!-- Campo Valore Ordine In Entrata in Valuta Azienda -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmValoreInSpedizioneVA =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ValoreInSpedizioneVA"); 
  OrdineVenditaRigaPrmValoreInSpedizioneVA.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmValoreInSpedizioneVA.getClassType()%>" id="<%=OrdineVenditaRigaPrmValoreInSpedizioneVA.getId()%>" maxlength="<%=OrdineVenditaRigaPrmValoreInSpedizioneVA.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmValoreInSpedizioneVA.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmValoreInSpedizioneVA.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
													<!-- Campo Valore Ordine ricevuto in Valuta Azienda -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmValoreConsegnatoVA =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ValoreConsegnatoVA"); 
  OrdineVenditaRigaPrmValoreConsegnatoVA.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmValoreConsegnatoVA.getClassType()%>" id="<%=OrdineVenditaRigaPrmValoreConsegnatoVA.getId()%>" maxlength="<%=OrdineVenditaRigaPrmValoreConsegnatoVA.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmValoreConsegnatoVA.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmValoreConsegnatoVA.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
												</tr>
												<tr>
													<td nowrap><label class="thLabel" id="LabelGroupBoxCostoVA">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.acquisti.ordineAC.resources.OrdineAcquistoRiga", "CostoGroupBox", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
													<!-- Campo Costo Ordine Ordinato in Valuta Azienda -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmCostoOrdinatoVA =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "CostoOrdinatoVA"); 
  OrdineVenditaRigaPrmCostoOrdinatoVA.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmCostoOrdinatoVA.getClassType()%>" id="<%=OrdineVenditaRigaPrmCostoOrdinatoVA.getId()%>" maxlength="<%=OrdineVenditaRigaPrmCostoOrdinatoVA.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmCostoOrdinatoVA.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmCostoOrdinatoVA.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
													<!-- Campo Costo Ordine In Entrata in Valuta Azienda -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmCostoInSpedizioneVA =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "CostoInSpedizioneVA"); 
  OrdineVenditaRigaPrmCostoInSpedizioneVA.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmCostoInSpedizioneVA.getClassType()%>" id="<%=OrdineVenditaRigaPrmCostoInSpedizioneVA.getId()%>" maxlength="<%=OrdineVenditaRigaPrmCostoInSpedizioneVA.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmCostoInSpedizioneVA.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmCostoInSpedizioneVA.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
													<!-- Campo Costo Ordine ricevuto in Valuta Azienda -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmCostoConsegnatoVA =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "CostoConsegnatoVA"); 
  OrdineVenditaRigaPrmCostoConsegnatoVA.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmCostoConsegnatoVA.getClassType()%>" id="<%=OrdineVenditaRigaPrmCostoConsegnatoVA.getId()%>" maxlength="<%=OrdineVenditaRigaPrmCostoConsegnatoVA.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmCostoConsegnatoVA.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmCostoConsegnatoVA.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
												</tr>
												<tr>
													<td nowrap><label class="thLabel" id="LabelGroupBoxScostamento">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.acquisti.ordineAC.resources.OrdineAcquistoRiga", "ScostamentoGroupBox", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
													<!-- Campo Costo Ordine Ordinato in Valuta Azienda -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmScostamentoOrdinatoVA =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ScostamentoOrdinatoVA"); 
  OrdineVenditaRigaPrmScostamentoOrdinatoVA.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmScostamentoOrdinatoVA.getClassType()%>" id="<%=OrdineVenditaRigaPrmScostamentoOrdinatoVA.getId()%>" maxlength="<%=OrdineVenditaRigaPrmScostamentoOrdinatoVA.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmScostamentoOrdinatoVA.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmScostamentoOrdinatoVA.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
													<!-- Campo Costo Ordine In Entrata in Valuta Azienda -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmScostamentoInSpedizioneVA =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ScostamentoInSpedizioneVA"); 
  OrdineVenditaRigaPrmScostamentoInSpedizioneVA.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmScostamentoInSpedizioneVA.getClassType()%>" id="<%=OrdineVenditaRigaPrmScostamentoInSpedizioneVA.getId()%>" maxlength="<%=OrdineVenditaRigaPrmScostamentoInSpedizioneVA.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmScostamentoInSpedizioneVA.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmScostamentoInSpedizioneVA.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
													<!-- Campo Costo Ordine ricevuto in Valuta Azienda -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmScostamentoConsegnatoVA =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ScostamentoConsegnatoVA"); 
  OrdineVenditaRigaPrmScostamentoConsegnatoVA.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmScostamentoConsegnatoVA.getClassType()%>" id="<%=OrdineVenditaRigaPrmScostamentoConsegnatoVA.getId()%>" maxlength="<%=OrdineVenditaRigaPrmScostamentoConsegnatoVA.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmScostamentoConsegnatoVA.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmScostamentoConsegnatoVA.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
												</tr>
												<!-- Commentato queste celle vuote per recuperare spazio dopo aver aggiunto                i campi dei totati (chiesto a Piettro se potevo farlo!!!)-->
												<!--<tr>              <td>&nbsp;</td>              <td>&nbsp;</td>              <td>&nbsp;</td>              <td>&nbsp;</td>              <td>&nbsp;</td>             </tr>-->
												<tr>
													<!-- Fix 5245 BP ini... -->
													<!--<td nowrap="true"><label for="ValoreImpostaOrdVAField">Imposta</label></td>-->
													<td nowrap><label id="LabelImposta">Imposta</label></td>
													<!-- Fix 5245 BP fine -->
													<!-- Campo Valore Ordine Ordinato in Valuta Ordine -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmValoreImpostaOrdVA =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ValoreImpostaOrdVA"); 
  OrdineVenditaRigaPrmValoreImpostaOrdVA.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmValoreImpostaOrdVA.getClassType()%>" id="<%=OrdineVenditaRigaPrmValoreImpostaOrdVA.getId()%>" maxlength="<%=OrdineVenditaRigaPrmValoreImpostaOrdVA.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmValoreImpostaOrdVA.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmValoreImpostaOrdVA.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
													<!-- Campo Valore Ordine In Entrata in Valuta Ordine -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmValoreImpostaInSpVA =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ValoreImpostaInSpVA"); 
  OrdineVenditaRigaPrmValoreImpostaInSpVA.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmValoreImpostaInSpVA.getClassType()%>" id="<%=OrdineVenditaRigaPrmValoreImpostaInSpVA.getId()%>" maxlength="<%=OrdineVenditaRigaPrmValoreImpostaInSpVA.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmValoreImpostaInSpVA.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmValoreImpostaInSpVA.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
													<!-- Campo Valore Ordine ricevuto in Valuta Ordine -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmValoreImpostaConsVA =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ValoreImpostaConsVA"); 
  OrdineVenditaRigaPrmValoreImpostaConsVA.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmValoreImpostaConsVA.getClassType()%>" id="<%=OrdineVenditaRigaPrmValoreImpostaConsVA.getId()%>" maxlength="<%=OrdineVenditaRigaPrmValoreImpostaConsVA.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmValoreImpostaConsVA.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmValoreImpostaConsVA.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
												</tr>
												<!-- FIX 5518 LP inizio -->
												<tr id="tr_ValoreTotaleRigaVA">
													<td nowrap><label class="thLabel" id="LabelTotale">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.base.comuniVenAcq.resources.DocumentoRiga", "ValoreTotaleRiga", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label></td>
													<!-- Campo Valore Totale Ordinato in Valuta Aziendale -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmValoreTotaleRigaVA =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ValoreTotaleRigaVA"); 
  OrdineVenditaRigaPrmValoreTotaleRigaVA.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmValoreTotaleRigaVA.getClassType()%>" id="<%=OrdineVenditaRigaPrmValoreTotaleRigaVA.getId()%>" maxlength="<%=OrdineVenditaRigaPrmValoreTotaleRigaVA.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmValoreTotaleRigaVA.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmValoreTotaleRigaVA.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
													<!-- Campo Valore Totale In Spedizione in Valuta Aziendale -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmValoreInSpedTotaleRigaVA =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ValoreInSpedTotaleRigaVA"); 
  OrdineVenditaRigaPrmValoreInSpedTotaleRigaVA.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmValoreInSpedTotaleRigaVA.getClassType()%>" id="<%=OrdineVenditaRigaPrmValoreInSpedTotaleRigaVA.getId()%>" maxlength="<%=OrdineVenditaRigaPrmValoreInSpedTotaleRigaVA.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmValoreInSpedTotaleRigaVA.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmValoreInSpedTotaleRigaVA.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
													<!-- Campo Valore Totale consegnato in Valuta Aziendale -->
													<td align="center" colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmValoreConsTotaleRigaVA =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ValoreConsTotaleRigaVA"); 
  OrdineVenditaRigaPrmValoreConsTotaleRigaVA.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmValoreConsTotaleRigaVA.getClassType()%>" id="<%=OrdineVenditaRigaPrmValoreConsTotaleRigaVA.getId()%>" maxlength="<%=OrdineVenditaRigaPrmValoreConsTotaleRigaVA.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmValoreConsTotaleRigaVA.getName()%>" size="20" style="width: 147px" type="text"><% 
  OrdineVenditaRigaPrmValoreConsTotaleRigaVA.write(out); 
%>
</td>
													<td style="display: none">&nbsp;</td>
												</tr>
												<!-- FIX 5518 LP fine -->
												<!-- Fix 39280 - fine -->
											</table>
										</fieldset>
									</td>
								</tr>
								<!-- Fix 2183 -->
								<tr>
									<td>
								<!-- Fine fix 2183 -->
										<!-- Fix 39280 - inizio modifiche grafiche -->
										<!-- <table cellspacing="0" cellpadding="0"> -->
										<table cellpadding="2" cellspacing="2" style="width: 100%;">
											<tr>
												<td nowrap style="width: 166px;"><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "CostoUnitario", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="CostoUnitarioField"><%label.write(out);%></label><%}%></td><!-- Fix 39280 - fine -->
												<!-- Campo Costo Unitario -->
												<td colspan="2"><% 
  WebTextInput OrdineVenditaRigaPrmCostoUnitario =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "CostoUnitario"); 
  OrdineVenditaRigaPrmCostoUnitario.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmCostoUnitario.getClassType()%>" id="<%=OrdineVenditaRigaPrmCostoUnitario.getId()%>" maxlength="<%=OrdineVenditaRigaPrmCostoUnitario.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmCostoUnitario.getName()%>" size="20" type="text"><% 
  OrdineVenditaRigaPrmCostoUnitario.write(out); 
%>
</td>
											</tr>
											<tr>
												<!-- Combo Stato Invio EDI -->
												<td nowrap><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "StatoInvioEDI", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="StatoInvioEDICombo"><%label.write(out);%></label><%}%></td>
												<td><% 
  WebComboBox OrdineVenditaRigaPrmStatoInvioEDI =  
     new com.thera.thermfw.web.WebComboBox("OrdineVenditaRigaPrm", "StatoInvioEDI", null); 
  OrdineVenditaRigaPrmStatoInvioEDI.setParent(OrdineVenditaRigaPrmForm); 
%>
<select id="<%=OrdineVenditaRigaPrmStatoInvioEDI.getId()%>" name="<%=OrdineVenditaRigaPrmStatoInvioEDI.getName()%>"><% 
  OrdineVenditaRigaPrmStatoInvioEDI.write(out); 
%> 
</select></td>
											</tr>
											<tr>
										<!-- Combo Stato Invio Data Warehouse -->
										<td nowrap>
											<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "StatoInvioDatawarehouse", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="StatoInvioDatawarehouseCombo"><%label.write(out);%></label><%}%>
										</td>
										<td>
											<% 
  WebComboBox OrdineVenditaRigaPrmStatoInvioDatawarehouse =  
     new com.thera.thermfw.web.WebComboBox("OrdineVenditaRigaPrm", "StatoInvioDatawarehouse", null); 
  OrdineVenditaRigaPrmStatoInvioDatawarehouse.setParent(OrdineVenditaRigaPrmForm); 
%>
<select id="<%=OrdineVenditaRigaPrmStatoInvioDatawarehouse.getId()%>" name="<%=OrdineVenditaRigaPrmStatoInvioDatawarehouse.getName()%>"><% 
  OrdineVenditaRigaPrmStatoInvioDatawarehouse.write(out); 
%> 

												
											</select>
										</td>
									</tr>
									<tr>
										<!-- Combo Stato Invio Logistica -->
										<td nowrap>
											<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "StatoInvioLogistica", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="StatoInvioLogisticaCombo"><%label.write(out);%></label><%}%>
										</td>
										<td>
											<% 
  WebComboBox OrdineVenditaRigaPrmStatoInvioLogistica =  
     new com.thera.thermfw.web.WebComboBox("OrdineVenditaRigaPrm", "StatoInvioLogistica", null); 
  OrdineVenditaRigaPrmStatoInvioLogistica.setParent(OrdineVenditaRigaPrmForm); 
%>
<select id="<%=OrdineVenditaRigaPrmStatoInvioLogistica.getId()%>" name="<%=OrdineVenditaRigaPrmStatoInvioLogistica.getName()%>"><% 
  OrdineVenditaRigaPrmStatoInvioLogistica.write(out); 
%> 

												
											</select>
										</td>
									</tr>
									<tr>
										<!-- Combo Stato Invio Contabil. Analitica -->
										<td nowrap>
											<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "OrdineVenditaRigaPrm", "StatoInvioContAnalitica", null); 
   label.setParent(OrdineVenditaRigaPrmForm); 
%><label class="<%=label.getClassType()%>" for="StatoInvioContabAnaliticaCombo"><%label.write(out);%></label><%}%>
										</td>
										<td>
											<% 
  WebComboBox OrdineVenditaRigaPrmStatoInvioContAnalitica =  
     new com.thera.thermfw.web.WebComboBox("OrdineVenditaRigaPrm", "StatoInvioContAnalitica", null); 
  OrdineVenditaRigaPrmStatoInvioContAnalitica.setParent(OrdineVenditaRigaPrmForm); 
%>
<select id="<%=OrdineVenditaRigaPrmStatoInvioContAnalitica.getId()%>" name="<%=OrdineVenditaRigaPrmStatoInvioContAnalitica.getName()%>"><% 
  OrdineVenditaRigaPrmStatoInvioContAnalitica.write(out); 
%> 

												
											</select>
										</td>
									</tr>
								</table>
<!-- Fix 2183 -->
								</td>
								</tr>
<!-- Fine fix 2183 -->
							</table>
						<% MainTabbed.endTab(); %> 
</div>
					</div><% MainTabbed.endTabbed();%> 

     </td>
   </tr>
</table><!--</span>-->
				 </td>
			</tr>

			<!-- **************************************************************************************************** -->
			<!-- ErrorList -->
			<tr>
					<td style="height:0">
						<% 
  WebErrorList errorList = new com.thera.thermfw.web.WebErrorList(); 
  errorList.setParent(OrdineVenditaRigaPrmForm); 
  errorList.write(out); 
%>
<!--<span class="errorlist"></span>-->
					</td>
			</tr>
			<tr>
    			<td style="display:none">
		<!-- **************************************************************************************************** -->
		<!-- Campi nascosti -->
		<table cellpadding="0" cellspacing="0">
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmIdAzienda =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "IdAzienda"); 
  OrdineVenditaRigaPrmIdAzienda.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmIdAzienda.getClassType()%>" id="<%=OrdineVenditaRigaPrmIdAzienda.getId()%>" maxlength="<%=OrdineVenditaRigaPrmIdAzienda.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmIdAzienda.getName()%>" size="<%=OrdineVenditaRigaPrmIdAzienda.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmIdAzienda.write(out); 
%>

				</td>
			</tr>
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmAnnoOrdine =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "AnnoOrdine"); 
  OrdineVenditaRigaPrmAnnoOrdine.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmAnnoOrdine.getClassType()%>" id="<%=OrdineVenditaRigaPrmAnnoOrdine.getId()%>" maxlength="<%=OrdineVenditaRigaPrmAnnoOrdine.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmAnnoOrdine.getName()%>" size="<%=OrdineVenditaRigaPrmAnnoOrdine.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmAnnoOrdine.write(out); 
%>

				</td>
			</tr>
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmNumeroOrdine =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "NumeroOrdine"); 
  OrdineVenditaRigaPrmNumeroOrdine.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmNumeroOrdine.getClassType()%>" id="<%=OrdineVenditaRigaPrmNumeroOrdine.getId()%>" maxlength="<%=OrdineVenditaRigaPrmNumeroOrdine.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmNumeroOrdine.getName()%>" size="<%=OrdineVenditaRigaPrmNumeroOrdine.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmNumeroOrdine.write(out); 
%>

				</td>
			</tr>
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmRigaOrdine =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "RigaOrdine"); 
  OrdineVenditaRigaPrmRigaOrdine.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmRigaOrdine.getClassType()%>" id="<%=OrdineVenditaRigaPrmRigaOrdine.getId()%>" maxlength="<%=OrdineVenditaRigaPrmRigaOrdine.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmRigaOrdine.getName()%>" size="<%=OrdineVenditaRigaPrmRigaOrdine.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmRigaOrdine.write(out); 
%>

				</td>
			</tr>
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmDettaglioRigaOrdine =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "DettaglioRigaOrdine"); 
  OrdineVenditaRigaPrmDettaglioRigaOrdine.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmDettaglioRigaOrdine.getClassType()%>" id="<%=OrdineVenditaRigaPrmDettaglioRigaOrdine.getId()%>" maxlength="<%=OrdineVenditaRigaPrmDettaglioRigaOrdine.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmDettaglioRigaOrdine.getName()%>" size="<%=OrdineVenditaRigaPrmDettaglioRigaOrdine.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmDettaglioRigaOrdine.write(out); 
%>

				</td>
			</tr>
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmTipoRiga =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "TipoRiga"); 
  OrdineVenditaRigaPrmTipoRiga.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmTipoRiga.getClassType()%>" id="<%=OrdineVenditaRigaPrmTipoRiga.getId()%>" maxlength="<%=OrdineVenditaRigaPrmTipoRiga.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmTipoRiga.getName()%>" size="<%=OrdineVenditaRigaPrmTipoRiga.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmTipoRiga.write(out); 
%>

				</td>
			</tr>
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmIdUMPrm =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "IdUMPrm"); 
  OrdineVenditaRigaPrmIdUMPrm.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmIdUMPrm.getClassType()%>" id="<%=OrdineVenditaRigaPrmIdUMPrm.getId()%>" maxlength="<%=OrdineVenditaRigaPrmIdUMPrm.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmIdUMPrm.getName()%>" size="<%=OrdineVenditaRigaPrmIdUMPrm.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmIdUMPrm.write(out); 
%>

				</td>
			</tr>
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmIdUMSec =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "IdUMSec"); 
  OrdineVenditaRigaPrmIdUMSec.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmIdUMSec.getClassType()%>" id="<%=OrdineVenditaRigaPrmIdUMSec.getId()%>" maxlength="<%=OrdineVenditaRigaPrmIdUMSec.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmIdUMSec.getName()%>" size="<%=OrdineVenditaRigaPrmIdUMSec.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmIdUMSec.write(out); 
%>

				</td>
			</tr>
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmIdVersioneSaldi =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "IdVersioneSaldi"); 
  OrdineVenditaRigaPrmIdVersioneSaldi.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmIdVersioneSaldi.getClassType()%>" id="<%=OrdineVenditaRigaPrmIdVersioneSaldi.getId()%>" maxlength="<%=OrdineVenditaRigaPrmIdVersioneSaldi.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmIdVersioneSaldi.getName()%>" size="<%=OrdineVenditaRigaPrmIdVersioneSaldi.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmIdVersioneSaldi.write(out); 
%>

				</td>
			</tr>
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmServizioValutaTestata =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ServizioValutaTestata"); 
  OrdineVenditaRigaPrmServizioValutaTestata.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmServizioValutaTestata.getClassType()%>" id="<%=OrdineVenditaRigaPrmServizioValutaTestata.getId()%>" maxlength="<%=OrdineVenditaRigaPrmServizioValutaTestata.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmServizioValutaTestata.getName()%>" size="<%=OrdineVenditaRigaPrmServizioValutaTestata.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmServizioValutaTestata.write(out); 
%>

				</td>
			</tr>
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmServizioValutaAzienda =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ServizioValutaAzienda"); 
  OrdineVenditaRigaPrmServizioValutaAzienda.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmServizioValutaAzienda.getClassType()%>" id="<%=OrdineVenditaRigaPrmServizioValutaAzienda.getId()%>" maxlength="<%=OrdineVenditaRigaPrmServizioValutaAzienda.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmServizioValutaAzienda.getName()%>" size="<%=OrdineVenditaRigaPrmServizioValutaAzienda.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmServizioValutaAzienda.write(out); 
%>

				</td>
			</tr>
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmServizioCalcDatiVendita =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ServizioCalcDatiVendita"); 
  OrdineVenditaRigaPrmServizioCalcDatiVendita.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmServizioCalcDatiVendita.getClassType()%>" id="<%=OrdineVenditaRigaPrmServizioCalcDatiVendita.getId()%>" maxlength="<%=OrdineVenditaRigaPrmServizioCalcDatiVendita.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmServizioCalcDatiVendita.getName()%>" size="<%=OrdineVenditaRigaPrmServizioCalcDatiVendita.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmServizioCalcDatiVendita.write(out); 
%>

				</td>
			</tr>
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmServizioListVenScaglione =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ServizioListVenScaglione"); 
  OrdineVenditaRigaPrmServizioListVenScaglione.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmServizioListVenScaglione.getClassType()%>" id="<%=OrdineVenditaRigaPrmServizioListVenScaglione.getId()%>" maxlength="<%=OrdineVenditaRigaPrmServizioListVenScaglione.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmServizioListVenScaglione.getName()%>" size="<%=OrdineVenditaRigaPrmServizioListVenScaglione.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmServizioListVenScaglione.write(out); 
%>

				</td>
			</tr>
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmServizioArticoloConfig =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ServizioArticoloConfig"); 
  OrdineVenditaRigaPrmServizioArticoloConfig.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmServizioArticoloConfig.getClassType()%>" id="<%=OrdineVenditaRigaPrmServizioArticoloConfig.getId()%>" maxlength="<%=OrdineVenditaRigaPrmServizioArticoloConfig.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmServizioArticoloConfig.getName()%>" size="<%=OrdineVenditaRigaPrmServizioArticoloConfig.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmServizioArticoloConfig.write(out); 
%>

				</td>
			</tr>
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmPrezzoListino =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "PrezzoListino"); 
  OrdineVenditaRigaPrmPrezzoListino.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmPrezzoListino.getClassType()%>" id="<%=OrdineVenditaRigaPrmPrezzoListino.getId()%>" maxlength="<%=OrdineVenditaRigaPrmPrezzoListino.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmPrezzoListino.getName()%>" size="<%=OrdineVenditaRigaPrmPrezzoListino.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmPrezzoListino.write(out); 
%>

				</td>
			</tr>
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmPrezzoExtraListino =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "PrezzoExtraListino"); 
  OrdineVenditaRigaPrmPrezzoExtraListino.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmPrezzoExtraListino.getClassType()%>" id="<%=OrdineVenditaRigaPrmPrezzoExtraListino.getId()%>" maxlength="<%=OrdineVenditaRigaPrmPrezzoExtraListino.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmPrezzoExtraListino.getName()%>" size="<%=OrdineVenditaRigaPrmPrezzoExtraListino.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmPrezzoExtraListino.write(out); 
%>

				</td>
			</tr>
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmTipoCostoRiferimento =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "TipoCostoRiferimento"); 
  OrdineVenditaRigaPrmTipoCostoRiferimento.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmTipoCostoRiferimento.getClassType()%>" id="<%=OrdineVenditaRigaPrmTipoCostoRiferimento.getId()%>" maxlength="<%=OrdineVenditaRigaPrmTipoCostoRiferimento.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmTipoCostoRiferimento.getName()%>" size="<%=OrdineVenditaRigaPrmTipoCostoRiferimento.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmTipoCostoRiferimento.write(out); 
%>

				</td>
			</tr>
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmServizioIdModalitaPagam =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ServizioIdModalitaPagam"); 
  OrdineVenditaRigaPrmServizioIdModalitaPagam.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmServizioIdModalitaPagam.getClassType()%>" id="<%=OrdineVenditaRigaPrmServizioIdModalitaPagam.getId()%>" maxlength="<%=OrdineVenditaRigaPrmServizioIdModalitaPagam.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmServizioIdModalitaPagam.getName()%>" size="<%=OrdineVenditaRigaPrmServizioIdModalitaPagam.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmServizioIdModalitaPagam.write(out); 
%>

				</td>
			</tr>
			<tr>
				<td style="height:0">
					<label class="thLabel" id="LabelPulsanteSalda" style="display:none">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "SaldaPulsanteLabel", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label>
				</td>
			</tr>
			<tr>
				<td style="height:0">
					<label class="thLabel" id="LabelPulsanteRiapri" style="display:none">
 <% { WebLabelSimple label = new com.thera.thermfw.web.WebLabelSimple("it.thera.thip.vendite.ordineVE.resources.OrdineVenditaRiga", "RiapriPulsanteLabel", null, null, null, null); 
 label.setParent(OrdineVenditaRigaPrmForm); 
label.write(out); }%> 
</label>
				</td>
			</tr>
			<!-- Fix 1918 inizio -->
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmServizioCatalEstConfig =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ServizioCatalEstConfig"); 
  OrdineVenditaRigaPrmServizioCatalEstConfig.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmServizioCatalEstConfig.getClassType()%>" id="<%=OrdineVenditaRigaPrmServizioCatalEstConfig.getId()%>" maxlength="<%=OrdineVenditaRigaPrmServizioCatalEstConfig.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmServizioCatalEstConfig.getName()%>" size="<%=OrdineVenditaRigaPrmServizioCatalEstConfig.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmServizioCatalEstConfig.write(out); 
%>

				</td>
			</tr>
			<!-- Fix 1918 fine -->
			<!-- Fix 2384 inizio -->
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmServizioProvenienzaPrezzo =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ServizioProvenienzaPrezzo"); 
  OrdineVenditaRigaPrmServizioProvenienzaPrezzo.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmServizioProvenienzaPrezzo.getClassType()%>" id="<%=OrdineVenditaRigaPrmServizioProvenienzaPrezzo.getId()%>" maxlength="<%=OrdineVenditaRigaPrmServizioProvenienzaPrezzo.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmServizioProvenienzaPrezzo.getName()%>" size="<%=OrdineVenditaRigaPrmServizioProvenienzaPrezzo.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmServizioProvenienzaPrezzo.write(out); 
%>

				</td>
			</tr>
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmServizioRiferimUMPrezzo =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ServizioRiferimUMPrezzo"); 
  OrdineVenditaRigaPrmServizioRiferimUMPrezzo.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmServizioRiferimUMPrezzo.getClassType()%>" id="<%=OrdineVenditaRigaPrmServizioRiferimUMPrezzo.getId()%>" maxlength="<%=OrdineVenditaRigaPrmServizioRiferimUMPrezzo.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmServizioRiferimUMPrezzo.getName()%>" size="<%=OrdineVenditaRigaPrmServizioRiferimUMPrezzo.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmServizioRiferimUMPrezzo.write(out); 
%>

				</td>
			</tr>
			<!-- Fix 2384 fine -->
			<!-- Fix 2333 -->
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmAPrezziExtra =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "APrezziExtra"); 
  OrdineVenditaRigaPrmAPrezziExtra.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmAPrezziExtra.getClassType()%>" id="<%=OrdineVenditaRigaPrmAPrezziExtra.getId()%>" maxlength="<%=OrdineVenditaRigaPrmAPrezziExtra.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmAPrezziExtra.getName()%>" size="<%=OrdineVenditaRigaPrmAPrezziExtra.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmAPrezziExtra.write(out); 
%>

				</td>
			</tr>
			<!-- Fine Fix 2333 -->
			<!-- Fix 2844 inizio -->
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmIdConfigurazione =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "IdConfigurazione"); 
  OrdineVenditaRigaPrmIdConfigurazione.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmIdConfigurazione.getClassType()%>" id="<%=OrdineVenditaRigaPrmIdConfigurazione.getId()%>" maxlength="<%=OrdineVenditaRigaPrmIdConfigurazione.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmIdConfigurazione.getName()%>" size="<%=OrdineVenditaRigaPrmIdConfigurazione.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmIdConfigurazione.write(out); 
%>

				</td>
			</tr>
			<!-- Fix 2844 fine -->
			<!-- Fix 3197 inizio -->
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmServeRicalProvvAg =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ServeRicalProvvAg"); 
  OrdineVenditaRigaPrmServeRicalProvvAg.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmServeRicalProvvAg.getClassType()%>" id="<%=OrdineVenditaRigaPrmServeRicalProvvAg.getId()%>" maxlength="<%=OrdineVenditaRigaPrmServeRicalProvvAg.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmServeRicalProvvAg.getName()%>" size="<%=OrdineVenditaRigaPrmServeRicalProvvAg.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmServeRicalProvvAg.write(out); 
%>

				</td>
			</tr>
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmServeRicalProvvSubag =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ServeRicalProvvSubag"); 
  OrdineVenditaRigaPrmServeRicalProvvSubag.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmServeRicalProvvSubag.getClassType()%>" id="<%=OrdineVenditaRigaPrmServeRicalProvvSubag.getId()%>" maxlength="<%=OrdineVenditaRigaPrmServeRicalProvvSubag.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmServeRicalProvvSubag.getName()%>" size="<%=OrdineVenditaRigaPrmServeRicalProvvSubag.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmServeRicalProvvSubag.write(out); 
%>

				</td>
			</tr>
			<!-- Fix 3197 fine -->
			<!-- Fix 3016 -->
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmConCantiere =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ConCantiere"); 
  OrdineVenditaRigaPrmConCantiere.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmConCantiere.getClassType()%>" id="<%=OrdineVenditaRigaPrmConCantiere.getId()%>" maxlength="<%=OrdineVenditaRigaPrmConCantiere.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmConCantiere.getName()%>" size="<%=OrdineVenditaRigaPrmConCantiere.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmConCantiere.write(out); 
%>

				</td>
			</tr>
			<!-- Fine fix 3016 -->
			<!-- Fix 3770 inizio -->
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmIdCatalogoEsterno =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "IdCatalogoEsterno"); 
  OrdineVenditaRigaPrmIdCatalogoEsterno.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmIdCatalogoEsterno.getClassType()%>" id="<%=OrdineVenditaRigaPrmIdCatalogoEsterno.getId()%>" maxlength="<%=OrdineVenditaRigaPrmIdCatalogoEsterno.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmIdCatalogoEsterno.getName()%>" size="<%=OrdineVenditaRigaPrmIdCatalogoEsterno.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmIdCatalogoEsterno.write(out); 
%>

				</td>
			</tr>
			<!-- Fix 3770 fine -->
			<!-- Fix 3910 inizio -->
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmQtaMinListino =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "QtaMinListino"); 
  OrdineVenditaRigaPrmQtaMinListino.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmQtaMinListino.getClassType()%>" id="<%=OrdineVenditaRigaPrmQtaMinListino.getId()%>" maxlength="<%=OrdineVenditaRigaPrmQtaMinListino.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmQtaMinListino.getName()%>" size="<%=OrdineVenditaRigaPrmQtaMinListino.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmQtaMinListino.write(out); 
%>

				</td>
			</tr>
			<!-- Fix 3910 fine -->
						<!-- Inizio mmm -->
			<tr>
				<td style="height:0">
					<input id="AzioneManuale" name="AzioneManuale" type="hidden">
				</td>
			</tr>
			<!-- Fine mmm -->
			<!-- Fix 7024 inizio -->
			<tr>
				<td style="height:0">
					<% 
  WebTextInput OrdineVenditaRigaPrmSpecializzazioneRiga =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "SpecializzazioneRiga"); 
  OrdineVenditaRigaPrmSpecializzazioneRiga.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmSpecializzazioneRiga.getClassType()%>" id="<%=OrdineVenditaRigaPrmSpecializzazioneRiga.getId()%>" maxlength="<%=OrdineVenditaRigaPrmSpecializzazioneRiga.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmSpecializzazioneRiga.getName()%>" size="<%=OrdineVenditaRigaPrmSpecializzazioneRiga.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmSpecializzazioneRiga.write(out); 
%>

				</td>
			</tr>
			<!-- Fix 7024 fine -->
			<!-- Fix 10173 inizio -->
			<tr>
				<td style="height:0">
					<input id="ForzaCambioCausale" name="ForzaCambioCausale" type="hidden">
				</td>
			</tr>
			<!-- Fix 10173 fine -->
      <!--Fix 23345 inizio-->
      <tr>
        <td>
          <% 
  WebTextInput OrdineVenditaRigaPrmControlloRicalCondiz =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ControlloRicalCondiz"); 
  OrdineVenditaRigaPrmControlloRicalCondiz.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmControlloRicalCondiz.getClassType()%>" id="<%=OrdineVenditaRigaPrmControlloRicalCondiz.getId()%>" maxlength="<%=OrdineVenditaRigaPrmControlloRicalCondiz.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmControlloRicalCondiz.getName()%>" size="<%=OrdineVenditaRigaPrmControlloRicalCondiz.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmControlloRicalCondiz.write(out); 
%>

        </td>
      </tr>
      <!--Fix 23345 fine-->
      <!--Fix 24613 inizio-->
      <tr>
        <td>
          <% 
  WebTextInput OrdineVenditaRigaPrmControlloRicalVlrDettCfg =  
     new com.thera.thermfw.web.WebTextInput("OrdineVenditaRigaPrm", "ControlloRicalVlrDettCfg"); 
  OrdineVenditaRigaPrmControlloRicalVlrDettCfg.setParent(OrdineVenditaRigaPrmForm); 
%>
<input class="<%=OrdineVenditaRigaPrmControlloRicalVlrDettCfg.getClassType()%>" id="<%=OrdineVenditaRigaPrmControlloRicalVlrDettCfg.getId()%>" maxlength="<%=OrdineVenditaRigaPrmControlloRicalVlrDettCfg.getMaxLength()%>" name="<%=OrdineVenditaRigaPrmControlloRicalVlrDettCfg.getName()%>" size="<%=OrdineVenditaRigaPrmControlloRicalVlrDettCfg.getSize()%>" type="hidden"><% 
  OrdineVenditaRigaPrmControlloRicalVlrDettCfg.write(out); 
%>

        </td>
      </tr>
      <!--Fix 24613 fine-->
		</table>

				</td>
			</tr>
		</table>


	<%
  OrdineVenditaRigaPrmForm.writeFormEndElements(out); 
%>
</form></td>
</tr>

<tr>
<td style="height:0">
<% String ftr = OrdineVenditaRigaPrmForm.getCompleteFooter();
 if (ftr != null) { 
   request.setAttribute("dataCollector", OrdineVenditaRigaPrmBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= ftr %>" flush="true"/> 
<% } %> 
</td>
</tr>
</table>



<%
           // blocco YYY  
           // a completamento blocco di codice XXX in head 
              OrdineVenditaRigaPrmForm.writeBodyEndElements(out); 
           } 
           else 
              errors.addAll(0, OrdineVenditaRigaPrmBODC.getErrorList().getErrors()); 
        } 
        else 
           errors.addAll(0, OrdineVenditaRigaPrmBODC.getErrorList().getErrors()); 
           if(OrdineVenditaRigaPrmBODC.getConflict() != null) 
                conflitPresent = true; 
     } 
     else 
        errors.add(new ErrorMessage("BAS0000010")); 
  } 
  catch(NamingException e) { 
     errorMessage = e.getMessage(); 
     errors.add(new ErrorMessage("CBS000025", errorMessage));  } 
  catch(SQLException e) {
     errorMessage = e.getMessage(); 
     errors.add(new ErrorMessage("BAS0000071", errorMessage));  } 
  catch(Throwable e) {
     e.printStackTrace(Trace.excStream);
  }
  finally 
  {
     if(OrdineVenditaRigaPrmBODC != null && !OrdineVenditaRigaPrmBODC.close(false)) 
        errors.addAll(0, OrdineVenditaRigaPrmBODC.getErrorList().getErrors()); 
     try 
     { 
        se.end(); 
     }
     catch(IllegalArgumentException e) { 
        e.printStackTrace(Trace.excStream); 
     } 
     catch(SQLException e) { 
        e.printStackTrace(Trace.excStream); 
     } 
  } 
  if(!errors.isEmpty())
  { 
      if(!conflitPresent)
  { 
     request.setAttribute("ErrorMessages", errors); 
     String errorPage = OrdineVenditaRigaPrmForm.getErrorPage(); 
%> 
     <jsp:include page="<%=errorPage%>" flush="true"/> 
<% 
  } 
  else 
  { 
     request.setAttribute("ConflictMessages", OrdineVenditaRigaPrmBODC.getConflict()); 
     request.setAttribute("ErrorMessages", errors); 
     String conflictPage = OrdineVenditaRigaPrmForm.getConflictPage(); 
%> 
     <jsp:include page="<%=conflictPage%>" flush="true"/> 
<% 
   } 
   } 
%> 
</body>
</html>
