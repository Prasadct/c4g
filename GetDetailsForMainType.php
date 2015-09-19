<?php

include( dirname(__FILE__)."/ConnectToDB.php");

$mainType = $_GET['mainType'] ;
$cropId= $_GET['cropId'] ;

	/* grab the posts from the db */
	if ($mainType == "Disease")
	{
		 $result= mysqli_query($con,"SELECT * FROM disease where crop_id='$cropId' ");
	}
	elseif($mainType == "Detail")
	{
		 $result= mysqli_query($con,"SELECT * FROM detail where crop_id = '$cropId'");
	}
	elseif($mainType == "ReportDisease")
	{
		$result= mysqli_query($con,"SELECT * FROM reprotdesease where crop_id = '$cropId'");
	}
	elseif($mainType == "Support")
	{
		$result= mysqli_query($con,"SELECT * FROM support where crop_id = '$cropId'");
	}
	
	

	/* create one master array of the records */
	$posts = array();
	if(isset($result	) && mysqli_num_rows($result)) {
		while($post = mysqli_fetch_array($result)) {
			$posts[] = $post;
		}
	}	
		echo json_encode($posts);
		
mysqli_close($con);

?>