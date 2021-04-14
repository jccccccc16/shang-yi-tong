预约挂号平台

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

可通过`$route.params.id`去获取url的参数

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



## 10.数据字典列表显示

系统中一些常用的**分类数据（有等级）**和**固定数据**，比如，地区，民族。有层级关系的数据，在页面中按层级显示

### 10.1 医院等级显示

#### 10.1.1 后端接口开发

根据id查出该数据的子数据

service层

将子数据查出，并且判断子数据是否有子数据，如果有hasChilren为true

```java
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {


    @Override
    public List<Dict> findChildrenData(Long id) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id", id);
        List<Dict> dictList = baseMapper.selectList(dictQueryWrapper);

        // 设置对象中的hasChildren字段
        for (Dict dict : dictList) {
            Long dictId = dict.getId();
            boolean hasChildren = hasChildren(dictId);
            dict.setHasChildren(hasChildren);
        }

        return dictList;
    }

    /**
     * 是否有子数据
     * @param id
     * @return
     */
    private boolean hasChildren(Long id){

        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        return count > 0;


    }
}

```



#### 10.1.2 编写页面显示

element-ui中的树形table

主要的部分：

- 指定`row-key = 'id'`为指定我们数据中的id为row-key，

- load，指定加载子数据的方法

- `:tree-props="{children: 'children', hasChildren: 'hasChildren'}">`指定table中的children为我们数据中的`children`字段，同理hasChildren，element-ui通过`hasChildren`去判断是否显示为可点击展开的项，children来获取数据的`children`来进行展示

```vue
 <el-table
      :data="list"
      style="width: 100%"
      row-key="id"
      border
      lazy
      :load="getChildren"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}">

      <el-table-column label="名称" width="230" align="left">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="编码" width="220">
        <template slot-scope="{row}">
          {{ row.dictCode }}
        </template>
      </el-table-column>
      <el-table-column label="值" width="230" align="left">
        <template slot-scope="scope">
          <span>{{ scope.row.value }}</span>
        </template>
      </el-table-column>

      <el-table-column label="创建时间" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
    </el-table>
```



```js
export default {
  data() {
    return {
      dialogImportVisible:false,//设置弹框是否弹出
      list:[] //数据字典列表数组
    }
  },
  created() {
    // 显示一级列表
    this.getDictList(1)
  },
  methods: {
    //导入数据字典
    importData() {
      this.dialogImportVisible = true
    },
    //上传成功调用
    onUploadSuccess() {
      //关闭弹框
      this.dialogImportVisible = false
      //刷新页面
      this.getDictList(1)
    },
    //导出数据字典
    exportData() {
      //调用导出接口
      window.location.href="http://localhost:8202/admin/cmn/dict/exportData"
    },
    //数据字典列表
    getDictList(id) {
      dict.dictList(id)
        .then(response => {
          this.list = response.data
        })
    },
      // 获取子节点
    getChildren(tree, treeNode, resolve) {
	// tree.id 为获取row-key，也就是我们指定的数据id
      dict.getDictList(tree.id).then(response => {
        resolve(response.data)
      })
    }
  }
}
</script>
```

但是页面中的数据点不开，发现element-ui的版本为

```json
"dependencies": {
    "axios": "0.18.0",
    "element-ui": "2.4.6",
    "js-cookie": "2.2.0",
    "normalize.css": "7.0.0",
    "nprogress": "0.2.0",
    "vue": "2.5.17",
    "vue-router": "3.0.1",
    "vuex": "3.0.1"
  },
```

更换高版本。

高版本滴行！

#### 10.1.3 遇到端口错误

由于之前我们是使用的是service-hosp模块，端口号为8201，在前端中，我们在config.dev.env.js中的设置为BASE_API: `'"http://localhost:8201/"',`

，而现在我们使用的模块是端口号为8202的service-cmn模块。

多个端口怎么办？

使用unix，但放到后面解决



## 11.easyExcel

将数据生成excel；读取excel中的数据

### 11.1 导出数据

#### 11.1.1 编写后端接口

```java
/**
     * 导出数据字典接口
     * @return
     */
    @GetMapping("/exportData")
    public void exportDict(HttpServletResponse response) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String filename = URLEncoder.encode("数据字典", "UTF-8");
        // Content-disposition 以下载的方式打开
        response.setHeader("Content-disposition","attachment;filename="+filename+".xlsx");
        // 查询数据
        BaseMapper<Dict> baseMapper = dictService.getBaseMapper();
        List<Dict> dictList = baseMapper.selectList(null);
        log.info("查询的数据为：dictList="+dictList);
        // 封装为DICTEevo
        List<DictEeVo> dictEeVoList = new ArrayList<>();
        for (Dict dict : dictList) {
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict,dictEeVo);
            dictEeVoList.add(dictEeVo);
        }
        log.info("转换为dictEeVo dictEeVoList = "+dictEeVoList);
        // 写出excel数据
        EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict").doWrite(dictEeVoList);

    }
```

