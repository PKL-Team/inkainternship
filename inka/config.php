<?php
$host = "localhost";
$user = "root";
$password = "";
$db = "id13458025_db_inka";

$con = mysqli_connect($host,$user,$password,$db);

if(mysqli_connect_errno()){
	echo "Failed to connect".mysqli_connect_error();
}else{
	// echo "connect";
}
?>