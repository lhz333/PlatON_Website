<template>
    <div>
        <!--head-->
        <div class="content-c-breadcrumb">
            <el-breadcrumb separator="-">
                <el-breadcrumb-item>新闻管理</el-breadcrumb-item>
                <el-breadcrumb-item>英文版本</el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <!--操作按钮-->
        <div class="content-util">
            <el-row :gutter="20">
                <el-col :span="18">
                    <el-button type="primary" @click="buildNews">新建</el-button>
                    <!-- <el-button type="primary" :disabled="moveup" @click="handleMoveUp">上移</el-button>
                    <el-button type="primary" :disabled="movedown" @click="handleMovevDown">下移</el-button> -->
                </el-col>
                <el-col :span="6">
                    <el-input placeholder="输入新闻标题关键字搜索" v-model="searchValue" @keyup.native.enter="handleSearch()" :clearable="true">
                        <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
                    </el-input>
                </el-col>
            </el-row>
        </div>
        <!--表格数据-->
        <el-table ref="selectDate" class="table-m20" v-loading.body="loading" :data="listData" border header-cell-style="text-align:center;background-color:#ddd;color:#333;" cell-style="padding:2px 0;" style="width:100%;" :row-class-name="handleRowClass" @filter-change="filterChange" @row-click = "handleRowClick" highlight-current-row>
            <!--<el-table-column type="selection" width="50"></el-table-column>-->
            <el-table-column label="选择" width="50">
                <template slot-scope="scope">
                    <el-radio :label="scope.row.id" v-model="selectedRowId" @change.native="getTemplateRow(scope.$index,scope.row)">&nbsp;</el-radio>
                </template>
            </el-table-column>
            <el-table-column prop='descNo' label="序号" width="60"></el-table-column>
            <el-table-column prop="typeId" label="分类" :filters="typeList" :filter-method="filterType" column-key="typeId" :filter-multiple="false">
                <template slot-scope="scope">
                    <span>{{scope.row.typeName}}</span>
                </template>
            </el-table-column>
            <el-table-column :show-overflow-tooltip="showTooltip" label="新闻标题">
                <template slot-scope="scope">
                    <span>{{scope.row.title}}</span>
                </template>
            </el-table-column>
            <el-table-column prop='creator' label="创建人"></el-table-column>
            <el-table-column prop='createTime' width="160" :formatter="formatterTimestamp" label="创建时间"></el-table-column>
            <el-table-column label="状态" prop="status"
    column-key="status" width="70" :filter-method="filterStatus" :filter-multiple="false" :filters="statusList">
                <template slot-scope="scope">
                    <span v-if="scope.row.status==0">未发布</span>
                    <span v-if="scope.row.status==1">已发布</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" fixed="right" width="300">
                <template slot-scope="scope">
                    <el-button type="text" v-if="scope.row.status==1" @click="withdraw(scope.row)">撤回</el-button>
                    <el-button type="text" v-if="scope.row.status==0" @click="release(scope.row)">发布</el-button>
                    <el-button style="min-width: 56px;" type="text" v-if="scope.row.bindStatus==0" @click="relation(scope.row)">关联</el-button>
                    <el-button type="text" v-if="scope.row.bindStatus>0" @click="cancelRelation(scope.row)">取消关联</el-button>
                    <el-button type="text" v-if="scope.row.status==1" @click="seeNews">查看</el-button>

                    <el-button type="text" v-if="scope.row.status==0" @click="preview(scope.row)">预览</el-button>
                    <el-button type="text" @click="record(scope.row)">记录</el-button>
                    <el-button type="text" @click="edit(scope.row)" :disabled="scope.row.status==1">编辑</el-button>
                    <el-button type="text" @click="detele(scope.row)" :disabled="scope.row.status==1">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!--分页-->
        <div class="pager-wrap content-pages">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="page.currentNo" :page-sizes="[10, 20, 50, 100]" :page-size="page.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="page.totalPages">
            </el-pagination>
        </div>
        <!--记录-->
        <el-dialog title="操作记录" :visible.sync="recordDialog" width="50%" center>
            <div class="record-content">
                <h3>新闻标题：{{recordRow.title}}</h3>
                <ul class="record-detail">
                    <li v-for="(item,index) in recordList" :key="index">
                        <span v-html="formatterTimestamp(item)">{{item.createTime}} &nbsp;&nbsp;</span>
                        <span>“{{item.creator}}”</span>
                        <b v-html="statusChange(item)">{{item.status}}</b>
                        <span>了新闻</span>
                    </li>
                </ul>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="recordDialog = false">返回</el-button>
            </span>
        </el-dialog>

        <!--关联-->
        <el-dialog title="关联新闻" :visible.sync="relationDialog" width="80%" center :before-close="cancelSureRecore">
            <div class="record-content">
                <h3 class="mb20">新闻标题：{{relationRow.title}}</h3>
                <el-table :data="relationList" border height="400" highlight-current-row  @row-click="rowClickRelation">
                    <el-table-column label="选择" width="50">
                        <template slot-scope="scope">
                            <el-radio :label="scope.row.id" v-model="relationRowId">&nbsp;</el-radio>
                        </template>
                    </el-table-column>
                    <el-table-column prop='descNo' label="序号" width="80"></el-table-column>
                    <el-table-column :show-overflow-tooltip="showTooltip" prop='title' label="新闻标题"></el-table-column>
                </el-table>
                <!--分页-->
                <div class="pager-wrap content-pages">
                    <el-pagination @size-change="handleSizeChangeGl" @current-change="handleCurrentChangeGl" :current-page.sync="pageGl.currentNo" :page-sizes="[10, 20, 50, 100]" :page-size="pageGl.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="pageGl.totalPages">
                    </el-pagination>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="cancelSureRecore">取消</el-button>
                <el-button type="primary" @click="sureRecord">确 认</el-button>
            </span>
        </el-dialog>

    </div>
