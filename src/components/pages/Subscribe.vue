<template>
    <div>
        <!--head-->
        <div class="content-c-breadcrumb">
            <el-breadcrumb separator="-">
                <el-breadcrumb-item>订阅管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <!--操作按钮-->
        <div class="content-util">
            <el-form label-width="70px">
                <el-row :gutter="20">
                    <el-col :span="2">
                        <el-button type="primary" @click="handleExport">导出</el-button>
                    </el-col>
                    <el-col :span="4">
                        <el-form-item label="按身份：">
                            <el-select @change="handleIdentify" placeholder="请选择" v-model="queryVo.identify">
                                <el-option label="全部" value=""></el-option>
                                <el-option label="开发者" value="1"></el-option>
                                <el-option label="投资人" value="2"></el-option>
                                <el-option label="媒体" value="3"></el-option>
                                <el-option label="其他" value="4"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="10">
                        <el-form-item label="按时间：">
                            <el-col :span="11">
                                <el-date-picker @change="changeBeginDate" type="date" placeholder="选择日期" v-model="queryVo.beginDate" style="width: 100%;"></el-date-picker>
                            </el-col>
                            <el-col class="line" :span="2">-</el-col>
                            <el-col :span="11">
                                <el-date-picker @change="changeEndDate" type="date" placeholder="选择日期" v-model="queryVo.endDate" style="width: 100%;"></el-date-picker>
                            </el-col>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="搜索：">
                            <el-input placeholder="输入邮箱地址关键字搜索" v-model.trim="queryVo.searchValue" @keyup.native.enter="handleSearch()" :clearable="true">
                                <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
        </div>
        <!--表格数据-->
        <el-table ref="selectDate" class="table-m20" :data="listData" border @selection-change="selectionChange" @row-click="clickRow" header-cell-style="text-align:center;background-color:#ddd;color:#333;" cell-style="padding:2px 0;" style="width:100%;">
            <el-table-column type="selection" width="50"></el-table-column>
            <el-table-column prop='descNo' label="序号" width="80"></el-table-column>
            <el-table-column prop='email' label="邮箱地址"></el-table-column>
            <el-table-column prop='name' label="姓名"></el-table-column>
            <el-table-column prop='companyName' label="公司名称"></el-table-column>
            <el-table-column label="身份">
                <template slot-scope="scope">
                    <span v-html="statusChange(scope.row.idType)"></span>
                </template>
            </el-table-column>
            <el-table-column prop='createTime' :formatter="formatterTimestamp" label="提交时间"></el-table-column>
            <el-table-column label="操作" width="80">
                <template slot-scope="scope">
                    <el-button type="text" @click="detele(scope.row)">删除</el-button>
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
    import r from '../../util/RequestUtil.js'
    export default {
        data() {
            return {
                queryVo: {
                    identify: '',
                    beginDate: null,
                    endDate: null,
                    searchValue: null,
                },
                listData: [],
                page: {
                    totalPages: 100,
                    currentNo: 1,
                    pageSize: 50
                },
                selectedIds: null
            }
        },
        methods: {
            //获取数据
            getData() {
                let _this = this;
                let param = {
                    staTime: r.formatterTimestampT(_this.queryVo.beginDate),
                    endTime: r.formatterTimestampT(_this.queryVo.endDate),
                    pageSize: _this.page.pageSize,
                    pageNo: _this.page.currentNo,
                    email: _this.queryVo.searchValue,
                    idType: _this.queryVo.identify
                }
                _this.ApiService.subscribe.findPlatSubscription(param).then(data => {
                    if(data && data.code === 0) {
                        _this.listData = data.data.result;
                        _this.page.totalPages = data.data.totalCount;
                    } else {
                        r.message(data.message, "warning");
                    }
                })
                .catch(err => {
                    console.log(err);
                });
            },
            //按身份查找
            handleIdentify() {
                if(this.queryVo.identify=='0'){
                    this.queryVo.identify = null;
                }
                this.page.currentNo = 1;
                this.getData();
            },
            //搜索
            handleSearch() {
                this.page.currentNo = 1;
                this.getData();
            },
            //修改开始时间
            changeBeginDate() {
                if(this.queryVo.endDate!==null){
                    this.page.currentNo = 1;
                    this.getData();
                }else if(this.queryVo.beginDate!==null){
                    r.message('请选择结束时间', "info");
                }
            },
            //修改结束时间
            changeEndDate() {
                if(this.queryVo.beginDate!==null){
                    this.page.currentNo = 1;
                    this.getData();
                }else if(this.queryVo.endDate!==null){
                    r.message('请选择开始时间', "info");
                }
            },
            //时间转换
            formatterTimestamp(row, column) {
                return r.formatterTimestamp(row.createTime);
            },
            //身份验证
            statusChange(row) {
                switch(row) {
                    case null:
                        return '';
                        break;
                    case 1:
                        return '开发者';
                        break;
                    case 2:
                        return '投资者';
                        break;
                    case 3:
                        return '媒体';
                        break;
                    case 4:
                        return '其他';
                        break;
                    default:
                        break;
                }
            },
            //导出数据
            handleExport() {
                var _this = this;

                window.location.href = r.DOWN_BASE_URL+'subscription/downloadExecl.do?strStaTime='+ r.formatterTimestampT(_this.queryVo.beginDate)+'&ids='+this.selectedIds+'&strEndTime='+r.formatterTimestampT(_this.queryVo.endDate)+'&email='+_this.queryVo.searchValue+'&idTypes='+ _this.queryVo.identify;
            },
            //删除
            detele(row) {
                let _this = this;
                this.$confirm('是否确认删除邮箱为 “' + row.email + '” 的数据?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let param = {
                        id: row.id
                    }
                    _this.ApiService.subscribe.removeSubscription(param).then(data => {
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
            clickRow(row){
                this.$refs.selectDate.toggleRowSelection(row);
            },
            //选择
            selectionChange(val) {
                this.selectDate = val;
                if(this.selectDate.length > 0) {
                    let params = new Array();
                    for(let i = 0; i < this.selectDate.length; i++) {
                        let temp = this.selectDate[i].id;
                        params.push(temp);
                    }
                    this.selectedIds = params.join(',');
                }
            },
            //修改每页显示数据条数
            handleSizeChange(val) {
                this.page.pageSize = val;
                window.sessionStorage.setItem('subPagesize',val);
                this.getData();
            },
            //修改当前页码
            handleCurrentChange(val) {
                this.page.currentNo = val;
                this.getData();
            },
        },
        mounted() {
            let subPagesize = window.sessionStorage.getItem('subPagesize');
            if(subPagesize && subPagesize !== null){
                this.page.pageSize = parseInt(subPagesize);
            }
            this.getData();
        }
    }
</script>

<style>

</style>