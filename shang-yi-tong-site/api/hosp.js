import request from '@/utils/request'
const api_name = '/api/hosp/hospital'
export default {
  getPageList(page,limit,searchObj){
    return request({
      url: api_name+'/list/'+page+'/'+limit,
      method: 'get',
      params: searchObj
    })
  },
  getByHosname(hosname){
    return request({
      url: api_name+'/find/hospital/by/hosname/'+hosname,
      method: 'get',
    })
  },
  // 根据hoscode查询医院详情
  getHospitalDetails(hoscode){
    return request({
      url: api_name+'/details/by/'+hoscode,
      method: "get"
    })
  },
  // 根据hoscode获取科室
  getDepartment(hoscode){
    return request({
      url: api_name+'/get/department/'+hoscode,
      method: 'get'
    })
  },
  //获取医院预约挂号规则详情以及医院详情
  getBookingRuleAndHospital(hoscode){
    return request({
      url: api_name+'/get/bookingRule/'+hoscode,
      method: 'get'
    })
  }

}