#### 11.1.2 前端调用

```js
//导出数据字典
    exportData() {
      //调用导出接口
      window.location.href="http://localhost:8202/admin/cmn/dict/exportData"
    },
```



### 11.2 导入数据

#### 11.2.1 编写后端接口

需要自定义一个listener，listener决定如何去处理读取的每一行数据

```java
    /**
     * 导入数据
     * @param file
     */
    @Override
    public void importData(MultipartFile file) throws IOException {

        EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(dictMapper)).sheet().doRead();;

    }
```

listener：

```java
@Slf4j
public class DictListener extends AnalysisEventListener<DictEeVo> {

    private int count = 0;

    private DictMapper dictMapper;

    public DictListener(DictMapper dictMapper){
        this.dictMapper = dictMapper;
    }

    /**
     * 读取一行都会调用该方法
     * 读取数据后如何做
     * @param dictEeVo
     * @param analysisContext
     */
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        count = count + 1;
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo,dict);
        dict.setIsDeleted(0);
        dictMapper.insert(dict);
        log.info("插入第 "+ count +" 行数据 dict："+dict.toString());
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}

```





#### 11.2.2 前端

我们希望点击导入按钮，会弹出一个选择文件的框，当上传完文件时会自动关闭.

element-ui的一个组件：

`:visible.sync="dialogImportVisible"`为是否显示

`onUploadSuccess`为上传成功后的回调方法

`:action="'http://localhost:8202/admin/cmn/dict/importData'"`为后端处理路径

```vue
// 上传按钮
<el-button type="text" @click="importData"><i class="fa fa-plus"/> 导入</el-button>

// 上传文件框
<el-dialog title="导入" :visible.sync="dialogImportVisible" width="480px">
      <el-form label-position="right" label-width="170px">

        <el-form-item label="文件">

          <el-upload
            :multiple="false"
            :on-success="onUploadSuccess"
            :action="'http://localhost:8202/admin/cmn/dict/importData'"
            class="upload-demo">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传excel文件，且不超过500kb</div>
          </el-upload>

        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogImportVisible = false">
          取消
        </el-button>
      </div>
    </el-dialog>
```

```JS
data() {
    return {
      dialogImportVisible: false,//设置弹框是否弹出
      list:[] //数据字典列表数组
    }
  },
methods: {
    //导入数据字典
    importData() {
      this.dialogImportVisible = true // 显示输入框
    },
    //上传成功调用
    onUploadSuccess() {
      //关闭弹框
      this.dialogImportVisible = false
      //刷新页面
      this.getDictList(1)
    },
}
      
      
```



## 12.spring cache + reids 缓存数据

由于我们的缓存在其他的模块也会用到，所以我们将缓存加入common模块中

依赖：

```xml
<!--        reis-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
<!--        spring集成redis所需的-->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
            <version>2.6.0</version>
        </dependency>
```

配置类是相对固定的，直接看代码，不用写

缓存的几个注解

### 12.1 操作

加入缓存

`@Cacheable(value="dict",keyGenerator = "keyGenerator")`

value="dict" 为key以dict开头，+ keyGenerator 生成的key

```java
	@Override
    @Cacheable(value="dict",keyGenerator = "keyGenerator")
    public List<Dict> findChildrenData(Long id) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id", id);
        List<Dict> dictList = baseMapper.selectList(dictQueryWrapper);

        // 设置对象中的hasChildren字段
        for (Dict dict : dictList) {
            Long dictId = dict.getId();
            boolean hasChildren = hasChildren(dictId);
            dict.setHasChildren(hasChildren);
        }

        return dictList;
    }
```



更新操作，将缓存清空

`@CacheEvict(value = "dict",allEntries = true)`为：value="dict" ，以dict开头的缓存清空

```java
    /**
     * 导入数据
     * @param file
     */
    @Override
    @CacheEvict(value = "dict",allEntries = true)
    public void importData(MultipartFile file) throws IOException {

        EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(dictMapper)).sheet().doRead();;

    }
```



## 13.配置nginx

后端网关配置

下载uginx

### 13.1 uginx.conf配置文件

打开uginx.conf配置文件

`listen`为对外暴露的端口号

`location`配置url映射到那一个ip:端口号上

比如：

路径中包含hosp的路径，就会被代理到`http://localhost:8021`中

```
location ~/hosp/{

	proxy_pass http://localhost:8021

}
```



```
 server {
        listen       80;
        server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            root   html;
            index  index.html index.htm;
        }

        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

        # proxy the PHP scripts to Apache listening on 127.0.0.1:80
        #
        #location ~ \.php$ {
        #    proxy_pass   http://127.0.0.1;
        #}

        # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
        #
        #location ~ \.php$ {
        #    root           html;
        #    fastcgi_pass   127.0.0.1:9000;
        #    fastcgi_index  index.php;
        #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
        #    include        fastcgi_params;
        #}

        # deny access to .htaccess files, if Apache's document root
        # concurs with nginx's one
        #
        #location ~ /\.ht {
        #    deny  all;
        #}
    }


```



