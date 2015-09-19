<?php

include(dirname(__FILE__)."/ConnectToDB.php");

	/* grab the posts from the db */
	 $result= mysqli_query($con,"SELECT * FROM province ");

	/* create one master array of the records */
	$posts = array();
	if(mysqli_num_rows($result)) {
		while($post = mysqli_fetch_array($result)) {
			array_push($posts, $post);
		}
	}

	
		echo json_encode(array("province" => $posts));
		
mysqli_close($con);
	
	?>