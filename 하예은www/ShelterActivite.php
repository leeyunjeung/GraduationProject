<?php

error_reporting(E_ALL); 
ini_set('display_errors',1); 
include('dbcon.php');

$Addr =$_GET["careAddr"];
$kind = $_GET["kindCd"];
$stmt = $con->prepare("select * from animalList where careAddr like '%$Addr%' and kindCd like '%$kind%'");
$stmt->execute();

$response = array();

while($row=$stmt->fetch(PDO::FETCH_ASSOC)){
    extract($row);
    array_push($response, array("desertionNo"=>$desertionNo,
        "filename"=>$filename,
        "happenDt"=>$happenDt,
        "happenPlace"=>$happenPlace,
        "kindCd"=>$kindCd,
        "colorCd"=>$colorCd,
        "age"=>$age,
        "weight"=>$weight,
        "noticeNo"=>$noticeNo,
        "noticeSdt"=>$noticeSdt,
        "noticeEdt"=>$noticeEdt,
        "popfile"=>$popfile,
        "processState"=>$processState,
        "sexCd"=>$sexCd,
        "neuterYn"=>$neuterYn,
        "specialMark"=>$specialMark,
        "careNm"=>$careNm,
        "careTel"=>$careTel,
        "careAddr"=>$careAddr,
        "orgNm"=>$orgNm,
        "officetel"=>$officetel));
}

header('Content-Type: application/json');
echo json_encode(array("response"=>$response),JSON_UNESCAPED_UNICODE);

?>