</template>

<script>
    import r from '../../util/RequestUtil.js'
    export default {
        data() {
            return {
                searchValue: '',
                loading: false,
                moveup: true,
                moveIng: false,
                movedown: true,
                showTooltip: true,
                recordDialog: false,
                relationDialog: false,
                relationRow: {},
                listData: [],
                page: {
                    totalPages: 100,
                    currentNo: 1,
                    pageSize: 50
                },
                pageGl: {
                    totalPages: 100,
                    currentNo: 1,
                    pageSize: 50
                },
                selectedRowId: null,
                selectedRow: null,
                recordRow: {},
                recordList: [],
                relationRowId:null,
                relationList:[],
                statusList:[{
                    text:"未发布",
                    value:0
                },{
                    text:"已发布",
                    value:1
                }],
                typeList:[{
                    text:'',
                    value:0
                }],
                typeId:null,
                status:null
            }
        },
        methods: {
            //新闻分类
            getType(){
                let _this = this;
                let param = {};
                let types = [];
                _this.ApiService.newTypeEn.findTypeEn(param).then(data => {
                    if(data && data.code === 0) {
                        for(let i=0;i<data.data.length;i++){
                            let typedata = {
                                text:data.data[i].name,
                                value:data.data[i].id
                            }
                            types.push(typedata);
                        }
                        _this.typeList = types;
                    } else{
                        r.message(data.message, "warning");
                    }
                })
                .catch(err => {
                    console.log(err);
                });
            },
            //获取数据
            getData() {
                var _this = this;
                _this.loading = true;
                var param = {
                    typeId:_this.typeId,
                    status:_this.status,
                    title: _this.searchValue,
                    pageNo: _this.page.currentNo,
                    pageSize: _this.page.pageSize
                }
                _this.ApiService.newsEn.findNewsEnInfo(param).then(data => {
                    _this.loading = false;
                    if(data && data.code === 0) {
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
            //时间转换
            formatterTimestamp(row, column) {
                return r.formatterTimestamp(row.createTime);
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
            //搜索
            handleSearch() {
                this.getData();
            },
            //新建
            buildNews() {
                this.$router.push({
                    path: '/releaseNews',
                    query: {
                        language: 'en',
                        newsId: null,
                    }
                })
            },
            handleSizeChange(val) {
                this.page.pageSize = val;
                window.sessionStorage.setItem('enPagesize',val);
                this.getData();
            },
            handleCurrentChange(val) {
                this.page.currentNo = val;
                this.getData();
            },
            //撤回
            withdraw(row) {
                let _this = this;
                this.$confirm('是否确认撤回标题为 “' + row.title + '” 的新闻?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let param = {
                        id: row.id,
                        status: 0
                    }
                    _this.ApiService.newsEn.uptPlatNewsEnStatus(param).then(data => {
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
            //查看
            seeNews() {
                window.open(r.seeurl+"/#/news");
            },
            //发布
            release(row) {
                let _this = this;
                if(row.typeName==='Events'&&row.topImgUrl===null){
                    r.message('活动必须上传活动图片，才能发布', "warning");
                    return false;
                }
                if(row.complete === 1) {
                    _this.$confirm('是否确认发布标题为 “' + row.title + '” 的新闻?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        let param = {
                            id: row.id,
                            status: 1
                        }
                        _this.ApiService.newsEn.uptPlatNewsEnStatus(param).then(data => {
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
                } else {
                    r.message('内容不完整，请点击编辑进行补充', "warning");
                }
            },
            //预览
            preview(row) {
                if(row.typeName === 'Events'){
                    const { href } = this.$router.resolve({
                        path: "activityNew",
                        query: {
                            language: 'en'
                        }
                    });
                    window.open(href, '_blank');
                }else{
                    const { href } = this.$router.resolve({
                        path: "previewnews",
                        query: {
                            language: 'en'
                        }
                    });
                    window.open(href, '_blank');
                }
            },
            //关联
            relation(row) {
//              let _this = this;
                this.relationRow = row;
                this.relationDialog = true;
                let enGlPagesize = window.sessionStorage.getItem('enGlPagesize');
                if(enGlPagesize && enGlPagesize !== null){
                    this.pageGl.pageSize = parseInt(enGlPagesize);
                }
                this.relationGetData();
            },
            relationGetData(){
                let _this = this;
                let param = {
                    title: null,
                    pageNo: _this.pageGl.currentNo,
                    pageSize: _this.pageGl.pageSize,
                    status: null
                };
                _this.ApiService.newsEn.findNotBindPlatNewsCn(param).then(data => {
                    if(data && data.code === 0) {
                        _this.relationList = data.data.result;
                        _this.pageGl.totalPages = data.data.totalCount;
                    } else {
                        r.message(data.message, "warning");
                    }
                })
                .catch(err => {
                    console.log(err);
                });
            },
            handleSizeChangeGl(val) {
                this.pageGl.pageSize = val;
                window.sessionStorage.setItem('enGlPagesize',val);
                this.relationGetData();
            },
            handleCurrentChangeGl(val) {
                this.pageGl.currentNo = val;
                this.relationGetData();
            },
            cancelSureRecore(){
                this.relationDialog = false;
                this.relationRowId = null;
            },
            rowClickRelation(row){
                this.relationRowId = row.id;
            },
            //确认关联
            sureRecord(){
                let _this = this;
                let param = {
                    enId:_this.relationRow.id,
                    cnId: _this.relationRowId
                };
                if(param.cnId !==null){
                    _this.ApiService.newsEn.bindNewsCnEn(param).then(data => {
                        if(data && data.code === 0) {
                            _this.relationDialog = false;
                            _this.getData();
                            r.message('已成功关联', "success");
                        } else {
                            r.message(data.message, "warning");
                        }
                    })
                    .catch(err => {
                        console.log(err);
                    });
                }else{
                    r.message('请选择需要关联的新闻', "warning");
                }
            },
            //取消关联
            cancelRelation(row){
                var _this = this;
                this.$confirm('是否确认取消关联?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let param = {
                        enId:row.id
                    };
                    _this.ApiService.newsEn.removeNewsCnen(param).then(data => {
                        if(data && data.code === 0) {
                            r.message('已取消关联', "success");
                            _this.getData();
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
            //记录
            record(row) {
                let _this = this;
                _this.recordDialog = true;
                _this.recordRow = row;
                let param = {
                    newId: row.id
                }
                _this.ApiService.newsEn.findNewsEnOpertion(param).then(data => {
                    if(data && data.code === 0) {
                        _this.recordList = data.data;
                    } else {
                        r.message(data.message, "warning");
                    }
                })
                .catch(err => {
                    console.log(err);
                });
            },
            //编辑
            edit(row) {
                this.$router.push({
                    path: '/releaseNews',
                    query: {
                        language: 'en',
                        newsId: row.id
                    }
                })
            },
            //删除
            detele(row) {
                let _this = this;
                if(row.bindStatus>0){
                    r.message('此新闻已于其他新闻关联，请取消关联之后再删除', "warning");
                    return;
                }
                this.$confirm('是否确认删除标题为 “' + row.title + '” 的新闻?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let param = {
                        id: row.id,
                        status: 2
                    }
                    _this.ApiService.newsEn.uptPlatNewsEnStatus(param).then(data => {
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
            //设置每一行的索引值
            handleRowClass({row, rowIndex}){
                row.index = rowIndex;
            },
            //点击当前行获取数据
            handleRowClick(row, event, column){
                this.selectedRowId = row.id;
                this.getTemplateRow(row.index, row);
            },
            //单选框
            getTemplateRow(index, row) {
                this.selectedRow = row;
                //第一页第一条数据不可上移    最后一页最后一条数据不可下移
                if(this.listData.length ==1 && this.page.currentNo === 1){

                }else if(this.page.currentNo === 1 && index === 0) {
                    this.moveup = true;
                    this.movedown = false;
                } else if(this.page.currentNo == Math.ceil(this.page.totalPages/this.page.pageSize) && (index + 1) === this.listData.length) {
                    this.moveup = false;
                    this.movedown = true;
                }else {
                    this.movedown = false;
                    this.moveup = false;
                }
            },
            //sortType 1 是 上移
            handleMoveUp() {
                if(!this.moveIng) {
                    this.moveIng = true;
                    let param = {
                        sortType: 1,
                        id: this.selectedRow.id,
                        sortId: this.selectedRow.sortId
                    }
                    this.moveData(param);
                } else {
                    return;
                }
            },
            //sortType 2 是下移
            handleMovevDown() {
                if(!this.moveIng) {
                    this.moveIng = true;
                    let param = {
                        sortType: 2,
                        id: this.selectedRow.id,
                        sortId: this.selectedRow.sortId
                    }
                    this.moveData(param);
                } else {
                    return;
                }
            },
            //移动调用的方法
            moveData(param) {
                var _this = this;
                _this.ApiService.newsEn.sortNewsEnUporDown(param).then(data => {
                    if(data && data.code === 0) {
                        _this.selectedRowId = null;
                        _this.getData();
                        _this.moveIng = false;
                        _this.movedown = true;
                        _this.moveup = true;
                    } else {
                        r.message(data.message, "warning");
                    }
                })
                .catch(err => {
                    console.log(err);
                });
            },
            //状态筛选
            filterStatus(value, row, column){
                return row.status === value;
            },
            filterType(value, row, column){
                return row.typeId === value;
            },
            // 筛选
            filterChange(filters){
                let _this = this;
                if(filters.typeId!==null && filters.typeId!==undefined){
                    _this.typeId = filters.typeId[0];
                }
                if(filters.status!==null && filters.status!==undefined){
                    _this.status = filters.status[0];
                }
                _this.getData();
            }
        },
        mounted() {
            let enPagesize = window.sessionStorage.getItem('enPagesize');
            if(enPagesize && enPagesize !== null){
                this.page.pageSize = parseInt(enPagesize);
            }
            this.getType();
            this.getData();
        }
    }
</script>

<style scoped="scoped">
    .record-content {
        padding: 0 40px;
    }

    .record-detail {
        height: 300px;
        overflow-y: auto;
        margin-top: 30px;
        padding: 20px;
        border: 1px solid #ccc;
    }
</style>