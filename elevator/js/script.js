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

function addTableReportDisease(str) {
    $.ajax({
        url: '/GetDetailsForMainType.php?mainType=ReportDisease&cropId='+str,
        dataType: 'application/json',
        complete: function(data){
            alert(data)
        },
        success: function(data){
            alert(data)
        }
    });

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
		
		
	}
	 
  }
xmlhttp.open("GET","http://128.199.125.48/GetDetailsForMainType.php?mainType=ReportDisease&province="+province.value+"&textval"+textB.value ,true);
xmlhttp.send(); 	
 }

function DeleteRows() {


}
