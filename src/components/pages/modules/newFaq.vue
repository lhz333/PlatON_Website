<template>
    <div>
        <!--head-->
        <div class="content-c-breadcrumb">
            <el-breadcrumb separator="-">
                <el-breadcrumb-item>FAQ管理</el-breadcrumb-item>
                <el-breadcrumb-item>新增FAQ</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <!--操作按钮-->
        <div class="btns">
            <el-button @click="back">返回</el-button>
            <el-button type="primary" @click="save">保存</el-button>
        </div>
        <!--表单-->
        <div class="fill-form">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="160px" class="demo-ruleForm">
                <h3 class="text-center mb20">中文版本</h3>
                <el-form-item label="问题（中文）" prop="problemCn">
                    <el-input v-model.trim="ruleForm.problemCn" placeholder="请输入问题内容"></el-input>
                </el-form-item>
                <el-form-item label="回答（中文）" prop="replyCn">
                    <el-input type="textarea" :rows="5" placeholder="请输入问题回答" v-model.trim="ruleForm.replyCn"></el-input>
                </el-form-item>
                <h3 class="text-center mb20">英文版本</h3>
                <el-form-item label="问题（英文）" prop="problemEn">
                    <el-input v-model.trim="ruleForm.problemEn" placeholder="请输入问题内容"></el-input>
                </el-form-item>
                <el-form-item label="回答（英文）" prop="replyEn">
                    <el-input type="textarea" :rows="5" placeholder="请输入问题回答" v-model.trim="ruleForm.replyEn"></el-input>
                </el-form-item>
            </el-form>
        </div>
        <!--返回-->
        <el-dialog title="离开确认" :visible.sync="backDialog" width="30%" center>
            <span>未保存，是否保存？</span>
            <span slot="footer" class="dialog-footer">
            <el-button @click="backDialog = false">取消</el-button>
            <el-button @click="notSave">不保存</el-button>
            <el-button type="primary" @click="save">保存</el-button>
          </span>
        </el-dialog>
    </div>
</template>

<script>
    import r from '../../../util/RequestUtil.js'
    export default {
        data() {
            return {
                backDialog: false,
                validateValue:false,
                selected:null,
                ruleForm: {
                    problemCn: null,
                    replyCn:null,
                    problemEn:null,
                    replyEn:null,
                    complete:0
                },
                rules: {
                    problemCn: [{
                        required: true,
                        validator: (rule, value, callback) => {
                            if(!value) {
                                return callback(new Error('请输入问题内容(中文)'));
                            } else if(value.length>50){
                                return callback(new Error('问题最多为50个字符!'));
                            }else {
                                callback();
                            }
                        },
                        trigger: 'blur'
                    }],
                    replyCn: [{
                        required: true,
                        validator: (rule, value, callback) => {
                            if(!value) {
                                return callback(new Error('请输入回答内容(中文)'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'blur'
                    }],
                    problemEn: [{
                        required: true,
                        validator: (rule, value, callback) => {
                            if(!value) {
                                return callback(new Error('请输入问题内容(英文)'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'blur'
                    }],
                    replyEn: [{
                        required: true,
                        validator: (rule, value, callback) => {
                            if(!value) {
                                return callback(new Error('请输入回答内容(英文)'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'blur'
                    }]
                }
            }
        },
        methods: {
            //返回
            back() {
                this.validateData();
                if(this.validateValue){
                    this.backDialog = true;
                }else{
                    this.$router.push({
                        path: '/faq'
                    })
                }
            },
            validateData(){
                if((this.ruleForm.problemCn == null || this.ruleForm.problemCn =='') && (this.ruleForm.replyCn == null || this.ruleForm.replyCn =='') && (this.ruleForm.problemEn == null || this.ruleForm.problemEn == '') && (this.ruleForm.replyEn == null || this.ruleForm.replyEn == '')){
                    this.validateValue = false;
                }
                else{
                    this.validateValue = true;
                }
            },
            //保存
            save() {
                if(this.ruleForm.problemCn!==null && this.ruleForm.problemCn.length>50){
                    r.message('中文问题标题超过了50个字符', "warning");
                    return;
                }
                if(this.ruleForm.replyCn!==null && this.ruleForm.replyCn.length>3000){
                    r.message('中文问题标题超过了3000个字符', "warning");
                    return;
                }
                if(this.ruleForm.problemEn!==null && this.ruleForm.problemEn.length>100){
                    r.message('英文问题标题超过了100个字符', "warning");
                    return;
                }
                if(this.ruleForm.replyEn!==null && this.ruleForm.replyEn.length>3000){
                    r.message('英文问题标题超过了3000个字符', "warning");
                    return;
                }
                this.$refs.ruleForm.validate((valid) => {
                    if(valid) {
                        this.ruleForm.complete = 1;
//                      alert('submit!');
                    } else {
                        this.ruleForm.complete = 0;
//                      r.message('请确认信息是否填写完整', "warning");
                        return false;
                    }
                });
                this.validateData();
                if(this.validateValue){
                    let _this = this;
                    if(this.selected!==null && this.selected!==undefined){
                        _this.ApiService.faq.uptFaq(_this.ruleForm).then(data=>{
                            if(data && data.code === 0) {
                                r.message('修改成功', "success");
                                _this.$router.push({
                                    path: '/faq'
                                })
                            } else{
                                r.message(data.message, "warning");
                            }
                        })
                        .catch(err => {
                            console.log(err);
                        });
                    }else{
                        _this.ApiService.faq.insFaq(_this.ruleForm).then(data=>{
                            if(data && data.code === 0) {
                                r.message('新增成功', "success");
                                _this.$router.push({
                                    path: '/faq'
                                })
                            } else{
                                r.message(data.message, "warning");
                            }
                        })
                        .catch(err => {
                            console.log(err);
                        });
                    }
                }else{
                    r.message('请填写相关问题及回答', "warning");
                }
            },

            //不保存
            notSave() {
                this.backDialog = false;
                this.$refs.ruleForm.resetFields();
                this.$router.push({
                    path: '/faq'
                })
            }
        },
        mounted() {
            let _this = this;
            let query = this.$route.query;
            this.selected = query.qId;
            if(this.selected!==null && this.selected!==undefined){
                let param ={id:this.selected};
                _this.ApiService.faq.findPlatFaqById(param).then(data=>{
                    if(data && data.code === 0) {
                        _this.ruleForm = data.data;
                    } else{
                        r.message(data.message, "warning");
                    }
                })
                .catch(err => {
                    console.log(err);
                });
            }
        }
    }
</script>

<style scoped="scoped">
    
</style>