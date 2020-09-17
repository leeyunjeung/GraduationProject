<?php

error_reporting(E_ALL); 
ini_set('display_errors',1); 
include('dbcon.php');

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if((($_SERVER['REQUEST_METHOD'] == 'POST' && isset($_POST['submit'])) )|| $android)
{
try{
    $id = $_GET["id"];

    $stmt = $con->prepare('select file_name from promote where id = :id ');
    $stmt->bindValue(":id",$id);
    $stmt->execute();
    $result = $stmt->fetch(PDO::FETCH_ASSOC);
        if(sizeof($result)>0){
            $file_path = $_SERVER['DOCUMENT_ROOT'].'/promote/'.$result['file_name'];
            $stmt = $con->prepare("DELETE FROM promote where id='$id'");
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