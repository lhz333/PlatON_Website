import Vue from 'vue';
import { Message } from 'element-ui';
import { MessageBox } from 'element-ui';
import { Notification } from 'element-ui';
import { Loading } from 'element-ui';
//import axios from 'axios's
const BASE_URL = 'http://192.168.20.99:8080/'

export default {
	post: function(url, params, success) {
		if(arguments.length === 0) {
			return;
		}
		if(arguments.length === 2) {
			if(typeof arguments[1] === 'function') {
				params = new Object();
				success = arguments[1];
			}
		}
		if(params === undefined || params === null) {
			params = new Object();
		}

		Vue.http.interceptors.push((request, next) => {
			next((response) => {
				return response
			})
		});
		let loadingInstance = Loading.service();
		Vue.http.post(BASE_URL + url, JSON.stringify(params)).then(
			(response) => {
				loadingInstance.close();
				if((typeof response.body.ret) !==undefined) {
					success(response.body);
				} else {
					if("string" === (typeof response.body)) {
						let v = JSON.parse(response.body);
//						Notification.warning(v.message);
					} else {
//						Notification.warning(response.body.message);
					}
				}
			},
			(response) => {
			    loadingInstance.close();
				Message.error('System error！');
			}
		);
	},
	get: function(url, success) {
		Vue.http.interceptors.push((request, next) => {
			next((response) => {
				return response
			})
		});
		let loadingInstance = Loading.service();
		Vue.http.get(BASE_URL + url).then(
			(response) => {
				loadingInstance.close();
				if((typeof response.body.ret) !==undefined) {
					success(response.body);
				} else {
					if("string" === (typeof response.body)) {
						let v = JSON.parse(response.body);
						Notification.warning(v.message);
					} else {
						Notification.warning(response.body.message);
					}
				}
			},
			(response) => {
				loadingInstance.close();
				Message.error('系统异常！');
			}
		);
	},
	trace: function(obj) {
		console.log(JSON.stringify(obj, null, 4));
	},
	message: function(message, type) {
		if(arguments.length === 0) {
			return;
		}
		if(arguments.length === 1) {
			Message({
				showClose: true,
				message: message
			});
			return;
		}
		Message({
			showClose: true,
			message: message,
			type: type
		});
	}
}