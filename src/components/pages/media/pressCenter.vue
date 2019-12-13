<template>
    <div>
        <!--head-->
        <div class="content-c-breadcrumb">
            <el-breadcrumb separator="-">
                <el-breadcrumb-item>媒体报道</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <!--操作按钮-->
        <div class="content-util">
            <el-row :gutter="20">
                <el-col :span="18">
                    <el-button type="primary" @click="buildMedia">新建</el-button>
                </el-col>
                <el-col :span="6">
                    <el-input placeholder="输入标题关键字搜索" v-model.trim="searchValue" @keyup.native.enter="handleSearch()" :clearable="true">
                        <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
                    </el-input>
                </el-col>
            </el-row>
        </div>

        <!--表格数据-->
        <el-table ref="selectDate" class="table-m20" v-loading.body="loading" :data="listData" border header-cell-style="text-align:center;background-color:#ddd;color:#333;" cell-style="padding:2px 0;" style="width:100%;" highlight-current-row @filter-change="filterChange">
            <el-table-column label="序号" type="index" width="50"></el-table-column>
            <el-table-column prop='name' label="媒体" :filters="nameList" :filter-method="filterName" column-key="name" :filter-multiple="false"></el-table-column>
            <el-table-column :show-overflow-tooltip="showTooltip" label="文章标题">
                <template slot-scope="scope">
                    <span>{{scope.row.title}}</span>
                </template>
            </el-table-column>
            <el-table-column prop='reTime' width="120" :formatter="formatterTimestampT" label="文章发布时间"></el-table-column>
            <el-table-column prop='releaseTime' width="160" :formatter="formatterTimestamp" label="官网发布时间"></el-table-column>
            <el-table-column prop='language' label="文章语言" width="100">
                <template slot-scope="scope">
                    <span v-html="languageChange(scope.row)"></span>
                </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="80" :filters="statusList" :filter-method="filterStatus" column-key="status" :filter-multiple="false">
                <template slot-scope="scope">
                    <span v-if="scope.row.status==0">未发布</span>
                    <span v-if="scope.row.status==1">已发布</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="200">
                <template slot-scope="scope">
                    <el-button type="text" v-if="scope.row.status==1" @click="withdraw(scope.row)">撤回</el-button>
                    <el-button type="text" v-if="scope.row.status==0" @click="release(scope.row)">发布</el-button>
                    <el-button type="text" :disabled="scope.row.status==0" @click="seeNews">查看</el-button>
                    <el-button type="text" @click="edit(scope.row)">编辑</el-button>
                    <el-button type="text" @click="detele(scope.row)" :disabled="scope.row.status==1">删除</el-button>
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
export default {
    data(){
        return {
            searchValue:null,
            loading:false,
            page: {
                totalPages: 10,
                currentNo: 1,
                pageSize: 50
            },
            listData:[],
            nameList:[{}],
            showTooltip:true,
            statusList:[{
                text:"未发布",
                value:0
            },{
                text:"已发布",
                value:1
            }],
            selectedStatus:null,
            selectedName:null
        }
    },
    methods:{
        //获取媒体名称列表
        getMediaName(){
            let _this = this;
            let names = [];
            _this.ApiService.media.findMediaNames().then(data => {
                if(data && data.code === 0) {
                    for(let i=0;i<data.data.length;i++){
                        let namedata = {
                            text:data.data[i].name,
                            value:data.data[i].name
                        }
                        names.push(namedata);
                    }
                    _this.nameList = names;
                } else{
                    r.message(data.message, "warning");
                }
            })
            .catch(err => {
                console.log(err);
            });
        },
        // 获取列表数据
        getData(){
            let _this = this;
            _this.loading = true;
            let param = {
                pageNo: _this.page.currentNo,
                pageSize: _this.page.pageSize,
                title:_this.searchValue,
                name:_this.selectedName,
                status:_this.selectedStatus
            };
            _this.ApiService.media.findPlatMedia(param).then(data => {
                if(data && data.code === 0) {
                    _this.listData = data.data.result;
                    _this.page.totalPages = data.data.totalCount;
                    _this.loading = false;
                } else {
                    r.message(data.message, "warning");
                    _this.loading = false;
                }
            })
            .catch(err => {
                _this.loading = false;
                console.log(err);
            });
        },
        //新建
        buildMedia(){
            this.$router.push({
                path: '/buildMedia',
                query: {
                    pressId: null,
                }
            })
        },
        // 搜索
        handleSearch(){
            this.getData();
        },
        //时间转换
        //时间转换
        formatterTimestampT(row, column) {
            return r.formatterTimestampT(row.reTime);
        },
        formatterTimestamp(row, column) {
            return r.formatterTimestamp(row.releaseTime);
        },
        languageChange(row) {
            switch(row.language) {
                case 'zh':
                    return '中文';
                    break;
                case 'en':
                    return '英文';
                    break;
                default:
                    break;
            }
        },
        statusChange(row) {
            switch(row.status) {
                case 0:
                    return '创建';
                    break;
                case 1:
                    return '发布';
                    break;
                case 2:
                    return '编辑';
                    break;
                case 3:
                    return '撤回';
                    break;
                default:
                    break;
            }
        },
        // 撤回
        withdraw(row){
            let _this = this;
            this.$confirm('是否确认撤回标题为 “' + row.title + '” 的媒体报道?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let param = {
                    id: row.id,
                    status: 0
                }
                _this.ApiService.media.uptMediaStatus(param).then(data => {
                    if(data && data.code === 0) {
                        _this.getData();
                        _this.$message({
                            type: 'success',
                            message: '已成功撤回'
                        });
                    } else {
                        r.message(data.message, "warning");
                    }
                })
                .catch(err => {
                    console.log(err);
                });
            }).catch(() => {

            });
        },
        //发布
        release(row){
            let _this = this;
            _this.$confirm('是否确认发布标题为 “' + row.title + '” 的媒体报道?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let param = {
                    id: row.id,
                    status: 1
                }
                _this.ApiService.media.uptMediaStatus(param).then(data => {
                    if(data && data.code === 0) {
                        _this.getData();
                        _this.$message({
                            type: 'success',
                            message: '已成功发布'
                        });
                    } else {
                        r.message(data.message, "warning");
                    }
                })
                .catch(err => {
                    console.log(err);
                });
            }).catch(() => {

            });
        },
        //查看
        seeNews(){
           window.open(r.seeurl+"/#/pressCenter");
        },
        // 编辑
        edit(row){
            this.$router.push({
                path: '/buildMedia',
                query: {
                    pressId: row.id
                }
            })
        },
        // 删除
        detele(row){
            let _this = this;
            this.$confirm('是否确认删除标题为 “' + row.title + '” 的媒体报道?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let param = {
                    id: row.id,
                    status: 2
                }
                _this.ApiService.media.uptMediaStatus(param).then(data => {
                    if(data && data.code === 0) {
                        _this.getData();
                        _this.$message({
                            type: 'success',
                            message: '已成功删除'
                        });
                    } else {
                        r.message(data.message, "warning");
                    }
                })
                .catch(err => {
                    console.log(err);
                });
            }).catch(() => {

            });
        },
        handleSizeChange(val) {
            this.page.pageSize = val;
            this.getData();
        },
        handleCurrentChange(val) {
            this.page.currentNo = val;
            this.getData();
        },
        //状态筛选
        filterStatus(value, row, column){
            return row.status === value;
        },
        filterName(value, row, column){
            return row.name === value;
        },
        // 筛选
        filterChange(filters){
            let _this = this;
            if(filters.name!==null && filters.name!==undefined){
                _this.selectedName = filters.name[0];
            }
            if(filters.status!==null && filters.status!==undefined){
                _this.selectedStatus = filters.status[0];
            }
            _this.getData();
        }
    },
    mounted(){
        this.getMediaName();
        this.getData();
    }
}
</script>

<style scoped>

</style>



