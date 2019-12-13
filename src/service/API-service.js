import Vue from 'vue';
import Http from 'axios'
import API from './API-path';
import CONFIG from '../config/config';
import Cookie from '../util/cookie';
import { Message } from 'element-ui';

Http.defaults.headers.post['Content-Type'] = "application/json;charset=utf-8";
Http.interceptors.response.use(function (response) {
    var Base = window.location.href.split("//")[0]+ '//' + window.location.host;
    if(response.data.code === 9999){
        Message.error('您还没有登录，请先登录');
        setTimeout(function(){
            window.location.href = Base+'/platon_web/#/login';
        },100);
        return false;
    }
    return response;
}, function (error) {
    console.log(error);
    return Promise.reject(error);
});
/**
 * encode get 请求对象
 * @param params
 * @returns {string}
 */
const encodeParams = (params) => {
    let r = '?',
        p = [];
    for (let key in params) {
        p.push(`${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`);
    }
    return r + p.join('&');
};

/**
 * 请求类
 */
class ApiService {
    constructor() {
        //登录
        this.login = {
            login: this.post.bind(this, API.login.login),
            logout: this.get.bind(this, API.login.logout),
            findMenu: this.post.bind(this, API.login.findMenu),
            uptUser: this.post.bind(this, API.login.uptUser),
        }

        //中文新闻管理
        this.newsCn = {
            findNewsCnInfo: this.post.bind(this, API.newsCn.findNewsCnInfo),
            uptPlatNewsCnStatus: this.post.bind(this, API.newsCn.uptPlatNewsCnStatus),
            findNotBindPlatNewsEn: this.post.bind(this, API.newsCn.findNotBindPlatNewsEn),
            removeNewsCnen: this.post.bind(this, API.newsCn.removeNewsCnen),
            findNewsCnOpertion: this.post.bind(this, API.newsCn.findNewsCnOpertion),
            sortNewsCnUporDown: this.post.bind(this, API.newsCn.sortNewsCnUporDown),
            findNewsCnInfoById: this.post.bind(this, API.newsCn.findNewsCnInfoById),
            uptNewsCn: this.post.bind(this, API.newsCn.uptNewsCn),
            saveNewsCnInfo: this.post.bind(this, API.newsCn.saveNewsCnInfo),
            findNewsDetail: this.post.bind(this, API.newsCn.findNewsDetail),
            findPlatNewsCnByLableId: this.post.bind(this, API.newsCn.findPlatNewsCnByLableId)
        }
        //英文新闻管理
        this.newsEn = {
            findNewsEnInfo: this.post.bind(this, API.newsEn.findNewsEnInfo),
            uptPlatNewsEnStatus: this.post.bind(this, API.newsEn.uptPlatNewsEnStatus),
            findNotBindPlatNewsCn: this.post.bind(this, API.newsEn.findNotBindPlatNewsCn),
            bindNewsCnEn: this.post.bind(this, API.newsEn.bindNewsCnEn),
            removeNewsCnen: this.post.bind(this, API.newsEn.removeNewsCnen),
            findNewsEnOpertion: this.post.bind(this, API.newsEn.findNewsEnOpertion),
            sortNewsEnUporDown: this.post.bind(this, API.newsEn.sortNewsEnUporDown),
            findNewsEnInfoById: this.post.bind(this, API.newsEn.findNewsEnInfoById),
            uptNewsEn: this.post.bind(this, API.newsEn.uptNewsEn),
            saveNewsEnInfo: this.post.bind(this, API.newsEn.saveNewsEnInfo),
            findPlatNewsEnByLableId: this.post.bind(this, API.newsEn.findPlatNewsEnByLableId)
        }

        //中文分类管理
        this.newTypeCn = {
            findTypeCn: this.post.bind(this, API.newTypeCn.findTypeCn),
            pageTypeCn: this.post.bind(this, API.newTypeCn.pageTypeCn),
            addTypeCn: this.post.bind(this, API.newTypeCn.addTypeCn),
            updTypeCnName: this.post.bind(this, API.newTypeCn.updTypeCnName),
            removeTypeCn: this.post.bind(this, API.newTypeCn.removeTypeCn),
        }

        //英文分类管理
        this.newTypeEn = {
            findTypeEn: this.post.bind(this, API.newTypeEn.findTypeEn),
            pageTypeEn: this.post.bind(this, API.newTypeEn.pageTypeEn),
            addTypeEn: this.post.bind(this, API.newTypeEn.addTypeEn),
            updTypeEnName: this.post.bind(this, API.newTypeEn.updTypeEnName),
            removeTypeEn: this.post.bind(this, API.newTypeEn.removeTypeEn),
        }
        
        // FAQ管理
        this.faq = {
            findPlatFaq: this.post.bind(this,API.faq.findPlatFaq),
            findPlatFaqById: this.post.bind(this,API.faq.findPlatFaqById),
            uptFaq: this.post.bind(this,API.faq.uptFaq),
            insFaq: this.post.bind(this,API.faq.insFaq),
            sortFaqUporDown: this.post.bind(this,API.faq.sortFaqUporDown),
            uptFaqStatus: this.post.bind(this,API.faq.uptFaqStatus),
            findFaqCnOpertion: this.post.bind(this,API.faq.findFaqCnOpertion),
        }

        //订阅管理
        this.subscribe = {
            //获取列表数据
            findPlatSubscription: this.post.bind(this,API.subscribe.findPlatSubscription),
            //删除
            removeSubscription: this.post.bind(this,API.subscribe.removeSubscription),
        }

        //媒体报道
        this.media = {
            findMediaNames: this.post.bind(this,API.media.findMediaNames),
            findPlatMedia: this.post.bind(this,API.media.findPlatMedia),
            uptMediaStatus: this.post.bind(this,API.media.uptMediaStatus),
            addMedia: this.post.bind(this,API.media.addMedia),
            uptMedia: this.post.bind(this,API.media.uptMedia),
            findPlatMediaById: this.post.bind(this,API.media.findPlatMediaById),
        }
        /* 测试币管理 */
        this.testcoin = {
            //渠道管理
            addChannelInfo: this.post.bind(this, API.testcoin.addChannelInfo),
            pageChannelInfo: this.post.bind(this,API.testcoin.pageChannelInfo),
            findChannelById: this.post.bind(this,API.testcoin.findChannelById),
            uptChannelInfo: this.post.bind(this,API.testcoin.uptChannelInfo),
            uptChannelStatus: this.post.bind(this,API.testcoin.uptChannelStatus),
            dowloadChannel: this.downFile.bind(this,API.testcoin.dowloadChannel),  
            
            //领取记录
            //渠道领取记录
            pageRecevingInfo:this.post.bind(this,API.testcoin.pageRecevingInfo),
            dowloadReceive:this.downFile.bind(this,API.testcoin.dowloadReceive),  
            findReceiveCount:this.post.bind(this,API.testcoin.findReceiveCount),

            //转账列表
            pageTransferInfo:this.post.bind(this,API.testcoin.pageTransferInfo),
            addTransferInfo:this.post.bind(this,API.testcoin.addTransferInfo),
            findTransferCount:this.post.bind(this,API.testcoin.findTransferCount),
            dowloadTransfer:this.downFile.bind(this,API.testcoin.dowloadTransfer),
            recevingTestCurrency:this.post.bind(this,API.testcoin.recevingTestCurrency),

            //发币账户管理
            pageAccountInfo:this.post.bind(this,API.testcoin.pageAccountInfo),
            addAccountInfo:this.post.bind(this,API.testcoin.addAccountInfo),
            findAccouontBalance:this.post.bind(this,API.testcoin.findAccouontBalance),
            removeAccountInfo:this.post.bind(this,API.testcoin.removeAccountInfo),
            findAccountList:this.post.bind(this,API.testcoin.findAccountList),
            
        }

        this.downloadM = {
            dowloadTotal:this.post.bind(this,API.downloadM.dowloadTotal),
            dowloadManager:this.post.bind(this,API.downloadM.dowloadManager),
            dowloadBrowser:this.downFile.bind(this,API.downloadM.dowloadBrowser)
        }
        this.interceptorsOfReq();
        this.interceptorsOfRes();
    }

