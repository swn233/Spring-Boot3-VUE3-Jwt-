<script setup>
import { Transition } from 'vue'
</script>

<template>
  <body style="margin: 0 0 0 0">
  <div style="width: 100vw;height: 100vh;overflow:hidden;display: flex">
  <div style="flex: 1; background-color: black ;">
    <el-image src="https://s2.loli.net/2023/12/08/vc8P5tFisApqMY4.jpg" style="width: 100%;height: 100%" fit="cover"/>
  </div>
  <div class="welcome-title">
    <p style="font-size: 28px;font-weight: bold">欢迎来到摄影照片管理系统</p>
    <p style="font-size: 14px; margin-top: 10px">在这里你可以保存你满意的作品，并让更多人看见</p>
    <p style="font-size: 14px; margin-top: 5px">在这里你可以与世界连接，连接人与自然</p>

  </div>
    <div class="right-card">
      <router-view v-slot="{ Component }">
        <Transition name="el-fade-in-linear"  mode="out-in">
          <component :is="Component"></component>
        </Transition>
      </router-view>
    </div>
  </div>
  </body>
</template>

<style scoped>
.welcome-title{
  position:absolute;
  bottom:30px;
  left:30px;
  color: white;
  text-shadow: 0px 0px 10px;
}
.right-card{
  width: 25%;
  z-index: 1;
  background-color: white;
}
</style>