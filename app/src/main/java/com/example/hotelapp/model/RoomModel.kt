package com.example.hotelapp.model

class RoomModel {
    var id: Int?= null
    var size: String?= null
    var style: String?= null
    var note: String?= null
    var status: String?= null
    var price: Float ?= null
    constructor()
    constructor(id: Int, size: String, style: String, note: String, status: String, price: Float){
        this.id = id
        this.size = size
        this.style = style
        this.note = note
        this.status = status
        this.price = price
    }
}