<?php  
include( dirname(__FILE__)."/ConnectToDB.php");

$provinceid= $_GET['province'];
$description= $_GET['textval'];

print_r($provinceid);
//Save
  /* grab the posts from the db */
 
 
mysqli_query($con,"INSERT INTO `notification`(`province_id`, `description`) VALUES ('$provinceid','$description')");
mysqli_close($con);

  //INSERT INTO `notification`(`id`, `province_id`, `description`) VALUES ([value-1],[value-2],[value-3])
  ?>