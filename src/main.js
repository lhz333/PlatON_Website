// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import VueResource from 'vue-resource'

/* ReferenceError: “Promise”未定义 */
import 'babel-polyfill';

import Cookie from './util/cookie'

import VueQuillEditor from 'vue-quill-editor'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
import ImageResize from 'quill-image-resize-module'
//
Quill.register('modules/imageResize', ImageResize)
Vue.use(VueQuillEditor)

Vue.use(ElementUI);
Vue.use(VueResource);

import ApiService from './service/API-service';
Vue.prototype.ApiService = ApiService;



Vue.config.productionTip = false

/*全局路由拦截器逻辑*/
router.beforeEach((to, from, next) => {
    let userInfo = {};
    if (Cookie.get('userInfo')) {
        userInfo = JSON.parse(Cookie.get('userInfo'));
    }
    if (typeof userInfo.loginName != 'undefined') {
        next();
    } else {
        if (to.path == '/login') {
            next();
        } else {
            next('/login');
        }
    }
});

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
