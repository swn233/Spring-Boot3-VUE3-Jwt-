<script setup>
import { ref, computed } from 'vue';
import {get}from '@/net'
import { onMounted,onBeforeMount,onUpdated } from 'vue'
import axios from "axios";
import {post} from '../../net/index'
import {ElImage, ElMessage, componentSizeMap} from "element-plus";
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

//为图片添加渐入效果

let prevOpacity={};
let imageElement=[];
let shadow="3px 3px 5px rgb(44,44,44)";

function createObserver(){
  let observer;
  let options={
    root:document.querySelector(".image-container"),
    rootMargin:"0px",
    threshld:buildThresholdlist(),
  };

  observer=new IntersectionObserver(handleIntersect,options);
  imageElement.forEach((item,index) => {
    item.setAttribute("id",index);
    // console.log(item.getAttribute("id"));
    prevOpacity[item.getAttribute("id")] = 0;
    observer.observe(item);
  });
}

function buildThresholdlist() {
  let thresholds = [];
  let numSteps = 3;

  for (let i = 1.0; i <= numSteps; i++) {
    let opacity= i / numSteps;
    thresholds.push(opacity);
  }

  thresholds.push(0);
  return thresholds;
}

function handleIntersect(entries, observer) {
  entries.forEach((entry) => {
    
    // console.log(prevOpacity[entry.target.src] );
    if (entry.intersectionRatio > prevOpacity[entry.target.getAttribute("id")]) {
      if(entry.target.getAttribute("id")==25){
    console.log(entry.intersectionRatio);
    }
      // entry.target.style.opacity = entry.intersectionRatio;
      if(entry.target.getAttribute("id")==25){
        console.log(entry.target.getAttribute("id")+"::"+entry.target.style.opacity);
      }
      // if(entry.target.style.opacity>0.9){
      //   entry.target.style.opacity=0.9;
      // }
      entry.target.style.boxShadow=shadow;
      // entry.target.style.translate=translate;
      prevOpacity[entry.target.getAttribute("id")] = entry.intersectionRatio;
    } 
   
  });
}

onMounted(() => {
  imageGet();
})

onUpdated(()=>{
  imageElement=document.querySelectorAll(".image-item");
  createObserver();
})
</script>

<template>
  <div>
  <el-container >
    <el-affix  :offset="0" style="width: 100%; text-align:center;height: 60px;z-index: 50">
      <div style="border-bottom-left-radius: 10px;border-bottom-right-radius:10px ; width:100%;height: 60px; background-color: #c3fff6;"></div>
    </el-affix>

  </el-container>

  <div class="image-container" style="height: 100vh;scrollbar-width: none;">
    <div class="image-column" v-for="(col, index_col) in imageColumns" :key="index_col">
      <ElImage
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
          loading="lazy"
      />
    </div>
  </div>
  <el-container>
    <el-affix :offset="800" style="width: 100%; text-align:center;height: 60px;z-index: 50">
      <div style="border-top-left-radius: 10px;border-top-right-radius:10px ; width:100vw;height: 60px; background-color: #eaeaea;"></div>
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
  margin: 0.03% 0.03%; /* 调整行与行之间的间距，根据需要进行调整 */
  flex-wrap: wrap;
  }

.image-item {
  margin-top: 10px;
  width: 100%; /* 图片占据行的 30% 宽度，留出一些空白间距 */
  opacity: 0.9; /* 初始时设置透明度为 0 */
  transition: opacity 0.5s,
  box-shadow 0.5s;
  box-shadow: 0 0 0 rgb(255, 255, 255);
}

.image-item:hover{
  opacity: 1;
}
</style>
