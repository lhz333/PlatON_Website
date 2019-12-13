let CONFIG = {
//错误处理
  handleErr:(err)=>{
    console.log(err.toString());
    console.log(`err = ${err}`);
    console.log(`err.config = ${err.config}`);
    console.log(`err.code = ${err.code}`);
    console.log(`err.response = ${err.response}`);
    // if(typeof err.response =='undefined'){
    //   alert('网络异常')
    //   return;
    // }
    // if(err.response.status.toString().startsWith(4)){
    //   alert('资源不存在');
    //   return;
    // }
    // if(err.response.status.toString().startsWith(5)){
    //   alert('服务器出错');
    //   return;
    // }
    // alert('未知错误');
  }
}

export default CONFIG;
