<?php
include_once 'config.php';

$query = "SELECT * FROM tb_provinsi";

$hasil = mysqli_query($con,$query);
if (mysqli_num_rows($hasil)>0) {
	$response = array();
	 if(mysqli_num_rows($hasil) > 0){  
		 while ($row = mysqli_fetch_assoc($hasil)) {
		 	$tampil['id_prov'] = $row['id_prov'];
		 	$tampil['nama_prov'] = $row['nama_prov'];
	        $response[] = $tampil;
	    }
    echo json_encode(array("data" => $response) );
	}
}

?>