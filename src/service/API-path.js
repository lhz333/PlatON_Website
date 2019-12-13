//动态获取本地环境
const Base = process.env.API_ROOT;
const API = Base+'/platon/';

//登录
const login = {
    //登录
    login :`${API}login/login.do`,
    //退出登录
    logout:`${API}login/logout.do`,
    //获取菜单
    findMenu:`${API}common/findMenu.do`,
    //修改密码
    uptUser:`${API}common/uptUser.do`
}

//中文新闻管理
const newsCn = {
    //获取中文新闻列表
    findNewsCnInfo :`${API}news/findNewsCnInfo.do`,
    //修改状态
    uptPlatNewsCnStatus: `${API}news/uptPlatNewsCnStatus.do`,
    //未关联的数据
    findNotBindPlatNewsEn: `${API}news/findNotBindPlatNewsEn.do`,
    //中英文关联
    removeNewsCnen: `${API}news/removeNewsCnen.do`,
    //操作记录
    findNewsCnOpertion: `${API}news/findNewsCnOpertion.do`,
    //排序  上移下移
    sortNewsCnUporDown: `${API}news/sortNewsCnUporDown.do`,
    // 详情
    findNewsCnInfoById: `${API}news/findNewsCnInfoById.do`,
    // 编辑修改新闻
    uptNewsCn: `${API}news/uptNewsCn.do`,
    //新增新闻
    saveNewsCnInfo: `${API}news/saveNewsCnInfo.do`,
    // 查找相关语言
    findNewsDetail: `${API}news/findNewsDetail.do`,
    // 相关阅读
    findPlatNewsCnByLableId: `${API}news/findPlatNewsCnByLableId.do`
}
//英文新闻管理
const newsEn = {
    //获取英文列表
    findNewsEnInfo: `${API}news/findNewsEnInfo.do`,
    //修改状态
    uptPlatNewsEnStatus: `${API}news/uptPlatNewsEnStatus.do`,
    //未关联的数据
    findNotBindPlatNewsCn: `${API}news/findNotBindPlatNewsCn.do`,
    //中英文关联
    bindNewsCnEn: `${API}news/bindNewsCnEn.do`,
    //取消关联
    removeNewsCnen: `${API}news/removeNewsCnen.do`,
    //操作记录
    findNewsEnOpertion: `${API}news/findNewsEnOpertion.do`,
    //排序  上移下移
    sortNewsEnUporDown: `${API}news/sortNewsEnUporDown.do`,
    // 详情
    findNewsEnInfoById: `${API}news/findNewsEnInfoById.do`,
    // 编辑修改新闻
    uptNewsEn: `${API}news/uptNewsEn.do`,
    //新增新闻
    saveNewsEnInfo: `${API}news/saveNewsEnInfo.do`,
    // 相关阅读
    findPlatNewsEnByLableId: `${API}news/findPlatNewsEnByLableId.do`
}
//中文分类管理
const newTypeCn = {
    findTypeCn:`${API}type/findTypeCn.do`,
    //获取分类数据列表
    pageTypeCn: `${API}type/pageTypeCn.do`,
    //新建分类
    addTypeCn: `${API}type/addTypeCn.do`,
    //重命名
    updTypeCnName: `${API}type/updTypeCnName.do`,
    //删除分类
    removeTypeCn: `${API}type/removeTypeCn.do`
}
//英文分类管理
const newTypeEn = {
    findTypeEn:`${API}type/findTypeEn.do`,
    //获取分类数据列表
    pageTypeEn: `${API}type/pageTypeEn.do`,
    //新建分类
    addTypeEn: `${API}type/addTypeEn.do`,
    //重命名
    updTypeEnName: `${API}type/updTypeEnName.do`,
    //删除分类
    removeTypeEn: `${API}type/removeTypeEn.do`
}
// FAQ管理
const faq = {
    //列表数据
    findPlatFaq:`${API}faq/findPlatFaq.do`,
    //编辑
    findPlatFaqById: `${API}faq/findPlatFaqById.do`,
    //修改
    uptFaq:`${API}faq/uptFaq.do`,
    //新增
    insFaq:`${API}faq/insFaq.do`,
    //排序
    sortFaqUporDown: `${API}faq/sortFaqUporDown.do`,
    //修改状态
    uptFaqStatus: `${API}faq/uptFaqStatus.do`,
    //操作记录
    findFaqCnOpertion: `${API}faq/findFaqCnOpertion.do`
}

//订阅管理
const subscribe = {
    //获取列表数据
    findPlatSubscription: `${API}subscription/findPlatSubscription.do`,
    //删除
    removeSubscription: `${API}subscription/removeSubscription.do`
}

//媒体报道
const media = {
    //获取媒体名称列表
    findMediaNames: `${API}media/findMediaNames.do`,
    //获取列表数据
    findPlatMedia: `${API}media/findPlatMedia.do`,
    //修改状态
    uptMediaStatus: `${API}media/uptMediaStatus.do`,
    //新增
    addMedia: `${API}media/addMedia.do`,
    //修改
    uptMedia: `${API}media/uptMedia.do`,
    //详情
    findPlatMediaById: `${API}media/findPlatMediaById.do`
}

/* 测试币管理 */
const testcoin = {
    //新建渠道
    addChannelInfo:`${API}channel/addChannelInfo.do`,
    //渠道列表
    pageChannelInfo:`${API}channel/pageChannelInfo.do`,
    //获取单个渠道
    findChannelById:`${API}channel/findChannelById.do`,
    //渠道修改
    uptChannelInfo:`${API}channel/uptChannelInfo.do`,
    //渠道操作
    uptChannelStatus:`${API}channel/uptChannelStatus.do`,
    //渠道导出
    dowloadChannel: `${API}channel/dowloadChannel.do`,

    //领取记录
    //渠道领取记录
    pageRecevingInfo:`${API}receive/pageReceiveInfo.do`,
    //领取统计
    findReceiveCount:`${API}receive/findReceiveCount.do`,
    // 渠道领取记录导出
    dowloadReceive:`${API}receive/dowloadReceive.do`,

    //转账列表
    pageTransferInfo:`${API}transfer/pageTransferInfo.do`,
    addTransferInfo:`${API}transfer/addTransferInfo.do`,
    findTransferCount:`${API}transfer/findTransferCount.do`,
    dowloadTransfer:`${API}transfer/dowloadTransfer.do`,
    recevingTestCurrency:`${API}transfer/recevingTestCurrency.do`,

    //发币账户管理
    pageAccountInfo:`${API}account/pageAccountInfo.do`,
    addAccountInfo:`${API}account/addAccountInfo.do`,
    findAccouontBalance:`${API}account/findAccouontBalance.do`,
    removeAccountInfo:`${API}account/removeAccountInfo.do`,
    findAccountList:`${API}account/findAccountList.do`
}
// 下载管理
const downloadM = {
    //顶部信息汇总 
    dowloadTotal: `${API}verify/dowload/total/dowload.do`,
    //列表信息统计
    dowloadManager: `${API}verify/dowload/total/dowloadManager.do`,
    //导出
    dowloadBrowser:`${API}verify/dowload/dowloadBrowser.do`
}

export default {
    login,
    newsCn,
    newsEn,
    newTypeCn,
    newTypeEn,
    faq,
    subscribe,
    testcoin,
    media,
    downloadM
}