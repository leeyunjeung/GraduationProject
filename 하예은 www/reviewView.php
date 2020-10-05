<?php


    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

$select_categorize=$_POST["select_categorize"];

$stmt = $con->prepare("select * from review inner join user on review.email = user.email where categorize like '$select_categorize%' order by id desc");
$stmt->execute();
$response = array();

while($row=$stmt->fetch(PDO::FETCH_ASSOC)){
    extract($row);
    array_push($response, array("id"=>$id,
	"note_title"=>$note_title,
	"note_memo"=>$note_memo,
	"nickname"=>$nickname,
	"picture"=>$picture,
	"categorize"=>$categorize,
	"email"=>$email,
	"image"=>$image,
	"file_name"=>$file_name));
	
}

echo json_encode(array("response"=>$response));


?>