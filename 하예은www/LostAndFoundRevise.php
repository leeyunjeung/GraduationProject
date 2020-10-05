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
        $id=$_POST['id'];
        $file_name=$_POST['file_name'];

		
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
                $stmt = $con->prepare('select file_name from missing_find where id = :id ');
                $stmt->bindValue(":id",$id);
                $stmt->execute();
                $result = $stmt->fetch(PDO::FETCH_ASSOC);
                
                if(sizeof($result)>0){
                    $file_path = $_SERVER['DOCUMENT_ROOT'].'/lostandfound/'.$result['file_name'];
                    $stmt = $con->prepare('UPDATE missing_find SET email=:email,sex=:sex,missing_date=:missing_date,place=:place,picture=:picture,m_f=:m_f,age=:age,kg=:kg,tnr=:tnr,color=:color,etc=:etc,feature=:feature,type=:type,file_name=:file_name WHERE id=:id');
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
                    $stmt->bindParam(':id',$id);
                    $stmt->bindParam(':file_name',$file_name);

                    if($stmt->execute())
                        {
                            $successMSG = "게시글이 수정 되었습니다.";
                        }
                    else
                        {
                            $errMSG = "수정 실패";
                        }
                }
                else{
                    $errMSG = "수정 실패";
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