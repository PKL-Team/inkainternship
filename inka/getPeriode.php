<?php
include_once 'config.php';

// if (isset($_POST['id'])) {

	$query = "SELECT * FROM tb_periode";

	$hasil = mysqli_query($con,$query);
	if (mysqli_num_rows($hasil)>0) {
		$response = array();
		 if(mysqli_num_rows($hasil) > 0){  
			 while ($row = mysqli_fetch_assoc($hasil)) {
			 	$tampil['id_periode'] = $row['id_periode'];
			 	$tampil['bulan'] = $row['bulan'];
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