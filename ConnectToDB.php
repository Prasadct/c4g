<?php
$con = mysqli_connect("localhost","cropadvisor","cropadvisor","");
mysql_select_db('cropadvisor',$con);
if (mysqli_connect_errno()) 
{
	echo "Failed to connect to MySQL: ". mysqli_connect_error();	
}
?>
