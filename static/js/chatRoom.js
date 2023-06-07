var textInput;
var fileInput = `<div class="file-input"></div>`;
// 全局文件数组，保存用户添加的文件
var fileArray = new Array();

$('.contact').click(function () {
    window.open('https://www.doruo.cn/s/leaving', '_blank');
})

$(document).on('mouseenter', '.chat-item', function () {
    $(this).find('.chat-del').css('visibility', 'visible');
});

$(document).on('mouseleave', '.chat-item', function () {
    $(this).find('.chat-del').css('visibility', 'hidden');
});

function selectChatRoom(selectDom) {
    $('.chat-selected').removeClass('chat-selected');
    $(selectDom).addClass('chat-selected');
    let chatTitle = $(selectDom).find('.chat-title').text();
    // TODO 选择聊天室
    $('.right-title').html(chatTitle);
}

$(document).on('click', '.chat-item-main', function () {
    selectChatRoom(this)
});

$(document).on('click', '.chat-item',function () {
    selectChatRoom(this)
});

function delChatRoom(isCreate, dom) {
    if (isCreate) {
        if (confirm("创建者删除将会清空聊天记录！")){
            dom.detach();
        }else {

        }
    }else {
        dom.detach();
    }
}

$(document).on('click', '.chat-del', function () {
    // TODO 请求后端删除
    let delDom = $(this).parent('.chat-item')
    delChatRoom(true, delDom);
});

function hiddenModel() {
    // 隐藏模态框
    $('.overlay').css('z-index', '');
    $('.model').css('visibility', 'hidden');
}

function showModel() {
    // 显示模态框
    $('.overlay').css('z-index', '1');
    $('.model').css('visibility', 'visible');
}

function addChat() {
    let chatRoom = $('.model-input').val();
    if (chatRoom !== '') {
        $('.item-box').append(`<div class="chat-item">
                                <div class="chat-del"></div>
                                <div class="chat-title">${chatRoom}</div>
                                <div class="chat-bottom">
                                    <div class="chat-time">2023-06-29 09:45</div>
                                    <div class="chat-num">9999条对话</div>
                                </div>
                            </div>`);
        $('.model-input').val("");
    }else {
        alert("聊天室不能为空！！")
    }

}

$('.overlay').click(function () {
    hiddenModel()
});

$(".new-chat").click(function () {
    showModel()
});

$('.add').click(function () {
    // TODO 请求成功隐藏模态框，并添加聊天室
    hiddenModel();
    addChat()
});

$('.create').click(function () {
    // TODO 请求成功隐藏模态框，并添加聊天室
    hiddenModel();
    addChat()
});

$('#text-message').on('change', function () {
    if ($('#text-message').prop('checked')) {
        // 更改发送按钮属性
        $('.send-img').removeClass('file');
        $('.send-img').addClass('text');
        // 更改发送框
        fileInput = $('.file-input').detach();
        $('.message-input').prepend(textInput)
    }

});

$('#file-message').on('change', function () {
    if ($('#file-message').prop('checked')) {
        // 更改发送按钮属性
        $('.send-img').removeClass('text');
        $('.send-img').addClass('file');
        // 更改发送框
        textInput = $('.text-input').detach();
        $('.message-input').prepend(fileInput)
    }

});

$('.right-panel').on("dragenter", function (event) {
    event.preventDefault();

});

$('.right-panel').on("dragover", function (event) {
    event.preventDefault();
    $('.left-overlay').css('display', 'block');
    // 判断radio是否处于选中状态
    if ($('#text-message').prop('checked')) {
        $('#text-message').prop('checked', false);
        $('#file-message').prop('checked', true);
        // // 更改发送按钮属性
        $('.send-img').removeClass('text');
        $('.send-img').addClass('file');
        // 更改发送框
        textInput = $('.text-input').detach();
        $('.message-input').prepend(fileInput)

    }
});

$('.right-panel').on("dragleave", function () {
    $('.left-overlay').css('display', 'none');
});

$('.right-panel').on("drop", function (event) {
    event.preventDefault();

    const files = Array.from(event.originalEvent.dataTransfer.files);
    // 定义一个数组来存储不重复的文件
    const uniqueFiles = [];
    // 遍历文件列表，将不重复的文件添加到 uniqueFiles 中
    files.forEach(file => {
        if (!uniqueFiles.find(f => f.name === file.name && f.size === file.size)) {
            uniqueFiles.push(file);
        }
    });
    const finalFiles = []
    // 遍历不重复文件数组，将已经添加过的文件从中去除
    uniqueFiles.forEach(file => {
        if (!fileArray.find(f => f.name === file.name && f.size === file.size)) {
            finalFiles.push(file)
        }
    });

    if (finalFiles && finalFiles.length > 0) {
        for (let i = 0; i < finalFiles.length; i++) {
            var file = finalFiles[i];
            // 将文件添加到全局数组中
            fileArray.push(file)
            var reader = new FileReader();
            // 处理图片类文件
            if (file.type.indexOf("image") === 0) {
                let flag = i
                reader.onload = function (event) {
                    $('.file-input').append(`<div class="file-item">
                                                    <div class="file-del"></div>
                                                    <img src="${event.target.result}" alt="${finalFiles[flag].name}" class="file-preview" draggable="false">
                                                    <span class="file-name" title="${finalFiles[flag].name}">${finalFiles[flag].name}</span>
                                                </div>`)

                };

                reader.readAsDataURL(file);
            } else {
                let flag = i
                reader.onload = function (event) {
                    $('.file-input').append(`<div class="file-item">
                                                    <div class="file-del"></div>
                                                    <img src="../../static/img/unknown.png" alt="${finalFiles[flag].name}" class="file-preview" draggable="false">
                                                    <span class="file-name" title="${finalFiles[flag].name}">${finalFiles[flag].name}</span>
                                                </div>`)

                };

                reader.readAsArrayBuffer(file);
            }
        }

    }

    $('.left-overlay').css('display', 'none');
});

