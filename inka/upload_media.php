<?php

$target_dir = "upload/";
$target_file_name = $target_dir.basename($_FILES["file"]["name"]);
$response = array();

if(isset($_FILES["file"])){
	if (move_uploaded_file($_FILES["file"]["tmp_name"], $target_file_name)) {
		$success = true;
		$message = "Successfully Uploaded";
	}else{
		$success = false;
		$message = "Error Uploading";

	}
}else{
	$success = false;
	$message = "Required Field Missing";
}
$response["success"] = $success;
$response["message"] = $message;
echo json_encode($response);


?>