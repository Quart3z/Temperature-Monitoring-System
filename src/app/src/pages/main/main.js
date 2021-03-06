import Vue from 'vue'
import App from './App.vue'

// Css
import "bootstrap-icons/font/bootstrap-icons.css"
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap"
import "../../css/main.css"


Vue.config.productionTip = false


new Vue({
  render: h => h(App),
}).$mount('#app')
  