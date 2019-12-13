import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/user/Login'
import Main from '@/components/Main'
import ZhNews from '@/components/pages/zhnews'
import EnNews from '@/components/pages/ennews'
import Faq from '@/components/pages/faq'
import Subscribe from '@/components/pages/Subscribe'
const ReleaseNews = resolve => require(['@/components/pages/modules/releaseNews.vue'], resolve)
const PreviewNews = resolve => require(['@/components/pages/news.vue'], resolve)
const NewsDetail = resolve => require(['@/components/pages/newsDetail.vue'], resolve)
const ActivityNew = resolve => require(['@/components/pages/activityNew.vue'], resolve)
// 新闻分类
const NewFaq = resolve => require(['@/components/pages/modules/newFaq.vue'], resolve)
const ZhFaq = resolve => require(['@/components/pages/faq/zhFaq.vue'], resolve)
const EnFaq = resolve => require(['@/components/pages/faq/enFaq.vue'], resolve)
// 新闻分类
const ZhInsightsType = resolve => require(['@/components/pages/type/zhInsightsType.vue'], resolve)
const EnInsightsType = resolve => require(['@/components/pages/type/enInsightsType.vue'], resolve)
//媒体报道
const PressCenter = resolve => require(['@/components/pages/media/pressCenter.vue'], resolve)
const BuildMedia = resolve => require(['@/components/pages/media/buildMedia.vue'], resolve)
//测试币管理
const ChannelManage = resolve => require(['@/components/pages/testCoin/channelManage.vue'], resolve)
const NewChannel = resolve => require(['@/components/pages/modules/newChannel.vue'], resolve)
const ReceiveRecords = resolve => require(['@/components/pages/testCoin/ReceiveRecords.vue'], resolve)
const ManualTransfer = resolve => require(['@/components/pages/testCoin/ManualTransfer.vue'], resolve)
const CurrencyAccount = resolve => require(['@/components/pages/testCoin/CurrencyAccount.vue'], resolve)

// 下载管理
const DownLoad = resolve => require(['@/components/pages/download/download.vue'],resolve)

Vue.use(Router)

export default new Router({
    routes: [{
            path: '/',
            name: 'login',
            component: Login
        },
        {
            path: '/login',
            name: 'login',
            component: Login
        },
        {
            path:'/previewnews',
            name:'previewnews',
            component:PreviewNews
        },
        {
            path:'/newsDetail',
            name:'newsDetail',
            component:NewsDetail
        },
        {
            path: '/activityNew',
            name: 'activityNew',
            component: ActivityNew
        },
        {
            path: '/main',
            name: 'main',
            redirect: '/zhnews',
            component: Main,
            children: [{
                    path: '/zhnews',
                    name: 'zhnews',
                    component: ZhNews
                },
                {
                    path: '/ennews',
                    name: 'ennews',
                    component: EnNews
                },
                {
                    path: '/releaseNews',
                    name: 'releaseNews',
                    component: ReleaseNews
                },
                {
                    path: '/zhInsightsType',
                    name: 'zhInsightsType',
                    component: ZhInsightsType
                },
                {
                    path: '/enInsightsType',
                    name: 'enInsightsType',
                    component: EnInsightsType
                },
                {
                    path: '/pressCenter',
                    name: 'pressCenter',
                    component: PressCenter
                },
                {
                    path: '/buildMedia',
                    name: 'buildMedia',
                    component: BuildMedia
                },
                {
                    path:'/newFaq',
                    name: 'newFaq',
                    component: NewFaq
                },
                {
                    path: '/faq',
                    name: 'faq',
                    component: Faq
                },
                {
                    path: '/zhfaq',
                    name: 'zhfaq',
                    component: ZhFaq
                },
                {
                    path: '/enfaq',
                    name: 'enfaq',
                    component: EnFaq
                },
                {
                    path: '/subscribe',
                    name: 'subscribe',
                    component: Subscribe
                },
                //测试币管理
                {
                    path: '/channelManage',
                    name: 'channelManage',
                    component: ChannelManage
                },
                {
                    path: '/newChannel',
                    name: 'newChannel',
                    component: NewChannel
                },
                {
                    path: '/receiveRecords',
                    name: 'receiveRecords',
                    component: ReceiveRecords
                },
                {
                    path: '/manualTransfer',
                    name: 'manualTransfer',
                    component: ManualTransfer
                },
                {
                    path: '/currencyAccount',
                    name: 'currencyAccount',
                    component: CurrencyAccount
                },
                {
                    path: '/download',
                    name: 'download',
                    component: DownLoad
                }
            ]
        }
    ],
    scrollBehavior(to, from, savedPosition) {
        if(savedPosition) {
            return savedPosition
        } else {
            return {
                x: 0,
                y: 0
            }
        }
    }
})