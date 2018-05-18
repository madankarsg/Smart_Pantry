<?php 
$id=$_POST['id'];
$name = $_POST['name'];
$address=$_POST['address'];
$location = $_POST['location'];
$contact = $_POST['contact'];

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
    $pos=0;

    $qur = "SELECT * from sellers";

$flag=0;
 if($res=mysqli_query($conn,$qur))
                   {
                     if(mysqli_num_rows($res) > 0)
                     {
                        while($row = mysqli_fetch_array($res))
                        {
                          if($id==$row['id'])
                          {
                          	$flag=1;
                          	break;
                          }

                        }
                      }
                    }
if($flag==1)
{
 $qur = "UPDATE  sellers set name='$name', address='$address', location='$location',contact='$contact' where id='$id'";
  if($result=$conn->query($qur))
    echo "YES";
  else
    echo "NO";
}
else
{
  $qur = "INSERT into sellers values('$id', '$name','$address','$location','$contact')";
  if($result=$conn->query($qur))
    echo "YES";
  else
    echo "NO";
  
}

?>