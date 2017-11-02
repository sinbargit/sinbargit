const fetch = require('isomorphic-fetch');

module.exports=function (req,resp) {
    return new Promise(function (resolve) {
        fetch("http://127.0.0.1:8080/resource/xiaobai/index", {
            method: "GET",
            headers: {
                "ps": "201314"
            }
        }).then((res) =>
        {
            if (res.status == "200") {
                res.text().then(function (data)
                {
                    resolve({article:data})
                });
            }
        });
    })
}