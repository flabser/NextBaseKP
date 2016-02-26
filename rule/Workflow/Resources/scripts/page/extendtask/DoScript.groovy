package page.extendtask
import kz.nextbase.script.*
import kz.nextbase.script.events._DoScript
import kz.nextbase.script.task._Control
import kz.nextbase.script.task._ExecsBlocks

class DoScript extends _DoScript {

	@Override
	public void doProcess(_Session session, _WebFormData formData, String lang) {
		println(formData)

		def deletedList = []
		def unDeletedList = []

		def db = session.getCurrentDatabase()
		def task = db.getDocumentByID(formData.getNumberValueSilently("docid",0));
		def taskdesc = task.getDescendants();
		taskdesc.push(task);
		taskdesc.each {
			if(it.getDocumentForm() == 'task'){
				String rusExecName = ""
				def execblocks = (_ExecsBlocks)it.getValueObject("execblock")
				execblocks.getExecutors().each{
					def exec = session.getStructure().getEmployer(it.getUserID());
					rusExecName += exec.getShortName() + ","
				}
				def control =  (_Control)it.getValueObject("control");
				control.addProlongation(formData.getNumberValueSilently("extenddays",0), '0', session)
				def struct = session.getStructure();
				def u = struct.getEmployer(it.getValueString("taskauthor"));
				String sh = u.getShortName();
				//it.addField("control", control)
				it.setViewText(_Helper.getDateAsStringShort(it.getValueDate("taskdate")) + ":" +  sh + "  (" +  rusExecName + "),  " + it.getValueString("briefcontent") + ", " + _Helper.getDateAsStringShort(control.getCtrlDate()),0)
				it.setViewText(control.getDiffBetweenDays(),5)
				it.save("[supervisor]")
			}
		}
	}
}
