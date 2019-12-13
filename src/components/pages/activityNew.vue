<template>
  <div class="news-container">
    <!--头部-->
    <div class="block-header">
      <div class="content-min-size header-content paddingLR50">
        <a
          class="logo"
          href="JavaScript:;"
        >
          <img
            src="../../assets/img/nav-logo.png"
            alt=""
            style="width: 50%;"
          />
        </a>
        <ul class="block-header-ul">
          <li
            class="change-language"
            id="language"
            @mouseenter="tabLanguageShow"
            @mouseleave="tabLanguageHide"
          >
            <p>
              <span>{{languageName}}</span>
              <i class="el-icon-arrow-down el-icon--right"></i>
            </p>
            <transition name="height">
              <div
                class="model-language"
                id="model-language"
                v-show="languageDialog"
              >
                <ul class="clear text-center">
                  <li @click="handleCommand('en')">
                    <a
                      href="JavaScript:;"
                      name="en"
                    >English</a>
                  </li>
                  <li @click="handleCommand('zh')">
                    <a
                      href="JavaScript:;"
                      name="zh"
                    >中文</a>
                  </li>
                </ul>
              </div>
            </transition>
          </li>
        </ul>
      </div>
    </div>
    <!--新闻公告-->
    <div class="index-block-2">
      <div
        class="content-min-size paddingLR50"
        style="height:734px;"
      >
        <div class="insights">
          <div class="insights-left">
            <!-- news -->
            <p class="insight-type-title">{{newsTitle.news}}</p>
            <ul
              class="new-list clear"
              v-loading="loading"
              style="min-height:296px;"
            >
              <li
                v-for="(item,index) in newsList"
                :key="index"
              >
                <div
                  class="new-list-content"
                  @click="gotoDetail(item.id)"
                >
                  <div class="imgUrl">
                    <img
                      v-if="item.imgUrl!==null"
                      :src="imagesUrl+item.imgUrl"
                    />
                  </div>
                  <div class="news-basic">
                    <p class="news-title fontWeight">{{item.title}}</p>
                    <div class="author-time-container">
                      <p class="author-time">
                        {{item.author}}
                      </p>
                      <i v-if="item.author!==null && item.releaseTime !== null">&nbsp;·&nbsp;</i>
                      <span v-html="formatterTimestamp(item.releaseTime)"></span>
                    </div>
                  </div>
                </div>
              </li>
            </ul>
            <!-- insights -->
            <p class="insight-type-title marginT20">{{newsTitle.insights}}</p>
            <ul
              class="new-list clear"
              v-loading="loadingInsight"
              style="min-height:296px;"
            >
              <li
                v-for="(item,index) in insightList"
                :key="index"
              >
                <div
                  class="new-list-content"
                  @click="gotoDetail(item.id)"
                >
                  <div class="imgUrl">
                    <img
                      v-if="item.imgUrl!==null"
                      :src="imagesUrl+item.imgUrl"
                    />
                  </div>
                  <div class="news-basic">
                    <p class="news-title fontWeight">{{item.title}}</p>
                    <div class="author-time-container">
                      <p class="author-time">
                        {{item.author}}
                      </p>
                      <i v-if="item.author!==null && item.releaseTime !== null">&nbsp;·&nbsp;</i>
                      <span v-html="formatterTimestamp(item.releaseTime)"></span>
                    </div>
                  </div>
                </div>
              </li>
            </ul>
          </div>
          <!-- events -->
          <div
            class="insights-right"
            v-loading="loadingEvent"
          >
            <p class="insight-type-title">{{newsTitle.events}}</p>
            <div
              class="events"
              v-if="eventsList.length>0"
              @click="gotoDetail(eventsList[0].id)"
            >
              <div class="event-img">
                <img
                  v-if="eventsList[0].topImgUrl!==null"
                  :src="imagesUrl+eventsList[0].topImgUrl"
                />
              </div>
              <div class="news-basic clear">
                <p
                  class="events-time fl text-center"
                  v-if="eventsList[0].activityBeginTime"
                >
                  <span v-html="formatterTimesDate(eventsList[0].activityBeginTime)"></span>
                  <span
                    class="fontSize34 fontWeight"
                    v-html="formatterTimesDates(eventsList[0].activityBeginTime)"
                  ></span>
                </p>
                <p
                  class="events-time fl text-center"
                  v-else
                >
                  <span v-html="formatterTimesDate(eventsList[0].releaseTime)"></span>
                  <span
                    class="fontSize34 fontWeight"
                    v-html="formatterTimesDates(eventsList[0].releaseTime)"
                  ></span>
                </p>
                <p class="events-title fl fontWeight">{{eventsList[0].title}}</p>
              </div>
            </div>
          </div>
        </div>
        <!-- 查看更多 -->
        <p
          class="text-right marginT20 seemore"
          @click="gotoNews()"
        >{{newsTitle.seemore}}</p>
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
import r from "../../util/RequestUtil.js";
export default {
  name: "",
  data() {
    return {
      newsList: [],
      insightList: [],
      eventsList: [],
      languageDialog: false,
      languageName: "中文",
      language: null,
      newsId: null,
      imagesUrl: null,
      newsTitle: {
          news: null,
          events: null,
          insights: null,
          seemore: null
      }
    };
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
    handleCurrentChange(val) {
      document.documentElement.scrollTop = 0;
      document.body.scrollTop = 0;
      this.getData();
    },
    //时间转换
    formatterTimestamp(row, column) {
      return r.formatterTimestampT(row);
    },
    formatterDate(timestamp) {
      if (timestamp !== null) {
        let date = new Date(timestamp);
        return r.prefixIntrger(date.getDate()) + "日";
      } else {
        return "";
      }
    },
    formatterTimesDate(row, column) {
      return r.formatterTimesDate(row);
    },
    formatterTimesDates(row, column) {
      return r.formatterTimesDates(row);
    },
    //语言切换
    handleCommand(command) {
      switch (command) {
        case "zh":
          this.languageName = "中文";
          this.language = "zh";
          this.newsTitle = {
              news: '新闻',
              events: '活动',
              insights: '观点',
              seemore: "查看更多",
          }
          break;
        case "en":
          this.languageName = "English";
          this.language = "en";
          this.newsTitle = {
            news: 'News',
            events: 'Events',
            insights: 'Insights',
            seemore: "See More",
        }
          break;
        default:
          break;
      }
      // 获取新闻数据
        this.getNewsData();
        this.getEventsData();
        this.getInsightsData();
    },
    //获取观点数据
    getInsightsData() {
      let _this = this;
      _this.loadingInsight = true;
      this.imagesUrl = r.GETIMG_URL;
      let param = {
        pageNo: 1,
        pageSize: 2,
        typeName: _this.newsTitle.insights
      };
      let baseUrl = null;
      if (this.language == "zh") {
        baseUrl = "findNewsCnInfo";
      } else {
        baseUrl = "findNewsEnInfo";
      }
      let typeLangueName = _this.language == 'zh'? 'newsCn' : 'newsEn';
      _this.ApiService[typeLangueName][baseUrl](param)
        .then(data => {
          _this.loadingInsight = false;
          if (data && data.code === 0) {
            _this.insightList = data.data.result;
          } else {
            r.message(data.message, "warning");
          }
        })
        .catch(err => {
          _this.loadingInsight = false;
          console.log(err);
        });
    },
    //获取活动数据
    getEventsData() {
      let _this = this;
      _this.loadingEvent = true;
      this.imagesUrl = r.GETIMG_URL;
      let param = {
        pageNo: 1,
        pageSize: 1,
        typeName: _this.newsTitle.events
      };
      let baseUrl = null;
      if (this.language == "zh") {
        baseUrl = "findNewsCnInfo";
      } else {
        baseUrl = "findNewsEnInfo";
      }
      let typeLangueName = _this.language == 'zh'? 'newsCn' : 'newsEn';
      _this.ApiService[typeLangueName][baseUrl](param)
        .then(data => {
          _this.loadingEvent = false;
          if (data && data.code === 0) {
            _this.eventsList = data.data.result;
          } else {
            r.message(data.message, "warning");
          }
        })
        .catch(err => {
          _this.loadingEvent = false;
          console.log(err);
        });
    },
    //获取新闻数据
    getNewsData() {
      let _this = this;
      _this.loading = true;
      this.imagesUrl = r.GETIMG_URL;
      let param = {
        pageNo: 1,
        pageSize: 2,
        typeName: _this.newsTitle.news
      };
      let baseUrl = null;
      if (this.language == "zh") {
        baseUrl = "findNewsCnInfo";
      } else {
        baseUrl = "findNewsEnInfo";
      }
      let typeLangueName = _this.language == 'zh'? 'newsCn' : 'newsEn';
      _this.ApiService[typeLangueName][baseUrl](param)
        .then(data => {
          _this.loading = false;
          if (data && data.code === 0) {
            _this.newsList = data.data.result;
          } else {
            r.message(data.message, "warning");
          }
        })
        .catch(err => {
          _this.loading = false;
          console.log(err);
        });
    },
    // 进去新闻页
    gotoNews(){
        const { href } = this.$router.resolve({
            path: "previewnews",
            query: {
                language: this.language
            }
        });
        window.open(href, '_blank');
    },
    gotoDetail(newsId) {
      var _this = this;
      this.$router.push({
        path: "/newsDetail",
        query: {
          language: _this.language,
          newsId: newsId
        }
      });
    }
  },
  mounted() {
    var _this = this;
    let query = this.$route.query;
    _this.language = query.language;
    if (this.language == "zh") {
      this.languageName = "中文";
      this.newsTitle = {
          news: '新闻',
          events: '活动',
          insights: '观点',
          seemore: "查看更多",
      }
    } else {
      this.languageName = "English";
      this.newsTitle = {
          news: 'News',
          events: 'Events',
          insights: 'Insights',
          seemore: "See More",
      }
    }
    // 获取新闻数据
    this.getNewsData();
    this.getEventsData();
    this.getInsightsData();
  }
};
</script>