在http添加一个server

```
 server {
        listen       80;
        server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        #location / {
           # root   html;
           # index  index.html index.htm;
       # }

       location ~/hosp/{
	proxy_pass http://localhost:8201;
       }

       localtion ~/cmn/{
       	proxy_pass http://localhost:8202;
       }

```

### 13.2 启动uginx

windows版使用cmd在目录下输入ugiunx.exe启动

### 13.3 修改前端配置

由于我们uginx配置对外暴露的端口号为80

在config.dev.env.js中，修改为

```js
module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  //123
  // BASE_API: '"https://easy-mock.com/mock/5950a2419adc231f356a6636/vue-admin"',
  // BASE_API: '"http://localhost:8202/"',
    BASE_API: '"http://localhost/"',
})

```



## 14.mongodb

### 14.1 安装mongodb

使用docker安装mongodb

`docker pull mongo:latest`

`docker run -d --restart=always -p 27017:27017 --name mymongo  -v /data/db:/data/db/ -d mongo`

`docker exec -it name /bin/bash`

`mongo`进入操作

### 14.2 几个概念与基本数据类型

#### 14.2.1 概念

对象叫做文档

不管我们学习什么数据库都应该学习其中的基础概念，在mongodb中基本的概念是文档、集合、数据库，下面我们挨个介绍。

下表将帮助您更容易理解Mongo中的一些概念：

| SQL术语/概念 | MongoDB术语/概念 | 解释/说明                           |
| :----------- | :--------------- | :---------------------------------- |
| database     | database         | 数据库                              |
| table        | collection       | 数据库表/集合                       |
| row          | document         | 数据记录行/文档                     |
| column       | field            | 数据字段/域                         |
| index        | index            | 索引                                |
| table joins  |                  | 表连接,MongoDB不支持                |
| primary key  | primary key      | 主键,MongoDB自动将_id字段设置为主键 |







#### 14.2.2 基本数据类型

##### MongoDB 数据类型

下表为MongoDB中常用的几种数据类型。

| 数据类型           | 描述                                                         |
| :----------------- | :----------------------------------------------------------- |
| String             | 字符串。存储数据常用的数据类型。在 MongoDB 中，UTF-8 编码的字符串才是合法的。 |
| Integer            | 整型数值。用于存储数值。根据你所采用的服务器，可分为 32 位或 64 位。 |
| Boolean            | 布尔值。用于存储布尔值（真/假）。                            |
| Double             | 双精度浮点值。用于存储浮点值。                               |
| Min/Max keys       | 将一个值与 BSON（二进制的 JSON）元素的最低值和最高值相对比。 |
| Array              | 用于将数组或列表或多个值存储为一个键。                       |
| Timestamp          | 时间戳。记录文档修改或添加的具体时间。                       |
| Object             | 用于内嵌文档。                                               |
| Null               | 用于创建空值。                                               |
| Symbol             | 符号。该数据类型基本上等同于字符串类型，但不同的是，它一般用于采用特殊符号类型的语言。 |
| Date               | 日期时间。用 UNIX 时间格式来存储当前日期或时间。你可以指定自己的日期时间：创建 Date 对象，传入年月日信息。 |
| Object ID          | 对象 ID。用于创建文档的 ID。                                 |
| Binary Data        | 二进制数据。用于存储二进制数据。                             |
| Code               | 代码类型。用于在文档中存储 JavaScript 代码。                 |
| Regular expression | 正则表达式类型。用于存储正则表达式。                         |



### 14.3 场景

使用场景：

- 高并发，实时插入

- 缓存
- 用于对象以及json的存储

### 14.4 增删查改

可参考菜鸟教程

###### (1)save和insert

指定数据库名字：

将数据插入数据库

```shell
// 创建数据库
> use testdb
switched to db testdb
// db.数据库名字.insert
> db.testdb.insert({"name":"cjc"})
WriteResult({ "nInserted" : 1 })
> 


```

指定集合的名字：

将数据插入集合

```shell
> db.createCollection("student")
{ "ok" : 1 }
> db.student.insert({"name":"cjc"})
WriteResult({ "nInserted" : 1 })
> 

```







### 14.5 与redis的比较

基本类型与sql相差无比，但与redis相比的话，mongodb有集合的概念，可以自增id， **主键,MongoDB自动将_id字段设置为主键**，有索引



### 14.6 springboot 和 mongodb

springboot提供了`mongoTemplate`和`MongoRepository`

#### 14.6.1 配置

```yml
spring:
  data:
    mongodb:
      uri: mongodb://192.168.52.138:27017/testdb

  

```

只要我们继承了mongoRepository，我们的方法符合规范，我们就不用自己实现接口，具体规范查看day07

## 15.与医院模拟系统整合

