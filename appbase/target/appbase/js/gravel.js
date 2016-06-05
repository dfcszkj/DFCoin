/**
 * Created by gravel on 16/5/18.
 */
var gravel = {
    ajax: function (url, data, successFun, errorFun) {
        $.ajax({
            type: "post",
            async: false,
            url: url,
            data: data,
            dataType: "json",
            error: (errorFun !== null && errorFun != undefined) ? errorFun : function (req) {
                console.info("i:");
                console.info(req.Id);
                alert("Error!");
            },
            success: (successFun !== null && successFun != undefined) ? successFun : function (req) {

            }
        });
    }, secondSelect: function (ele1, ele2, url, param, data) {
        $(ele1).change(function () {
            gravel.ajax(url + "?" + param + "=" + $(this).val() + "&_=" + Math.random(), data, function (json) {
                var str = "<option value='-1'></option>";
                if (json != null) {
                    $.each(json, function (i, v) {
                        var index = 0;
                        var key = "";
                        var value = "";
                        for (var o in v) {
                            if (index == 1) {
                                key = v[o];
                            }
                            if (index == 0) {
                                value = v[o];
                            }
                            index++;
                        }
                        str += "<option value='" + value + "'>" + key + "</option>";
                    });
                }
                $(ele2).html(str);
            });
        });
    }, csubstr: function (str, s, e) {
        var r = '';
        if (typeof (str) != 'string') {
            return str;
        }
        var l = str.length;
        if (e > l * 2) {
            return str;
        }
        for (var i = 0; i < l; i++) {
            if (s > i) continue;
            if (str.charCodeAt(i) > 255) {
                if (e == 1) {//剩一个字符
                    return r;
                }
                e = e - 2;
            } else {
                e--;
            }
            if (str.charAt(i) == undefined) {//防止取值溢出
                return r;
            }
            r = r + str.charAt(i);
            if (e <= 0) {
                return r;
            }
        }
        return r;
    }, hideName: function (str) {
        return str.replace(/^.$/, "*").replace(/^.(.)$/, "*$1").replace(/^(.).+(.)$/, "$1*$2");
    }, changeURLPar: function (url, ref, value) {
        var str = "";
        if (url.indexOf('?') != -1)
            str = url.substr(url.indexOf('?') + 1);
        else
            return url + "?" + ref + "=" + value;
        var returnurl = "";
        var setparam = "";
        var arr;
        var modify = "0";
        if (str.indexOf('&') != -1) {
            arr = str.split('&');
            for (i in arr) {
                if (arr[i].split('=')[0] == ref) {
                    setparam = value;
                    modify = "1";
                } else {
                    setparam = arr[i].split('=')[1];
                }
                returnurl = returnurl + arr[i].split('=')[0] + "=" + setparam
                    + "&";
            }
            returnurl = returnurl.substr(0, returnurl.length - 1);
            if (modify == "0")
                if (returnurl == str)
                    returnurl = returnurl + "&" + ref + "=" + value;
        } else {
            if (str.indexOf('=') != -1) {
                arr = str.split('=');
                if (arr[0] == ref) {
                    setparam = value;
                    modify = "1";
                } else {
                    setparam = arr[1];
                }
                returnurl = arr[0] + "=" + setparam;
                if (modify == "0")
                    if (returnurl == str)
                        returnurl = returnurl + "&" + ref + "=" + value;
            } else
                returnurl = ref + "=" + value;
        }
        return url.substr(0, url.indexOf('?')) + "?" + returnurl;
    }, getQueryString: function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return 0;
    },levenshteinDistance:function(s,t){
        if(s.length>t.length){
            var temp=s;
            s=t;
            t=temp;
            delete temp;
        }
        var n=s.length;
        var m=t.length;
        if(m==0){
            return n;
        }
        else if(n==0){
            return m;
        }
        var v0=[];
        for(var i=0;i<=m;i++){
            v0[i]=i;
        }
        var v1=new Array(n+1);
        var cost=0;
        for(var i=1;i<=n;i++){
            if(i>1){
                v0=v1.slice(0);
            }
            v1[0]=i;
            for(var j=1;j<=m;j++){
                if(s[i-1].toLowerCase()==t[j-1].toLowerCase()){
                    cost=0;
                }
                else{
                    cost=1;
                }
                v1[j]=Math.min.call(null,v1[j-1]+1,v0[j]+1,v0[j-1]+cost);
            }
        }
        return v1.pop();
    }
}