<?php
include("ConnectToDb.php");

$result= mysqli_query($con,"SELECT * FROM province");

$posts = array();
if(mysql_num_rows($result)) {
    while($row = mysql_fetch_assoc($result)) {
        array_push($posts, array("post" => $row));
    }
}


echo json_encode(array('posts'=>$posts));


mysql_close();

?>