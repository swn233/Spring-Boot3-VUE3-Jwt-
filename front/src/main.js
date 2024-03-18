import router from '@/router'
import { createApp } from 'vue'
import App from './App.vue'
import 'element-plus/theme-chalk/dark/css-vars.css'
import axios from "axios";
axios.defaults.baseURL="http://43.142.101.58:8080"
const app=createApp(App)
app.use(router)
app.mount('#app')