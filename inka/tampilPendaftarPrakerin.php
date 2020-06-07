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
    $user = $db->getDataPrakerin($uid);
 
    if ($user != false) {
        // user ditemukan
        $response["error"] = FALSE;
        $response["uid"] = $user["id_user"];
        $response["user"]["nama"] = $user["nama_lengkap"];
        $response["user"]["nis"] = $user["nis"];
        $response["user"]["raport"] = $user["nilai_raport"];
        $response["user"]["sekolah"] = $user["nama_sekolah"];
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