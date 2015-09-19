<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
  

</head>

<body>
 <?php
   
include( dirname(__FILE__)."/ConnectToDB.php");
if(isset($_GET['cropId'])){
    $crop_id = $_GET['cropId'];
	$id = $_GET['id'];
    
     $result = mysqli_query($con,"SELECT * FROM disease where crop_id='$crop_id' and id=$id");
     $row = mysqli_fetch_array($result);



?>
<form>
<table align="center">
<tr>
<td>
<img src="<?php  echo $row['image']; ?>" width="160" height="120">
</td>

</tr>
</table> 
<table align="center">

<tr>
<td style="width: 224px">
<?php echo htmlspecialchars($row['details']);?>
</td>
</tr>


</table>


</form>


<?php mysqli_close($con);
}
?>
</body>

</html>
