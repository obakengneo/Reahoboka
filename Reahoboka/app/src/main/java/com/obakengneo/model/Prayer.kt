package com.obakengneo.model

data class Prayer(var language:String, var prayer:String, var id:Int, var name:String){

    public constructor() : this("","",0,"")
}