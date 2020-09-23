<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');


    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {
		
        $email=$_POST['email'];
        $note_title=$_POST['note_title'];
		$note_memo=$_POST['note_memo'];
		$local=$_POST['local'];		
		$picture=$_POST['picture'];	
        $type=$_POST['type'];	
<<<<<<< HEAD
=======
        $fileName=$_POST['fileName'];
>>>>>>> yeeun
        $adoption=$_POST['adoption'];

		
		if(empty($note_title)){
            $errMSG = "제목을 입력하세요.";
        }
        else if(empty($note_memo)){
            $errMSG = "내용을 입력하세요.";
        }
        if(empty($local)){
            $errMSG = "지역을 입력하세요.";
        }
        if(empty($picture)){
            $errMSG = "사진을 입력하세요.";
        }
        if(empty($type)){
            $errMSG = "품종을 선택하세요.";
        }
        if(!isset($errMSG))
        {
            try{
<<<<<<< HEAD
                $stmt = $con->prepare('INSERT INTO promote(email, note_title, note_memo, local,picture,type,adoption) VALUES(:email, :note_title,:note_memo, :local,:picture,:type,:adoption)');
=======
                $stmt = $con->prepare('INSERT INTO promote(email, note_title, note_memo, local,picture,type,adoption,file_name) VALUES(:email, :note_title,:note_memo, :local,:picture,:type,:adoption,:fileName)');
>>>>>>> yeeun
                $stmt->bindParam(':email', $email);
                $stmt->bindParam(':note_title', $note_title);
				$stmt->bindParam(':note_memo', $note_memo);
                $stmt->bindParam(':local', $local);
				$stmt->bindParam(':picture',$picture);
                $stmt->bindParam(':type',$type);
<<<<<<< HEAD
=======
                $stmt->bindParam(':fileName',$fileName);
>>>>>>> yeeun
                $stmt->bindParam(':adoption',$adoption);

                if($stmt->execute())
                {
                    $successMSG = "새로운 글을 추가했습니다.";
                }
                else
                {
                    $errMSG = "게시글 추가 에러";
                }

            } catch(PDOException $e) {
                die("Database error: " . $e->getMessage()); 
            }
        }

    }

    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;

	$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
   

?>