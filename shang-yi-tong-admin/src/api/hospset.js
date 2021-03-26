import request from '@/utils/request'



export default {

  // 获取医院设置列表
  getHospSetList(current, limit,searchObj) {
    return request({
      url: '/admin/hosp/hospitalSet/findPageHospSet/'+current+'/'+limit,
      method: 'post',
      data: searchObj // 会将我们的searchObj转换成json发送到后端
    })
  },

  // 删除医院设置
  removeDataById(id) {

    return request({
      url: '/admin/hosp/hospitalSet/'+id,
      method: 'delete',
    })
  },

  // 批量删除
  batchRemoveHsopSet(idList){
    return request({
      url: '/admin/hosp/hospitalSet/batchRemove',
      method: 'delete',
      data: idList
    })
  },
  // 锁定与接锁定医院设置
  lockHospSet(id,status){
    return request({
      url: '/admin/hosp/hospitalSet/lockHospitalSet/'+id+'/'+status,
      method: 'put',
    })
  },

  addHosp(hospitalSet){
    return request({
      url: '/admin/hosp/hospitalSet/saveHospitalSet',
      method: 'post',
      data: hospitalSet
    })
  },

  getHospSetById(id) {
    return request({
      url: '/admin/hosp/hospitalSet/getHospSet/'+id,
      method: 'get',
    })
  },
  updateHosp(hospitalSet){
    return request({
      url: '/admin/hosp/hospitalSet/updateHospitalSet',
      method: 'POST',
      data: hospitalSet
    })
  }
}


