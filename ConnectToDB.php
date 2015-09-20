
<?php
$con = mysqli_connect("localhost","cropadvisor","cropadvisor","cropadvisor");
mysqli_query ($con,"set character_set_results='utf8'");
if (mysqli_connect_errno()) 
{
	echo "Failed to connect to MySQL: ". mysqli_connect_error();	
}
?>

