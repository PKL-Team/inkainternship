<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['id_jurusan']) && isset($_POST['id_periode'])) {
 
    // menerima parameter POST ( email dan password )
    $id_jurusan = $_POST['id_jurusan'];
    $id_periode = $_POST['id_periode'];
 
    // get the user account by id
    // get user berdasarkan id
    $user1 = $db->getTahun($id_periode);
    $user2 = $db->getKuotaPkl($id_jurusan,$id_periode);
 
    if ($user1 != false && $user2 != false) {
        // user ditemukan
        $response["error"] = FALSE;
        // $response["uid"] = $user["unique_id"];
        $response["user"]["tahun"] = $user1["tahun"];
        $response["user"]["kuota"] = $user2["jumlah"];
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