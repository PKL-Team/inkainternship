<?php
 
    require_once 'include/DB_Functions.php';
    $db = new DB_Functions();

// json response array
$response = array("error" => FALSE);
 
if ($_SERVER['REQUEST_METHOD']=='POST') {

    $tempName1= $_FILES['filename1']['tmp_name'];
    $tempName2= $_FILES['filename2']['tmp_name'];
    $tempName3= $_FILES['filename3']['tmp_name'];
    $tempName4= $_FILES['filename4']['tmp_name'];


    $id_get =$_POST['id'];
    $nama_get = $_POST['nama'];
    $nis_get = $_POST['nis'];
    $raport_get = $_POST['raport'];
    $sekolah_get = $_POST['sekolah'];
    $divisi_get = $_POST['divisi'];    

    $id = str_replace('"', '', $id_get);
    $nama = str_replace('"', '', $nama_get);
    $nis = str_replace('"', '', $nis_get);
    $raport = str_replace('"', '', $raport_get);
    $sekolah = str_replace('"', '', $sekolah_get);
    $divisi = str_replace('"', '', $divisi_get);

    // buat user baru
    $user = $db->daftarAwalPrakerin($id,$nama,$nis,$raport,$sekolah,$divisi,$tempName1,$tempName2,$tempName3,$tempName4);
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