$(document).on('click', '.file-del', function () {
    $(this).parent('.file-item').detach();
    let fileName = $(this).siblings('.file-name').text();
    fileArray = fileArray.filter(file => file.name !== fileName);
});

$(document).on('mousewheel', '.file-input', function(e) {
    // 设置鼠标移动的距离
    let moveX = e.originalEvent.deltaY;

    // 控制容器横向滚动
    $(this).scrollLeft($(this).scrollLeft() + moveX);
});

/**
 *
 * @param dom:  元素的class或者id
 * @return:
 * @Author: Junior
 * @Date: 2023/6/6
 */
function changeTextareaHeight(dom) {
    let textarea = document.querySelector(dom)
    let _scrollHeight = textarea.scrollHeight;
    if (dom === '.text-input') {
        // 最大显示五行
        if (_scrollHeight / 21 <= 5){
            textarea.style.height = `${_scrollHeight}px`;
        }else {
            textarea.style.height = `105px`;
        }
    }else if (dom === '.bubble-text') {
        textarea.style.height = `${_scrollHeight}px`;
    }

}

$('.text-input').on('input', function () {
    changeTextareaHeight('.text-input')
});

function leftInsertHeadText(user, sendMsg) {
    // 往前添加靠左的气泡
    let $rightChatRoom = $('.right-chatRoom')
    $rightChatRoom.prepend(`<div class="bubble">
                                        <div class="bubble-user-box">
                                            <span class="bubble-user">${user}</span>
                                        </div>
                                        <div class="bubble-info-box">
                                            <div class="bubble-info">
                                                <span class="bubble-text"></span>
                                            </div>
                                        </div>
                                    </div>`);
    // 获取最后添加的气泡
    let lastBubble = $($rightChatRoom.children().first()).find('.bubble-text')[0];
    lastBubble.textContent = sendMsg
}

function leftInsertEndText(user, sendMsg) {
    // 往后添加靠左的气泡
    let $rightChatRoom = $('.right-chatRoom')
    $rightChatRoom.append(`<div class="bubble">
                                        <div class="bubble-user-box">
                                            <span class="bubble-user">${user}</span>
                                        </div>
                                        <div class="bubble-info-box">
                                            <div class="bubble-info">
                                                <span class="bubble-text"></span>
                                            </div>
                                        </div>
                                    </div>`);
    // 获取最后添加的气泡
    let lastBubble = $($rightChatRoom.children().last()).find('.bubble-text')[0];
    lastBubble.textContent = sendMsg
}

function rightInsertHeadText(user, sendMsg) {
    // 往前添加靠右的气泡
    let $rightChatRoom = $('.right-chatRoom')
    $rightChatRoom.prepend(`<div class="bubble">
                                        <div class="bubble-user-box-right">
                                            <span class="bubble-user">${user}</span>
                                        </div>
                                        <div class="bubble-info-box-right">
                                            <div class="bubble-info-right">
                                                <span class="bubble-text"></span>
                                            </div>
                                        </div>
                                    </div>`);
    // 获取最后添加的气泡
    let lastBubble = $($rightChatRoom.children().first()).find('.bubble-text')[0];
    lastBubble.textContent = sendMsg
}

function rightInsertEndText(user, sendMsg) {
    let $rightChatRoom = $('.right-chatRoom')
    // 往后添加靠右的气泡
    $rightChatRoom.append(`<div class="bubble">
                                        <div class="bubble-user-box-right">
                                            <span class="bubble-user">${user}</span>
                                        </div>
                                        <div class="bubble-info-box-right">
                                            <div class="bubble-info-right">
                                                <span class="bubble-text"></span>
                                            </div>
                                        </div>
                                    </div>`)
    // 获取最后添加的气泡
    let lastBubble = $($rightChatRoom.children().last()).find('.bubble-text')[0];
    lastBubble.textContent = sendMsg

}

/**
 *
 * @param horizontal:  决定气泡水平显示位置
 * @param vertical:  决定气泡垂直添加方向，从头添加或者从尾部添加
 * @param user:  用户名
 * @param sendMsg:  消息内容
 * @param sendType:  消息类型
 * @param isFail:  是否发送成功
 * @return:
 * @Author: Junior
 * @Date: 2023/6/6
 */
