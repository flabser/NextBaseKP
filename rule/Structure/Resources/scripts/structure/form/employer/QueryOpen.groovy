package structure.form.employer

import kz.flabs.runtimeobj.document.structure.UserRole
import kz.nextbase.script._Document
import kz.nextbase.script._Helper
import kz.nextbase.script._Session
import kz.nextbase.script._WebFormData
import kz.nextbase.script.actions._Action
import kz.nextbase.script.actions._ActionType
import kz.nextbase.script.constants._DocumentType
import kz.nextbase.script.events._FormQueryOpen
import kz.nextbase.script.struct._Employer
import kz.nextbase.script.struct._EmployerStatusType
import kz.nextbase.script.struct._UserRole
import kz.pchelka.env.Environment

class QueryOpen extends _FormQueryOpen {


	@Override
	public void doQueryOpen(_Session ses, _WebFormData webFormData, String lang) {
		def db = ses.getCurrentDatabase()
		
		
		def pdoc = db.getDocumentByComplexID(webFormData.getValue("parentdoctype"),webFormData.getValue("parentdocid"))
		if (pdoc != null){
			if (pdoc.getDocumentType() == _DocumentType.ORGANIZATION){

				publishValue("organization", pdoc.getViewText())
			}else{
				publishValue("parent", pdoc.getViewText())
				def org = pdoc.getGrandParentDocument()
				publishValue("organization", org.getViewText())
			}			
		}	
		
		def user = ses.getCurrentAppUser()
		def actionBar = ses.createActionBar();
		
		if (user.hasRole(["struct_keeper","supervisor"])){
			actionBar.addAction(new _Action(getLocalizedWord("Сохранить и закрыть",lang),getLocalizedWord("Сохранить и закрыть",lang),_ActionType.SAVE_AND_CLOSE))
		}
		
		actionBar.addAction(new _Action(getLocalizedWord("Закрыть",lang),getLocalizedWord("Закрыть без сохранения",lang),_ActionType.CLOSE))

        def def_roles = Environment.applications.globalSetting.roleCollection.rolesList*.findResults{
            it.active == true ? new _UserRole(new UserRole(it)) : null
        }.collectMany {it}
        publishValue("role", def_roles)
		publishElement(actionBar)
	}

	@Override
	public void doQueryOpen(_Session ses, _Document doc, _WebFormData webFormData, String lang) {
		def user = ses.getCurrentAppUser()

		def emp  = (_Employer)doc;
		publishValue("title",getLocalizedWord("Сотрудник", lang) + " " + emp.getFullName())
		publishValue("fullname", emp.getFullName())
		publishValue("shortname", emp.getShortName())
		publishValue("userid", emp.getUserID())
		publishValue("email", emp.getEmail())
		publishValue("instmsgaddress", emp.getInstMessengerAddr())
		def org = emp.getOrganization();
		publishValue("organization", org.getShortName())
		def dep = emp.getMainDepartment();
		publishValue("depid", dep.getValueString("shortName"))
		publishValue("role", emp.getListOfRoles())
		if(user.hasRole("supervisor")){
			publishValue("issupervisor", "1")
		}
		publishValue("rank", emp.getRank())
		publishValue("phone", emp.getPhone())
		publishValue("sendto", emp.getSendto())
		publishValue("comment", emp.getComment())
		publishValue("group", emp.getListOfGroups())
		publishValue("replacer", emp.getFullName())
		publishValue("index", emp.getFullName())
        def apps = new HashSet();
        for(def userprof : emp.getEnabledApps()){
            apps.add(userprof.appName);
        }
		publishValue("apps", apps)
		publishValue("post", emp.getPostID(), emp.getPost())  
		//publishValue("birthdate", _Helper.getDateAsStringShort(emp.getBirthDate()))
		def birthDate = emp.getBirthDate();
		if (birthDate) {
			def bd = _Helper.getDateAsStringShort(birthDate)
			if (bd) publishValue("birthdate", bd)
		}
		
		if (emp.getStatus() == _EmployerStatusType.FIRED){
			publishValue("fired", "1")
		}
		
		def actionBar = ses.createActionBar();
        if (user.hasRole(["supervisor", "struct_keeper"]) ){
			actionBar.addAction(new _Action(getLocalizedWord("Сохранить и закрыть",lang),getLocalizedWord("Сохранить и закрыть",lang),_ActionType.SAVE_AND_CLOSE))
			actionBar.addAction(new _Action("Новый сотрудник","Новый сотрудник", "NEW_EMPLOYER"))
			actionBar.addAction(new _Action("Новый департамент","Новый департамент", "NEW_DEPARTMENT"))
		}
		
		actionBar.addAction(new _Action(getLocalizedWord("Закрыть",lang),getLocalizedWord("Закрыть без сохранения",lang),_ActionType.CLOSE))
		publishElement(actionBar)
	}

	
	
}