<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');


    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {

       

        $id=$_POST['id'];


        if(!isset($errMSG)) 
        {
            try{

                $stmt = $con->prepare('select file_name from missing_find where id = :id ');
                $stmt->bindValue(":id",$id);
                $stmt->execute();
                $result = $stmt->fetch(PDO::FETCH_ASSOC);
                
                if(sizeof($result)>0){
                    $file_path = $_SERVER['DOCUMENT_ROOT'].'/lostandfound/'.$result['file_name'];
                    $stmt = $con->prepare('delete from missing_find where id = :id ');
                    $stmt->bindParam(':id', $id);

                    if($stmt->execute())
                    {
                            $successMSG = "삭제되었습니다.";
                    }
                    else
                    {
                            $errMSG = "삭제 실패";
                    }
                }
                else{
                    $errMSG = "삭제 실패";
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