<script setup>
import { Transition } from 'vue'
</script>

<template>
  <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex">
    <div style="flex: 1;background-color: black">
      <el-image src="http://pic.bizhi360.com/bbpic/53/11153.jpg" style="width: 100%;height: 100%" fit="cover"/>
    </div>
    <div class="welcome-title">
      <div style="font-size: 30px;font-weight: bold">欢迎来到全球最大同性交友网站</div>
      <div style="margin-top: 10px;">在这里你可以学习哲学，并且与哲学之父密切交流</div>
      <div style="margin-top: 5px">在这里你可以同性交友，因为都是男的，没有学哲学的女生</div>
    </div>
    <div class="right-card">
      <router-view v-slot="{ Component }">
        <Transition name="el-fade-in-linear"  mode="out-in">
          <component :is="Component"></component>
        </Transition>
      </router-view>
    </div>
  </div>
</template>

<style scoped>
.welcome-title {
  position: absolute;
  bottom:30px;
  left: 30px;
  color: white;
  text-shadow: 0 0 10px;
}
.right-card {
  width: 400px;
  z-index: 1;
  background-color: white;
}
</style>