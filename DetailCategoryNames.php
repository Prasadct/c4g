<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
  

</head>

<body>
 <?php
   
include( dirname(__FILE__)."/ConnectToDB.php");
if(isset($_GET['categoryName'])){
    $category_name = $_GET['categoryName'];
	
   
     $result = mysqli_query($con,"SELECT * FROM detail where categoryName='$category_name' ");
     //$row = mysqli_fetch_array($result);



?>

<form>
<table align="center">
<?php while ($row = mysqli_fetch_array($result)){?>	 
<tr>
<td >
<?php echo htmlspecialchars($row['name']);?>
</td>
</tr>
<tr>
<td>
<img src="<?php  echo $row['image']; ?>" width="160" height="120">
</td>
</tr>
<tr>
<td>
<?php echo htmlspecialchars($row['description']);?>
</td>
</tr>
<tr>
<td>
</td>
</tr>
<tr>
<td>
</td>
</tr>
<?php }

?>
</table>


</form>


<?php mysqli_close($con);
}
?>
</body>

</html>
