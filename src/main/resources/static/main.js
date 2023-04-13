let father = document.getElementById("father");
let ip = "127.0.0.1";               // 所部署服务器的IP
let localServerPort = "10088";       // application.yaml 端口号
let port = null;
const btn = document.getElementById('button-send');

// 创建Tcp连接
function createTcp() {
    $.ajax({
        type: "GET",
        url: `http://${ip}:${localServerPort}/api/connect`,
        dataType: "JSON",
        success: function (data) {
            let result = JSON.parse(JSON.stringify(data));
            if (result.status == "200") {
                port = result.data;
                let p = document.createElement("p");
                p.innerText = `服务器创建成功，建立在【${ip} : ${port}】 ${getNowDateTime()} `;
                father.appendChild(p);
                document.getElementById("onlineIp").innerText = `TCP服务器IP及端口【${ip} : ${port}】`;
                father.scrollTop = father.scrollHeight
                createWebSocket(port);
            }
        }
    })
}

function createWebSocket(port) {
    let websocketObj = undefined;
    websocketObj = new WebSocket(`ws://${ip}:${localServerPort}/ws/${port}`);

    // 接收到消息的回调方法
    websocketObj.onmessage = function (event) {
        setMessageInnerHTML(event.data);
    }

    //将消息显示在div
    function setMessageInnerHTML(innerHTML) {
        let p = document.createElement("p");
        p.innerText = innerHTML + " " + getNowDateTime();
        father.appendChild(p);
        father.scrollTop = father.scrollHeight

    }

    // 发送
    btn.addEventListener('click', function () {
        let input = document.getElementById("input-send");
        // 判断文本框数据是否为空
        if (input.value.trim() !== "") {
            // 将消息发送到服务器
            websocketObj.send(input.value);
        }

    })

}


// 关闭Tcp服务器
function closeTcp() {
    $.ajax({
        type: "POST",
        url: `http://${ip}:${localServerPort}/api/disconnect`,
        data: {
            "port": port
        },
        success: function (data) {
            let result = JSON.parse(JSON.stringify(data));
            if (result.status == "200") {
                let p = document.createElement("p");
                p.innerText = `服务器已关闭！ ${getNowDateTime()} `;
                father.appendChild(p);
                father.scrollTop = father.scrollHeight
                document.getElementById("onlineIp").innerText = `服务器已关闭，请刷新重建。`;
            } else if (result.status == "500") {
                let p = document.createElement("p");
                p.innerText = `服务器已关闭，请刷新重建。 ${getNowDateTime()} `;
                father.appendChild(p);
                father.scrollTop = father.scrollHeight
                document.getElementById("onlineIp").innerText = `服务器已关闭，请刷新重建。`;
            }
        }
    })
}

// 清空
function clearDiv() {
    father.innerHTML = "";
}


// 获取当前时间
function getNowDateTime() {
    return new Date().Format("yyyy-MM-dd HH:mm:ss");
}


// 时间格式化
Date.prototype.Format = function (fmt) {
    let o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}