    /**
     * encode请求参数
     * @param url
     * @param params
     * @returns {Promise.<TResult>}
     */
    getParam(url, params) {
        if (params) {
            url += '?param=' + encodeURIComponent(JSON.stringify(params));
        }
        return Http.get(url).then(res => {
            if (res.statusText == "OK") {
                window.location.href = url
                return {result: 0}
            } else {
                return {result: -1}
            }
        });
    }

    /**
     * 新增标签进行下载
     * @param url
     * @param params
     */
    tagDownFile(url, params) {
        let _params = {};
        _params.param = params;
        if (params) {
            url += '?param=' + encodeURIComponent(JSON.stringify(params));
        }
        window.open(url, '_blank');
    }

    /**
     * 下载文件 当前页面下载 可多次调用下载多个文件
     * @param url
     * @param params
     * @returns {Promise.<TResult>}
     */
    downFile(url, params) {
        if (!params) {
            params = {}
        }
        return Http({
            method: 'post',
            url: url,
            data: params,
            responseType: 'blob',     //不加它导致乱码
            headers: {
                'Content-Type': 'application/json'
            }         
        }).then(res => {
            const blob = new Blob([res.data]);//new Blob([res])中不加data就会返回[objece objece]内容（少取一层）
            let date = new Date();
            let fileNameTime = date.getFullYear() + "" + (date.getMonth() + 1) + "" + date.getDate();
            const fileName = params.fileNames + fileNameTime+'.xlsx';//下载文件名称
            const elink = document.createElement('a');
            elink.download = fileName;
            elink.style.display = 'none';
            elink.href = URL.createObjectURL(blob);
            document.body.appendChild(elink);
            elink.click();
            URL.revokeObjectURL(elink.href); // 释放URL 对象
            document.body.removeChild(elink);
        })
    }

