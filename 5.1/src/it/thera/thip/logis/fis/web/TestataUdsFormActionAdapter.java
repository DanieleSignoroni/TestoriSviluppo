package it.thera.thip.logis.fis.web;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: THERA</p>
 * @author not attributable
 * @version 1.1
 *
 * 15941  07.03.12    IE - LOGIS NLS
 * 31021  02.04.20    GM - gestione 6 decimali
 */

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.thera.thermfw.ad.*;
import com.thera.thermfw.base.*;
import com.thera.thermfw.collector.*;
import com.thera.thermfw.common.*;
import com.thera.thermfw.gui.cnr.*;
import com.thera.thermfw.persist.*;
import com.thera.thermfw.type.*;
import com.thera.thermfw.web.*;
import com.thera.thermfw.web.servlet.*;
import it.thera.thip.logis.fis.*;

/*
 * Revisions:
 * Fix      Date          Owner      Description
 * 41044	29/01/2024    BN     	 Aggiungi il pulsante "calcola" "ChiudiUds" "ApriUds".
 * 50548	01/09/2026	  AM		 Modificare il calcolo del volume eseguito con l'apposito bottone nella gui
 */
public class TestataUdsFormActionAdapter extends FormActionAdapter{

  protected static final String ACT_TYPE_BACK = "ACT_UDSTYPE_BACK";
//Fix 41044 INIZIO
  protected static final String RES = "it.thera.thip.logis.fis.resources.LogisFis";
  public static final String CALCOLA_PESOLORDO_BT = "CALCOLA_PESOLORDO_BT"; 
  public static final String OPEN_UDS = "THOPENUDS";
  public static final String CLOSE_UDS = "THCLOSEUDS";
//Fix 41044 FINE

  public static final String CALCOLA_VOLUME_BT = "CALCOLA_VOLUME_BT"; //Fix 50548 
  
  protected void otherActions(ClassADCollection	cadc, ServletEnvironment se) throws ServletException, IOException
  {
    String action = se.getRequest().getParameter(ACTION).toUpperCase();
    if(action.equals(ACT_TYPE_BACK))
      tipoUdsSearchBack(cadc,se);
    //Fix 41044 inizio
	if (action.equals(CALCOLA_PESOLORDO_BT))
		try {
			calcolaPesoLordo(cadc, se);
		} catch (Exception e) {
			e.printStackTrace();
		}
	else if(action.equals(OPEN_UDS) || action.equals(CLOSE_UDS)) {
        se.sendRequest(getServletContext(), se.getServletPath() + "it.thera.thip.logis.fis.web.TestataUdsServlet", true);
	}
	//Fix 50548 inizio
	else if (action.equals(CALCOLA_VOLUME_BT)) 
	{ try {
			 calcolaVolumeIngombroENetto(cadc, se);
		   } catch (Exception e){
			e.printStackTrace(Trace.excStream);
		   }
	}
	//Fix 50548 fine
	//Fix 41044 fine
  }


protected void setValues(ClassADCollection cadc, BODataCollector boDC, ServletEnvironment se)
  {
    HttpServletRequest req = se.getRequest();
    String mode = req.getParameter(MODE);
    String key = req.getParameter(KEY);
    int rc = BODataCollector.OK;
    if (!mode.equals("NEW")) {
      BusinessObject bo = boDC.getBo();
      if (mode.equals("COPY"))
        boDC.setMode(OpenType.COPY);
      rc = boDC.retrieve(key);
    }
    if (rc == BODataCollector.OK) {
      Set objectAttributeNames = getObjectAttributeNames(req.getParameterNames());
      Iterator it = objectAttributeNames.iterator();
      while (it.hasNext()) {
        String parameter = (String) it.next();
        updateDataCollector(boDC, mode, parameter, req.getParameter(parameter));
      }

      Enumeration e = boDC.getComponents();
      while (e.hasMoreElements()) {
        BaseBOComponentManager man = ((BaseBOComponent)e.nextElement()).getComponentManager();
        ClassAD cad = man.getClassAD();
        if ( (cad.getType() instanceof BooleanType) && (!man.isReadOnly())) { //Mod. 552
          String attributeName = man.getKeyForBaseComponentsCollection();
          if (!objectAttributeNames.contains(attributeName) &&
              (mode.equals("COPY") || mode.equals("NEW") ||
               (mode.equals("UPDATE") && cad.getKeySequence() == 0)))
            boDC.set(attributeName, String.valueOf(Column.FALSE_CHAR));
        }
      }
    }
    se.addErrorMessages(boDC.getErrorList().getErrors());
    }

