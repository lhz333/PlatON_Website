<template>
    <div class="main">
        <!--头部-->
        <header class="header">
            <div class="head-left">
                PlatON官网运营管理系统
            </div>
            <div class="head-right">
                <div class="right-content">
                    <span>{{loginName}}，你好</span>
                    <el-button class="ml50 mr20" @click="modifyPassword" type="text">修改密码</el-button>
                    <el-button type="text" @click="loginOut">退出</el-button>
                </div>
            </div>
        </header>
        <!--侧边栏-->
        <article class="content">
            <nav class="content-nav">
                <el-menu :default-active="defaultActive" ref="menu" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose" background-color="#324157" text-color="#fff" :unique-opened="true" :router="true">
                    <template v-for="(item,index) in datamenulist">
                        <el-submenu v-if="item.children" :index="item.url+index" :key="item.url" >
                            <template slot="title">
                                <i :class="item.icon"></i>
                                <span>{{item.name}}</span>
                            </template>
                            <el-menu-item v-for="(ite,i) in item.children" :index="ite.url" :key="i">{{ite.name}}</el-menu-item>
                        </el-submenu>
                        <template v-if="!item.children">
                            <el-menu-item :index="item.url" :key="item.url">
                                <i :class="item.icon"></i>
                                <span slot="title">{{item.name}}</span>
                            </el-menu-item>
                        </template>
                    </template>
                </el-menu>
            </nav>
            <section class="content-content">
                <router-view></router-view>
            </section>
        </article>
        <!--修改密码-->
        <el-dialog title="修改密码" :visible.sync="modifyPadDialog" width="30%" :before-close="resetForm">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="80px">
                <el-form-item label="原密码" prop="oldpassword">
                    <el-input type="password" v-model="ruleForm.oldpassword" placeholder="请输入原密码" auto-complete="off" :clearable="true" oncopy="return false;" onpaste="return false;" oncut="return false;" onkeyup="this.value=this.value.replace(/\s+/g,'')"></el-input>
                </el-form-item>
                <el-form-item label="新密码" prop="newpassword">
                    <el-input type="password" v-model="ruleForm.newpassword" placeholder="请输入新密码" auto-complete="off" :clearable="true" oncopy="return false;" onpaste="return false;" oncut="return false;" onkeyup="this.value=this.value.replace(/\s+/g,'')"></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="renewpassword">
                    <el-input type="password" v-model="ruleForm.renewpassword" placeholder="请再次输入新密码" auto-complete="off" :clearable="true" onpaste="return false;" oncopy="return false;" oncut="return false;" onkeyup="this.value=this.value.replace(/\s+/g,'')"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm">取 消</el-button>
                <el-button type="primary" @click="submitForm">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import r from '../util/RequestUtil.js'
    import Cookie from '../util/cookie.js'
    import MD5 from 'crypto-js/md5';
    export default {
        data() {
            return {
                datamenulist: [],
                loginName: 'hello',
                modifyPadDialog: false,
                defaultActive:'/zhnews',
                ruleForm: {
                    oldpassword: '',
                    newpassword: '',
                    renewpassword: '',
                },
                rules: {
                    oldpassword: [{
                        required: true,
                        validator: (rule, value, callback) => {
                            if(!value) {
                                return callback(new Error('请输入原密码'));
                            } else if(value.length<6 || value.length>18) {
                                return callback(new Error('密码长度在6-18位之间'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'blur'
                    }],
                    newpassword: [{
                        required: true,
                        validator: (rule, value, callback) => {
                            if(!value) {
                                return callback(new Error('请输入新密码'));
                            } else if(value.length<6 || value.length>18) {
                                return callback(new Error('密码长度在6-18位之间'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'blur'
                    }],
                    renewpassword: [{
                        required: true,
                        validator: (rule, value, callback) => {
//                          let reg = /^[A-Za-z0-9]{6,18}$/;
                            if(!value) {
                                return callback(new Error('请再次确认密码'));
                            } else if(value !== this.ruleForm.newpassword) {
                                callback(new Error('两次输入密码不一致!'));
                            } else if(value.length<6 || value.length>18) {
                                return callback(new Error('密码长度在6-18位之间'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'blur'
                    }],
                }
            }
        },
        watch: {
            '$route' (to) {
                if(to.path==='/receiveRecords'){
                    this.$refs.menu.activeIndex = to.path
                }
            }
        },
        methods: {
            //修改密码弹框
            modifyPassword() {
                this.modifyPadDialog = true;
            },
            //提交
            submitForm() {
                var that = this;
                that.$refs.ruleForm.validate((valid) => {
                    if(valid) {
                        let param = {
                            password: MD5(that.ruleForm.oldpassword).toString(),
                            rePassWord: MD5(that.ruleForm.renewpassword).toString()
                        }
                        that.ApiService.login.uptUser(param).then(data => {
                            if(data && data.code === 0) {
                                r.message('修改成功', "success");
                                that.resetForm();
//                              that.$router.replace('login');
//                              Cookie.del('userInfo');
                            } else if(data && data.code === 1){
                                r.message(data.message, "warning");
                            }else{
                                r.message(data.message, "warning");
                            }
                        })
                        .catch(err => {
                            console.log(err);
                        });
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            //取消  重置
            resetForm() {
                this.modifyPadDialog = false;
                this.$refs.ruleForm.resetFields();
            },
            handleOpen(key, keyPath) {
            //  console.log(key, keyPath);
            },
            handleClose(key, keyPath) {
            //  console.log(key, keyPath);
            },
            //从url截取数据
            getQueryStr(sArgName) {
                let args = window.location.href.split("?");
                let retval = "";
                if(args[0] == window.location.href) /*参数为空*/ {
                    return retval; /*无需做任何处理*/
                }
                let str = args[1];
                args = str.split("&");
                for(let i = 0; i < args.length; i++) {
                    str = args[i];
                    let arg = str.split("=");
                    if(arg.length <= 1) continue;
                    if(arg[0] == sArgName) retval = arg[1];
                }
                return retval;
            },
            getUrl(){
                let _this = this;
                let currentUrl = window.location.href;
                let currentEnd = currentUrl.split('#')[1];
                let lanuageUrl = _this.getQueryStr('language');
                //新建页面
                if(lanuageUrl=='zh' && currentEnd.indexOf("releaseNews") != -1){
                    currentEnd = '/zhnews';
                }else if(lanuageUrl=='en' && currentEnd.indexOf("releaseNews") != -1){
                    currentEnd = '/ennews';
                }else if(lanuageUrl=='zh'&&currentEnd.indexOf("newfaq") != -1){
                    currentEnd = '/zhFaq';
                }else if(lanuageUrl=='en'&&currentEnd.indexOf("newfaq") != -1){
                    currentEnd = '/enFaq';
                }else if(currentEnd.indexOf("buildMedia") != -1){
                    currentEnd = '/pressCenter';
                }else if(currentEnd.indexOf("newChannel") != -1){
                    currentEnd = '/channelManage';
                }
                _this.defaultActive = currentEnd;
            },
            //退出登录
            loginOut() {
                var that = this;
                that.$confirm('是否确认退出登录?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    that.ApiService.login.logout().then(data => {
                        if(data && data.code === 0) {
                            r.message("成功退出登录", "success");
                            Cookie.del('userInfo');
                            that.$router.replace('login');
                        } else {
                            //r.message("", "warning");
                        }
                    })
                    .catch(err => {
                        console.log(err);
                    });
                }).catch(() => {

                });
            }
        },
        created(){
            this.getUrl();
        },
        mounted() {
            let _this = this;
            let userInfo = JSON.parse(Cookie.get('userInfo'));
            this.loginName = decodeURIComponent(userInfo.name);
            //获取菜单
            _this.ApiService.login.findMenu().then(data => {
                if(data && data.code === 0) {
                    _this.datamenulist = data.data;
                } else if(data.code === 9999) {
                    r.message(data.data, "warning");
                    _this.$router.replace('login');
                    Cookie.del('userInfo');
                } else {
                    //r.message("", "warning");
                }
            })
            .catch(err => {
                console.log(err);
            });
        }
    }
</script>

<style type="text/css">
    .el-menu-vertical-demo .el-submenu .el-menu-item {
        min-width: 175px;
        text-align: center;
    }
</style>
<style scoped="scoped">
    .ml50 {
        margin-left: 50px;
    }

    .mr20 {
        margin-right: 20px;
    }

    .main {
        min-width: 1200px;
    }

    .right-content {
        position: absolute;
        right: 60px;
        top: 0;
    }
</style>