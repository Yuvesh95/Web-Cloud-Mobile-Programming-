var url = "https://translate.yandex.net/api/v1.5/tr.json/translate",
    keyAPI = "trnsl.1.1.20180619T192739Z.41e5e24ba5289a73.b1f0ceda977c7b5437699ec7ecb5c3313ee14433";

document.querySelector('#translate').addEventListener('click', function() {
    var xhr = new XMLHttpRequest(),
        textAPI = document.querySelector('#source').value,
        langAPI = document.querySelector('#lang').value
    data = "key="+keyAPI+"&text="+textAPI+"&lang="+langAPI;
    xhr.open("POST",url,true);
    xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xhr.send(data);
    xhr.onreadystatechange = function() {
        if (this.readyState==4 && this.status==200) {
            var res = this.responseText;
            document.querySelector('#json').innerHTML = res;
            var json = JSON.parse(res);
            if(json.code == 200) {
                document.querySelector('#output').innerHTML = json.text[0];
            }
            else {
                document.querySelector('#output').innerHTML = "Error Code: " + json.code;
            }
        }
    }
}, false);