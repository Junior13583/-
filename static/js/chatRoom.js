var textInput;
var fileInput = `<div class="file-input" tabindex="0"></div>`;
// 全局文件数组，保存用户添加的文件
var fileArray = [];
// 全局已发送文件数组，保存已发送的文件，用于当文件发送失败时进行重新发送
var sendFileArray = [];

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
    $('.show-img').css('visibility', 'hidden');
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
        finalFiles.forEach(file => {
            // 将文件添加到全局数组中
            fileArray.push(file)
            const reader = new FileReader();
            reader.onload = function (event) {
                // 处理图片类文件
                if (file.type.indexOf("image") === 0) {
                    $('.file-input').append(`<div class="file-item">
                                                    <div class="file-del"></div>
                                                    <img src="${event.target.result}" alt="${file.name}" class="file-preview" draggable="false">
                                                    <span class="file-name" title="${file.name}">${file.name}</span>
                                                </div>`);
                } else {
                    $('.file-input').append(`<div class="file-item">
                                                    <div class="file-del"></div>
                                                    <img src="../../static/img/unknown.png" alt="${file.name}" class="file-preview" draggable="false">
                                                    <span class="file-name" title="${file.name}">${file.name}</span>
                                                </div>`)
                }
            };
            reader.readAsDataURL(file);
        });
        $('.file-input').focus();

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
    }
}

$('.text-input').on('input', function () {
    changeTextareaHeight('.text-input')
});

function getConvertSize(fileSize) {
    let convertSize = '';
    let M = 1048576;
    let K = 1024;
    if (fileSize < M) {
        convertSize = `${(fileSize / K).toFixed(2)} KB`;
    } else {
        convertSize = `${(fileSize / M).toFixed(2)} MB`
    }
    return convertSize
}

