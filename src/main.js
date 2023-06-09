import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import { vueAxios } from '@baloise/vue-axios'

const app = createApp(App)

app.use(vueAxios);

app.mount('#app')
