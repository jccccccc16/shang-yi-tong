<template>
  <div class="app-container">
    <h2>医院设置列表</h2>
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input  v-model="searchObj.hosname" placeholder="医院名称"/>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchObj.hoscode" placeholder="医院编号"/>
      </el-form-item>
      <el-button type="primary" icon="el-icon-search" @click="getList()">查询</el-button>
    </el-form>

    <!-- 工具条 -->
    <div>
      <el-button type="danger" size="mini" @click="removeRows()">批量删除</el-button>
      <el-button type="primary" size="mini" @click="addHosp()">添加医院</el-button>
    </div>

    <el-table
      :data="list" stripe style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"/>
      <el-table-column type="index" width="50" label="序号"/>
      <el-table-column prop="hosname" label="医院名称"/>
      <el-table-column prop="hoscode" label="医院编号"/>
      <el-table-column prop="apiUrl" label="api基础路径" width="200"/>
      <el-table-column prop="contactsName" label="联系人姓名"/>
      <el-table-column prop="contactsPhone" label="联系人手机"/>
      <el-table-column label="状态" width="80">
        <template slot-scope="scope">
          {{ scope.row.status === 1 ? '可用' : '不可用' }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" align="center">
        <template slot-scope="scope">
          <el-button type="danger" size="mini"
                     icon="el-icon-delete" @click="removeDataById(scope.row.id)">删除</el-button>
          <el-button v-if="scope.row.status==1" type="primary" size="mini"
                     icon="el-icon-delete" @click="lockHostSet(scope.row.id,0)">锁定</el-button>
          <el-button v-if="scope.row.status==0" type="primary" size="mini"
                     icon="el-icon-delete" @click="lockHostSet(scope.row.id,1)">取消锁定</el-button>
          <router-link :to="'/hospSet/edit/'+scope.row.id">
            <el-button type="primary" size="mini" icon="el-icon-edit">更改</el-button>
          </router-link>
        </template>
      </el-table-column>

    </el-table>
<!--    分页-->
    <el-pagination
      :current-page="current"
      :page-size="limit"
      :total="total"
      background
      layout="prev, pager, next"
      @current-change="getList"
    >
    </el-pagination>
  </div>
</template>

<script>
import hospset from '@/api/hospset'
import utils from '@/utils/utils'
export default {
  name: "list",
  // data: {
  //
  // }
  data(){ // 定义数据
    return {
      // 定义我们的数据
      current: 1,
      limit: 3,
      searchObj: {
      },
      // 每页数据集合
      list:[],
      total: 0, // 总记录数
      multipleSelection: [], // 批量删除复选框的id
    }
  },
  // 初始化方法，渲染之前执行
  // 一半调用methods中的方法，得到数据
  created() {
    this.getList();
  },
  methods:{ // 定义方法，接口调用

    // 医院设置列表
    // 默认为1
    getList(page=1){
      this.current = page;
      hospset.getHospSetList(this.current,this.limit,this.searchObj)
      .then(response=>{
        this.list = response.data.records;
        this.total = response.data.total;
        console.log(response);
      }).catch(error=>{//请求失败
        console.log(error);
      })
    },


    // 获取选择复选框的id值
    // element-ui 封装好了的方法，当点击复选框时会返回该行数据给我们，
    // 当选择了多个selection，就包含多个数据
    handleSelectionChange(selection){
      this.multipleSelection = selection
    },


    // 批量删除
    removeRows(){
      // 删除之前跳出弹框
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 创建idList
        var idList = []
        // 遍历multipleSelection获取所有的id
        for(var i=0;i<this.multipleSelection.length;i++){
          var data = this.multipleSelection[i];
          var id = data.id;
          idList.push(id)
        }
        hospset.batchRemoveHsopSet(idList)
          .then(response=>{
            // 显示提示信息
            this.$message({
              message: '删除成功',
              type: 'success'
            });
            // 更新页面
            this.getList();

          }).catch(error=>{
          this.$message.error('删除失败');
          console.log(error);
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });


    },

    // 根据id删除数据
    removeDataById(id){

      // 删除之前跳出弹框
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        hospset.removeDataById(id)
          .then(response=>{
            // 显示提示信息
            this.$message({
              message: '删除成功',
              type: 'success'
            });
            // 更新页面
            this.getList();

          }).catch(error=>{
          this.$message.error('删除失败');
          console.log(error);
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });

    },


    lockHostSet(id,status){
      var type=''
      var message=''
      hospset.lockHospSet(id,status).then(response=>{

        // 如果是0 那么就是1改为0，为锁定
        if(status===0){
          message='锁定成功'
        }else{
          message='解锁成功'
        }
        this.$message.success(message);
        this.getList(this.current)
      }).catch(error=>{
        this.$message.error('解锁错误');
      })




    },

    // 添加医院设置
    addHosp(){

    }



  }
}
</script>

<style scoped>

</style>
