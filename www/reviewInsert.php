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
		$categorize=$_POST['categorize'];		
<<<<<<< HEAD
		$picture=$_POST['picture'];		
=======
		$picture=$_POST['picture'];	
		$fileName=$_POST['fileName'];	
>>>>>>> yeeun

		
		if(empty($note_title)){
            $errMSG = "제목을 입력하세요.";
        }
        else if(empty($note_memo)){
            $errMSG = "내용을 입력하세요.";
        }
        if(empty($categorize)){
            $errMSG = "구분을 선택하세요";
        }
        if(empty($picture)){
            $errMSG = "사진을 입력하세요.";
        }

        if(!isset($errMSG))
        {
            try{
<<<<<<< HEAD
                $stmt = $con->prepare('INSERT INTO review(email, note_title, note_memo,categorize,picture) VALUES(:email, :note_title,:note_memo, :categorize,:picture)');
=======
                $stmt = $con->prepare('INSERT INTO review(email, note_title, note_memo,categorize,picture,file_name) VALUES(:email, :note_title,:note_memo, :categorize,:picture,:fileName)');
>>>>>>> yeeun
                $stmt->bindParam(':email', $email);
                $stmt->bindParam(':note_title', $note_title);
				$stmt->bindParam(':note_memo', $note_memo);
                $stmt->bindParam(':categorize', $categorize);
				$stmt->bindParam(':picture',$picture);
<<<<<<< HEAD
=======
		$stmt->bindParam(':fileName', $fileName);
>>>>>>> yeeun

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