<?php
require_once('ConnectToDb.php');

$sql = "SELECT * FROM province";
$result = mysqli_query($conn, $sql);

$province_arr = array();

if (mysqli_num_rows($result) > 0) {
    while($row = mysqli_fetch_assoc($result)) {
        echo $row;
        array_push($province_arr, array($row));
    }
}

echo json_encode($province_arr);

?>