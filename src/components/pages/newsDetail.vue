<template>
    <div>
        <!--头部-->
        <div class="block-header">
            <div class="content-min-size header-content paddingLR50">
                <a class="logo" href="JavaScript:;">
                    <img src="../../assets/img/nav-logo.png" alt="" style="width: 50%;" />
                </a>
                <ul class="block-header-ul">
                    <li class="change-language" id="language" @mouseenter="tabLanguageShow" @mouseleave="tabLanguageHide">
                        <p>
                            <span>{{languageName}}</span>
                            <i class="el-icon-arrow-down el-icon--right"></i>
                        </p>
                        <transition name="height">
                            <div class="model-language" id="model-language" v-show="languageDialog">
                                <ul class="clear text-center">
                                    <li @click="handleCommand('en')">
                                        <a href="JavaScript:;" name="en">English</a>
                                    </li>
                                    <li @click="handleCommand('zh')">
                                        <a href="JavaScript:;" name="zh">中文</a>
                                    </li>
                                </ul>
                            </div>
                        </transition>
                    </li>
                </ul>
            </div>
        </div>
        <!--新闻内容-->
        <div class="newsdetail">
            <div class="content-min-size paddingLR50 clear" v-loading="loading">
                <div class="news-detail fl">
                    <p class="news-time">
                        <span class="type fontWeight">{{newsList.typeName}}</span>
                        {{newsList.author}}
                        <i v-if="newsList.author!==null && newsList.releaseTime !== null">&nbsp;&nbsp;·&nbsp;&nbsp;</i>
                        <span v-html="formatterTimestamp(newsList.releaseTime)"></span>
                    </p>
                    <h3 class="news-title">{{newsList.title}}</h3>
                    <div class="news-content ql-container ql-snow newsDetail">
                        <pre class="ql-editor" v-html="newsList.newsText"></pre>
                    </div>
                    <el-tag
                        class="newsDetailTag"
                        v-for="tag in newsList.lables"
                        :key="tag.id"
                        :disable-transitions="false">
                        {{tag.lable}}
                    </el-tag>
                </div>
                <div class="related fr">
                    <h3>相关阅读</h3>
                    <ul class="related-news clear" v-if="relatShow">
                        <li v-for="(item,index) in relatList" :key="index" @click="gotoDetail(item.id)">
                            <img :src="imagesUrl+item.imgUrl" alt="">
                            <div class="related-content">
                                <h4>{{item.title}}</h4>
                                <p class="related-time marginT10"><span>{{item.author}}</span>   ·   2018-9-12</p>
                            </div>
                        </li>
                    </ul>
                    <p class="color00223A" v-if="!relatShow">暂无相关阅读</p>
                </div>
            </div>
        </div>
        
        <!--底部-->
        <div class="block-footer">
            <div class="content-min-size paddingLR50">
                <div class="footer-bottom text-center">
                    COPYRIGHT &copy; 2018 PLATON INTERNATIONAL LIMITED ALL RIGHTS RESERVED.
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import r from '../../util/RequestUtil.js'
    export default {
        name: '',
        data() {
            return {
                newsList: [],
                relatList:[],
                relatShow:true,
                languageDialog: false,
                language: '中文',
                newsId :null,
                languageName:null,
                imagesUrl: r.GETIMG_URL
            }
        },
        methods: {
            //语言栏显示
            tabLanguageShow() {
                this.languageDialog = true;
            },
            //语言栏隐藏
            tabLanguageHide() {
                this.languageDialog = false;
            },
            //时间转换
            formatterTimestamp(row, column) {
                return r.formatterTimestampT(row);
            },
            //语言切换
            handleCommand(command) {
                let param={};
                switch(command) {
                    case 'zh':
                        this.language = 'zh';
                        this.languageName = '中文';
                        if(this.$route.query.language == 'en'){
                            param = {
                                enId:this.newsId
                            }
                            this.validateRecord(param);
                        }else{
                            this.newsId = this.$route.query.newsId;
                            this.getData();
                        }
                        break;
                    case 'en':
                        this.language = 'en';
                        this.languageName = 'English';
                        if(this.$route.query.language == 'zh'){
                            param = {
                                cnId:this.newsId
                            }
                            this.validateRecord(param);
                        }else{
                            this.newsId = this.$route.query.newsId;
                            this.getData();
                        }
                        break;
                    default:
                        break;
                }
            },
            validateRecord(param){
                let _this = this;
                _this.ApiService.newsCn.findNewsDetail(param).then(data => {
                    if(data && data.code === 0) {
                        _this.newsId = data.data.id;
                        _this.getData();
                    } else {
//                      r.message('没有相关语言的版本', "warning");
                    }
                })
                .catch(err => {
                    _this.loading = false;
                    console.log(err);
                });
            },
            getData(){
                let _this = this;
                let imagesUrl = r.GETIMG_URL;
                let baseUrl = null;
                if(this.language == 'zh'){
                    baseUrl = "findNewsCnInfoById";
                }else{
                    baseUrl = "findNewsEnInfoById";
                }
                let typeLangueName = _this.language == 'zh'? 'newsCn' : 'newsEn';
                let param ={
                    id: _this.newsId
                }
                _this.ApiService[typeLangueName][baseUrl](param).then(data => {
                    if(data && data.code === 0) {
                        _this.newsList = data.data;
                        _this.getRelatedData();
                        if(data.data.newsText!==null){
                            _this.newsList.newsText = data.data.newsText.replace(/imagesUrl=/g, imagesUrl);
                        }
                    } else {
                        r.message(data.message, "warning");
                    }
                })
                .catch(err => {
                    console.log(err);
                });
            },
            // 相关阅读
            getRelatedData(){
                let _this = this;
                let imagesUrl = r.GETIMG_URL;
                let baseUrl = null;
                let lableIds = [];

                if(this.language == 'zh'){
                    baseUrl = "findPlatNewsCnByLableId";
                }else{
                    baseUrl = "findPlatNewsEnByLableId";
                }
                if(_this.newsList.lables.length<=0){
                    _this.relatShow = false;
                    return false;
                }
                for(let i=0;i<_this.newsList.lables.length;i++){
                    lableIds.push(_this.newsList.lables[i].id);
                }
                let typeLangueName = _this.language == 'zh'? 'newsCn' : 'newsEn';
                let param ={
                    id:_this.newsId,
                    length: 4,
                    lableIds:lableIds
                }
                _this.ApiService[typeLangueName][baseUrl](param).then(data => {
                    if(data && data.code === 0) {
                        if(data.data.length<=0){
                            _this.relatShow = false;
                            return false;
                        }
                        _this.relatList = data.data;
                    } else {
                        r.message(data.message, "warning");
                    }
                })
                .catch(err => {
                    console.log(err);
                });
            },
            gotoDetail(newsId) {
                // var _this = this;
                // const {href} = this.$router.resolve({
                //     name: "newsDetail",
                //     query: {
                //         language: _this.language,
                //         newsId: newsId
                //     }
                // });
                // window.open(href, '_blank');
                var _this = this;
                this.$router.push({
                    path: '/newsDetail',
                    query: {
                        language:_this.language,
                        newsId: newsId
                    }
                })
                location.reload();
            },
        },
        mounted(){
            var _this = this;
            let query = this.$route.query;
            _this.newsId = parseInt(query.newsId);
            _this.language = query.language;
            if(this.language == 'zh'){
                this.languageName = '中文';
            }else{
                this.languageName = 'English';
            }

            this.getData();
        }
    }
</script>

<style type="text/css">
    .newsDetailTag.el-tag + .newsDetailTag.el-tag {
        margin-left: 10px;
    }
    .newsDetailTag.el-tag{
        background: #E8EAEA;
        color: #8B8C8E;
        line-height: 32px;
        border:none;
    }
    .ql-editor{
        padding: 0;
        margin:0;
    }
	.ql-editor .ql-video{
        width: 500px;
        height: 300px;
        margin: auto;
    }
    .newsDetail.ql-container.ql-snow{
        border: none;
    }
    .newsDetail .ql-editor{
        overflow-x: hidden;
    }
</style>
<style scoped="scoped">
    @import url("../../assets/css/newsdetail.css");

</style>