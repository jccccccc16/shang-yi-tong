<template>
  <div class="app-container">
    <template>
      <el-table
        :data="list"
        style="width: 100%">
        <el-table-column type="index" width="50" label="序号"/>
        <el-table-column
          label="医院logo">
          <template slot-scope="scope">
            <img :src="'data:image/jpeg;base64,'+scope.row.logoData" width="80">
          </template>
        </el-table-column>
        <el-table-column
          prop="hosname"
          label="医院名称"
          width="180">
        </el-table-column>
        <el-table-column
          prop="param.hostypeString"
          label="医院类型">
        </el-table-column>
        <el-table-column
          prop="param.fullAddress"
          label="详细地址">
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="180">
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template slot-scope="scope">
            {{ scope.row.status === 0 ? '未上线' : '已上线' }}
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
           width="280" align="center">
          <template slot-scope="scope">
            <router-link :to="'/hospSet/hosp/details/'+scope.row.id">
              <el-button type="primary" size="mini"
                       icon="el-icon-delete">查看详情</el-button>
            </router-link>
            <router-link :to="'/hospSet/hosp/dept/'+scope.row.hoscode">
              <el-button type="primary" size="mini"
                         icon="el-icon-delete">查看排班</el-button>
            </router-link>
            <el-button type="success" size="mini"
                       icon="el-icon-delete" @click="updateHospitalStatus(scope.row.id,scope.row.status)">修改状态</el-button>
          </template>
        </el-table-column>
      </el-table>
    </template>
  </div>
</template>

<script>
import hospital from '@/api/hospital'
export default {
  name: "hosp",
  data(){
    return{
      current: 1,
      limit: 3,
      searchObj: {

      },
      list: [],
      total: 0,
    }
  },
  created() {
    this.getHospitals();
  },
  methods: {
    getHospitals(page=1){
      hospital.getHospital(page,this.limit,this.searchObj)
      .then(response=>{
        this.list = response.data.content;
        this.total = response.data.totalElements;
        console.log(response);
      }).catch(error=>{//请求失败
        console.log(error);
      })
    },
    updateHospitalStatus(id,status){
      hospital.updateStatus(id,status)
        .then(response=>{
          console.log("修改成功")
          this.$message.success('修改成功')
          this.getHospitals();
        }).catch(error=>{
        this.$message.error('修改失败')
      })


    }

  }
}
</script>

<style scoped>

</style>
