<?php
 
 include_once 'config.php';

// $query = mysqli_query($con, "SELECT * FROM tabel_sekolah ORDER BY id_sekolah ASC");
// $json = array();	
	
// 	while($row = mysqli_fetch_assoc($query)){
// 		$json[] = $row;
// 	 }
	
// 	 echo json_encode($json);
	
// 	 mysqli_close($con);
	$query= "SELECT * FROM tabel_sekolah ORDER BY id_sekolah ASC";
    $result= mysqli_query($con, $query);
    $emparray = array();
         if(mysqli_num_rows($result) > 0){  
         while ($row = mysqli_fetch_assoc($result)) {
                     $emparray[] = $row;
                   }
                   echo json_encode(array( "status" => "true","message" => "Successfully file added!" , "data" => $emparray) );
                   
         }else{
         		echo json_encode(array( "status" => "false","message" => "Failed!") );
         }
?>