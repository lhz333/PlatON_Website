<template>
    <div>
         <!--head-->
        <div class="content-c-breadcrumb">
            <el-breadcrumb separator="-">
                <el-breadcrumb-item>Energon管理</el-breadcrumb-item>
                <el-breadcrumb-item>手动转账</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <!--操作按钮-->
        <div class="content-util mb20">
            <el-row :gutter="20">
                <el-col :span="5">
                    <el-button type="primary" @click="handleStartTransfer">开始转账</el-button>
                    <el-button type="primary" @click="exportData">导出</el-button>
                </el-col>
                <el-col :span="7">
                    <el-input placeholder="钱包地址" v-model="queryVo.fromAddress" @keyup.native.enter="handleSearch()" @blur="handleSearch" :clearable="true" class="input-with-select">
                        <el-button slot="append" @click="handleSearch" icon="el-icon-search"></el-button>
                    </el-input>
                </el-col>
                <el-col :span="12">
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
            </el-row>
            
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
            <el-table-column prop="fromAddress" label="发Energon钱包地址" width="330" align="center">
                <template slot-scope="scope">
                     <span>{{scope.row.fromAddress}} ({{scope.row.addressName}})</span>
                </template>
            </el-table-column>
            <el-table-column prop="toAddress" width="260" label="接收钱包地址" align="center"></el-table-column>
            <el-table-column align="center" width="135" :formatter="formatterTimestamp" prop="createTime" label="操作时间"></el-table-column>
            <el-table-column align="center" label="状态" width="90">
                <template slot-scope="scope">
                    <span v-html="statusChange(scope.row.status)"></span>
                </template>
            </el-table-column>
            <el-table-column align="center" prop="mark" label="备注"></el-table-column>
            <el-table-column align="center" prop="sendQuantity" width="160" label="发送数量(Energon)"></el-table-column>
        </el-table>

        <!--分页-->
        <div class="pager-wrap content-pages">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="page.currentNo" :page-sizes="[10, 20, 50, 100]" :page-size="page.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="page.totalPages">
            </el-pagination>
        </div>

        <!-- 新增钱包 -->
        <el-dialog title="手动转账" :before-close="closehandleTransfer" :visible.sync="dialogTransfer">
            <el-form :model="ruleForm" :rules="rules" label-width="160px" ref="ruleForm">
                <el-form-item label="发Energon账户：" prop="fromAddress">
                    <el-select style="width:100%" v-model="ruleForm.fromAddress" placeholder="请选择">
                        <el-option
                        v-for="item in fromAddressOptions"
                        :key="item.address"
                        :label="item.address+'('+item.name+')'"
                        :value="item.address">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="接收方：" prop="toAddress">
                    <el-input v-model.trim="ruleForm.toAddress" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="转账数量(Energon)：" prop="sendQuantity">
                    <el-input v-model.trim="ruleForm.sendQuantity" @keyup.native="onlyNonNegative" maxlength="20" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="备注：" prop="mark">
                    <el-input v-model.trim="ruleForm.mark" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer text-center">
                <el-button @click="closehandleTransfer">取 消</el-button>
                <el-button type="primary" @click="handleTransfer">转 帐</el-button>
            </div>
        </el-dialog>

        <!-- 转账确认 -->
        <el-dialog title="转账确认" :before-close="closeSureTransfer" :visible.sync="dialogSureTransfer">
            <el-form  style="margin:auto;" label-width="160px">
                <el-form-item label="发Energon账户：">
                    <p>{{ruleForm.fromAddress}}</p>
                </el-form-item>
                <el-form-item label="接收方：">
                    <p>{{ruleForm.toAddress}}</p>
                </el-form-item>
                <el-form-item label="转账数量(Energon)：">
                    <p>{{ruleForm.sendQuantity}}</p>
                </el-form-item>
                <el-form-item label="备注：">
                    <p>{{ruleForm.mark}}</p>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer text-center">
                <el-button @click="closeSureTransfer">返 回</el-button>
                <el-button type="primary" @click="handleSureTransfer">确 认</el-button>
            </div>
        </el-dialog>
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
                fromAddress:null,
                timeRange:null,
                beginTime:null,
                endTime:null
            },
            page: {
                totalPages: 100,
                currentNo: 1,
                pageSize: 50
            },
            loading:false,
            dialogTransfer:false,
            dialogSureTransfer:false,
            ruleForm:{
                fromAddress:null,
                toAddress:null,
                sendQuantity:null,
                mark:null
            },
            fromAddressOptions:[{
                value:1,
                label:'lll'
            }],
            rules: {
                fromAddress: [{
                    required: true,
                    validator: (rule, value, callback) => {
                        if(!value) {
                            return callback(new Error('请选择发Energon账户'));
                        } else {
                            callback();
                        }
                    },
                    trigger: 'change'
                }],
                toAddress: [{
                    required: true,
                    validator: (rule, value, callback) => {
                        if(!value) {
                            return callback(new Error('请输入接收方账户'));
                        }else if (value.length!==40){
                            return callback(new Error('钱包地址长度固定为40个字符'));
                        }else if (value===this.ruleForm.fromAddress){
                            return callback(new Error('接收方地址不可与发Energon账户相同'));
                        } else {
                            callback();
                        }
                    },
                    trigger: 'change'
                }],
                sendQuantity: [{
                    required: true,
                    validator: (rule, value, callback) => {
                        if(!value) {
                            return callback(new Error('请输入转账数量'));
                        }else if (value==0){
                            return callback(new Error('转账数量不能为0'));
                        } else {
                            callback();
                        }
                    },
                    trigger: 'change'
                }],
            },
            listData:[]
        }
    },
    methods:{
        //获取列表
        getData(){
            let _this = this;
            let param = {
                pageNo:_this.page.currentNo,
                pageSize:_this.page.pageSize,
                strCreateTime:_this.queryVo.beginTime,
                endCreateTime:_this.queryVo.endTime,
                fromAddress:_this.queryVo.fromAddress
            }
            _this.loading = true;
            _this.ApiService.testcoin.pageTransferInfo(param).then(data => {
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
                strCreateTime:_this.queryVo.beginTime,
                endCreateTime:_this.queryVo.endTime,
                fromAddress:_this.queryVo.fromAddress
            }
            _this.ApiService.testcoin.findTransferCount(param).then(data => {
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
        //每次发放次数 Energon单位
        onlyNonNegative() {
            if(this.ruleForm.sendQuantity!==null && this.ruleForm.sendQuantity!=='' &&this.ruleForm.sendQuantity.length<=20){
                this.ruleForm.sendQuantity = this.ruleForm.sendQuantity.replace(/[^\d.]/g, ""); //清除“数字”和“.”以外的字符     
                this.ruleForm.sendQuantity = this.ruleForm.sendQuantity.replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的     
                this.ruleForm.sendQuantity = this.ruleForm.sendQuantity.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
                this.ruleForm.sendQuantity = this.ruleForm.sendQuantity.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); //只能输入两个小数
                // if(this.ruleForm.sendQuantity.indexOf(".") < 0 && this.ruleForm.sendQuantity != "") { //以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额    
                //     this.ruleForm.sendQuantity = parseFloat(this.ruleForm.sendQuantity);
                // }
            }
        },
        //状态
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
        //时间转换
        formatterTimestamp(row, column) {
            return r.formatterTimeToMinutes(row.createTime);
        },
        // 搜索
        handleSearch(){
            let _this = this;
            _this.getData();
            _this.getStatisticsData();
        },
        // 时间范围
        changeTimeRange(){
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
        //开始转账按钮
        handleStartTransfer(){
            let _this = this;
            _this.dialogTransfer = true;
            
            _this.ApiService.testcoin.findAccountList().then(data => {
                if (data && data.code === 0) {
                    _this.fromAddressOptions = data.data;
                } else {
                    r.message(data.message, "warning");
                }
                })
                .catch(err => {
                    console.log(err);
                });
        },
        //取消
        closehandleTransfer(){
            this.dialogTransfer = false;
            this.$refs.ruleForm.resetFields();
        },
        //关闭转账确认
        closeSureTransfer(){
            this.dialogSureTransfer = false;
        },
        handleTransfer(){
            let _this = this;
            _this.$refs.ruleForm.validate((valid) => {
                if(valid) {
                   this.dialogSureTransfer = true;
                }else{
                    r.message("请按要求填写必填字段", "warning");
                }
            });
        },
        //确认转账
        handleSureTransfer(){
            let _this = this;
            _this.ApiService.testcoin.addTransferInfo(this.ruleForm).then(data => {
                if (data && data.code === 0) {
                    r.message('转账成功', "success");
                    _this.dialogSureTransfer = _this.dialogTransfer = false;
                    _this.$refs.ruleForm.resetFields();
                    _this.getData();
                    _this.getStatisticsData();
                } else {
                    r.message(data.message, "warning");
                }
                })
                .catch(err => {
                    console.log(err);
                });
        },
        //导出
        exportData(){
            let _this = this;
            if(_this.listData.length<=0){
                r.message('该条件下无数据可导出', "warning");
                return false;
            }
            let param = {
                strCreateTime:_this.queryVo.beginTime,
                endCreateTime:_this.queryVo.endTime,
                fromAddress:_this.queryVo.fromAddress,
                fileNames:'手动转账'
            }
            _this.ApiService.testcoin.dowloadTransfer(param);
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