<style scoped="scoped">
i {
  font-style: normal;
}
.marginT20 {
    margin-top: 20px;
}
.text-right {
    text-align: right;
}
.fontSize34{
    font-size: 34px;
}
/*新闻样式*/
.index-block-2{
    padding:100px 0 80px;
    background: #f5f7fa;
}
.insights{
    display: table;
    width: 100%;
}
.insights-left,.insights-right{
    display: table-cell;
}
.insights-left{
    width: 62.5%;
    max-width: 700px;
}
.insights-right{
    padding-left:10px;
}
.insight-type-title{
    font-size: 22px;
    color: #00223A;
    font-weight: bold;
    margin-bottom: 16px;
}
.new-list li{
    float: left;
    width: 50%;
    font-size: 14px;
    color: #05283F;
    
}
.new-list-content{
    background: #FFFFFF;
    border: 1px solid #F5F5F5;
    margin-right: 10px;
    cursor: pointer;
}
.imgUrl{
    width: 100%;
    height: 220px;
    overflow: hidden;
    background-color: #fff;
}
.imgUrl img{
    width: 100%;
    height: 100%;
}
.news-basic {
    padding:5px 10px;
}
.news-title{
    height: 40px;
    line-height: 20px;
    font-size: 14px;
    color: #05283F;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    cursor: pointer;
}
/* 活动 */
.events{
    background: #FFFFFF;
    border: 1px solid #F5F5F5;
    cursor: pointer;
}
.event-img{
    width: 100%;
    height: 540px;
    overflow: hidden;
}
.event-img img{
    width: 100%;
    height: 100%;
}
.events-title{
    width: 70%;
    height: 60px;
    line-height: 20px;
    font-size: 14px;
    color: #05283F;
    margin:15px 0 28px 20px;
    overflow: hidden;
    text-overflow: ellipsis;
    cursor: pointer;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
}
.events-time{
    position: relative;
    top:-20px;
    width: 80px;
    height: 88px;
    padding:10px 0;
    color: #fff;
    background: #48CCEC;
}
.seemore{
    font-size: 14px;
    color: #01233B;
    cursor: pointer;
}
.author-time-container {
    line-height: 24px;
    font-size: 12px;
    cursor: pointer;
}
.author-time-container span {
    color:#b1b1b1;
}
.author-time {
    display: inline-block;
    max-width: 55%;
    word-break: break-all;
    vertical-align: top;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
}
</style>