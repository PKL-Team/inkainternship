<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);

if (isset($_POST['id'])) {

	$id = $_POST['id'];
	$user = $db->pklExisted($id);

	if ($user != false) {
        // user telah ada
        $response["error"] = FALSE;
        $response["result"] = "Yes";
        $response["error_msg"] = "Peserta telah ada dengan id_user " . $id;
        echo json_encode($response);
    }else{
        $response["error"] = TRUE;
        $response["error_msg"] = "Peserta belum daftar ";
        echo json_encode($response);

    }
}else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Parameter tidak ada";
    echo json_encode($response);
}
 
 ?>