    /**
     * get请求
     * @param url
     * @param params
     * @returns {Promise.<TResult>}
     */
    get(url, params) {
        if (params) {
            url += encodeParams(params);
        }
        return Http.get(url).then(res => res.data);
    }

    /**
     * post请求
     * @param url       请求地址
     * @param params    请求参数
     * @param flag      是否需要加签名
     * @returns {Promise.<TResult>}
     */
    post(url, params) {
        if(!params) {
            params = {}
        }
        return Http.post(url, params).then(res => res.data);
    }

    /**
     * 上传文件
     * @param url
     * @param formData
     * @param config
     * @returns {Promise.<TResult>}
     */
    postFile(url, formData, config) {
        // console.log('formData提交数据== ' + formData);
        return Http({
            url: url,
            method: 'post',
            data: formData,
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }).then(res => res.data);
    }

    /**
     * 上传文件返回进度以及结果
     * @param url
     * @param formData
     * @param callback1     进度对象
     * @param callback2     返回结果
     * @returns {Promise.<TResult>}
     */
    postFilePercent(url, formData, callback1, callback2) {
        return Http({
            url: url,
            method: 'post',
            data: formData,
            onUploadProgress: function (progressEvent) {
                if (progressEvent.lengthComputable) {
                    //属性lengthComputable主要表明总共需要完成的工作量和已经完成的工作是否可以被测量
                    //如果lengthComputable为false，就获取不到progressEvent.total和progressEvent.loaded
                    callback1(progressEvent);
                }
            }
        }).then(res => {
            callback2(res.data)
        }).then(err => {
            console.log(err);
        })
    }

    /**
     * 请求拦截器
     * @returns {number}
     */
    interceptorsOfReq() {
        return Http.interceptors.request.use(
            config => {
                // console.log('请求URL== ' + config.url);
                // console.log('请求参数==', config.data);
                return config;
            },
            err => {
                return Promise.reject(err);
            });
    }

    /**
     * 响应拦截器
     */
    interceptorsOfRes() {
        Http.interceptors.response.use(function (response) {
            // console.log('响应完整数据==', response);
            // console.log('响应数据==', response.data);
            return response;
        }, function (error) {
            return Promise.reject(error);
        });
    }

}

//导出一个对象
export default new ApiService();
