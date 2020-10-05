<?php

error_reporting(E_ALL); 
ini_set('display_errors',1); 
include('dbcon.php');

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
if((($_SERVER['REQUEST_METHOD'] == 'POST' && isset($_POST['submit'])) )|| $android)
{
try{
    $id = $_GET["id"];
    $note_title= $_POST["note_title"];
    $note_memo= $_POST["note_memo"];
    $categorize= $_POST["categorize"];
    $picture= $_POST["picture"];
	$fileName = $_POST["fileName"];
    $stmt = $con->prepare("UPDATE review SET note_title='$note_title', note_memo = '$note_memo', categorize='$categorize',picture='$picture',file_name='$fileName' WHERE id='$id'");
    
    if($stmt->execute()){
        $successMSG = "수정 완료";
		$stmt = $con->prepare("select * from review inner join user on review.email = user.email where id='$id'order by id desc");
		$stmt->execute();

		$response = array();

		while($row=$stmt->fetch(PDO::FETCH_ASSOC)){
		extract($row);
		array_push($response, array("id"=>$id,
			"note_title"=>$note_title,
			"note_memo"=>$note_memo,
			"nickname"=>$nickname,
			"categorize"=>$categorize,
			"picture"=>$picture,
			"email"=>$email));
		}
		//header('Content-Type: application/json; charset=utf8');
		echo json_encode(array("response"=>$response));

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