    protected Set getObjectAttributeNames(Enumeration e)
    {
      Set set = new HashSet();
      while (e.hasMoreElements()) {
        String p = (String) e.nextElement();
        set.add(p);
      }
      return set;
    }

    protected void updateDataCollector(BODataCollector dataCollector, String mode, String attName, String attValue)
    {
      BaseBOComponentManager cm = dataCollector.getComponentManager(attName);
      if (cm != null && !cm.isDenied() && (cm.getClassAD().getKeySequence() == 0 || mode.equals("NEW") || mode.equals("COPY")))
      {
        Type t = cm.getType();
        if (!(t instanceof EnumerationType))
          attValue = t.unFormat(attValue);
        dataCollector.set(attName, attValue);
        if(attName.equals("Codice"))
          ((TestataUds)dataCollector.getBo()).setCodice(attValue);
        else if(attName.equals("TipoUds"))
          ((TestataUds)dataCollector.getBo()).setChiaveTipoUds(attValue);
      }
    }

    BODataCollector boDC = null;
    protected BODataCollector getBODC()
    {
      return boDC;
    }



    public void tipoUdsSearchBack(ClassADCollection	cadc, ServletEnvironment se) throws ServletException, IOException
    {
      if(boDC == null)
        {
          String className = se.getRequest().getParameter(CLASS_NAME);
          String collectorName = se.getRequest().getParameter(COLLECTOR_NAME);
          boDC = (BODataCollector) Factory.createObject(collectorName);
          if (boDC instanceof WebDataCollector)
            ( (WebDataCollector) boDC).setServletEnvironment(se);

          boDC.initialize(className, true, PersistentObject.NO_LOCK);
        }
      setValues(cadc, boDC, se);
      TestataUds testata = (TestataUds)getBODC().getBo();
      try {
        if(testata.getTipoUds().getTipoCodice() != null && testata.getTipoUds().getTipoCodice().getTipoNumeratore() != null)
           se.getRequest().setAttribute("codice",testata.getTipoUds().getTipoCodice().dammiProssimoCodice());
      }
      catch (Exception ex) {
        ex.printStackTrace(Trace.excStream);//Fix 15941
      }

       se.getRequest().setAttribute("forma",testata.getTipoUds().getChiaveForma());
       if(testata.getTipoUds().getForma() != null)
         se.getRequest().setAttribute("descForma",testata.getTipoUds().getForma().getDescrizioneBr());
       //02.04.20 - inizio
       /*se.getRequest().setAttribute("volIngombro",testata.getTipoUds().getDim().getVolumeIngombro().setScale(3));
       se.getRequest().setAttribute("volNetto",testata.getTipoUds().getDim().getVolumeNetto().setScale(3));
       se.getRequest().setAttribute("larghezza",testata.getTipoUds().getDim().getLarghezza().setScale(3));
       se.getRequest().setAttribute("lunghezza",testata.getTipoUds().getDim().getLunghezza().setScale(3));
       se.getRequest().setAttribute("profondita",testata.getTipoUds().getDim().getProfondita().setScale(3));
       se.getRequest().setAttribute("pesoMax",testata.getTipoUds().getDim().getPesoMax().setScale(3));
       se.getRequest().setAttribute("tara",testata.getTipoUds().getTara().setScale(3));
       se.getRequest().setAttribute("pesoNetto",(testata.getTipoUds().getDim().getPesoMax().subtract(testata.getTipoUds().getTara())).setScale(3));*/
       BigDecimal zero = new BigDecimal(0);//50548
       if(testata.getTipoUds().getDim().getVolumeIngombro()== null){
    	 se.getRequest().setAttribute("volIngombro",zero);//50548
       }
       else
       {
    	   se.getRequest().setAttribute("volIngombro",Q6Calc.get().setScale(testata.getTipoUds().getDim().getVolumeIngombro(),3));
       }
       
       if(testata.getTipoUds().getDim().getVolumeNetto()== null){
      	 se.getRequest().setAttribute("volNetto",zero);//50548
         }
         else
         {
      	   se.getRequest().setAttribute("volNetto",Q6Calc.get().setScale(testata.getTipoUds().getDim().getVolumeNetto(),3));
         }
       se.getRequest().setAttribute("larghezza",Q6Calc.get().setScale(testata.getTipoUds().getDim().getLarghezza(),3));
       se.getRequest().setAttribute("lunghezza",Q6Calc.get().setScale(testata.getTipoUds().getDim().getLunghezza(),3));
       se.getRequest().setAttribute("profondita",Q6Calc.get().setScale(testata.getTipoUds().getDim().getProfondita(),3));
       se.getRequest().setAttribute("pesoMax",Q6Calc.get().setScale(testata.getTipoUds().getDim().getPesoMax(),3));
       se.getRequest().setAttribute("tara",Q6Calc.get().setScale(testata.getTipoUds().getTara(),3));
       //50548 inizio
       se.getRequest().setAttribute("pesoLordo",Q6Calc.get().setScale(testata.getPesoLordo(),3));
       se.getRequest().setAttribute("pesoNetto",Q6Calc.get().setScale(testata.getPesoNetto(),3));
      //50548se.getRequest().setAttribute("pesoNetto",Q6Calc.get().setScale((testata.getTipoUds().getDim().getPesoMax().subtract(testata.getTipoUds().getTara())),3));
     //50548 Fine
       //02.04.20 - fine
      se.sendRequest(getServletContext(), "/it/thera/thip/logis/fis/InitLottiFields.jsp", false);
    }

