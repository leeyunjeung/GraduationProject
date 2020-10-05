<?php

error_reporting(E_ALL); 
ini_set('display_errors',1); 
include('dbcon.php');

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if((($_SERVER['REQUEST_METHOD'] == 'POST' && isset($_POST['submit'])) )|| $android)
{
try{
    $id = $_GET["id"];
	$file_name = $_GET["file_name"];

  
    $stmt = $con->prepare('select file_name from review where id = :id ');
$stmt->bindValue(":id",$id);
    $stmt->execute();
$result = $stmt->fetch(PDO::FETCH_ASSOC);
	if(sizeof($result)>0){
            $file_path = $_SERVER['DOCUMENT_ROOT'].'/review/'.$result['file_name'];
		unlink($file_path);
            $stmt = $con->prepare("DELETE FROM review where id='$id'");
                        if($stmt->execute())
                        {
                            $successMSG = "삭제되었습니다.";
                        }
                        else
                        {
                            $errMSG = "삭제 실패";
                        }
                }
                else{
                    $errMSG = "삭제 실패";
                }
 
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