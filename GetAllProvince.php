<?php
require_once('ConnectToDb.php');

$result = mysql_query("select * from province");

$province_arr = array();
if(mysql_num_rows($result)> 0){
    while($row = mysql_fetch_array($result)){
        array_push($province_arr, array($row));
    }

    echo json_encode($province_arr);
}

?>