医院系统可以调用我们预约挂号后台系统的接口，进行上传信息或者其他的操作，上传科室接口，上传医院接口，上传排班接口。医院系统先保存自己的医院的信息到数据库，包括医院编号以及signkey签名。点击上传医院，将自己医院的信息上传到预约挂号后台系统。此后医院系统发送任何请求都携带该医院的编号以及该医院的签名。预约挂号后台系统在处理医院系统的请求时，都会判断该医院系统的签名是否有效。

![1617177427421](C:\Users\cjc\AppData\Roaming\Typora\typora-user-images\1617177427421.png)

### 15.1 医院模拟系统

直接复制过来到项目中，应为我们主要不是开发这个系统（项目中的hospital-manage）

医院系统如何调用我们预约挂号系统并发送数据呢

查看医院模拟系统ApiController中的保存操作

```java

	@RequestMapping(value="/hospital/save",method=RequestMethod.POST)
	public String saveHospital(String data, HttpServletRequest request) {
		try {
			apiService.saveHospital(data);
		} catch (YyghException e) {
			return this.failurePage(e.getMessage(),request);
		} catch (Exception e) {
			return this.failurePage("数据异常",request);
		}
		return this.successPage(null,request);
	}
```

调用service中的save方法，传入data数据

查看service的save方法

```java
@Override
    public boolean saveHospital(String data) {
        // 
        // 将json字符串封装为json对象
        JSONObject jsonObject = JSONObject.parseObject(data);

        
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode",this.getHoscode());
        paramMap.put("hosname",jsonObject.getString("hosname"));
        paramMap.put("hostype",jsonObject.getString("hostype"));
        paramMap.put("provinceCode",jsonObject.getString("provinceCode"));
        paramMap.put("cityCode", jsonObject.getString("cityCode"));
        paramMap.put("districtCode",jsonObject.getString("districtCode"));
        paramMap.put("address",jsonObject.getString("address"));
        paramMap.put("intro",jsonObject.getString("intro"));
        paramMap.put("route",jsonObject.getString("route"));
        //图片
        paramMap.put("logoData", jsonObject.getString("logoData"));

        JSONObject bookingRule = jsonObject.getJSONObject("bookingRule");
        paramMap.put("bookingRule",bookingRule.toJSONString());

        paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
        paramMap.put("sign", MD5.encrypt(this.getSignKey()));

        // 发送请求，调用预约挂号系统
        JSONObject respone =
           HttpRequestHelper.sendRequest(paramMap,this.getApiUrl()+"/api/hosp/saveHospital");
        System.out.println(respone.toJSONString());

        if(null != respone && 200 == respone.getIntValue("code")) {
            return true;
        } else {
            throw new YyghException(respone.getString("message"), 201);
        }
    }
```

查看`HttpRequestHelper.sendRequest`方法

```java
public static JSONObject sendRequest(Map<String, Object> paramMap, String url){
        String result = "";
        try {
            //封装post参数
            StringBuilder postdata = new StringBuilder();
            for (Map.Entry<String, Object> param : paramMap.entrySet()) {
                postdata.append(param.getKey()).append("=")
                        .append(param.getValue()).append("&");
            }
            log.info(String.format("--> 发送请求：post data %1s", postdata));
            byte[] reqData = postdata.toString().getBytes("utf-8");
            //发送post请求调用 
            byte[] respdata = HttpUtil.doPost(url,reqData);
            result = new String(respdata);
            log.info(String.format("--> 应答结果：result data %1s", result));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return JSONObject.parseObject(result);
    }
```





### 15.2 上传医院接口

在医院设置的页面中，通过填写表单中的医院code，签名key，预约平台路径，去设置该医院的信息，然后保存到该医院系统的数据库中。

结合尚医通业务流程就比较清晰

功能描述：

​	在医院管理页面中，医院系统可以将自己医院信息添加到预约挂号系统中，在预约挂号系统展示出来

实现:

​	调用预约挂号系统的接口，将医院数据发送到预约挂号系统添加到mongodb中

#### 15.2.1 医院系统添加功能

示例数据:

