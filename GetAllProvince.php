<?php
include("ConnectToDb.php");

 mysql_select_db("cropadvisor");
   

 $result= mysqli_query($con,"SELECT * FROM province ");
/* create one master array of the records */
	$posts = array();
	if(mysql_num_rows($result)) {
		while($post = mysql_fetch_assoc($result)) {
			$posts[] = array('post'=>$post);
		}
	}

	/* output in necessary format */
	if($format == 'json') {
		header('Content-type: application/json');
		echo json_encode(array('posts'=>$posts));
	}

 mysql_close();

?>