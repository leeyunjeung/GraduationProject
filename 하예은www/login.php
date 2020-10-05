<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');


    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {

        // 안드로이드 코드의 postParameters 변수에 적어준 이름을 가지고 값을 전달 받습니다.

        $email=$_POST['email'];
        $password=$_POST['password'];

        if(empty($email)){
            $errMSG = "아이디를입력하세요.";
        }
        else if(empty($password)){
            $errMSG = "비밀번호를 입력하세요.";
        }

        if(!isset($errMSG)) // 이름과 나라 모두 입력이 되었다면 
        {
            try{
                // SQL문을 실행하여 데이터를 MySQL 서버의 person 테이블에 저장합니다. 
                $stmt = $con->prepare('select email,password from user where email = :email and password = :password');
                $stmt->bindParam(':email', $email);
                $stmt->bindParam(':password', $password);
                $stmt->execute();
                $result = $stmt->fetchAll(PDO::FETCH_NUM);

                if(sizeof($result)>0)
                {
                    $successMSG = "로그인 완료.";
                }
                else
                {
                    $errMSG = "로그인 실패.";
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
                Email: <input type = "text" name = "email" />
                Password: <input type = "text" name = "password" />
                <input type = "submit" email = "submit" />
            </form>
       
       </body>
    </html>

<?php 
    }
?>