```json
{
  "hoscode": "1000_0",
  "hosname": "北京协和医院",
  "hostype": "1",
  "provinceCode": "110000",
  "cityCode": "110100",
  "districtCode": "110102",
  "address": "大望路",
  "intro": "北京协和医院是集医疗、教学、科研于一体的大型三级甲等综合医院，是国家卫生计生委指定的全国疑难重症诊治指导中心，也是最早承担高干保健和外宾医疗任务的医院之一，以学科齐全、技术力量雄厚、特色专科突出、多学科综合优势强大享誉海内外。在2010、2011、2012、2013、2014年复旦大学医院管理研究所公布的“中国最佳医院排行榜”中连续五年名列榜首。\n\n医院建成于1921年，由洛克菲勒基金会创办。建院之初，就志在“建成亚洲最好的医学中心”。90余年来，医院形成了“严谨、求精、勤奋、奉献”的协和精神和兼容并蓄的特色文化风格，创立了“三基”、“三严”的现代医学教育理念，形成了以“教授、病案、图书馆”著称的协和“三宝”，培养造就了张孝骞、林巧稚等一代医学大师和多位中国现代医学的领军人物，并向全国输送了大批的医学管理人才，创建了当今知名的10余家大型综合及专科医院。2011年在总结90年发展经验的基础上，创新性提出了“待病人如亲人，提高病人满意度；待同事如家人，提高员工幸福感”新办院理念。\n\n目前，医院共有2个院区、总建筑面积53万平方米，在职职工4000余名、两院院士5人、临床和医技科室53个、国家级重点学科20个、国家临床重点专科29个、博士点16个、硕士点29个、国家级继续医学教育基地6个、二级学科住院医师培养基地18个、三级学科专科医师培养基地15个。开放住院床位2000余张，单日最高门诊量约1.5万人次、年出院病人约8万余人次。被评为“全国文明单位”、“全国创先争优先进基层党组织”、“全国卫生系统先进集体”、“首都卫生系统文明单位”、“最受欢迎三甲医院”，荣获全国五一劳动奖章。同时，医院还承担着支援老少边穷地区、国家重要活动和突发事件主力医疗队的重任，在2008年北京奥运工作中荣获“特别贡献奖”。\n\n90多年来，协和人以执着的医志、高尚的医德、精湛的医术和严谨的学风书写了辉煌的历史，今天的协和人正为打造“国际知名、国内一流”医院的目标而继续努力。",
  "route": "东院区乘车路线：106、108、110、111、116、684、685路到东单路口北；41、104快、814路到东单路口南；1、52、802路到东单路口西；20、25、37、39路到东单路口东；103、104、420、803路到新东安市场；地铁1、5号线到东单。\n西院区乘车路线：68路到辟才胡同东口；更多乘车路线详见须知。",
  "logoData": "",
  "bookingRule": {
    "cycle": "10",
    "releaseTime": "08:30",
    "stopTime": "11:30",
    "quitDay": "-1",
    "quitTime": "15:30",
    "rule": [
      "西院区预约号取号地点：西院区门诊楼一层大厅挂号窗口取号",
      "东院区预约号取号地点：东院区老门诊楼一层大厅挂号窗口或新门诊楼各楼层挂号/收费窗口取号"
    ]
  }
}
```



controller

```java
@RequestMapping(value="/hospitalSet/save")
	public String createHospitalSet(ModelMap model,HospitalSet hospitalSet) {
		hospitalSetMapper.updateById(hospitalSet);
		return "redirect:/hospitalSet/index";
	}
```

service

```java
@Override
    public boolean saveHospital(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode",this.getHoscode());
        paramMap.put("hosname",jsonObject.getString("hosname"));
        paramMap.put("hostype",jsonObject.getString("hostype"));
        paramMap.put("provinceCode",jsonObject.getString("provinceCode"));
        paramMap.put("cityCode", jsonObject.getString("cityCode"));
        paramMap.put("districtCode",jsonObject.getString("districtCode"));
        paramMap.put("address",jsonObject.getString("address"));
        paramMap.put("intro",jsonObject.getString("intro"));
        paramMap.put("route",jsonObject.getString("route"));
        //图片
        paramMap.put("logoData", jsonObject.getString("logoData"));

        JSONObject bookingRule = jsonObject.getJSONObject("bookingRule");
        paramMap.put("bookingRule",bookingRule.toJSONString());

        paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
        paramMap.put("sign", MD5.encrypt(this.getSignKey()));

        JSONObject respone =
                HttpRequestHelper.sendRequest(paramMap,this.getApiUrl()+"/api/hosp/saveHospital");
        System.out.println(respone.toJSONString());

        if(null != respone && 200 == respone.getIntValue("code")) {
            return true;
        } else {
            throw new YyghException(respone.getString("message"), 201);
        }
    }

```



#### 15.2.2预约挂号系统

过程中会校验签名是否有效，才会进行添加数据

```java
@PostMapping("/saveHospital")
    public Result saveHospital(HttpServletRequest request){
        Map<String, String[]> rquestMap = request.getParameterMap();
        Map<String, Object> parameterMap = HttpRequestHelper.switchMap(rquestMap);
        // 判断该医院的数据签名是否一致
        // 获取签名
        String signMD5 = (String) parameterMap.get("sign");

        // 根据医院编号查询数据库
        String hoscode = (String) parameterMap.get("hoscode");

        String signKeyFromPersis =  hospitalSetService.getSignKey(hoscode);

        String signKeyFromPersisMD5 = MD5.encrypt(signKeyFromPersis);

        // 判断签名是否一致
        if(!signKeyFromPersisMD5.equals(signMD5)){
            // 抛出签名不一致错误
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        // 为了数据传输我们将图片进行了base64的编码
        // 传输过程中"+"会转换为" "空格，所以我们需要转换回来
        String logoData = (String) parameterMap.get("logoData");
        logoData = logoData.replace(" ","+");
        parameterMap.put("logoData",logoData);

        hospitalService.save(parameterMap);
        return Result.ok();
    }
```