    //Fix 41044 inizio
    // correspend a TestataUdsFrm.calcolaPesoLordoBt_actionPerformed
	void calcolaPesoLordo(ClassADCollection cadc, ServletEnvironment se)throws ServletException, IOException, SQLException {
		//50548TestataUds tu;
		//41044 Inizio
		/*
		String docKey = BaseServlet.getStringParameter(se.getRequest(), "thKey");// get key
		tu = TestataUds.elementWithKey(docKey, PersistentObject.NO_LOCK);
		if (tu.getStatoAllestimento() == tu.NUOVO)
			tu.setPesoLordo(tu.getPesoLordoPrevistoCalcolato());
		else {
			String val = tu.getPesoLordoCalcolato().toString();
			tu.setPesoLordo(tu.getPesoLordoCalcolato());
			PrintWriter out = se.getResponse().getWriter();
			out.println("<script language='JavaScript1.2'>");
			out.println("parent.setAttValue('pesoMax', parseFloat('" + val + "').toFixed(6));");
			out.println("</script>");
			out.flush();
		}
		*/
		//Fix 50548 inizio
		String mode = BaseServlet.getStringParameter(se.getRequest(),MODE);
		TestataUds tu = null ;
		PrintWriter out = se.getResponse().getWriter();
	   	 if(mode != null && mode.equals("NEW")){
	   		 tu = (TestataUds)Factory.createObject(cadc.getBOClassName());
	   	 }
		 else if(mode != null && !mode.equals("NEW"))
		   { String testataUdsKey = BaseServlet.getStringParameter(se.getRequest(), "thKey");// get key
		     if(testataUdsKey != null && !testataUdsKey.equals("")) {
		    	 tu = TestataUds.elementWithKey(testataUdsKey, PersistentObject.NO_LOCK);
		     }
		   }
				// PrintWriter out = se.getResponse().getWriter();
		out.println("<script language='JavaScript1.2'>");
		  if (tu != null){//(docKey != null) {
			//tu = TestataUds.elementWithKey(docKey, PersistentObject.NO_LOCK);
			setTestataFromInformazioneDaGui(se,cadc,tu);
			if (tu.getPesoLordo()== null) {
				if (tu.getStatoAllestimento() == tu.NUOVO)
					tu.setPesoLordo(tu.getPesoLordoPrevistoCalcolato());
				else {
					//50548String val = tu.getPesoLordoCalcolato().toString();
					tu.setPesoLordo(tu.getPesoLordoCalcolato());
					/*50548out.println("parent.setAttValue('pesoMax', parseFloat('" + val + "').toFixed(6));");
					out.flush();*/
				}
			 	out.println("parent.setAttValue('pesoLordo', parseFloat('" + tu.getPesoLordo() + "').toFixed(6));");
					out.flush();
				}
			else
			{
				//72XXX Softre <
				BigDecimal pesoNetto = tu.getPesoLordo().subtract(tu.getTara());
				out.println("parent.setAttValue('pesoNetto', parseFloat('" + pesoNetto + "').toFixed(6));");
				//72XXX Softre >
				out.println("parent.clearMessage();");
			}
		}
		out.println("parent.enableFormActions();");
		out.println("</script>");
		//41044 Fine
	}
		
