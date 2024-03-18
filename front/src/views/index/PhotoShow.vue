<script setup>
import {onMounted, ref} from 'vue'
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

onMounted(() => {
  imageGet()
})
</script>

<template>
  <div>
    <div  :class="{'odd-image':index%2!==0}" style="margin: 10vh;display: flex" v-for="(img,index) in urls" >
      <img loading="lazy" style="height: 56vh"  class="picture" :src="img" :key="index"   alt="this a picture"/>
      <div style="width: 100%;text-align: center;display: flex;flex-direction: column ;justify-content: center;align-items: ;">
        <div class="font">{{place[index]}}</div>
        <div class="font">{{theme[index]}}</div>
        <div class="font" style="">{{date[index]}}</div>
      </div>
    </div>
  </div>
</template>
<style scoped>
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