`hospitalService.save(parameterMap)`

将医院添加到mongodb中

```java
@Override
    public void save(Map<String, Object> parameterMap) {

        // 将参数mapper转换为对象
        String mapString = JSONObject.toJSONString(parameterMap);

        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);

        // 判断数据是否存在
        String hoscode = hospital.getHoscode();
        Hospital existHospital = hospitalRepository.getHospitalByHoscode(hoscode);
        // 存在，更新
        if(existHospital!=null){
            hospital.setStatus(existHospital.getStatus());
            hospital.setCreateTime(existHospital.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);

            hospitalRepository.save(hospital);
        }else {
            // 不存在，保存
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);

        }
    }
```



### 15.3查询医院

在医院系统的医院管理页面可以查询到，之前在医院系统中添加的医院信息，也就是自己医院的信息

#### 15.3.1医院系统

apiController

与前面的上传的思路一样

通过调用预约挂号平台的接口去获取该医院的信息，然后在医院系统的医院管理页面进行渲染

```java
@RequestMapping("/hospital/index")
	public String getHospital(ModelMap model,HttpServletRequest request,RedirectAttributes redirectAttributes) {
		try {
			HospitalSet hospitalSet = hospitalSetMapper.selectById(1);
			if(null == hospitalSet || StringUtils.isEmpty(hospitalSet.getHoscode()) || StringUtils.isEmpty(hospitalSet.getSignKey())) {
				this.failureMessage("先设置医院code与签名key", redirectAttributes);
				return "redirect:/hospitalSet/index";
			}

            // 调用接口apiService.getHospital()，service不详细说明
			model.addAttribute("hospital", apiService.getHospital());
		} catch (YyghException e) {
			this.failureMessage(e.getMessage(), request);
		} catch (Exception e) {
			this.failureMessage("数据异常", request);
		}
		return "hospital/index";
	}
```



#### 15.3.2预约挂号系统

获取医院系统请求中的医院编号可以在mongodb中查找出该医院的信息

controller

```java
@PostMapping("/hospital/show")
    public Result getHospital(HttpServletRequest request){
        Map<String, Object> parameterMap = HospitalUtils.getParameterMap(request);
        String targetSignKey = (String) parameterMap.get("sign");
        String hoscode = (String) parameterMap.get("hoscode");
        String signKeySource = hospitalSetService.getSignK
            ey(hoscode);

        // 如果签名一致

        if (!HospitalUtils.isSignKeyValidated(targetSignKey, signKeySource)) {
            log.error("上传医院失败，签名不一致");
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        // 执行操作
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        // 返回result
        return Result.ok(hospital);

    }
```

service

```java
/**
     * 根据hoscode查找医院
     * @param hoscode
     */
    @Override
    public Hospital getByHoscode(String hoscode) {
        Hospital hospitalByHoscode = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospitalByHoscode;
    }
```













### 15.4上传科室

在医院系统点击添加科室，输入以下示例数据，将数据发送到预约挂号系统中，保存到mongodb中

示例数据

```json
[
{"hoscode":"1000_8","depcode":"200050923","depname":"门诊部核酸检测门诊(东院)","intro":"门诊部核酸检测门诊(东院)","bigcode":"44f162029abb45f9ff0a5f743da0650d","bigname":"全部科室"},
{"hoscode":"1000_8","depcode":"200050924","depname":"国际医疗部门诊","intro":"国际医疗部门诊","bigcode":"44f162029abb45f9ff0a5f743da0650d","bigname":"全部科室"},
{"hoscode":"1000_8","depcode":"200050931","depname":"临床营养科(西院国际医疗)","intro":"临床营养科(西院国际医疗)","bigcode":"44f162029abb45f9ff0a5f743da0650d","bigname":"全部科室"}
]
```



#### 15.4.1医院系统

```java
@RequestMapping(value="/department/save",method=RequestMethod.POST)
	public String save(String data, HttpServletRequest request) {
		try {
			apiService.saveDepartment(data);
		} catch (YyghException e) {
			return this.failurePage(e.getMessage(),request);
		} catch (Exception e) {
			return this.failurePage("数据异常",request);
		}
		return this.successPage(null,request);
	}
```

service

