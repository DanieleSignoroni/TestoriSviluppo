var oldmyControllaIdLotto = controllaIdLotto;

controllaIdLotto = function() {
	oldmyControllaIdLotto();
	if (window.XMLHttpRequest) {
		ajaxRequest = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}

	if (ajaxRequest != null) {
		//Ricalcolo quantitÃ  e unitÃ  di misura
		var ricalcoloQuantita = false;
		var siglaUMDest = new Array();
		var flagRigaLotto = new Array();
		var quantVen = eval("document.forms[0]." + idFromName["QtaInUMVen"]).value;
		var quantPrm = getCampoQtaUMPrm().value;
		var quantSec = getCampoQtaUMSec().value;
		var idUMPrmMag = getCampoUMPrm().value;
		var idUMVen = eval("document.forms[0]." + idFromName["IdUMRif"]).value;
		var idLotto = document.getElementById(idFromName["Lotti.IdLotto"]).value;
		var keyOrdVenRig = document.forms[0].thKey.value;
		var url = "/" + parent.webAppPath + "/" + parent.servletPath + "/it.testori.thip.vendite.ordineVE.web.YRecuperaQuantitaLotto";
		var params = "?idLotto=" + URLEncode(idLotto)
		params += "&thKey=" + URLEncode(keyOrdVenRig);
		params += "&SiglaUMDestinazione=" + siglaUMDest;
		params += "&SiglaUMOrigine=V";
		params += "&IdUMDestinazione=" + idUMVen;
		params += "&IdUMOrigine=" + idUMPrmMag;
		params += "&FlagRigaLotto=" + flagRigaLotto;
		params += "&SiglaUMDestinazione=" + siglaUMDest;
		params += "&SiglaUMDestinazione=" + siglaUMDest;

		params += "&IdUMRif=" + idUMVen;
		params += "&IdUMPrm=" + getCampoUMPrm().value;
		params += "&IdUMSec=" + getCampoUMSec().value;

		params += "&IdArticolo=" + eval("document.forms[0]." + idFromName["IdArticolo"]).value;
		params += "&IdVersione=" + eval("document.forms[0]." + idFromName["IdVersioneRichiesta"]).value;

		params += "&qtaUMVen=" + quantVen;
		params += "&qtaUMPrm=" + quantPrm;
		params += "&qtaUMSec=" + quantSec;

		params += "&RicalcoloQta=" + ricalcoloQuantita;
		ajaxRequest.open("GET", url + params, false);
		ajaxRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		ajaxRequest.onreadystatechange = function() {
			if (ajaxRequest.readyState == 4) {
				var json = eval("(" + ajaxRequest.responseText + ")");
				var qtaInUmVen = json.YQtaInUmVen;
				var qtaInUmPrmMag = json.YQtaInUmPrmMag;
				document.getElementById(idFromName["Lotti.QtaInUMVen"]).value = qtaInUmVen;
				document.getElementById(idFromName["Lotti.QtaInUMPrmMag"]).value = qtaInUmPrmMag;
				document.getElementById(idFromName["Lotti.QuantitaOrdinata.QuantitaInUMRif"]).value = qtaInUmVen;
				document.getElementById(idFromName["Lotti.QuantitaOrdinata.QuantitaInUMPrm"]).value = qtaInUmPrmMag;
				var msg = json.Msg;
				if (msg != "") {
					window.alert(msg);
				}
			}
		}
		ajaxRequest.send(null);
	}
}

//72247
function generaDocumenti() {
	var key = document.forms[0].thKey.value;
	var url = "/" + webAppPath + "/" + servletPath + "/it.testori.thip.vendite.ordineVE.web.GeneraDocumentiOrdineVenditaRigaPrm?";
	url = url + "thKey=" + key;
	setLocationOnWindow(document.getElementById(errorsFrameName).contentWindow, url);
}
//72247

function selezionaLottiMultipliTestori() {
	var className = eval("document.forms[0].thClassName.value");
	var key = eval("document.forms[0].thKey.value");
	runActionDirect('SEL_MULTIPLA_LOTTI_TESTORI', 'action_submit', className, key, 'new', 'no');
}