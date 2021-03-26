<template>
  <div class="app-container">

    <el-form label-width="120px">
      <el-form-item label="医院名称">
        <el-input v-model="hospitalSet.hosname"/>
      </el-form-item>

      <el-form-item label="医院编号">
        <el-input v-model="hospitalSet.hoscode"/>
      </el-form-item>

      <el-form-item label="api基础路径">
        <el-input v-model="hospitalSet.apiUrl"/>
      </el-form-item>

      <el-form-item label="联系人姓名">
        <el-input v-model="hospitalSet.contactsName"/>
      </el-form-item>
      <el-form-item label="联系人手机">
        <el-input v-model="hospitalSet.contactsPhone"/>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="addOrUpdate()">保存</el-button>
      </el-form-item>
    </el-form>

  </div>
</template>

<script>
import hospset from '@/api/hospset'
export default {
  name: "add",
  data(){
    return {
      hospitalSet: {},
      isSave: true
    }
  },
  created(){
    // 判断url是否有参数id
    // 如果有，那么就是更新操作
    if(this.$route.params && this.$route.params.id){
      this.isSave = false;
      // 回显数据
      const id = this.$route.params.id;
      this.getHospSet(id);
    }else{
      this.hospitalSet = {}
    }
  },
  methods: {

    updateHospSet() {
      hospset.updateHosp(this.hospitalSet)
        .then(response => {
          // 更新成功
          this.$message.success('更新成功')
          // 跳转到列表页面
          this.$router.push({path: '/hospSet/list'})

        }).catch(error => {
        this.$message.error('更新失败');
      })
    },

    addOrUpdate(){
      // 如果是添加
      if(this.isSave){
        this.addHosp();
      }else{
        this.updateHospSet();
      }
    },

    addHosp(){
      hospset.addHosp(this.hospitalSet)
      .then(response=>{
        this.$message.success("添加成功")
        this.$router.push({path:'/hospSet/list'})
      }).catch(error=>{
        this.$message.error('添加失败')
      })

    },

    getHospSet(id){
      hospset.getHospSetById(id)
      .then(response=>{

        this.hospitalSet = response.data;
      })
    }
  },
}
</script>

<style scoped>

</style>
