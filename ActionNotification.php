<?php

include( dirname(__FILE__)."/ConnectToDB.php");

$actionType = $_GET['actionType'] ;
if ($actionType=="select")
{
	/* grab the posts from the db */
	 $result= mysqli_query($con,"SELECT * FROM notification where isupdated=0 ");

	/* create one master array of the records */
	$posts = array();
	if(mysqli_num_rows($result)) {
		while($post = mysqli_fetch_array($result)) {
			$posts[] = $post;
		}
	}	
		echo json_encode($posts);
		}
		elseif ($actionType=="update")
		{
			mysqli_query($con,"UPDATE `notification` SET isupdated=1 ");
		}
mysqli_close($con);
	
	?>