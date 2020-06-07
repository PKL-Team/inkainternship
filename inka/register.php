<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['nama']) && isset($_POST['email']) && isset($_POST['password']) && isset($_POST['no_telp']) && isset($_POST['jenis_kegiatan'])) {
 
    // menerima parameter POST ( nama, email, password )
    $nama = $_POST['nama'];
    $email = $_POST['email'];
    $password = $_POST['password'];
    $no_telp = $_POST['no_telp'];
    $jenis_kegiatan = $_POST['jenis_kegiatan'];
    $foto_profile = $_POST['foto_profile'];
     
    // Cek jika user ada dengan email yang sama
    if ($db->isUserExisted($email)) {
        // user telah ada
        $response["error"] = TRUE;
        $response["error_msg"] = "User telah ada dengan email " . $email;
        echo json_encode($response);
    } else {
        //level user
        if ($jenis_kegiatan == "Prakerin (SMK)") {
            $level_user = "1";
        }else if ($jenis_kegiatan == "PKL (Mahasiswa)") {
            $level_user = "2";
        }else{
            $level_user = "0";
        }

        // buat user baru
        $user = $db->simpanUser($nama, $email, $password, $no_telp, $jenis_kegiatan, $level_user, $foto_profile);
        if ($user != false) {
            // simpan user berhasil
            $response["error"] = FALSE;
            $response["uid"] = $user["unique_id"];
            $response["user"]["nama"] = $user["nama"];
            $response["user"]["email"] = $user["email"];
            echo json_encode($response);
        } else {
            // gagal menyimpan user
            $response["error"] = TRUE;
            $response["error_msg"] = "Terjadi kesalahan saat melakukan registrasi";
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Parameter (nama, email, password, nomor telepon atau jenis kegiatan) ada yang kurang";
    echo json_encode($response);
}
?>