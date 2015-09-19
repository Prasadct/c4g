var $ = jQuery.noConflict();



// Progress Bar

$(document).ready(function ($) {
    "use strict";
    
    $('.skill-shortcode').appear(function () {
        $('.progress').each(function () {
            $('.progress-bar').css('width',  function () { return ($(this).attr('data-percentage') + '%')});
        });
    }, {accY: -100});
        
        
});

function addTable(str) {	
     var xmlhttp;    
if (window.XMLHttpRequest)
  {// code for IE7+, Firefox, Chrome, Opera, Safari
  xmlhttp=new XMLHttpRequest();
  }
else
  {// code for IE6, IE5
  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }
xmlhttp.onreadystatechange=function()
  {
  if (xmlhttp.readyState==4 && xmlhttp.status==200)
    {
var table=document.getElementById("excelDataTable");
var tags = $.parseJSON(xmlhttp.responseText);
var myTableDiv = document.getElementById("DynamicTable");
var table = document.createElement('TABLE');
table.border='1';
var tableBody = document.createElement('TBODY');
table.appendChild(tableBody);
for (var i=0; i<3; i++){
var tr = document.createElement('TR');
tableBody.appendChild(tr);
var td = document.createElement('TD');
td.width='75';
td.appendChild(document.createTextNode(tags[i].crop_id));
tr.appendChild(td);
var td1 = document.createElement('TD');
td1.width='75';
td1.appendChild(document.createTextNode(tags[i].Description));
tr.appendChild(td1);
}
myTableDiv.appendChild(table);
}
}
  
xmlhttp.open("GET","http://128.199.125.48/GetDetailsForMainType.php?mainType=ReportDisease&cropId="+str ,true);
xmlhttp.send();    
}


 function sendData(){
	 var province= document.getElementById("selector");
	 var textB=document.getElementById("textb");
	 var xmlhttp;    
if (window.XMLHttpRequest)
  {// code for IE7+, Firefox, Chrome, Opera, Safari
  xmlhttp=new XMLHttpRequest();
  }
else
  {// code for IE6, IE5
  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }
xmlhttp.onreadystatechange=function()
  {
  if (xmlhttp.readyState==4 && xmlhttp.status==200)
    {
		
		textB.value="";
	}
	 
  }
xmlhttp.open("GET","http://128.199.125.48/InsertNotification.php?province="+province.value+"&textval="+textB.value ,true);
xmlhttp.send(); 	
 }