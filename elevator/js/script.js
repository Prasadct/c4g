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
//document.getElementById("txtHint").innerHTML=xmlhttp.responseText;
	var obj = JSON.parse(xmlhttp.responseText);
	for (var key in xmlhttp.responseText) {
  if (xmlhttp.responseText.hasOwnProperty(key)) {
    document.getElementById("txtHint").innerHTML=key + " -> " + xmlhttp.responseText[key];
  }
}
    }
  }
xmlhttp.open("GET","http://128.199.125.48/GetDetailsForMainType.php?mainType=ReportDisease&cropId="+str ,true);
xmlhttp.send(); 
    
    
}
 function DeleteRows() {
	 
	
 }