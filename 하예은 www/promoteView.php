<?php


    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

$select_local=$_POST["select_local"];
$select_type=$_POST["select_type"];
$select_adoption=$_POST["select_adoption"];

$stmt = $con->prepare("select * from promote inner join user on promote.email = user.email where local like'$select_local%'and type like '$select_type%' and adoption like '$select_adoption%' order by id desc");
$stmt->execute();
$response = array();

while($row=$stmt->fetch(PDO::FETCH_ASSOC)){
    extract($row);
    array_push($response, array("id"=>$id,
	"note_title"=>$note_title,
	"note_memo"=>$note_memo,
	"nickname"=>$nickname,
	"local"=>$local,
	"picture"=>$picture,
	"type"=>$type,
	"email"=>$email,
	"adoption"=>$adoption,
	"image"=>$image,
	"file_name"=>$file_name));
}

echo json_encode(array("response"=>$response));
?>