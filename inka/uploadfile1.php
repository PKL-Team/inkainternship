<?php
 
 
    require_once 'include/DB_Functions.php';
    $db = new DB_Functions();

// json response array
// $response = array("error" => FALSE);
 
if ($_SERVER['REQUEST_METHOD']=='POST') {
    // menerima parameter POST ( nama, email, password )
    $id_get = $_POST['id'];
    $id = str_replace('"', '', $id_get);

    // $originalImgName1= $_FILES['filename1']['name'];
    $tempName1= $_FILES['filename1']['tmp_name'];

    // $originalImgName2= $_FILES['filename2']['name'];
    $tempName2= $_FILES['filename2']['tmp_name'];
    // buat user baru
    $user = $db->simpanFile2($tempName1,$tempName2,$id);
    if ($user) {
        // simpan user berhasil
        // $response["error"] = FALSE;
        $response["status"] = "true";
        $response["message"] = "Successfully file added!";
        $response["user"]["url"] = $user["scan1"];
        echo json_encode($response);
    } else {
        // gagal menyimpan user
        // $response["error"] = TRUE;
        $response["status"] = "false";
        $response["message"] = "Failed";
        echo json_encode($response);
    }
    
} else {
    $response["status"] = "False";
    $response["message"] = "Failed";
    echo json_encode($response);
}
?>