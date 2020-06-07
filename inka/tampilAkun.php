<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['id'])) {
 
    // menerima parameter POST ( email dan password )
    $uid = $_POST['id'];
 
    // get the user account by id
    // get user berdasarkan id
    $user = $db->getUserAccount($uid);
 
    if ($user != false) {
        // user ditemukan
        $response["error"] = FALSE;
        $response["uid"] = $user["unique_id"];
        $response["user"]["nama"] = $user["nama"];
        $response["user"]["email"] = $user["email"];
        $response["user"]["no_telp"] = $user["no_telp"];
        $response["user"]["jenis_kegiatan"] = $user["jenis_kegiatan"];
        $response["user"]["foto_profile"] = $user["foto_profile"];
        echo json_encode($response);
    } else {
        // user tidak ditemukan
        $response["error"] = TRUE;
        $response["error_msg"] = "Get data gagal. Id salah";
        echo json_encode($response);
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Parameter kosong";
    echo json_encode($response);
}
?>