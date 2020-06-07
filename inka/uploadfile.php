<?php
 if($_SERVER['REQUEST_METHOD']=='POST'){
  	// echo $_SERVER["DOCUMENT_ROOT"];  // /home1/demonuts/public_html
	//including the database connection file
  	
        include_once 'config.php';
  	  	
  	//$_FILES['image']['name']   give original name from parameter where 'image' == parametername eg. city.jpg
  	//$_FILES['image']['tmp_name']  temporary system generated name
  

        $originalImgName1= $_FILES['filename1']['name'];
        $tempName1= $_FILES['filename1']['tmp_name'];
        $folder1="file1/";
        $url1 = "http://192.168.43.36/inka/file1/".$originalImgName1;

        $originalImgName2= $_FILES['filename2']['name'];
        $tempName2= $_FILES['filename2']['tmp_name'];
        $folder2="file2/";
        $url2 = "http://192.168.43.36/inka/file2/".$originalImgName2;
         //update path as per your directory structure 
        
        if(move_uploaded_file($tempName1,$folder1.$originalImgName1) && move_uploaded_file( $tempName2,$folder2.$originalImgName2)){
                $query = "INSERT INTO tbl_file (scan1,scan2) VALUES ('$url1','$url2')";
                if(mysqli_query($con,$query)){
                
                	 $query= "SELECT * FROM tbl_file WHERE scan1='$url1'";
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
			   
                }else{
                	echo json_encode(array( "status" => "false","message" => "Failed!") );
                }
        	//echo "moved to ".$url;
        }else{
        	echo json_encode(array( "status" => "false","message" => "Failed!") );
        }
  }
?>