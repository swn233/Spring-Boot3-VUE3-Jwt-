import axios from 'axios'
import 'element-plus/es/components/message/style/css'
import {ElMessage} from "element-plus";

export function unauthorized() {
    return !takeAccessToken()
}


const authItemName="access_token"
const roleName="Role"
const defaultError=(err)=>{
    console.log(err)
    ElMessage.warning('发生了一些错误，请联系管理员')
}
const defaultFailure=(message,code,url)=>{
    console.warn(`请求地址：${url},状态码：${code},错误信息：${message}`)
    ElMessage.warning(message)
}

//@data:表单提交的json格式的数据
//@header:由于采用jwt校验方案，因此请求头中要携带token
function internalPost(url, data, header, success, failure, error = defaultError) {
    // 使用axios进行POST请求
    axios
        .post(url, data, { headers: header })
        .then(({ data }) => {
            // 如果响应状态码为200（成功）
            if (data.code === 200) {
                // 调用成功回调函数，并传入响应数据
                success(data.data);
            } else {
                // 调用失败回调函数，并传入错误消息、响应状态码和请求URL
                failure(data.message, data.code, url);
            }
        })
        .catch(err => error(err));
}

function internalGet(url,header,success,failure,error=defaultError){
    axios.get(url,{headers:header})
        .then(({data})=>{
            if(data.code===200){
                success(data.data)
            }else{
                failure(data.message,data.code,data.url)
            }
        }).catch(err=>error(err))
}

function storeAccessToken(token, remember, expire) {
    const authObj={
        token:token,
        expire:expire,
    }
    const str=JSON.stringify(authObj)
    if(remember){
        localStorage.setItem(authItemName,authObj.token)
    }
    else {
        sessionStorage.setItem(authItemName,str)
    }
}

function deleteAccessToken() {
    localStorage.removeItem(authItemName)
    sessionStorage.removeItem(authItemName)
}

function takeAccessToken(){
    const str=localStorage.getItem(authItemName)||sessionStorage.getItem(authItemName)
    if(!str)return null
    const authObj=JSON.parse(str)
    if(authObj.expire<=new Date()){
        deleteAccessToken()
        ElMessage.warning('登陆状态已过期，请重新登陆')
        return null
    }
    return authObj.token
}

function storeRole(role){
    let str=sessionStorage.getItem(roleName)
    if (str)deleteRole()
    str=JSON.stringify(role)
    sessionStorage.setItem(roleName,str)
}

function takeRole(){
    const str=sessionStorage.getItem(roleName)
    if(!str)return null
    return str
}

function deleteRole(){
    sessionStorage.removeItem(roleName)
}
function login(username,password,remember,success,failure=defaultFailure){
    internalPost('/api/auth/login',{
        username:username,
        password:password,
    },{
        'Content-Type':'application/x-www-form-urlencoded'
    },(data)=>{
        storeAccessToken(data.token,remember,data.expire)
        ElMessage.success(`登陆成功，欢迎${data.username}来到湿物男的自留地`)
        storeRole(data.role)
        success(data)
    },failure)
}

function post(url,data,success,failure=defaultFailure){
    internalPost(url,data,accessHeader(),success,failure)
}
function accessHeader() {
    const token=takeAccessToken()
    return token?{Authorization:`Bearer ${token}`}:{}
}

function get(url,success,failure=defaultFailure){
    internalGet(url,accessHeader(),success,failure)
}

function logout(success,failure=defaultFailure){
    get("/api/auth/logout",()=>{
        deleteAccessToken()
        ElMessage.success("退出登录成功，欢迎您再次使用")
        success()
    },failure)
}

export  {login,logout,get,post,accessHeader,takeRole}