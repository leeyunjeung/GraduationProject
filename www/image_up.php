<?php

error_reporting(E_ALL); 
ini_set('display_errors',1); 

include('dbcon.php');

 echo "confirm file information <br />";

 $uploadfile = $_FILES['upload']['name'];
 $handle = fopen($uploadfile,"rb");
 if(move_uploaded_file($_FILES['upload']['tmp_name'],$uploadfile )){
  echo "파일이 업로드 되었습니다.<br />";
  echo "<img src ={$_FILES['upload']['name']}> <p>";
  echo "1. file name : {$_FILES['upload']['name']}<br />";
  echo "2. file type : {$_FILES['upload']['type']}<br />";
  echo "3. file size : {$_FILES['upload']['size']} byte <br />";
  echo "4. temporary file size : {$_FILES['upload']['size']}<br />";
  $imageblob = addslashes(fread($handle, filesize($uploadfile)));
  $stmt = $con->prepare("INSERT INTO images(image) VALUES '$file_name'");
  if($stmt->execute())
                {
                    $successMSG = "새로운 사용자를 추가했습니다.";
                    echo $successMSG;
                }
  else
                {
                    $errMSG = "사용자 추가 에러";
                    echo $errMSG;
                }



  
 } else {
  echo "파일 업로드 실패 !! 다시 시도해주세요.<br />";
 }
?>