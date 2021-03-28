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