    public void modifyToolBar(WebToolBar toolBar)
	{
    	super.modifyToolBar(toolBar);
		WebToolBarButton openUds = 
		new WebToolBarButton("OpenUds", "action_submit", "errorsFrame", "no", RES, "ApriUds", "it/thera/thip/logis/fis/images/ApriUds.gif", OPEN_UDS, "single", false);
		WebToolBarButton closeUds = new WebToolBarButton("CloseUds", "action_submit", "errorsFrame", "no", RES, "ChiudiUds", "it/thera/thip/logis/fis/images/ChiudiUds.gif", CLOSE_UDS, "single", false);
		toolBar.addSeparator();
		toolBar.addButton("Copy",openUds);
		toolBar.addButton("Copy",closeUds);
	}
	  //Fix 41044 fine
    
    //Fix 50548 inizio
    // correspend a TestataUdsFrm.calcolaVolumeBt_actionPerformed
    protected void calcolaVolumeIngombroENetto(ClassADCollection cadc, ServletEnvironment se)throws ServletException, IOException, SQLException {
    	 String mode = BaseServlet.getStringParameter(se.getRequest(),MODE);
    	 PrintWriter out = se.getResponse().getWriter();
    	 TestataUds testata = null ;
    	 
    	 if(mode != null && mode.equals("NEW")){
    		 testata = (TestataUds)Factory.createObject(cadc.getBOClassName());
    	 }
    	 else if(mode != null && !mode.equals("NEW"))
    	 {   String testataUdsKey = BaseServlet.getStringParameter(se.getRequest(), "thKey");// get key
    		 if(testataUdsKey != null && !testataUdsKey.equals("")) 
    	 		{
    			 testata = TestataUds.elementWithKey(testataUdsKey, PersistentObject.NO_LOCK);
    	 		}
    	 }
    	    out.println("<script language='JavaScript1.2'>");
    	    if(testata != null) 
    	    {
    	    	setTestataFromInformazioneDaGui(se,cadc,testata);
    	    	Dimensioni dim = testata.getDim();
	 			if (dim.getLunghezza() != null && dim.getLarghezza() != null && dim.getProfondita() != null) 
	 			{   BigDecimal volume = dim.getLunghezza().multiply(dim.getLarghezza().multiply(dim.getProfondita()));
	 				dim.setVolumeIngombro(testata.conversioneVolume(volume));
	        	
	 				BigDecimal volNetto = testata.getTipoUds().getDim().getVolumeNetto();
		        	if (volNetto != null) {
		        		testata.getDim().setVolumeNetto(volNetto);}
		        	else
		        		{testata.getDim().setVolumeNetto(testata.getDim().getVolumeIngombro());}
		        	
		        	out.println("parent.setAttValue('volIngombro',parseFloat('" + testata.getDim().getVolumeIngombro() + "').toFixed(6));");
					out.println("parent.setAttValue('volNetto',parseFloat('" + testata.getDim().getVolumeNetto() + "').toFixed(6));");
					out.flush();
	        	}
	 			else {
	 				out.println("parent.clearMessage();");
	 			}
		}
		out.println("parent.enableFormActions();");
		out.println("</script>");
		
    }
    
