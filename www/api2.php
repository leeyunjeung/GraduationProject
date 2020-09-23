<?php
error_reporting(E_ALL); 
ini_set('display_errors',1); 
include('dbcon.php');

$url  = 'http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?numOfRows=5000&ServiceKey=lkB0hPshvWYvwGC3NtluZa5t716rAF7HHbUYFX6uI3fFDMBlw%2F1PpLlfPZxsR1%2FNFuGtI%2BWfWg%2BWxE87JpnUAw%3D%3D';

$data = file_get_contents($url);
//$data = json_decode($data,true);
$result_data = simplexml_load_string($data);

//print_r($result_data);

$result = $result_data->body->items;


for($count=0;$count<1000;$count++){
    $age= $result->item[$count]->age;
    $careAddr =$result->item[$count]->careAddr;
    $careNm =$result->item[$count]->careNm;
    $careTel =$result->item[$count]->careTel;
    $colorCd =$result->item[$count]->colorCd;
    $desertionNo =$result->item[$count]->desertionNo;
    $fileName =$result->item[$count]->filename;
    $happenDt =$result->item[$count]->happenDt;
    $happenPlace =$result->item[$count]->happenPlace;
    $kindCd =$result->item[$count]->kindCd;
    $neuterYn =$result->item[$count]->neuterYn;
    $noticeEdt =$result->item[$count]->noticeEdt;
    $noticeNo =$result->item[$count]->noticeNo;
    $noticeSdt =$result->item[$count]->noticeSdt;
    $officetel =$result->item[$count]->officetel;
    $orgNm =$result->item[$count]->orgNm;
    $popfile =$result->item[$count]->popfile;
    $processState =$result->item[$count]->processState;
    $sexCd =$result->item[$count]->sexCd;
    $specialMark =$result->item[$count]->specialMark;
    $weight =$result->item[$count]->weight;


    $stmt = $con->prepare(
        'insert into animalList(desertionNo,filename,happenDt,happenPlace,kindCd,
        colorCd,age,weight,noticeNo,noticeSdt,noticeEdt,popfile,processState,sexCd,neuterYn,specialMark,
        careNm,careTel,careAddr, orgNm,officetel) 
        select :desertionNo,:fileName,:happenDt,:happenPlace,:kindCd,
        :colorCd,:age,:weight,:noticeNo,:noticeSdt,:noticeEdt,:popfile,:processState,:sexCd,:neuterYn,
        :specialMark,:careNm,:careTel,:careAddr,:orgNm,:officetel
        from dual
        where not exists(select * from animalList where desertionNo=:desertionNo)');

    $stmt->bindParam(':desertionNo',$desertionNo);
    $stmt->bindParam(':fileName',$fileName);
    $stmt->bindParam(':happenDt',$happenDt);
    $stmt->bindParam(':happenPlace',$happenPlace);
    $stmt->bindParam(':kindCd',$kindCd);
    $stmt->bindParam(':colorCd',$colorCd);
    $stmt->bindParam(':age',$age);
    $stmt->bindParam(':weight',$weight);
    $stmt->bindParam(':noticeNo',$noticeNo);
    $stmt->bindParam(':noticeSdt',$noticeSdt);
    $stmt->bindParam(':noticeEdt',$noticeEdt);
    $stmt->bindParam(':popfile',$popfile);
    $stmt->bindParam(':processState',$processState);
    $stmt->bindParam(':sexCd',$sexCd);
    $stmt->bindParam(':neuterYn',$neuterYn);
    $stmt->bindParam(':specialMark',$specialMark);
    $stmt->bindParam(':careNm',$careNm);
    $stmt->bindParam(':careTel',$careTel);
    $stmt->bindParam(':careAddr',$careAddr);
    $stmt->bindParam(':orgNm',$orgNm);
    $stmt->bindParam(':officetel',$officetel);

    $stmt->execute();
}


?>
