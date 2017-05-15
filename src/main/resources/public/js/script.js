var xmlhttprequest=function(url){
    xmlhttp=new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("cart").innerHTML = xmlhttp.responseText;
        }
    };
    xmlhttp.open("GET",url,false);
    xmlhttp.send();
};