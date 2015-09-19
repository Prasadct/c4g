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
	var result = [];

for(var i in obj)
    result.push([i,obj[i]]);


var data = new google.visualization.DataTable();
data.addColumn('string', 'key');
data.addColumn('string', 'value');
data.addRows(result);
}
    }
  
xmlhttp.open("GET","http://128.199.125.48/GetDetailsForMainType.php?mainType=ReportDisease&cropId="+str ,true);
xmlhttp.send(); 
    
    
}
 function DeleteRows() {
	 
	
 }