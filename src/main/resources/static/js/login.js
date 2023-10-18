/**
 * Variables
 */
const signupButton = document.getElementById('signup-button'),
    loginButton = document.getElementById('login-button'),
    userForms = document.getElementById('user_options-forms'),
    registerForm = document.getElementById('register_form'),
    loginForm = document.getElementById('login_form')

cocoMessage.config({
    duration: 2000,
});

/**
 * Add event listener to the "Sign Up" button
 */
signupButton.addEventListener('click', () => {
    userForms.classList.remove('bounceRight')
    userForms.classList.add('bounceLeft')
}, false)

/**
 * Add event listener to the "Login" button
 */
loginButton.addEventListener('click', () => {
    userForms.classList.remove('bounceLeft')
    userForms.classList.add('bounceRight')
}, false)

registerForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    let name = registerForm.name.value;
    let email = registerForm.email.value;
    let password = registerForm.password.value;

    let formData = new FormData();
    formData.append("name", name);
    formData.append("email", email);
    formData.append("password", password);

    try {
        const response = await fetch('/register', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            const data = await response.json();
            if (data.code === 200) {
                cocoMessage.success('2000', data.data);
                // 注册成功后，将 email， password 复制到登录框，同时切换到登录部分
                loginForm.email.value = email;
                loginForm.password.value = password;
                userForms.classList.remove('bounceLeft')
                userForms.classList.add('bounceRight')

            } else {
                cocoMessage.error('2000', data.msg);
            }

        } else {
            cocoMessage.error('2000', '请求失败');
        }
    } catch (error) {
        cocoMessage.error('2000', '请求发生错误：' + error);
    }
}, false);

loginForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    let email = loginForm.email.value;
    let password = loginForm.password.value;

    let formData = new FormData();
    formData.append("email", email);
    formData.append("password", password);

    try {
        const response = await fetch('/login', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            const data = await response.json();
            if (data.code === 200) {
                cocoMessage.success('2000', data.data);
                window.location.href = "/";

            } else {
                cocoMessage.error('2000', data.msg);
            }

        } else {
            cocoMessage.error('2000', '请求失败');
        }
    } catch (error) {
        cocoMessage.error('2000', '请求发生错误：' + error);
    }
}, false);
