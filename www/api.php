<?php

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

$options = array(CURLOPT_URL => 'http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?ServiceKey=tAYKbEZoElKC6FZC2J0g67Zg6GD5505OL7EtVUedRPrlEQeRe%2BOFWR77a5erXsoiElwMZsQjREQe5EC9SiZWEw%3D%3D',
        CURLOPT_RETURNTRANSFER => 1,
        CURLOPT_HEADER => false,
        CURLOPT_SSL_VERIFYPEER => false);
//$url  = 'http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?ServiceKey=tAYKbEZoElKC6FZC2J0g67Zg6GD5505OL7EtVUedRPrlEQeRe%2BOFWR77a5erXsoiElwMZsQjREQe5EC9SiZWEw%3D%3D';
$ch = curl_init();
//curl_setopt($ch, CURLOPT_URL,$url);
curl_setopt_array($ch,$options);
//curl_setopt_array($ch,CURLOPT_RETURNTRANSFER,1);
//curl_setopt_array($ch,CURLOPT_CONNECTTIMEOUT,10);
//curl_setopt_array($ch,CURLOPT_SSL_VERIFYPEER,false);

$response = curl_exec($ch);

//var_dump($response);
echo $response;
//print_r($response);

//$xml = simplexml_load_string($response);
//$result = $xml->body[0]->itmes[0];

//echo $result;


curl_close($ch);

?>