<template>
    <div>
         <!--head-->
        <div class="content-c-breadcrumb">
            <el-breadcrumb separator="-">
                <el-breadcrumb-item>Energon管理</el-breadcrumb-item>
                <el-breadcrumb-item>账户管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <!--操作按钮-->
        <div class="content-util">
            <el-row :gutter="20">
                <el-col :span="4">
                    <el-button type="primary" @click="buildAccount">新增账户</el-button>
                </el-col>
            </el-row>
        </div>
        <!--表格数据-->
        <el-table ref="selectDate" class="table-m20" v-loading="loading" :data="listData" border header-cell-style="text-align:center;background-color:#ddd;color:#333;" cell-style="padding:2px 0;" style="width:100%;" highlight-current-row>
            <el-table-column prop="address" label="钱包地址" align="center"></el-table-column>
            <el-table-column prop='name' align="center" label="钱包名称"></el-table-column>
            <el-table-column label="账户类型" width="140" align="center">
                 <template slot-scope="scope">
                     <span v-html="filtertype(scope.row.type)"></span>
                 </template>
            </el-table-column>
            
            <el-table-column label="操作" width="200">
                <template slot-scope="scope">
                    <el-button type="text" class="copy-link" :data-clipboard-text="scope.row.address" @click="copyLink(scope.row)">复制地址</el-button>
                    <el-button type="text" @click="queryBalance(scope.row)">查询余额</el-button>
                    <el-button type="text" v-if="scope.row.type!==1" @click="deleteData(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!--分页-->
        <div class="pager-wrap content-pages">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="page.currentNo" :page-sizes="[10, 20, 50, 100]" :page-size="page.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="page.totalPages">
            </el-pagination>
        </div>

        <!-- 新增钱包 -->
        <el-dialog title="新增钱包" :before-close="closeBuildAccount"  :visible.sync="dialogNewAccount">
            <p class="mb20" style="color:red;">钱包文件必须放到/opt/key目录下面，钱包文件名和钱包地址必须保持一致</p>
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm">
                <el-form-item label="新增钱包地址：" prop="address">
                    <el-input v-model.trim="ruleForm.address" autocomplete="off"></el-input>
                </el-form-item>
                 <el-form-item label="钱包地址密码：" prop="password">
                    <el-input type="password" v-model.trim="ruleForm.password" maxlength="50" autocomplete="off"></el-input>
                </el-form-item>
                 <el-form-item label="钱包名称：" prop="name">
                    <el-input v-model.trim="ruleForm.name" maxlength="20" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer text-center">
                <el-button @click="closeBuildAccount">取 消</el-button>
                <el-button type="primary" @click="sureBuildAccount">确 定</el-button>
            </div>
        </el-dialog>

        <!-- 删除 -->
        <el-dialog title="删除钱包"  :visible.sync="deleteAccount" v-loading="loading2">
            <div style="width:95%;margin:auto;" >
                <p class="mb20 text-center" style="color:red;">您确认要删除该账户吗？</p>
                <p class="mb20">账户地址：{{selectRow.address}}</p>
                <p class="balance">账户余额 (Energon)：{{balance}}</p>
            </div>
            <div slot="footer" class="dialog-footer text-center" >
                <el-button @click="deleteAccount = false">取 消</el-button>
                <el-button type="primary" @click="sureDeleteAccount">确 定</el-button>
            </div>
        </el-dialog>

        <!-- 查询余额 -->
        <el-dialog title="查询余额" :visible.sync="dialogQueryBalance" v-loading="loading2">
            <div style="width:95%;margin:auto;">
                <p class="mb20">账户地址：{{selectRow.address}}</p>
                <p class="balance">账户余额 (Energon)：{{balance}}</p>
            </div>
            <div slot="footer" class="dialog-footer text-center">
                <el-button type="primary" @click="dialogQueryBalance = false">确定</el-button>
            </div>
        </el-dialog>
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
            loading2:false,
            listData:[],
            dialogNewAccount:false,
            deleteAccount:false,
            dialogQueryBalance:false,
            page: {
                totalPages: 100,
                currentNo: 1,
                pageSize: 50
            },
            ruleForm:{
                address:null,
                password:null,
                name:null
            },
            selectRow:{
                address:null,
                balance:null
            },
            balance:null,
            rules: {
                address: [{
                    validator: (rule, value, callback) => {
                        if(!value) {
                            return callback(new Error('请输入钱包地址'));
                        }else if (value.length!==40){
                            return callback(new Error('钱包地址长度固定为40个字符'));
                        }else {
                            callback();
                        }
                    },
                    trigger: 'change'
                }],
                password:[{
                     validator: (rule, value, callback) => {
                        if(!value) {
                            return callback(new Error('请输入钱包地址密码'));
                        }else {
                            callback();
                        }
                    },
                    trigger: 'change'
                }],
                name: [{
                    validator: (rule, value, callback) => {
                        if(!value) {
                            return callback(new Error('请输入钱包名称'));
                        } else {
                            callback();
                        }
                    },
                    trigger: 'change'
                }],
            }
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
            _this.ApiService.testcoin.pageAccountInfo(param).then(data => {
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
        //修改页面显示条数
        handleSizeChange(val) {
            this.page.pageSize = val;
            this.getData();
        },
        handleCurrentChange(val) {
            this.page.currentNo = val;
            this.getData();
        },
        filtertype(type){
            switch(type) {
                case 1:
                    return '主账户';
                    break;
                case 2:
                    return '备用账户';
                    break;
                default:
                    break;
            }
        },
        //复制链接
        copyLink(row){
            let clipboard = new Clipboard('.copy-link');
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
         //新增账户
        buildAccount(){
            this.dialogNewAccount = true;
        },
        //取消新增账户
        closeBuildAccount(){
            this.dialogNewAccount = false;
            this.$refs.ruleForm.resetFields();
        },
        //确认新增账户
        sureBuildAccount(){
            let _this = this;
            _this.$refs.ruleForm.validate((valid) => {
                if(valid) {
                    _this.ApiService.testcoin.addAccountInfo(this.ruleForm).then(data => {
                        if (data && data.code === 0) {
                            r.message('新增成功', "success");
                            _this.dialogNewAccount = false;
                            _this.$refs.ruleForm.resetFields();
                            _this.getData();
                        } else {
                            r.message(data.message, "warning");
                        }
                    })
                    .catch(err => {
                        console.log(err);
                    });
                }else{
                    r.message("钱包地址或者钱包名称不能为空", "warning");
                }
            });
        },
        //查询余额
        queryBalance(row){
            let _this = this;
            _this.dialogQueryBalance = true;
            _this.selectRow = row;
            _this.balance = null;
            _this.loading2 = true;
            let param = {
                address:row.address,
            }
            _this.ApiService.testcoin.findAccouontBalance(param).then(data => {
                _this.loading2 = false;
                if (data && data.code === 0) {
                    console.log(_this.balance);
                    _this.balance = data.data;
                } else {
                    r.message(data.message, "warning");
                }
                })
                .catch(err => {
                    _this.loading2 = false;
                    console.log(err);
                });
        },
        //删除
        deleteData(row){
            let _this=this;
            _this.selectRow = row;
            _this.balance = null;
            let param = {
                address:row.address,
            }
            _this.loading2 = true;
            _this.ApiService.testcoin.findAccouontBalance(param).then(data => {
                _this.loading2 = false;
                if (data && data.code === 0) {
                    _this.balance = data.data;
                } else {
                    r.message(data.message, "warning");
                }
                })
                .catch(err => {
                    _this.loading2 = false;
                    console.log(err);
                });
            this.deleteAccount = true;
        },
        sureDeleteAccount(){
            let _this = this;
            let param = {
                address:_this.selectRow.address,
            }
            _this.ApiService.testcoin.removeAccountInfo(param).then(data => {
                if (data && data.code === 0) {
                    r.message('删除成功', "success");
                    _this.deleteAccount = false;
                    _this.getData();
                } else {
                    r.message(data.message, "warning");
                }
                })
                .catch(err => {
                    console.log(err);
                });
        }
    },
    mounted(){
        this.getData();
    }
}
</script>

<style scoped>
.balance{
    word-break: keep-all;
    word-wrap: break-word;
    white-space: pre-wrap;
}
</style>