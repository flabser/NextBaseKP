function message(text,elID){
	var dialogtitle = "Предупреждение";
	 if ($.cookie("lang")=="KAZ") {
		 dialogtitle = "Ескерту";
	 }else if ($.cookie("lang")=="ENG"){
		 dialogtitle = "Warning";
	 }
	 var divhtml ="<div id='dialog-message' title='"+dialogtitle+"'>";
	 divhtml+="<span style='height:50px; margin-top:4%; width:100%; text-align:center'>"+
	 "<font style='font-size:13px;'>"+ text +"</font></span></div>";
	 $("body").append(divhtml);
	 $("#dialog-message").dialog("destroy");
	 $("#dialog-message").dialog({
		height:140,
		modal: true,
		buttons: {
			"Ок": function() {
				$(this).dialog("close").remove();
				if (elID !=null){
					$("#"+elID).focus()
				}
			}
		}
	});
}