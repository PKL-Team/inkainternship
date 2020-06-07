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
    $user = $db->getDataPkl($uid);
 
    if ($user != false) {
        // user ditemukan
        $response["error"] = FALSE;
        $response["uid"] = $user["id_user"];
        $response["user"]["nama"] = $user["nama_lengkap"];
        $response["user"]["nim"] = $user["nim"];
        $response["user"]["ipk"] = $user["nilai_ipk"];
        $response["user"]["kampus"] = $user["perguruan_tinggi"];
        $response["user"]["divisi"] = $user["penempatan_divisi"];
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