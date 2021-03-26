## 1.vue项目的目录结构

笔记/教案/day04中有详细的图

## 2.编写vue步骤

编写router

编写api

编写component

在component中引用api

比如：

编写router

```js
{
    path: '/hospSet',
    component: Layout,
    redirect: '/hospSet/list',
    name: '医院设置管理',
    meta: { title: '医院设置管理', icon: 'example' },
    children: [
      {
        path: 'list',
        name: '医院设置列表',
        component: () => import('@/views/hospset/list'),
        meta: { title: '医院设置列表', icon: 'table' }
      },
      {
        path: 'add',
        name: '医院设置列表',
        component: () => import('@/views/hospset/add'),
        meta: { title: 'Tree', icon: 'tree' }
      }
    ]
  },
```

编写所需的api接口

```js
export default {
  getHospSetList(current, limit,searchObj) {
    return request({
      url: '/admin/hosp/hospitalSet/findPageHospSet/{current}/{limit}',
      method: 'post',
      data: searchObj // 会将我们的searchObj转换成json发送到后端
    })
  }
}
```



编写children中的component

并且引用api

```vue
<template>
  <div class="app-container">
    医院设置列表
  </div>
</template>

<script>
import hospset from '@/api/hospset'
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
      list:[]
    }
  },
  // 初始化方法，渲染之前执行
  // 一半调用methods中的方法，得到数据
  created() {
    this.list=this.getList()
  },
  methods:{ // 定义方法，接口调用

    // 医院设置列表
    getList(){
      hospset.getHospSetList(this.current,this.limit,this.searchObj)
      .then(response=>{
        console.log(response);
      }).catch(error=>{//请求失败
        console.log(error);
      })


    }

  }
}
</script>

<style scoped>

</style>


```

## 3.跨域问题

但我们在客户端访问该页面时，发现报错

`Access to XMLHttpRequest at 'http://localhost:8201/admin/hosp/hospitalSet/findPageHospSet/%7Bcurrent%7D/%7Blimit%7D' from origin 'http://localhost:9528' has been blocked by CORS policy: Response to preflight request doesn't pass access control check: No 'Access-Control-Allow-Origin' header is present on the requested resource.`

### 3.1 以下情况属于跨域

以下情况都属于跨域：

| 跨域原因说明       | 示例                                |
| ------------------ | ----------------------------------- |
| 域名不同           | www.jd.com 与 www.taobao.com        |
| 域名相同，端口不同 | www.jd.com:8080  与 www.jd.com:8081 |
| 二级域名不同       | item.jd.com 与 miaosha.jd.com       |

### 3.2 如何解决跨域问题

简单方便的一个方法：

在我们后端的controller上加上`@CrossOrigin`的注解

```java
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@CrossOrigin  // 允许跨域
public class HospitalSetController {
    
}
```



## 4.编写获取后后端医院设置列表

按照之前的步骤：

- 编写router:
- 编写component（views）
- 编写api
- 在component中调用api



### 4.1编写router

```js
{
    path: '/hospSet',
    component: Layout,
    redirect: '/hospSet/list',
    name: '医院设置管理',
    meta: { title: '医院设置管理', icon: 'example' },
    children: [
      {
        path: 'list',
        name: '医院设置列表',
        component: () => import('@/views/hospset/list'),
        meta: { title: '医院设置列表', icon: 'table' }
      },
      {
        path: 'add',
        name: '医院添加列表',
        component: () => import('@/views/hospset/add'),
        meta: { title: '医院添加列表', icon: 'tree' }
      }
    ]
  },
```

### 4.2 编写api

```js
export default {
  getHospSetList(current, limit,searchObj) {
    return request({
      url: '/admin/hosp/hospitalSet/findPageHospSet/'+current+'/'+limit,
      method: 'post',
      data: searchObj // 会将我们的searchObj转换成json发送到后端
    })
  }
}

```

### 4.3编写component（view）

```vue
<template>
  <div class="app-container">
    医院设置列表
  </div>
</template>

<script>
import hospset from '@/api/hospset'
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
    }
  },
  // 初始化方法，渲染之前执行
  // 一半调用methods中的方法，得到数据
  created() {
    this.getList();
  },
  methods:{ // 定义方法，接口调用

    // 医院设置列表
    getList(){
      hospset.getHospSetList(this.current,this.limit,this.searchObj)
      .then(response=>{
        this.list = response.data.records;
        this.total = response.data.total;
        console.log(response);
      }).catch(error=>{//请求失败
        console.log(error);
      })


    }

  }
}
</script>

<style scoped>

</style>

```

### 4.5 显示数据

vue + element-ui

到菜鸟教程找vue语法`https://www.runoob.com/vue3/vue3-install.html`

页面布局主要去element-ui去找需要的元素

```vue
<template>
  <div class="app-container">
    医院设置列表
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
    </el-table>
  </div>
</template>
```

#### 4.5.1 分页

用户点击页码，将该页更改为该页码页。

修改getList方法，增加page参数，传给current。

```vue
 methods:{ // 定义方法，接口调用

    // 医院设置列表
    getList(page){
      this.current = page;
      hospset.getHospSetList(this.current,this.limit,this.searchObj)
      .then(response=>{
        this.list = response.data.records;
        this.total = response.data.total;
        console.log(response);
      }).catch(error=>{//请求失败
        console.log(error);
      })


    }
```



#### 4.5.2 pagination分页插件

使用该插件来创建页码导航栏

绑定好页面的一些数据，以及点击页码调用的方法

`<el-table-column type="index" width="50" label="序号"/>` index为序号，显示1，2，3..

`<el-table-column prop="hosname" label="医院名称"/>` prop为我们属性名

