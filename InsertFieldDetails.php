<?php  
include( dirname(__FILE__)."/ConnectToDB.php");

$description= $_REQUEST['description'];
$image= $_REQUEST['image'];

//Save
  /* grab the posts from the db */
 
 
mysqli_query($con,"INSERT INTO `fielddetails`(`description`, `image`) VALUES ('$description', '$image')");
mysqli_close($con);

  //INSERT INTO `fielddetails`(`id`, `description`, `image`) VALUES ([value-1],[value-2],[value-3])
  ?>