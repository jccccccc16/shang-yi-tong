

export default {

  SUCCESS_TYPE(){
    return "success"
  },

  INFO_TYPE(){
    return "info"
  },

  ERROR_TYPE(){
    return "error"
  },

  MESSAGE_OPERATION_FAIL(){
    return '系统错误，操作失败'
  },

  showMessage(message,type){
    if(type==='success'){
      this.$message.success(message)
    }else if(type==='info'){
      this.$message.info(message)
    }else{
      this.$message.error(message)
    }

  }
}
