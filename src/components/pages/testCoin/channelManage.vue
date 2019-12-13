<template>
    <div>
         <!--head-->
        <div class="content-c-breadcrumb">
            <el-breadcrumb separator="-">
                <el-breadcrumb-item>Energon管理</el-breadcrumb-item>
                <el-breadcrumb-item>渠道管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <!--操作按钮-->
        <div class="content-util">
            <el-row :gutter="20">
                <el-col :span="6">
                    <el-button type="primary" @click="buildChannel">新建渠道</el-button>
                    <el-button type="primary" @click="exportData">导出</el-button>
                </el-col>
            </el-row>
        </div>
        <!--表格数据-->
        <el-table ref="selectDate" class="table-m20" v-loading="loading" :data="listData" border header-cell-style="text-align:center;background-color:#ddd;color:#333;" cell-style="padding:2px 0;" style="width:100%;" highlight-current-row>
            <el-table-column prop='id' label="ID" align="center">
                <template slot-scope="scope">
                    <span @click="gotoReceive(scope.row)" class="idactive">{{scope.row.id}}</span>
                </template>
            </el-table-column>
            <el-table-column prop="name" label="活动主题" align="center"></el-table-column>
            <el-table-column :show-overflow-tooltip="showTooltip" align="center" label="渠道名称">
                <template slot-scope="scope">
                    <span>{{scope.row.channelName}}</span>
                </template>
            </el-table-column>
            <el-table-column prop='createTime' align="center" width="160" :formatter="formatterTimestamp" label="创建时间"></el-table-column>
            <el-table-column label="已领取/可领取" align="center">
                <template slot-scope="scope">
                    <span>{{scope.row.useCount}}/{{scope.row.count}}</span>
                </template>
            </el-table-column>
            <el-table-column prop="sendQuantity" label="每轮发放数(Energon)" width="160" align="center"></el-table-column>
            <el-table-column prop="dayQuantity" label="每日限领量" align="center"></el-table-column>
            <el-table-column :formatter="formatterTimestampT" prop='effectiveTime' label="有效期" align="center"></el-table-column>
            <el-table-column label="状态" width="140" align="center">
                 <template slot-scope="scope">
                    <el-switch
                        v-model="scope.row.status"
                        active-color="#00A854"
                        active-text="启用"
                        :active-value='1'
                        inactive-color="#F04134"
                        inactive-text="禁用"
                        :inactive-value='0'
                        @change="changeStatus(scope.row)">
                    </el-switch>
                </template>
            </el-table-column>
            
            <el-table-column label="操作" width="125">
                <template slot-scope="scope">
                    <el-button type="text" @click="edit(scope.row)">编辑</el-button>
                    <el-button type="text" class="copy-link" :data-clipboard-text="scope.row.channelLink" @click="copyLink()">复制链接</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!--分页-->
        <div class="pager-wrap content-pages">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="page.currentNo" :page-sizes="[10, 20, 50, 100]" :page-size="page.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="page.totalPages">
            </el-pagination>
        </div>
    </div>
</template>

<script>
import r from '@/util/RequestUtil.js'
//复制功能
import Clipboard from 'clipboard';

export default {
    data(){
        return {
            loading:false,
            showTooltip: true,
            listData:[],
            page: {
                totalPages: 100,
                currentNo: 1,
                pageSize: 50
            },
        }
    },
    methods:{
        //获取数据
        getData(){
            let _this = this;
            let param = {
                pageNo:_this.page.currentNo,
                pageSize:_this.page.pageSize
            }
            _this.loading = true;
            _this.ApiService.testcoin.pageChannelInfo(param).then(data => {
                if (data && data.code === 0) {
                    _this.loading = false;
                    _this.listData = data.data.result;
                    _this.page.totalPages = data.data.totalCount;
                } else {
                    _this.loading = false;
                    r.message(data.message, "warning");
                }
                })
                .catch(err => {
                    _this.loading = false;
                    console.log(err);
                });
        },
        //时间转换
        formatterTimestamp(row, column) {
            return r.formatterTimeToMinutes(row.createTime);
        },
        formatterTimestampT(row, column) {
            return r.formatterTimestampT(row.effectiveTime);
        },
        //修改页面显示条数
        handleSizeChange(val) {
            this.page.pageSize = val;
            this.getData();
        },
        handleCurrentChange(val) {
            this.page.currentNo = val;
            this.getData();
        },
        //新建
        buildChannel(){
            this.$router.push({
                path: '/newChannel'
            })
        },
        //导出
        exportData(){
            let _this = this;
            let param = {
                fileNames:'渠道管理'
            }
            _this.ApiService.testcoin.dowloadChannel(param);
            // window.location.href = r.DOWN_BASE_URL+'channel/dowloadChannel.do';
        },
        //改变启动或禁用状态
        changeStatus(row){
            let _this = this;
            if(parseInt(row.status)===1){
                if(row.useCount>=row.count){
                    row.status = 0;
                    r.message('可领取次数已用完，重新编辑后才可启动', "warning");
                    return false;
                }
                let effectiveTime = row.effectiveTime+86399000;
                
                var oDate1 = new Date(effectiveTime);  
                var oDate2 = new Date();

                if(oDate1.getTime()<oDate2.getTime()){
                    row.status = 0;
                    r.message('有效期时间已过，重新编辑后才可启动', "warning");
                    return false;
                }
            }
            let param = {
                id:row.id,
                status:parseInt(row.status)
            }
            _this.loading = true;
            _this.ApiService.testcoin.uptChannelStatus(param).then(data => {
                _this.loading = false;
                if (data && data.code === 0) {
                    _this.getData();
                    _this.$message({
                        type: 'success',
                        message: '已成功修改状态'
                    });
                } else {
                    r.message(data.message, "warning");
                }
                })
                .catch(err => {
                    _this.loading = false;
                    console.log(err);
                });
        },
        //编辑
        edit(row){
            this.$router.push({
                path: '/newChannel',
                query: {
                    channelId: row.id
                }
            })
        },
        //复制链接
        copyLink(){
            let clipboard = new Clipboard('.copy-link')
            clipboard.on('success', e => {
                r.message('复制成功', "success");
                // 释放内存
                clipboard.destroy();
            })
            clipboard.on('error', e => {
                // 不支持复制
                r.message('该浏览器不支持自动复制', "warning");
                // 释放内存
                clipboard.destroy();
            })
        },
        //进入单渠道领取记录
        gotoReceive(row){
            this.$router.push({
                path: '/receiveRecords',
            })
            window.sessionStorage.setItem('channelId',row.id);
        }
    },
    mounted(){
        //获取列表数据
        let _this = this;
        _this.getData();
    }
}
</script>

<style scoped>
.idactive{
    color:#0033ff;
    text-decoration:underline;
    cursor: pointer;
}
</style>


