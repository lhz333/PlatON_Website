import Vue from 'vue';
import { Message } from 'element-ui';

//动态获取本地环境
const Base = window.location.href.split("//")[0]+ '//' + window.location.host;
const BaseHost = window.location.href.split("//")[0]+ '//' + window.location.hostname;
export default {
    seeurl:'https://platon.network',
    UPLOAD_URL: Base+'/platon/',
    GETIMG_URL: BaseHost+'/',
    // UPLOAD_URL: 'http://192.168.18.71:8081/platon/',
    // GETIMG_URL: 'http://192.168.18.71:81/',
    DOWN_BASE_URL:Base+'/platon/',

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
    },
    formatterTimestamp: function(timestamp) {
        if(timestamp !== null) {
            let date = new Date(timestamp);
            return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + this.prefixIntrger(date.getHours(),2) + ":" + this.prefixIntrger(date.getMinutes(),2) + ":" + this.prefixIntrger(date.getSeconds(),2);
        } else {
            return "";
        }
    },
    formatterTimestampT: function(timestamp) {
        if(timestamp !== null) {
            let date = new Date(timestamp);
            return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
        } else {
            return "";
        }
    },
    formatterTimeToMinutes:function(timestamp){
        if(timestamp !== null) {
            let date = new Date(timestamp);
            return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + "  " + this.prefixIntrger(date.getHours(),2) + ":" + this.prefixIntrger(date.getMinutes(),2);
        } else {
            return "";
        }
    },
    formatterTimesDate: function(timestamp) {
        if(timestamp !== null) {
            let date = new Date(timestamp);
            return date.getFullYear() + " / " + (date.getMonth() + 1);
        } else {
            return "";
        }
	},
	formatterTimesDates: function(timestamp) {
        if(timestamp !== null) {
            let date = new Date(timestamp);
            return date.getDate();
        } else {
            return "";
        }
    },
    prefixIntrger:function(num,length){
        return (Array(length).join('0')+num).slice(-length);
    }
}