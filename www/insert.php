<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');


    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {

        $email=$_POST['email'];
        $password=$_POST['password'];
		$telephone=$_POST['telephone'];
		$nickname=$_POST['nickname'];
		$image = $_POST['image'];
		if(empty($email)){
            $errMSG = "이메일을 입력하세요.";
        }
        else if(empty($password)){
            $errMSG = "패스워드를 입력하세요.";
        }
        if(empty($telephone)){
            $errMSG = "전화번호을 입력하세요.";
        }

        if(empty($nickname)){
            $errMSG = "닉네임을 입력하세요.";
        }

        if(!isset($errMSG))
        {
            try{
                $stmt = $con->prepare('INSERT INTO user(email, password, telephone, nickname, image) VALUES(:email, :password,:telephone, :nickname, :image)');
                $stmt->bindParam(':email', $email);
                $stmt->bindParam(':password', $password);
				$stmt->bindParam(':telephone', $telephone);
                $stmt->bindParam(':nickname', $nickname);
                $stmt->bindParam(':image', $image);
                
                if($stmt->execute())
                {
                    $successMSG = "새로운 사용자를 추가했습니다.";
                }
                else
                {
                    $errMSG = "사용자 추가 에러";
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
            password: <input type = "text" name = "password" />
            telephone: <input type = "text" name = "telephone" />
            nickname: <input type = "text" name = "nickname" />
            image : <input type = "text" name = "image"/>
            <input type = "submit" name = "submit" />
        </form>
   
   </body>
</html>



<?php 
    }
?>