<?php

error_reporting(E_ALL); 
ini_set('display_errors',1); 
include('dbcon.php');

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if((($_SERVER['REQUEST_METHOD'] == 'POST' && isset($_POST['submit'])) )|| $android)
{
try{
    $id = $_GET["id"];

  
    $stmt = $con->prepare("DELETE FROM review where id='$id'");
    
    if($stmt->execute()){
        $successMSG = "게시글이 삭제되었습니다.";
    }
    else{
        $errMSG = "에러";
    }
}catch(PDOException $e){
    die("DATABASE ERROR : " . $e->getMessage());
}

    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;

}


?>