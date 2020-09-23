<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');


    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {
		
        $email=$_POST['email'];
        $sex=$_POST['sex'];
		$missing_date=$_POST['missing_date'];
		$place=$_POST['place'];		
        $picture=$_POST['picture'];	
        $m_f=$_POST['m_f'];	
        $type=$_POST['type'];	
        $age=$_POST['age'];	
        $kg=$_POST['kg'];	
        $type=$_POST['type'];	
        $tnr=$_POST['tnr'];	
        $color=$_POST['color'];	
        $etc=$_POST['etc'];	
        $feature=$_POST['feature'];	

        $fileName=$_POST['fileName'];	



		
		//$data=base64_decode($imagedevice);	
		//$escaped_values=mysql_real_escape_string($data);
		
		if(empty($sex)){
            $errMSG = "성별을 입력하세요.";
        }
        else if(empty($missing_date)){
            $errMSG = "날짜를 입력하세요.";
        }
        if(empty($place)){
            $errMSG = "지역을 입력하세요.";
        }
        if(empty($picture)){
            $errMSG = "사진을 입력하세요.";
        }
        if(empty($feature)){
            $errMSG = "특징을 입력하세요.";
        }
        if(empty($color)){
            $errMSG = "색상을 입력하세요.";
        }

        if(!isset($errMSG))
        {
            try{

                $stmt = $con->prepare('INSERT INTO missing_find(email, sex, missing_date, place,picture,m_f,age,kg,tnr,color,etc,feature,type,file_name) VALUES(:email, :sex,:missing_date, :place,:picture,:m_f,:age,:kg,:tnr,:color,:etc,:feature,:type,:fileName)');

                $stmt->bindParam(':email', $email);
                $stmt->bindParam(':sex', $sex);
				$stmt->bindParam(':missing_date', $missing_date);
                $stmt->bindParam(':place', $place);
                $stmt->bindParam(':picture',$picture);
                $stmt->bindParam(':m_f',$m_f);
                $stmt->bindParam(':age',$age);
                $stmt->bindParam(':kg',$kg);
                $stmt->bindParam(':tnr',$tnr);
                $stmt->bindParam(':color',$color);
                $stmt->bindParam(':etc',$etc);
                $stmt->bindParam(':feature',$feature);
                $stmt->bindParam(':type',$type);

                $stmt->bindParam(':fileName',$fileName);




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
            sex: <input type = "text" name = "sex" />
            missing_date: <input type = "text" name = "missing_date" />
            place: <input type = "text" name = "place" />
            m_f: <input type = "text" name = "m_f" />
            <input type = "submit" name = "submit" />
        </form>
   
   </body>
</html>



<?php 
    }
?>