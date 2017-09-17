let pro = new Promise(function (resolve,reject) {
    setTimeout(function () {
        console.log(1)
        resolve("1")
    },2000)
})
pro();
