<?php


    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');


$select_local=$_POST["select_local"];
$select_type=$_POST["select_type"];
$select_mf=$_POST["select_mf"];
$end_date=$_POST["end_date"];
$start_date=$_POST["start_date"];

$stmt = $con->prepare("select * from missing_find where place like '$select_local%' and type like '$select_type%' and m_f like '$select_mf%' and DATE(missing_date) between '$start_date' and '$end_date' order by id");
$stmt->execute();

$response = array();

while($row=$stmt->fetch(PDO::FETCH_ASSOC)){
      extract($row);
    array_push($response, array("id"=>$id,
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
    "email"=>$email,
	"file_name"=>$file_name));

}


//header('Content-Type: application/json; charset=utf8');
echo json_encode(array("response"=>$response));



?>