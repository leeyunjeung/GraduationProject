<?php

error_reporting(E_ALL); 
ini_set('display_errors',1); 
include('dbcon.php');

$email = $_GET["email"];
$listName = $_GET["listName"];

if($listName == "promote"){
    $result = $con->prepare("select * from promote inner join user  where promote.email='$email' and user.email = '$email'");
$result->execute();

$response = array();

while($row=$result->fetch(PDO::FETCH_ASSOC)){
           extract($row);
    array_push($response,array("note_title"=>$note_title,"id"=>$id,"picture"=>$picture,"note_memo"=>$note_memo,"nickname"=>$nickname,
	"local"=>$local,"email"=>$email,"adoption"=>$adoption,"type"=>$type,"image"=>$image));
}

}
else if($listName == "missing_find"){
    $result = $con->prepare("select * from missing_find inner join user where missing_find.email='$email' and user.email='$email'");
$result->execute();

$response = array();

while($row=$result->fetch(PDO::FETCH_ASSOC)){
           extract($row);
    array_push($response,array("id"=>$id,
	"m_f"=>$m_f,
	"missing_date"=>$missing_date,
	"place"=>$place,
	"sex"=>$sex,
    "picture"=>$picture,
    "type"=>$type,
    "tnr"=>$tnr,
    "kg"=>$kg,
    "age"=>$age,
    "color"=>$color,
    "feature"=>$feature,
    "etc"=>$etc,
    "email"=>$email));
}

}
else if($listName == "review"){
    $result = $con->prepare("select * from review inner join user where review.email='$email' and user.email='$email'");

    $result->execute();

$response = array();

while($row=$result->fetch(PDO::FETCH_ASSOC)){
           extract($row);
    array_push($response,array("note_title"=>$note_title,"id"=>$id,"picture"=>$picture,"note_memo"=>$note_memo,"nickname"=>$nickname,
	"categorize"=>$categorize,"email"=>$email,"image"=>$image));
}
}

header('Content-Type: application/json');
echo json_encode(array("response"=>$response),JSON_UNESCAPED_UNICODE);


?>