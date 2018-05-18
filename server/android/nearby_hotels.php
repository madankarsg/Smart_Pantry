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
$i=0;
$flag=0;


$id=$_POST["id"];

$qur = "SELECT location from sellers where id=$id";
     if($res=mysqli_query($conn,$qur))
                       {
                         if(mysqli_num_rows($res) > 0)
                         {
                            while($row = mysqli_fetch_array($res))
                            {
                              $location = $row['location'];

                                          $qur2= "SELECT * from hotels where location='".$location."'";
                                          if($res2=mysqli_query($conn,$qur2))
                                          {
                                            if(mysqli_num_rows($res2)>0)
                                              {
                                                while($row2 = mysqli_fetch_array($res2))
                                                {
                                                  $flag=1;
                                                  $obj=new stdClass();
                                                  $obj->name=$row2['name'];
                                                  $obj->address=$row2['address'];
                                                  $obj->contact = $row2['id'];
                                                   $obj_arr[$i]=($obj);
                                                  $i=$i+1;
                                                }
                                              }
                                            }
                                             
                            }
                          }
                        }


   if($flag==1)
      echo json_encode($obj_arr);
    else
      echo "null";
       
?>