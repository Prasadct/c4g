<?php
include("ConnectToDb.php");

 mysql_select_db("cropadvisor");
   

 $result= mysqli_query($con,"SELECT * FROM province ");
$rows = array();
while($r = mysql_fetch_assoc($result )) {
    $rows[] = $r;
}
echo json_encode($rows);

 mysql_close();

?>