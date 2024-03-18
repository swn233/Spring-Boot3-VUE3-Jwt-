<script lang="ts" setup>
import {onMounted, onUpdated, ref} from 'vue'
import type { UploadProps, UploadUserFile } from 'element-plus'
import {post,get,accessHeader} from '@/net'
import {ElMessage} from "element-plus";
import router from "@/router";
import {Lock, User,Message,Edit,Top,ArrowUp} from "@element-plus/icons-vue";
const selectedFile=ref()
const windowHeight=ref()
const fileList = ref<UploadUserFile[]>([
])
const urls=ref({})
const headers=ref({})
function getHeaders(){
  headers.value=accessHeader();
}
function imageGet(){
  get("/api/data/image",(data)=>{
    fileList.value = data;
    // 遍历 fileList 数组，为每个对象设置 name 属性
    fileList.value.forEach((file) => {
      file.name = file.url; // 将 url 赋值给 name
    });

  })
}

//删除图片
const handleRemove: UploadProps['onRemove'] = (uploadFile, uploadFiles) => {
  //删除事件触发前判断是否为成功上传的文件
  if(uploadFile.status==="success"){
    console.log(uploadFile)
    const formData = new FormData();
    let url=uploadFile.url
// 判断 url 是否以 'blob:' 开头,是的话为刚刚上传的图片，数据与数据库中的不同
    if (url.startsWith('blob:')) {
      url=uploadFile.name
      //console.log(response)
    }

    formData.append('url', url);

    post('/api/data/delete',formData,()=>{
      ElMessage.success("图片删除成功")
    },()=>{
      ElMessage.error("图片删除失败")
    })
  }
}

//点击图片链接后跳转
const handlePreview: UploadProps['onPreview'] = (file) => {
  // console.log(file.url)
  if (file.url) {
    // 在新标签页中打开 URL
    window.open(file.url, '_blank');
  }
}

//图片上传成功和失败后的处理
const handleSuccess:UploadProps['onSuccess']=(response, uploadFile, uploadFiles)=>{
  // 判断response是否以https开头,是的话上传成功
  console.log(response)
  let resp=response
  if (resp.startsWith('https')) {
    ElMessage.success("上传成功")
    uploadFile.name=response
  }else {
    ElMessage.error(response)
    uploadFile.status="fail"
  }
}

const handleError:UploadProps['onError']=(error, uploadFile, uploadFiles)=>{

}
//初始化
onMounted(() => {
  imageGet()
  getHeaders()
})
</script>

<template>
  <div>
  <el-container style="">

    <el-upload style="width: 66%;min-width: 650px;text-align: center"
        v-model:file-list="fileList"
        class="upload affix-container"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        list-type="picture"
        action="http://43.142.101.58:8080/api/data/upload"
        method="post"
        accept="image"
        :multiple="true"
        :limit="9999"
        :on-success="handleSuccess"
        :on-error="handleError"
        :headers="headers"
    >
      <el-affix style="" target=".affix-container" :offset="10">
        <el-button class="button" style="border-radius: 10px;width: 100px;background-color: #10c191" type="success">Upload</el-button>
      </el-affix>
      <template #tip>
        <div class="el-upload__tip">
          jpg/png files with a size less than 5Mb
        </div>
      </template>
    </el-upload>
  </el-container>

  </div>
</template>

<style scoped>
.button{
  opacity:0.7;
  transition:opacity 0.1s ease;
}
.button:hover{
  opacity: 1;
}
</style>