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
    document.getElementById("txtHint").innerHTML=xmlhttp.responseText;
	var myTableDiv = document.getElementById("DynamicTable");
      
    var table = document.createElement('TABLE');
	table.setAttribute('id','dist');
    table.border='1';
    
    var tableBody = document.createElement('TBODY');
    table.appendChild(tableBody);
      
    for (var i=0; i<3; i++){
       var tr = document.createElement('TR');
       tableBody.appendChild(tr);
       
       for (var j=0; j<4; j++){
           var td = document.createElement('TD');
           td.width='75';
           td.appendChild(document.createTextNode("Cell " + i + "," + j));
           tr.appendChild(td);
       }
    }
    myTableDiv.appendChild(table);
    }
  }
xmlhttp.open("GET","http://128.199.125.48/GetDetailsForMainType.php?mainType=ReportedDisease&cropId="+str ,true);
xmlhttp.send(); 
    
    
}
 function DeleteRows() {
	 
	 var table=document.getElementById('dist')
        var rowCount = table.rows.length;
        for (var i = rowCount-1; i >=0; i--) {
            table.deleteRow(i);
 }}