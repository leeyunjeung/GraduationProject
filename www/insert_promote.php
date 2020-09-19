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
		
		//$data=base64_decode($imagedevice);	
		//$escaped_values=mysql_real_escape_string($data);
		
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

        if(!isset($errMSG))
        {
            try{
                $stmt = $con->prepare('INSERT INTO promote(email, note_title, note_memo, local,picture) VALUES(:email, :note_title,:note_memo, :local,:picture)');
                $stmt->bindParam(':email', $email);
                $stmt->bindParam(':note_title', $note_title);
				$stmt->bindParam(':note_memo', $note_memo);
                $stmt->bindParam(':local', $local);
				$stmt->bindParam(':picture',$picture);

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
?>
<?php 
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;

	$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
   
    if( !$android )
    {
?>
<html>
   <body>
        
        <form action="<?php $_PHP_SELF ?>" method="POST">
            email: <input type = "text" name = "email" />
            note_memo: <input type = "text" name = "note_memo" />
            note_title: <input type = "text" name = "note_title" />
            local: <input type = "text" name = "local" />
            <input type = "submit" name = "submit" />
        </form>
   
   </body>
</html>



<?php 
    }
?>