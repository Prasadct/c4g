<?php
$ervername = "localhost";
$username = "cropadvisor";
$password = "cropadvisor";
$dbname = "cropadvisor";

// Create connection
$conn = mysqli_connect($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
	die("Connection failed: " . mysqli_connect_error());
}
?>