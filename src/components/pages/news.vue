<template>
    <div class="news-container">
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
        <!--新闻资讯-->
        <!--banner-->
        <div class="case-block-1">
            <div class="back-mask"></div>
            <div class="content-min-size paddingLR50 case-banner">
                <div class="case-title text-center">
                    <h3 class="marginB20">{{newsTitle}}</h3>
                </div>                
            </div>
        </div>
        <!-- 锚点导航 -->
        <div class="nav-anchor" :class="isFixed== true?'isFixed':''">
            <div class="content-min-size paddingLR50 new-nav">
                <!-- <div class="nav-type">
                    <ul class="marginAuto nav-anchor-ul clear text-center">
                        <li v-for="(item,index) in typeList" :key="index" @click="getTypeNews(item.id)" :class="{active:activeIndex===item.id}">{{item.name}}</li>
                    </ul>
                </div> -->
                <div class="nav-type">
                    <div class="swiper-container">
                        <ul class="swiper-wrapper text-center">
                            <li class="swiper-slide" v-for="(item,index) in typeList" :key="index" @click="getTypeNews(item.id)" :class="{active:activeIndex===item.id}">{{item.name}}</li>
                        </ul>
                    </div>
                </div>
                
                <el-input
                    class="news-search"
                    placeholder="搜索"
                    v-model.trim="searchValue" @blur="handleSearch" @keyup.native.enter="handleSearch()" :clearable="true">
                    <i slot="prefix" class="el-input__icon el-icon-search"></i>
                </el-input>
            </div>
        </div>
        <!-- 新闻 -->
        <div class="news-block-2">
            <div class="content-min-size paddingLR50">
                <div class="news-left-margin">
                    <ul class="newsLists clear" v-if="newsList.length>0">
                        <li v-for="(item,index) in newsList" :key="index">
                            <div class="news-item" @click="gotoDetail(item.id)">
                                <div class="imgUrl">
                                    <img :src="imagesUrl+item.imgUrl" />
                                </div>
                                <div class="news-basic">
                                    <p class="news-title fontWeight">{{item.title}}</p>
                                    <p class="author-time marginT10">
                                        {{item.author}}
                                        <i v-if="item.author!==null && item.releaseTime !== null">&nbsp;&nbsp;·&nbsp;&nbsp;</i>
                                        <span v-html="formatterTimestamp(item.releaseTime)"></span>
                                    </p>
                                </div>
                            </div>
                        </li>
                    </ul>
                    <p class="text-center color00223A" v-else>暂无相关新闻</p>
                </div>
            </div>

            <!--分页-->
            <div class="pager-wrap content-pages" v-if="newsList.length>0">
                <el-pagination
                  @current-change="handleCurrentChange"
                  :current-page="page.currentNo"
                  :page-size="page.pageSize"
                  layout="prev, pager, next"
                  :total="page.totalPages">
                </el-pagination>
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
                languageDialog: false,
                languageName: '中文',
                newsTitle:null,
                language: null,
                newsId: null,
                imagesUrl: null,
                page: {
                    totalPages: 100,
                    currentNo: 1,
                    pageSize: 9
                },
                typeList:[],
                activeIndex:null,
                anchorsTop:0,
                isFixed: false,
            }
        },
        methods: {
            initSwiper() {
                var mySwiper = new Swiper('.swiper-container', {
                    // width: 1073,
                    slidesPerView: 6,
                    slidesPerGroup: 1,
                    // spaceBetween: 25,
                });
            },
            //   滚动事件
            handleScroll() {
                this.$nextTick(() => {
                    let scrollTop =
                        document.documentElement.scrollTop || document.body.scrollTop;
                    if (scrollTop > this.anchorsTop) {
                        this.isFixed = true;
                    } else {
                        this.isFixed = false;
                    }
                });
            },
            //获取分类数据
            getTypes(){
                let _this = this;
                let typeLangue = null;
                let baseUrl = null;
                if(_this.language == 'zh') {
                    typeLangue = 'newTypeCn';
                    baseUrl = "findTypeCn";
                } else {
                    typeLangue = 'newTypeEn'
                    baseUrl = "findTypeEn";
                }
                _this.ApiService[typeLangue][baseUrl]().then(data => {
                    if(data && data.code === 0) {
                       _this.typeList = data.data;
                        if(_this.language == 'zh') {
                            _this.typeList.unshift({name:"全部",id:null})
                        } else {
                            _this.typeList.unshift({name:"All",id:null})
                        }
                        _this.$nextTick(() => {
                            _this.initSwiper()
                        })
                    } else {
                        r.message(data.message, "warning");
                    }
                })
                .catch(err => {
                    console.log(err);
                });
            },
            //点击分类 获取不同分类下的数据
            getTypeNews(id){
                let _this = this;
                _this.activeIndex = id;
                _this.page.currentNo = 1;
                _this.getData();
            },
            //语言栏显示
            tabLanguageShow() {
                this.languageDialog = true;
            },
            //语言栏隐藏
            tabLanguageHide() {
                this.languageDialog = false;
            },
            handleCurrentChange(val) {
                this.page.currentNo = val;
                document.documentElement.scrollTop=0;
                document.body.scrollTop =0;
                this.getData();
            },
            //时间转换
            formatterTimestamp(row, column) {
                return r.formatterTimestampT(row);
            },
            formatterDate(timestamp){
                if(timestamp !== null) {
                    let date = new Date(timestamp);
                    return r.prefixIntrger(date.getDate())+'日';
                } else {
                    return "";
                }
            },
            //语言切换
            handleCommand(command) {
                switch(command) {
                    case 'zh':
                        this.languageName = '中文';
                        this.language = 'zh';
                        this.newsTitle = "观点";
                        this.page.currentNo = 1;
                        this.activeIndex=null;
                        this.getData();
                        this.getTypes();
                        break;
                    case 'en':
                        this.languageName = 'English';
                        this.language = 'en';
                        this.newsTitle = "Insights";
                        this.page.currentNo = 1;
                        this.activeIndex=null;
                        this.getData();
                        this.getTypes();
                        break;
                    default:
                        break;
                }
            },
            gotoDetail(newsId) {
                var _this = this;
                this.$router.push({
                    path: '/newsDetail',
                    query: {
                        language: _this.language,
                        newsId: newsId
                    }
                })
            },
            //搜索
            handleSearch() {
                this.getData();
            },
            //获取数据
            getData() {
                let _this = this;
                this.imagesUrl = r.GETIMG_URL;
                let param = {
                    title: _this.searchValue,
                    pageNo: _this.page.currentNo,
                    pageSize: _this.page.pageSize,
                    typeId:_this.activeIndex
                }
                let baseUrl = null;
                let newLangue = null;
                if(this.language == 'zh') {
                    newLangue = 'newsCn';
                    baseUrl = "findNewsCnInfo";
                } else {
                    newLangue = 'newsEn';
                    baseUrl = "findNewsEnInfo";
                }
                _this.ApiService[newLangue][baseUrl](param).then(data => {
                    if(data && data.code === 0) {
                        _this.newsList = data.data.result;
                        _this.page.totalPages = data.data.totalCount;
                    } else {
                        r.message(data.message, "warning");
                    }
                })
                .catch(err => {
                    console.log(err);
                });
            },
        },
        mounted() {
            var _this = this;
            let query = this.$route.query;
            _this.language = query.language;
            if(this.language == 'zh') {
                this.languageName = '中文';
                this.newsTitle = "观点";
            } else {
                this.languageName = 'English';
                this.newsTitle = "Insights";
            }
            this.getTypes();
            this.getData();

            let scrollTop =
              document.documentElement.scrollTop || document.body.scrollTop;
            this.anchorsTop =
              document.querySelector(".nav-anchor").getBoundingClientRect().top +
              scrollTop -
              50;
            window.addEventListener("scroll", this.handleScroll);
        },
        destroyed() {
            window.removeEventListener("scroll", this.handleScroll);
        }
    }
</script>

<style>
    .news-search .el-input__inner{
        border: 1px solid #B0B5B9;
        border-radius: 100px;
        height:32px;
        line-height: 32px;
        color:#b0b5b9;
        background: transparent;
    }
    .news-search .el-input__icon{
        line-height: 32px;
    }
</style>

<style scoped="scoped">
    @import url("../../assets/css/news.css");
    
    i {
        font-style: normal;
    }
</style>