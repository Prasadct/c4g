<?php

include( dirname(__FILE__)."/ConnectToDB.php");

$number_of_posts = isset($_GET['cropType']) ? intval($_GET['cropType']) : 0;


	/* grab the posts from the db */
	if ($number_of_posts == 0)
	{
		 $result= mysqli_query($con,"SELECT * FROM crop ");
	}
	else
	{
		 $result= mysqli_query($con,"SELECT * FROM crop where type = '$number_of_posts'");
	}
	
	

	/* create one master array of the records */
	$posts = array();
	if(mysqli_num_rows($result)) {
		while($post = mysqli_fetch_array($result)) {
			$posts[] = $post;
		}
	}	
		echo json_encode($posts);
		
mysqli_close($con);

?>