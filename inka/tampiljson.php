<?php
include_once 'config.php';

// if (isset($_POST['id'])) {

	$query = "SELECT * FROM tb_sekolah";

	$hasil = mysqli_query($con,$query);
	if (mysqli_num_rows($hasil)>0) {
		$response = array();
		 if(mysqli_num_rows($hasil) > 0){  
			 while ($row = mysqli_fetch_assoc($hasil)) {
			 	$tampil['id_sekolah'] = $row['id_sekolah'];
			 	$tampil['nama_sekolah'] = $row['nama_sekolah'];
		        $response[] = $tampil;
		    }
	    echo json_encode(array("data" => $response) );
		}
	}
// }else{
//     $response["error"] = TRUE;
//     $response["error_msg"] = "Parameter kosong";
//     echo json_encode($response);
// }

?>