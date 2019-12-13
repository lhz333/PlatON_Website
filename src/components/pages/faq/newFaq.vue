<template>
    <div v-loading="loading">
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
                <h3 class="text-center mb20">{{language}}版本</h3>
                <el-form-item :label="'问题（'+language+'）'" prop="problemCn">
                    <el-input v-model.trim="ruleForm.problemCn" placeholder="请输入问题内容"></el-input>
                </el-form-item>
                <el-row>
                    <!-- 图片上传组件辅助-->
                    <el-upload class="avatar-uploader" :action="serverUrl" name="imgUrl" :show-file-list="false" :on-success="uploadSuccess" :on-error="uploadError" :before-upload="beforeUpload">
                    </el-upload>
                    <el-form-item :label="'回答（'+language+'）'" prop="replyCn">
                        <quill-editor name="replyCn" v-loading="quillUpdateImg" class="appwrapper" v-model.trim="ruleForm.replyCn" ref="myQuillEditor" :options="editorOption" @change="onEditorChange($event)" @ready="onEditorReady($event)" @blur="onEditorBlur($event)" @focus="onEditorFocus($event)">
                            <div id="toolbar" slot="toolbar">
                                <!-- Add a bold button -->
                                <button class="ql-bold" title="加粗">Bold</button>
                                <button class="ql-italic" title="斜体">Italic</button>
                                <button class="ql-underline" title="下划线">underline</button>
                                <button class="ql-strike" title="删除线">strike</button>
                                <button class="ql-blockquote" title="引用"></button>
                                <button class="ql-code-block" title="代码"></button>
                                <button class="ql-header" value="1" title="标题1"></button>
                                <button class="ql-header" value="2" title="标题2"></button>
                                <!--Add list -->
                                <button class="ql-list" value="ordered" title="有序列表"></button>
                                <button class="ql-list" value="bullet" title="无序列表"></button>
                                <button class="ql-script" value="sub" title="下标"></button>
                                <button class="ql-script" value="super" title="上标"></button>
                                <button class="ql-indent" value="-1" title="左缩进"></button>
                                <button class="ql-indent" value="+1" title="右缩进"></button>
                                <button class="ql-direction" value="rtl" title="右对齐"></button>
                                <!-- Add font size dropdown -->
                                <select class="ql-size" title="字体大小">
                                    <option value="10px">10px</option>
                                    <option value="12px">12px</option>
                                    <option value="14px" selected>14px</option>
                                    <option value="16px">16px</option>
                                    <option value="18px">18px</option>
                                    <option value="20px">20px</option>
                                </select>
                                <select class="ql-header" title="段落格式">
                                    <option selected>段落</option>
                                    <option value="1">标题1</option>
                                    <option value="2">标题2</option>
                                    <option value="3">标题3</option>
                                    <option value="4">标题4</option>
                                    <option value="5">标题5</option>
                                    <option value="6">标题6</option>
                                </select>
                                <select class="ql-color" value="color" title="字体颜色"></select>
                                <select class="ql-background" value="background" title="背景颜色"></select>
                                <select class="ql-font" title="字体" value="font"></select>
                                <select class="ql-align" value="align" title="对齐"></select>
                                <button class="ql-link" title="超链接"></button>
                                <button class="ql-image" title="上传图片"></button>
                                <!-- <button class="ql-video" title="视频"></button> -->
                                <button class="ql-clean" title="还原"></button>
                            </div>
                        </quill-editor>
                    </el-form-item>
                    <!-- <el-form-item>
                        <el-button @click="previewClick">预览</el-button>
                    </el-form-item> -->
                </el-row>
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
    // 自定义字体大小
    let Size = Quill.import('attributors/style/size')
    Size.whitelist = ['12px', '14px', '16px', '18px', '20px']
    Quill.register(Size, true)

    export default {
        data() {
            return {
                // 富文本编辑器配置
                editorOption: {
                    placeholder: '请输入回答',
                    theme: 'snow', // or 'bubble'
                    modules: {
                        imageResize: {},
                        toolbar: {
                            container: '#toolbar', // 工具栏
                            handlers: {
                                'image': function(value) {
                                    if(value) {
                                        // 触发input框选择图片文件
                                        document.querySelector('.avatar-uploader input').click()
                                    } else {
                                        this.quill.format('image', false);
                                    }
                                }
                            }
                        }
                    }
                },
                // 根据图片上传状态来确定是否显示loading动画，刚开始是false,不显示
                quillUpdateImg: false,
                loading:false,
                // 这里写你要上传的图片服务器地址
                serverUrl: r.UPLOAD_URL + 'news/newsUploadImg.do',
                language: null,
                backDialog: false,
                validateValue:false,
                selected:null,
                ruleForm: {
                    problemCn: null,
                    replyCn:null,
                },
                rules: {
                    problemCn: [{
                        required: true,
                        validator: (rule, value, callback) => {
                            if(!value) {
                                return callback(new Error('请输入问题内容'));
                            } else if(value.length>50&&this.languageName==='zh'){
                                return callback(new Error('中文问题最多为50个字符!'));
                            }else if(value.length>100&&this.languageName==='en'){
                                return callback(new Error('英文问题最多为100个字符!'));
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
                                return callback(new Error('请输入回答内容'));
                            } else if(value.length>3000&&this.languageName==='zh'){
                                return callback(new Error('中文回答最多为3000个字符!'));
                            }else if(value.length>3000&&this.languageName==='en'){
                                return callback(new Error('英文回答最多为3000个字符!'));
                            }else {
                                callback();
                            }
                        },
                        trigger: 'blur'
                    }],
                }
            }
        },
        methods: {
            // jump
            routerGoto(){
                let _this = this;
                if(_this.languageName == 'zh') {
                    _this.$router.push({
                        path: '/zhFaq'
                    })
                } else {
                    _this.$router.push({
                        path: '/enFaq'
                    })
                }
            },
            //返回
            back() {
                let _this = this;
                _this.validateData();
                if(_this.validateValue){
                    _this.backDialog = true;
                }else{
                    _this.routerGoto();
                }
            },
            //失去焦点事件
            onEditorBlur() {
                //console.log(this.ruleForm.content);
            },
            //获得焦点事件
            onEditorFocus() {

            },
            //内容改变事件
            onEditorChange() {

            },
            // 上传图片前
            beforeUpload(file) {
                this.quillUpdateImg = true;
                const isImg = file.type.indexOf("image") != -1;
                const isVideo = file.type.indexOf("video") != -1;

                if(isImg) {
                    this.uploadType = 'image';
                } else if(isVideo) {
                    this.uploadType = 'video';
                }

                if(!isImg && !isVideo) {
                    this.$message.error('上传文件只能是图片或视频格式!');
                }
                return isImg || isVideo;
            },
            uploadSuccess(res, file) {
                // res为图片服务器返回的数据
                // 获取富文本组件实例
                let quill = this.$refs.myQuillEditor.quill
                // 如果上传成功
                if(res.code === 0 && res) {
                    // 获取光标所在位置
                    let length = quill.getSelection().index;
                    // 插入图片  res.data为服务器返回的图片地址
                    if(this.uploadType === 'image') {
                        quill.insertEmbed(length, 'image', r.GETIMG_URL + res.data);
                    } else if(this.uploadType === 'video') {
                        quill.insertEmbed(length, 'video', r.GETIMG_URL + res.data);
                    }
                    // 调整光标到最后
                    quill.setSelection(length + 1);
                } else {
                    this.$message.error('插入失败');
                }
                // loading动画消失
                this.quillUpdateImg = false
            },
            // 富文本图片上传失败
            uploadError() {
                // loading动画消失
                this.quillUpdateImg = false
                this.$message.error('图片插入失败')
            },
            onEditorReady() {

            },
            validateData(){
                if((this.ruleForm.problemCn == null || this.ruleForm.problemCn =='') && (this.ruleForm.replyCn == null || this.ruleForm.replyCn =='')){
                    this.validateValue = false;
                }
                else{
                    this.validateValue = true;
                }
            },
            //保存
            save() {
                let _this = this;
                _this.$refs.ruleForm.validate((valid) => {
                    if(valid) {
                        if(_this.selected!==null && _this.selected!==undefined){
                            _this.updateFaq();
                        }else{
                            //新增
                            _this.insFaq();
                        }
                    } else {
                        r.message('请确认信息是否填写完整', "warning");
                        return false;
                    }
                });
            },

            //不保存
            notSave() {
                let _this = this;
                this.backDialog = false;
                this.$refs.ruleForm.resetFields();
                _this.routerGoto();
            },
            //编辑faq
            updateFaq(){
                let _this = this;
                let baseUrl = null;
                _this.loading = true;
                if(_this.languageName == 'zh') {
                    baseUrl = "uptFaq";
                } else if(_this.languageName == 'en') {
                    baseUrl = "uptFaq";
                }

                _this.ApiService.faq[baseUrl](this.ruleForm).then(data=>{
                    _this.loading = false;
                    if(data && data.code === 0) {
                        r.message('修改成功', "success");
                        _this.routerGoto();
                    } else{
                        r.message(data.message, "warning");
                    }
                })
                .catch(err => {
                    _this.loading = false;
                    console.log(err);
                });
            },
            //新增faq
            insFaq(){
                let _this = this;
                let baseUrl = null;
                _this.loading = true;
                if(_this.languageName == 'zh') {
                    baseUrl = "insFaq";
                } else if(_this.languageName == 'en') {
                    baseUrl = "insFaq";
                }
                
                _this.ApiService.faq[baseUrl](this.ruleForm).then(data=>{
                    _this.loading = false;
                    if(data && data.code === 0) {
                        r.message('新增成功', "success");
                        _this.routerGoto();
                    } else{
                        _this.loading = false;
                        r.message(data.message, "warning");
                    }
                })
                .catch(err => {
                    console.log(err);
                });
            },
            getData(baseUrl){
                let _this = this;
                _this.loading = true;
                let param ={id:this.selected};
                _this.ApiService.faq[baseUrl](param).then(data=>{
                    _this.loading = false;
                    if(data && data.code === 0) {
                        _this.ruleForm = data.data;
                    } else{
                        r.message(data.message, "warning");
                    }
                })
                .catch(err => {
                    _this.loading = false;
                    console.log(err);
                });
            }
        },
        mounted() {
            let _this = this;
            let query = this.$route.query;
            this.selected = query.qId;
            _this.languageName = query.language;
            let baseUrl = null;

            if(_this.languageName == 'zh') {
                _this.language = '中文';
                if(_this.selected !== null) {
                    baseUrl = "findPlatFaqById";
                    this.getData(baseUrl);
                }
            } else if(_this.languageName == 'en') {
                _this.language = '英文';
                if(_this.selected !== null) {
                    baseUrl = "findPlatFaqById";
                    this.getData(baseUrl);
                }
            }
        }
    }
</script>
<style>
    .appwrapper .ql-container {
        height: 400px;
    }
</style>

<style scoped="scoped">
    
</style>