import request from '@/utils/request'

export default {
  // 获取医院列表
  getHospital(current, limit,searchObj){
    return request({
      url: "/admin/hosp/hospital/list/"+current+"/"+limit,
      method: "post",
      data: searchObj
    })
  },
  updateStatus(id,status){
    return request({
      url:"/admin/hosp/hospital/update/hospital/"+status+"/by/"+id,
      method: "get",
    })
  },
  getHospitalDetails(id) {
    return request({
      url: "/admin/hosp/hospital/get/hospital/details/by/"+id,
      method: "get"
    })
  }
}
