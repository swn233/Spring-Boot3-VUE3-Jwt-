<script setup>
import {onMounted,ref}from 'vue'
import {login}from '../../net/index'
import router from "@/router"

const form=ref({
  username:'visitor',
  password:'visitor',
  remember:false,
})

function guestLogin(){
  login(form.value.username,form.value.password,form.value.remember,()=>{router.push('/index')})
}

onMounted(() => {
  guestLogin()
})
</script>

<template>
  <div></div>
</template>

<style scoped>

</style>