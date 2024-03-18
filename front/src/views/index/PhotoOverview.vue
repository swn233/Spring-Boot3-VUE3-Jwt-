<script setup>
import { ref, computed } from 'vue';
import {get}from '@/net'
import { onMounted,onBeforeMount  } from 'vue'
import axios from "axios";
import {post} from '../../net/index'
import {ElMessage} from "element-plus";
import router from "@/router"
import {ArrowUp} from "@element-plus/icons-vue";

const urls = ref([
  // 'https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg',
  // 'https://s2.loli.net/2023/12/08/vc8P5tFisApqMY4.jpg',
  // 'https://fuss10.elemecdn.com/1/34/19aa98b1fcb2781c4fba33d850549jpeg.jpeg',
  // 'https://s2.loli.net/2023/12/08/UN5wmVvPM7zgfeL.jpg'
])
//从数据库中读取图片资源，包括url，摄影时间，地点，照片主题
const image=ref({})

//实际渲染的数据
const imageColumns = computed(()=>{
  //console.log("每列有："+ColItemsNum.value)
  //console.log(urls)
  let temp=[];
  for (let i = 0; i < colNums; i++) {
    temp.push(urls.value.slice(i * ColItemsNum.value, (i + 1) * ColItemsNum.value))
  }
  //console.log(temp)
  return temp
})

// 将图片链接按照三列进行分组
const colNums = 3

    // const ColItemsNum =  4
    // Math.ceil(urls.value.length / colNums)

const ColItemsNum =  computed(()=>{
  let temp=Math.ceil(urls.value.length / colNums)
  //console.log(temp)
  return temp
})

function imageGet(){
  get("/api/data/image",(data)=>{
    image.value = data;
    // console.log("image.value:", image.value);
    urls.value = image.value.map((item) => item.url);
    // console.log(urls.value);
  })
}

onMounted(() => {
  imageGet()
})
</script>

<template>
  <div>
  <el-container >
    <el-affix  :offset="0" style="width: 100%; text-align:center;height: 60px;z-index: 50">
      <div style="border-bottom-left-radius: 10px;border-bottom-right-radius:10px ; width:100%;height: 60px; background-color: #c3fff6;"></div>
    </el-affix>

  </el-container>

  <div class="image-container">
    <div class="image-column" v-for="(col, index_col) in imageColumns" :key="index_col">
      <el-image
          class="image-item"
          v-for="(url,index) in col"
          :key="url"
          :src="url"
          fit="contain"
          :preview-src-list="urls"
          :initial-index="index+index_col*ColItemsNum"
          :zoom-rate="1.1"
          :max-scale="1.5"
          :min-scale="0.5"
          lazy
      />
    </div>
  </div>
  <el-container>
    <el-affix :offset="800" style="width: 100%; text-align:center;height: 60px;z-index: 50">
      <div style="border-top-left-radius: 10px;border-top-right-radius:10px ; width:100%;height: 60px; background-color: #eaeaea;"></div>
    </el-affix>
  </el-container>
  </div>
</template>

<style>
.image-container {
  overflow-y: auto;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.image-column {
  width: 32.6%;/* 每行占据父容器的 1/3 宽度 */
  display: flex;
  flex-direction: column;
  margin: 5px 5px; /* 调整行与行之间的间距，根据需要进行调整 */
  flex-wrap: wrap;
}

.image-item {
  margin-top: 10px;
  width: 100%; /* 图片占据行的 30% 宽度，留出一些空白间距 */
  opacity: 0.95; /* 初始时设置透明度为 0 */
  transition-property: opacity;
  transition-duration: 0.1s;
}

.image-item:hover{
  opacity: 1;
}
</style>
