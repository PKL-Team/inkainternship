<?php
include_once 'config.php';

if (isset($_POST['id'])) {

	$id = $_POST['id'];
	$query = "SELECT * FROM tb_perguruan_tinggi WHERE id_kab = '$id'";

	$hasil = mysqli_query($con,$query);
	if (mysqli_num_rows($hasil)>0) {
		$response = array();
		 if(mysqli_num_rows($hasil) > 0){  
			 while ($row = mysqli_fetch_assoc($hasil)) {
			 	$tampil['id_pt'] = $row['id_pt'];
			 	$tampil['nama_pt'] = $row['nama_pt'];
		        $response[] = $tampil;
		    }
	    echo json_encode(array("data" => $response) );
		}
	}
}else{
    $response["error"] = TRUE;
    $response["error_msg"] = "Parameter kosong";
    echo json_encode($response);
}

?>