<?php
  
    error_reporting(E_ALL); 
    ini_set('display_errors',1); 
    

    $file_path = '/home/project/lostandfound/'.$_FILES['uploaded_file']['name'];
    //$handle = fopen($file_path,"rb");

    if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
        echo "success";
    } else{
        echo "fail";
    }

 ?>

