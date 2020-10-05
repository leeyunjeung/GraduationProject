<?php

error_reporting(E_ALL); 
ini_set('display_errors',1); 
include('dbcon.php');

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if((($_SERVER['REQUEST_METHOD'] == 'POST' && isset($_POST['submit'])) )|| $android)
{
try{
    $email = $_GET["email"];
      
    $password= $_POST["password"];
    $nickname= $_POST["nickname"];
    $telephone= $_POST["telephone"];
    $temp= $_POST["temp"];
    //$data=base64_decode($temp);
  
    $stmt = $con->prepare("UPDATE user SET password = '$password', nickname='$nickname',telephone='$telephone',image='$temp' WHERE email='$email'");
    
    if($stmt->execute()){
        $successMSG = "정보를 저장했습니다.";
    }
    else{
        $errMSG = "정보 저장 에러";
    }
}catch(PDOException $e){
    die("DATABASE ERROR : " . $e->getMessage());
}

    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;

}


?>