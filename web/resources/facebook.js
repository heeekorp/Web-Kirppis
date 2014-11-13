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
        version    : 'v2.1'
    });
    CheckLoginStatus();
};

(function (doc) {
    var js;
    var id = 'facebook-jssdk';
    var ref = doc.getElementsByTagName('script')[0];
    if (doc.getElementById(id)) {
      return;
    }
    js = doc.createElement('script');
    js.id = id;
    js.async = true;
    js.src = "///connect.facebook.net/fi_FI/all.js";
    ref.parentNode.insertBefore(js, ref);
}(document));

function CheckLoginStatus(){
    FB.getLoginStatus(function(response) {
        if (response.status === 'connected') {
            console.log("Olet kirjautunut!");
            console.log(onkoKirjautunut);
            
            if(onkoKirjautunut === "false"){
                console.log("HaeFace!");
                haeface();
                //alert(document.getElementById('facebookid:hiddenFacebookID').value);
                var delay=400;//0.1 seconds
                setTimeout(function(){
                    facebookid();
                    facebooknimi();
                    kirjauduJS();
                    //your code to be executed after 1 seconds
                },delay);     
            }
        }
        else {
            console.log("Et ole kirjautunut!");
            console.log(onkoKirjautunut);
            
            if(onkoKirjautunut === "true"){
                console.log("Nollaa Face!");
                document.getElementById('facebookid:hiddenFacebookID').value = "0";
                document.getElementById('facebooknimi:hiddenNimi').value = "";
                facebookid();
                facebooknimi();
                kirjauduUlosJS();           
            }
        }
    });
}

function kirjauduJS(){
    console.log("kirjauduJS");
    kirjaudu();
    
    var delay=100;//0.1 seconds
    setTimeout(function(){
        location.reload(); 
        //your code to be executed after 1 seconds
    },delay);      
}

function kirjauduUlosJS(){
    console.log("kirjauduUlosJS");
    kirjauduUlos();
    
    var delay=100;//0.1 seconds
    setTimeout(function(){
         location.reload();
        //your code to be executed after 1 seconds
    },delay);  
    
}

function haeface() {
    FB.api('/me', 
    {fields:['id','first_name','last_name']},
    function (response) {
    document.getElementById('facebookid:hiddenFacebookID').value = response.id;

    var kokonimi = response.first_name + " " + response.last_name;
    document.getElementById('facebooknimi:hiddenNimi').value = kokonimi;
    //alert(document.getElementById('facebooknimi:hiddenNimi').value);
    console.log(document.getElementById('facebooknimi:hiddenNimi').value);
    });

}

