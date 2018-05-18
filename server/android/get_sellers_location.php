<?php 


    $servername = "localhost";
    $username = "root";
    $password = "";
    $dbname = "smart_pantry";
    
    // Create connection
    $conn = new mysqli($servername, $username, $password, $dbname);
    // Check connection
    if ($conn->connect_error) 
    {
        die("Connection failed: " . $conn->connect_error);
    }

    $obj_arr=array();
    $pos=0;
    $i=0;
    $qur = "SELECT location from sellers";

    $flag=0;
     if($res=mysqli_query($conn,$qur))
                       {
                         if(mysqli_num_rows($res) > 0)
                         {
                            while($row = mysqli_fetch_array($res))
                            {
                                $flag = 1;
                                $obj=new stdClass();
                                $obj->location=$row['location'];
                                $obj_arr[$i]=($obj);
                                $i=$i+1;
                            }
                          }
                        }
   if($flag==1)
      echo json_encode($obj_arr);
    else
      echo "null";
   
?>