`<template slot-scope="scope">
          {{ scope.row.status === 1 ? '可用' : '不可用' }}
        </template>`

scope为表格，通过scope.row可以获取这一行的数据

```vue
  <div class="app-container">
    医院设置列表
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
```

#### 4.5.3 条件查询

```vue
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
```

感受到了吗，这就是前后端分离

## 5.删除医院设置列表

在每一行数据后面加上删除按钮

通过scope去获取一行数据的id

```vue
<template slot-scope="scope">
          <el-button type="danger" size="mini"
                     icon="el-icon-delete" @click="removeDataById(scope.row.id)">删除</el-button>
</template>
```

### 5.1编写api

```js
 removeDataById(id) {
    return request({
      url: '/admin/hosp/hospitalSet/'+id,
      method: 'delete',
    })
  }
```

### 5.2 调用api

在vue的methods中调用api

```js
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
            // 显示数据
            this.getList(this.current);

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

    }

```



## 6.批量删除

点击数据前面的复选框选择要删除的数据，获取所要删除数据的所有id，执行批量删除

### 6.1 编写api

写在data的数据会被转为json，发到后端

```js
// 批量删除
  batchRemoveHsopSet(idList){
    return request({
      url: '/admin/hosp/hospitalSet/batchRemove',
      method: 'delete',
      data: idList
    })
      
  }
```



### 6.2编写页面

增加批量删除按钮

```vue
 <div>
      <el-button type="danger" size="mini" @click="removeRows()">批量删除</el-button>
    </div>
```

### 6.3 获取要批量删除的数据

在table中有一个element-ui封装好的一个@selection-change方法，当表格中的数据被选择时，该数据就会被封装为selection传到该方法中，我们编写了`handleSelectionChange`来接受被选择的数据

```vue
 <el-table
      :data="list" stripe style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"/>
      <el-table-column type="index" width="50" label="序号"/>
```

编写方法接收被选数据

先定义数据接收数据

```js
data(){ // 定义数据
    return {
      ....
      multipleSelection: [], // 被选择的数据
    }
  },
```

接收方法

```js
handleSelectionChange(selection){
      this.multipleSelection = selection
    },

```

### 6.4 调用api删除

遍历`multipleSelection`获取要被删除的数据的id，然后调用列表

```js
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
```



## 7.锁定与取消锁定医院

设置医院的状态为0，则显示状态不可用，将该医院锁定

### 7.1编写api

修改该id的数据的status

```js
// 锁定与接锁定医院设置
  lockHospSet(id,status){
    return request({
      url: '/admin/hosp/hospitalSet/lockHospitalSet/'+id+'/'+status,
      method: 'put',
    })
  }
```



### 7.2 编写页面

```vue
<el-table-column label="状态" width="80">
        <template slot-scope="scope">
          {{ scope.row.status === 1 ? '可用' : '不可用' }}
        </template>
      </el-table-column>
```



### 7.3 调用api

```js
 lockHostSet(id,status){
      var message='';
      var type=''
      hospset.lockHospSet(id,status).then(response=>{

        // 如果是0 那么就是1改为0，为锁定
        if(status===0){
          message='锁定成功'
        }else{
          message='解锁成功'
        }
        type=utils.SUCCESS_TYPE();
        this.getList(this.current)
      }).catch(error=>{
        message=utils.MESSAGE_OPERATION_FAIL;
        type=utils.ERROR_TYPE();
      })

      // 显示提示框
      utils.showMessage(message,type)

    }
```

## 8.添加医院设置

### 8.1 编写api

```js
  addHosp(hospitalSet){
    return request({
      url: '/admin/hosp/hospitalSet/saveHospitalSet',
      method: 'post',
      data: hospitalSet
    })
  },
```

### 8.2 编写component

```vue
<div class="app-container">
    <h2>医院设置添加</h2>
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
        <el-button type="primary" @click="save()">保存</el-button>
      </el-form-item>
    </el-form>

  </div>
```

定义数据

```js
data(){
    return {
      hospitalSet: {}
    }
  },
```

### 8.3 调用api

```js
save(){
      hospset.addHosp(this.hospitalSet)
      .then(response=>{
        this.$message.success("添加成功")
        this.$router.push({path:'/hospSet/list'})
      }).catch(error=>{
        this.$message.error('添加失败')
      })

    },
```

## 9.更新医院设置

在一行数据后面添加更改操作

点击更改操作跳转到更改页面

更改完毕跳转到列表页面

思路：

更新与添加使用同一个页面

当点击更改时，url带着该行数据的id，

在添加页面中，通过在created方法中判断url中是否有id来确定是添加还是更新操作

如果是更新操作，那么就回显操作。

而且，当点击保存按钮时，同样要通过id判断是添加操作做还是更新操作

### 9.1编写api

```js
updateHosp(hospitalSet){
    return request({
      url: '/admin/hosp/hospitalSet/updateHospitalSet',
      method: 'POST',
      data: hospitalSet
    })
  }
```

### 9.2 编写所需方法与调用api

定义数据：

```js
data(){  
    return {    
        hospitalSet: {},
        isSave: true // 是否为保存操作
    }
},
```

判断是否是更新操作：

```js
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
```

回显操作：

```js
getHospSet(id){
      hospset.getHospSetById(id)
      .then(response=>{

        this.hospitalSet = response.data;
      })
    }
```

当点击保存按钮时,判断是否为更新

```js
addOrUpdate(){  
    // 如果是添加  
    if(this.isSave){    
        this.addHosp();  
    }else{    
        // 如果是更新
        this.updateHospSet();  
    }},
```

绑定按钮

```vue
<el-form-item>
        <el-button type="primary" @click="addOrUpdate()">保存</el-button>
      </el-form-item>
```

