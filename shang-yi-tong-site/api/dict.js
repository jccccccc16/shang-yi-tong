import request from '@/utils/request'
const api_name = '/admin/cmn/dict'
export default {
  findByDictCode(dictCode){
    return request({
      url: api_name+'/findByDictCode/'+dictCode,
      method: 'get',
    })
  },
  getByParentId(parentId){
    return request({
      url: api_name+'/find/children/data/'+parentId,
      method: 'get'
    })

  }
}
