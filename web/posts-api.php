<?php
define("BASE_URL","http://www.tutorialwebz.com");
$myurl = strlen($_SERVER['QUERY_STRING']) ? basename($_SERVER['PHP_SELF'])."?".$_SERVER['QUERY_STRING'] : basename($_SERVER['PHP_SELF']);
$base_url = BASE_URL."/wp-json/wp/v2/posts";
$content = isset($_GET['with_content'])?$_GET['with_content']:0;
if($myurl!="posts-api.php"){
	$base_url.="?".substr($myurl,14);
}
$id = isset($_GET['id'])?$_GET['id']:0;
if($id!=0){
	$base_url = BASE_URL."/wp-json/wp/v2/posts/".$id;
	$content=1;
	header('Content-Type: application/json');
	echo file_get_contents($base_url);
	exit();
}

$data = file_get_contents($base_url);

$tmp_data = json_decode($data);
foreach($tmp_data as $key=>$val){
  		$media=get_media($tmp_data[$key]->featured_media);
	    $tmp_data[$key]->featured_media_url=$media;
		if($content==0)
			$tmp_data[$key]->content=array("rendered"=>"");
}
header('Content-Type: application/json');
echo json_encode($tmp_data);
function get_media($media_id){
	try{
	$media_url = BASE_URL."/wp-json/wp/v2/media";
	$media_json = json_decode(file_get_contents($media_url."/".$media_id));
	return $media_json->media_details->sizes->medium->source_url;
	}
	catch(Exception $e){
		return "";
	}
	  
}