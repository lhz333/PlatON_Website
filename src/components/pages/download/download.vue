<template>
    <div>
        <!--head-->
        <div class="content-c-breadcrumb">
            <el-breadcrumb separator="-">
                <el-breadcrumb-item>下载管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="content-util mb20">
            <p class="mb20">下载总量：{{statistics.totalDowloads}}</p>
            <el-row :gutter="20">
                <el-col :span="6">
                    <p>Samurai(Windows)：{{statistics.samuraiWindow}}</p>
                </el-col>
                <el-col :span="6">
                    <p>Samurai(Linux)：{{statistics.samuraiLinux}}</p>
                </el-col>
                <el-col :span="6">
                    <p>ATON(Android)：{{statistics.atonAndroid}}</p>
                </el-col>
                <el-col :span="6">
                    <p>IP：{{statistics.ip}}</p>
                </el-col>
            </el-row>
        </div>
        <p style="color:red;">2019年3月7日15：00 点之前的数据无法统计浏览器信息</p>
        <!--操作按钮-->
        <div class="content-util mb20">
            <el-row :gutter="20">
                <el-col :span="7">
                    <span>浏览器类型：</span>
                    <el-select @change="handleBrowser" placeholder="请选择" v-model="queryVo.browserType">
                        <el-option label="All" value="All"></el-option>
                        <el-option label="Chrome" value="Chrome"></el-option>
                        <el-option label="Firefox" value="Firefox"></el-option>
                        <el-option label="Safari" value="Safari"></el-option>
                        <el-option label="IE" value="IE"></el-option>
                        <el-option label="Others" value="Others"></el-option>
                    </el-select>
                </el-col>
                <el-col :span="11">
                    <span>时间范围：</span>
                    <el-date-picker
                        v-model="queryVo.timeRange"
                        type="datetimerange"
                        range-separator="-"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        @change="changeTimeRange">
                    </el-date-picker>
                </el-col>
                <el-col :span="2">
                    <el-button type="primary" @click="exportData">导出</el-button>
                </el-col>
            </el-row>
        </div>
        <!--表格数据-->
        <el-table ref="selectDate" class="table-m20" v-loading="loading" :data="listData" border header-cell-style="text-align:center;background-color:#ddd;color:#333;" style="width:100%;"  @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column align="center" prop="type" label="客户端类别"></el-table-column>
            <el-table-column align="center" prop="dowloadsCount" label="下载量"></el-table-column>
            <el-table-column align="center" prop="ratio" label="占比"></el-table-column>
            <el-table-column align="center" prop="ipCount" label="IP数量"></el-table-column>
        </el-table>
    </div>
</template>

<script>
import r from '@/util/RequestUtil.js'
export default {
    data(){
        return {
            queryVo:{
                timeRange: [new Date(new Date().setHours(0, 0, 0, 0)), new Date(new Date().setHours(23, 59, 59, 0))],
                beginTime: new Date(new Date().setHours(0, 0, 0, 0)),
                endTime: new Date(new Date().setHours(23, 59, 59, 0)),
                browserType: 'All'
            },
            multipleSelection: [],
            listData:[],
            statistics:{
                totalDowloads:null,
                samuraiWindow:null,
                samuraiLinux:null,
                atonAndroid:null,
                ip:null
            }
        }
    },
    methods: {
        //获取列表数据
        getData(){
            let _this = this;
            let param = {
                browserType:_this.queryVo.browserType,
                beginTime:_this.queryVo.beginTime,
                endTime:_this.queryVo.endTime,
            }
            _this.loading = true;
            _this.ApiService.downloadM.dowloadManager(param).then(data => {
                _this.loading = false;
                if (data && data.code === 0) {
                    _this.listData = data.data;
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
            _this.ApiService.downloadM.dowloadTotal().then(data => {
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
        //选择浏览器类型
        handleBrowser() {
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
        //选择导出数据
        handleSelectionChange(val) {
            let typeNames = [];
            val.forEach((value, index) => {
                typeNames.push(value.type)
            })
            var typeName=typeNames.join('--');
            this.multipleSelection = typeName;
        },
        //导出
        exportData(){
            let _this = this;
            if(_this.listData.length<=0){
                r.message('该条件下无数据可导出', "warning");
                return false;
            }
            console.log(r.DOWN_BASE_URL+'verify/dowload/dowloadBrowser.do?StrBeginTime='+ r.formatterTimestamp(_this.queryVo.beginTime)+'&browserType='+_this.queryVo.browserType+'&StrEndTime='+r.formatterTimestamp(_this.queryVo.endTime)+'&typeName='+_this.multipleSelection)
            // let param = {
            //     typeNames:_this.multipleSelection,
            //     browserType:_this.queryVo.browserType,
            //     beginTime:_this.queryVo.beginTime,
            //     endTime:_this.queryVo.endTime,
            //     fileNames:'下载管理'
            // }
            // _this.ApiService.downloadM.dowloadBrowser(param);
            window.location.href = r.DOWN_BASE_URL+'verify/dowload/dowloadBrowser.do?StrBeginTime='+ r.formatterTimestamp(_this.queryVo.beginTime)+'&browserType='+_this.queryVo.browserType+'&StrEndTime='+r.formatterTimestamp(_this.queryVo.endTime)+'&typeName='+_this.multipleSelection;
        }
    },
    mounted(){
        this.getData();
        this.getStatisticsData();
    }
}
</script>

<style scoped>

</style>

