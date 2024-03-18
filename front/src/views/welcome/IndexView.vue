<script lang="ts" setup>
import { Transition} from 'vue'
import {takeRole}from '../../net/index'
import { ref,onMounted } from 'vue'
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting, ArrowUp,
} from '@element-plus/icons-vue'
import {logout} from "@/net";
import {ElMessage, UploadProps} from "element-plus";
import router from "@/router";
const scrollbarRef=ref()
const scrolltop=ref()
const role=ref(false)
function logout_func(){
  logout(()=>{
    router.push("/welcome")
  })
}

const onscroll: ({ scrollTop: number, scrollLeft: number }) => void = ({ scrollTop, scrollLeft }) => {
  scrolltop.value=scrollTop
};

//回到顶部
function backup() {
  const start = scrolltop.value; // 获取当前滚动位置
  const end = 0; // 滚动到顶部的位置
  const duration = 500; // 过渡时间，单位是毫秒

  const startTime = performance.now(); // 获取动画开始时间

  function animateScroll(time: number) {
    const elapsed = time - startTime;
    const progress = Math.min(elapsed / duration, 1); // 计算动画进度

    const easedProgress = easeInOutCubic(progress); // 使用缓动函数

    const newPosition = start + (end - start) * easedProgress;

    scrollbarRef.value!.setScrollTop(newPosition);

    if (progress < 1) {
      window.requestAnimationFrame(animateScroll); // 继续下一帧动画
    }
  }

  window.requestAnimationFrame(animateScroll);
}

// 缓动函数，这里使用了先慢后快的缓动函数（easeInOutCubic）
function easeInOutCubic(t: number): number {
  return t < 0.5 ? 4 * t * t * t : (t - 1) * (2 * t - 2) * (2 * t - 2) + 1;
}

onMounted(()=>{
  const tmp=takeRole()
  const admin='"admin"'
  if (tmp==admin)
    role.value=true
  else
    role.value=false
})
</script>

<template>
  <div class="common-layout">
    <el-container style="width: 100vw; height: 100vh;">
      <el-aside class="showMenu" style="background-color: white;overflow-y: hidden" width="0px">
        <!--        icon-->
        <el-affix :offset="10" style="text-align: left;margin-left:70px;height: 0px">
          <el-image  class="image" style="width: 50px" src="https://s2.loli.net/2023/12/21/jxC7e5nNRvHpdDJ.png"></el-image>
        </el-affix>
        <div class="shadow" style="border-top-right-radius:10px;background-color:#eaeaea; height: 100px"></div>
        <el-menu
            style="border-bottom-right-radius:10px; background-color: #eaeaea; width: 100%;height: 100%; font-family: 'freight-big-pro', serif;font-size: 15px; border:0px"
            default-active="1-1"
            class="shadow el-menu-vertical font"
            :collapse="false"
        >
<!--侧栏1          -->
          <el-menu-item @click="router.push('/index/')" index="1">
            <template #title>
              <span  class="font">Overview</span>
            </template>
          </el-menu-item>
<!--          侧栏2-->
          <el-menu-item index="2" @click="router.push('/index/PhotoShow')">
            <template #title >
              <span  class="font">Photo</span>
            </template>
          </el-menu-item>
          <!--          侧栏3-->
          <el-menu-item  v-if="role" @click="router.push('/index/PhotoManage')" index="3" >
            <template #title >
              <span  class="font">Manage</span>
            </template>
          </el-menu-item>
          <el-menu-item  v-if="role" index="4" @click="logout_func">

            <template #title class="font">
              <span  class="font">Exit</span>
            </template>
          </el-menu-item>
          <div style="height: 50%"></div>
          <el-menu-item :disabled="true" index="5" >
            <template #title class="font">
              <span style="font-size: 28px;font-weight: bold" class="font">by swn</span>
            </template>
          </el-menu-item>
          <div style="height: 5%"></div>
          <div>
            <a href="https://beian.miit.gov.cn/" style=" text-align: left;font-size: 12px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; " class="font" target="_blank">沪ICP备2023037215号</a>
          </div>
          <div>
            <a href="https://beian.mps.gov.cn/#/query/webSearch" style=" text-align: left;font-size: 12px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; " class="font" target="_blank">沪公网安备31011302007557号</a>
          </div>
        </el-menu>
      </el-aside>

<!--内容页面-->
      <el-container>
        <div class="scrollObj" style="background-color: white;width: 100%;height: 100%;" >
          <el-scrollbar ref="scrollbarRef" @scroll="onscroll">
          <!--        backup-->
          <div style="position: absolute;right: 50px">
            <el-affix  :offset="10" style="text-align:center;margin-left:100px;height: 0px">
              <el-button class="shadow" @click="backup" size="large" style="width:35px; background-color: #f6f6f6 ;border-radius: 100px;border:0px" :icon="ArrowUp" dark />
            </el-affix>
          </div>
          <router-view v-slot="{ Component }">
            <Transition name="el-fade-in-linear"  mode="out-in">
              <component :is="Component"></component>
            </Transition>
          </router-view>
          </el-scrollbar>
        </div>
      </el-container>
    </el-container>
  </div>
</template>


<style scoped>

.el-menu-item {
  display: block !important;
}

.font {
  font-family:  freight-big-pro, serif;
  font-size: 3.5em;
  line-height: 1em;
  color: black;
  text-align: left; /* 文字左对齐 */
  text-indent: 3.5em; /* 第一个字母左缩进一个字母的宽度 */
}

.showMenu {
  width: 0px;
  overflow: hidden;
  transition: width 0.3s ease; /* 添加过渡效果 */
}

.image:hover ,
.showMenu:hover {
  width: 350px;
}
.shadow{
  transition: box-shadow 0.3s;
}
.shadow:hover{
  box-shadow: 4px 2px 8px rgba(0, 0, 0, 0.1);;
}
</style>