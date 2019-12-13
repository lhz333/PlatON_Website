<template>
    <div>
        <!--head-->
        <div class="content-c-breadcrumb">
            <el-breadcrumb separator="-">
                <el-breadcrumb-item>新闻管理</el-breadcrumb-item>
                <el-breadcrumb-item>新增新闻({{language}}版)</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <!--表单-->
        <div class="fill-form">
            <!--操作按钮-->
            <div class="btns">
                <el-button @click="back">返回</el-button>
                <el-button type="primary" @click="save">保存</el-button>
            </div>
            <el-form :model="ruleForm" id="myForm" :rules="rules" ref="ruleForm" label-width="165px" class="demo-ruleForm" @submit="save" enctype="multipart/form-data">
                <el-form-item :label="'新闻标题（'+language+'）'" prop="title">
                    <el-input name="title" v-model.trim="ruleForm.title" placeholder="请输入新闻标题"></el-input>
                </el-form-item>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="'作者名（'+language+'）'" prop="author">
                            <el-input name="author" v-model.trim="ruleForm.author" placeholder="请输入作者名"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="请选择发布日期" prop="releaseTime">
                            <el-date-picker name="releaseTime" type="date" v-model.trim="ruleForm.releaseTime" style="width:100%;" placeholder="请选择发布日期"></el-date-picker>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item :label="'活动封面图（'+language+'）'" prop="top_img_url">
                            <el-upload ref="topUpload" class="upload-demo" name="imgUrl" :action="serverUrl" :on-preview="handlePreview" :file-list="topImgUrlList" :on-success="topimgSuccess" list-type="picture" :before-upload="topbeforeAvatarUpload" :before-remove="topImgUrlRemove">
                                <el-button slot="trigger" size="small" type="primary">点击上传</el-button>
                                <div slot="tip" class="el-upload__tip">首页活动图片：请上传尺寸为408*540px的图片，格式为png/jpg</div>
                            </el-upload>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="'新闻封面图（'+language+'）'" prop="imgUrl">
                            <el-upload ref="imgUpload" name="imgUrl" class="upload-demo" :action="serverUrl" :on-preview="handlePreview" :file-list="imgUrlList" list-type="picture" :before-upload="beforeAvatarUpload" :on-success="imgSuccess" :before-remove="imgUrlRemove">
                                <el-button slot="trigger" size="small" type="primary">点击上传</el-button>
                                <div slot="tip" class="el-upload__tip">请上传尺寸为338*220px的图片，格式为png/jpg</div>
                            </el-upload>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col>
                        <el-form-item :label="'新闻分类（'+language+'）'" prop="typeId">
                            <el-radio-group class="radio-group" @change="handleTypeChange" v-model="ruleForm.typeId">
                                <el-radio v-for="(item,index) in typeList" :key="index" :label="item.id" border>{{item.name}}</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="活动开始时间" prop="`activityBeginTime">
                            <el-date-picker name="activityBeginTime" type="date" v-model.trim="ruleForm.activityBeginTime" placeholder="活动开始时间" style="width:100%;"></el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="活动结束时间" prop="activityEndTime">
                            <el-date-picker name="activityEndTime" type="date" v-model.trim="ruleForm.activityEndTime" placeholder="活动结束时间" style="width:100%;"></el-date-picker>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col>
                        <el-form-item :label="'新闻标签（'+language+'）'">
                            <el-tag
                                class="newsTag"
                                :key="tag"
                                v-for="tag in ruleForm.lables"
                                closable
                                :disable-transitions="false"
                                @close="handleClose(tag)">
                                {{tag.lable}}
                            </el-tag>
                            <el-input
                                class="input-new-tag"
                                v-if="inputVisible"
                                v-model="inputValue"
                                ref="saveTagInput"
                                @keyup.enter.native="handleInputConfirm"
                                @blur="handleInputConfirm"
                                >
                            </el-input>
                            <el-button v-else class="button-new-tag" @click="showInput">+ 添加标签</el-button>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <!-- 图片上传组件辅助-->
                    <el-upload class="avatar-uploader" :action="serverUrl" name="imgUrl" :show-file-list="false" :on-success="uploadSuccess" :on-error="uploadError" :before-upload="beforeUpload">
                    </el-upload>
                    <!-- <el-upload class="video-uploader" :action="serverUrlVideo" name="imgUrlVideo"  :show-file-list="false" :on-success="uploadSuccessVideo" :on-error="uploadErrorVideo" :before-upload="beforeUploadVideo">
                    </el-upload> -->
                    <el-form-item :label="'新闻内容（'+language+'）'" prop="newsText">
                        <quill-editor name="newsText" v-loading="quillUpdateImg" class="appwrapper" v-model.trim="ruleForm.newsText" ref="myQuillEditor" :options="editorOption" @change="onEditorChange($event)" @ready="onEditorReady($event)" @blur="onEditorBlur($event)" @focus="onEditorFocus($event)">
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
                                <button class="ql-video" title="视频"></button>
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
    import r from '../../../util/RequestUtil.js'
    // 自定义字体大小
    let Size = Quill.import('attributors/style/size')
    Size.whitelist = ['12px', '14px', '16px', '18px', '20px']
    Quill.register(Size, true)

    export default {
        data() {
            return {
                // 富文本编辑器配置
                editorOption: {
                    placeholder: '请输入新闻正文内容',
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
                // 这里写你要上传的图片服务器地址
                serverUrl: r.UPLOAD_URL + 'news/newsUploadImg.do',
                language: null,
                languageName: null,
                newsId: null,
                topImgUrlList: [],
                imgUrlList: [],
                validateValue: false,  
                activityTime: false,
                ruleForm: {
                    title: null,
                    author: null,
                    releaseTime: null,
                    activityBeginTime: null,
                    activityEndTime: null,
                    newsText: null,
                    imgUrl: null,
                    topImgUrl: null,
                    complete: null,
                    typeId:null,
                    lables: [],
                },
                backDialog: false,
                uploadType: null,
                saveBtn: false,
                //form表单验证
                rules: {
                    title: [{
                        required: true,
                        validator: (rule, value, callback) => {
                            if(!value) {
                                return callback(new Error('请输入新闻标题'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'blur'
                    }],
                    author: [{
                        required: true,
                        validator: (rule, value, callback) => {
                            if(!value) {
                                return callback(new Error('请输入作者名'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'blur'
                    }],
                    releaseTime: [{
                        required: true,
                        validator: (rule, value, callback) => {
                            if(!value) {
                                return callback(new Error('请选择发布日期'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'change'
                    }],
                    imgUrl: [{
                        required: true,
                        validator: (rule, value, callback) => {
                            if(!value) {
                                return callback(new Error('请上传非头条封面图'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'change'
                    }],
                    typeId:[{
                        required: true,
                        validator: (rule, value, callback) => {
                            if(!value) {
                                return callback(new Error('请选择新闻分类'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'blur'
                    }],
                    newsText: [{
                        required: true,
                        validator: (rule, value, callback) => {
                            if(!value) {
                                return callback(new Error('请填写新闻内容'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'blur'
                    }]
                },
                // 标签
                inputVisible: false,
                inputValue: '',
                // 分类
                typeList:[],
            }
        },
        methods: {
            // 上传图片前
            beforeUpload(file) {
                // 显示loading动画
                this.quillUpdateImg = true;

                const isLt1M = file.size / 1024 / 1024 < 0.1;
                if(!isLt1M) {
                    this.quillUpdateImg = false;
                    this.$message.error('上传图片大小最好不要超过 100kb!');
                }
                const isImg = file.type.indexOf("image") != -1;
                const isVideo = file.type.indexOf("video") != -1;

                if(isImg) {
                    this.uploadType = 'image';
                } else if(isVideo) {
                    this.uploadType = 'video';
                }

                if(!isImg && !isVideo) {
                    this.quillUpdateImg = false;
                    this.$message.error('上传文件只能是图片或视频格式!');
                }
                return isLt1M && (isImg || isVideo);
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
            topimgSuccess(res, file) {
                this.$refs.topUpload.uploadFiles.length = 0;
                if(res.code === 0 && res) {
                    this.ruleForm.topImgUrl = res.data;
                    this.topImgUrlList = [{
                        name: res.data,
                        url: r.GETIMG_URL + res.data
                    }]
                }
            },
            imgSuccess(res, file) {
                this.$refs.imgUpload.uploadFiles.length = 0;
                if(res.code === 0 && res) {
                    this.ruleForm.imgUrl = res.data;
                    this.imgUrlList = [{
                        name: res.data,
                        url: r.GETIMG_URL + res.data
                    }]
                }
            },

            // 富文本图片上传失败
            uploadError() {
                // loading动画消失
                this.quillUpdateImg = false
                this.$message.error('图片插入失败')
            },
            onEditorReady() {

            },
            //返回
            back() {
                this.validateData();
                if(this.validateValue) {
                    this.backDialog = true;
                } else {
                    if(this.languageName == 'zh') {
                        this.$router.push({
                            path: '/zhnews'
                        })
                    } else {
                        this.$router.push({
                            path: '/ennews'
                        })
                    }
                }
            },
            handleRemove(file, fileList) {
                //              console.log(file, fileList);
            },
            handlePreview(file) {
                //              console.log(file);
            },
            handleExceed(files, fileList) {
                this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
            },
            //成功上传
            handleSuccess(file, fileList) {
                this.fileList.push(file);
            },
            topbeforeAvatarUpload(file) {
//              this.ruleForm.topImgUrl = file;
                const isJPG = file.type === 'image/jpeg';
                const isPNG = file.type === 'image/png';
                const isLt1M = file.size / 1024 / 1024 < 0.05;
                if(!isJPG && !isPNG) {
                    this.$message.error('上传图片只能是 JPG或PNG 格式!');
                }
                if(!isLt1M) {
                    this.$message.error('上传图片大小最好不要超过 50kb!');
                }
                if(isJPG || isPNG){
                    this.ruleForm.topImgUrl = null;
                }
                return (isJPG || isPNG) && isLt1M;
            },
            //移除非头条图片
            imgUrlRemove() {
                this.ruleForm.imgUrl = null;
                this.imgUrlList = [];
            },
            //移除头条图片
            topImgUrlRemove() {
                this.ruleForm.topImgUrl = null;
                this.topImgUrlList = [];
            },
            //图片上传前
            beforeAvatarUpload(file) {
//              this.ruleForm.imgUrl = file;
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
                //              const isSize = new Promise(function(resolve, reject) {
                //                  let width = 570;
                //                  let height = 290;
                //                  let _URL = window.URL || window.webkitURL;
                //                  let img = new Image();
                //                  img.onload = function() {
                //                      let valid = img.width == width && img.height == height;
                //                      valid ? resolve() : reject();
                //                  }
                //                  img.src = _URL.createObjectURL(file);
                //              }).then(() => {
                //                  return file;
                //              }, () => {
                //                  this.$message.error('上传的图片必须是等于570*290!');
                //                  return Promise.reject();
                //              });
                if(isJPG || isPNG){
                    this.ruleForm.imgUrl = null;
                }
                return (isJPG || isPNG) && isLt1M;
            },
            //失去焦点事件
            onEditorBlur() {
                //              console.log(this.ruleForm.content);
            },
            //获得焦点事件
            onEditorFocus() {

            },
            //内容改变事件
            onEditorChange() {

            },
            //不保存
            notSave() {
                this.backDialog = false;
                this.saveBtn = true;
                this.$refs.ruleForm.resetFields();
                if(this.languageName == 'zh') {
                    this.$router.push({
                        path: '/zhnews'
                    })
                } else {
                    this.$router.push({
                        path: '/ennews'
                    })
                }
            },
            validateData() {
                if((this.ruleForm.title == null || this.ruleForm.title == '') && (this.ruleForm.author == null || this.ruleForm.author == '') && (this.ruleForm.releaseTime == null || this.ruleForm.releaseTime == '') && (this.ruleForm.newsText == null || this.ruleForm.newsText == '') && this.ruleForm.imgUrl == null && this.ruleForm.topImgUrl == null) {
                    this.validateValue = false;
                } else {
                    this.validateValue = true;
                }
            },
            //保存
            save() {
                let _this = this;
                let newUrl = null;
                let updateUrl = null;
                this.validateData();
                _this.$refs.ruleForm.validate((valid) => {
                    if(valid) {
                        _this.ruleForm.complete = 1;
                    } else {
                        _this.ruleForm.complete = 0;
                        //r.message('请确认信息是否填写完整', "warning");
                        return false;
                    }
                });
                if(this.validateValue) {
                    if(this.ruleForm.imgUrl !== null && this.ruleForm.imgUrl.uid !== undefined) {
                        r.message('非头条图片正在上传加载中，请稍后提交', "warning");
                        return;
                    }
                    if(this.ruleForm.topImgUrl !== null && this.ruleForm.topImgUrl.uid !== undefined) {
                        r.message('头条图片正在上传加载中，请稍后提交', "warning");
                        return;
                    }
                    this.saveBtn = true;
                    if(_this.languageName == 'zh') {
                        if(this.ruleForm.title !== null && this.ruleForm.title.length > 100) {
                            r.message('新闻标题不可超过100个字符', "warning");
                            return;
                        }
                        if(this.ruleForm.author !== null && this.ruleForm.author.length > 20) {
                            r.message('新闻作者不可超过20个字符', "warning");
                            return;
                        }
                        if(_this.newsId === null) {
                            newUrl = "saveNewsCnInfo";
                            this.postNewData(newUrl);
                        } else {
                            updateUrl = "uptNewsCn";
                            this.ruleForm.id = this.newsId;
                            this.updateData(updateUrl);
                        }
                    } else if(this.languageName == 'en') {
                        if(this.ruleForm.title !== null && this.ruleForm.title.length > 200) {
                            r.message('新闻标题不可超过200个字符', "warning");
                            return;
                        }
                        if(this.ruleForm.author !== null && this.ruleForm.author.length > 40) {
                            r.message('新闻作者不可超过40个字符', "warning");
                            return;
                        }
                        if(_this.newsId === null) {
                            newUrl = "saveNewsEnInfo";
                            this.postNewData(newUrl);
                        } else {
                            updateUrl = "uptNewsEn";
                            this.ruleForm.id = this.newsId;
                            this.updateData(updateUrl);
                        }
                    }
                } else {
                    r.message("请填写相关新闻信息", "warning");
                }
            },
            //新增新闻
            postNewData(url) {
                let _this = this;
                let typeLangue = _this.languageName == 'zh'? 'newsCn' : 'newsEn';
                _this.ApiService[typeLangue][url](_this.ruleForm).then(data => {
                    if(data && data.code === 0) {
                        r.message(data.message, "success");
                        if(_this.languageName == 'zh') {
                            _this.$router.push({
                                path: '/zhnews'
                            })
                        } else {
                            _this.$router.push({
                                path: '/ennews'
                            })
                        }
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
                let typeLangue = _this.languageName == 'zh'? 'newsCn' : 'newsEn';
                _this.ApiService[typeLangue][url](_this.ruleForm).then(data => {
                    if(data && data.code === 0) {
                        r.message(data.message, "success");
                        if(_this.languageName == 'zh') {
                            _this.$router.push({
                                path: '/zhnews'
                            })
                        } else {
                            _this.$router.push({
                                path: '/ennews'
                            })
                        }
                    } else {
                        r.message(data.message, "warning");
                    }
                })
                .catch(err => {
                    console.log(err);
                });
            },
            //点击编辑进入页面获取页面数据
            getData(url) {
                let _this = this;
                //              let imagesUrl = r.UPLOAD_URL + 'news/readImg.do?imgUrl=';
                let imagesUrl = r.GETIMG_URL;
                let typeLangue = _this.languageName == 'zh'? 'newsCn' : 'newsEn';
                let param = {
                    id: _this.newsId
                }
                _this.ApiService[typeLangue][url](param).then(data => {
                    if(data && data.code === 0) {
                        _this.ruleForm = data.data;

                        if(data.data.newsText !== null) {
                            _this.ruleForm.newsText = data.data.newsText.replace(/imagesUrl=/g, imagesUrl);
                        } else {
                            _this.ruleForm.newsText = data.data.newsText;
                        }
                        _this.ruleForm.topImgUrl = data.data.topImgUrl;
                        _this.ruleForm.imgUrl = data.data.imgUrl;
                        if(data.data.topImgUrl !== null) {
                            _this.topImgUrlList = [{
                                name: data.data.topImgUrl,
                                url: imagesUrl + data.data.topImgUrl
                            }]
                        }
                        if(data.data.imgUrl !== null) {
                            _this.imgUrlList = [{
                                name: data.data.imgUrl,
                                url: imagesUrl + data.data.imgUrl
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
            //新闻分类
            getType(url){
                let _this = this;
                let param = {};
                let typeLangue = _this.languageName == 'zh'? 'newTypeCn' : 'newTypeEn';
                _this.ApiService[typeLangue][url]().then(data => {
                    if(data && data.code === 0) {
                        _this.typeList = data.data;
                    } else{
                        r.message(data.message, "warning");
                    }
                })
                .catch(err => {
                    console.log(err);
                });
            },
            // 标签
            handleClose(tag) {
                this.ruleForm.lables.splice(this.ruleForm.lables.indexOf(tag), 1);
            },

            showInput() {
                if(this.ruleForm.lables.length>=5){
                    r.message('一个新闻最多只能添加5个标签', "warning");
                    return false;
                }
                this.inputVisible = true;
                this.$nextTick(_ => {
                    this.$refs.saveTagInput.$refs.input.focus();
                });
            },

            handleInputConfirm() {
                let inputValue = this.inputValue;
                let flag = true;
                if (inputValue) {
                    let param = {
                        lable:inputValue
                    }

                    this.ruleForm.lables.forEach(item=>{
                        if(param.lable===item.lable){
                            flag = false;
                        }
                    })

                    if (flag) {
                        this.ruleForm.lables.push(param);
                    }else{
                        r.message('此新闻已添加过该标签，请修改标签名称', "warning");
                        return false;
                    }
                }
                this.inputVisible = false;
                this.inputValue = '';
            },
            // 预览
            previewClick(){
                
            },
            // 选择活动时间
            handleTypeChange(val){
                console.log(val);
                // if(){

                // }
            }
        },
        beforeRouteLeave: function(to, from, next) {
            this.validateData();
            if(this.validateValue && !this.saveBtn) {
                next(false)
                this.backDialog = true;
            } else {
                next(true)
            }
        },
        mounted() {
            let _this = this;
            let query = this.$route.query;
            _this.newsId = query.newsId;
            _this.languageName = query.language;

            let baseUrl = null;
            let typeBaseUrl = null;

            if(_this.languageName == 'zh') {
                _this.language = '中文';
                //获取分类数据
                typeBaseUrl = "findTypeCn";
                this.getType(typeBaseUrl);
                if(_this.newsId !== null) {
                    baseUrl = "findNewsCnInfoById";
                    this.getData(baseUrl);
                }
            } else if(_this.languageName == 'en') {
                _this.language = '英文';
                //获取分类数据
                typeBaseUrl = "findTypeEn";
                this.getType(typeBaseUrl);
                if(_this.newsId !== null) {
                    baseUrl = "findNewsEnInfoById";
                    this.getData(baseUrl);
                }
            }

            //绑定beforeunload事件 刷新或关闭浏览器事件
            window.onbeforeunload = function(e) {
                _this.validateData();
                if(_this.validateValue && _this.$route.fullPath.indexOf("releaseNews") != -1) {
                    e = e || window.event;
                    // 兼容IE8和Firefox 4之前的版本
                    if(e) {
                        e.returnValue = '关闭提示';
                    }
                    // Chrome, Safari, Firefox 4+, Opera 12+ , IE 9+
                    return '关闭提示';
                } else {
                    window.onbeforeunload = null
                }
            };
        }
    }
</script>
<style>
    .newsTag.el-tag + .newsTag.el-tag {
        margin-left: 10px;
    }
    .radio-group .el-radio.is-bordered{
        margin-right: 10px;
    }
    .radio-group .el-radio.is-bordered+.el-radio.is-bordered{
        margin:0 10px 10px 0;
    }
    .newsTag.el-tag{
        height: 40px;
        line-height: 40px;
        font-size: 14px;
    }
    .button-new-tag {
        height: 40px;
        line-height: 40px;
        padding-top: 0;
        padding-bottom: 0;
    }
    .input-new-tag.el-input {
        width: 100px;
        margin-left: 10px;
        vertical-align: bottom;
    }
    .upload-demo .el-upload__tip {
        margin-top: 0;
        line-height: 24px;
        color: red;
    }

    .appwrapper .ql-container {
        height: 400px;
    }
</style>
<style scoped="scoped">
</style>