package io.kekasquad.kyeue.vo.mapper

interface Mapper<IA, R> {
    fun fromInappToRemote(data: IA): R
    fun fromRemoteToInapp(data: R): IA
}