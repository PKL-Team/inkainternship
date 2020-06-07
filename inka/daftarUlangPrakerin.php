<?php
 
    require_once 'include/DB_Functions.php';
    $db = new DB_Functions();

// json response array
$response = array("error" => FALSE);
 
if ($_SERVER['REQUEST_METHOD']=='POST') {

    $tempName= $_FILES['filename']['tmp_name'];

    $id_get =$_POST['id'];
    $id = str_replace('"', '', $id_get);

    // buat user baru
    $user = $db->daftarUlangPrakerin($id,$tempName);
    if ($user) {
        // simpan user berhasil
        $response["error"] = FALSE;
        $response["uid"] = $user["id_user"];
        $response["user"]["id"] = $user["id_user"];
        echo json_encode($response);
    } else {
        // gagal menyimpan user
        $response["error"] = TRUE;
        $response["error_msg"] = "Daftar Gagal. Ada kesalahan";
        echo json_encode($response);
    }
    
} else {    
    $response["error"] = TRUE;
    $response["error_msg"] = "File belum dipilih atau beberapa field belum diisi";
    echo json_encode($response);
}
?>