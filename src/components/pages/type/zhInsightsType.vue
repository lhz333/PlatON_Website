<template>
   <div>
       <!--head-->
        <div class="content-c-breadcrumb">
            <el-breadcrumb separator="-">
                <el-breadcrumb-item>新闻分类</el-breadcrumb-item>
                <el-breadcrumb-item>中文版本</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <!--操作按钮-->
        <div class="content-util">
            <el-row :gutter="20">
                <el-col :span="18">
                    <el-button type="primary" @click="buildType">新建</el-button>
                </el-col>
            </el-row>
        </div>

        <!-- 数据列表 -->
        <el-table ref="selectDate" class="table-m20" v-loading.body="loading" :data="typeList" border align="center" cell-style="padding:2px 0;" header-cell-style="text-align:center;background-color:#ddd;color:#333;" style="width:100%;" highlight-current-row>
            <el-table-column prop='name' label="分类名称"></el-table-column>
            <el-table-column prop='count' label="文章数量"></el-table-column>
            <el-table-column label="操作" width="150">
                <template slot-scope="scope">
                    <el-button type="text" @click="rename(scope.row)">重命名</el-button>
                    <el-button type="text" @click="detele(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--分页-->
        <div class="pager-wrap content-pages">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="page.currentNo" :page-sizes="[10, 20, 50, 100]" :page-size="page.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="page.totalPages">
            </el-pagination>
        </div>

        <!-- 新建分类 -->
        <el-dialog title="新建分类" :visible.sync="dialogFormType">
            <el-form>
                <el-form-item label="分类名称" :label-width="formLabelWidth">
                    <el-input v-model.trim="buildTypeName" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormType = false">取 消</el-button>
                <el-button type="primary" @click="sureBuildType">确 定</el-button>
            </div>
        </el-dialog>

        <!-- 重命名 -->
        <el-dialog title="重命名" :visible.sync="dialogRenameType">
            <el-form>
                <el-form-item label="原分类名称" :label-width="formLabelWidth">
                    <el-input v-model="renameRow.name" disabled></el-input>
                </el-form-item>
                <el-form-item label="重命名分类名称" :label-width="formLabelWidth">
                    <el-input v-model.trim="reTypeName" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogRenameType = false">取 消</el-button>
                <el-button type="primary" @click="sureRenameType">确 定</el-button>
            </div>
        </el-dialog>
    </div> 
</template>
<script>
import r from '@/util/RequestUtil.js'
export default {
    name:"",
    data(){
        return{
            loading:false,
            typeList:[],
            formLabelWidth:'120px',
            dialogFormType:false,
            dialogRenameType:false,
            buildTypeName:null,
            reTypeName:null,
            renameRow:{},
            page: {
                totalPages: 10,
                currentNo: 1,
                pageSize: 50
            },
        }
    },
    methods:{
        handleSizeChange(val) {
            this.page.pageSize = val;
            window.sessionStorage.setItem('faqPagesize',val);
            this.getData();
        },
        handleCurrentChange(val) {
            this.page.currentNo = val;
            this.getData();
        },
        //获取分类数据
        getData(){
            let _this = this;
            _this.loading = true;
            let param = {
                pageNo: _this.page.currentNo,
                pageSize: _this.page.pageSize,
            };
            _this.ApiService.newTypeCn.pageTypeCn(param).then(data => {
                _this.loading = false;
                if(data && data.code === 0) {
                    _this.typeList = data.data.result;
                    _this.page.totalPages = data.data.totalCount;
                } else{
                    r.message(data.message, "warning");
                }
            })
            .catch(err => {
                _this.loading = false;
                console.log(err);
            });
        },
        // 新建分类
        buildType(){
            this.buildTypeName = null;
            this.dialogFormType = true;
        },
        // 确认新建分类
        sureBuildType(){
            let _this = this;
            if(_this.buildTypeName === null || _this.buildTypeName===''){
                r.message("请填写分类名称", "warning"); 
                return; 
            }
            if(_this.buildTypeName.length>30){
                r.message("分类名称不超过30个字符", "warning"); 
                return; 
            }
            let param = {
                name:_this.buildTypeName
            };

            _this.ApiService.newTypeCn.addTypeCn(param).then(data => {
                if(data && data.code === 0) {
                    _this.dialogFormType = false;
                    r.message('新建成功', "success");
                    _this.getData();
                }else if(data.code === 3){
                        r.message("不能重复添加分类", "warning");    
                } else{
                    r.message(data.message, "warning");
                }
            })
            .catch(err => {
                console.log(err);
            });
        },
        // 重命名
        rename(row){
            let _this = this;
            if(row.name ==='新闻' || row.name ==='活动'){
                r.message("此项分类不允许修改名称", "warning"); 
                return; 
            }
            _this.reTypeName = null;
            _this.dialogRenameType = true;
            _this.renameRow = row;
        },
        //确认重命名分类名称
        sureRenameType(){
            let _this = this;
            if(_this.reTypeName === null || _this.reTypeName===''){
                r.message("请填写重命名分类名称", "warning"); 
                return; 
            }
            if(_this.reTypeName.length>30){
                r.message("分类名称不超过30个字符", "warning"); 
                return; 
            }
            let param = {
                id:_this.renameRow.id,
                name:_this.reTypeName
            }
            _this.ApiService.newTypeCn.updTypeCnName(param).then(data => {
                if(data && data.code === 0) {
                    _this.dialogRenameType = false;
                    r.message('重命名成功', "success");
                    _this.reTypeName = null;
                    _this.getData();
                }else if(data.code === 3){
                    r.message("不能重复添加分类", "warning");    
                } else{
                    r.message(data.message, "warning");
                }
            })
            .catch(err => {
                console.log(err);
            });
        },
        // 删除分类
        detele(row){
            let _this = this;
            // 分类下文章数量大于0的时候   不能删除
            if(row.count>0){
                r.message('该分类下的文章需重新分类，当该分类下无文章时才可删除', "warning");
                return;
            }
            this.$confirm('是否确认删除名称为 “' + row.name + '” 的分类?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let param = {
                    id: row.id
                }
                _this.ApiService.newTypeCn.removeTypeCn(param).then(data => {
                    if(data && data.code === 0) {
                        _this.getData();
                        _this.$message({
                            type: 'success',
                            message: '已成功删除'
                        });

                    } else if(data.code === 4){
                        r.message("该分类下的文章需重新分类，当该分类下无文章时才可删除", "warning");    
                    }else{
                        r.message(data.message, "warning");
                    }
                })
                .catch(err => {
                    console.log(err);
                });
            }).catch(() => {

            });
        }
    },
    mounted(){
        //获取列表数据
        this.getData();
    }
}
</script>
<style>

</style>


