<template>
    <div v-loading.body="loading">
        <!--head-->
        <div class="content-c-breadcrumb">
            <el-breadcrumb separator="-">
                <el-breadcrumb-item>渠道管理</el-breadcrumb-item>
                <el-breadcrumb-item>{{pageTitle}}渠道活动</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="fill-form" >
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="180px" class="demo-ruleForm">
                <el-form-item label="渠道名称：" prop="channelName">
                    <el-input :disabled="disabled" v-model.trim="ruleForm.channelName" maxlength="20"></el-input>
                </el-form-item>
                <el-form-item label="活动主题：" prop="name">
                   <el-input :disabled="disabled" v-model.trim="ruleForm.name" maxlength="20"></el-input>
                </el-form-item>
                <el-form-item label="每次发放数量 (Energon)：" prop="sendQuantity">
                    <el-input :disabled="disabled" v-model.trim="ruleForm.sendQuantity" @keyup.native="onlyNonNegative" @focus="clearInput" maxlength="20"></el-input>
                </el-form-item>
                <el-form-item label="规则：" prop="ruleType">
                     <el-select style="width:100%;" :disabled="disabled" v-model="ruleForm.ruleType" placeholder="请选择">
                        <el-option
                        v-for="item in ruleOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="可领取总次数：" prop="count">
                    <el-input v-model.trim="ruleForm.count" @keyup.native="blurNum" @blur="blurNum" maxlength="20" :clearable="true"></el-input>
                </el-form-item>
                <el-form-item label="每日限领量: " prop="dayQuantity">
                    <el-input v-model.trim="ruleForm.dayQuantity" @keyup.native="onlyNonNegative2" maxlength="20" @focus="clearInput2" :clearable="true"></el-input>
                </el-form-item>
                <el-form-item label="有效期：" prop="effectiveTime">
                    <el-date-picker type="date" :editable="false" v-model.trim="ruleForm.effectiveTime" style="width:100%;" :picker-options="pickerOptions0"></el-date-picker>
                </el-form-item>
            </el-form>
        </div>

        <!--操作按钮-->
        <div class="btns">
            <el-button @click="cancel">取消</el-button>
            <el-button type="primary" @click="submit">提交</el-button>
        </div>
    </div>
</template>