```java
 @Override
    public boolean saveDepartment(String data) {
        JSONArray jsonArray = new JSONArray();
        if(!data.startsWith("[")) {
            JSONObject jsonObject = JSONObject.parseObject(data);
            jsonArray.add(jsonObject);
        } else {
            jsonArray = JSONArray.parseArray(data);
        }

        // 发送多个科室
        for(int i=0, len=jsonArray.size(); i<len; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("hoscode",this.getHoscode());
            paramMap.put("depcode",jsonObject.getString("depcode"));
            paramMap.put("depname",jsonObject.getString("depname"));
            paramMap.put("intro",jsonObject.getString("intro"));
            paramMap.put("bigcode", jsonObject.getString("bigcode"));
            paramMap.put("bigname",jsonObject.getString("bigname"));

            paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
            paramMap.put("sign",MD5.encrypt(this.getSignKey()));
            // 发送请求
            JSONObject respone = HttpRequestHelper.sendRequest(paramMap,this.getApiUrl()+"/api/hosp/saveDepartment");
            System.out.println(respone.toJSONString());

            if(null == respone || 200 != respone.getIntValue("code")) {
                throw new YyghException(respone.getString("message"), 201);
            }
        }
        return true;
    }
 @Override
    public boolean saveDepartment(String data) {
        JSONArray jsonArray = new JSONArray();
        if(!data.startsWith("[")) {
            JSONObject jsonObject = JSONObject.parseObject(data);
            jsonArray.add(jsonObject);
        } else {
            jsonArray = JSONArray.parseArray(data);
        }

        // 发送多个科室
        for(int i=0, len=jsonArray.size(); i<len; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("hoscode",this.getHoscode());
            paramMap.put("depcode",jsonObject.getString("depcode"));
            paramMap.put("depname",jsonObject.getString("depname"));
            paramMap.put("intro",jsonObject.getString("intro"));
            paramMap.put("bigcode", jsonObject.getString("bigcode"));
            paramMap.put("bigname",jsonObject.getString("bigname"));

            paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
            paramMap.put("sign",MD5.encrypt(this.getSignKey()));
            // 发送请求
            JSONObject respone = HttpRequestHelper.sendRequest(paramMap,this.getApiUrl()+"/api/hosp/saveDepartment");
            System.out.println(respone.toJSONString());

            if(null == respone || 200 != respone.getIntValue("code")) {
                throw new YyghException(respone.getString("message"), 201);
            }
        }
        return true;
    }

```

#### 15.4.2预约挂号系统

```java
 @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest request){

        ParameterWrapper parameterWrapper = ParameterWrapper.build(request);
        Department department = parameterWrapper.toObject(Department.class);
//        String hoscode = parameterWrapper.getParameter("hoscode");
//        String signMD5 = parameterWrapper.getParameter("sign");
        String hospcode = parameterWrapper.getHospcode();
        String signMD5 = parameterWrapper.getSignKey();
        String signKey = hospitalSetService.getSignKey(hospcode);
        // 判断签名是否一致
        parameterWrapper.isSignKeyValidated(signMD5,signKey);
        departmentService.save(department);
        log.info("上传医院成功!");
        return Result.ok();
    }
```

service

```java
public boolean save(Department department) {
        try{

            Department departmentFromMongo = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
            if(departmentFromMongo!=null){
                departmentFromMongo.setUpdateTime(new Date());
                departmentFromMongo.setIsDeleted(0);
                departmentRepository.save(departmentFromMongo);
            }else{
                department.setCreateTime(new Date());
                department.setUpdateTime(new Date());
                department.setIsDeleted(0);
                departmentRepository.save(department);
            }

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
```



### 15.5查询科室与删除科室

完成

### 15.6 排班查询与删除

完成



## 16.backTo预约挂号后台管理系统

### 16.1在医院列表中查看详情

在此前，医院系统通过医院管理页面中的添加功能，将其医院的详细信息上传到预约挂号后台管理系统，保存到mongodb中。在预约挂号后台管理系统中，在医院列表中点击查看详情，可以查看该医院的详情信息

在mongodb中查询出对应的医院详情，以显示。

但有一个问题，该医院信息保存到mongodb中，其中医院等级编号为数据，当我们显示时需要显示为文字（比如1为三甲医院），也就是说**service-hosp模块需要调用service-cmn中的接口去获取数据字典中对应的数据。**

#### 16.1.1 使用远程调用service-cmn

`注册中心：nacos`

`远程调用：`

##### 16.1.1.1 nacos注册中心

安装nacos

- 下载nacos-server
- 双击startup.sh

访问：localhost:8848/nacos

用户名密码：nacos

依赖

```xml
<dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
```

配置

```yml
cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
```

在启动类中配置注解

`@EnableDiscoveryClient`

```java
@SpringBootApplication
@ComponentScan(value = "com.cjc")
@MapperScan("com.cjc.syt.hosp.mapper")
@EnableDiscoveryClient
public class ServiceHospApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class,args);
    }
}

```

完成，即可注册服务

##### 16.1.1.2 远程调用feign

###### service-hosp创建controller

```java
/**
     * 查询医院了列表
     */
    @GetMapping("/list/{page}/{limit}")
    public Result getList(@PathVariable("page") int page,
                          @PathVariable("limit") int limit
    , HospitalQueryVo hospitalSetQueryVo){
        Page<Hospital> pageModel = hospitalService.selectHospPage(page,limit,hospitalSetQueryVo);
        return Result.ok(pageModel);
    }
```



###### service-cmn提供接口

提供接口查询数据字典