function drawBubble(horizontal, vertical, user, sendMsg, sendType, isFail) {

    // 气泡在左边显示
    if (vertical === 'left') {
        // 文字气泡
        if (sendType === 'text') {
            // 从头部插入
            if (horizontal === 'head') {
                leftInsertHeadText(user, sendMsg);
                // 从尾部插入
            }else if (horizontal === 'end') {
                leftInsertEndText(user, sendMsg);
            }
            // 文件气泡
        }else if (sendType === 'file') {

        }
        // 气泡在右边显示
    }else if (vertical === 'right') {
        // 文字气泡
        if (sendType === 'text') {
            // 从头部插入
            if (horizontal === 'head') {
                rightInsertHeadText(user, sendMsg);
                // 从尾部插入
            }else if (horizontal === 'end') {
                rightInsertEndText(user, sendMsg);
            }
            // 文件气泡
        }else if (sendType === 'file') {

        }
    }

}

function sendMsg() {
    let user = '10.197.24.79';
    let sendType = 'text';
    let sendMsg = $('.text-input').val();
    let isFail = true;
    $('.text-input').val('');
    drawBubble('end', 'right', user, sendMsg, sendType, isFail)
    // todo 向websocket发送请求
    ws.send(sendMsg);
    // 发消息后滚动到最底部
    $('.right-chatRoom').scrollTop($('.right-chatRoom').prop('scrollHeight'))
}

function sendFile() {
    let user = '10.197.24.79';
    let sendType = 'file';
    let isFail = true;
    fileArray.forEach(file => {
        const reader = new FileReader();
        reader.onload = function (event) {
            drawBubble('end', 'right', user, event.target.result, sendType, isFail)
        };
        reader.readAsDataURL(file);
    });
}

function send() {
    // 发送文字
    if ($(this).hasClass('text')) {
        sendMsg();
        // 发送文件
    }else if ($(this).hasClass('file')) {
        sendFile();
    }
}

$('.send-img').click(function () {
    send();
});

$(document).on("keydown", "textarea", function(e) {
    // 取消默认回车换行改为消息发送，添加Ctrl+Enter组合键换行
    if ((e.keyCode === 13 && e.ctrlKey) || (e.keyCode === 13 && e.metaKey)) {
        e.preventDefault();
        var value = $(this).val();
        var start = this.selectionStart;
        var end = this.selectionEnd;
        $(this).val(value.slice(0, start) + "\n" + value.slice(end));
        this.selectionStart = this.selectionEnd = start + 1;
        changeTextareaHeight('.text-input');
    }else if (e.keyCode === 13) {
        e.preventDefault();
        send();
    }
});

$('.right-chatRoom').scroll(function () {
    // todo 向后台拉取历史消息
    let msgArray = new Array();
    msgArray.push({horizontal: 'head',vertical: 'left', user: '10.197.24.79',  sendMsg: '我房里有些好康的！', sendType: 'text', isFail: true})
    msgArray.push({horizontal: 'head',vertical: 'right', user: '10.197.24.79', sendMsg: '开玩笑，我超勇的好不好！', sendType: 'text', isFail: true})
    msgArray.push({horizontal: 'head',vertical: 'left', user: '10.197.24.79', sendMsg: '让我看看你发育正常不正常！', sendType: 'text', isFail: true})
    msgArray.push({horizontal: 'head',vertical: 'right', user: '10.197.24.79', sendMsg: '不要啦，杰哥，你干嘛啊！', sendType: 'text', isFail: true})

    let res = msgArray.reverse();
    if ($(this).scrollTop() === 0) {
        // 添加前滚动条长度
        let beforeScrollLength = $(this).prop('scrollHeight');
        for (let i = 0; i<res.length; i++) {
            drawBubble(res[i].horizontal, res[i].vertical, res[i].user, res[i].sendMsg, res[i].sendType, res[i].isFail)
        }
        // 添加后滚动条长度
        let afterScrollLength = $(this).prop('scrollHeight');
        // 将滚动条滚动到没添加前的位置
        $(this).scrollTop(afterScrollLength - beforeScrollLength)
    }
});

// 模拟接受websocket消息
function acceptMsg(res) {

    // 添加前滚动条位置
    let beforeScrollLength = $('.right-chatRoom').scrollTop();
    // 绘制聊天气泡
    drawBubble('end', 'left', 'ws服务器', res, 'text', true)
    // 添加后滚动条长度
    let afterScrollLength = $('.right-chatRoom').prop('scrollHeight');
    // 滚动范围较小将聚焦到底部
    if (afterScrollLength - beforeScrollLength < 1000) {
        $('.right-chatRoom').scrollTop($('.right-chatRoom').prop('scrollHeight'))
    }


}


var ws = new WebSocket('ws:localhost:10000/websocket/Junior 的聊天室');
ws.onopen = function(evt){
    console.log("on open");
}
ws.onclose = function(evt){
    console.log("on close");
}
ws.onmessage = function(evt){
    acceptMsg(evt.data)
    console.log(evt.data);
}
