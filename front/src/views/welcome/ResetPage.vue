<script setup>
import {Lock, User,Message} from "@element-plus/icons-vue";
import router from "@/router";
import {ref,computed}from 'vue'
import {ElMessage} from "element-plus";
import {get,post}from '../../net/index'
const active=ref(0)
const form=ref({
  password:'',
  repeatPassword:'',
  email:'',
  code:''
})

const formRef=ref()
const coldTime=ref(0)
const validateUsername = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入用户名'));
  } else if (!/^[a-zA-Z0-9_]{3,16}$/.test(value)) {
    callback(new Error('用户名不能包含特殊字符'));
  }
  else {
    callback();
  }
}

const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入密码'));
  } else if (value!==form.value.password) {
    callback(new Error('两次输入的密码不一致'));
  }
  else {
    callback();
  }
}

const rule={
  password:[
    {required:true,message:'请输入密码',trigger:'blur'},
    {min:6,max:20,message:'密码长度在6-20之间',trigger:['blur','change']},
  ],
  repeatPassword:[
    {validator:validatePassword,trigger:['blur','change']},
    {min:6,max:20,message:'密码长度在6-20之间',trigger:['blur','change']},
  ],
  code:[
    {required:true,message:'请输入验证码',trigger:'blur'},
    {min:6,max:6,message:'验证码长度为6',trigger:'blur'},
  ],
  email:[
    {required:true,message:'请输入邮箱地址',trigger:'blur'},
    {type:'email',message:'请输入正确的邮箱地址',trigger:['blur','change']},
  ]
}

const isEmailValid = computed(() => {
  return /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$/.test(form.value.email)
})

function askCode(){
  if (isEmailValid) {
    coldTime.value=60
    get(`/api/auth/ask-code?email=${form.value.email}&type=register`, () => {
      ElMessage.success('验证码发送成功')
      setInterval(()=>coldTime.value--,1000)
    }, () => {
      ElMessage.error('验证码发送失败')
    })

  }
  else
  {
    ElMessage.error('请输入正确的邮箱地址')
  }
}

function confirmReset(){
  formRef.value.validate((valid)=>{
    if(valid){
      post('/api/auth/reset-confirm',{
        email:form.value.email,
        code:form.value.code
      },()=>active.value++)
    }
  })
}

function doReset(){
  formRef.value.validate((valid)=>{
      if (valid){
        post('/api/auth/reset-password',{
          email:form.value.email,
          code:form.value.code,
          password:form.value.password
        },()=>{
          ElMessage.success('重置成功，请重新登录')
          router.push('/welcome')
        })
      }
  })
}

</script>

<template>
<div style="text-align: center">
  <div style="margin-top:30px"  >
    <el-steps :active="active" align-center finish-status="success">
      <el-step title="验证电子邮箱"></el-step>
      <el-step title="重新设定密码"></el-step>
    </el-steps>
  </div>

<!--  step page1 -->
  <div v-if="active===0">
  <div style="margin-top: 100px" >
    <div style="font-size: 28px; font-weight: bold">重置密码</div>
    <div style="font-size: 14px; color: grey">请输入需要重置密码的电子邮箱地址</div>
  </div>
  <div style="margin-top: 50px">
    <el-form :model="form" :rules="rule" ref="formRef">
    <el-form-item prop="email">
      <el-input style="margin:0 20px;" v-model="form.email" placeholder="电子邮箱地址" type="text">
        <template #prefix>
          <el-icon><Message /></el-icon>
        </template>
      </el-input>
    </el-form-item>

    <el-form-item prop="code">
      <el-row>
        <el-col :span="16" style="text-align: left">
          <el-input  style="margin:0 20px;"  v-model="form.code" placeholder="验证码" type="text">
            <template #prefix>
              <el-icon><Message /></el-icon>
            </template>
          </el-input>
        </el-col>
        <el-col :span="8" >
          <el-button :disabled="!isEmailValid||coldTime" @click="askCode" plain style="margin-left: 25px" type="success">
            {{coldTime > 0 ? `请稍后${coldTime}秒`: '获取验证码'}}
          </el-button>
        </el-col>
      </el-row>
    </el-form-item>
    </el-form>
  </div>
  <div style="margin-top: 50px" >
    <el-button @click="active++" type="warning" plain>开始重置密码</el-button>
  </div>
  </div>

  <!--  step page2 -->
  <div style="margin-top: 25px" v-if="active===1">
    <div>
      <div style="font-size: 28px; font-weight: bold">重置密码</div>
      <div style="font-size: 14px;color:grey">请填写你的新密码，务必牢记，防止丢失</div>
    </div>
    <div style="margin-top: 50px">
      <el-form :model="form" :rules="rule" ref="formRef">
        <el-form-item prop="password">
          <el-input style="margin:0 20px;" v-model="form.password" placeholder="密码" type="password">
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="repeatPassword">
              <el-input  style="margin:0 20px;"  v-model="form.repeatPassword" placeholder="重复密码" type="password">
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
        </el-form-item>
      </el-form>

      <el-button @click="doReset" style="margin-top: 25px;width:200px" type="danger" plain>立即重置密码</el-button>
    </div>

  </div>



</div>
</template>

<style scoped>

</style>