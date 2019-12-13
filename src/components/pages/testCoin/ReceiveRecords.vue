<template>
    <div>
         <!--head-->
        <div class="content-c-breadcrumb">
            <el-breadcrumb separator="-">
                <el-breadcrumb-item>Energon管理</el-breadcrumb-item>
                <el-breadcrumb-item>领取记录</el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <!--操作按钮-->
        <div class="content-util mb20">
            <el-form label-width="100px">
                <el-row :gutter="20">
                    <el-col :span="10">
                        <el-input placeholder="钱包地址/渠道名称/渠道ID" v-model="queryVo.channelId" class="input-with-select" :clearable="true" @blur="handleSearch" @keyup.native.enter="handleSearch()">
                            <el-button slot="append" @click="handleSearch" icon="el-icon-search"></el-button>
                        </el-input>
                    </el-col>
                    <el-col :span="10">
                        <el-form-item label="时间范围：">
                            <el-date-picker
                                style="width:100%;" 
                                v-model="queryVo.timeRange"
                                type="datetimerange"
                                range-separator="-"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                @change="changeTimeRange">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="4">
                        <el-button type="primary" @click="exportData">导出</el-button>
                    </el-col>
                </el-row>
            </el-form>
        </div>

        <el-row>
            <el-col :span="4" :offset="12">
                <p>领取数量：{{statistics.receiveCount}}</p>
            </el-col>
            <el-col :span="4">
                <p>领取次数：{{statistics.reveiveSize}}</p>
            </el-col>
            <el-col :span="4">
                <p>钱包数量：{{statistics.addressCount}}</p>
            </el-col>
        </el-row>

         <!--表格数据-->
        <el-table ref="selectDate" class="table-m20" v-loading="loading" :data="listData" border header-cell-style="text-align:center;background-color:#ddd;color:#333;" style="width:100%;" highlight-current-row>
            <el-table-column prop="address" label="钱包地址" align="center"></el-table-column>
            <el-table-column align="center" prop="email" label="邮箱"></el-table-column>
            <el-table-column align="center" prop="channelName" label="渠道名称"></el-table-column>
            <el-table-column align="center" prop="activityId" label="ID"></el-table-column>
            <el-table-column align="center" prop="count" width="160" label="领取数量(Energon)"></el-table-column>
            <el-table-column align="center" label="状态" width="90">
                <template slot-scope="scope">
                    <span v-html="statusChange(scope.row.status)"></span>
                </template>
            </el-table-column>
            <el-table-column align="center" prop="createTime" :formatter="formatterTimestamp" label="领取时间"></el-table-column>
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

export default {
    data(){
        return {
            statistics:{
                receiveCount:null,
                reveiveSize:null,
                addressCount:null
            },
            queryVo:{
                channelId:null,
                timeRange:null,
                beginTime:null,
                endTime:null,
            },
            page: {
                totalPages: 100,
                currentNo: 1,
                pageSize: 50
            },
            placeholderType:null,
            loading:false,
            typeOptions:[{
                label:'多条件',
                value:1
            },{
                label:'单渠道',
                value:2
            }],
            listData:[]
        }
    },
    methods:{
        //获取列表数据
        getData(){
            let _this = this;
            let param = {
                activityId:_this.queryVo.channelId,
                strCreateTime:_this.queryVo.beginTime,
                endCreateTime:_this.queryVo.endTime,
                pageNo:_this.page.currentNo,
                pageSize:_this.page.pageSize
            }
            _this.loading = true;
            _this.ApiService.testcoin.pageRecevingInfo(param).then(data => {
                _this.loading = false;
                if (data && data.code === 0) {
                    _this.listData = data.data.result;
                    _this.page.totalPages = data.data.totalCount;
                } else {
                    r.message(data.message, "warning");
                }
                })
                .catch(err => {
                    _this.loading = false;
                    console.log(err);
                });
        },
        //获取列表统计数据
        getStatisticsData(){
            let _this = this;
            let param = {
                activityId:_this.queryVo.channelId,
                strCreateTime:_this.queryVo.beginTime,
                endCreateTime:_this.queryVo.endTime,
            }
            _this.ApiService.testcoin.findReceiveCount(param).then(data => {
                if (data && data.code === 0) {
                    _this.statistics = data.data;
                } else {
                    r.message(data.message, "warning");
                }
                })
                .catch(err => {
                    console.log(err);
                });
        },
        //搜索
        handleSearch(){
            this.getData();
            this.getStatisticsData();
        },
        //时间转换
        formatterTimestamp(row, column) {
            return r.formatterTimeToMinutes(row.createTime);
        },
        statusChange(status) {
            switch(status) {
                case 0:
                    return '处理中';
                    break;
                case 1:
                    return '成功';
                    break;
                case 2:
                    return '失败';
                    break;
                case 3:
                    return '异常';
                    break;
                case 9:
                    return '发起申请';
                    break;
                default:
                    break;
            }
        },
        //修改页面显示条数
        handleSizeChange(val) {
            this.page.pageSize = val;
            this.getData();
            this.getStatisticsData();
        },
        handleCurrentChange(val) {
            this.page.currentNo = val;
            this.getData();
            this.getStatisticsData();
        },
        //时间范围
        changeTimeRange(value){
            if(this.queryVo.timeRange!==null){
                this.queryVo.beginTime = this.queryVo.timeRange[0];
                this.queryVo.endTime = this.queryVo.timeRange[1];
            }else{
                this.queryVo.beginTime = null;
                this.queryVo.endTime = null;
            }
            this.getData();
            this.getStatisticsData();
        },
        //导出
        exportData(){
            let _this = this;
            if(_this.listData.length<=0){
                r.message('该条件下无数据可导出', "warning");
                return false;
            }
            let param = {
                activityId:_this.queryVo.channelId,
                strCreateTime:_this.queryVo.beginTime,
                endCreateTime:_this.queryVo.endTime,
                fileNames:'领取记录'
            }
            _this.ApiService.testcoin.dowloadReceive(param);
        }
    },
    mounted(){
        let _this = this;
        let queryChannelId = window.sessionStorage.getItem('channelId');

        if(queryChannelId!==null && queryChannelId != undefined){
            _this.queryVo.channelId = queryChannelId;
        }else{
            _this.queryVo.channelId = null;
        }

        this.getData();
        this.getStatisticsData();
    },
    destroyed() {
        window.sessionStorage.setItem('channelId','');
    }
}
</script>



<style scoped>

</style>


