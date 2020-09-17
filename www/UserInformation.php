<?php


    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');


$email = $_GET["email"];


$stmt = $con->prepare("select nickname,telephone,image from user where email='$email'");
$stmt->execute();

$response = array();

while($row=$stmt->fetch(PDO::FETCH_ASSOC)){
      extract($row);
    array_push($response, array("nickname"=>$nickname,"telephone"=>$telephone,"image"=>$image));

}


//header('Content-Type: application/json; charset=utf8');
echo json_encode(array("response"=>$response));



?>