    protected void  setTestataFromInformazioneDaGui(ServletEnvironment se ,ClassADCollection cadc, TestataUds tes) throws SQLException {
    	String mode = BaseServlet.getStringParameter(se.getRequest(),MODE);
    	
    	String lunghezza = BaseServlet.getStringParameter(se.getRequest(), "Dimensioni.Lunghezza");
    	String larghezza = BaseServlet.getStringParameter(se.getRequest(), "Dimensioni.Larghezza");
    	String profondita = BaseServlet.getStringParameter(se.getRequest(), "Dimensioni.Profondita");
    	
    	String pesoMax = BaseServlet.getStringParameter(se.getRequest(), "Dimensioni.PesoMax");
    	String pesoNetto = BaseServlet.getStringParameter(se.getRequest(), "PesoNetto");
    	String pesoLordo = BaseServlet.getStringParameter(se.getRequest(), "PesoLordo");
    	String tara = BaseServlet.getStringParameter(se.getRequest(), "Tara");
    	
   	 	if(mode != null && mode.equals("NEW")){
   	 		String codiceTipoUds = BaseServlet.getStringParameter(se.getRequest(), "TipoUds");
   	 		if(codiceTipoUds != null && !codiceTipoUds.isEmpty()) {
   	 			TipoUds tipoUds = (TipoUds) TipoUds.readOnlyElementWithKey(TipoUds.class, codiceTipoUds);
   	 			tes.setTipoUds(tipoUds);
   	 		}
   	 	}
    	
    	if(lunghezza != null && !lunghezza.isEmpty())
        {
          BigDecimal valore = getValore(cadc,"Dimensioni.Lunghezza",lunghezza);
          tes.getDim().setLunghezza(valore);
        }
    	if(larghezza != null && !larghezza.isEmpty())
        {
    		BigDecimal valore = getValore(cadc,"Dimensioni.Larghezza",larghezza);
 	        tes.getDim().setLarghezza(valore);
        }
    	if(profondita != null && !profondita.isEmpty())
        {
    		BigDecimal valore = getValore(cadc,"Dimensioni.Profondita",profondita);
    	    tes.getDim().setProfondita(valore);     
        }
    	
    	if(pesoMax != null && !pesoMax.isEmpty())
        {
          BigDecimal valore = getValore(cadc,"Dimensioni.PesoMax",pesoMax);
          tes.getDim().setPesoMax(valore);
        }
    	if(pesoNetto != null && !pesoNetto.isEmpty())
        {
    		BigDecimal valore = getValore(cadc,"PesoNetto",pesoNetto);
 	        tes.setPesoNetto(valore);
        }
    	if(pesoLordo != null && !pesoLordo.isEmpty())
        {
    		BigDecimal valore = getValore(cadc,"PesoLordo",pesoLordo);
    	    tes.setPesoLordo(valore);     
        }else
        {
        	tes.setPesoLordo(null);
        }
    	if(tara != null && !tara.isEmpty())
        {
    		BigDecimal valore = getValore(cadc,"Tara",tara);
    	    tes.setTara(valore);     
        }
    	else
    	{
    		 tes.setTara(null);	
    	}
    }
    
	protected BigDecimal getValore(ClassADCollection cadc, String attributeName, String valoreStr) {
		BigDecimal valore = null;
		try {
			ClassAD cad = cadc.getAttribute(attributeName);
			DecimalType perc = (DecimalType) cad.getType();
			perc.setCorrespondingJavaClass(BigDecimal.class);
			valore = (java.math.BigDecimal) perc.stringToObject(valoreStr);
		} catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
		return valore;
	}
    //Fix 50548 fine
}

