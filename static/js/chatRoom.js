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
            dom.remove();
        }else {

        }
    }else {
        dom.remove();
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

function drawBubble(direction, user, sendMsg, sendType, isFail) {
    let $rightChatRoom = $('.right-chatRoom')
    // 气泡在左边显示
    if (direction === 'left') {
        // 文字气泡
        if (sendType === 'text') {
            $rightChatRoom.append(`<div class="bubble">
                                        <div class="bubble-user-box">
                                            <span class="bubble-user">${user}</span>
                                        </div>
                                        <div class="bubble-info-box">
                                            <div class="bubble-info">
                                                <span class="bubble-text"></span>
                                            </div>
                                        </div>
                                    </div>`)
            // 获取最后添加的气泡
            let lastBubble = $($rightChatRoom.children().last()).find('.bubble-text')[0];
            lastBubble.textContent = sendMsg
        // 文件气泡
        }else if (sendType === 'file') {

        }
    // 气泡在右边显示
    }else if (direction === 'right') {
        // 文字气泡
        if (sendType === 'text') {
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
            // 文件气泡
        }else if (sendType === 'file') {

        }
    }

}

$('.send-img').click(function () {
    let user = '10.197.24.79';
    let sendType = 'text';
    let sendMsg = $('.text-input').val();
    let isFail = true;
    drawBubble('right', user, sendMsg, sendType, isFail)
});

