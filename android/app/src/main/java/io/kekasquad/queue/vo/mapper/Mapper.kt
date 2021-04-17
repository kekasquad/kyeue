package io.kekasquad.queue.vo.mapper

interface Mapper<IA, R> {
    fun fromInappToRemote(data: IA): R
    fun fromRemoteToInapp(data: R): IA
}