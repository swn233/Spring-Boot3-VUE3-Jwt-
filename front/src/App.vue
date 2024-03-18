<script setup>
import {Transition} from "vue";
</script>

<template>

  <div style="overflow: hidden">
    <router-view v-slot="{ Component }">
      <Transition name="el-fade-in-linear"  mode="out-in">
        <component :is="Component"></component>
      </Transition>
    </router-view>
  </div>

</template>

<style scoped>
body{
  overflow: hidden;
  margin: 0;
}
</style>