function insertText(horizontal, vertical, user, sendMsg) {
    let $rightChatRoom = $('.right-chatRoom')
    if (horizontal === 'left') {
        if (vertical === 'head') {
            // 往前添加靠左的气泡
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
        } else if (vertical === 'end'){
            // 往后添加靠左的气泡
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
    }else if (horizontal === 'right') {
        if (vertical === 'head') {
            // 往前添加靠右的气泡
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
        } else if (vertical === 'end'){
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
    }
    return $rightChatRoom.children().last();
}

function insertImage(horizontal, vertical, user, sendMsg) {
    let $rightChatRoom = $('.right-chatRoom')
    if (horizontal === 'left') {
        if (vertical === 'head') {
            // 往前添加靠左的气泡
            $rightChatRoom.prepend(`<div class="bubble">
                                        <div class="bubble-user-box">
                                            <span class="bubble-user">${user}</span>
                                        </div>
                                        <div class="bubble-info-box">
                                            <div class="bubble-info-file">
                                                <img class="bubble-image" src="${sendMsg}" draggable="false" alt="">
                                            </div>
                                        </div>
                                    </div>`);
        }else if (vertical === 'end') {
            // 往后添加靠左的气泡
            $rightChatRoom.append(`<div class="bubble">
                                <div class="bubble-user-box">
                                    <span class="bubble-user">${user}</span>
                                </div>
                                <div class="bubble-info-box">
                                    <div class="bubble-info-file">
                                        <img class="bubble-image" src="${sendMsg}" draggable="false" alt="">
                                    </div>
                                </div>
                            </div>`);
        }
    }else if (horizontal === 'right') {
        if (vertical === 'head') {
            // 往前添加靠右的气泡
            $rightChatRoom.prepend(`<div class="bubble">
                                        <div class="bubble-user-box-right">
                                            <span class="bubble-user">${user}</span>
                                        </div>
                                        <div class="bubble-info-box-right">
                                            <div class="bubble-info-right-file">
                                                <img class="bubble-image" src="${sendMsg}" draggable="false" alt="">
                                            </div>
                                        </div>
                                    </div>`);
        }else if (vertical === 'end') {
            // 往后添加靠右的气泡
            $rightChatRoom.append(`<div class="bubble">
                                    <div class="bubble-user-box-right">
                                        <span class="bubble-user">${user}</span>
                                    </div>
                                    <div class="bubble-info-box-right">
                                        <div class="bubble-info-right-file">
                                            <img class="bubble-image" src="${sendMsg}" draggable="false" alt="">
                                        </div>
                                    </div>
                                </div>`)
        }
    }
    return $rightChatRoom.children().last();
}

function insertOthers(horizontal, vertical, user, sendMsg, file) {
    let fileSize = file.size;
    let convertSize = getConvertSize(file.size);

    let $rightChatRoom = $('.right-chatRoom');
    if (horizontal === 'left') {
        if (vertical === 'head') {
            // 往前添加靠左的气泡
            $rightChatRoom.prepend(`<div class="bubble">
                                        <div class="bubble-user-box">
                                            <span class="bubble-user">${user}</span>
                                        </div>
                                        <div class="bubble-info-box">
                                            <div class="bubble-info-file">
                                                <div class="bubble-file">
                                                    <div class="bubble-file-info">
                                                        <div class="bubble-file-name" title="${file.name}">${file.name}</div>
                                                        <div class="bubble-file-size">${convertSize}</div>
                                                    </div>
                                                    <img class="bubble-file-img" src="${sendMsg}" alt="" draggable="false">
                                                </div>
                                            </div>
                                        </div>
                                    </div>`);
        }else if (vertical === 'end') {
            // 往后添加靠右的气泡
            $rightChatRoom.append(`<div class="bubble">
                                    <div class="bubble-user-box">
                                        <span class="bubble-user">${user}</span>
                                    </div>
                                    <div class="bubble-info-box">
                                        <div class="bubble-info-file">
                                            <div class="bubble-file">
                                                <div class="bubble-file-info">
                                                    <div class="bubble-file-name" title="${file.name}">${file.name}</div>
                                                    <div class="bubble-file-size">${convertSize}</div>
                                                </div>
                                                <img class="bubble-file-img" src="${sendMsg}" alt="" draggable="false">
                                            </div>
                                        </div>
                                    </div>
                                </div>`)
        }

    } else if (horizontal === 'right') {
        if (vertical === 'head') {
            // 往前添加靠右的气泡
            $rightChatRoom.prepend(`<div class="bubble">
                                    <div class="bubble-user-box-right">
                                        <span class="bubble-user">${user}</span>
                                    </div>
                                    <div class="bubble-info-box-right">
                                        <div class="bubble-info-right-file">
                                            <div class="bubble-file">
                                                <div class="bubble-file-info">
                                                    <div class="bubble-file-name" title="${file.name}">${file.name}</div>
                                                    <div class="bubble-file-size">${convertSize}</div>
                                                </div>
                                                <img class="bubble-file-img" src="${sendMsg}" alt="" draggable="false">
                                            </div>
                                        </div>
                                    </div>
                                </div>`)

        }else if (vertical === 'end') {
            // 往后添加靠右的气泡
            $rightChatRoom.append(`<div class="bubble">
                                    <div class="bubble-user-box-right">
                                        <span class="bubble-user">${user}</span>
                                    </div>
                                    <div class="bubble-info-box-right">
                                        <div class="bubble-info-right-file">
                                            <div class="bubble-file">
                                                <div class="bubble-file-info">
                                                    <div class="bubble-file-name" title="${file.name}">${file.name}</div>
                                                    <div class="bubble-file-size">${convertSize}</div>
                                                </div>
                                                <img class="bubble-file-img" src="${sendMsg}" alt="" draggable="false">
                                            </div>
                                        </div>
                                    </div>
                                </div>`)
        }
    }
    return $rightChatRoom.children().last();
}

/**
 *
 * @param horizontal:  决定气泡水平显示位置
 * @param vertical:  决定气泡垂直添加方向，从头添加或者从尾部添加
 * @param user:  用户名
 * @param sendMsg:  消息内容
 * @param sendType:  消息类型
 * @param file:  文件对象
 * @return:
 * @Author: Junior
 * @Date: 2023/6/6
 */
function drawBubble(horizontal, vertical, user, sendMsg, sendType, file) {
    let thisBubble;
    if (sendType === 'text') {
        thisBubble = insertText(horizontal, vertical, user, sendMsg);
    }else if (sendType === 'image') {
        thisBubble = insertImage(horizontal, vertical, user, sendMsg);
    }else if (sendType === 'others') {
        thisBubble = insertOthers(horizontal, vertical, user, sendMsg, file)
    }
    return thisBubble;
}

$(document).on('click', '.bubble-image', function () {
    // 显示大图
    let imgSrc = $(this).attr('src')
    $('.show-img').attr('src', imgSrc)
    $('.overlay').css('z-index', '1');
    $('.show-img').css('visibility', 'visible');
});

function isSending(dom) {
    // 移除发送失败标志
    $(dom).find('.bubble-error').remove();
    // 添加正在发送标志
    $(dom).find('.bubble-info-box-right').prepend(`<img src="../../static/img/loading.png" class="bubble-loading">`)
}

function sendSuccess(dom) {
    // 移除正在发送标志
    $(dom).find('.bubble-loading').remove();
}
function sendError(dom) {
    // 移除正在发送标志
    $(dom).find('.bubble-loading').remove();
    // 添加发送失败标志
    $(dom).find('.bubble-info-box-right').prepend(`<img src="../../static/img/error.png" class="bubble-error">`)
}

function sendMsg() {
    let user = $('.user-ip').text();
    let sendType = 'text';
    let sendMsg = $('.text-input').val();
    let file = '';
    $('.text-input').val('');
    drawBubble('right', 'end', user, sendMsg, sendType, file)
    // todo 向websocket发送请求
    ws.send(sendMsg, function (error) {
        console.log(`${sendMsg}发送失败`)
    });
    // 发消息后滚动到最底部
    $('.right-chatRoom').scrollTop($('.right-chatRoom').prop('scrollHeight'))
}

function sendFile() {
    let index = 0;
    let user = $('.user-ip').text();
    let bubbles = [];
    fileArray.forEach(file => {
        const reader = new FileReader();
        reader.onload = function (event) {
            if (file.type.indexOf("image") === 0) {
                bubbles.push(drawBubble('right', 'end', user, event.target.result, 'image', ''));
            } else {
                bubbles.push(drawBubble('right', 'end', user, '../../static/img/unknown.png', 'others', file));
            }
            // 添加正在发送标志
            isSending(bubbles[index]);
            // 将文件添加到已发送文件数组中
            if (!sendFileArray.find(f => f.name === file.name && f.size === file.size)) {
                sendFileArray.push(file);
            }

            // todo ajax发送文件，发送成功去除正在发送标志，发送失败添加发送失败标志
            let fromData = new FormData();
            fromData.append('index', index);
            fromData.append('file', file);
            $.ajax({
                url: 'http://10.197.24.79:8000/',
                type: 'post',
                data: fromData,
                contentType: false,
                processData: false,
                crossDomain: true,
                success: function (res) {
                    sendSuccess(bubbles[index]);
                    index++;
                },
                error: function (res) {
                    sendError(bubbles[index]);
                    index++;
                }
            });

            // 发消息后滚动到最底部
            $('.right-chatRoom').scrollTop($('.right-chatRoom').prop('scrollHeight'));
        };
        reader.readAsDataURL(file);
    });
    // 点击发送之后，将输入的文件清空
    fileArray = [];
    $('.file-input').empty();
}

function send() {
    // 发送文字
    if ($('.send-img').hasClass('text')) {
        sendMsg();
        // 发送文件
    }else if ($('.send-img').hasClass('file')) {
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

$(document).on('keydown', '.file-input', function (e) {
    if (e.keyCode === 13) {
        e.preventDefault();
        send();
    }
})

$('.right-chatRoom').scroll(function () {
    // todo 向后台拉取历史消息
    let msgArray = new Array();
    msgArray.push({horizontal: 'left',vertical: 'head', user: '10.197.24.79',  sendMsg: '<div class="send-img text"></div>', sendType: 'text', file: ''})
    msgArray.push({horizontal: 'right',vertical: 'head', user: '10.197.24.79', sendMsg: '开玩笑，我超勇的好不好！', sendType: 'text', file: ''})
    msgArray.push({horizontal: 'left',vertical: 'head', user: '10.197.24.79', sendMsg: '让我看看你发育正常不正常！', sendType: 'text', file: ''})
    msgArray.push({horizontal: 'right',vertical: 'head', user: '10.197.24.79', sendMsg: '不要啦，杰哥，你干嘛啊！', sendType: 'text', file: ''})

    let res = msgArray.reverse();
    if ($(this).scrollTop() === 0) {
        // 添加前滚动条长度
        let beforeScrollLength = $(this).prop('scrollHeight');
        for (let i = 0; i<res.length; i++) {
            drawBubble(res[i].horizontal, res[i].vertical, res[i].user, res[i].sendMsg, res[i].sendType, res[i].file)
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
    drawBubble('left', 'end', 'ws服务器', res, 'text', '')
    // 添加后滚动条长度
    let afterScrollLength = $('.right-chatRoom').prop('scrollHeight');
    // 滚动范围较小将聚焦到底部
    if (afterScrollLength - beforeScrollLength < 1000) {
        $('.right-chatRoom').scrollTop($('.right-chatRoom').prop('scrollHeight'))
    }


}


let ws;

function connect() {
    ws = new WebSocket('ws:localhost:10000/websocket/Junior 的聊天室');

    ws.onopen = function() {
        console.log('连接已经建立');
    };

    ws.onmessage = function(evt) {
        acceptMsg(evt.data)
        console.log(evt.data);
    };

    ws.onerror = function() {
        console.log('onerror')
    };

    ws.onclose = function() {
        console.log("on close");

        setTimeout(function() {
            console.log('正在尝试重新连接...');
            connect();
        }, 1000);
    };
}

// connect();
