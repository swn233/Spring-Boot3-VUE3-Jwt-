<script setup>
import {onMounted, ref,toRaw, toValue,nextTick,onUpdated} from 'vue'
import {get} from "@/net";
const urls = ref([
  // 'https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg',
  // 'https://s2.loli.net/2023/12/08/vc8P5tFisApqMY4.jpg',
  // 'https://fuss10.elemecdn.com/1/34/19aa98b1fcb2781c4fba33d850549jpeg.jpeg',
  // 'https://s2.loli.net/2023/12/08/UN5wmVvPM7zgfeL.jpg'
])
const date=ref([])
const theme=ref([])
const place=ref([])
//从数据库中读取图片资源，包括url，摄影时间，地点，照片主题
const image=ref({})
function imageGet(){
   get("/api/data/image",(data)=>{
    image.value = data;
    place.value=image.value.map((item)=>item.place);
    urls.value = image.value.map((item) => item.url);
    date.value=image.value.map((item)=>item.date?item.date.slice(0,10):'');
    theme.value=image.value.map((item)=>item.theme);
  })
}

// 实现图片渐入效果
//为每一个图片添加一个当前透明度
let prevOpacity={};
let imageElement;

function createObserver() {
  let observer;
  let options = {
    root: null,
    rootMargin: "0px",
    threshold: buildThresholdList(),
  };

  observer = new IntersectionObserver(handleIntersect, options);

  imageElement.forEach((item,index) => {
    prevOpacity[item.src] = 0;
    observer.observe(item);
  });
  
  // observer.observe(imageElement);
}

function buildThresholdList() {
  let thresholds = [];
  let numSteps = 100;

  for (let i = 1.0; i <= numSteps; i++) {
    let opacity= i / numSteps;
    thresholds.push(opacity);
  }

  thresholds.push(0);
  return thresholds;
}

function handleIntersect(entries, observer) {
  
  entries.forEach((entry) => {
    // console.log(prevOpacity);
    // console.log(prevOpacity[entry.target.src] );
    if (entry.intersectionRatio > prevOpacity[entry.target.src]) {
      entry.target.style.opacity = entry.intersectionRatio;
      prevOpacity[entry.target.src] = entry.intersectionRatio;
    } 
    // else {
    //   entry.target.style.backgroundColor = decreasingColor.replace(
    //     "ratio",
    //     entry.intersectionRatio,
    //   );
    // }
  });
}


onMounted(() => {
  imageGet();
})

onUpdated(() => {
  imageElement=document.querySelectorAll(".picture");
  createObserver();
  console.log("update");
})

</script>

<template>
  <div class="image-container" style="overflow:auto;height: 100vh;scrollbar-width: none;">
    <div  ref="imageElement" :class="{'odd-image':index%2!==0}" style="margin: 10vh;display: flex" v-for="(img,index) in urls" >
      <img   loading="lazy" style="height: 56vh"  class="picture" :src="img" :key="index"   alt="this a picture"/>
      <div style="width: 100%;text-align: center;display: flex;flex-direction: column ;justify-content: center;">
        <div class="font">{{place[index]}}</div>
        <div class="font">{{theme[index]}}</div>
        <div class="font" style="">{{date[index]}}</div>
      </div>
    </div>
  </div>
</template>
<style scoped>
.picture{
  transition: opacity  0.3s;
  opacity: 0;
}

.odd-image{
  text-align: right;
  flex-direction: row-reverse;
}
.font {
  font-family:  freight-big-pro, serif;
  font-size: 1.5em;
  line-height: 2em;
  color: black;
  text-align: left; /* 文字左对齐 */
  text-indent: 3.5em; /* 第一个字母左缩进一个字母的宽度 */
}
</style>