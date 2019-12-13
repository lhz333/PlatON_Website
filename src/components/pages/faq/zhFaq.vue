<template>
    <div>
        <!--head-->
        <div class="content-c-breadcrumb">
            <el-breadcrumb separator="-">
                <el-breadcrumb-item>FAQ管理</el-breadcrumb-item>
                <el-breadcrumb-item>中文版本</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <!--操作按钮-->
        <div class="content-util">
            <el-row :gutter="20">
                <el-col :span="18">
                    <el-button type="primary" @click="handleBuild">新建</el-button>
                    <el-button type="primary" :disabled="moveup" @click="handleMoveUp">上移</el-button>
                    <el-button type="primary" :disabled="movedown" @click="handleMovevDown">下移</el-button>
                </el-col>
                <el-col :span="6">
                    <el-input placeholder="输入问题标题关键字搜索" v-model="searchValue" @keyup.native.enter="handleSearch()" :clearable="true">
                        <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
                    </el-input>
                </el-col>
            </el-row>
        </div>
        <!--表格数据-->
        <el-table ref="selectDate" class="table-m20" v-loading.body="loading" :data="listData" border align="center" cell-style="padding:2px 0;" header-cell-style="text-align:center;background-color:#ddd;color:#333;" style="width:100%;" :row-class-name="handleRowClass" @row-click = "handleRowClick" highlight-current-row>
            <el-table-column label="选择" width="50">
                <template slot-scope="scope">
                    <el-radio :label="scope.row.id" v-model="selectedRowId" @change.native="getTemplateRow(scope.$index,scope.row)">&nbsp;</el-radio>
                </template>
            </el-table-column>
            <el-table-column prop='descNo' label="序号" width="80"></el-table-column>
            <el-table-column :show-overflow-tooltip="showTooltip" prop='problemCn' width="320" label="问题"></el-table-column>
            <el-table-column prop='creator' label="编辑人"></el-table-column>
            <el-table-column prop='createTime' :formatter="formatterTimestamp" width="160" label="最近编辑时间"></el-table-column>
            <el-table-column label="状态">
                <template slot-scope="scope">
                    <span v-if="scope.row.status==0">未发布</span>
                    <span v-if="scope.row.status==1">已发布</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="240">
                <template slot-scope="scope">
                    <el-button type="text" v-if="scope.row.status==1" @click="withdraw(scope.row)">撤回</el-button>
                    <el-button type="text" v-if="scope.row.status==0" @click="release(scope.row)">发布</el-button>
                    <el-button type="text" :disabled="scope.row.status==0" @click="seeNews">查看</el-button>
                    <el-button type="text" @click="record(scope.row)">记录</el-button>
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
        <!--记录-->
        <el-dialog title="操作记录" :visible.sync="recordDialog" width="50%" center>
            <div class="record-content">
                <h3>问题：{{recordRow.problemCn}}</h3>
                <ul class="record-detail">
                    <li v-for="(item,index) in recordList" :key="index">
                        <span v-html="formatterTimestamp(item)">{{item.createTime}} &nbsp;&nbsp;</span>
                        <span>“{{item.creator}}”</span>
                        <b v-html="statusChange(item)">{{item.status}}</b>
                        <span>了问题</span>
                        <span v-if="item.status==4">的顺序</span>
                    </li>
                </ul>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="recordDialog = false">返回</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import r from '@/util/RequestUtil.js'
    export default {
        data() {
            return {
                moveup: true,
                movedown: true,
                moveIng: false,
                searchValue: null,
                loading: false,
                recordDialog: false,
                page: {
                    totalPages: 100,
                    currentNo: 1,
                    pageSize: 50
                },
                listData: [],
                selectedRow: null,
                recordRow: {},
                recordList: [],
                selectedRowId:null
            }
        },
        methods: {
            //搜索
            handleSearch() {
                this.getData();
            },
            //新建
            handleBuild() {
                this.$router.push({
                    path: '/newfaq',
                    query: {
                        language: 'zh',
                        qId: null,
                    }
                })
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
                    case 4:
                        return '调整顺序';
                        break;
                    default:
                        break;
                }
            },
            //时间转换
            formatterTimestamp(row, column) {
                return r.formatterTimestamp(row.createTime);
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
                _this.ApiService.faq.sortFaqUporDown(param).then(data => {
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
            handleSizeChange(val) {
                this.page.pageSize = val;
                this.getData();
            },
            handleCurrentChange(val) {
                this.page.currentNo = val;
                this.getData();
            },
            //撤回
            withdraw(row) {
                let _this = this;
                this.$confirm('是否确认撤回标题为 “' + row.problemCn + '” 的问题?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let param = {
                        id: row.id,
                        status: 0
                    }
                    _this.ApiService.faq.uptFaqStatus(param).then(data => {
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
            release(row) {
                let _this = this;
                // if(row.complete === 1) {
                    _this.$confirm('是否确认发布标题是 “' + row.problemCn + '” 的问题?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        let param = {
                            id: row.id,
                            status: 1
                        }
                        _this.ApiService.faq.uptFaqStatus(param).then(data => {
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
                // } else {
                //     r.message('内容不完整，请点击编辑进行补充', "warning");
                // }
            },
            //查看
            seeNews() {
                window.open(r.seeurl+"/#/faq");
            },
            //记录
            record(row) {
                let _this = this;
                this.recordDialog = true;
                this.recordRow = row;
                let param = {
                    faqId: row.id
                }
                _this.ApiService.faq.findFaqCnOpertion(param).then(data => {
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
                    path: '/newfaq',
                    query: {
                        language: 'zh',
                        qId: row.id
                    }
                })
            },
            //删除
            detele(row) {
                let _this = this;
                this.$confirm('是否确认删除标题为 “' + row.problemCn + '” 的问题?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let param = {
                        id: row.id,
                        status: 2
                    }
                    _this.ApiService.faq.uptFaqStatus(param).then(data => {
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
            getData() {
                let _this = this;
                _this.loading = true;
                let param = {
                    pageNo: _this.page.currentNo,
                    pageSize: _this.page.pageSize,
                    problemCn: _this.searchValue
                };
                _this.ApiService.faq.findPlatFaq(param).then(data => {
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
            }
        },
        mounted() {
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