<script>
import r from '@/util/RequestUtil.js'
export default {
    data(){
        return {
            pageTitle:"新增",
            loading:false,
            ruleForm: {
                channelName: null,
                name:null,
                sendQuantity:0,
                ruleType:1,
                effectiveTime:null,
                count:null,
                dayQuantity:null
            },
            channelId:null,
            disabled:false,
            ruleOptions:[{
                value: 1,
                label: '每个钱包24小时只能领一次'
            },{
                value: 2,
                label: '每个钱包只能领一次'
            },{
                value: 3,
                label: '不限'
            }],
            pickerOptions0: {
                disabledDate(time) {
                    return time.getTime() < Date.now() - 8.64e7;
                }
            },  
            rules: {
                channelName: [{
                    required: true,
                    validator: (rule, value, callback) => {
                        if(!value) {
                            return callback(new Error('请输入渠道名称'));
                        }else if(value.length>20){
                            return callback(new Error('渠道名称最多为20个字符!'));
                        } else {
                            callback();
                        }
                    },
                    trigger: 'change'
                }],
                name: [{
                    validator: (rule, value, callback) => {
                        if(value!==null && value.length>20) {
                            return callback(new Error('活动名称最多为20个字符!'));
                        } else {
                            callback();
                        }
                    },
                    trigger: 'change'
                }],
                sendQuantity:[{
                    validator: (rule, value, callback) => {
                        if(value===null) {
                            return callback(new Error('每次发放数量不能为空!'));
                        } else {
                            callback();
                        }
                    },
                    trigger: 'change'
                }],
                count:[{
                    validator: (rule, value, callback) => {
                        callback();
                    },
                    trigger: 'blur'
                }],
                dayQuantity:[{
                    validator: (rule, value, callback) => {
                        if((value*100) % (this.ruleForm.sendQuantity*100)!==0){
                            return callback(new Error('请输入每次发放数量的倍数'));
                        }else{
                            callback();
                        }
                    },
                    trigger: 'blur'
                }],
                ruleType: [{
                    required: true,
                    validator: (rule, value, callback) => {
                        if(!value) {
                            return callback(new Error('请选择规则'));
                        }else {
                            callback();
                        }
                    },
                    trigger: 'change'
                }],
                effectiveTime:[{
                    validator: (rule, value, callback) => {
                        callback();
                    },
                    trigger: 'blur'
                }]
            }
        }
    },
    methods:{
        //获取单个渠道活动信息
        getData(){
            let _this = this;
            let param = {
                id:_this.channelId
            }
            _this.loading = true;
            _this.ApiService.testcoin.findChannelById(param).then(data => {
                _this.loading = false;
                if (data && data.code === 0) {
                    _this.ruleForm = data.data;
                } else {
                    r.message(data.message, "warning");
                }
                })
                .catch(err => {
                    _this.loading = false;
                    console.log(err);
                });
        },
        //取消
        cancel(){
            this.$refs.ruleForm.resetFields();
            this.$router.push({
                path: '/channelManage'
            })
        },
        //提交
        submit(){
            let _this = this;
            _this.$refs.ruleForm.validate((valid) => {
                if(valid) {
                    if(_this.channelId === null || _this.channelId == undefined) {
                        if(_this.ruleForm.sendQuantity===null){
                            r.message("每次发放数量不能为空", "warning");
                            return false;
                        }
                        _this.addChannelInfo();
                    } else {
                        _this.updateChannelInfo();
                    }
                }else{
                    r.message("页面必填字段不能为空", "warning");
                }
            });
        },
        //新增渠道活动
        addChannelInfo(){
            let _this = this;
            _this.loading = true;
            _this.ApiService.testcoin.addChannelInfo(this.ruleForm).then(data => {
                _this.loading = false;
                if (data && data.code === 0) {
                    r.message('新增成功', "success");
                    _this.$router.push({
                        path: '/channelManage'
                    })
                } else {
                    r.message(data.message, "warning");
                }
                })
                .catch(err => {
                    _this.loading = false;
                    console.log(err);
                });
        },
        //编辑渠道活动
        updateChannelInfo(){
            let _this = this;
            _this.loading = true;
            _this.ApiService.testcoin.uptChannelInfo(this.ruleForm).then(data => {
                _this.loading = false;
                if (data && data.code === 0) {
                    r.message('修改成功', "success");
                    _this.$router.push({
                        path: '/channelManage'
                    })
                } else {
                    r.message(data.message, "warning");
                }
                })
                .catch(err => {
                    _this.loading = false;
                    console.log(err);
                });
        },
        //每次发放次数 Energon单位
        onlyNonNegative() {
            this.ruleForm.sendQuantity = this.onlyNonNegativeFc(this.ruleForm.sendQuantity);
        },
        //每日限领量 
        onlyNonNegative2(){
            if(this.ruleForm.sendQuantity===0 || this.ruleForm.sendQuantity==='0'){
                r.message('每次发放数量不能为0', "error");
                this.ruleForm.dayQuantity = null;
                return false;
            }
            this.ruleForm.dayQuantity = this.onlyNonNegativeFc(this.ruleForm.dayQuantity);
        },
        onlyNonNegativeFc(value) {
            if(value!==null && value!=='' &&value.length<=20){
                value = value.replace(/[^\d.]/g, ""); //清除“数字”和“.”以外的字符     
                value = value.replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的     
                value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
                value = value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); //只能输入两个小数
                return value;
            }
        },
        // 可领取次数
        blurNum(){
            if(this.ruleForm.count!==null && this.ruleForm.count!==''){
                this.ruleForm.count=this.ruleForm.count.replace(/\D/g,'')
            }
        },
        //清除可领取次数
        clearInput(){
            if(this.ruleForm.sendQuantity===0 || this.ruleForm.sendQuantity==='0'){
                this.ruleForm.sendQuantity = null;
            }
        },
        clearInput2(){
            if(this.ruleForm.dayQuantity===0 || this.ruleForm.dayQuantity==='0'){
                this.ruleForm.dayQuantity = null;
            }
        }
    },
    mounted(){
        let _this = this;
        let query = this.$route.query;
        _this.channelId = query.channelId;

        if(_this.channelId!==null && _this.channelId != undefined){
            _this.disabled = true;
            _this.pageTitle="编辑";
            _this.getData();
        }else{
            _this.pageTitle="新增";
        }

    }
}
</script>

<style scoped>
.fill-form{
    margin:5% auto 0;
    width: 60%;
}
.btns{
    margin-top:50px;
    text-align: center;
}
</style>


