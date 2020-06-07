<?php
 
    require_once 'include/DB_Functions.php';
    $db = new DB_Functions();

// json response array
$response = array("error" => FALSE);
 
if ($_SERVER['REQUEST_METHOD']=='POST') {

    $id =$_POST['id'];
    $nama = $_POST['nama'];
    $nim = $_POST['nim'];
    $ipk = $_POST['ipk'];

    // buat user baru
    $user = $db->editInfoPkl($id,$nama,$nim,$ipk);
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