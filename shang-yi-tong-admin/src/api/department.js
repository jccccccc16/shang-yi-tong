import request from '@/utils/request'

export default {

  // 数据字典列表
  getDepartments(hoscode) {
    return request({
      url: '/admin/hosp/department/get/department/'+hoscode,
      method: 'get',
    })
  },
  getScheduleRule(page,limit,hoscode,depcode){
    return request({
      url: '/admin/hosp/schedule/get/schedule/rule/'+page+'/'+limit+'/'+hoscode+'/'+depcode,
      method: 'get'
    })
  },
  getDetailSchedule(hoscode,depcode,workDate){
    return request({
      url: '/admin/hosp/schedule/get/schedule/details/'+hoscode+'/'+depcode+'/'+workDate,
      method: 'get'
    })
  }



}
