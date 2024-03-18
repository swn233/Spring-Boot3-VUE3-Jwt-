import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    AutoImport({
      resolvers: [ElementPlusResolver()],
    }),
    Components({
      resolvers: [ElementPlusResolver()],
    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
    // devServer: {
    //   host:'0.0.0.0',
    //   port:'8080',
    //   // https:false,
    //   open:true,
    //   //以上的ip和端口是我们本机的;下面为需要跨域的
    //   proxy: {
    //     '/api/auth/upload': {
    //       target:'https://sm.ms/api/v2/upload',//这里填入你要请求的接口的前缀
    //       // ws:true,//代理websocked
    //       changeOrigin:true,//虚拟的站点需要更管origin
    //       secure: true,                   //是否https接口
    //       pathRewrite:{
    //         '^/api/auth/upload':''//重写路径
    //       }
    //     }
    //   }
    // },
    // lintOnSave:false
})
