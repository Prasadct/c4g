<?php
$con = mysqli_connect("http://128.199.125.48/","cropadvisor","cropadvisor");
mysql_select_db('cropadvisor',$con);
if (mysqli_connect_errno()) 
{
	echo "Failed to connect to MySQL: ". mysqli_connect_error();	
}
?>
