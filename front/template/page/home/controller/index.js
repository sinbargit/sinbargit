const fetch = require('isomorphic-fetch');

module.exports=function (req,resp) {
    return new Promise(function (resolve) {
        fetch("//127.0.0.1:8080/resource/admin/root", {
            method: "GET",
            headers: {
                "ps": "admin"
            }
        }).then((res) =>
        {
            if (res.status == "200") {
                res.text().then(function (data)
                {
                    resolve(data)
                });
            }
        });
    })
}