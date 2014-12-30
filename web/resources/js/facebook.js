/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

 window.fbAsyncInit = function() {
    FB.init({
        appId      : '856102604401590',
        xfbml      : true,
        status     : true,
        cookie     : true,
        version    : 'v2.2'
    });
    CheckLoginStatus();
};

(function(d, s, id) {
  var js, fjs = d.getElementsByTagName(s)[0];
  if (d.getElementById(id)) return;
  js = d.createElement(s); js.id = id;
  js.src = "//connect.facebook.net/fi_FI/sdk.js#xfbml=1&appId=856102604401590&version=v2.0";
  fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

function CheckLoginStatus(){
    FB.getLoginStatus(function(response) {
        if (response.status === 'connected') {
            console.log("Olet kirjautunut!");
            if(onkoKirjautunut === "false" && rekisterointi === "false"){
                haeface();
                var delay=700;//0.7 seconds
                setTimeout(function(){
                    facebookid();
                    facebooknimi();
                    kirjaudu();
                    //your code to be executed after 0.7 seconds
                },delay);  
            }
        }
        else {
            console.log("Et ole kirjautunut!");
            if(onkoKirjautunut === "true" || rekisterointi === "true"){
                document.getElementById('facebookid:hiddenFacebookID').value = "0";
                document.getElementById('facebooknimi:hiddenNimi').value = "";
                facebookid();
                facebooknimi();
                kirjauduUlos();          
            }
        }
    });
}

function haeface() {
    FB.api('/me', 
    {fields:['id','first_name','last_name']},
    function (response) {
    document.getElementById('facebookid:hiddenFacebookID').value = response.id;

    var kokonimi = response.first_name + " " + response.last_name;
    document.getElementById('facebooknimi:hiddenNimi').value = kokonimi;
    });

}

