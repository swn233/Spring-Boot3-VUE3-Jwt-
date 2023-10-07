<script setup>
import {reactive} from "vue";
import router from "@/router";
import {Lock, User, Message} from "@element-plus/icons-vue";
const form=reactive(({
  username: '',
  password:'',
  password_repeat:'',
  email:'',
  code:''
}))
const rule={

}
</script>

<template>
<div style="text-align: center;margin:0 20px">
  <div style="margin-top: 100px">
    <div style="font-size: 25px;font-weight: bold">注册新用户</div>
    <div style="font-size: 14px;color:grey">欢迎注册我们的学习平台，请在下方填写相关信息</div>
  </div>
  <div style="margin-top:50px">
    <el-form :model="form" :rules="rule">
      <el-form-item>
        <el-input v-model="form.username" maxlength="10" type="text" placeholder="用户名">
          <template #prefix>
            <el-icon> <User /> </el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="form.password" maxlength="10" type="password" placeholder="密码">
          <template #prefix>
            <el-icon> <Lock /> </el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="form.password_repeat" maxlength="10" type="password" placeholder="重复密码">
          <template #prefix>
            <el-icon> <Lock /> </el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="form.email" maxlength="10" type="text" placeholder="电子邮件地址">
          <template #prefix>
            <el-icon> <Message /> </el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-row :gutter="10" style="width: 100%">
          <el-col :span="17">
            <el-input v-model="form.code" maxlength="10" type="password" placeholder="验证码">
              <template #prefix>
                <el-icon> <Message /> </el-icon>
              </template>
            </el-input>
          </el-col>
          <el-col :span="5">
      <el-button type="success" >获取验证码</el-button>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>
    <div style="">
      <el-button style="width: 270px" type="warning" plain>
        立即注册
      </el-button>
    </div>
    <div style="margin-top: 20px">
      <span style="font-size: 14px;color: grey;line-height: 15px">已有账号?</span>
      <el-link style="translate:0 -1px" @click="router.push('/')">立即登录</el-link>
    </div>
  </div>

</div>
</template>

<style scoped>

</style>