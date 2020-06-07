<?php
 
    require_once 'include/DB_Functions.php';
    $db = new DB_Functions();

// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['id']) && isset($_POST['nama']) && isset($_POST['nim']) && isset($_POST['ipk'])  && isset($_POST['kampus'])) {

    $id_get =$_POST['id'];
    $nama_get = $_POST['nama'];
    $nim_get = $_POST['nim'];
    $ipk_get = $_POST['ipk'];
    $kampus_get = $_POST['kampus'];
    $divisi_get = $_POST['divisi'];

    $id = str_replace('"', '', $id_get);
    $nama = str_replace('"', '', $nama_get);
    $nim = str_replace('"', '', $nim_get);
    $ipk = str_replace('"', '', $ipk_get);
    $kampus = str_replace('"', '', $kampus_get);
    $divisi = str_replace('"', '', $divisi_get);

    $tempName1= $_FILES['filename1']['tmp_name'];
    $tempName2= $_FILES['filename2']['tmp_name'];
    $tempName3= $_FILES['filename3']['tmp_name'];
    $tempName4= $_FILES['filename4']['tmp_name'];
    $tempName5= $_FILES['filename5']['tmp_name'];

    // buat user baru
    $user = $db->daftarAwalPkl($id,$nama,$nim,$ipk,$kampus,$divisi,$tempName1,$tempName2,$tempName3,$tempName4,$tempName5);
    if ($user != false) {
        // simpan user berhasil
        $response["error"] = FALSE;
        $response["uid"] = $user["id_user"];
        $response["user"]["id"] = $user["id_user"];
        echo json_encode($response);
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