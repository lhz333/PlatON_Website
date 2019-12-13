<template>
    <div class="login">
        <h1>PlatON官网运营管理系统</h1>
        <el-form label-width="60px" :model="loginForm" :rules="rules" ref="loginForm">
            <el-form-item label="账号" prop="loginName">
                <el-input v-model.trim="loginForm.loginName" placeholder="请输入账号" auto-complete="off" @keyup.native.enter="login()" onkeyup="this.value=this.value.replace(/\s+/g,'')"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
                <el-input type="password" v-model.trim="loginForm.password" placeholder="请输入密码" auto-complete="new-password" @keyup.native.enter="login()" onkeyup="this.value=this.value.replace(/\s+/g,'')"></el-input>
            </el-form-item>
        </el-form>
        <el-button type="primary" @click="login">登录</el-button>
    </div>
</template>

<script>
    import r from '../../util/RequestUtil.js'
    import Cookie from '../../util/cookie.js'
    import MD5 from 'crypto-js/md5';
    export default {
        data() {
            return {
                loginForm: {
                    loginName: null,
                    password: null,
                },
                rules: {
                    loginName: [{
                            required: true,
                            message: '请输入账号',
                            trigger: 'blur'
                        },
                        {
                            min: 5,
                            max: 18,
                            message: '长度在 5 到 18 个字符',
                            trigger: 'blur'
                        }
                    ],
                    password: [{
                            required: true,
                            message: '请输入密码',
                            trigger: 'blur'
                        },
                        {
                            min: 6,
                            max: 18,
                            message: '长度在 6 到 18 个字符',
                            trigger: 'blur'
                    }]
                }
            }
        },
        methods: {
            login() {
                var that = this;
                that.$refs.loginForm.validate((valid) => {
                    if(valid) {
                        let param = {
                            loginName: that.loginForm.loginName,
                            password: MD5(that.loginForm.password).toString(),
                        }
                        that.ApiService.login.login(param).then(data => {
                                if(data && data.code === 0) {
                                    setTimeout(() => {
                                        that.$router.push({
                                            path: '/zhnews'
                                        })
                                    },0);
                                    var param = {};
                                    param = {
                                        "id":data.data.id,
                                        "loginName":data.data.loginName,
                                        "password":data.data.password,
                                        "name":encodeURIComponent(data.data.name),
                                        "mobile":data.data.mobile,
                                        "status":data.data.status,
                                        "rePassWord":data.data.rePassWord
                                    }
                                    Cookie.set('userInfo',JSON.stringify(param));
                                }else if(data && data.code === -1){
                                    r.message("登录失败", "warning");
                                }else{
                                    r.message(data.message, "warning");
                                }
                        })
                        .catch(err => {
                            console.log(err);
                        });
                    } else {
                        return false;
                    }
                });
            }
        },
        mounted(){

        }
    }
</script>

<style scoped>
    .login {
        width: 410px;
        background-color: #fff;
        padding: 30px 20px;
        border-radius: 5px;
        position: fixed;
        top: 20%;
        left: 35%;
        text-align: center;
        border: 1px solid #eee;
    }

    h1 {
        font-size: 32px;
        color: #666;
        margin-bottom: 50px;
        text-align: center;
    }
</style>