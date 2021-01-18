package com.ndk.server.model.response

class CommonResponse{
    var data:Any?=null
    var message:String="SUCCESS"
    var code=0
    constructor(data:Any){
        this.data = data
    }
    constructor(message:String, code:Int){
        this.message = message
        this.code = code
    }
}