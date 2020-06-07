<?php
 
    require_once 'include/DB_Functions.php';
    $db = new DB_Functions();

    // json response array
    $response = array();
    // mengambil data 
    $user = $db->tampilSekolah();

    while ($row = mysqli_fetch_row($user)) {
        //menampilkan data pada array index ke 1
        $response[] = $row[1];
    }
        echo json_encode(array("data"=>$response));
    

?>