```java
/**
     * 根据dictCode和value查询
     * @param dictCode
     * @param value
     * @return
     */
    @GetMapping("/getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode,
                          @PathVariable String value){
        String dictName = dictService.getDictName(dictCode,value);
        return dictName;
    }

    /**
     * 根据value查询
     * @param value
     * @return
     */
    @GetMapping("/getName/{value}")
    public String getName(@PathVariable String value){
        String dictName = dictService.getDictName("",value);
        return dictName;
    }
```



**做远程调用**

创建远程调用接口模块

service_client.service_cmn_client

`@FeignClient("service-cmn")`表示在注册中心钟找service-cmn模块进行注入

```java
@FeignClient("service-cmn")
public interface DictFeignClient {

    /**
     * 根据dictCode和value查询
     * @param dictCode
     * @param value
     * @return
     */
    @GetMapping("/admin/cmn/dict/getName/{dictCode}/{value}")
    public String getName(@PathVariable("dictCode") String dictCode,
                          @PathVariable("value") String value);

    /**
     * 根据value查询
     * @param value
     * @return
     */
    @GetMapping("/admin/cmn/dict/getName/{value}")
    public String getName(@PathVariable("value") String value);


}
```

###### service_hosp调用

引入service_cmn_client依赖

`@EnableDiscoveryClient`开启搜索客户端`@EnableFeignClients(basePackages = "com.cjc")`扫描引入的feignclient模块（service_cmn_client）

```java
@SpringBootApplication
@ComponentScan(value = "com.cjc")
@MapperScan("com.cjc.syt.hosp.mapper")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.cjc")
public class ServiceHospApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class,args);
    }
}

```



#### 16.1.2前端页面显示

##### 搜索框

搜索医院查询框，通过省市，医院名称查询

问题：当用户在查询框中，选选择省，然后再显示该省的市，然后用户选择市

后端：当选择省时，查询该省下的市，进行显示

获取省份接口以及获取市区接口

service_hosp模块

HospitalController

```java
 /**
     * 获取省份
     * @return
     */
    @GetMapping("/get/province/list")
    @ApiOperation(value = "获取省份")
    public Result getProvinceList(){
        return hospitalService.getProvince();
    }

    @GetMapping("/get/city/list/by/{provinceId}")
    @ApiOperation(value = "根据省份id获取对应市区列表")
    public Result getCityList(@PathVariable("provinceId") long provinceId){
        return hospitalService.getCityListByProvinceId(provinceId);
    }
```

service

```java
@Override
    public Result getProvince() {
        long provinceId = 86;
        return dictFeignClient.findChildrenData(provinceId);
    }

    @Override
    public Result getCityListByProvinceId(long provinceId) {
        return dictFeignClient.findChildrenData(provinceId);
    }
```

远程接口

```java
@GetMapping("/admin/cmn/dict/find/children/data/{id}")
public Result findChildrenData(@PathVariable("id")Long id);
```

##### 前端显示搜索框

##### 显示数据

显示 医院logo，医院名称，医院类型，医院详细地址，状态，创建时间以及操作

##### 操作

######  设置状态 

设置状态，状态为已上线，那么预约挂号平台就显示该医院，供用户进行预约挂号。



###### 查看医院详情

跳转到医院详情页面

简单，但有一个问题就是，接口返回的hospital中还有bookingRule对象，如果直接将返回的hospital赋值给data中的hospital：{} ,然后在前端页面通过hospital.bookingrule调用会出现错误。

解决方法：

接口返回map<String,Object>，将bookingrule和hospital放入map中，返回，然后前端调用。



### 16.2 查看科室以及该科室的排班

前端页面：

在医院列表页面中，点击查看排班，查看该医院的排班

科室页面：

左边以树形显示科室，点击大科室，然后显示大科室以下的小科室

当点击小科室时，左边显示该该科室的排班情况



后端接口：

根据hoscode查询该医院对应的科室

根据科室查找对应的排班情况



### 16.3 某一时间的排班详情

使用mongo的聚合查询



## 16.4 整合网关

spring coud gateway代替zuul

跨域请求可以统一处理

### 16.4.1 gateway

创建一个新的模块

依赖

```xml
<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
        <!-- 服务注册 -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
```

配置

```yml
server:
  port: 80
spring:
  application:
    name: service-gateway
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
#        使用服务发现路由
    gateway:
      discovery:
        locator:
          enabled: true
      routes:
        - id: service-hosp
          uri: lb://service-hosp
          predicates: Path=/*/hosp/**
        - id: service-cmn
          uri: lb://service-cmn
          predicates: Path=/*/cmn/**
```



### 16.4..2 统一解决跨域问题

注释掉之前在controller上的@CrossOrigin注解

```java
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return new CorsWebFilter( source);

    }

}

```

# 17.用户前端系统

nuxt：服务端渲染技术，基于vue

下载nuxt安装包，解压

### 收获

springboot与mongodb的整合

两个系统之间的接口调用，需要设置签名，保存到双方的系统中，通过判断签名是否一致，来判断该请求是否有效

