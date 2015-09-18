<?php
$con = mysql_connect("localhost","cropadvisor","cropadvisor");
mysql_select_db('cropadvisor',$con);
if (mysql_connect_errno()) 
{
	echo "Failed to connect to MySQL: ". mysql_connect_error();	
}
?>
