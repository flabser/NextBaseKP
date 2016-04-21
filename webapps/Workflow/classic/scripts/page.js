/* author John - Lab of the future */

var _currentLang = $.cookie("lang") || "RUS";
var dlgTitleStr = {
	"RUS": {
		deleteerror: "Ошибка удаления",
		deleting:"Удаление",
		deletingsuccess:"Удаление завершено успешно",
		notdeleted:"Не удалено :",
		deleted:"Удалено :",
		notseldeldoc:"Не выбран документ для удаления"
	},
	"KAZ": {
		deleteerror: "Жою қателігі",
		deleting:"Жою",
		deletingsuccess:"Жою сәтті аяқталды",
		notdeleted:"Жойылмады :",
		deleted:"Жойылды :",
		notseldeldoc:"Жойылатын құжат таңдалмады"
	},
	"ENG": {
		deleteerror: "Error deleting",
		deleting:"Deleting",
		deletingsuccess:"Successfully deleted",
		notdeleted:"Not deleted :",
		deleted:"Deleted :",
		notseldeldoc:"Document is not selected"
	}
};

function sorting(pageid,column, direction){
	$.ajax({
		type: "POST",
		datatype:"XML",
		url: "Provider?type=service&operation=tune_session&element=page&id="+pageid+"&param=sorting_mode~on&param=sorting_column~"+column.toLowerCase() +"&param=sorting_direction~"+direction.toLowerCase(),
		cache:false,
		success: function (msg){
			window.location.reload()
		},
		error: function(data,status,xhr) {
		}
	})
}

function delDocument(dbID,typedel){
	var checkboxes = $("input[name^='chbox']:checked");
	if(checkboxes.length != 0){
		loadingOutline();
		var paramfields="";
		checkboxes.each(function(index, element){
			var ck={
				doctype: $(element).val(),
				docid: $(element).attr("id")
			};
			paramfields += $.param(ck);
			if (index+1 != checkboxes.length){
				paramfields +="&";
			}
		});

		$.ajax({
			type: "POST",
			datatype:"XML",
			url: "Provider?type=page&id=delete_document",
			cache:false,
			data: paramfields,
			success: function(msg){
				endLoadingOutline();
				var deleted = $(msg).find("deleted").attr("count");
				var undeleted = $(msg).find("undeleted").attr("count");
				var response = $(msg).find("response");
				var divhtml ="<div id='dialog-message' title='"+ dlgTitleStr[_currentLang].deleting +"'>";
				if(response.find("error").text().length != 0 || deleted == 0 || response.attr("status") == "error"){
					divhtml += "<p style='font-weight:bold'>" + dlgTitleStr[_currentLang].deleteerror + "</p><br/>";
					divhtml += "<div style='width:100%; max-height:65px; overflow:hidden; word-wrap:break-word; font-size:12px; margin-top:5px'>"+response.find("error").text()+"</div>";
				}else{
					divhtml += "<p style='font-weight:bold'>" + dlgTitleStr[_currentLang].deletingsuccess + "</p><br/>";
					divhtml += "<div style='width:100%; font-size:13px; margin-top:5px'>";
					divhtml +=dlgTitleStr[_currentLang].notdeleted + undeleted + "</div>";
					$(msg).find("undeleted").find("entry").not(":contains('undefined')").each(function(){
						divhtml += "<div style='width:360px; margin-left:20px; font-size:12px; overflow:hidden'>"+$(this).text()+"</div>";
					});
					divhtml += "<div style='width:100%; font-size:13px'>";
					divhtml += dlgTitleStr[_currentLang].deleted + deleted +"</div>";
					$(msg).find("deleted").find("entry").not(":contains('undefined')").each(function(){
						divhtml += "<div style='width:360px; margin-left:20px; font-size:12px; overflow:hidden'>"+$(this).text()+"</div>";
					})
				}

				divhtml += "</div>";
				$("body").append(divhtml);
				$("#dialog-message").dialog({
					modal: true,
					width: 400,
					buttons: {
						"Ок": function() {
							window.location.reload();
						}
					},
					beforeClose: function() {
						window.location.reload();
					}
				});
			},
			error: function(data,status,xhr){
				infoDialog(dlgTitleStr[_currentLang].deleteerror);
				endLoadingOutline()
			}
		})
	}else{
		infoDialog(dlgTitleStr[_currentLang].notseldeldoc);
		endLoadingOutline()
	}
}

function removeFromFavs(){
	var checkboxes = $("input[name^='chbox']:checked");
	if(checkboxes.length != 0){
		checkboxes.each(function(){
				$(this).closest("tr").children("td :last").children("img").click();
				$(this).closest("tr").remove();
			}
		);
		$("#allchbox").removeAttr("checked");
	}else{
		infoDialog(dlgTitleStr[_currentLang].notseldeldoc);
	}
}

