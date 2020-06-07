<?php
include_once 'config.php';


	$query = "SELECT * FROM tb_jurusan_smk";

	$hasil = mysqli_query($con,$query);
	if (mysqli_num_rows($hasil)>0) {
		$response = array();
		 if(mysqli_num_rows($hasil) > 0){  
			 while ($row = mysqli_fetch_assoc($hasil)) {
			 	$tampil['id_jur_smk'] = $row['id_jur_smk'];
			 	$tampil['jurusan_smk'] = $row['jurusan_smk'];
		        $response[] = $tampil;
		    }
	    echo json_encode(array("data" => $response) );
		}
	}
?>