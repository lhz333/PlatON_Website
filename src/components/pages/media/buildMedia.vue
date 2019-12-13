<template>
    <div>
        <!--head-->
        <div class="content-c-breadcrumb">
            <el-breadcrumb separator="-">
                <el-breadcrumb-item>媒体报道</el-breadcrumb-item>
                <el-breadcrumb-item>新增报道</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <!--操作按钮-->
        <div class="btns">
            <el-button @click="back">返回</el-button>
            <el-button type="primary" @click="save">保存</el-button>
        </div>
        <div class="fill-form">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="160px" class="demo-ruleForm">
                <el-form-item label="媒体名称" prop="name">
                    <el-input v-model.trim="ruleForm.name" placeholder="请输入媒体名称"></el-input>
                </el-form-item>
                <el-form-item label="媒体Logo" prop="logoUrl">
                    <el-upload ref="imgUpload" name="imgUrl" class="upload-demo" :action="serverUrl" :on-preview="handlePreview" :file-list="imgUrlList" list-type="picture" :before-upload="beforeAvatarUpload" :on-success="imgSuccess" :before-remove="imgUrlRemove">
                        <el-button slot="trigger" size="small" type="primary">点击上传</el-button>
                        <div slot="tip" class="el-upload__tip">请上传尺寸不超过205x60的图片，格式为png/jpg，且不超过50kb</div>
                    </el-upload>
                </el-form-item>
                <el-form-item label="文章发布时间" prop="reTime">
                    <el-date-picker name="reTime" type="date" v-model.trim="ruleForm.reTime" style="width:100%;"></el-date-picker>
                </el-form-item>
                <el-form-item label="文章标题" prop="title">
                    <el-input v-model.trim="ruleForm.title" placeholder="请输入文章标题"></el-input>
                </el-form-item>
                <el-form-item label="文章链接" prop="link">
                    <el-input v-model.trim="ruleForm.link" placeholder="请输入文章链接"></el-input>
                </el-form-item>
                <el-form-item label="文章语言" prop="language">
                    <el-radio-group v-model="ruleForm.language">
                    <el-radio border label="zh">中文</el-radio>
                    <el-radio border label="en">英文</el-radio>
                    </el-radio-group>
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
import r from '@/util/RequestUtil.js'
export default {
    data(){
        return {
            backDialog:false,
            // 这里写你要上传的图片服务器地址
            serverUrl: r.UPLOAD_URL + 'news/newsUploadImg.do',
            imgUrlList: [],
            validateValue:false,
            pressId:null,
            ruleForm:{
                name:null,
                title:null,
                link:null,
                language:null,
                reTime:null,
                logoUrl:null,
                //完成度
                // complete:null
            },
            rules: {
                name: [{
                    required: true,
                    validator: (rule, value, callback) => {
                        if(!value) {
                            return callback(new Error('请输入媒体名称'));
                        } else if(value.length>50){
                            return callback(new Error('名称最多为50个字符!'));
                        }else {
                            callback();
                        }
                    },
                    trigger: 'blur'
                }],
                logoUrl: [{
                    required: true,
                    validator: (rule, value, callback) => {
                        if(!value) {
                            return callback(new Error('请上传媒体logo'));
                        } else {
                            callback();
                        }
                    },
                    trigger: 'change'
                }],
                reTime: [{
                    required: true,
                    validator: (rule, value, callback) => {
                        if(!value) {
                            return callback(new Error('请选择文章发布时间'));
                        } else {
                            callback();
                        }
                    },
                    trigger: 'change'
                }],
                title: [{
                    required: true,
                    validator: (rule, value, callback) => {
                        if(!value) {
                            return callback(new Error('请输入文章标题'));
                        } else if(value.length>100){
                            return callback(new Error('标题最多为100个字符!'));
                        }else {
                            callback();
                        }
                    },
                    trigger: 'blur'
                }],
                link: [{
                    required: true,
                    validator: (rule, value, callback) => {
                        if(!value) {
                            return callback(new Error('请输入文章链接'));
                        }
                        // else if(value.length>150){
                        //     return callback(new Error('链接最多为150个字符!'));
                        // }
                        else {
                            callback();
                        }
                    },
                    trigger: 'blur'
                }],
                language:[{
                    required: true,
                    validator: (rule, value, callback) => {
                        if(!value) {
                            return callback(new Error('请选择文章语言'));
                        }else {
                            callback();
                        }
                    },
                    trigger: 'change'
                }]
            }
        }
    },
    methods:{
        //返回
        back() {
            this.$refs.ruleForm.validate((valid) => {
                if(valid) {
                    this.backDialog = true;
                }else{
                    this.$router.push({
                        path: '/pressCenter'
                    })
                }
            });
        },
        //不保存
        notSave(){
            this.backDialog = false;
            this.$refs.ruleForm.resetFields();
            this.$router.push({
                path: '/pressCenter'
            })
        },
        handlePreview(file) {
            //console.log(file);
        },
        //图片上传前
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg';
            const isPNG = file.type === 'image/png';
            const isLt1M = file.size / 1024 / 1024 < 0.05;

            if(!isJPG && !isPNG) {
                this.$message.error('上传图片只能是 JPG或PNG 格式!');
            }
            if(!isLt1M) {
                this.$message.error('上传图片大小最好不要超过 50kb!');
            }

            // 限制图片上传的宽高
            const isSize = new Promise(function(resolve, reject) {
                let width = 205;
                let height = 60;
                let _URL = window.URL || window.webkitURL;
                let img = new Image();
                img.onload = function() {
                    let valid = img.width <= width && img.height <= height;
                    valid ? resolve() : reject();
                }
                img.src = _URL.createObjectURL(file);
            }).then(() => {
                return file;
            }, () => {
                this.$message.error('上传的图片尺寸必须小于205*60!');
                return Promise.reject();
            });

            if(isJPG || isPNG){
                this.ruleForm.logoUrl = null;
            }
            return isLt1M && (isJPG || isPNG)&&isSize;
        },
        imgSuccess(res, file) {
            this.$refs.imgUpload.uploadFiles.length = 0;
            if(res.code === 0 && res) {
                this.ruleForm.logoUrl = res.data;
                this.imgUrlList = [{
                    name: res.data,
                    url: r.GETIMG_URL + res.data
                }]
            }
        },
        //移除logo图片
        imgUrlRemove() {
            this.ruleForm.logoUrl = null;
            this.imgUrlList = [];
        },
        // 保存
        save(){
            let _this = this;
            let newUrl = null;
            let updateUrl = null;
            _this.$refs.ruleForm.validate((valid) => {
                if(valid) {
                    if(_this.ruleForm.logoUrl !== null && _this.ruleForm.logoUrl.uid !== undefined) {
                        r.message('媒体logo正在上传加载中，请稍后提交', "warning");
                        return;
                    }
                    if(_this.pressId === null) {
                        newUrl = "addMedia";
                        _this.postNewData(newUrl);
                    } else {
                        updateUrl = "uptMedia";
                        _this.ruleForm.id = _this.pressId;
                        _this.updateData(updateUrl);
                    }
                }else{
                    r.message("请填写完整媒体报道相关信息", "warning");
                }
            });
        },
        //新增媒体报道
        postNewData(url) {
            let _this = this;
            _this.ApiService.media[url](_this.ruleForm).then(data => {
                if(data && data.code === 0) {
                    r.message('新增成功', "success");
                    _this.$router.push({
                        path: '/pressCenter'
                    })
                } else {
                    r.message(data.message, "warning");
                }
            })
            .catch(err => {
                console.log(err);
            });
        },
        //编辑修改新闻
        updateData(url) {
            let _this = this;
            _this.ApiService.media[url](_this.ruleForm).then(data => {
                if(data && data.code === 0) {
                    r.message('修改成功', "success");
                    _this.$router.push({
                        path: '/pressCenter'
                    })
                } else {
                    r.message(data.message, "warning");
                }
            })
            .catch(err => {
                console.log(err);
            });
        },
        //点击编辑进入页面获取页面数据
        getData() {
            let _this = this;
            let imagesUrl = r.GETIMG_URL;
            let param = {
                id: _this.pressId
            }
            _this.ApiService.media.findPlatMediaById(param).then(data => {
                if(data && data.code === 0) {
                    _this.ruleForm = data.data;
                    _this.ruleForm.logoUrl = data.data.logoUrl;
                    if(data.data.logoUrl !== null) {
                        _this.imgUrlList = [{
                            name: data.data.logoUrl,
                            url: imagesUrl + data.data.logoUrl
                        }]
                    }
                } else {
                    r.message(data.message, "warning");
                }
            })
            .catch(err => {
                console.log(err);
            });
        },
    },
    mounted(){
        let _this = this;
        let query = this.$route.query;
        _this.pressId = query.pressId;

        if(_this.pressId !== null) {
            let baseUrl = "";
            this.getData(baseUrl);
        }
    }
}
</script>

<style scoped>

</style>

