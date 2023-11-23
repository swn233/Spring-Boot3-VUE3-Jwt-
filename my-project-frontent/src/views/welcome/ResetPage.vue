<script setup>
import {ref, reactive, computed} from "vue";
import {Lock, Message} from "@element-plus/icons-vue";
import {get,post} from "@/net";
import {ElMessage} from "element-plus";
import router from "@/router";

const active = ref(0)
const coldTime=ref(0)
const formRef=ref()

const form =reactive({
  email:'',
  code:'',
  password:'',
  password_repeat:''
})

const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入密码'));
  } else if (value!==form.password) {
    callback(new Error('两次输入的密码不一致'));
  }
  else {
    callback();
  }
};

const rule={
  password:[
    {required:true,message:'请输入密码',trigger:'blur'},
    {min:6,max:20,message:'密码长度在6-20之间',trigger:['blur','change']},
  ],
  password_repeat:[
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

function askCode() {
  if (isEmailValid) {
    coldTime.value=60
    get(`/api/auth/ask-code?email=${form.email}&type=register`, () => {
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

const isEmailValid = computed(() => {
  return /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$/.test(form.email)
})

function confirmReset(){
  formRef.value.validate((valid)=>{
    if(valid){
      post('/api/auth/reset-confirm',{
        email:form.email,
        code:form.code
      },()=>active.value++)
    }
  })

}


function doReset(){
  formRef.value.validate((valid)=>{
    if(valid){
      post('/api/auth/reset-password',{...form},()=> {
        ElMessage.success('重置成功,请重新登录')
        router.push('/')
      })
    }
  })
}

</script>

<template>

<div style="text-align: center">
  <div style="margin-top:30px"  >
    <el-steps :active="active" align-center>
      <el-step title="验证电子邮箱"></el-step>
      <el-step title="重新设定密码"></el-step>
    </el-steps>
  </div>
  <div style="margin-top: 80px" v-if="active===0">
    <div style="margin-top: 80px ;font-weight: bold ;font-size: 25px">重置密码</div>
    <div style="font-size: 14px;color:grey">请输入需要重置密码的电子邮件地址</div>
    <div style="margin-top: 50px">
      <el-form :model="form" :rules="rule" ref="formRef">
        <el-form-item prop="email">
          <el-input v-model="form.email" type="email" placeholder="电子邮件地址">
            <template #prefix>
              <el-icon> <Message /> </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-row :gutter="10" style="width: 100%">
            <el-col :span="17">
              <el-input v-model="form.code" maxlength="10" type="password" placeholder="验证码">
                <template #prefix>
                  <el-icon> <Message /> </el-icon>
                </template>
              </el-input>
            </el-col>
            <el-col :span="5">
              <el-button @click="askCode" :disabled="!isEmailValid||coldTime" type="success" >
                {{coldTime > 0 ? `请稍后${coldTime}秒`: '获取验证码'}}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </div>
    <div style="margin-top: 80px">
      <el-button @click="confirmReset" style="width: 270px" type="warning" plain>
        开始重置密码
      </el-button>
    </div>
  </div>
  <div style="margin:20px" v-if="active===1">
    <div style="font-size: 25px;font-weight: bold">
      重置密码
    </div>
    <div style="font-size: 14px;color: grey">请填写你的新密码，务必牢记，防止丢失</div>
    <div style="margin-top: 50px">
      <el-form :model="form" >
        <el-form-item prop="password">
          <el-input v-model="form.password" maxlength="10" type="password" placeholder="密码">
            <template #prefix>
              <el-icon> <Lock /> </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password_repeat">
          <el-input v-model="form.password_repeat" maxlength="30" type="password" placeholder="重复密码">
            <template #prefix>
              <el-icon> <Lock /> </el-icon>
            </template>
          </el-input>
        </el-form-item>
      </el-form>
      <el-button @click="doReset" style="width: 270px" type="danger" plain>
        立即重置密码
      </el-button>
  </div>
  </div>
</div>
</template>
<style scoped>

</style>